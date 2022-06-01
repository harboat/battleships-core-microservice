package com.github.harboat.core.games;

import com.github.harboat.clients.exceptions.BadRequest;
import com.github.harboat.clients.exceptions.ResourceNotFound;
import com.github.harboat.clients.game.GameCreated;
import com.github.harboat.clients.game.PlayerWon;
import com.github.harboat.clients.notification.EventType;
import com.github.harboat.core.websocket.Event;
import com.github.harboat.core.websocket.WebsocketService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.testng.Assert.assertEquals;

@Listeners({MockitoTestNGListener.class})
public class GameServiceTest {

    @Mock
    private GameRepository repository;
    @Mock
    private WebsocketService websocketService;
    @Mock
    private GameService service;
    private String playerId;
    private String enemyId;
    private String gameId;

    @BeforeMethod
    public void setUp() {
        service = new GameService(repository, websocketService);
        playerId = "testPlayer";
        gameId = "test";
        enemyId = "testEnemy";
    }

    @Test
    public void shouldCreateWithProperPlayers() {
        //given
        GameCreated gameCreated = new GameCreated(gameId, List.of(playerId, enemyId), playerId);
        ArgumentCaptor<String> playerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        given(repository.save(any())).willReturn(null);
        //when
        service.create(gameCreated);
        verify(websocketService, times(2)).notifyFrontEnd(playerCaptor.capture(), eventCaptor.capture());
        var actual = playerCaptor.getAllValues();
        //then
        assertEquals(actual, List.of(playerId,enemyId));
    }

    @Test
    public void shouldCreateWithProperEventType() {
        //given
        GameCreated gameCreated = new GameCreated(gameId, List.of(playerId, enemyId), playerId);
        ArgumentCaptor<String> playerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        given(repository.save(any())).willReturn(null);
        //when
        service.create(gameCreated);
        verify(websocketService, times(2)).notifyFrontEnd(playerCaptor.capture(), eventCaptor.capture());
        var actual = eventCaptor.getAllValues();
        //then
        assertEquals(actual.stream()
                .map(Event::getEventType)
                .filter(e -> e == EventType.GAME_STARTED)
                .count(), 2);
    }

    @Test
    public void shouldCreateWithProperGameCreated() {
        //given
        GameCreated gameCreated = new GameCreated(gameId, List.of(playerId, enemyId), playerId);
        ArgumentCaptor<String> playerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        given(repository.save(any())).willReturn(null);
        //when
        service.create(gameCreated);
        verify(websocketService, times(2)).notifyFrontEnd(playerCaptor.capture(), eventCaptor.capture());
        var actual = eventCaptor.getAllValues();
        //then
        assertEquals(actual.stream()
                .map(Event::getContent)
                .filter(e -> e == gameCreated)
                .count(), 2);
    }

    @Test(expectedExceptions = ResourceNotFound.class, expectedExceptionsMessageRegExp = "Game not found!")
    public void forfeitShouldThrowWhenThereIsNoGameWithThisId() {
        //given
        given(repository.findByGameId(gameId)).willReturn(Optional.empty());
        //when
        service.forfeit(playerId, gameId);
        //then
    }

    @Test(expectedExceptions = BadRequest.class, expectedExceptionsMessageRegExp = "You are not in this game")
    public void forfeitShouldThrowWhenPlayerIsNotInThisGame() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of())
                .build();
        given(repository.findByGameId(gameId)).willReturn(Optional.of(game));
        //when
        service.forfeit(playerId, gameId);
        //then
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void forfeitShouldThrowWhenThereIsNoEnemy() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .build();
        given(repository.findByGameId(gameId)).willReturn(Optional.of(game));
        //when
        service.forfeit(playerId, gameId);
        //then
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void endGameShouldThrowWhenThereIsNoGameWithThisId() {
        //given
        PlayerWon playerWon = new PlayerWon(gameId, playerId);
        given(repository.findByGameId(gameId)).willReturn(Optional.empty());
        //when
        service.endGame(playerWon);
        //then
    }

    @Test
    public void shouldEndGameWithProperPlayers() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .build();
        PlayerWon playerWon = new PlayerWon(gameId, playerId);
        given(repository.findByGameId(gameId)).willReturn(Optional.of(game));
        ArgumentCaptor<String> playerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.endGame(playerWon);
        verify(websocketService).notifyFrontEnd(playerCaptor.capture(), eventCaptor.capture());
        var actual = playerCaptor.getAllValues();
        //then
        assertEquals(actual, game.getPlayers());
    }

    @Test
    public void shouldEndGameWithProperEventTypes() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .build();
        PlayerWon playerWon = new PlayerWon(gameId, playerId);
        given(repository.findByGameId(gameId)).willReturn(Optional.of(game));
        ArgumentCaptor<String> playerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.endGame(playerWon);
        verify(websocketService).notifyFrontEnd(playerCaptor.capture(), eventCaptor.capture());
        var actual = eventCaptor.getAllValues();
        //then
        assertEquals(actual.stream()
                .map(Event::getEventType)
                .filter(e -> e == EventType.GAME_END)
                .count(), game.getPlayers().size());
    }

    @Test
    public void shouldEndGameWithProperWinner() {
        //given
        Game game = Game.builder()
                .gameId(gameId)
                .players(List.of(playerId))
                .build();
        PlayerWon playerWon = new PlayerWon(gameId, playerId);
        given(repository.findByGameId(gameId)).willReturn(Optional.of(game));
        ArgumentCaptor<String> playerCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        //when
        service.endGame(playerWon);
        verify(websocketService).notifyFrontEnd(playerCaptor.capture(), eventCaptor.capture());
        var actual = eventCaptor.getAllValues();
        //then
        assertEquals(actual.stream()
                .map(Event::getContent)
                .filter(e -> e.equals(new GameEnded(playerId)))
                .count(), game.getPlayers().size());
    }
}
