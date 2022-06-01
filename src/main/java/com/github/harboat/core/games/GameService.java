package com.github.harboat.core.games;

import com.github.harboat.clients.exceptions.BadRequest;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.clients.game.GameCreated;
import com.github.harboat.clients.game.PlayerWon;
import com.github.harboat.clients.notification.EventType;
import com.github.harboat.core.websocket.Event;
import com.github.harboat.core.websocket.WebsocketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameService {

    private GameRepository repository;
    private WebsocketService websocketService;

    public void create(GameCreated gameCreated) {
        repository.save(
                Game.builder()
                        .gameId(gameCreated.gameId())
                        .players(gameCreated.players())
                        .playerTurn(gameCreated.playerTurn())
                        .ended(false)
                        .build()
        );
        gameCreated.players().forEach(p -> {
            websocketService.notifyFrontEnd(p, new Event<>(EventType.GAME_STARTED, gameCreated));
        });
    }

    public void forfeit(String playerId, String gameId) {
        Game game = repository.findByGameId(gameId).orElseThrow(() -> new ResourceNotFound("Game not found!"));
        if (!game.getPlayers().contains(playerId)) throw new BadRequest("You are not in this game");
        String enemyId = game.getPlayers().stream()
                .dropWhile(p -> p.equals(playerId))
                .findFirst().orElseThrow();
        endGame(new PlayerWon(gameId, enemyId));
    }

    public void endGame(PlayerWon playerWon) {
        Game game = repository.findByGameId(playerWon.gameId()).orElseThrow();
        game.setEnded(true);
        repository.save(game);
        game.getPlayers().forEach(p ->
                websocketService.notifyFrontEnd(
                        p,
                        new Event<>(EventType.GAME_END, new GameEnded(playerWon.playerId()))
                )
        );
    }

}
