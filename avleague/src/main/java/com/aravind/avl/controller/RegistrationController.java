package com.aravind.avl.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
@SessionAttributes ({ "newTeamName", "designatedCaptain", "playingEight"})
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
		String q = "START t=node({teamId}) MATCH player-[:PLAYED_WITH_TEAM]->t-[:CONTESTED_IN]->league WITH ID(player) AS playerId, player.name AS playerName, league.startDate AS startDate, league.name AS leagueName ORDER BY startDate RETURN playerId, playerName, collect(leagueName) AS leagueNames";

		LOG.debug("Participated as team {} in previous league", selectedTeam);
		Map<String, Object> params = Maps.newHashMap();
		params.put("teamId", selectedTeam);

		Result<Map<String, Object>> result = template.query(q, params);
		final List<PlayerView> players = new ArrayList<PlayerView>();

		result.handle(new Handler<Map<String, Object>>()
		{
			@Override
			public void handle(Map<String, Object> value)
			{
				LOG.debug("Team participated in the following leagues {}", value);
				PlayerView pv = new PlayerView();
				pv.setName((String) value.get("playerName"));
				pv.setParticipatedInLeagueNames((List<String>) value.get("leagueNames"));
				pv.setPlayerId(((Long) value.get("playerId")).toString());
				players.add(pv);
			}

		});
		Team team = teamRepo.findOne(selectedTeam);
		model.addAttribute("teamName", team.getName());
		model.addAttribute("players", players);
		return "registration/players";
	}

	@RequestMapping (value = "/newTeam", method = RequestMethod.POST)
	public String newdTeam(@RequestParam String newTeamName, @RequestParam ("players") List<String> ids,
			@RequestParam String isCaptain, @RequestParam ("newPlayer1") String newPlayer1,
			@RequestParam ("newPlayer2") String newPlayer2, @RequestParam ("newPlayer3") String newPlayer3,
			@RequestParam ("newPlayer4") String newPlayer4, @RequestParam ("newPlayer5") String newPlayer5,
			@RequestParam ("newPlayer6") String newPlayer6, @RequestParam ("newPlayer7") String newPlayer7,
			@RequestParam ("newPlayer8") String newPlayer8, Model model)
	{
		LOG.debug("New name provided for the team: {}", newTeamName);
		LOG.debug("Selected players from existing list: {}", ids);
		LOG.debug("Selected Captain: {}", isCaptain);
		LOG.debug("New player: {}", newPlayer1);

		List<PlayerView> playingEight = Lists.newArrayList();
		addNewPlayer(newPlayer1, isCaptain, playingEight);
		addNewPlayer(newPlayer2, isCaptain, playingEight);
		addNewPlayer(newPlayer3, isCaptain, playingEight);
		addNewPlayer(newPlayer4, isCaptain, playingEight);
		addNewPlayer(newPlayer5, isCaptain, playingEight);
		addNewPlayer(newPlayer6, isCaptain, playingEight);
		addNewPlayer(newPlayer7, isCaptain, playingEight);
		addNewPlayer(newPlayer8, isCaptain, playingEight);

		addExistingPlayers(ids, isCaptain, playingEight);

		model.addAttribute("newTeamName", newTeamName);
		model.addAttribute("designatedCaptain", isCaptain);
		model.addAttribute("playingEight", playingEight);

		return "registration/confirmation";
	}

	private void addExistingPlayers(List<String> ids, String isCaptain, List<PlayerView> playingEight)
	{
		for (String id: ids)
		{
			Player p = playerRepo.findOne(Long.valueOf(id));
			LOG.debug("Retrieved {}", p);

			PlayerView pv = new PlayerView();
			pv.setName(p.getName());
			pv.setPlayerId(p.getNodeId().toString());

			if (pv.getName().equalsIgnoreCase(isCaptain))
			{
				pv.setCaptain(true);
				LOG.debug("Marking [{}] as captain", pv.getName());
			}
			playingEight.add(pv);
		}
	}

	private void addNewPlayer(String newPlayer, String isCaptain, List<PlayerView> playingEight)
	{
		PlayerView newPlyr = new PlayerView();
		newPlyr.setName(newPlayer);

		if (newPlyr.getName().equalsIgnoreCase(isCaptain))
		{
			newPlyr.setCaptain(true);
			LOG.debug("Marking [{}] as captain", newPlyr.getName());
		}

		playingEight.add(newPlyr);
	}

	/**
	 * @param newTeamName binding from @SessionAttribute
	 * @param designatedCaptain binding from @SessionAttribute
	 * @param playingEight binding from @SessionAttribute
	 * @param model
	 * @return
	 */
	@RequestMapping (value = "/end", method = RequestMethod.POST)
	public String end(@ModelAttribute ("newTeamName") String newTeamName,
			@ModelAttribute ("designatedCaptain") String designatedCaptain,
			@ModelAttribute ("playingEight") List<PlayerView> playingEight, Model model)
	{
		LOG.debug("Team Name: {}", newTeamName);
		LOG.debug("Captain Name: {}", designatedCaptain);
		LOG.debug("Team {}", playingEight);

		return "registration/success";
	}
}
