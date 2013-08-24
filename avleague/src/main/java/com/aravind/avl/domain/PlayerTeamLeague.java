package com.aravind.avl.domain;

import java.util.Date;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class PlayerTeamLeague
{
	@GraphId
	private Long nodeId;

	@RelatedTo (type = "PLAYED_FOR")
	Team team;

	@RelatedTo (type = "IN_LEAGUE")
	private League inLeague;

	@GraphProperty (propertyType = Long.class)
	private Date during;

	@GraphProperty
	private boolean asCaptain;

	public PlayerTeamLeague()
	{}

	public PlayerTeamLeague(Team t, Date d, League l, boolean isCaptain)
	{
		team = t;
		during = d;
		inLeague = l;
		asCaptain = isCaptain;
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

	public boolean isAsCaptain()
	{
		return asCaptain;
	}

	public void setAsCaptain(boolean asCaptain)
	{
		this.asCaptain = asCaptain;
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inLeague == null) ? 0 : inLeague.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		PlayerTeamLeague other = (PlayerTeamLeague) obj;
		if (inLeague == null)
		{
			if (other.inLeague != null)
			{
				return false;
			}
		}
		else if (!inLeague.equals(other.inLeague))
		{
			return false;
		}
		if (nodeId == null)
		{
			if (other.nodeId != null)
			{
				return false;
			}
		}
		else if (!nodeId.equals(other.nodeId))
		{
			return false;
		}
		if (team == null)
		{
			if (other.team != null)
			{
				return false;
			}
		}
		else if (!team.equals(other.team))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "PlayerTeamLeague [nodeId=" + nodeId + ", team=" + team + ", inLeague=" + inLeague + ", during=" + during
				+ ", asCaptain=" + asCaptain + "]";
	}

}
