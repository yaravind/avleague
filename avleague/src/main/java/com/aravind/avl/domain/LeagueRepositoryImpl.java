package com.aravind.avl.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Handler;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;

public class LeagueRepositoryImpl implements LeagueRepositoryExtension
{
	@Autowired
	private Neo4jTemplate template;

	@Override
	public Iterable<Level> findAllLevels(Long id)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);

		Result<Map<String, Object>> result = template.query(
				"START league=node({id}) MATCH p=league-[r:LEVEL|NEXT*]->(level) RETURN last(nodes(p)) as levels", params);

		final List<Level> levels = new ArrayList<Level>();

		result.handle(new Handler<Map<String, Object>>()
		{
			@Override
			public void handle(Map<String, Object> row)
			{
				// They cypher just returns a NodeProxy. You need to use convert to make it a domain entity
				Level level = template.convert(row.get("levels"), Level.class);
				levels.add(level);
			}
		});

		return levels;
	}
}
