package com.aravind.avl.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Venue
{
	@GraphId
	private Long nodeId;

	@GraphProperty
	private String name;

	@Fetch
	@RelatedTo (type = "COURT")
	private Set<Court> courts = new HashSet<Court>();

	public Venue()
	{}

	public Venue(String n)
	{
		this.name = StringUtil.capitalizeFirstLetter(n);
	}

	public Court findCourtByName(String name)
	{
		for (Court c: courts)
		{
			if (c.getName().equalsIgnoreCase(name))
			{
				return c;
			}
		}
		return null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = StringUtil.capitalizeFirstLetter(name);
	}

	public Set<Court> getCourts()
	{
		return courts;
	}

	public void setCourts(Set<Court> courts)
	{
		this.courts = courts;
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	@Override
	public String toString()
	{
		return "Venue [nodeId=" + nodeId + ", name=" + name + ", courts=" + courts + "]";
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
		Venue other = (Venue) obj;
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

	public void addCourt(Court court)
	{
		courts.add(court);
	}
}
