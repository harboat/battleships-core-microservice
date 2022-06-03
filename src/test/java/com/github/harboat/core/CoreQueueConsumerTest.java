package com.github.harboat.core;

import com.github.harboat.clients.configuration.SetGameSize;
import com.github.harboat.clients.game.*;
import com.github.harboat.clients.rooms.RoomCreated;
import com.github.harboat.clients.rooms.RoomGameStart;
import com.github.harboat.clients.rooms.RoomPlayerJoined;
import com.github.harboat.core.configuration.ConfigurationService;
import com.github.harboat.core.games.GameService;
import com.github.harboat.core.rooms.RoomService;
import com.github.harboat.core.shot.ShotService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import static org.testng.Assert.*;

import static org.mockito.BDDMockito.*;
import org.testng.annotations.Test;

import java.util.ArrayList;

@Listeners(MockitoTestNGListener.class)
public class CoreQueueConsumerTest {

    @Mock
    private RoomService roomService;
    @Mock
    private ConfigurationService configurationService;
    @Mock
    private GameService gameService;
    @Mock
    private ShotService shotService;
    private CoreQueueConsumer coreQueueConsumer;
    private String roomId;
    private String playerId;

    @BeforeMethod
    public void setUp() {
        coreQueueConsumer = new CoreQueueConsumer(roomService, configurationService, gameService, shotService);
        roomId = "testRoom";
        playerId = "testPLayer";
    }

    @Test
    public void consumeShouldCreateWithProperRoomCreated() {
        //given
        RoomCreated roomCreated = new RoomCreated(roomId, playerId);
        ArgumentCaptor<RoomCreated> captor = ArgumentCaptor.forClass(RoomCreated.class);
        //when
        coreQueueConsumer.consume(roomCreated);
        verify(roomService).create(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, roomCreated);
    }

    @Test
    public void consumeShouldStartWithProperRoomGameStart() {
        //given
        RoomGameStart room = new RoomGameStart(roomId);
        ArgumentCaptor<RoomGameStart> captor = ArgumentCaptor.forClass(RoomGameStart.class);
        //when
        coreQueueConsumer.consume(room);
        verify(roomService).start(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, room);
    }

    @Test
    public void consumeShouldSetSizeWithProperSetGameSize() {
        //given
        Size size = new Size(10,10);
        SetGameSize setGameSize = new SetGameSize(roomId, playerId, size);
        ArgumentCaptor<SetGameSize> captor = ArgumentCaptor.forClass(SetGameSize.class);
        //when
        coreQueueConsumer.consume(setGameSize);
        verify(configurationService).setSize(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, setGameSize);
    }

    @Test
    public void consumeShouldPlayerJoinWithProperRoomPlayerJoined() {
        //given
        RoomPlayerJoined playerJoined = new RoomPlayerJoined(roomId, playerId);
        ArgumentCaptor<RoomPlayerJoined> captor = ArgumentCaptor.forClass(RoomPlayerJoined.class);
        //when
        coreQueueConsumer.consume(playerJoined);
        verify(roomService).playerJoin(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, playerJoined);
    }

    @Test
    public void consumeShouldCreateWithProperGameCreated() {
        //given
        GameCreated gameCreated = new GameCreated("testGame", new ArrayList<String>(), playerId);
        ArgumentCaptor<GameCreated> captor = ArgumentCaptor.forClass(GameCreated.class);
        //when
        coreQueueConsumer.consume(gameCreated);
        verify(gameService).create(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, gameCreated);
    }

    @Test
    public void consumeShouldTakeAShotWithProperShotResponse() {
        //given
        ShotResponse shotResponse = new ShotResponse("testGame", playerId,new ArrayList<Cell>());
        ArgumentCaptor<ShotResponse> captor = ArgumentCaptor.forClass(ShotResponse.class);
        //when
        coreQueueConsumer.consume(shotResponse);
        verify(shotService).takeAShoot(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, shotResponse);
    }

    @Test
    public void consumeShouldEndGameWithProperPlayerWon() {
        //given
        PlayerWon playerWon = new PlayerWon("testGame", playerId);
        ArgumentCaptor<PlayerWon> captor = ArgumentCaptor.forClass(PlayerWon.class);
        //when
        coreQueueConsumer.consume(playerWon);
        verify(gameService).endGame(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, playerWon);
    }
}