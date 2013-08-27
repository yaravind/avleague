package com.aravind.avl.controller;

import java.util.Collection;
import java.util.List;

import org.neo4j.helpers.collection.IteratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.PlayerRepository;
import com.aravind.avl.domain.PlayerTeamLeague;
import com.aravind.avl.domain.Team;
import com.aravind.avl.domain.TeamRepository;

@Controller
public class TeamController
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	Neo4jTemplate template;

	@RequestMapping (value = "/teams", method = RequestMethod.GET)
	public String teams(Model model)
	{
		Iterable<Team> result = teamRepo.findAll();

		Collection<Team> teams = IteratorUtil.asCollection(result);
		LOG.debug("Found {} teams", teams.size());

		model.addAttribute("teams", teams);
		return "leagues/teams/list";
	}

	@RequestMapping (value = "/teams/{teamName}", method = RequestMethod.GET)
	public String team(@PathVariable String teamName, Model model)
	{
		// try exact match
		Team team = teamRepo.findByName(teamName);

		// didnt find with exact match, use case-insensitive search
		if (team == null)
		{
			LOG.debug("Didn't find with exact match, using case-insensitive search");
			EndResult<Team> teams = teamRepo.findAllByQuery("TeamName", "name", teamName);
			team = teams.single();
		}
		LOG.debug("Finding team with name {}: {}", teamName, team);

		// template.fetch(team.getAliases());

		List<String> leaguesContestedIn = teamRepo.findLeaguesContestedIn(teamName);
		LOG.debug("Leagues contested in {}", leaguesContestedIn);

		List<League> leaguesWithLevels = teamRepo.findLeaguesLevelsAndPools(teamName);
		LOG.debug("Leagues and Levels {}", leaguesWithLevels);

		for (Player p: team.getPlayers())
		{
			for (PlayerTeamLeague ptl: p.getPlayedforInLeague())
			{
				template.fetch(ptl.getInLeague());
			}
		}
		model.addAttribute("team", team);
		model.addAttribute("leaguesContestedIn", leaguesContestedIn);
		model.addAttribute("leagues", leaguesWithLevels);

		return "leagues/teams/teamDetails";
	}
}