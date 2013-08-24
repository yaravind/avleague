package com.aravind.avl.domain;

import java.util.List;

public interface TeamRepositoryExtension
{
	List<League> findLeaguesLevelsAndPools(String teamName);
}