package com.aravind.avl.domain;

import java.util.Date;

import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.google.common.base.Joiner;

@NodeEntity
public class Match
{
	@GraphId
	private Long nodeId;

	@RelatedTo (type = "PART_OF_LEAGUE")
	private League league;

	@RelatedTo (type = "TEAM_A")
	private Team teamA;

	@RelatedTo (type = "TEAM_B")
	private Team teamB;

	@RelatedTo (type = "WINNER")
	private Team winner;

	@GraphProperty
	private String name;

	@RelatedTo (type = "MVP")
	private Player mvp;

	@Indexed
	@GraphProperty
	private Level level;

	@Fetch
	@RelatedTo (type = "PART_OF_POOL")
	private Pool pool;

	@GraphProperty (propertyType = Long.class)
	private Date time;

	@RelatedTo (type = "PLAYED_ON")
	private Court playedOnCourt;

	public enum Level
	{
		PLAYOFFS, QUARTER_FINALS, SEMI_FINALS, ALL_STAR
	}

	@Transient
	private static final transient Joiner NAME_MAKER = Joiner.on(" ");

	public Match()
	{}

	public Match(Team team1, Team team2, League partOf)
	{
		teamA = team1;
		teamB = team2;
		league = partOf;

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

	public League getLeague()
	{
		return league;
	}

	public void setLeague(League league)
	{
		this.league = league;
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

	public Level getLevel()
	{
		return level;
	}

	public void setLevel(Level level)
	{
		this.level = level;
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

	public void setTime(Date time)
	{
		this.time = time;
	}

	@Override
	public String toString()
	{
		return "Match [nodeId=" + nodeId + ", league=" + league + ", teamA=" + teamA + ", teamB=" + teamB + ", winner=" + winner
				+ ", name=" + name + ", mvp=" + mvp + ", level=" + level + ", pool=" + pool + ", time=" + time + ", playedOnCourt="
				+ playedOnCourt + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((league == null) ? 0 : league.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
		Match other = (Match) obj;
		if (league == null)
		{
			if (other.league != null)
				return false;
		}
		else if (!league.equals(other.league))
			return false;
		if (level != other.level)
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (nodeId == null)
		{
			if (other.nodeId != null)
				return false;
		}
		else if (!nodeId.equals(other.nodeId))
			return false;
		return true;
	}

}
