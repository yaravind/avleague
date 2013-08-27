package com.aravind.avl.domain;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import com.google.common.base.Splitter;

import static com.aravind.avl.domain.StringUtil.capitalizeFirstLetter;

@NodeEntity
public class Player
{
	@GraphId
	private Long nodeId;

	@Indexed (unique = true, indexType = IndexType.FULLTEXT, indexName = "PlayerName")
	private String name;// Uses default index which is the name of the class.

	@GraphProperty
	@Indexed (indexType = IndexType.FULLTEXT, indexName = "FirstName")
	// enable custom
	// index if we want to support
	// case-insensitive search
	private String firstName;

	@GraphProperty
	private String email;

	@GraphProperty
	private String phoneNumber;

	@GraphProperty
	@Indexed (indexType = IndexType.FULLTEXT, indexName = "LastName")
	private String lastName;

	// TODO: Temporarily ignoring since Jackson cannot serialize this to JSON
	@JsonIgnore
	@Fetch
	@RelatedTo (type = "PLAYED_FOR_IN_LEAGUE", direction = Direction.OUTGOING)
	Set<PlayerTeamLeague> playedforInLeague = new HashSet<PlayerTeamLeague>();

	/**
	 * We doesn't want this to be saved in the Player nodes as this is a property of PlayedWith relationship.
	 */
	@Transient
	private Boolean captain;

	public static final Comparator<Player> NAME_CASE_INSENSITIVE_COMPARATOR = new Comparator<Player>()
	{
		@Override
		public int compare(Player o1, Player o2)
		{
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};

	private static final transient Splitter NAME_SPLITTER = Splitter.on(" ").trimResults().omitEmptyStrings();

	public Player()
	{}

	public PlayerTeamLeague playedForInLeague(Team t, Date during, League inLeague)
	{
		PlayerTeamLeague ptl = new PlayerTeamLeague(t, during, inLeague, false);
		playedforInLeague.add(ptl);

		return ptl;
	}

	public PlayerTeamLeague playedForInLeagueAsCaptain(Team t, Date during, League inLeague)
	{
		PlayerTeamLeague ptl = new PlayerTeamLeague(t, during, inLeague, true);
		playedforInLeague.add(ptl);

		return ptl;
	}

	public Long getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(Long nodeId)
	{
		this.nodeId = nodeId;
	}

	public String getName()
	{
		return name;
	}

	public Set<PlayerTeamLeague> getPlayedforInLeague()
	{
		return playedforInLeague;
	}

	public void setPlayedforInLeague(Set<PlayerTeamLeague> playedforInLeague)
	{
		this.playedforInLeague = playedforInLeague;
	}

	public void setName(String fullName)
	{
		String cleansed = fullName.trim();
		cleansed = cleansed.toLowerCase();

		if (cleansed.contains(" "))
		{
			Iterator<String> names = NAME_SPLITTER.split(cleansed).iterator();
			setFirstName(capitalizeFirstLetter(names.next()));
			setLastName(capitalizeFirstLetter(names.next()));
		}
		else
		{
			setFirstName(capitalizeFirstLetter(cleansed));
		}
		name = capitalizeFirstLetter(cleansed);
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public Boolean getCaptain()
	{
		return captain;
	}

	@Override
	public String toString()
	{
		return "Player [nodeId=" + nodeId + ", name=" + name + ", firstName=" + firstName + ", lastName=" + lastName + "]";
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
		Player other = (Player) obj;
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

	public void setEmail(String e)
	{
		email = e;
	}

	public void setPhoneNumber(String p)
	{
		phoneNumber = p;
	}

	public void setCaptain(Boolean b)
	{
		captain = b;
	}

	public Boolean isCaptain()
	{
		return captain == null ? Boolean.FALSE : captain;
	}
}
