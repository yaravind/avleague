package com.aravind.avl.service;

import static com.aravind.avl.domain.StringUtil.capitalizeFirstLetter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.Team;
import com.google.common.base.Splitter;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.google.common.io.Files;

/**
 * @author Aravind R. Yarram
 */
public class LeagueFactory
{

	private static final Splitter SPLIT_ON_COMMA = Splitter.on(",").trimResults();

	public League createLeague(File leagueFile) throws CannotCreateLeagueException
	{
		List<String> rows = null;
		try
		{
			rows = Files.readLines(leagueFile, Charset.forName("UTF-8"));
		}
		catch (IOException e1)
		{
			throw new CannotCreateLeagueException(e1);
		}

		// Derive league name, start and end date from the file name
		String[] split = leagueFile.getName().split("_");
		String leagueName = split[0];

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Calendar leagueStartDate = Calendar.getInstance(Locale.US);
		Calendar leagueEndDate = Calendar.getInstance(Locale.US);

		try
		{
			leagueStartDate.setTime(df.parse(split[1]));
			leagueEndDate.setTime(df.parse(split[2]));
		}
		catch (ParseException e)
		{
			throw new CannotCreateLeagueException("Cannot create league as start date or end date are invalid.", e);
		}
		// First row contains team names
		List<String> rawTeamNames = Lists.newArrayList(SPLIT_ON_COMMA.split(rows.get(0)));
		Table<Integer, String, String> leagueTable = HashBasedTable.create();

		for (int rowNum = 1; rowNum < rows.size(); rowNum++)
		{
			List<String> columns = Lists.newArrayList(SPLIT_ON_COMMA.split(rows.get(rowNum)));

			for (int colNum = 0; colNum < columns.size(); colNum++)
			{
				leagueTable.put(rowNum, rawTeamNames.get(colNum), columns.get(colNum));
			}
		}

		League league = new League(leagueName, leagueStartDate.getTime(), leagueEndDate.getTime());

		Table<String, Integer, String> teamPerRow = Tables.transpose(leagueTable);

		for (String teamName: teamPerRow.rowKeySet())
		{
			createTeamIfDoesntExist(league, teamName);

			Map<Integer, String> rawTeam = leagueTable.column(teamName);
			// TODO 1st column is always captain, 9th column is always email and 10th column is always phone number
			// league.addCaptainTo(rawTeam.get(1), rawTeam.get(10), rawTeam.get(9), teamName);

			// remaining columns (from 2-8) are player names
			for (int i = 2; i <= 8; i++)
			{
				addPlayerTo(league, rawTeam.get(i), teamName);
			}
			System.err.println(league.getTeams());
		}

		return league;
	}

	public Team createTeamIfDoesntExist(League league, String teamName)
	{
		boolean newTeam = true;

		for (Team t: league.getTeams())
		{
			if (t.getName().equalsIgnoreCase(teamName))
			{
				newTeam = false;
				return t;
			}
		}

		Team t = null;

		if (newTeam)
		{
			System.out.printf("Creating team: %s \n", teamName);
			t = new Team();
			t.setName(capitalizeFirstLetter(teamName));
			league.addTeam(t);
		}

		return t;
	}

	public void addPlayerTo(League league, String fullName, String teamName)
	{
		Player p = new Player();
		p.setName(fullName);
		Team team = createTeamIfDoesntExist(league, teamName);
		team.addPlayer(p);
	}
}
