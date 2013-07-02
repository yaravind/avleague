package com.aravind.avl.api;

import com.aravind.avl.domain.League;
import com.aravind.avl.domain.LeagueRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sasidhar
 * Date: 6/22/13
 * Time: 10:53 AM
 *
 * Rest Operations for CRUD operations on League domain entity.
 */

@Controller
@RequestMapping("/api/")
public class LeagueRestService {

    @Autowired
    private LeagueRepository leagueRepo;

    @RequestMapping(value = "/leagues", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<League> getLeagues()
    {
        Iterable<League> all = leagueRepo.findAll();
        return Lists.newArrayList(all);
    }

    @RequestMapping(value = "/leagues", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody League createLeague(League newLeague)
    {
        League savedLeague = leagueRepo.save(newLeague);
        return savedLeague;
    }
}
