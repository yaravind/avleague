package com.aravind.avl.domain;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface LeagueRepository extends GraphRepository<League>
{
	@Query ("START n=node(*) WHERE has(n.__type__) and n.__type__='com.aravind.avl.domain.League' RETURN n ORDER BY n.startDate DESC LIMIT 1")
	League findCurrentLeague();
}
