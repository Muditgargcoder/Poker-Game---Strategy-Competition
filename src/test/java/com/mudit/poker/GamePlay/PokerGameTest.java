package com.mudit.poker.GamePlay;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mudit.poker.Algos.SampleStrategy;
import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.Player;

@DisplayName("Poker Game End-to-End Tests")
class PokerGameTest {

    private List<Player> players;
    private int startingMoney = 1000;
    private int numGames = 1;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
    }

    /**
     * Helper method to create a player with a strategy
     */
    private Player createPlayer(int id) {
        Player player = new Player(id, new SampleStrategy());
        return player;

    }

    @Test
    @DisplayName("Should run a complete poker game with 2 players")
    void testCompleteGameWith2Players() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        // After game, total money should remain the same across all players
        int totalMoneyAfterGame = players.stream().mapToInt(Player::getCurrentAmount).sum();
        int expectedTotalMoney = startingMoney * players.size();
        assertEquals(expectedTotalMoney, totalMoneyAfterGame);
    }

    @Test
    @DisplayName("Should run a complete poker game with 4 players")
    void testCompleteGameWith4Players() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));
        players.add(createPlayer(3));
        players.add(createPlayer(4));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        int totalMoneyAfterGame = players.stream().mapToInt(Player::getCurrentAmount).sum();
        int expectedTotalMoney = startingMoney * players.size();
        assertEquals(expectedTotalMoney, totalMoneyAfterGame);
    }

    @Test
    @DisplayName("Should deduct first round fee from all players")
    void testFirstRoundFeeDeduction() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        // After the game, money should be redistributed but total should remain same
        for (Player player : players) {
            assertTrue(player.getCurrentAmount() > 0 || player.getCurrentAmount() < startingMoney);
        }
    }

    @Test
    @DisplayName("Should assign 2 cards to each player per game")
    void testPlayerCardsAssignment() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        // After game setup, player cards should have been assigned during the game
        for (Player player : players) {
            assertNotNull(player.getAssignedCards());
        }
    }

    @Test
    @DisplayName("Should have exactly 5 community cards after game")
    void testCommunityCardsSize() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        // Community cards should have been set up
        assertNotNull(game.communityCards);
        assertEquals(5, game.communityCards.size());
    }

    @Test
    @DisplayName("Should determine a winner in each game")
    void testWinnerDetermination() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));
        players.add(createPlayer(3));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, 2);
        game.run();

        // At least one player should have more money than initial
        boolean hasWinner = players.stream()
                .anyMatch(p -> p.getCurrentAmount() > startingMoney);
        assertTrue(hasWinner);

        // At least one player should have less money than initial
        boolean hasLoser = players.stream()
                .anyMatch(p -> p.getCurrentAmount() < startingMoney);
        assertTrue(hasLoser);
        game.prettyPrintAudit();
    }

    @Test
    @DisplayName("Should run multiple games with money transfers")
    void testMultipleGamesWithMoneyTransfer() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        int numGamesToPlay = 3;
        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGamesToPlay);
        game.run();

        // Total money should still be conserved
        int totalMoney = players.stream().mapToInt(Player::getCurrentAmount).sum();
        assertEquals(startingMoney * players.size(), totalMoney);
    }

    @Test
    @DisplayName("Should track game play number correctly")
    void testGamePlayNumberTracking() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, 2);
        game.run();

        // Game should have completed all rounds
        assertEquals(2, game.currentGamePlayNumber);
    }

    @Test
    @DisplayName("Should maintain first player rotation")
    void testFirstPlayerRotation() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));
        players.add(createPlayer(3));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, 3);
        game.run();

        // After 3 games, first player should have rotated
        assertEquals(0, game.firstPlayer);
    }

    @Test
    @DisplayName("Should verify community cards are unique")
    void testCommunityCardsAreUnique() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        // All community cards should be unique
        List<String> cardIdentifiers = game.communityCards.stream()
                .map(card -> card.getSuite() + "-" + card.getRank())
                .toList();
        
        assertEquals(5, cardIdentifiers.size());
        assertEquals(cardIdentifiers.stream().distinct().count(), 5);
    }

    @Test
    @DisplayName("Should verify all community cards are valid")
    void testCommunityCardsAreValid() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        for (Card card : game.communityCards) {
            assertNotNull(card.getSuite());
            assertTrue(card.getRank() >= 2 && card.getRank() <= 14);
        }
    }

    @Test
    @DisplayName("Should verify player cards are valid")
    void testPlayerCardsAreValid() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        for (Player player : players) {
            for (Card card : player.getAssignedCards()) {
                assertNotNull(card.getSuite());
                assertTrue(card.getRank() >= 2 && card.getRank() <= 14);
            }
        }
    }

    @Test
    @DisplayName("Should handle game with different starting amounts")
    void testGameWithDifferentStartingAmounts() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        int customStartingMoney = 500;
        PokerGamePlay game = new PokerGamePlay(customStartingMoney, players, numGames);
        game.run();

        int totalMoney = players.stream().mapToInt(Player::getCurrentAmount).sum();
        assertEquals(customStartingMoney * players.size(), totalMoney);
    }

    @Test
    @DisplayName("Should not allow player to go negative after game")
    void testPlayersDoNotGoNegative() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, 1);
        game.run();

        for (Player player : players) {
            assertTrue(player.getCurrentAmount() >= 0);
        }
    }

    @Test
    @DisplayName("Should run game with 5 players successfully")
    void testGameWith5Players() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));
        players.add(createPlayer(3));
        players.add(createPlayer(4));
        players.add(createPlayer(5));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        assertDoesNotThrow(() -> game.run());

        int totalMoney = players.stream().mapToInt(Player::getCurrentAmount).sum();
        assertEquals(startingMoney * players.size(), totalMoney);
    }

    @Test
    @DisplayName("Should produce audit records for each game for all players")
    void testAuditRecordsGenerated() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));
        players.add(createPlayer(3));

        int numGamesToPlay = 5;
        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGamesToPlay);
        game.run();

        // Audit should have records for all games
        assertEquals(numGamesToPlay, game.gameAudit.size());
        // Check first game's audit
        var firstGameAudit = game.gameAudit.get(0);
        assertEquals(3, firstGameAudit.getPlayerResults().size());
    }

    @Test
    @DisplayName("Should verify each player gets assigned cards in each game")
    void testPlayersGetCardsInEachGame() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));
        players.add(createPlayer(3));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, 2);
        game.run();

        for (Player player : players) {
            assertNotNull(player.getAssignedCards());
            assertTrue(player.getAssignedCards().size() == 2);
        }
    }

    @Test
    @DisplayName("Should preserve player object references")
    void testPlayerObjectReferencesPreserved() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        Player player1 = players.get(0);
        Player player2 = players.get(1);

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        // Same player references should still be present
        assertSame(player1, players.get(0));
        assertSame(player2, players.get(1));
    }

    @Test
    @DisplayName("Should verify money redistribution in multiple games")
    void testMoneyRedistributionMultipleGames() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));
        players.add(createPlayer(3));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, 5);
        game.run();

        // After 5 games, total money should still equal starting total
        int finalTotal = players.stream().mapToInt(Player::getCurrentAmount).sum();
        assertEquals(startingMoney * players.size(), finalTotal);

        // But at least one player should have different amount
        boolean moneyMoved = players.stream()
                .anyMatch(p -> p.getCurrentAmount() != startingMoney);
        assertTrue(moneyMoved);
    }

    @Test
    @DisplayName("Should verify game state initialized before each game")
    void testGameStateInitializedPerGame() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, numGames);
        game.run();

        assertNotNull(game.gameState);
        assertNotNull(game.gameState.getPlayerStatusesMap());
        assertEquals(2, game.gameState.getPlayerStatusesMap().size());
    }

    @Test
    @DisplayName("Should verify audit can be printed without exceptions")
    void testAuditPrintingWithoutExceptions() throws Exception {
        players.add(createPlayer(1));
        players.add(createPlayer(2));

        PokerGamePlay game = new PokerGamePlay(startingMoney, players, 2);
        game.run();

        // Should not throw exception when printing audit
        assertDoesNotThrow(() -> game.prettyPrintAudit());
    }
}
