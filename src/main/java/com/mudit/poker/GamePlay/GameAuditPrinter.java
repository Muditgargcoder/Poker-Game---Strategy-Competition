package com.mudit.poker.GamePlay;

import java.util.List;
import java.util.Map;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.CombinationResult;
import com.mudit.poker.Pojos.Player;
import com.mudit.poker.Pojos.Suite;

/**
 * Handles pretty printing of poker game audit reports
 */
public class GameAuditPrinter {

    private static final String BORDER = "=".repeat(120);
    private static final String LINE = "-".repeat(120);

    /**
     * Print the complete audit report for all games played
     * 
     * @param gameAudit List of game results (each game contains rankings)
     * @param startingMoney Initial amount each player started with
     */
    public static void printAuditReport(List<List<Map.Entry<Player, CombinationResult>>> gameAudit,
            int startingMoney) {
        System.out.println("\n" + BORDER);
        System.out.println("POKER GAME AUDIT REPORT");
        System.out.println(BORDER);

        // Print game-by-game results
        printGameByGameResults(gameAudit);

        // Print final standings
        printFinalStandings(gameAudit, startingMoney);

        System.out.println(BORDER + "\n");
    }

    /**
     * Print results for each individual game
     */
    private static void printGameByGameResults(List<List<Map.Entry<Player, CombinationResult>>> gameAudit) {
        for (int gameNum = 0; gameNum < gameAudit.size(); gameNum++) {
            System.out.println("\n" + LINE);
            System.out.printf("GAME %d - FINAL STANDINGS%n", gameNum + 1);
            System.out.println(LINE);

            List<Map.Entry<Player, CombinationResult>> gameResult = gameAudit.get(gameNum);

            System.out.printf("%-8s | %-12s | %-15s | %-20s | %-50s%n",
                    "RANK", "PLAYER ID", "WINNER", "AMOUNT ($)", "COMBINATION & CARDS");
            System.out.println(LINE);

            for (int rank = 0; rank < gameResult.size(); rank++) {
                Map.Entry<Player, CombinationResult> entry = gameResult.get(rank);
                Player player = entry.getKey();
                CombinationResult result = entry.getValue();

                String rankStr = formatRank(rank + 1);
                String playerId = String.valueOf(player.getId());
                String isWinner = (rank == 0) ? "🏆 WINNER" : "-";
                String amount = String.format("$%,d", player.getCurrentAmount());
                String combination = result.getCombination().toString();
                String cards = formatCards(result.getHighCards());
                String combinationDisplay = combination + " - " + cards;

                System.out.printf("%-8s | %-12s | %-15s | %-20s | %-50s%n",
                        rankStr, playerId, isWinner, amount, combinationDisplay);
            }
        }
    }

    /**
     * Print final standings after all games
     */
    private static void printFinalStandings(List<List<Map.Entry<Player, CombinationResult>>> gameAudit,
            int startingMoney) {
        System.out.println("\n" + LINE);
        System.out.println("FINAL PLAYER STANDINGS (After All Games)");
        System.out.println(LINE);

        if (gameAudit.isEmpty()) {
            System.out.println("No games played.");
            return;
        }

        // Get players from the last game and sort by their final amount
        List<Map.Entry<Player, CombinationResult>> lastGame = gameAudit.get(gameAudit.size() - 1);
        List<Map.Entry<Player, CombinationResult>> sortedByFinalAmount = lastGame.stream()
                .sorted((a, b) -> -Integer.compare(a.getKey().getCurrentAmount(), b.getKey().getCurrentAmount()))
                .toList();

        System.out.printf("%-8s | %-12s | %-20s | %-20s | %-20s%n",
                "RANK", "PLAYER ID", "FINAL AMOUNT ($)", "STARTING AMOUNT ($)", "PROFIT/LOSS ($)");
        System.out.println(LINE);

        for (int rank = 0; rank < sortedByFinalAmount.size(); rank++) {
            Player player = sortedByFinalAmount.get(rank).getKey();
            String rankStr = formatRank(rank + 1);
            String playerId = String.valueOf(player.getId());
            int finalAmount = player.getCurrentAmount();
            String finalAmountStr = String.format("$%,d", finalAmount);
            String startingAmountStr = String.format("$%,d", startingMoney);
            
            int profitLoss = finalAmount - startingMoney;
            String profitLossStr = (profitLoss >= 0 ? "+" : "") + String.format("$%,d", profitLoss);
            String color = profitLoss >= 0 ? "✓" : "✗";

            System.out.printf("%-8s | %-12s | %-20s | %-20s | %-20s %s%n",
                    rankStr, playerId, finalAmountStr, startingAmountStr, profitLossStr, color);
        }

        // Print summary statistics
        printSummaryStatistics(sortedByFinalAmount, startingMoney);
    }

    /**
     * Print summary statistics
     */
    private static void printSummaryStatistics(List<Map.Entry<Player, CombinationResult>> sortedPlayers,
            int startingMoney) {
        System.out.println("\n" + LINE);
        System.out.println("SUMMARY STATISTICS");
        System.out.println(LINE);

        Map.Entry<Player, CombinationResult> biggestWinner = sortedPlayers.get(0);
        Map.Entry<Player, CombinationResult> biggestLoser = sortedPlayers.get(sortedPlayers.size() - 1);

        int totalProfit = sortedPlayers.stream()
                .mapToInt(e -> e.getKey().getCurrentAmount() - startingMoney)
                .sum();

        long winnerCount = sortedPlayers.stream()
                .filter(e -> e.getKey().getCurrentAmount() > startingMoney)
                .count();

        long loserCount = sortedPlayers.stream()
                .filter(e -> e.getKey().getCurrentAmount() < startingMoney)
                .count();

        int biggestWinnerProfit = biggestWinner.getKey().getCurrentAmount() - startingMoney;
        int biggestLoserLoss = biggestLoser.getKey().getCurrentAmount() - startingMoney;

        System.out.printf("%-30s: Player %d with +$%,d%n", "Biggest Winner", biggestWinner.getKey().getId(),
                biggestWinnerProfit);
        System.out.printf("%-30s: Player %d with -$%,d%n", "Biggest Loser", biggestLoser.getKey().getId(),
                Math.abs(biggestLoserLoss));
        System.out.printf("%-30s: %d%n", "Players with Profit", winnerCount);
        System.out.printf("%-30s: %d%n", "Players with Loss", loserCount);
        System.out.printf("%-30s: $%,d%n", "Total Money Movement", totalProfit);
    }

    /**
     * Format rank with proper ordinal suffix (1st, 2nd, 3rd, etc.)
     */
    private static String formatRank(int rank) {
        String suffix = switch (rank % 10) {
            case 1 -> (rank % 100 == 11) ? "th" : "st";
            case 2 -> (rank % 100 == 12) ? "th" : "nd";
            case 3 -> (rank % 100 == 13) ? "th" : "rd";
            default -> "th";
        };
        return rank + suffix;
    }

    /**
     * Format cards for display with rank and suit symbols
     */
    private static String formatCards(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return "No cards";
        }
        return cards.stream()
                .map(card -> {
                    String rankStr = switch (card.getRank()) {
                        case 11 -> "J";
                        case 12 -> "Q";
                        case 13 -> "K";
                        case 14 -> "A";
                        default -> String.valueOf(card.getRank());
                    };
                    String suiteStr = switch (card.getSuite()) {
                        case Heart -> "♥";
                        case Diamond -> "♦";
                        case Club -> "♣";
                        case Spade -> "♠";
                    };
                    return rankStr + suiteStr;
                })
                .reduce((a, b) -> a + " " + b)
                .orElse("No cards");
    }
}
