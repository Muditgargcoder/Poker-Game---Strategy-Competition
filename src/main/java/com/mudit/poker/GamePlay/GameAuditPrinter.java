package com.mudit.poker.GamePlay;

import java.util.List;
import java.util.Map;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.CombinationResult;
import com.mudit.poker.Pojos.GameAudit;
import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameState;
import com.mudit.poker.Pojos.Player;
import com.mudit.poker.Pojos.PlayerMove;
import com.mudit.poker.Pojos.Suite;

/**
 * Handles pretty printing of poker game audit reports with play-by-play replay
 */
public class GameAuditPrinter {

    private static final String BORDER = "═".repeat(140);
    private static final String THICK_LINE = "╔" + "═".repeat(138) + "╗";
    private static final String THIN_LINE = "─".repeat(140);
    private static final String SEPARATOR = "║ " + "─".repeat(136) + " ║";

    /**
     * Print the complete audit report for all games played with play-by-play
     * 
     * @param gameAudit List of GameAudit objects (each contains game state and
     *                  results)
     * @param startingMoney Initial amount each player started with
     */
    public static void printAuditReport(List<GameAudit> gameAudit, int startingMoney) {
        System.out.println("\n" + THICK_LINE);
        printCentered("🃏 POKER GAME TOURNAMENT AUDIT REPORT 🃏", 140);
        System.out.println(THICK_LINE);

        // Print game-by-game results with play-by-play
        printGameByGamePlayByPlay(gameAudit);

        // Print final standings
        printFinalTournamentStandings(gameAudit, startingMoney);

        System.out.println(THICK_LINE);
        printCentered("End of Tournament Report", 140);
        System.out.println(THICK_LINE + "\n");
    }

    /**
     * Print detailed play-by-play for each game
     */
    private static void printGameByGamePlayByPlay(List<GameAudit> gameAudit) {
        for (int gameNum = 0; gameNum < gameAudit.size(); gameNum++) {
            GameAudit audit = gameAudit.get(gameNum);
            GameState gameState = audit.getGameState();
            List<Map.Entry<Player, CombinationResult>> gameResult = audit.getPlayerResults();

            System.out.println("\n" + THIN_LINE);
            printCentered("🎮 GAME " + (gameNum + 1) + " - PLAY BY PLAY REPLAY 🎮", 140);
            System.out.println(THIN_LINE);

            // Game intro
            printGameIntro(gameState);

            // Play-by-play for each round
            printRoundByRoundReplay(gameState);

            // Final results
            printGameResults(gameResult, gameNum + 1);
        }
    }

    /**
     * Print game introduction with first player
     */
    private static void printGameIntro(GameState gameState) {
        System.out.println("\n┌─ GAME STATUS ─────────────────────────────────┐");
        System.out.printf("│ First Player ID: %3d                           │%n", gameState.getFirstPlayer());
        System.out.printf("│ Total Rounds: 3 (Pre-flop, Flop, Turn+River) │%n");
        System.out.printf("│ Initial Pot: $0                               │%n");
        System.out.println("└──────────────────────────────────────────────┘");
    }

    /**
     * Print round-by-round replay with all moves
     */
    private static void printRoundByRoundReplay(GameState gameState) {
        List<List<PlayerMove>> allMoves = gameState.getPlayerMoves();
        List<Card> communityCards = gameState.getRevealedCommunityCards();

        if (allMoves == null || allMoves.isEmpty()) {
            System.out.println("No moves recorded for this game.");
            return;
        }

        for (int roundIdx = 0; roundIdx < Math.min(allMoves.size(), 3); roundIdx++) {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════════╗");
            System.out.printf("║ ROUND %d - BETTING PHASE%n", roundIdx + 1);
            System.out.println("╚═══════════════════════════════════════════════════════════════════╝");

            // Show community cards revealed
            System.out.print("\n📇 Community Cards Revealed: ");
            int cardsToShow = (roundIdx == 0) ? 3 : roundIdx + 3;
            if (communityCards != null && !communityCards.isEmpty()) {
                for (int i = 0; i < Math.min(cardsToShow, communityCards.size()); i++) {
                    System.out.print("  " + formatCard(communityCards.get(i)));
                }
            }
            System.out.println();

            // Show all moves in this round
            List<PlayerMove> roundMoves = allMoves.get(roundIdx);
            System.out.println("\n" + "▼".repeat(70));
            System.out.println("MOVES IN THIS ROUND:");
            System.out.println("▲".repeat(70));

            if (roundMoves != null && !roundMoves.isEmpty()) {
                int moveCounter = 1;
                int roundPot = 0;
                for (PlayerMove move : roundMoves) {
                    String moveIcon = getMoveIcon(move.getMoveType());
                    String moveType = move.getMoveType().toString();
                    roundPot += move.getBetAmount();

                    System.out.printf("  %2d. Player %-3d: %s %-6s Bet: $%-4d │ Pot: $%-6d%n",
                            moveCounter, move.getPlayerId(), moveIcon, moveType,
                            move.getBetAmount(), roundPot);
                    moveCounter++;
                }
            }

            System.out.println();
        }
    }

