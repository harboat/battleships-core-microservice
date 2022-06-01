package com.github.harboat.core.games;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameUtility {

    private GameRepository repository;

    public Collection<String> getNotStartedGamesIdsForUser(String playerId) {
        return repository.findGamesByOwnerIdAndNotStarted(playerId).stream()
                .map(Game::getGameId)
                .toList();
    }

    public Optional<Game> findByGameId(String gameId) {
        return repository.findByGameId(gameId);
    }

    public void switchTurn(String gameId, String enemyId) {
        Game game = repository.findByGameId(gameId).orElseThrow();
        game.setPlayerTurn(enemyId);
        repository.save(game);
    }

    public String getEnemyId(String gameId, String playerId) {
        Game game = repository.findByGameId(gameId).orElseThrow();
        return game
                .getPlayers().stream()
                .dropWhile(s -> s.equals(playerId))
                .findAny().orElseThrow();
    }
}
