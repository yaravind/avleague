package com.aravind.avl.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aravind.avl.domain.League;
import com.aravind.avl.service.LeagueFactory;
import com.aravind.avl.service.LeaguePopulator;

@Controller
@RequestMapping ("/admin")
public class AdminController
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private LeaguePopulator populator;

	@RequestMapping (value = "/populate", method = RequestMethod.GET)
	public String populateDatabase(Model model)
	{
		LOG.debug("Before populating the graph db.");
		LeagueFactory.ImportProfile profile = new LeagueFactory.ImportProfile();
		profile.teamNameRowNum = 0;
		profile.emailColumnNum = 11;
		profile.phoneColumnNum = 12;
		profile.poolColumnNum = 13;

		Collection<League> june2012 = populator.populateDatabase(
				"Sri Bala Bharathi 2012 June League_2012-06-16_2012-06-17.properties", profile);
		LOG.debug("After populating the graph db.");

		LOG.debug("Before populating the graph db.");
		profile = new LeagueFactory.ImportProfile();
		profile.teamNameRowNum = 0;
		profile.emailColumnNum = 9;
		profile.phoneColumnNum = 10;
		profile.poolColumnNum = 11;

		Collection<League> september2012 = populator.populateDatabase(
				"Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties", profile);
		LOG.debug("After populating the graph db.");

		/*
		 * TODO used for testing the league save. can delete after issue #1 has been tested League league =
		 * Iterables.getOnlyElement(june2012); Long nodeId = Iterables.get(league.getTeams(), 0).getNodeId();
		 * LOG.debug("Retrieving team with node id as {}", nodeId); Team team = teamRepo.findOne(nodeId);
		 * LOG.debug("Retrieved team {} with node id as {}", team, nodeId); model.addAttribute("team", team);
		 */

		List<League> leagues = new ArrayList<League>();
		leagues.addAll(june2012);
		leagues.addAll(september2012);

		model.addAttribute("leagues", leagues);

		return "redirect:/leagues";
	}
}