    /**
     * Print final game results with rankings
     */
    private static void printGameResults(List<Map.Entry<Player, CombinationResult>> gameResult, int gameNum) {
        System.out.println("\n" + THIN_LINE);
        System.out.printf("║ %-138s ║%n", String.format("GAME %d - FINAL RESULTS & WINNER", gameNum));
        System.out.println(THIN_LINE);

        System.out.printf("║ %-8s │ %-12s │ %-15s │ %-50s │ %-30s ║%n",
                "RANK", "PLAYER ID", "RESULT", "AMOUNT", "HAND");
        System.out.println(SEPARATOR);

        for (int rank = 0; rank < gameResult.size(); rank++) {
            Map.Entry<Player, CombinationResult> entry = gameResult.get(rank);
            Player player = entry.getKey();
            CombinationResult result = entry.getValue();

            String rankStr = formatRank(rank + 1);
            String playerId = String.valueOf(player.getId());
            String resultStr = (rank == 0) ? "🏆 WINNER 🏆" : (rank == gameResult.size() - 1) ? "Last Place" : "Still In";
            String amount = String.format("$%,d", player.getCurrentAmount());
            String combination = result.getCombination().toString();
            String cards = formatCards(result.getHighCards());

            String handStr = String.format("%s | %s", combination, cards);

            System.out.printf("║ %-8s │ %-12s │ %-15s │ %-50s │ %-30s ║%n",
                    rankStr, playerId, resultStr, amount,
                    handStr.length() > 30 ? handStr.substring(0, 27) + "..." : handStr);
        }
        System.out.println(THIN_LINE);
    }

    /**
     * Print final tournament standings after all games
     */
    private static void printFinalTournamentStandings(List<GameAudit> gameAudit, int startingMoney) {
        System.out.println("\n" + THICK_LINE);
        printCentered("🏅 FINAL TOURNAMENT STANDINGS 🏅", 140);
        System.out.println(THICK_LINE);

        if (gameAudit.isEmpty()) {
            System.out.println("No games played.");
            return;
        }

        // Reconstruct final player standings from last game audit
        GameAudit lastGameAudit = gameAudit.get(gameAudit.size() - 1);
        List<Map.Entry<Player, CombinationResult>> lastGame = lastGameAudit.getPlayerResults();

        List<Map.Entry<Player, CombinationResult>> sortedByFinalAmount = lastGame.stream()
                .sorted((a, b) -> -Integer.compare(a.getKey().getCurrentAmount(), b.getKey().getCurrentAmount()))
                .toList();

        System.out.printf("║ %-8s │ %-12s │ %-20s │ %-20s │ %-35s │ %-20s ║%n",
                "RANK", "PLAYER ID", "FINAL AMOUNT", "STARTING AMOUNT", "PROFIT/LOSS", "CHANGE %");
        System.out.println(SEPARATOR);

        for (int rank = 0; rank < sortedByFinalAmount.size(); rank++) {
            Player player = sortedByFinalAmount.get(rank).getKey();
            String rankStr = formatRank(rank + 1);
            String playerId = String.valueOf(player.getId());
            int finalAmount = player.getCurrentAmount();
            String finalAmountStr = String.format("$%,d", finalAmount);
            String startingAmountStr = String.format("$%,d", startingMoney);

            int profitLoss = finalAmount - startingMoney;
            String profitLossStr = (profitLoss >= 0 ? "+" : "") + String.format("$%,d", profitLoss);
            double changePercent = (double) profitLoss / startingMoney * 100;
            String changePercentStr = String.format("%+.1f%%", changePercent);
            String statusIcon = profitLoss >= 0 ? "✓" : "✗";

            System.out.printf("║ %-8s │ %-12s │ %-20s │ %-20s │ %-35s │ %-20s ║%n",
                    rankStr, playerId, finalAmountStr, startingAmountStr,
                    statusIcon + " " + profitLossStr, changePercentStr);
        }
        System.out.println(THIN_LINE);

        // Print tournament statistics
        printTournamentStatistics(sortedByFinalAmount, startingMoney, gameAudit.size());
    }

