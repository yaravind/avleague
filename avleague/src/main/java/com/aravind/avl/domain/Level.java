package com.aravind.avl.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import static org.neo4j.graphdb.Direction.OUTGOING;

@NodeEntity
public class Level
{
	@GraphId
	private Long nodeId;

	/**
	 * Playoffs, Round 2, Semifinal, Quarterfinal, Finals, All Star
	 */
	@GraphProperty
	private String name;

	@RelatedTo (type = "FIXTURE", direction = OUTGOING)
	private Set<Match> fixtures = new HashSet<Match>();

	@Fetch
	@RelatedTo (type = "NEXT", direction = OUTGOING)
	private Level nextLevel;

	public Level()
	{}

	public Level(String n)
	{
		name = StringUtil.capitalizeFirstLetter(n);
	}

	public Match conductMatch(Team teamA, Team teamB, Court court)
	{
		Match m = new Match(teamA, teamB);
		m.setPlayedOn(court);
		fixtures.add(m);
		return m;
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public Set<Match> getFixtures()
	{
		return fixtures;
	}

	public void setFixtures(Set<Match> fixtures)
	{
		this.fixtures = fixtures;
	}

	public void setNodeId(Long nodeId)
	{
		this.nodeId = nodeId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String n)
	{
		this.name = StringUtil.capitalizeFirstLetter(n);
	}

	public Level getNextLevel()
	{
		return nextLevel;
	}

	public void setNextLevel(Level nl)
	{
		this.nextLevel = nl;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
		Level other = (Level) obj;
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
		return true;
	}

	@Override
	public String toString()
	{
		return "Level [nodeId=" + nodeId + ", name=" + name + ", nextLevel=" + nextLevel + "]";
	}
}
