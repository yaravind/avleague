package com.aravind.avl.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import com.aravind.avl.domain.LevelRepository;
import com.aravind.avl.domain.Match;
import com.aravind.avl.domain.MatchRepository;
import com.aravind.avl.domain.Pool;
import com.aravind.avl.domain.PoolRepository;
import com.aravind.avl.domain.Team;
import com.aravind.avl.domain.TeamRepository;
import com.aravind.avl.domain.Venue;
import com.aravind.avl.domain.VenueRepository;
import com.aravind.avl.service.LeagueFactory;
import com.aravind.avl.service.LeaguePopulator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

@Controller
public class LeagueController
{

	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private LeaguePopulator populator;

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private LeagueRepository leagueRepo;

	@Autowired
	private PoolRepository poolRepo;

	@Autowired
	private MatchRepository matchRepo;

	@Autowired
	private VenueRepository venueRepo;

	@Autowired
	private LevelRepository levelRepo;

	@Autowired
	private Neo4jTemplate template;

	@RequestMapping (value = "/leagues/new", method = RequestMethod.GET)
	public String newLeague(Model model)
	{
		League l = new League();
		model.addAttribute("newLeague", l);
		return "/leagues/new";
	}

	@RequestMapping (value = "/leagues/list", method = RequestMethod.GET)
	public String list(Model model)
	{
		Iterable<League> all = leagueRepo.findAll();
		Collection<League> leagues = IteratorUtil.asCollection(all);
		model.addAttribute("leagues", leagues);

		Map<Long, Iterable<Level>> levelsByLeague = Maps.newHashMap();
		for (League l: leagues)
		{
			template.fetch(l.getPlayedAt());
			levelsByLeague.put(l.getNodeId(), leagueRepo.findAllLevels(l.getNodeId()));
		}
		model.addAttribute("levelsByLeague", levelsByLeague);
		return "/leagues/list";
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

		return "redirect:list";
	}

	@RequestMapping (value = "/leagues/{leagueId}/matches/new", method = RequestMethod.GET)
	public String newMatch(@PathVariable Long leagueId, Model model)
	{
		LOG.debug("Adding an empty Match binding to model");
		Match m = new Match();
		model.addAttribute("newMatch", m);

		Map<Long, String> teams = new LinkedHashMap<Long, String>();

		League league = leagueRepo.findOne(leagueId);
		template.fetch(league.getPlayedAt());
		LOG.debug("Venues {}", league.getPlayedAt());

		for (Team t: league.getTeams())
		{
			teams.put(t.getNodeId(), t.getName());
		}
		model.addAttribute("teamsOfCurrentLeague", teams);

		Map<String, String> levels = new LinkedHashMap<String, String>();
		for (Level l: leagueRepo.findAllLevels(league.getNodeId()))
		{
			levels.put(l.getName(), l.getName());
		}
		model.addAttribute("levels", levels);

		List<String> courts = new ArrayList<String>();
		for (Venue v: league.getPlayedAt())
		{
			for (Court c: v.getCourts())
			{
				String label = v.getName() + " - " + c.getName();
				courts.add(label);
				LOG.debug("Adding court {}", label);
			}
		}

		model.addAttribute("courts", courts);
		return "/leagues/matches/new";
	}

	@Transactional
	@RequestMapping (value = "/leagues/{leagueName}/matches/", method = RequestMethod.GET)
	public String matches(@PathVariable String leagueName, Model model)
	{
		LOG.debug("Finding league by name: [{}]", leagueName);
		League l = leagueRepo.findByName(leagueName);
		LOG.debug("Found league: {}", l);

		LOG.debug("Fetching matches");
		Iterable<Level> levels = leagueRepo.findAllLevels(l.getNodeId());
		for (Level level: levels)
		{
			template.fetch(level.getFixtures());

			LOG.debug("After fetch: Level {} Matches {}", level, level.getFixtures());
		}

		model.addAttribute("league", l);
		model.addAttribute("levels", levels);
		return "/leagues/matches/list";
	}

