package com.github.harboat.core.games;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Listeners({MockitoTestNGListener.class})
public class GameUtilityTest {

    @Mock
    private GameRepository repository;
    private GameUtility gameUtility;

    @BeforeMethod
    public void setUp() {
        gameUtility = new GameUtility(repository);
    }

    @Test
    public void shouldNotFindByGameId() {
        //given
        given(repository.findByGameId("test")).willReturn(Optional.empty());
        //when
        Optional<Game> actual = gameUtility.findByGameId("test");
        //then
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldFindByGameId() {
        //given
        Game game = Game.builder()
                .gameId("test")
                .build();
        given(repository.findByGameId("test")).willReturn(Optional.of(game));
        //when
        Optional<Game> actual = gameUtility.findByGameId("test");
        //then
        assertEquals(actual.get(), game);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void switchTurnShouldThrow() {
        //given
        given(repository.findByGameId("test")).willReturn(Optional.empty());
        //when
        gameUtility.switchTurn("test", "testEnemy");
        //then
    }

    @Test
    public void shouldSwitchTurn() {
        //given
        Game game = Game.builder().gameId("test").build();
        given(repository.findByGameId("test")).willReturn(Optional.of(game));
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        //when
        gameUtility.switchTurn("test", "testEnemy");
        verify(repository).save(captor.capture());
        var actual = captor.getValue();
        //then
        assertEquals(actual.getPlayerTurn(), "testEnemy");
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void getEnemyIdShouldThrow() {
        //given
        given(repository.findByGameId("test")).willReturn(Optional.empty());
        //when
        gameUtility.getEnemyId("test", "testPlayer");
        //then
    }

    @Test
    public void shouldGetEnemyId() {
        //given
        Game game = Game.builder()
                .gameId("test")
                .players(List.of("testPlayer", "testEnemy"))
                .build();
        given(repository.findByGameId("test")).willReturn(Optional.of(game));
        //when
        var actual = gameUtility.getEnemyId("test", "testPlayer");
        //then
        assertEquals(actual, "testEnemy");
    }
    @Test
    public void shouldGetNotStartedGamesIdsForUser() {
        //given
        Game game = Game.builder()
                .gameId("test")
                .build();
        given(repository.findGamesByOwnerIdAndNotStarted("testPlayer")).willReturn(List.of(game));
        //when
        var actual = gameUtility.getNotStartedGamesIdsForUser("testPlayer");
        //then
        assertEquals(actual, List.of("test"));
    }
}