package com.mudit.poker.Combinations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.CombinationResult;
import com.mudit.poker.Pojos.Suite;
import com.mudit.poker.Pojos.Player;

@DisplayName("CombinationChecker Tests")
class CombinationCalculatorTest {

    private List<Card> cards;

    @BeforeEach
    void setUp() {
        cards = new ArrayList<>();
    }

    // Helper method to easily create cards
    private Card createCard(Suite suite, int rank) {
        return new Card(suite, rank);
    }

    @Test
    @DisplayName("Should detect FOUR_OF_A_KIND")
    void testGetCardCombination_FOUR_OF_A_KIND() throws Exception {
        // Four Aces and three other cards
        cards.add(createCard(Suite.Diamond, 14)); // A
        cards.add(createCard(Suite.Heart, 14));   // A
        cards.add(createCard(Suite.Spade, 14));   // A
        cards.add(createCard(Suite.Club, 14));    // A
        cards.add(createCard(Suite.Diamond, 13)); // K
        cards.add(createCard(Suite.Heart, 12));   // Q
        cards.add(createCard(Suite.Spade, 11));   // J

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.FOUR_OF_A_KIND, result.getCombination());
        assertEquals(5, result.getHighCards().size());
        // Should contain 4 Aces and 1 King
        long aceCount = result.getHighCards().stream().filter(c -> c.getRank() == 14).count();
        assertEquals(4, aceCount);
    }

    @Test
    @DisplayName("Should detect FULL_HOUSE")
    void testGetCardCombination_FULL_HOUSE() throws Exception {
        // Three Kings and two Queens
        cards.add(createCard(Suite.Diamond, 13)); // K
        cards.add(createCard(Suite.Heart, 13));   // K
        cards.add(createCard(Suite.Spade, 13));   // K
        cards.add(createCard(Suite.Diamond, 12)); // Q
        cards.add(createCard(Suite.Heart, 12));   // Q
        cards.add(createCard(Suite.Spade, 10));   // 10
        cards.add(createCard(Suite.Club, 9));     // 9

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.FULL_HOUSE, result.getCombination());
        assertEquals(5, result.getHighCards().size());
    }

    @Test
    @DisplayName("Should detect FLUSH")
    void testGetCardCombination_FLUSH() throws Exception {
        // Five hearts
        cards.add(createCard(Suite.Heart, 14)); // A
        cards.add(createCard(Suite.Heart, 13)); // K
        cards.add(createCard(Suite.Heart, 12)); // Q
        cards.add(createCard(Suite.Heart, 11)); // J
        cards.add(createCard(Suite.Heart, 10)); // 10
        cards.add(createCard(Suite.Diamond, 9));
        cards.add(createCard(Suite.Spade, 8));

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.FLUSH, result.getCombination());
        assertEquals(5, result.getHighCards().size());
        // All cards should be hearts
        assertTrue(result.getHighCards().stream().allMatch(c -> c.getSuite() == Suite.Heart));
    }

    @Test
    @DisplayName("Should detect STRAIGHT")
    void testGetCardCombination_STRAIGHT() throws Exception {
        // 5-6-7-8-9 straight
        cards.add(createCard(Suite.Diamond, 9));
        cards.add(createCard(Suite.Heart, 8));
        cards.add(createCard(Suite.Spade, 7));
        cards.add(createCard(Suite.Club, 6));
        cards.add(createCard(Suite.Diamond, 5));
        cards.add(createCard(Suite.Heart, 3));
        cards.add(createCard(Suite.Spade, 2));

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.STRAIGHT, result.getCombination());
        assertEquals(5, result.getHighCards().size());
    }

    @Test
    @DisplayName("Should detect THREE_OF_A_KIND")
    void testGetCardCombination_THREE_OF_A_KIND() throws Exception {
        // Three Aces
        cards.add(createCard(Suite.Diamond, 14)); // A
        cards.add(createCard(Suite.Heart, 8));    // 8
        cards.add(createCard(Suite.Spade, 14));   // A
        cards.add(createCard(Suite.Diamond, 13)); // K
        cards.add(createCard(Suite.Heart, 12));   // Q
        cards.add(createCard(Suite.Spade, 14));   // A
        cards.add(createCard(Suite.Club, 10));    // 10

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.THREE_OF_A_KIND, result.getCombination());
        assertEquals(5, result.getHighCards().size());
    }

    @Test
    @DisplayName("Should detect TWO_PAIR")
    void testGetCardCombination_TWO_PAIR() throws Exception {
        // Two Aces and Two Kings
        cards.add(createCard(Suite.Diamond, 14)); // A
        cards.add(createCard(Suite.Heart, 14));   // A
        cards.add(createCard(Suite.Spade, 13));   // K
        cards.add(createCard(Suite.Club, 13));    // K
        cards.add(createCard(Suite.Diamond, 12)); // Q
        cards.add(createCard(Suite.Heart, 11));   // J
        cards.add(createCard(Suite.Spade, 10));   // 10

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.TWO_PAIR, result.getCombination());
        assertEquals(5, result.getHighCards().size());
    }

    @Test
    @DisplayName("Should detect PAIR")
    void testGetCardCombination_PAIR() throws Exception {
        // One pair of Aces
        cards.add(createCard(Suite.Diamond, 14)); // A
        cards.add(createCard(Suite.Heart, 14));   // A
        cards.add(createCard(Suite.Spade, 2));    // 2
        cards.add(createCard(Suite.Club, 12));    // Q
        cards.add(createCard(Suite.Diamond, 11)); // J
        cards.add(createCard(Suite.Heart, 3));    // 3
        cards.add(createCard(Suite.Spade, 9));    // 9

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.PAIR, result.getCombination());
        assertEquals(5, result.getHighCards().size());
    }

    @Test
    @DisplayName("Should detect HIGH_CARD")
    void testGetCardCombination_HIGH_CARD() throws Exception {
        // All different ranks
        cards.add(createCard(Suite.Diamond, 14)); // A
        cards.add(createCard(Suite.Heart, 13));   // K
        cards.add(createCard(Suite.Spade, 11));   // J
        cards.add(createCard(Suite.Club, 9));     // 9
        cards.add(createCard(Suite.Diamond, 7));  // 7
        cards.add(createCard(Suite.Heart, 5));    // 5
        cards.add(createCard(Suite.Spade, 3));    // 3

        CombinationResult result = CombinationCalculator.getCardCombination(cards);

        assertEquals(Combination.HIGH_CARD, result.getCombination());
        assertEquals(5, result.getHighCards().size());
    }

    @Test
    @DisplayName("Should throw exception when card count is not 7")
    void testGetCardCombination_InvalidCardCount() {
        cards.add(createCard(Suite.Diamond, 14));
        cards.add(createCard(Suite.Heart, 13));
        cards.add(createCard(Suite.Spade, 12));

        assertNull(CombinationCalculator.getCardCombination(cards));
    }

    @Test
    @DisplayName("Should return null when no flush exists")
    void testCheckFlush_NoFlush() {
        // No five cards of same suite
        cards.add(createCard(Suite.Heart, 14));
        cards.add(createCard(Suite.Heart, 13));
        cards.add(createCard(Suite.Heart, 12));
        cards.add(createCard(Suite.Spade, 11));
        cards.add(createCard(Suite.Diamond, 10));
        cards.add(createCard(Suite.Club, 9));
        cards.add(createCard(Suite.Heart, 8));

        List<Card> result = CombinationCalculator.checkFlush(cards);

        assertNull(result);
    }

    @Test
    @DisplayName("Should identify straight sequence")
    void testCheckStraight_WithStraight() {
        // 10-J-Q-K-A straight
        cards.add(createCard(Suite.Diamond, 14)); // A
        cards.add(createCard(Suite.Heart, 13));   // K
        cards.add(createCard(Suite.Spade, 12));   // Q
        cards.add(createCard(Suite.Club, 11));    // J
        cards.add(createCard(Suite.Diamond, 10)); // 10
        cards.add(createCard(Suite.Heart, 5));
        cards.add(createCard(Suite.Spade, 3));

        List<Card> result = CombinationCalculator.checkStraight(cards);

        assertNotNull(result);
        assertEquals(5, result.size());
        // Verify it's a valid straight (consecutive ranks)
        assertEquals(14, result.get(0).getRank());
        assertEquals(13, result.get(1).getRank());
        assertEquals(12, result.get(2).getRank());
        assertEquals(11, result.get(3).getRank());
        assertEquals(10, result.get(4).getRank());
    }

    @Test
    @DisplayName("Should return null when no straight exists")
    void testCheckStraight_NoStraight() {
        cards.add(createCard(Suite.Diamond, 14)); // A
        cards.add(createCard(Suite.Heart, 13));   // K
        cards.add(createCard(Suite.Spade, 11));   // J (gap at Q)
        cards.add(createCard(Suite.Club, 9));
        cards.add(createCard(Suite.Diamond, 7));
        cards.add(createCard(Suite.Heart, 5));
        cards.add(createCard(Suite.Spade, 3));

        List<Card> result = CombinationCalculator.checkStraight(cards);

        assertNull(result);
    }

    @Test
    @DisplayName("Should group cards by decreasing frequency")
    void testGetCardsListByDecreasingFrequency() {
        // Three 7s, two 5s, two 3s
        cards.add(createCard(Suite.Diamond, 7));
        cards.add(createCard(Suite.Heart, 7));
        cards.add(createCard(Suite.Spade, 7));
        cards.add(createCard(Suite.Diamond, 5));
        cards.add(createCard(Suite.Heart, 5));
        cards.add(createCard(Suite.Spade, 3));
        cards.add(createCard(Suite.Club, 3));

        List<List<Card>> result = CombinationCalculator.getCardsListByDecreasingFrequency(cards);

        assertNotNull(result);
        assertEquals(3, result.get(0).size()); // Three 7s
        assertEquals(2, result.get(1).size()); // Two 5s
        assertEquals(2, result.get(2).size()); // Two 3s
    }

    @Test
    @DisplayName("Should rank players with flush higher than straight")
    void testComparePlayerCombinations_FlushVsStraight() {
        // Community cards: K, Q, J, 10, 9
        List<Card> communityCards = new ArrayList<>();
        communityCards.add(createCard(Suite.Diamond, 13)); // K
        communityCards.add(createCard(Suite.Diamond, 12)); // Q
        communityCards.add(createCard(Suite.Diamond, 11)); // J
        communityCards.add(createCard(Suite.Diamond, 10)); // 10
        communityCards.add(createCard(Suite.Heart, 9));    // 9

        // Player 1: Has flush (A, 8 of diamonds)
        List<Card> player1Cards = new ArrayList<>();
        player1Cards.add(createCard(Suite.Diamond, 14)); // A
        player1Cards.add(createCard(Suite.Diamond, 8));  // 8
        Player player1 = new Player(1, 1000, player1Cards);

        // Player 2: Has pair (9, 9)
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(createCard(Suite.Club, 9));     // 9
        player2Cards.add(createCard(Suite.Spade, 9));    // 9
        Player player2 = new Player(2, 1000, player2Cards);

        List<Player> players = new ArrayList<>();
        players.add(player2);
        players.add(player1);

        var result = CombinationCalculator.getPlayerCombinationsSorted(communityCards, players);

        // Player 1 with flush should be first (winner)
        assertEquals(player1.getId(), result.get(0).getKey().getId());
        assertEquals(Combination.FLUSH, result.get(0).getValue().getCombination());
        
        // Player 2 with pair should be second
        assertEquals(player2.getId(), result.get(1).getKey().getId());
        assertEquals(Combination.STRAIGHT, result.get(1).getValue().getCombination());
    }

    @Test
    @DisplayName("Should rank players with high card sum when combinations are same")
    void testComparePlayerCombinations_SameCombinationDifferentHighCards() {
        // Community cards: 2, 3, 4, 5, 6
        List<Card> communityCards = new ArrayList<>();
        communityCards.add(createCard(Suite.Diamond, 6));
        communityCards.add(createCard(Suite.Heart, 5));
        communityCards.add(createCard(Suite.Spade, 10));
        communityCards.add(createCard(Suite.Club, 3));
        communityCards.add(createCard(Suite.Diamond, 2));

        // Player 1: Has Ace-King high card (A, K)
        List<Card> player1Cards = new ArrayList<>();
        player1Cards.add(createCard(Suite.Heart, 14)); // A
        player1Cards.add(createCard(Suite.Spade, 13)); // K
        Player player1 = new Player(1, 1000, player1Cards);

        // Player 2: Has Queen-Jack high card (Q, J)
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(createCard(Suite.Club, 12)); // Q
        player2Cards.add(createCard(Suite.Diamond, 11)); // J
        Player player2 = new Player(2, 1000, player2Cards);

        List<Player> players = new ArrayList<>();
        players.add(player2);
        players.add(player1);

        var result = CombinationCalculator.getPlayerCombinationsSorted(communityCards, players);

        // Player 1 with higher cards should be first
        assertEquals(player1.getId(), result.get(0).getKey().getId());
        assertEquals(Combination.HIGH_CARD, result.get(0).getValue().getCombination());
        
        // Player 2 should be second
        assertEquals(player2.getId(), result.get(1).getKey().getId());
        assertEquals(Combination.HIGH_CARD, result.get(1).getValue().getCombination());
    }

    @Test
    @DisplayName("Should correctly rank three players with different combinations")
    void testComparePlayerCombinations_ThreePlayersMultipleCombinations() {
        // Community cards: 7, 7, 8, 9, 10
        List<Card> communityCards = new ArrayList<>();
        communityCards.add(createCard(Suite.Diamond, 10));
        communityCards.add(createCard(Suite.Heart, 9));
        communityCards.add(createCard(Suite.Spade, 8));
        communityCards.add(createCard(Suite.Club, 7));
        communityCards.add(createCard(Suite.Diamond, 7));

        // Player 1: Has Three of a Kind (7, 7, 7)
        List<Card> player1Cards = new ArrayList<>();
        player1Cards.add(createCard(Suite.Heart, 7));
        player1Cards.add(createCard(Suite.Spade, 2));
        Player player1 = new Player(1, 1000, player1Cards);

        // Player 2: Has Pair (7, 7)
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(createCard(Suite.Club, 2));
        player2Cards.add(createCard(Suite.Heart, 3));
        Player player2 = new Player(2, 1000, player2Cards);

        // Player 3: Has Straight (6, J)
        List<Card> player3Cards = new ArrayList<>();
        player3Cards.add(createCard(Suite.Diamond, 6));
        player3Cards.add(createCard(Suite.Heart, 11)); // J
        Player player3 = new Player(3, 1000, player3Cards);

        List<Player> players = new ArrayList<>();
        players.add(player2);
        players.add(player1);
        players.add(player3);

        var result = CombinationCalculator.getPlayerCombinationsSorted(communityCards, players);

        assertEquals(3, result.size());
        // Player 3 with straight should be first
        assertEquals(player3.getId(), result.get(0).getKey().getId());
        assertEquals(Combination.STRAIGHT, result.get(0).getValue().getCombination());
        
        // Player 1 with three of a kind should be second
        assertEquals(player1.getId(), result.get(1).getKey().getId());
        assertEquals(Combination.THREE_OF_A_KIND, result.get(1).getValue().getCombination());
        
        // Player 2 with pair should be last
        assertEquals(player2.getId(), result.get(2).getKey().getId());
        assertEquals(Combination.PAIR, result.get(2).getValue().getCombination());
    }

    @Test
    @DisplayName("Should handle single player")
    void testComparePlayerCombinations_SinglePlayer() {
        // Community cards
        List<Card> communityCards = new ArrayList<>();
        communityCards.add(createCard(Suite.Diamond, 14)); // A
        communityCards.add(createCard(Suite.Heart, 13));   // K
        communityCards.add(createCard(Suite.Spade, 12));   // Q
        communityCards.add(createCard(Suite.Club, 11));    // J
        communityCards.add(createCard(Suite.Diamond, 10)); // 10

        // Player 1: Has Royal Flush (A, K of diamonds)
        List<Card> playerCards = new ArrayList<>();
        playerCards.add(createCard(Suite.Diamond, 14)); // A
        playerCards.add(createCard(Suite.Diamond, 13)); // K
        Player player = new Player(1, 1000, playerCards);

        List<Player> players = new ArrayList<>();
        players.add(player);

        var result = CombinationCalculator.getPlayerCombinationsSorted(communityCards, players);

        assertEquals(1, result.size());
        assertEquals(player.getId(), result.get(0).getKey().getId());
    }

    @Test
    @DisplayName("Should rank four of a kind highest")
    void testComparePlayerCombinations_FourOfAKindVsFullHouse() {
        // Community cards: 5, 5, 5, 10, 3
        List<Card> communityCards = new ArrayList<>();
        communityCards.add(createCard(Suite.Diamond, 5));
        communityCards.add(createCard(Suite.Heart, 5));
        communityCards.add(createCard(Suite.Spade, 5));
        communityCards.add(createCard(Suite.Club, 10));
        communityCards.add(createCard(Suite.Diamond, 3));

        // Player 1: Has Four of a Kind (5, 5, 5, 5)
        List<Card> player1Cards = new ArrayList<>();
        player1Cards.add(createCard(Suite.Club, 5));
        player1Cards.add(createCard(Suite.Heart, 2));
        Player player1 = new Player(1, 1000, player1Cards);

        // Player 2: Has Full House (10, 10, 5, 5, 5)
        List<Card> player2Cards = new ArrayList<>();
        player2Cards.add(createCard(Suite.Heart, 10));
        player2Cards.add(createCard(Suite.Spade, 3));
        Player player2 = new Player(2, 1000, player2Cards);

        List<Player> players = new ArrayList<>();
        players.add(player2);
        players.add(player1);

        var result = CombinationCalculator.getPlayerCombinationsSorted(communityCards, players);

        // Player 1 with four of a kind should be first
        assertEquals(player1.getId(), result.get(0).getKey().getId());
        assertEquals(Combination.FOUR_OF_A_KIND, result.get(0).getValue().getCombination());
        
        // Player 2 with full house should be second
        assertEquals(player2.getId(), result.get(1).getKey().getId());
        assertEquals(Combination.FULL_HOUSE, result.get(1).getValue().getCombination());
    }
}
