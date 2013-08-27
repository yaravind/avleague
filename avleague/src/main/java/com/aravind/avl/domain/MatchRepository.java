package com.aravind.avl.domain;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

public interface MatchRepository extends NamedIndexRepository<Match>, GraphRepository<Match>
{}