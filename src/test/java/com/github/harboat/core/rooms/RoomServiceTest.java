package com.github.harboat.core.rooms;

import com.github.harboat.clients.notification.EventType;
import com.github.harboat.clients.rooms.*;
import com.github.harboat.core.websocket.Event;
import com.github.harboat.core.websocket.WebsocketService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.testng.Assert.*;
import static org.mockito.BDDMockito.*;

@Listeners({MockitoTestNGListener.class})
public class RoomServiceTest {

    @Mock
    private RoomRepository repository;
    @Mock
    private RoomQueueProducer roomQueueProducer;
    @Mock
    private WebsocketService websocketService;
    private RoomService service;
    private String playerId;
    private String roomId;

    @BeforeMethod
    public void setUp() {
        service = new RoomService(repository, roomQueueProducer, websocketService);
        playerId = "testPlayer";
        roomId = "testRoom";
    }

    @Test
    public void shouldCreateWithProperPlayerId() {
        //given
        ArgumentCaptor<RoomCreate> captor = ArgumentCaptor.forClass(RoomCreate.class);
        //when
        service.create(playerId);
        verify(roomQueueProducer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.playerId(), playerId);
    }

    @Test
    public void shouldCreateWithProperOwnerId() {
        //given
        RoomCreated roomCreated = new RoomCreated(roomId, playerId);
        given(repository.save(any())).willReturn(null);
        ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.create(roomCreated);
        verify(websocketService).notifyFrontEnd(ownerCaptor.capture(), eventCaptor.capture());
        var actual = ownerCaptor.getValue();
        //then
        assertEquals(actual, playerId);
    }

    @Test
    public void shouldCreateWithProperEventType() {
        //given
        RoomCreated roomCreated = new RoomCreated(roomId, playerId);
        given(repository.save(any())).willReturn(null);
        ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.create(roomCreated);
        verify(websocketService).notifyFrontEnd(ownerCaptor.capture(), eventCaptor.capture());
        var actual = eventCaptor.getValue();
        //then
        assertEquals(actual.getEventType(), EventType.ROOM_CREATED);
    }

    @Test
    public void shouldCreateWithProperEventContent() {
        //given
        RoomCreated roomCreated = new RoomCreated(roomId, playerId);
        given(repository.save(any())).willReturn(null);
        ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.create(roomCreated);
        verify(websocketService).notifyFrontEnd(ownerCaptor.capture(), eventCaptor.capture());
        var actual = eventCaptor.getValue();
        //then
        assertEquals(actual.getContent(), roomCreated);
    }

    @Test
    public void changeReadinessShouldSendProperRoomId() {
        //given
        ArgumentCaptor<ChangePlayerReadiness> captor = ArgumentCaptor.forClass(ChangePlayerReadiness.class);
        //when
        service.changeReadiness(roomId, playerId);
        verify(roomQueueProducer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.roomId(), roomId);
    }

    @Test
    public void changeReadinessShouldSendProperPlayerId() {
        //given
        ArgumentCaptor<ChangePlayerReadiness> captor = ArgumentCaptor.forClass(ChangePlayerReadiness.class);
        //when
        service.changeReadiness(roomId, playerId);
        verify(roomQueueProducer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.playerId(), playerId);
    }

    @Test
    public void startShouldSendMarkStartWithProperRoomId() {
        //given
        ArgumentCaptor<MarkStart> captor = ArgumentCaptor.forClass(MarkStart.class);
        //when
        service.start(roomId, playerId);
        verify(roomQueueProducer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.roomId(), roomId);
    }

