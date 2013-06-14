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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.LeagueRepository;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.PlayerRepository;
import com.aravind.avl.domain.Team;
import com.aravind.avl.domain.TeamRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping ("/registration")
@SessionAttributes ({ "teamName", "designatedCaptain", "playingEight", "matchedPlayers"})
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
		return "/registration/start";
	}

	@Transactional
	@RequestMapping (value = "/start", method = RequestMethod.POST)
	public String hasParticipatedEarlier(@RequestParam boolean participatedInEarlierLeague, Model model)
	{
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
		}
		return "/registration/leagues";
	}

	@RequestMapping (value = "/selectTeam", method = RequestMethod.POST)
	public String selectTeam(@RequestParam Long selectedTeam, Model model)
	{
		String q = "START t=node({teamId}) MATCH player-[:PLAYED_WITH_TEAM]->t-[:CONTESTED_IN]->league WITH player AS player, league.startDate AS startDate, league.name AS leagueName ORDER BY startDate RETURN player,  collect(leagueName) AS leagueNames";

		LOG.debug("Participated as team {} in previous league", selectedTeam);
		Map<String, Object> params = Maps.newHashMap();
		params.put("teamId", selectedTeam);

		Result<Map<String, Object>> result = template.query(q, params);
		final List<Player> players = new ArrayList<Player>();

		result.handle(new Handler<Map<String, Object>>()
		{
			@Override
			public void handle(Map<String, Object> value)
			{
				LOG.debug("Team participated in the following leagues {}", value);
				// They cypher just returns a NodeProxy. You need to use convert to make it a domain entity
				Player player = template.convert(value.get("player"), Player.class);
				System.err.println(value.get("leagueNames"));// returns List<String>
				players.add(player);
			}
		});
		Team team = teamRepo.findOne(selectedTeam);
		model.addAttribute("teamName", team.getName());
		model.addAttribute("players", players);
		return "registration/players";
	}

	@RequestMapping (value = "/newTeam", method = RequestMethod.POST)
	public String newdTeam(@RequestParam String newTeamName, @RequestParam ("players") List<String> ids,
			@RequestParam ("isCaptain") String captainName, @RequestParam ("newPlayer1") String newPlayer1,
			@RequestParam ("newPlayer2") String newPlayer2, @RequestParam ("newPlayer3") String newPlayer3,
			@RequestParam ("newPlayer4") String newPlayer4, @RequestParam ("newPlayer5") String newPlayer5,
			@RequestParam ("newPlayer6") String newPlayer6, @RequestParam ("newPlayer7") String newPlayer7,
			@RequestParam ("newPlayer8") String newPlayer8, Model model)
	{
		LOG.debug("Selected players from existing list: {}", ids);
		LOG.debug("Selected Captain: {}", captainName);
		LOG.debug("New player: {}", newPlayer1);

		List<Player> playingEight = Lists.newArrayList();
		List<String> newPlayers = Lists.newArrayList();

		if (StringUtils.isNotBlank(newTeamName))
		{
			LOG.debug("New name provided for the team: {}", newTeamName);
			model.addAttribute("teamName", newTeamName.replaceAll(newTeamName, " "));
			Team existingTeam = teamRepo.findByName(newTeamName);
			LOG.error("Team with the name {} already exists: {}", newTeamName, existingTeam);
		}
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

		// handle new players
		Map<String, Collection<Player>> matchedPlayers = Maps.newTreeMap();

		for (String np: newPlayers)
		{
			Collection<Player> matches = tryNameMatch(np);
			if (matches.isEmpty())
			{
				addNewPlayer(np, captainName, playingEight);
			}
			else
			{
				matchedPlayers.put(np, matches);
			}
		}

		// handle existing players
		addExistingPlayers(ids, captainName, playingEight);

		model.addAttribute("newTeamName", newTeamName);
		model.addAttribute("designatedCaptain", captainName);
		model.addAttribute("matchedPlayers", matchedPlayers);
		model.addAttribute("playingEight", playingEight);

		return "registration/confirmation";
	}

	private void addExistingPlayers(List<String> ids, String isCaptain, List<Player> playingEight)
	{
		for (String id: ids)
		{
			Player p = playerRepo.findOne(Long.valueOf(id));
			LOG.debug("Retrieved {}", p);

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
			LOG.debug("[{}] already participated in earlier leagues. Exact match by First Name.", newPlayer);
			for (Player pl: players)
			{
				LOG.debug("Matched player(s): {}", pl);
				System.err.println(pl.getPlayedWith());
			}
			return players;
		}

		result = playerRepo.findAllByQuery("lastName", p.getLastName());
		players = IteratorUtil.asCollection(result);
		if (players.size() > 0)
		{
			LOG.debug("[{}] already participated in earlier leagues. Exact match by Last Name.", newPlayer);
			for (Player pl: players)
			{
				LOG.debug("Matched player(s): {}", pl);
				System.err.println(pl.getPlayedWith());
			}
			return players;
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
			LOG.debug("[{}] already participated in earlier leagues.  Partial match {} on full name.", newPlayer, first3Characters);
			for (Player pl: players)
			{
				LOG.debug("Matched player(s): {}", pl);
				System.err.println(pl.getPlayedWith());
			}
			return players;
		}
		return Collections.emptyList();
	}

	/**
	 * @param teamName binding from @SessionAttribute
	 * @param designatedCaptain binding from @SessionAttribute
	 * @param playingEight binding from @SessionAttribute
	 * @param model
	 * @return
	 */
	@RequestMapping (value = "/end", method = RequestMethod.POST)
	public String end(@ModelAttribute ("teamName") String teamName, @ModelAttribute ("designatedCaptain") String designatedCaptain,
			@RequestParam ("players") List<String> ids, @ModelAttribute ("playingEight") List<Player> playingEight,
			@ModelAttribute ("matchedPlayers") TreeMap<String, Collection<Player>> matchedPlayers, Model model)
	{
		LOG.debug("Team Name: {}", teamName);
		LOG.debug("Captain Name: {}", designatedCaptain);

		Set<Player> pls = new TreeSet<Player>(Player.NAME_CASE_INSENSITIVE_COMPARATOR);

		for (String id: ids)
		{
			for (Map.Entry<String, Collection<Player>> cp: matchedPlayers.entrySet())
			{
				for (Player p: cp.getValue())
				{
					if (p.getNodeId().toString().equals(id))
					{
						LOG.debug("Adding {} to playing eight.", p.getName());
						pls.add(p);
					}
				}
			}
		}
		pls.addAll(playingEight);
		model.addAttribute("playingEight", Lists.newArrayList(pls));
		Team team = teamRepo.findByName(teamName);
		if (team == null)
		{
			team = new Team();
			team.setName(teamName);
			team.setPlayers(pls);

			for (Player p: team.getPlayers())
			{
				// TODO find league and derive start date
				// p.playedWith(team, during, inLeague)
			}
			teamRepo.save(team);
		}
		else
		{

		}

		LOG.debug("Final Team {}", pls);

		return "registration/success";
	}
}
