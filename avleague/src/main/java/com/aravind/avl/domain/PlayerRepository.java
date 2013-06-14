package com.aravind.avl.domain;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface PlayerRepository extends GraphRepository<Player>
{
	/**
	 * Can be used for EXACT matches and prefix matches
	 * 
	 * @param firstName
	 * @return <code>null</code> if no matches found
	 */
	Player findPlayerByFirstName(String firstName);

	/**
	 * Can be used for EXACT matches and prefix matches
	 * 
	 * @param lastName
	 * @return <code>null</code> if no matches found
	 */
	Player findPlayerByLastName(String lastName);

	/**
	 * Can be used for EXACT matches and prefix matches
	 * 
	 * @param fullName
	 * @return <code>null</code> if no matches found
	 */
	Player findByName(String fullName);

	Iterable<Player> findByNameLike(String partialName);
}
