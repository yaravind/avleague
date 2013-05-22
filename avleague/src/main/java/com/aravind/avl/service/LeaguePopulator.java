package com.aravind.avl.service;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.PlayerRepository;
import com.aravind.avl.domain.Team;
import com.aravind.avl.domain.TeamRepository;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

@Service
public class LeaguePopulator
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private TeamRepository teamRepo;

	@Transactional
	public Collection<League> populateDatabase()
	{
		League l = null;
		try
		{
			URI uri = Resources.getResource("Sri Bala Bharathi 2012 September League_2012-09-08_2012-09-15.properties").toURI();
			LOG.debug("Importing from: {}", uri);
			l = new LeagueFactory().createLeague(new File(uri));
			LOG.debug("Importing league: {}", l);

			for (Team t: l.getTeams())
			{
				for (Player p: t.getPlayers())
				{
					p.playedWith(t, l.getStartDate(), l);
				}

				LOG.debug("Saving team: {}", t);
				teamRepo.save(t);
				LOG.debug("Saved team: {}", t);
			}
			return Lists.newArrayList(l);
		}
		catch (Exception e)
		{
			LOG.error("Error while importing leagues.", e);
			throw new RuntimeException(e);
		}
	}
}
