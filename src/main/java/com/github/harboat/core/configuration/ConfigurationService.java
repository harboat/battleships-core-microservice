package com.github.harboat.core.configuration;

import com.github.harboat.clients.configuration.SetGameSize;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.clients.game.Size;
import com.github.harboat.clients.notification.EventType;
import com.github.harboat.core.rooms.Room;
import com.github.harboat.core.rooms.RoomRepository;
import com.github.harboat.core.websocket.Event;
import com.github.harboat.core.websocket.WebsocketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfigurationService {

    private ConfigurationQueueProducer producer;
    private RoomRepository roomRepository;
    private WebsocketService websocketService;

    public void setSize(String roomId, String playerId, Size size) {
        producer.send(
                new SetGameSize(roomId, playerId, size)
        );
    }

    public void setSize(SetGameSize setGameSize) {
        Room room = roomRepository.findByRoomId(setGameSize.roomId()).orElseThrow(() -> new ResourceNotFound("Room not found!"));
        room.setSize(setGameSize.size());
        roomRepository.save(room);
        room.getPlayers().forEach(p -> websocketService.notifyFrontEnd(
                p, new Event<>(EventType.BOARD_CREATED, setGameSize.size())
        ));
    }
}
