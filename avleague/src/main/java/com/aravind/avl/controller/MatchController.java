package com.aravind.avl.controller;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.LeagueRepository;
import com.aravind.avl.domain.Level;
import com.aravind.avl.domain.Match;
import com.aravind.avl.domain.MatchRepository;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.PlayerRepository;
import com.aravind.avl.domain.Pool;
import com.aravind.avl.domain.PoolRepository;
import com.aravind.avl.domain.TeamRepository;
import com.aravind.avl.domain.Venue;
import com.google.common.collect.Sets;

@Controller
@RequestMapping ("/leagues/{leagueName}/levels/{levelName}/pools/{poolName}")
public class MatchController
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private LeagueRepository leagueRepo;

	@Autowired
	private MatchRepository matchRepo;

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private PoolRepository poolRepo;

	@Autowired
	private Neo4jTemplate template;

	@Transactional
	@RequestMapping (value = "/matches/{matchName}", method = RequestMethod.GET)
	public String matches(@PathVariable String leagueName, @PathVariable String poolName, @PathVariable String levelName,
			@PathVariable String matchName, Model model)
	{
		LOG.debug("Retrieving match[{}] for {}, {}, {}", new Object[]{ matchName, leagueName, levelName, poolName});

		Match match = leagueRepo.findMatch(leagueName, levelName, poolName, matchName);

		model.addAttribute("league", leagueName);
		model.addAttribute("pool", poolName);
		model.addAttribute("level", levelName);

		template.fetch(match.getWinner());
		template.fetch(match.getMvp());

		LOG.debug("Found {}", match);

		model.addAttribute("match", match);

		return "/leagues/matches/matchDetails";
	}

	@Transactional
	@RequestMapping (value = "/matches", method = RequestMethod.GET)
	public String matches(@PathVariable String leagueName, @PathVariable String poolName, @PathVariable String levelName,
			Model model)
	{
		LOG.debug("Retrieving matches for {}, {}, {}", new Object[]{ leagueName, levelName, poolName});

		Iterable<Match> matches = leagueRepo.findMatches(leagueName, levelName, poolName);
		LOG.debug("Found {}", matches);
		model.addAttribute("leagueName", leagueName);
		model.addAttribute("poolName", poolName);
		model.addAttribute("levelName", levelName);
		model.addAttribute("matches", matches);

		return "/leagues/matches/list";
	}

	@Transactional
	@RequestMapping (value = "/matches", method = RequestMethod.POST)
	public String newMatch(@PathVariable String leagueName, @PathVariable String levelName, @PathVariable String poolName,
			@RequestParam Long teamA, @RequestParam Long teamB, @RequestParam String venueAndCourt, @RequestParam String time,
			@RequestParam List<Long> teamAPlaying6, @RequestParam List<Long> teamBPlaying6)
	{
		LOG.debug("Creating new match for League id: {}", leagueName);
		LOG.debug("Creating new match between Team A: {}", teamA);
		LOG.debug("and Team B: {}", teamB);
		LOG.debug("in Pool: {}", poolName);
		LOG.debug("on Court: {}", venueAndCourt);
		LOG.debug("for Level : {}", levelName);
		LOG.debug("Team A playing 6 : {}", teamAPlaying6);
		LOG.debug("Team B playing 6 : {}", teamBPlaying6);
		// Stores the given entity in the graph, if the entity is already
		// attached to the graph, the node is updated,
		// otherwise a new node is created.
		League league = leagueRepo.findByName(leagueName);
		LOG.debug("Before fetch {}", league.getPlayedAt());
		template.fetch(league.getPlayedAt());
		LOG.debug("After fetch {}", league.getPlayedAt());

		Level level = league.findLevelByName(levelName);
		template.fetch(level.getPools());
		Pool pool = level.findPoolByName(poolName);
		template.fetch(pool.getTeams());

		Match match = null;
		String venueName = venueAndCourt.split(" - ")[0];
		String courtName = venueAndCourt.split(" - ")[1];
		Venue venueByName = league.findVenueByName(venueName);
		if (venueByName == null)
		{
			LOG.error("Couldn't find venue with name {}", venueName);
			return "redirect:list";
		}
		else
		{
			Date at = null;

			try
			{
				at = DateUtils.parseDate(time, new String[]{ "MM-dd-yyyy HH:mm"});
			}
			catch (ParseException e)
			{
				LOG.error("Can't set the time of the match", e);
			}

			match = league.conductMatch(teamRepo.findOne(teamA), teamRepo.findOne(teamB), venueByName.findCourtByName(courtName),
					league.findLevelByName(levelName), pool, at);

			// Team A playing 6
			Iterable<Player> playing6 = playerRepo.findAll(teamAPlaying6);
			Collection<Player> asCollection = IteratorUtil.asCollection(playing6);
			LOG.debug("Team A playing 6 {}", asCollection);
			match.setTeamAPlaying6(Sets.newHashSet(asCollection));

			// Team B playing 6
			playing6 = playerRepo.findAll(teamBPlaying6);
			asCollection = IteratorUtil.asCollection(playing6);
			LOG.debug("Team B playing 6 {}", asCollection);
			match.setTeamBPlaying6(Sets.newHashSet(asCollection));
		}

		LOG.debug("Saving {}", match);
		matchRepo.save(match);
		LOG.debug("After save {}", match);

		LOG.debug("Saving {}", pool);
		poolRepo.save(pool);

		return "redirect:" + String.format("/leagues/%s/levels/%s/pools/%s", leagueName, levelName, poolName);
	}

	@Transactional
	@RequestMapping (value = "/matches/{matchName}", method = RequestMethod.POST)
	public String matchResult(@PathVariable String leagueName, @PathVariable String levelName, @PathVariable String poolName,
			@PathVariable String matchName, @RequestParam (required = false) Long winner,
			@RequestParam (required = false) Integer teamAScore, @RequestParam (required = false) Integer teamBScore,
			@RequestParam Long mvp, @RequestParam (required = false) String comments,
			@RequestParam (required = false) String subtitutions)
	{
		LOG.debug("Outcome winner: {}", winner);
		LOG.debug("Outcome mvp: {}", mvp);
		LOG.debug("Outcome comments: {}", comments);
		LOG.debug("Outcome subtitutions: {}", subtitutions);

		LOG.debug("Retrieving match[{}] for {}, {}, {}", new Object[]{ matchName, leagueName, levelName, poolName});

		Match match = leagueRepo.findMatch(leagueName, levelName, poolName, matchName);
		LOG.debug("Found {}", match);

		if (winner != null)
		{
			if (match.getTeamA().getNodeId().equals(winner))
			{
				match.setWinner(match.getTeamA());
				match.setLoser(match.getTeamB());
			}
			else if (match.getTeamB().getNodeId().equals(winner))
			{
				match.setWinner(match.getTeamB());
				match.setLoser(match.getTeamA());
			}
			else
			{
				LOG.error("Unable to find the winning team with id [{}]. Team A [{}], Team B [{}]",
						new Object[]{ winner, match.getTeamA(), match.getTeamB()});
			}
		}
		else
		{
			match.setTeamAScore(teamAScore);
			match.setTeamBScore(teamBScore);
		}
		match.setMvp(playerRepo.findOne(mvp));
		if (StringUtils.isNotBlank(comments))
		{
			match.setComments(comments.trim());
		}
		matchRepo.save(match);
		return "redirect:" + String.format("/leagues/%s/levels/%s/pools/%s", leagueName, levelName, poolName);
	}
}
