package com.aravind.avl.domain;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface PoolRepository extends GraphRepository<Pool>
{
	Pool findByName(String name);
}
