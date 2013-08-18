package com.aravind.avl.controller;

import java.util.Collection;

import org.neo4j.helpers.collection.IteratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aravind.avl.domain.PlayerRepository;
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

	@RequestMapping (value = "/teams", method = RequestMethod.GET)
	public String allTeams(Model model)
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
		Team team = teamRepo.findByName(teamName);
		LOG.debug("Finding team with name {}: {}", teamName, team);

		model.addAttribute("team", team);
		return "leagues/teams/teamDetails";
	}
}