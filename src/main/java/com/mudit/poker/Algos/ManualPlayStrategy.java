package com.mudit.poker.Algos;

import java.util.List;
import java.util.Scanner;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.GameResult;
import com.mudit.poker.Pojos.MoveType;
import com.mudit.poker.Pojos.PlayerMove;
import com.mudit.poker.UserInterfaces.GamePlayerStatusInfo;
import com.mudit.poker.UserInterfaces.GameStateInfo;
import com.mudit.poker.UserInterfaces.PlayerStrategy;

public class ManualPlayStrategy implements PlayerStrategy {
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public PlayerMove makeYourMove(GameStateInfo gameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        // Display game state and player information
        printGameStatePretty(gameState, myStatus, myCards);
        
        // Get move selection from player
        MoveType selectedMove = promptForMoveType();
        
        // Get bet amount based on move type
        int betAmount = 0;
        if (selectedMove == MoveType.RAISE || selectedMove == MoveType.CALL) {
            betAmount = promptForBetAmount(gameState, myStatus, selectedMove);
        }
        
        return new PlayerMove(selectedMove, betAmount);
    }

    @Override
    public void onNewMove(PlayerMove otherPlayerMove, GameStateInfo newGameState, GamePlayerStatusInfo myStatus,
            List<Card> myCards) {
        // Optional: Can be used to update the player's knowledge of the game state
    }

    @Override
    public void onSingleGameEnd(GameResult gameResult) {
        return;
    }

    /**
     * Display the current game state and player status in a pretty format
     */
    private void printGameStatePretty(GameStateInfo gameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        System.out.println("\n" + "╔" + "═".repeat(100) + "╗");
        printCentered("🎮 YOUR TURN - MANUAL PLAY MODE 🎮", 102);
        System.out.println("╚" + "═".repeat(100) + "╝");

        // Game metadata
        System.out.println("\n📊 GAME STATUS:");
        System.out.printf("   Round: %d/3  │  Pot: $%,d  │  Highest Bid: $%d%n",
                gameState.getRound(), gameState.getTotalPot(), gameState.getHighestCurrentBid());

        // Community cards
        System.out.println("\n🎴 COMMUNITY CARDS REVEALED:");
        printCommunityCards(gameState.getRevealedCommunityCards());

        // Your cards
        System.out.println("\n🎴 YOUR HOLE CARDS:");
        printYourCards(myCards);

        // Your status
        System.out.println("\n👤 YOUR STATUS:");
        System.out.printf("   Player ID: %d%n", myStatus.getPlayerId());
        System.out.printf("   Current Amount: $%,d%n", myStatus.getCurrentAmount());
        System.out.printf("   Current Round Bid: $%s%n", 
                myStatus.getCurrentRoundBid() != null ? String.format("%,d", myStatus.getCurrentRoundBid()) : "0");
        System.out.printf("   Last Move This Round: %s%n", 
                myStatus.getLastMoveTypeInCurrentRound() != null ? myStatus.getLastMoveTypeInCurrentRound() : "None");
        
        // Bids per round
        System.out.println("\n💰 BIDS PER ROUND:");
        List<Integer> bidsPerRound = myStatus.getBidsPerRound();
        for (int i = 0; i < bidsPerRound.size(); i++) {
            Integer bid = bidsPerRound.get(i);
            System.out.printf("   Round %d: $%s%n", i + 1, bid != null ? String.format("%,d", bid) : "0");
        }

        System.out.println("\n" + "─".repeat(102));
    }

    /**
     * Print community cards in a nice format
     */
    private void printCommunityCards(List<Card> communityCards) {
        if (communityCards == null || communityCards.isEmpty()) {
            System.out.println("   No cards revealed yet");
            return;
        }
        
        System.out.print("   ");
        for (Card card : communityCards) {
            System.out.print(formatCard(card) + "  ");
        }
        System.out.println();
    }

    /**
     * Print your hole cards in a nice format
     */
    private void printYourCards(List<Card> myCards) {
        System.out.print("   ");
        if (myCards != null && myCards.size() >= 2) {
            for (Card card : myCards) {
                System.out.print(formatCard(card) + "  ");
            }
            System.out.println();
        } else {
            System.out.println("Invalid cards");
        }
    }

    /**
     * Format a card for display (e.g., "A♥")
     */
    private String formatCard(Card card) {
        String rank = switch (card.getRank()) {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(card.getRank());
        };

        String suit = switch (card.getSuite()) {
            case Heart -> "♥";
            case Diamond -> "♦";
            case Club -> "♣";
            case Spade -> "♠";
        };

        return String.format("[%2s%s]", rank, suit);
    }

    /**
     * Prompt player to select their move
     */
    private MoveType promptForMoveType() {
        System.out.println("\n" + "▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼".repeat(2));
        System.out.println("SELECT YOUR MOVE:");
        System.out.println("  0 - FOLD  (Exit the current hand)");
        System.out.println("  1 - CALL  (Match the current highest bid)");
        System.out.println("  2 - RAISE (Increase the bid)");
        System.out.println("  3 - CHECK (No bet, pass to next player)");
        System.out.print("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲".repeat(2) + "\n");
        
        System.out.print("Enter move number (0-3): ");
        int moveIndex = -1;
        
        try {
            moveIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (moveIndex < 0 || moveIndex > 3) {
                System.out.println("❌ Invalid move index. Please try again.");
                return promptForMoveType();
            }
            
            return MoveType.values()[moveIndex];
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a number between 0 and 3.");
            scanner.nextLine(); // Clear buffer
            return promptForMoveType();
        }
    }

    /**
     * Prompt for bet amount
     */
    private int promptForBetAmount(GameStateInfo gameState, GamePlayerStatusInfo myStatus, MoveType moveType) {
        int highestBid = gameState.getHighestCurrentBid();
        Integer currentRoundBid = myStatus.getCurrentRoundBid();
        int currentContribution = currentRoundBid != null ? currentRoundBid : 0;
        
        System.out.println("\n💵 BET INFORMATION:");
        System.out.printf("   Highest current bid: $%d%n", highestBid);
        System.out.printf("   Your current contribution this round: $%d%n", currentContribution);
        System.out.printf("   Amount needed to call: $%d%n", Math.max(0, highestBid - currentContribution));
        System.out.printf("   Your available amount: $%,d%n", myStatus.getCurrentAmount());
        
        System.out.print("\nEnter bet amount: $");
        
        try {
            int betAmount = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (betAmount < 0) {
                System.out.println("❌ Bet amount cannot be negative. Please try again.");
                return promptForBetAmount(gameState, myStatus, moveType);
            }
            
            if (betAmount > myStatus.getCurrentAmount()) {
                System.out.println("❌ Bet amount exceeds your available amount. Please try again.");
                return promptForBetAmount(gameState, myStatus, moveType);
            }
            
            return betAmount;
        } catch (Exception e) {
            System.out.println("❌ Invalid input. Please enter a valid number.");
            scanner.nextLine(); // Clear buffer
            return promptForBetAmount(gameState, myStatus, moveType);
        }
    }

    /**
     * Print text centered in terminal
     */
    private void printCentered(String text, int width) {
        int padding = (width - text.length()) / 2;
        System.out.println("║" + " ".repeat(padding) + text + " ".repeat(width - padding - text.length()) + "║");
    }
}

