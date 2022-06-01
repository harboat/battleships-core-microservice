package com.github.harboat.core.games;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface GameRepository extends MongoRepository<Game, String> {
    Optional<Game> findByGameId(String gameId);
    @Query("{ownerId: ?0, started:  false}")
    Collection<Game> findGamesByOwnerIdAndNotStarted(String playerId);
}
