package com.aravind.avl.domain;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface LevelRepository extends GraphRepository<Level>
{
	Level findByName(String name);
}