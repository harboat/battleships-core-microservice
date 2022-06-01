package com.github.harboat.core.placement;

import com.github.harboat.clients.placement.GeneratePlacement;
import com.github.harboat.core.rooms.Room;
import com.github.harboat.core.rooms.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlacementService {

    private PlacementQueueProducer producer;
    private RoomRepository repository;

    public void palaceShips(String roomId, String playerId) {
        Room room = repository.findByRoomId(roomId).orElseThrow();
        producer.send(
                new GeneratePlacement(roomId, playerId, room.getSize())
        );
    }
}
