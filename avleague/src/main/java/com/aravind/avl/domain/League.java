package com.aravind.avl.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class League
{
	private Long nodeId;

	private final String name;

	private final Date startDate;

	private final Date endDate;

	private final Set<Team> teams = new HashSet<Team>();

	public League(String leagueName, Date leagueStartDate, Date leagueEndDate)
	{
		name = leagueName;
		startDate = leagueStartDate;
		endDate = leagueEndDate;
	}

	public void addTeam(Team t)
	{
		teams.add(t);
	}

	public Set<Team> getTeams()
	{
		return teams;
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public String getName()
	{
		return name;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	@Override
	public String toString()
	{
		return "League [nodeId=" + nodeId + ", name=" + name + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
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
		League other = (League) obj;
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
