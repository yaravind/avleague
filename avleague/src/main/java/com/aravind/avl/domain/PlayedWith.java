package com.aravind.avl.domain;

import java.util.Date;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity (type = "PLAYED_WITH_TEAM")
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

	@GraphProperty
	private Date during;

	@GraphProperty
	private boolean asCaptain;

	public PlayedWith()
	{
		// required by spring data
	}

	public PlayedWith(Player p, Team t, Date d, League l)
	{
		this(p, t, d, l, false);
	}

	public PlayedWith(Player p, Team t, Date d, League l, boolean isCaptain)
	{
		player = p;
		team = t;
		during = d;
		inLeague = l;
		asCaptain = isCaptain;
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

	@Override
	public String toString()
	{
		return "PlayedWith [nodeId=" + nodeId + ", player=" + player + ", team=" + team + ", inLeague=" + inLeague + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((during == null) ? 0 : during.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayedWith other = (PlayedWith) obj;
		if (during == null)
		{
			if (other.during != null)
				return false;
		}
		else if (!during.equals(other.during))
			return false;
		if (nodeId == null)
		{
			if (other.nodeId != null)
				return false;
		}
		else if (!nodeId.equals(other.nodeId))
			return false;
		if (player == null)
		{
			if (other.player != null)
				return false;
		}
		else if (!player.equals(other.player))
			return false;
		if (team == null)
		{
			if (other.team != null)
				return false;
		}
		else if (!team.equals(other.team))
			return false;
		return true;
	}

}
