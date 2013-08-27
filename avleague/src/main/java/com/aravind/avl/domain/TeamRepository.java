package com.aravind.avl.domain;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

public interface TeamRepository extends GraphRepository<Team>, NamedIndexRepository<Team>, TeamRepositoryExtension
{
	Team findByName(String name);

	@Query ("START team=node:TeamName(name={0}) MATCH team-[:CONTESTED_IN]->league RETURN league.name")
	List<String> findLeaguesContestedIn(String teamName);

	@Query ("START t=node({0}), l=node({1}) MATCH p-[:PLAYED_FOR_IN_LEAGUE]->hyperEdge-[:PLAYED_FOR]->t, hyperEdge-[:IN_LEAGUE]->l RETURN p")
	List<Player> findPlayers(Long teamId, Long leagueId);
}
