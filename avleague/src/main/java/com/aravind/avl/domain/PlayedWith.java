package com.aravind.avl.domain;

import java.util.Date;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "PLAYED_WITH_TEAM")
public class PlayedWith
{
	@GraphId
	private Long nodeId;

	@StartNode
	Player player;

	@EndNode
	Team team;

	// TODO transient for now
	private transient League inLeague;

	@GraphProperty(propertyType = Long.class)
	private Date during;

	public PlayedWith()
	{
		// required by spring data
	}

	public PlayedWith(Player p, Team t, Date d, League l)
	{
		player = p;
		team = t;
		during = d;
		inLeague = l;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Team getTeam()
	{
		return team;
	}

	public void setTeam(Team team)
	{
		this.team = team;
	}

	public League getInLeague()
	{
		return inLeague;
	}

	public void setInLeague(League inLeague)
	{
		this.inLeague = inLeague;
	}

	public Date getDuring()
	{
		return during;
	}

	public void setDuring(Date during)
	{
		this.during = during;
	}

	public Long getNodeId()
	{
		return nodeId;
	}
}
