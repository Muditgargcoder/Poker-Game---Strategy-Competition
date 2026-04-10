package com.mudit.poker.GamePlay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.Player;
import com.mudit.poker.Pojos.Suite;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SetupGame {
    static Random random = new Random();

    static final Set<Card> FULL_DECK = generateFull52CardDeck();

    static List<Card> distributeCards(List<Player> players) {
        Set<Card> remainingDeck = new HashSet<>(FULL_DECK);
        List<Card> communityCards = get5RandomCommunityCards(remainingDeck);
        assignPlayersNewCards(players, remainingDeck);
        return communityCards;
    }

    public static void assignPlayersNewCards(List<Player> players, Set<Card> remainingDeck) {
        for (Player player : players) {
            List<Card> newCards = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Card nextCard = generateRandomCard(remainingDeck);
                newCards.add(nextCard);
                remainingDeck.remove(nextCard);
            }
            player.setAssignedCards(newCards);
        }
    }

    public static List<Card> get5RandomCommunityCards(Set<Card> remainingDeck) {
        Set<Card> communityCards = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Card nextCard = generateRandomCard(remainingDeck);
            communityCards.add(nextCard);
            remainingDeck.remove(nextCard);
        }
        return new ArrayList<>(communityCards);
    }

    public static Set<Card> generateFull52CardDeck() {
        Set<Card> deck = new HashSet<>();
        for (int i = 2; i <= 14; i++) {
            for (int j = 0; j < 4; j++) {
                deck.add(new Card(Suite.values()[j], i));
            }
        }
        return deck;
    }

    static public Card generateRandomCard(Set<Card> availableCards) {
        int nextRank = random.nextInt(2, 14);
        Suite nextSuite = Suite.values()[random.nextInt(0, 3)];
        Card nextCard = new Card(nextSuite, nextRank);
        if (availableCards.contains(nextCard)) {
            return nextCard;
        } else {
            return generateRandomCard(availableCards);
        }
    }
}
