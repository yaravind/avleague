package com.aravind.avl.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.neo4j.helpers.collection.IteratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Handler;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.LeagueRepository;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.PlayerRepository;
import com.aravind.avl.domain.PlayerTeamLeague;
import com.aravind.avl.domain.Team;
import com.aravind.avl.domain.TeamRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping ("/registration")
@SessionAttributes ({ "teamName", "designatedCaptain", "playerList", "matchedPlayers", "participatedInEarlierLeague"})
public class RegistrationController
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private LeagueRepository leagueRepo;

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private Neo4jTemplate template;

	@RequestMapping (method = RequestMethod.GET)
	public String get()
	{
		return "registration/start";
	}

	@Transactional
	@RequestMapping (value = "/start", method = RequestMethod.POST)
	public String hasParticipatedEarlier(@RequestParam boolean participatedInEarlierLeague, Model model)
	{
		model.addAttribute("participatedInEarlierLeague", participatedInEarlierLeague);

		if (participatedInEarlierLeague)
		{
			LOG.debug("Participated in earlier leagues. Retrieving the past league teams.");
			Iterable<League> allLeagues = leagueRepo.findAll();
			Set<League> sortedLeagues = new TreeSet<League>();
			for (League l: allLeagues)
			{
				sortedLeagues.add(l);
			}
			LOG.debug("Past leagues sorted by start date {}", sortedLeagues);
			model.addAttribute("pastLeagues", sortedLeagues);
		}
		else
		{
			LOG.debug("Did not participate in earlier leagues. Redirecting to register the new one.");
			return "/registration/players";
		}
		return "/registration/leagues";
	}

	@Transactional
	@RequestMapping (value = "/selectTeam", method = RequestMethod.POST)
	public String selectTeam(@RequestParam Long selectedTeam, @RequestParam boolean participatedInEarlierLeague, Model model)
	{
		String q = "START t=node({teamId}) MATCH player-[:PLAYED_WITH_TEAM]->t-[:CONTESTED_IN]->league WITH player AS player, league.startDate AS startDate, league.name AS leagueName ORDER BY startDate RETURN player,  collect(leagueName) AS allParticipatedLeagues";

		LOG.debug("Participated as team {} in previous league", selectedTeam);
		Map<String, Object> params = Maps.newHashMap();
		params.put("teamId", selectedTeam);

		Result<Map<String, Object>> result = template.query(q, params);

		final List<Player> players = new ArrayList<Player>();

		result.handle(new Handler<Map<String, Object>>()
		{
			@Override
			public void handle(Map<String, Object> row)
			{
				// They cypher just returns a NodeProxy. You need to use convert to make it a domain entity
				Player player = template.convert(row.get("player"), Player.class);

				// row.get("allParticipatedLeagues")) returns List<String>
				LOG.debug("All leagues the {} had participated in: {}", player, row.get("allParticipatedLeagues"));

				players.add(player);
			}
		});
		Team team = teamRepo.findOne(selectedTeam);
		model.addAttribute("teamName", team.getName());
		model.addAttribute("players", players);
		model.addAttribute("participatedInEarlierLeague", participatedInEarlierLeague);

		return "registration/players";
	}

	@Transactional
	@RequestMapping (value = "/newTeam", method = RequestMethod.POST)
	public String newTeam(@RequestParam String teamName, @RequestParam String newTeamName,
			@RequestParam boolean participatedInEarlierLeague,
			@RequestParam (value = "players", required = false) List<String> ids, @RequestParam ("isCaptain") String captainName,
			@RequestParam ("newPlayer1") String newPlayer1, @RequestParam ("newPlayer2") String newPlayer2,
			@RequestParam ("newPlayer3") String newPlayer3, @RequestParam ("newPlayer4") String newPlayer4,
			@RequestParam ("newPlayer5") String newPlayer5, @RequestParam ("newPlayer6") String newPlayer6,
			@RequestParam ("newPlayer7") String newPlayer7, @RequestParam ("newPlayer8") String newPlayer8, Model model)
	{
		LOG.debug("newTeamName: [{}]", newTeamName);
		LOG.debug("teamName: [{}]", teamName);
		LOG.debug("participatedInEarlierLeague: [{}]", participatedInEarlierLeague);
		LOG.debug("captainName: [{}]", captainName);

		List<Player> playerList = Lists.newArrayList();

		if (!participatedInEarlierLeague)
		{
			if (isTeamNameAlreadyInUse(newTeamName))
			{
				return "registration/players";
			}
			model.addAttribute("teamName", newTeamName);
		}
		else
		{
			if (StringUtils.isNotBlank(newTeamName))
			{
				LOG.debug("New name provided for the team: {}", newTeamName);

				if (isTeamNameAlreadyInUse(newTeamName))
				{
					return "registration/players";
				}

				model.addAttribute("renamedFromTeamName", teamName);
				model.addAttribute("teamName", newTeamName);
			}
		}

		List<String> newPlayers = newPlayers(newPlayer1, newPlayer2, newPlayer3, newPlayer4, newPlayer5, newPlayer6, newPlayer7,
				newPlayer8);

		// handle new players
		Map<String, Collection<Player>> matchedPlayers = Maps.newTreeMap();

		for (String np: newPlayers)
		{
			Collection<Player> matchList = tryNameMatch(np);
			if (matchList.isEmpty())
			{
				LOG.debug("No match found for name {}", np);
				addNewPlayer(np, captainName, playerList);
			}
			else
			{
				matchedPlayers.put(np, matchList);
			}
		}

		// handle existing players
		if (!CollectionUtils.isEmpty(ids))
		{
			addExistingPlayers(ids, captainName, playerList);
		}

		model.addAttribute("designatedCaptain", captainName);
		model.addAttribute("matchedPlayers", matchedPlayers);
		model.addAttribute("playerList", playerList);

		return "registration/confirmation";
	}

	private boolean isTeamNameAlreadyInUse(String newTeamName)
	{
		Team existingTeam = teamRepo.findByName(newTeamName);
		if (existingTeam != null)
		{
			LOG.error("Team with the new name {} already exists {}", newTeamName, existingTeam);
			return true;
		}
		return false;
	}

	/**
	 * @param teamName binding from @SessionAttribute
	 * @param designatedCaptain binding from @SessionAttribute
	 * @param playerList binding from @SessionAttribute
	 * @param matchedPlayers binding from @SessionAttribute
	 */
	@Transactional
	@RequestMapping (value = "/end", method = RequestMethod.POST)
	public String end(@RequestParam (required = false) String renamedFromTeamName, @RequestParam String teamName,
			@RequestParam boolean participatedInEarlierLeague, @ModelAttribute ("designatedCaptain") String designatedCaptain,
			@RequestParam List<String> playerIds, @ModelAttribute List<Player> playerList,
			@ModelAttribute TreeMap<String, Collection<Player>> matchedPlayers, Model model)
	{
		LOG.debug("renamedFromTeamName: [{}]", renamedFromTeamName);
		LOG.debug("teamName: [{}]", teamName);
		LOG.debug("participatedInEarlierLeague: [{}]", participatedInEarlierLeague);
		LOG.debug("designatedCaptain: [{}]", designatedCaptain);

		Set<Player> pls = new TreeSet<Player>(Player.NAME_CASE_INSENSITIVE_COMPARATOR);

		// override playing eight if necessary
		for (String id: playerIds)
		{
			for (Map.Entry<String, Collection<Player>> cp: matchedPlayers.entrySet())
			{
				for (Player p: cp.getValue())
				{
					if (p.getNodeId().toString().equals(id))
					{
						LOG.debug("Adding EXISTING [{}] to playing eight.", p.getName());
						pls.add(p);
					}
				}
			}
		}
		pls.addAll(playerList);
		model.addAttribute("playerList", Lists.newArrayList(pls));

		League currentLeague = leagueRepo.findCurrentLeague();

		Team team = teamRepo.findByName(teamName);

		if (team == null)
		{
			// new team
			team = new Team();
			team.setName(teamName);

			LOG.debug("{} hasn't participated in the earlier leagues, so create new Team and relate to the current league {}",
					team, currentLeague);
			teamRepo.save(team);

			for (Player p: pls)
			{
				// is new player?
				if (p.getNodeId() == null)
				{
					LOG.debug("Before saving new {}", p);
					playerRepo.save(p);
					LOG.debug("After saving new {}", p);
				}
				createPlayedForInLeagueRelation(currentLeague, team, p);
			}
		}
		else
		{
			LOG.debug("{} participated in the earlier leagues, so just has to relate the existing Team to the current league {}",
					team, currentLeague);

			for (Player p: pls)
			{
				LOG.debug("Before saving new {}", p);
				playerRepo.save(p);
				LOG.debug("After saving new {}", p);
				createPlayedForInLeagueRelation(currentLeague, team, p);
			}
		}

		if (participatedInEarlierLeague && StringUtils.isNotBlank(renamedFromTeamName))
		{
			Team previouslyKnownAs = teamRepo.findByName(renamedFromTeamName);
			team.setPreviouslyKnownAs(previouslyKnownAs);
		}

		currentLeague.addTeam(team);
		teamRepo.save(team);

		LOG.debug("Before saving final Team {}", team);
		leagueRepo.save(currentLeague);
		LOG.debug("After saving final Team {}", team);

		return "registration/success";
	}

	private PlayerTeamLeague createPlayedForInLeagueRelation(League currentLeague, Team team, Player p)
	{
		// Map<String, Object> params = createParams(player);
		//
		// printRawQueryEngine(params);
		// Result<Map<String, Object>> result = template.query(
		// "start p=node({id}) match p-[r:PLAYED_WITH_TEAM]->team return r.during, id(r) order by r.during asc",
		// params);
		// result.handle(new Handler<Map<String, Object>>()
		// {
		// @Override
		// public void handle(Map<String, Object> row)
		// {
		// System.out.println("BEFORE---------------------------------------------------------");
		// LOG.debug("{}", row.get("r.during") + " - " + row.get("id(r)"));
		// }
		// });

		PlayerTeamLeague playedForInleague = p.playedForInLeague(team, currentLeague.getStartDate(), currentLeague);
		playedForInleague.setAsCaptain(p.isCaptain());

		team.addPlayer(p);
		template.save(p);

		return playedForInleague;
	}

	private Map<String, Object> createParams(Player player)
	{
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", player.getNodeId());
		return params;
	}

	private List<String> newPlayers(String newPlayer1, String newPlayer2, String newPlayer3, String newPlayer4, String newPlayer5,
			String newPlayer6, String newPlayer7, String newPlayer8)
	{
		List<String> newPlayers = Lists.newArrayList();
		if (StringUtils.isNotBlank(newPlayer1))
		{
			newPlayers.add(newPlayer1);
		}
		if (StringUtils.isNotBlank(newPlayer2))
		{
			newPlayers.add(newPlayer2);
		}
		if (StringUtils.isNotBlank(newPlayer3))
		{
			newPlayers.add(newPlayer3);
		}
		if (StringUtils.isNotBlank(newPlayer4))
		{
			newPlayers.add(newPlayer4);
		}
		if (StringUtils.isNotBlank(newPlayer5))
		{
			newPlayers.add(newPlayer5);
		}
		if (StringUtils.isNotBlank(newPlayer6))
		{
			newPlayers.add(newPlayer6);
		}
		if (StringUtils.isNotBlank(newPlayer7))
		{
			newPlayers.add(newPlayer7);
		}
		if (StringUtils.isNotBlank(newPlayer8))
		{
			newPlayers.add(newPlayer8);
		}
		return newPlayers;
	}

	private void addExistingPlayers(List<String> ids, String isCaptain, List<Player> playingEight)
	{
		for (String id: ids)
		{
			Player p = playerRepo.findOne(Long.valueOf(id));
			LOG.debug("Adding EXISTING [{}] to potential final list.", p);

			if (p.getName().equalsIgnoreCase(isCaptain))
			{
				p.setCaptain(true);
				LOG.debug("Marking [{}] as captain", p.getName());
			}
			playingEight.add(p);
		}
	}

	private void addNewPlayer(String newPlayer, String isCaptain, List<Player> playingEight)
	{
		Player newPlyr = new Player();
		newPlyr.setName(newPlayer);

		if (newPlyr.getName().equalsIgnoreCase(isCaptain))
		{
			LOG.debug("Adding NEW [{}] to potential final list.", newPlyr);
			newPlyr.setCaptain(true);
			LOG.debug("Marking [{}] as captain", newPlyr.getName());
		}

		playingEight.add(newPlyr);
	}

	private Collection<Player> tryNameMatch(String newPlayer)
	{
		LOG.debug("Tring to find match for [{}]", newPlayer);
		Player p = new Player();
		p.setName(newPlayer);

		Iterable<Player> result = playerRepo.findAllByQuery("firstName", p.getFirstName());
		Collection<Player> players = IteratorUtil.asCollection(result);
		if (players.size() > 0)
		{
			LOG.debug("Found exact match by First Name: [{}]", players);
			return players;
		}
		if (p.getLastName() != null)
		{
			LOG.debug("Ignoring exact match by Last Name as it is not available for : {}", p);
			result = playerRepo.findAllByQuery("lastName", p.getLastName());
			players = IteratorUtil.asCollection(result);
			if (players.size() > 0)
			{
				LOG.debug("Found exact match by Last Name: [{}]", players);
				return players;
			}
		}
		String first3Characters = p.getName().substring(0, 3);
		String searchString = first3Characters;
		if (first3Characters.contains(" "))
		{
			searchString = first3Characters.substring(0, first3Characters.indexOf(" "));
		}
		result = playerRepo.findByNameLike(searchString + "*");
		players = IteratorUtil.asCollection(result);
		if (players.size() > 0)
		{
			LOG.debug("Found partial match (Search string: {}) on Full Name: [{}]", searchString, players);
			return players;
		}
		return Collections.emptyList();
	}
}