	@Transactional
	@RequestMapping (value = "/leagues/{leagueId}/matches/new", method = RequestMethod.POST)
	public String newMatch(@PathVariable Long leagueId, @RequestParam ("teamA.nodeId") Long teamA,
			@RequestParam ("teamB.nodeId") Long teamB, @RequestParam ("pool") String pool, @RequestParam ("level") String level,
			@RequestParam ("playedOnCourt") String venueAndCourt, @RequestParam ("time") String time)
	{
		LOG.debug("Creating new match for League id: {}", leagueId);
		LOG.debug("Creating new match between Team A: {}", teamA);
		LOG.debug("and Team B: {}", teamB);
		LOG.debug("in Pool: {}", pool);
		LOG.debug("on Court: {}", venueAndCourt);
		LOG.debug("for Level : {}", level);
		// Stores the given entity in the graph, if the entity is already
		// attached to the graph, the node is updated,
		// otherwise a new node is created.
		League league = leagueRepo.findOne(leagueId);
		LOG.debug("Before fetch {}", league.getPlayedAt());
		template.fetch(league.getPlayedAt());
		LOG.debug("After fetch {}", league.getPlayedAt());

		String venueName = venueAndCourt.split(" - ")[0];
		String courtName = venueAndCourt.split(" - ")[1];

		Venue venueByName = league.findVenueByName(venueName);
		Match match = null;
		if (venueByName == null)
		{
			LOG.error("Couldn't fine venue with name {}", venueName);
			return "redirect:list";
		}
		else
		{
			match = league.conductMatch(teamRepo.findOne(teamA), teamRepo.findOne(teamB), venueByName.findCourtByName(courtName),
					levelRepo.findByName(level));
		}

		Pool p = poolRepo.findByName(pool);
		if (p == null)
		{
			LOG.debug("Didn't find Pool with name {} so creating one", pool);
			p = new Pool(pool);
			poolRepo.save(p);
		}
		match.setPool(p);
		try
		{
			match.setTime(DateUtils.parseDate(time, new String[]{ "MM-dd-yyyy HH:mm"}));
		}
		catch (ParseException e)
		{
			LOG.error("Can't set the time of the match", e);
		}
		matchRepo.save(match);

		LOG.debug("Saved Match {}", match);

		template.fetch(match);

		return "redirect:list";
	}

	@RequestMapping (value = "/admin/populate", method = RequestMethod.GET)
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

		return "/leagues/list";
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

		return "redirect:list";
	}

	@RequestMapping (value = "/leagues/{leagueName}/levels", method = RequestMethod.GET)
	public String levels(@PathVariable ("leagueName") String leagueName, Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		model.addAttribute("league", l);

		model.addAttribute("levels", leagueRepo.findAllLevels(l.getNodeId()));
		return "/leagues/newLevel";
	}

	@Transactional
	@RequestMapping (value = "/leagues/{leagueName}/levels", method = RequestMethod.POST)
	public String levels(@RequestParam ("leagueName") String leagueName, @RequestParam ("levelName") String levelName,
			@RequestParam ("beforeLevel") Long beforeLevelId, Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		Level matchedLevel = null;

		boolean firstLevel = beforeLevelId.equals(Long.valueOf(-1));

		if (firstLevel)
		{
			Level newLevel = new Level(levelName);
			l.setLevel(newLevel);
			LOG.debug("First level for this League. Before saving the Llevel {}", l.getLevel());
			leagueRepo.save(l);
			LOG.debug("After saving the level {}", l.getLevel());
		}
		else
		{
			Iterable<Level> levels = leagueRepo.findAllLevels(l.getNodeId());
			if (!Iterables.isEmpty(levels))
			{
				LOG.debug("Levels exist for the league {}", levels);

				// check if duplicate level
				for (Level lv: levels)
				{
					if (lv.getName().equalsIgnoreCase(levelName))
					{
						matchedLevel = lv;
						LOG.error("Level already exists. Found matching level {}", lv);
						break;
					}
				}
				if (matchedLevel == null)
				{
					Level prevLevel = levelRepo.findOne(beforeLevelId);
					Level newLevel = new Level(levelName);
					if (prevLevel == null)
					{
						LOG.error("{} is not first level but couldn't find the previous level (searched for id {}) to link",
								newLevel, beforeLevelId);
					}
					else
					{
						prevLevel.setNextLevel(newLevel);
						LOG.debug("Saving the next level for {}", prevLevel);
						levelRepo.save(prevLevel);
					}
				}
			}
		}
		model.addAttribute("league", l);
		model.addAttribute("levels", leagueRepo.findAllLevels(l.getNodeId()));
		return "redirect:list";
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