package com.aravind.avl.controller;

import java.util.List;

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
import com.aravind.avl.domain.LevelRepository;
import com.aravind.avl.domain.Pool;
import com.aravind.avl.domain.TeamRepository;
import com.google.common.collect.Iterables;

@Controller
@RequestMapping ("/leagues/{leagueName}")
public class LevelController
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private LeagueRepository leagueRepo;

	@Autowired
	private LevelRepository levelRepo;

	@Autowired
	private TeamRepository teamRepo;

	@Autowired
	private Neo4jTemplate template;

	@RequestMapping (value = "/levelForm", method = RequestMethod.GET)
	public String levelForm(@PathVariable ("leagueName") String leagueName, Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		LOG.debug("Levels {}", l.getAllLevels());
		model.addAttribute("league", l);

		return "/leagues/newLevel";
	}

	@RequestMapping (value = "/levels", method = RequestMethod.GET)
	public String levels(@PathVariable ("leagueName") String leagueName, Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		LOG.debug("Levels {}", l.getAllLevels());
		for (Level lev: l.getAllLevels())
		{
			template.fetch(lev.getPools());
		}
		model.addAttribute("league", l);

		return "/leagues/levels/list";
	}

	@Transactional
	@RequestMapping (value = "/levels", method = RequestMethod.POST)
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
		return "redirect:/leagues";
	}

	@RequestMapping (value = "/levels/{levelName}/pools", method = RequestMethod.GET)
	public String pools(@PathVariable ("leagueName") String leagueName, @PathVariable ("levelName") String levelName, Model model)
	{
		League l = leagueRepo.findByName(leagueName);

		model.addAttribute("league", l);
		Level level = l.findLevelByName(levelName);
		template.fetch(level.getPools());
		LOG.debug("Found level {} with name", level, levelName);
		model.addAttribute("level", level);
		return "/leagues/newPool";
	}

	@RequestMapping (value = "/levels/{levelName}/pools/{poolName}", method = RequestMethod.GET)
	public String pools(@PathVariable String leagueName, @PathVariable String levelName, @PathVariable String poolName, Model model)
	{
		Pool p = leagueRepo.findPool(leagueName, levelName, poolName);

		LOG.debug("Found pool with name {}: {}", poolName, p);

		template.fetch(p.getFixtures());

		model.addAttribute("pool", p);

		model.addAttribute("leagueName", leagueName);
		model.addAttribute("poolName", p.getName());
		model.addAttribute("levelName", levelName);
		model.addAttribute("matches", p.getFixtures());

		return "/leagues/pool";
	}

	@Transactional
	@RequestMapping (value = "/levels/{levelName}/pools", method = RequestMethod.POST)
	public String pools(@PathVariable ("leagueName") String leagueName, @PathVariable ("levelName") String levelName,
			@RequestParam String poolName, @RequestParam List<Long> teams, Model model)
	{
		League l = leagueRepo.findByName(leagueName);
		Level level = l.findLevelByName(levelName);
		LOG.debug("Found {} with name", level, levelName);

		Pool p = new Pool(poolName);
		for (Long id: teams)
		{
			p.addTeam(teamRepo.findOne(id));
		}
		level.addPool(p);
		LOG.debug("Before saving level with pool {}", level);
		levelRepo.save(level);
		LOG.debug("After saving level with pool {}", level);

		model.addAttribute("league", l);

		return "redirect:/leagues";
	}
}
