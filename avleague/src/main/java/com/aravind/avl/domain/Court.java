package com.aravind.avl.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Court
{
	@GraphId
	private Long nodeId;

	@GraphProperty
	private String name;

	public Court()
	{}

	public Court(String n)
	{
		name = StringUtil.capitalizeFirstLetter(n);
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = StringUtil.capitalizeFirstLetter(name);
	}

	@Override
	public String toString()
	{
		return "Court [nodeId=" + nodeId + ", name=" + name + "]";
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
		Court other = (Court) obj;
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
