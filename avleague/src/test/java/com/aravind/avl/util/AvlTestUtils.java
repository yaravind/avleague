package com.aravind.avl.util;

import com.aravind.avl.domain.League;
import com.aravind.avl.service.CannotCreateLeagueException;
import com.aravind.avl.service.LeagueFactory;
import com.aravind.avl.service.LeaguePopulator;
import com.google.common.io.Resources;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Sasidhar
 * Date: 6/22/13
 * Time: 1:20 PM
 * Test Utils.
 */
public class AvlTestUtils {

    private static final LeagueFactory lf = new LeagueFactory();

    private AvlTestUtils() {
        // No need to instantiate.
    }

    public static Collection<League> createLeague(LeaguePopulator populator, String leagueDataFile, int teamNameRow, int emailColumn, int pohoneColumn) throws URISyntaxException, CannotCreateLeagueException {
        URI uri = Resources.getResource(leagueDataFile).toURI();

        LeagueFactory.ImportProfile profile = new LeagueFactory.ImportProfile();
        profile.teamNameRowNum = teamNameRow;
        profile.emailColumnNum = emailColumn;
        profile.phoneColumnNum = pohoneColumn;
        League league = lf.createLeague(new File(uri), profile);
        assertNotNull(league);
        return populator.populateDatabase(leagueDataFile, profile);
    }

}
