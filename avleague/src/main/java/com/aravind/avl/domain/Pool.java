package com.aravind.avl.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import static org.neo4j.graphdb.Direction.OUTGOING;
import static com.aravind.avl.domain.StringUtil.capitalizeFirstLetter;

import static com.google.common.base.Preconditions.checkArgument;

@NodeEntity
public class Pool
{
	@GraphId
	private Long nodeId;

	@Indexed
	@GraphProperty
	private String name;

	@Fetch
	@RelatedTo (type = "TEAM", direction = OUTGOING)
	private Set<Team> teams = new HashSet<Team>();

	@RelatedTo (type = "FIXTURE", direction = OUTGOING)
	private Set<Match> fixtures = new HashSet<Match>();

	public void conductMatch(Match m)
	{
		checkArgument(teams.contains(m.getTeamA()),
				"Match can only be between teams that are part of same pool.  %s is not part of  %s", m.getTeamA(), this);
		checkArgument(teams.contains(m.getTeamB()),
				"Match can only be between teams that are part of same pool.  %s is not part of  %s", m.getTeamB(), this);
		checkArgument(m.getPool().equals(this), " %s isn't for this  %s.", m.getPool(), this);

		fixtures.add(m);
	}

	public Pool()
	{}

	public Pool(String n)
	{
		this.name = capitalizeFirstLetter(n);;
	}

	public void addTeam(Team t)
	{
		teams.add(t);
	}

	public Set<Team> getTeams()
	{
		return teams;
	}

	public void setTeams(Set<Team> teams)
	{
		this.teams = teams;
	}

	public Set<Match> getFixtures()
	{
		return fixtures;
	}

	public void setFixtures(Set<Match> fixtures)
	{
		this.fixtures = fixtures;
	}

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

	@Override
	public String toString()
	{
		return "Pool [nodeId=" + nodeId + ", name=" + name + ", teams=" + teams + "]";
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
		Pool other = (Pool) obj;
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
}