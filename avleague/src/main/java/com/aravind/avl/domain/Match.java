package com.aravind.avl.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.google.common.base.Joiner;

@NodeEntity
public class Match
{
	@GraphId
	private Long nodeId;

	@RelatedTo(type = "PART_OF_LEAGUE")
	private League league;

	@RelatedTo(type = "TEAM_A")
	private Team teamA;

	@RelatedTo(type = "TEAM_B")
	private Team teamB;

	@RelatedTo(type = "WINNER")
	private Team winner;

	@GraphProperty
	private String name;

	@Transient
	private static final transient Joiner NAME_MAKER = Joiner.on("-");

	public Match()
	{
	}

	public Match(Team team1, Team team2, League partOf)
	{
		teamA = team1;
		teamB = team2;
		league = partOf;

		// DateFormat df = new SimpleDateFormat("MMM dd, yyyy");

		name = NAME_MAKER.join(teamA.getName(), "vs.", teamB.getName());
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

	public void setWinner(Team winner)
	{
		this.winner = winner;
	}

}
