package com.aravind.avl.controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.Team;
import com.aravind.avl.domain.TeamRepository;
import com.aravind.avl.service.LeaguePopulator;
import com.google.common.collect.Iterables;

@Controller
public class LeagueController
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private LeaguePopulator populator;

	@Autowired
	private TeamRepository teamRepo;

	@RequestMapping (value = "/admin/populate", method = RequestMethod.GET)
	public String populateDatabase(Model model)
	{
		LOG.debug("Before populating the graph db.");
		Collection<League> leagues = populator.populateDatabase();
		LOG.debug("After populating the graph db.");
		model.addAttribute("leagues", leagues);

		League league = Iterables.getOnlyElement(leagues);
		Long nodeId = Iterables.get(league.getTeams(), 0).getNodeId();
		LOG.debug("Retrieving team with node id as {}", nodeId);
		Team team = teamRepo.findOne(nodeId);
		LOG.debug("Retrieved team {} with node id as {}", team, nodeId);
		model.addAttribute("team", team);

		return "/leagues/list";
	}
}
