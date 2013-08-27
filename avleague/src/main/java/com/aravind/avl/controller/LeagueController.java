package com.aravind.avl.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.neo4j.helpers.collection.IteratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aravind.avl.domain.Court;
import com.aravind.avl.domain.League;
import com.aravind.avl.domain.LeagueRepository;
import com.aravind.avl.domain.Level;
import com.aravind.avl.domain.MatchRepository;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.Pool;
import com.aravind.avl.domain.Team;
import com.aravind.avl.domain.TeamRepository;
import com.aravind.avl.domain.Venue;
import com.aravind.avl.domain.VenueRepository;
import com.google.common.collect.Maps;

@Controller
public class LeagueController
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private LeagueRepository leagueRepo;

	@Autowired
	private MatchRepository matchRepo;

	@Autowired
	private VenueRepository venueRepo;

	@Autowired
	private Neo4jTemplate template;

	@RequestMapping (value = "/leagues/new", method = RequestMethod.GET)
	public String newLeague(Model model)
	{
		League l = new League();
		model.addAttribute("newLeague", l);
		return "/leagues/new";
	}

	@RequestMapping (value = "/leagues", method = RequestMethod.GET)
	public String list(Model model)
	{
		Iterable<League> all = leagueRepo.findAll();
		Collection<League> leagues = IteratorUtil.asCollection(all);
		model.addAttribute("leagues", leagues);

		for (League l: leagues)
		{
			template.fetch(l.getPlayedAt());
			for (Level level: l.getAllLevels())
			{
				template.fetch(level.getPools());
			}
		}

		return "/leagues/list";
	}

	@RequestMapping (value = "/leagues/{leagueName}", method = RequestMethod.GET)
	public String leagues(@PathVariable String leagueName, Model model)
	{
		LOG.debug("Finding details for {}", leagueName);

		League l = leagueRepo.findByName(leagueName);

		System.err.println(l.getPlayedAt());
		template.fetch(l.getPlayedAt());
		System.err.println(l.getPlayedAt());
		for (Level level: l.getAllLevels())
		{
			template.fetch(level.getPools());
		}
		Map<String, List<Player>> teamToPlayers = Maps.newHashMap();
		for (Team t: l.getTeams())
		{
			List<Player> players = teamRepo.findPlayers(t.getNodeId(), l.getNodeId());
			LOG.debug("For Team {} Players {}", t.getNodeId(), players);
			teamToPlayers.put(t.getName(), players);
		}

		model.addAttribute("teamToPlayers", teamToPlayers);
		model.addAttribute("league", l);

		return "/leagues/league";
	}

	@Transactional
	@RequestMapping (value = "/leagues/{leagueName}", method = RequestMethod.DELETE)
	public String delete(@PathVariable String leagueName, Model model)
	{
		LOG.debug("Deleting league with name: {}", leagueName);

		League found = leagueRepo.findByName(leagueName);
		LOG.debug("Deleting league entity: {}", found);
		leagueRepo.delete(found);

		return "redirect:/leagues";
	}

	@Transactional
	@RequestMapping (value = "/leagues/new", method = RequestMethod.POST)
	public String newLeague(@ModelAttribute League newLeague)
	{
		LOG.debug("Creating new league: {}", newLeague);
		// Stores the given entity in the graph, if the entity is already
		// attached to the graph, the node is updated,
		// otherwise a new node is created.
		leagueRepo.save(newLeague);

		return "redirect:" + newLeague.getName();
	}

	@RequestMapping (value = "/leagues/{leagueName}/levels/{levelName}/pools/{poolName}/matchForm", method = RequestMethod.POST)
	public String matchForm(@PathVariable String leagueName, @PathVariable String levelName, @PathVariable String poolName,
			Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		LOG.debug("Found league: {}", l);

		Level level = l.findLevelByName(levelName);

		template.fetch(level.getPools());
		Pool pool = level.findPoolByName(poolName);
		LOG.debug("Retrieved pool {}", pool);

		template.fetch(l.getPlayedAt());
		LOG.debug("Venues {}", l.getPlayedAt());

		Map<String, List<Player>> teamToPlayers = Maps.newHashMap();
		for (Team t: pool.getTeams())
		{
			List<Player> players = teamRepo.findPlayers(t.getNodeId(), l.getNodeId());
			teamToPlayers.put(t.getName(), players);
		}

		model.addAttribute("venues", l.getPlayedAt());
		model.addAttribute("pool", pool);
		model.addAttribute("level", level);
		model.addAttribute("teamToPlayers", teamToPlayers);

		return "/leagues/matches/new";
	}

	@RequestMapping (value = "/leagues/{leagueName}/venues", method = RequestMethod.GET)
	public String venues(@PathVariable ("leagueName") String leagueName, Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		LOG.debug("Before fetch {}", l.getPlayedAt());
		template.fetch(l.getPlayedAt());
		LOG.debug("After fetch {}", l.getPlayedAt());
		model.addAttribute("league", l);

		return "/leagues/newVenue";
	}

	@Transactional
	@RequestMapping (value = "/leagues/{leagueName}/venues", method = RequestMethod.POST)
	public String venues(@RequestParam ("leagueName") String leagueName, @RequestParam ("venueName") String venue,
			@RequestParam ("courtName1") String court1, @RequestParam ("courtName2") String court2,
			@RequestParam ("courtName3") String court3, Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		template.fetch(l.getPlayedAt());
		Venue matchedVenue = null;

		// check if this is an existing venue
		Iterable<Venue> all = venueRepo.findAll();
		if (all.iterator().hasNext())
		{
			Collection<Venue> venues = IteratorUtil.asCollection(all);
			LOG.debug("Venue exisits with the given name {}", all);

			for (Venue v: venues)
			{
				if (v.getName().equalsIgnoreCase(venue))
				{
					matchedVenue = v;
					LOG.debug("Found matching venue {}", v);
				}
			}
		}

		if (matchedVenue != null)
		{
			l.addVenue(matchedVenue);
		}
		else
		{
			l.addVenue(buildVenue(venue, court1, court2, court3));
		}

		LOG.debug("Before linking the venue {}", l.getPlayedAt());
		leagueRepo.save(l);
		LOG.debug("After linking the venue {}", l.getPlayedAt());

		template.fetch(l.getPlayedAt());
		model.addAttribute("league", l);

		return "redirect:/leagues";
	}

	private Venue buildVenue(String venue, String court1, String court2, String court3)
	{
		Venue newVenue = new Venue(venue);
		if (StringUtils.isNotBlank(court1))
		{
			newVenue.addCourt(new Court(court1));
		}
		if (StringUtils.isNotBlank(court2))
		{
			newVenue.addCourt(new Court(court2));
		}
		if (StringUtils.isNotBlank(court3))
		{
			newVenue.addCourt(new Court(court3));
		}
		return newVenue;
	}
}