package com.aravind.avl.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Team
{
	@GraphId
	private Long nodeId;

	@GraphProperty
	@Indexed(unique = true)
	private String name;

	@RelatedTo(type = "PLAYED_WITH_TEAM", direction = Direction.INCOMING)
	private Set<Player> players = new HashSet<Player>();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public Iterable<Player> getPlayers()
	{
		return players;
	}
}
