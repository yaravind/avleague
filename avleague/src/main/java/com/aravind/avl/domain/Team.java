package com.aravind.avl.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
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
	@Indexed (unique = true)
	private String name;

	@Fetch
	@RelatedTo (type = "PLAYED_WITH_TEAM", direction = Direction.INCOMING)
	private final Set<Player> players = new HashSet<Player>();

	public Team()
	{
		System.err.println("Created team");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = StringUtil.capitalizeFirstLetter(name);
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public Collection<Player> getPlayers()
	{
		return players;
	}

	public void setPlayers(Set<Player> plyrs)
	{
		System.err.println("called set plrs");
		players.addAll(plyrs);
	}

	public void addPlayer(Player p)
	{
		players.add(p);
	}

	@Override
	public String toString()
	{
		return "Team [nodeId=" + nodeId + ", name=" + name + "]";
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
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
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
