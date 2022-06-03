package com.github.harboat.core.configuration;

import com.github.harboat.clients.configuration.SetGameSize;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.clients.game.Size;
import com.github.harboat.core.rooms.Room;
import com.github.harboat.core.rooms.RoomRepository;
import com.github.harboat.core.websocket.WebsocketService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;
import static org.mockito.BDDMockito.*;

@Listeners(MockitoTestNGListener.class)
public class ConfigurationServiceTest {

    @Mock
    private ConfigurationQueueProducer producer;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private WebsocketService websocketService;
    private ConfigurationService configurationService;
    private String roomId;
    private String playerId;
    private Size size;

    @BeforeMethod
    public void setUp() {
        configurationService = new ConfigurationService(producer, roomRepository, websocketService);
        roomId = "testRoom";
        playerId = "testPlayer";
        size = new Size(10, 10);
    }

    @Test
    public void setSizeShouldSendProperSetGameSize() {
        //given
        SetGameSize setGameSize = new SetGameSize(roomId, playerId, size);
        ArgumentCaptor<SetGameSize> captor = ArgumentCaptor.forClass(SetGameSize.class);
        //when
        configurationService.setSize(roomId, playerId, size);
        verify(producer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, setGameSize);
    }

    @Test(expectedExceptions = ResourceNotFound.class, expectedExceptionsMessageRegExp = "Room not found!")
    public void setSizeShouldThrowWhenThereIsNoRoomWithThisId() {
        //given
        SetGameSize setGameSize = new SetGameSize(roomId, playerId, size);
        given(roomRepository.findByRoomId(roomId)).willReturn(Optional.empty());
        //when
        configurationService.setSize(setGameSize);
        //then
    }

    @Test
    public void setSizeShouldSaveProperRoom() {
        //given
        SetGameSize setGameSize = new SetGameSize(roomId, playerId, size);
        String enemyId = "testEnemy";
        Room room = Room.builder()
                .roomId(roomId)
                .players(List.of(playerId, enemyId))
                .build();
        given(roomRepository.findByRoomId(roomId)).willReturn(Optional.of(room));
        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        //when
        configurationService.setSize(setGameSize);
        verify(roomRepository).save(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, room);
    }

    @Test
    public void setSizeShouldNotifyFrontEnd2Times() {
        //given
        SetGameSize setGameSize = new SetGameSize(roomId, playerId, size);
        String enemyId = "testEnemy";
        Room room = Room.builder()
                .roomId(roomId)
                .players(List.of(playerId, enemyId))
                .build();
        given(roomRepository.findByRoomId(roomId)).willReturn(Optional.of(room));
        //when
        configurationService.setSize(setGameSize);
        verify(websocketService, times(2)).notifyFrontEnd(anyString(), any());
        //then
    }
}
