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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public static class ImportProfile
	{
		public int teamNameRowNum;
		public int emailColumnNum;
		public int phoneColumnNum;
	}

	private transient final Logger LOG = LoggerFactory.getLogger(getClass());
	private static final Splitter SPLIT_ON_COMMA = Splitter.on(",").trimResults();

	public League createLeague(File leagueFile, ImportProfile profile) throws CannotCreateLeagueException
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
			LOG.error("Error while parsing Leage start and end dates.", e);
			throw new CannotCreateLeagueException("Cannot create league as start date or end date are invalid.", e);
		}
		// First row contains team names
		List<String> rawTeamNames = Lists.newArrayList(SPLIT_ON_COMMA.split(rows.get(profile.teamNameRowNum)));
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

		for (String teamName : teamPerRow.rowKeySet())
		{
			createTeamIfDoesntExist(league, teamName);

			Map<Integer, String> rawTeam = leagueTable.column(teamName);
			// TODO 1st column is always captain, and the last 2 columns are
			// email and phone number respectivley
			addCaptainTo(league, rawTeam.get(1), rawTeam.get(profile.emailColumnNum), rawTeam.get(profile.phoneColumnNum), teamName);

			// remaining columns (from 2-profile.emailColumnNum) are player
			// names
			for (int i = 2; i < profile.emailColumnNum; i++)
			{
				String name = rawTeam.get(i);
				if (StringUtils.isNotBlank(name))
				{
					addPlayerTo(league, name, teamName);
				}
			}
		}

		for (Team t : league.getTeams())
		{

			LOG.debug("Team: {}. Total players: {}", t.getName(), Lists.newArrayList(t.getPlayers()).size());
		}
		return league;
	}

	public Team createTeamIfDoesntExist(League league, String teamName)
	{
		boolean newTeam = true;

		for (Team t : league.getTeams())
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
			LOG.debug("Creating team: {}", teamName);
			t = new Team();
			t.setName(capitalizeFirstLetter(teamName));
			league.addTeam(t);
		}

		return t;
	}

	public void addPlayerTo(League league, String fullName, String teamName)
	{
		LOG.debug("Adding {} to team: {}", fullName, teamName);
		Player p = new Player();
		p.setName(fullName);
		Team team = createTeamIfDoesntExist(league, teamName);
		team.addPlayer(p);
	}

	public void addCaptainTo(League league, String captainName, String email, String phoneNumber, String teamName)
	{
		Player p = new Player();
		p.setCaptain(true);
		p.setName(captainName);
		p.setEmail(email);
		p.setPhoneNumber(phoneNumber);
		Team team = createTeamIfDoesntExist(league, teamName);
		team.addPlayer(p);
	}
}
