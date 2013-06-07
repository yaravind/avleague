package com.aravind.avl.controller;

import java.util.List;

public class PlayerView
{
	private String playerId;

	private String name;

	private List<String> participatedInLeagueNames;

	private boolean captain;

	public boolean isCaptain()
	{
		return captain;
	}

	public void setName(String n)
	{
		name = n;
	}

	public void setParticipatedInLeagueNames(List<String> lns)
	{
		participatedInLeagueNames = lns;
	}

	public String getName()
	{
		return name;
	}

	public List<String> getParticipatedInLeagueNames()
	{
		return participatedInLeagueNames;
	}

	public String getPlayerId()
	{
		return playerId;
	}

	public void setPlayerId(String playerId)
	{
		this.playerId = playerId;
	}

	@Override
	public String toString()
	{
		return "PlayerView [playerId=" + playerId + ", name=" + name + ", participatedInLeagueNames=" + participatedInLeagueNames
				+ "]";
	}

	public void setCaptain(boolean b)
	{
		captain = b;
	}
}
