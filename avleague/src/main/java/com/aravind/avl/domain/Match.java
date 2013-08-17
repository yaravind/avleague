package com.aravind.avl.domain;

import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.google.common.base.Joiner;

@NodeEntity
public class Match
{
	@GraphId
	private Long nodeId;

	@Fetch
	@RelatedTo (type = "TEAM_A")
	private Team teamA;

	@Fetch
	@RelatedTo (type = "TEAM_B")
	private Team teamB;

	@RelatedTo (type = "WINNER")
	private Team winner;

	@GraphProperty
	private String name;

	@RelatedTo (type = "MVP")
	private Player mvp;

	@Fetch
	@RelatedTo (type = "PART_OF_POOL")
	private Pool pool;

	@GraphProperty (propertyType = Long.class)
	private Date time;

	@Fetch
	@RelatedTo (type = "PLAYED_ON")
	private Court playedOnCourt;

	@Transient
	private static final transient Joiner NAME_MAKER = Joiner.on(" ");

	public Match()
	{}

	public Match(Team team1, Team team2, Pool p)
	{
		teamA = team1;
		teamB = team2;
		pool = p;
		name = NAME_MAKER.join(teamA.getName(), "v", teamB.getName());
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public String getName()
	{
		return name;
	}

	public Team getTeamA()
	{
		return teamA;
	}

	public void setTeamA(Team teamA)
	{
		this.teamA = teamA;
	}

	public Team getTeamB()
	{
		return teamB;
	}

	public void setTeamB(Team teamB)
	{
		this.teamB = teamB;
	}

	public Team getWinner()
	{
		return winner;
	}

	public Court getPlayedOnCourt()
	{
		return playedOnCourt;
	}

	public void setPlayedOn(Court playedOnCourt)
	{
		this.playedOnCourt = playedOnCourt;
	}

	public void setWinner(Team winner)
	{
		this.winner = winner;
	}

	public Player getMvp()
	{
		return mvp;
	}

	public void setMvp(Player mvp)
	{
		this.mvp = mvp;
	}

	public Pool getPool()
	{
		return pool;
	}

	public void setPool(Pool pool)
	{
		this.pool = pool;
	}

	public Date getTime()
	{
		return time;
	}

	public void setTime(Date t)
	{
		this.time = t;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((pool == null) ? 0 : pool.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Match other = (Match) obj;
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!name.equals(other.name))
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
		if (pool == null)
		{
			if (other.pool != null)
			{
				return false;
			}
		}
		else if (!pool.equals(other.pool))
		{
			return false;
		}
		if (time == null)
		{
			if (other.time != null)
			{
				return false;
			}
		}
		else if (!time.equals(other.time))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "Match [nodeId=" + nodeId + ", teamA=" + teamA + ", teamB=" + teamB + ", winner=" + winner + ", name=" + name
				+ ", mvp=" + mvp + ", pool=" + pool + ", time=" + time + ", playedOnCourt=" + playedOnCourt + "]";
	}
}