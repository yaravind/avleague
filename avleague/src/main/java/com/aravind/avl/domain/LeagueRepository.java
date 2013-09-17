package com.aravind.avl.domain;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface LeagueRepository extends GraphRepository<League>// , LeagueRepositoryExtension
{
	@Query ("START n=node:League('name:*') RETURN n ORDER BY n.startDate? DESC LIMIT 1")
	League findCurrentLeague();

	League findByName(String leagueName);

	@Query ("START league=node({0}) MATCH p=league-[r:LEVEL|NEXT*]->(level) RETURN last(nodes(p)) as levels")
	List<Level> findAllLevels(Long id);

	@Query ("START league=node:League(name={0}), level=node:Level(name={1}), pool=node:Pool(name={2}) MATCH league-[:LEVEL|NEXT*]->level-[:POOL]->pool-[:FIXTURE]->matches RETURN matches")
	List<Match> findMatches(String leagueName, String levelName, String poolName);

	@Query ("START league=node:League(name={0}), level=node:Level(name={1}), pool=node:Pool(name={2}) MATCH league-[:LEVEL|NEXT*]->level-[:POOL]->pool RETURN pool")
	Pool findPool(String leagueName, String levelName, String poolName);

	@Query ("START league=node:League(name={0}), level=node:Level(name={1}), pool=node:Pool(name={2}) MATCH league-[:LEVEL|NEXT*]->level-[:POOL]->pool-[:FIXTURE]->matches WHERE matches.name ={3} RETURN matches")
	Match findMatch(String leagueName, String levelName, String poolName, String matchName);

	@Query ("START l=node:League(name={0}) MATCH t-[:CONTESTED_IN]->l RETURN DISTINCT t.name AS teamName ORDER BY teamName")
	List<String> findTeams(String leagueName);

	@Query ("START league=node:League(name={0}), level=node:Level(name={1}), p=node:Pool(name={2}) MATCH team-[:CONTESTED_IN]->league WHERE NOT(team<-[:TEAM]-p<-[:POOL]-level<-[:LEVEL|NEXT*]-league) RETURN team ORDER BY team.name ASC")
	List<Team> findTeamsNotInPool(String leagueName, String levelName, String poolName);

	@Query ("START league=node:League(name='Sri Bala Bharathi 2012 September League'), level=node:Level(name='Playoffs') MATCH team-[:CONTESTED_IN]->league WHERE NOT(team<-[:TEAM]-()<-[:POOL]-level<-[:LEVEL|NEXT*]-league) RETURN team ORDER BY team.name ASC")
	List<Team> findTeamsNotInAnyPool(String leagueName, String levelName);

}