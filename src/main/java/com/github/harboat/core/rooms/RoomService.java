package com.github.harboat.core.rooms;

import com.github.harboat.clients.game.Size;
import com.github.harboat.clients.notification.EventType;
import com.github.harboat.clients.rooms.*;
import com.github.harboat.core.websocket.Event;
import com.github.harboat.core.websocket.WebsocketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {

    private RoomRepository repository;
    private RoomQueueProducer roomQueueProducer;
    private WebsocketService websocketService;

    public void create(String playerId) {
        roomQueueProducer.send(
                new RoomCreate(playerId)
        );
    }

    public void create(RoomCreated roomCreated) {
        repository.save(
                Room.builder()
                        .roomId(roomCreated.roomId())
                        .size(new Size(10, 10))
                        .players(List.of(roomCreated.ownerId()))
                        .visible(true)
                        .build()
        );
        websocketService.notifyFrontEnd(
                roomCreated.ownerId(), new Event<>(EventType.ROOM_CREATED, roomCreated)
        );
    }

    public void changeReadiness(String roomId, String playerId) {
        roomQueueProducer.send(
                new ChangePlayerReadiness(roomId, playerId)
        );
    }

    public void start(String roomId, String playerId) {
        roomQueueProducer.send(
                new MarkStart(roomId, playerId)
        );
    }

    public void start(RoomGameStart roomGameStart) {
        Room room = repository.findByRoomId(roomGameStart.roomId()).orElseThrow();
        room.getPlayers()
                .forEach(p -> websocketService.notifyFrontEnd(p, new Event<>(EventType.ROOM_GAME_STARTED, roomGameStart)));
    }

    public void playerJoin(RoomPlayerJoined roomPlayerJoined) {
        Room room = repository.findByRoomId(roomPlayerJoined.roomId()).orElseThrow();
        room.getPlayers().add(roomPlayerJoined.playerId());
        room.setVisible(false);
        repository.save(room);
        room.getPlayers()
                .forEach(p -> {
                    String enemyId = room.getPlayers().stream()
                                        .dropWhile(i -> !i.equals(p))
                                        .findFirst().orElseThrow();
                    websocketService.notifyFrontEnd(p, new Event<>(EventType.ROOM_JOINED, new PlayerRoomJoined(room.getRoomId(), p, enemyId)));
                });
        websocketService.notifyFrontEnd(roomPlayerJoined.playerId(), new Event<>(EventType.BOARD_CREATED, room.getSize()));
    }

    public void join(String roomId, String playerId) {
        roomQueueProducer.send(
                new RoomPlayerJoin(roomId, playerId)
        );
    }
}