    /**
     * Print tournament statistics
     */
    private static void printTournamentStatistics(List<Map.Entry<Player, CombinationResult>> sortedPlayers,
            int startingMoney, int totalGames) {
        System.out.println("\n" + THIN_LINE);
        System.out.println("┌─ TOURNAMENT STATISTICS ────────────────────────────────────┐");

        Map.Entry<Player, CombinationResult> biggestWinner = sortedPlayers.get(0);
        Map.Entry<Player, CombinationResult> biggestLoser = sortedPlayers.get(sortedPlayers.size() - 1);

        long winnerCount = sortedPlayers.stream()
                .filter(e -> e.getKey().getCurrentAmount() > startingMoney)
                .count();

        long loserCount = sortedPlayers.stream()
                .filter(e -> e.getKey().getCurrentAmount() < startingMoney)
                .count();

        long breakEvenCount = sortedPlayers.stream()
                .filter(e -> e.getKey().getCurrentAmount() == startingMoney)
                .count();

        int biggestWinnerProfit = biggestWinner.getKey().getCurrentAmount() - startingMoney;
        int biggestLoserLoss = Math.abs(biggestLoser.getKey().getCurrentAmount() - startingMoney);

        int totalProfit = sortedPlayers.stream()
                .mapToInt(e -> e.getKey().getCurrentAmount() - startingMoney)
                .sum();

        System.out.printf("│ 🎮 Total Games Played:        %-37d │%n", totalGames);
        System.out.printf("│ 👥 Total Players:             %-37d │%n", sortedPlayers.size());
        System.out.printf("│ ✓ Players with Profit:        %-37d │%n", winnerCount);
        System.out.printf("│ ✗ Players with Loss:          %-37d │%n", loserCount);
        System.out.printf("│ ↔ Players Breaking Even:      %-37d │%n", breakEvenCount);
        System.out.println("├────────────────────────────────────────────────────────────┤");
        System.out.printf("│ 🏆 Biggest Winner:  Player %d with +$%,7d       │%n", biggestWinner.getKey().getId(),
                biggestWinnerProfit);
        System.out.printf("│ 💸 Biggest Loser:   Player %d with -$%,7d       │%n", biggestLoser.getKey().getId(),
                biggestLoserLoss);
        System.out.printf("│ 💰 Total Movement:            +$%,35d │%n", totalProfit);
        System.out.println("└────────────────────────────────────────────────────────────┘");
    }

    /**
     * Get emoji icon for move type
     */
    private static String getMoveIcon(com.mudit.poker.Pojos.MoveType moveType) {
        return switch (moveType) {
            case FOLD -> "🚫";
            case CALL -> "✔️ ";
            case RAISE -> "📈";
            case CHECK -> "✓ ";
            default -> "❓";
        };
    }

    /**
     * Format rank with proper ordinal suffix and trophy
     */
    private static String formatRank(int rank) {
        String trophy = switch (rank) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> "  ";
        };
        String suffix = switch (rank % 10) {
            case 1 -> (rank % 100 == 11) ? "th" : "st";
            case 2 -> (rank % 100 == 12) ? "th" : "nd";
            case 3 -> (rank % 100 == 13) ? "th" : "rd";
            default -> "th";
        };
        return rank + suffix + " " + trophy;
    }

    /**
     * Format a single card for display
     */
    private static String formatCard(Card card) {
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
        return "[" + rankStr + suiteStr + "]";
    }

    /**
     * Format multiple cards for display
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

    /**
     * Print centered text
     */
    private static void printCentered(String text, int width) {
        int padding = (width - text.length() - 4) / 2;
        System.out.printf("║ %" + (padding + text.length()) + "s %" + (width - padding - text.length() - 4)
                + "s ║%n", text, "");
    }
}
