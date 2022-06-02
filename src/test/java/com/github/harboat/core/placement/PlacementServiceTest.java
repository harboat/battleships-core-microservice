package com.github.harboat.core.placement;

import com.github.harboat.clients.game.Size;
import com.github.harboat.clients.placement.GeneratePlacement;
import com.github.harboat.core.rooms.Room;
import com.github.harboat.core.rooms.RoomRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.testng.Assert.*;
import static org.mockito.BDDMockito.*;

@Listeners({MockitoTestNGListener.class})
public class PlacementServiceTest {

    @Mock
    private PlacementQueueProducer producer;
    @Mock
    private RoomRepository repository;
    private PlacementService service;
    private String roomId;
    private String playerId;

    @BeforeMethod
    public void setUp() {
        service = new PlacementService(producer, repository);
        roomId = "testRoom";
        playerId = "testPlayer";
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void palaceShipsShouldThrowWhenThereIsNoRoomWithThisId() {
        //given
        given(repository.findByRoomId(roomId)).willReturn(Optional.empty());
        //when
        service.palaceShips(roomId,playerId);
        //then
    }

    @Test
    public void palaceShipsShouldSendGeneratePlacementWithProperRoomId() {
        //given
        Size size = new Size(10,10);
        Room room = Room.builder()
                .roomId(roomId)
                .size(size)
                .build();
        given(repository.findByRoomId(roomId)).willReturn(Optional.of(room));
        ArgumentCaptor<GeneratePlacement> captor =ArgumentCaptor.forClass(GeneratePlacement.class);
        //when
        service.palaceShips(roomId,playerId);
        verify(producer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.roomId(), roomId);
    }

    @Test
    public void palaceShipsShouldSendGeneratePlacementWithProperPlayerId() {
        //given
        Size size = new Size(10,10);
        Room room = Room.builder()
                .roomId(roomId)
                .size(size)
                .build();
        given(repository.findByRoomId(roomId)).willReturn(Optional.of(room));
        ArgumentCaptor<GeneratePlacement> captor =ArgumentCaptor.forClass(GeneratePlacement.class);
        //when
        service.palaceShips(roomId,playerId);
        verify(producer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.playerId(), playerId);
    }

    @Test
    public void palaceShipsShouldSendGeneratePlacementWithProperSize() {
        //given
        Size size = new Size(10,10);
        Room room = Room.builder()
                .roomId(roomId)
                .size(size)
                .build();
        given(repository.findByRoomId(roomId)).willReturn(Optional.of(room));
        ArgumentCaptor<GeneratePlacement> captor =ArgumentCaptor.forClass(GeneratePlacement.class);
        //when
        service.palaceShips(roomId,playerId);
        verify(producer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.size(), size);
    }

}