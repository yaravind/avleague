package com.aravind.avl.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.google.common.collect.Lists;

import static org.neo4j.graphdb.Direction.INCOMING;
import static org.neo4j.graphdb.Direction.OUTGOING;

@NodeEntity
public class League implements Comparable<League>
{
	@GraphId
	private Long nodeId;

	@Indexed (unique = true)
	@GraphProperty
	private String name;

	@Indexed
	@GraphProperty (propertyType = Long.class)
	private Date startDate;

	@Indexed
	@GraphProperty (propertyType = Long.class)
	private Date endDate;

	@Fetch
	@RelatedTo (type = "LEVEL", direction = OUTGOING)
	private Level level;

	@Fetch
	@RelatedTo (type = "CONTESTED_IN", direction = INCOMING)
	private final Set<Team> teams = new HashSet<Team>();

	@RelatedTo (type = "PLAYED_AT", direction = OUTGOING)
	private Set<Venue> playedAt = new HashSet<Venue>();

	@RelatedTo (type = "PART_OF_LEAGUE", direction = INCOMING)
	private Set<Match> matches = new HashSet<Match>();

	public League()
	{}

	public League(String leagueName, Date leagueStartDate, Date leagueEndDate)
	{
		name = StringUtil.capitalizeFirstLetter(leagueName);
		startDate = leagueStartDate;
		endDate = leagueEndDate;
	}

	public Match conductMatch(Team teamA, Team teamB, Court court, Level l, Pool p, Date at)
	{
		Match m = l.conductMatch(teamA, teamB, court, p, at);
		matches.add(m);
		return m;
	}

	public Venue findVenueByName(String name)
	{
		for (Venue v: playedAt)
		{
			if (v.getName().equalsIgnoreCase(name))
			{
				return v;
			}
		}
		return null;
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

	public void setName(String name)
	{
		this.name = StringUtil.capitalizeFirstLetter(name);
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public Set<Match> getMatches()
	{
		return matches;
	}

	public void setMatches(Set<Match> ms)
	{
		this.matches = ms;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		League other = (League) obj;
		if (endDate == null)
		{
			if (other.endDate != null)
			{
				return false;
			}
		}
		else if (!endDate.equals(other.endDate))
		{
			return false;
		}
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
		if (startDate == null)
		{
			if (other.startDate != null)
			{
				return false;
			}
		}
		else if (!startDate.equals(other.startDate))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "League [nodeId=" + nodeId + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", level="
				+ level + "]";
	}

	@Override
	public int compareTo(League other)
	{
		return this.getStartDate().compareTo(other.getStartDate());
	}

	public void setPlayedAt(Set<Venue> v)
	{
		playedAt = v;
	}

	public Set<Venue> getPlayedAt()
	{
		return playedAt;
	}

	public void addVenue(Venue newVenue)
	{
		playedAt.add(newVenue);
	}

	public Level getLevel()
	{
		return level;
	}

	public void setLevel(Level level)
	{
		this.level = level;
	}

	public Level findLevelByName(String name)
	{
		return findLevel(level, name);
	}

	public List<Level> getAllLevels()
	{
		List<Level> all = Lists.newArrayList();
		return nextLevel(all, level);
	}

	private List<Level> nextLevel(List<Level> all, Level l)
	{
		if (l == null)
		{
			return all;
		}
		else
		{
			all.add(l);
		}
		return nextLevel(all, l.getNextLevel());
	}

	private Level findLevel(Level l, String name)
	{
		if (l == null)
		{
			return null;
		}

		if (l.getName().equals(name))
		{
			return l;
		}
		else
		{
			return findLevel(l.getNextLevel(), name);
		}
	}
}
