package com.mudit.poker.Combinations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.CombinationResult;
import com.mudit.poker.Pojos.GameResult;
import com.mudit.poker.Pojos.Player;

public class CombinationCalculator {

    public static Comparator<CombinationResult> compareCombinations = (x, y) -> {
        int compare = -Integer.compare(x.getCombination().ordinal(), y.getCombination().ordinal());
        if (compare == 0) {
            List<Card> xCard = x.getHighCards();
            List<Card> yCard = y.getHighCards();
            for (int i = 0; i < xCard.size(); i++) {
                if (xCard.get(i).getRank() != yCard.get(i).getRank()) {
                    return -Integer.compare(xCard.get(i).getRank(), yCard.get(i).getRank());
                }
            }
            return 0; // Tie
        }
        return compare;
    };

    /**
     * @param communityCards Common cards for all players
     * @param players        List of players
     * @return Sorted order of players score.
     */
    public static List<Map.Entry<Player, CombinationResult>> getPlayerCombinationsSorted(List<Card> communityCards,
            List<Player> players) {
        List<Map.Entry<Player, CombinationResult>> allPlayerCombinations = players.stream()
                .map(player -> {
                    List<Card> totalCards = new ArrayList<>(player.getAssignedCards());
                    totalCards.addAll(communityCards);
                    return Map.entry(player, getCardCombination(totalCards));
                })
                .sorted((a, b) -> compareCombinations.compare(a.getValue(), b.getValue()))
                .toList();

        return allPlayerCombinations;
    }

    /**
     * @param communityCards Common cards for all players
     * @param players        List of players
     * @return Winner players. (Multiple winners means they are in tie)
     */
    public static GameResult getGameResult(List<Card> communityCards, List<Player> players) {
        List<Map.Entry<Player, CombinationResult>> allPlayerResults = getPlayerCombinationsSorted(communityCards, players);
        List<Map.Entry<Player, CombinationResult>> winners = allPlayerResults;

        CombinationResult prevResult = allPlayerResults.get(0).getValue();
        prevResult.setWinner(true);
        for (int i = 1; i < allPlayerResults.size(); i++) {
            CombinationResult thisResult = allPlayerResults.get(i).getValue();
            if (compareCombinations.compare(thisResult, prevResult) != 0) {
                winners = allPlayerResults.subList(0, i);
                break;
            }
            thisResult.setWinner(true);
            prevResult = thisResult;
        }

        return GameResult.builder().allPlayerResult(allPlayerResults).winners(winners).build();
    }

    /**
     * @param cards Expects a list of 7 cards. (5 community + 2 player cards)
     * @return Returns the highest possible Combination along with the 5 high cards.
     */
    public static CombinationResult getCardCombination(List<Card> cards) {
        if (cards.size() != 7)
            return null;
        Collections.sort(cards);
        List<Card> result = new ArrayList<>();
        List<List<Card>> cardsListByDecresingFrequency = getCardsListByDecreasingFrequency(cards);
        List<Card> cardsByDecresingFrequency = cardsListByDecresingFrequency.stream().flatMap(List::stream).toList();

        List<Card> highestFirstFrequencyCards = cardsListByDecresingFrequency.get(0);
        List<Card> highestSecondFrequencyCards = cardsListByDecresingFrequency.get(1);

        if (highestFirstFrequencyCards.size() == 4) {
            result.addAll(highestFirstFrequencyCards);
            result.addAll(getFirstNCardsAfterIdxK(cardsByDecresingFrequency, 4, (5 - 4)));
            // These are remaining cards apart from the combination which are added after
            // sorting and picking the top remaining cards (here 5 - 4 = 1)
            return new CombinationResult(Combination.FOUR_OF_A_KIND, result);
        }
        if (highestFirstFrequencyCards.size() == 3 && highestSecondFrequencyCards.size() == 2) {
            return new CombinationResult(Combination.FULL_HOUSE, cardsByDecresingFrequency.subList(0, 5));
        }
        if ((result = checkFlush(cards)) != null) {
            return new CombinationResult(Combination.FLUSH, result);
        }
        if ((result = checkStraight(cards)) != null) {
            return new CombinationResult(Combination.STRAIGHT, result);
        }
        if (highestFirstFrequencyCards.size() == 3) {
            result = new ArrayList<>();
            result.addAll(highestFirstFrequencyCards);
            result.addAll(getFirstNCardsAfterIdxK(cardsByDecresingFrequency, 3, (5 - 3)));
            return new CombinationResult(Combination.THREE_OF_A_KIND, result);
        }
        if (highestFirstFrequencyCards.size() == 2 && highestSecondFrequencyCards.size() == 2) {
            result = new ArrayList<>();
            result.addAll(highestFirstFrequencyCards);
            result.addAll(highestSecondFrequencyCards);
            result.addAll(getFirstNCardsAfterIdxK(cardsByDecresingFrequency, 4, (5 - 4)));
            return new CombinationResult(Combination.TWO_PAIR, result);
        }
        if (highestFirstFrequencyCards.size() == 2) {
            result = new ArrayList<>();
            result.addAll(highestFirstFrequencyCards);
            result.addAll(getFirstNCardsAfterIdxK(cardsByDecresingFrequency, 2, (5 - 2)));
            return new CombinationResult(Combination.PAIR, result);
        }

        return new CombinationResult(Combination.HIGH_CARD, cards.subList(0, 5));
    }

    /**
     * @param cards [D1, H4, D2, D4, C4, D3, D5] (size = 7)
     * @return [D5, D4, D3, D2, D1]
     */
    public static List<Card> checkFlush(List<Card> cards) {
        List<Card> sameSuitCards = cards.stream()
                .collect(Collectors.groupingBy(Card::getSuite, Collectors.toList()))
                .values().stream()
                .filter(cardsOfSuite -> cardsOfSuite.size() >= 5)
                .findFirst().orElse(null);
        if (sameSuitCards == null)
            return null;
        Collections.sort(sameSuitCards);
        return sameSuitCards.subList(0, 5);
    }

    /**
     * @param cards [1, 2, 1, 4, 3, 1, 3] (size = 7)
     * @return [[1, 1, 1], [3, 3], [4], [2]]
     */
    public static List<List<Card>> getCardsListByDecreasingFrequency(List<Card> cards) {
        return cards.stream().collect(Collectors.groupingBy(Card::getRank))
                .values().stream().sorted((a, b) -> {
                    int compare = -Integer.compare(a.size(), b.size());
                    if (compare == 0) {
                        return -Integer.compare(a.get(0).getRank(), b.get(0).getRank());
                    } else {
                        return compare;
                    }
                }).toList();
    }

    /**
     * @param cards [2, 7, 8, 5, 9, 10, 6]
     * @return [10, 9, 8, 7, 6]
     */
    public static List<Card> checkStraight(List<Card> cards) {
        Collections.sort(cards);
        int counter = 1;
        int startIdx = 0;
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getRank() == cards.get(i - 1).getRank() - 1) {
                counter++;
                if (counter == 5)
                    return cards.subList(startIdx, i + 1);
            } else {
                counter = 1;
                startIdx = i;
            }
        }
        return null;
    }

    public static List<Card> getFirstNCardsAfterIdxK(List<Card> cards, int k, int n) {
        return (new ArrayList<>(cards.subList(k, 7))).stream().sorted().limit(n).toList();
    }
}
