package com.github.harboat.core.shot;

import com.github.harboat.clients.exceptions.BadRequest;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.clients.game.Cell;
import com.github.harboat.clients.game.NukeShotRequest;
import com.github.harboat.clients.game.ShotRequest;
import com.github.harboat.clients.game.ShotResponse;
import com.github.harboat.core.GameQueueProducer;
import com.github.harboat.core.games.Game;
import com.github.harboat.core.games.GameUtility;
import com.github.harboat.core.websocket.WebsocketService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;
import static org.mockito.BDDMockito.*;

@Listeners({MockitoTestNGListener.class})
public class ShotServiceTest {

    @Mock
    private GameQueueProducer producer;
    @Mock
    private GameUtility gameUtility;
    @Mock
    private WebsocketService websocketService;
    private ShotService service;
    private String gameId;
    private String playerId;
    private int cellId;

    @BeforeMethod
    public void setUp() {
        service = new ShotService(producer, gameUtility, websocketService);
        gameId = "test";
        playerId = "testPlayer";
        cellId = 1;
    }

    @Test(expectedExceptions = ResourceNotFound.class, expectedExceptionsMessageRegExp = "Game not found!")
    public void takeAShotShouldThrowWhenThereIsNoGameWithThisId() {
        //given
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.empty());
        //when
        service.takeAShoot(gameId, playerId, cellId);
        //then
    }

    @Test(expectedExceptions = BadRequest.class, expectedExceptionsMessageRegExp = "Player is not in the game!")
    public void takeAShotShouldThrowWhenPlayerIsNotInTheGame() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(new ArrayList<>())
                .build();
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.of(game));
        //when
        service.takeAShoot(gameId, playerId, cellId);
        //then
    }

    @Test(expectedExceptions = BadRequest.class, expectedExceptionsMessageRegExp = "It is not your turn!")
    public void takeAShotShouldThrowWhenItsNotPlayersTurn() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .playerTurn("")
                .build();
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.of(game));
        //when
        service.takeAShoot(gameId, playerId, cellId);
        //then
    }

    @Test
    public void takeAShotShouldSendProperShotRequest() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .playerTurn(playerId)
                .build();
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.of(game));
        ArgumentCaptor<ShotRequest> captor = ArgumentCaptor.forClass(ShotRequest.class);
        //when
        service.takeAShoot(gameId, playerId, cellId);
        verify(producer).sendRequest(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, new ShotRequest(gameId, playerId, cellId));
    }

    @Test(expectedExceptions = ResourceNotFound.class, expectedExceptionsMessageRegExp = "Game not found!")
    public void takeANukeShotShouldThrowWhenThereIsNoGameWithThisId() {
        //given
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.empty());
        //when
        service.takeANukeShoot(gameId, playerId, cellId);
        //then
    }

    @Test(expectedExceptions = BadRequest.class, expectedExceptionsMessageRegExp = "Player is not in the game!")
    public void takeANukeShotShouldThrowWhenPlayerIsNotInTheGame() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(new ArrayList<>())
                .build();
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.of(game));
        //when
        service.takeANukeShoot(gameId, playerId, cellId);
        //then
    }

    @Test(expectedExceptions = BadRequest.class, expectedExceptionsMessageRegExp = "It is not your turn!")
    public void takeANukeShotShouldThrowWhenItsNotPlayersTurn() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .playerTurn("")
                .build();
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.of(game));
        //when
        service.takeANukeShoot(gameId, playerId, cellId);
        //then
    }

    @Test
    public void takeANukeShotShouldSendProperShotRequest() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .playerTurn(playerId)
                .build();
        given(gameUtility.findByGameId(gameId)).willReturn(Optional.of(game));
        ArgumentCaptor<NukeShotRequest> captor = ArgumentCaptor.forClass(NukeShotRequest.class);
        //when
        service.takeANukeShoot(gameId, playerId, cellId);
        verify(producer).sendRequest(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual, new NukeShotRequest(gameId, playerId, cellId));
    }

    @Test
    public void takeAShotShouldSwitchTurnAfterSimpleShot() {
        //given
        String enemyId = "testEnemy";
        ShotResponse shotResponse = new ShotResponse(gameId, playerId, List.of(new Cell(3, false)));
        given(gameUtility.getEnemyId(gameId, playerId)).willReturn(enemyId);
        //when
        service.takeAShoot(shotResponse);
        //then
        verify(gameUtility, times(1)).switchTurn(gameId, enemyId);
    }

    @Test
    public void takeAShotShouldSwitchTurnAfterNukeShot() {
        //given
        String enemyId = "testEnemy";
        List<Cell> cells = new ArrayList<>() {{
            add(new Cell(1, false));
            add(new Cell(2, false));
            add(new Cell(3, false));
            add(new Cell(4, false));
            add(new Cell(5, false));
            add(new Cell(6, false));
            add(new Cell(7, false));
            add(new Cell(8, false));
            add(new Cell(9, false));
        }};
        ShotResponse shotResponse = new ShotResponse(gameId, playerId, cells);
        given(gameUtility.getEnemyId(gameId, playerId)).willReturn(enemyId);
        //when
        service.takeAShoot(shotResponse);
        //then
        verify(gameUtility, times(1)).switchTurn(gameId, enemyId);
    }

    @Test
    public void takeAShotShouldNotifyFrontEnd2Times() {
        //given
        String enemyId = "testEnemy";
        ShotResponse shotResponse = new ShotResponse(gameId, playerId, List.of(new Cell(3, false)));
        given(gameUtility.getEnemyId(gameId, playerId)).willReturn(enemyId);
        //when
        service.takeAShoot(shotResponse);
        //then
        verify(websocketService, times(2)).notifyFrontEnd(anyString(), any());
    }

}