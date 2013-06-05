package com.aravind.avl.domain;

import static com.aravind.avl.domain.StringUtil.capitalizeFirstLetter;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import com.google.common.base.Splitter;

@NodeEntity
public class Player
{
	@GraphId
	private Long nodeId;

	@Indexed(unique = true)
	private String name;// Uses default index which is the name of the class.

	@GraphProperty
	@Indexed
	// (indexType = IndexType.FULLTEXT, indexName = "playerName") enable custom
	// index if we want to support
	// case-insensitive search
	private String firstName;

	@GraphProperty
	private String email;

	@GraphProperty
	private String phoneNumber;

	@GraphProperty
	@Indexed
	// (indexType = IndexType.FULLTEXT, indexName = "playerName")
	private String lastName;

	@RelatedToVia
	Set<PlayedWith> playedWith = new HashSet<PlayedWith>();

	private transient Boolean isCaptain;

	private static final transient Splitter NAME_SPLITTER = Splitter.on(" ").trimResults().omitEmptyStrings();

	public PlayedWith playedWith(Team t, Date during, League inLeague)
	{
		PlayedWith playedWithTeamDuring = new PlayedWith(this, t, during, inLeague);
		playedWith.add(playedWithTeamDuring);

		return playedWithTeamDuring;
	}

	public PlayedWith playedWithAsCaptain(Team t, Date during, League inLeague)
	{
		PlayedWith playedWithTeamDuring = new PlayedWith(this, t, during, inLeague, true);
		playedWith.add(playedWithTeamDuring);

		return playedWithTeamDuring;
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
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
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
		isCaptain = b;
	}

	public Boolean isCaptain()
	{
		return isCaptain == null ? Boolean.FALSE : isCaptain;
	}
}