    @Test
    public void startShouldSendMarkStartWithProperPlayerId() {
        //given
        ArgumentCaptor<MarkStart> captor = ArgumentCaptor.forClass(MarkStart.class);
        //when
        service.start(roomId, playerId);
        verify(roomQueueProducer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.playerId(), playerId);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void startFromRoomGameStartShouldThrowIfThereIsNoRoomWithThisId() {
        //given
        RoomGameStart roomGameStart = new RoomGameStart(roomId);
        given(repository.findByRoomId(roomId)).willReturn(Optional.empty());
        //when
        service.start(roomGameStart);
        //then
    }

    @Test
    public void startFromRoomGameStartShouldNotifyFrontEndWithProperPlayerIds() {
        //given
        RoomGameStart roomGameStart = new RoomGameStart(roomId);
        Room room = Room.builder()
                .roomId(roomId)
                .players(List.of(playerId, playerId))
                .build();
        given(repository.findByRoomId(roomId)).willReturn(Optional.of(room));
        ArgumentCaptor<String> playerIdsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventsCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.start(roomGameStart);
        verify(websocketService, times(2)).notifyFrontEnd(playerIdsCaptor.capture(), eventsCaptor.capture());
        var actual = playerIdsCaptor.getAllValues();
        //then
        assertEquals(actual, List.of(playerId, playerId));
    }

    @Test
    public void startFromRoomGameStartShouldNotifyFrontEndWithProperEventTypes() {
        //given
        RoomGameStart roomGameStart = new RoomGameStart(roomId);
        Room room = Room.builder()
                .roomId(roomId)
                .players(List.of(playerId, playerId))
                .build();
        given(repository.findByRoomId(roomId)).willReturn(Optional.of(room));
        ArgumentCaptor<String> playerIdsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventsCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.start(roomGameStart);
        verify(websocketService, times(2)).notifyFrontEnd(playerIdsCaptor.capture(), eventsCaptor.capture());
        var actual = eventsCaptor.getAllValues();
        //then
        assertEquals(actual.stream()
                .map(Event::getEventType)
                .collect(Collectors.toList()), List.of(EventType.ROOM_GAME_STARTED, EventType.ROOM_GAME_STARTED));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void playerJoinShouldThrowWhenThereIsNoRoomWithThisId() {
        //given
        RoomPlayerJoined roomPlayerJoined = new RoomPlayerJoined(roomId, playerId);
        given(repository.findByRoomId(roomId)).willReturn(Optional.empty());
        //when
        service.playerJoin(roomPlayerJoined);
        //then
    }

    @Test
    public void playerJoinShouldNotifyFrontEndWithProperPlayerIds() {
        //given
        ArgumentCaptor<String> playerIdsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventsCaptor = ArgumentCaptor.forClass(Event.class);
        RoomPlayerJoined roomPlayerJoined = new RoomPlayerJoined(roomId, playerId);
        Room room = Room.builder()
                .roomId(roomId)
                .players(new ArrayList<>())
                .build();
        given(repository.findByRoomId(roomId)).willReturn(Optional.of(room));
        given(repository.save(any())).willReturn(null);
        //when
        service.playerJoin(roomPlayerJoined);
        verify(websocketService, times(2)).notifyFrontEnd(playerIdsCaptor.capture(), eventsCaptor.capture());
        var actual = playerIdsCaptor.getAllValues();
        //then
        assertEquals(actual, List.of(playerId, playerId));
    }

    @Test
    public void playerJoinShouldNotifyFrontEndWithProperEventTypes() {
        //given
        ArgumentCaptor<String> playerIdsCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventsCaptor = ArgumentCaptor.forClass(Event.class);
        RoomPlayerJoined roomPlayerJoined = new RoomPlayerJoined(roomId, playerId);
        Room room = Room.builder()
                .roomId(roomId)
                .players(new ArrayList<>())
                .build();
        given(repository.findByRoomId(roomId)).willReturn(Optional.of(room));
        given(repository.save(any())).willReturn(null);
        //when
        service.playerJoin(roomPlayerJoined);
        verify(websocketService, times(2)).notifyFrontEnd(playerIdsCaptor.capture(), eventsCaptor.capture());
        var actual = eventsCaptor.getAllValues();
        //then
        assertEquals(actual.stream()
                .map(Event::getEventType)
                .collect(Collectors.toList()), List.of(EventType.ROOM_JOINED, EventType.BOARD_CREATED));
    }

    @Test
    public void joinShouldSendRoomPlayerJoinWithProperRoomId() {
        //given
        ArgumentCaptor<RoomPlayerJoin> captor = ArgumentCaptor.forClass(RoomPlayerJoin.class);
        //when
        service.join(roomId, playerId);
        verify(roomQueueProducer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.roomId(), roomId);
    }

    @Test
    public void joinShouldSendRoomPlayerJoinWithProperPlayerId() {
        //given
        ArgumentCaptor<RoomPlayerJoin> captor = ArgumentCaptor.forClass(RoomPlayerJoin.class);
        //when
        service.join(roomId, playerId);
        verify(roomQueueProducer).send(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.playerId(), playerId);
    }
}