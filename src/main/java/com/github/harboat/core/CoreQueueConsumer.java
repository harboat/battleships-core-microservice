package com.github.harboat.core;

import com.github.harboat.clients.configuration.SetGameSize;
import com.github.harboat.clients.game.GameCreated;
import com.github.harboat.clients.game.PlayerWon;
import com.github.harboat.clients.game.ShotResponse;
import com.github.harboat.clients.rooms.RoomCreated;
import com.github.harboat.clients.rooms.RoomGameStart;
import com.github.harboat.clients.rooms.RoomPlayerJoined;
import com.github.harboat.core.configuration.ConfigurationService;
import com.github.harboat.core.games.GameService;
import com.github.harboat.core.rooms.RoomService;
import com.github.harboat.core.shot.ShotService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@RabbitListener(
        queues = {"${rabbitmq.queues.core}"}
)
public class CoreQueueConsumer {

    private RoomService roomService;
    private ConfigurationService configurationService;
    private GameService gameService;
    private ShotService shotService;

    @RabbitHandler
    @Async("coreQueueConsumerThreads")
    public void consume(RoomCreated roomCreated) {
        roomService.create(roomCreated);
    }

    @RabbitHandler
    @Async("coreQueueConsumerThreads")
    public void consume(RoomGameStart roomGameStart) {
        roomService.start(roomGameStart);
    }

    @RabbitHandler
    @Async("coreQueueConsumerThreads")
    public void consume(SetGameSize setGameSize) {
        configurationService.setSize(setGameSize);
    }

    @RabbitHandler
    @Async("coreQueueConsumerThreads")
    public void consume(RoomPlayerJoined roomPlayerJoined) {
        roomService.playerJoin(roomPlayerJoined);
    }

    @RabbitHandler
    @Async("coreQueueConsumerThreads")
    public void consume(GameCreated gameCreated) {
        gameService.create(gameCreated);
    }

    @RabbitHandler
    @Async("coreQueueConsumerThreads")
    public void consume(ShotResponse shotResponse) {
        shotService.takeAShoot(shotResponse);
    }

    @RabbitHandler
    @Async("coreQueueConsumerThreads")
    public void consume(PlayerWon playerWon) {
        gameService.endGame(playerWon);
    }
}
