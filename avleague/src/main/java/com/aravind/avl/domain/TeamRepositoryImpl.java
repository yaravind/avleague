package com.aravind.avl.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.helpers.collection.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Handler;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class TeamRepositoryImpl implements TeamRepositoryExtension
{
	@Autowired
	private Neo4jTemplate template;

	@Autowired
	private TeamRepository teamRepo;

	@Override
	public List<League> findLeaguesLevelsAndPools(String teamName)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("teamName", teamName);

		Result<Map<String, Object>> result = template
				.query("START team=node:Team(name={teamName}) MATCH team-[:CONTESTED_IN]->league-[:LEVEL|NEXT*]->level-[:POOL]->pool-[:TEAM]->team RETURN league.name, level.name, pool.name",
						params);

		final Multimap<String, Level> temp = ArrayListMultimap.create();

		result.handle(new Handler<Map<String, Object>>()
		{
			@Override
			public void handle(Map<String, Object> row)
			{
				// They cypher just returns a NodeProxy. You need to use convert to make it a domain entity
				// Level level = template.convert(row.get("levels"), Level.class);
				// levels.add(level);
				System.err.println(row.get("league.name"));
				System.err.println(row.get("level.name"));
				System.err.println(row.get("pool.name"));
				System.err.println("-----");

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
}
