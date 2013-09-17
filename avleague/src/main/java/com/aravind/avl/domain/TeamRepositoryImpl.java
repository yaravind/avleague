package com.aravind.avl.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.neo4j.helpers.collection.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Handler;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import static org.neo4j.helpers.collection.MapUtil.map;

public class TeamRepositoryImpl implements TeamRepositoryExtension
{
	private transient final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private Neo4jTemplate template;

	@Autowired
	private TeamRepository teamRepo;

	@Override
	public List<League> findLeaguesLevelsAndPools(String teamName)
	{
		Result<Map<String, Object>> result = template
				.query("START team=node:Team(name={teamName}) MATCH team-[:CONTESTED_IN]->league-[:LEVEL|NEXT*]->level-[:POOL]->pool-[:TEAM]->team RETURN league.name, level.name, pool.name",
						map("teamName", teamName));

		final Multimap<String, Level> temp = ArrayListMultimap.create();

		result.handle(new Handler<Map<String, Object>>()
		{
			@Override
			public void handle(Map<String, Object> row)
			{
				Level lev = new Level();
				lev.setName((String) row.get("level.name"));
				temp.put((String) row.get("league.name"), lev);
			}
		});
		List<League> leagues = Lists.newArrayList();

		for (Map.Entry<String, Collection<Level>> entry: temp.asMap().entrySet())
		{
			League league = new League();
			league.setName(entry.getKey());
			for (Level lev: entry.getValue())
			{
				List<Level> allLevels = league.getAllLevels();
				if (allLevels.isEmpty())
				{
					league.setLevel(lev);
				}
				else
				{
					Iterables.last(allLevels).setNextLevel(lev);
				}
			}
			leagues.add(league);
		}
		return leagues;
	}

	@Override
	public List<Player> findPlayers(String teamName)
	{
		String q = "START t=node:TeamName(name={teamName}) MATCH player-[:PLAYED_WITH_TEAM]->t-[:CONTESTED_IN]->league WITH player AS player, league.startDate AS startDate, league.name AS leagueName ORDER BY startDate RETURN player, collect(leagueName) AS allParticipatedLeagues";

		Result<Map<String, Object>> result = template.query(q, map("teamName", teamName));

		final List<Player> players = new ArrayList<Player>();

		result.handle(new Handler<Map<String, Object>>()
		{
			@Override
			public void handle(Map<String, Object> row)
			{
				// They cypher just returns a NodeProxy. You need to use convert to make it a domain entity
				Player player = template.convert(row.get("player"), Player.class);

				// TODO has to return all the leagues that this player has played row.get("allParticipatedLeagues"))
				// returns List<String>
				LOG.debug("All leagues the {} had participated in: {}", player, row.get("allParticipatedLeagues"));

				players.add(player);
			}
		});

		return players;
	}
}