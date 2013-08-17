package com.aravind.avl.domain;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface LeagueRepository extends GraphRepository<League>, LeagueRepositoryExtension
{
	@Query ("START n=node:League('name:*') RETURN n ORDER BY n.startDate? DESC LIMIT 1")
	League findCurrentLeague();

	League findByName(String leagueName);

	@Query ("START league=node:League(name={0}), level=node:Level(name={1}), pool=node:Pool(name={2}) MATCH league-[:LEVEL|NEXT*]->level-[:POOL]->pool-[:FIXTURE]->matches RETURN matches")
	List<Match> findMatches(String leagueName, String levelName, String poolName);

	@Query ("START league=node:League(name={0}), level=node:Level(name={1}), pool=node:Pool(name={2}) MATCH league-[:LEVEL|NEXT*]->level-[:POOL]->pool RETURN pool")
	Pool findPool(String leagueName, String levelName, String poolName);
}