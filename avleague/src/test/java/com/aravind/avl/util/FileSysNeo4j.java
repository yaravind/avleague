package com.aravind.avl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.Player;
import com.aravind.avl.domain.PlayerTeamLeague;
import com.aravind.avl.domain.Team;

public class FileSysNeo4j
{
	@Autowired
	GraphRepository<Player> repo;

	@Autowired
	GraphRepository<League> leagueRepo;

	@Autowired
	GraphRepository<Team> teamRepo;

	@Autowired
	Neo4jTemplate template;

	@Autowired
	GraphDatabaseService gds;
	PlatformTransactionManager txMgr;

	public static void main(String[] args) throws ParseException
	{
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("testContext-file-sys-db.xml");
		FileSysNeo4j main = new FileSysNeo4j();

		try
		{
			init(ctx, main);

			if ("1".equals(args[0]))
			{
				main.setup();

				main.organizeNewLeague();
				main.findPlayedWith();
				main.organizeNewLeague();// to generate more than 2 relations between same nodes
				main.findPlayedWith();
				main.organizeNewLeague();// to generate more than 2 relations between same nodes
				main.findPlayedWith();
				main.organizeNewLeague();// to generate more than 2 relations between same nodes
				main.findPlayedWith();
			}
			else
			{
				main.run();
				main.findPlayedWith();
			}

		}
		finally
		{
			main.gds.shutdown();
		}
	}

	private PlayerTeamLeague createPlayedWithRelation(League currentLeague, Team team, Player p)
	{
		System.err.println("Creating PLAYED_WITH_TEAM relation between " + team + " and " + p + " and " + currentLeague);

		// PlayedWith playedWith = template.createRelationshipBetween(p, team, PlayedWith.class, "PLAYED_WITH_TEAM",
		// true);
		// playedWith.setDuring(currentLeague.getStartDate());
		// playedWith.setInLeague(currentLeague);
		// playedWith.setPlayer(p);
		// playedWith.setTeam(team);
		// playedWith.setAsCaptain(p.isCaptain());
		//
		// team.addPlayer(p);
		// template.save(playedWith);

		return null;
	}

	private void organizeNewLeague()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txMgr.getTransaction(def);
		Date now = new Date();
		try
		{
			League l = new League();
			l.setName(df.format(now));
			l.setStartDate(now);
			leagueRepo.save(l);
			System.err.println("Saved new league" + l);

			Team team = teamRepo.findByPropertyValue("name", "MegaBytes");
			System.err.println("Found: " + team);
			Player aravind = repo.findByPropertyValue("name", "Aravind Yarram");
			System.err.println("Found: " + aravind);
			createPlayedWithRelation(l, team, aravind);

			l.addTeam(team);
			leagueRepo.save(l);
			System.err.println("Saved updated league" + l);

			txMgr.commit(status);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			txMgr.rollback(status);
		}
	}

	private static void init(ClassPathXmlApplicationContext ctx, FileSysNeo4j main)
	{
		main.txMgr = (PlatformTransactionManager) ctx.getBean("transactionManager");

		main.template = ctx.getBean(Neo4jTemplate.class);
		main.gds = ctx.getBean(GraphDatabaseService.class);

		main.repo = main.template.repositoryFor(Player.class);
		main.leagueRepo = main.template.repositoryFor(League.class);
		main.teamRepo = main.template.repositoryFor(Team.class);
	}

	private void setup()
	{
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txMgr.getTransaction(def);
		try
		{
			League june = new League();
			june.setName("June League");
			june.setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2013"));
			leagueRepo.save(june);
			System.err.println("Saved " + june);

			Team teamA = new Team();
			teamA.setName("MegaBytes");
			teamRepo.save(teamA);
			System.err.println("Saved " + teamA);

			Player p1 = new Player();
			p1.setName("Aravind Yarram");

			Player p2 = new Player();
			p2.setName("Threveni Mohan");

			teamA.addPlayer(p1);
			teamA.addPlayer(p2);

			repo.save(p1);
			System.err.println("Saved " + p1);
			repo.save(p2);
			System.err.println("Saved " + p2);

			createPlayedWithRelation(june, teamA, p1);
			createPlayedWithRelation(june, teamA, p2);
			teamRepo.save(teamA);
			System.err.println("Saved " + teamA);
			txMgr.commit(status);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			txMgr.rollback(status);
		}
	}

	public void run() throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txMgr.getTransaction(def);
		try
		{
			League june = new League();
			june.setName("League");
			leagueRepo.save(june);

			Team teamA = new Team();
			teamA.setName("Team");
			teamRepo.save(teamA);

			Player p = new Player();
			p.setName("Player");
			repo.save(p);

			PlayerTeamLeague playedWithJune = p.playedForInLeagueAsCaptain(teamA, june.getStartDate(), june);

			teamA.addPlayer(p);
			repo.save(p);

			League sept = new League();
			sept.setName("September League");
			leagueRepo.save(sept);

			PlayerTeamLeague playedWithSept = p.playedForInLeagueAsCaptain(teamA, june.getStartDate(), sept);

			repo.save(p);

			txMgr.commit(status);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			txMgr.rollback(status);
		}
	}

	public void findPlayedWith()
	{
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = txMgr.getTransaction(def);
		try
		{
			Iterable<Player> result = repo.findAll();

			for (Player p: result)
			{
				System.err.println("For " + p);

				// template.fetch(p.getPlayedWith());
				//
				// for (PlayedWith pp: p.getPlayedWith())
				// {
				// System.err.println("\t" + pp);
				// }
			}
			txMgr.commit(status);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			txMgr.rollback(status);
		}
	}
}