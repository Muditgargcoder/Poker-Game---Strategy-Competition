package com.mudit.poker.GamePlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mudit.poker.Combinations.CombinationCalculator;
import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.CombinationResult;
import com.mudit.poker.Pojos.GameAudit;
import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameResult;
import com.mudit.poker.Pojos.GameState;
import com.mudit.poker.Pojos.MoveType;
import com.mudit.poker.Pojos.Player;
import com.mudit.poker.Pojos.PlayerMove;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PokerGamePlay {

    final int startingMoney;
    final List<Player> players;
    final int NUM_GAME_PLAYS;
    final int ROUNDS = 3; // Three card revealing rounds in 1 game of poker
    final int FIRST_ROUND_FEE = 5;
    int firstPlayer = 0; // Changes in round-robbin fashion
    int currentGamePlayNumber;
    GameState gameState; // currentGameState;
    List<Card> communityCards = new ArrayList<>();
    List<GameAudit> gameAudit = new ArrayList<>();

    public void run() throws Exception {
        for (Player player : players) {
            player.setCurrentAmount(startingMoney);
        }
        for (int i = 1; i <= NUM_GAME_PLAYS; i++) {
            gameState = new GameState(players, firstPlayer);
            currentGamePlayNumber = i;

            initializeSingleGamePlay();

            firstPlayer = (firstPlayer + 1) % players.size();
        }
        prettyPrintAudit();
    }

    void initializeSingleGamePlay() throws Exception {
        SetupGame.distributeCards(communityCards, players);
        for (Player player : players) {
            player.reduceMoney(FIRST_ROUND_FEE); // TODO: removed players if in a end game they have less than
                                                 // FIRST_ROUND_FEE
        }
        gameState.setTotalPot(FIRST_ROUND_FEE * players.size());
        runSingleGamePlay();
        GameResult gameResult = computeWinnerAndGivePrize();
        recordAudit(gameResult.getAllPlayerResult());
    }

    void runSingleGamePlay() throws Exception {
        int currentPlayerIdx = firstPlayer;
        for (int round = 1; round <= ROUNDS; round++) {
            updateGameRound(round);
            // 3 cards revealed in first round, then 1 card for next 2 rounds each
            while (isRoundFinished()) { // untill all players play atleast once and bids matches
                Player currentPlayer = players.get(currentPlayerIdx);
                if (hasPlayerFolded(currentPlayer))
                    continue;
                PlayerMove playerMove = currentPlayer.getStrategy().makeYourMove(gameState,
                        currentPlayer.getCurrentGameStatus(), currentPlayer.getAssignedCards());
                playerMove.setPlayerId(currentPlayer.getId());
                validatePlayerMove(currentPlayer, playerMove);
                gameState = updateGameState(currentPlayer, playerMove, round);
                notifyAllPlayers(playerMove);
                currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
            }
        }
    }

    boolean isRoundFinished() {
        boolean hasEveryonePlayedOnce = gameState.getPlayerStatusesMap().values().stream()
                .allMatch(playerStatus -> playerStatus.getLastMoveTypeInCurrentRound() != null);
        if (!hasEveryonePlayedOnce)
            return false;

        boolean nonFoldedPlayersBidsMatched = gameState.getPlayerStatusesMap().values().stream()
                .filter(playerStatus -> playerStatus.getLastMoveTypeInCurrentRound() != MoveType.FOLD)
                .allMatch(playerStatus -> playerStatus.getCurrentRoundBid() == gameState.getHighestCurrentBid());

        return hasEveryonePlayedOnce && nonFoldedPlayersBidsMatched;
    }

    GameResult computeWinnerAndGivePrize() {
        List<Player> playersTillFinalRound = players.stream().filter(p -> !hasPlayerFolded(p)).toList();
        GameResult gameResult = CombinationCalculator.getGameResult(communityCards, playersTillFinalRound);
        // Can be multiple winners with a tie
        var winners = gameResult.getWinners();
        int winMoney = gameState.getTotalPot() / winners.size();
        for (Map.Entry<Player, CombinationResult> entry : winners) {
            Player winner = entry.getKey();
            winner.addMoney(winMoney);
        }
        return gameResult;
    }

    GameState updateGameState(Player currentPlayer, PlayerMove playerMove, int round) {
        currentPlayer.reduceMoney(playerMove.getBetAmount());
        gameState.getPlayerMoves().get(round - 1).add(playerMove);
        gameState.setTotalPot(gameState.getTotalPot() + playerMove.getBetAmount());

        GamePlayerStatus playerStatus = gameState.getPlayerStatusesMap().get(playerMove.getPlayerId());
        playerStatus.setLastMoveTypeInCurrentRound(playerMove.getMoveType());

        // player may have bid before in this same round
        int newCurrentRoundBid = Optional.ofNullable(playerStatus.getCurrentRoundBid()).orElse(0) + playerMove.getBetAmount();

        playerStatus.getBidsPerRound().set(gameState.getRound() - 1, newCurrentRoundBid);
        gameState.setHighestCurrentBid(Math.max(gameState.getHighestCurrentBid(), newCurrentRoundBid));
        return gameState;
    }

    void updateGameRound(int round) {
        gameState.setRound(round);
        gameState.setHighestCurrentBid(0);
        gameState.resetBidsOnRoundStart();
        gameState.setRevealedCommunityCards(communityCards.subList(0, 3 + (round - 1)));
    }

    boolean validatePlayerMove(Player player, PlayerMove playerMove) throws Exception {
        String baseError = "Invalid move by Player " + playerMove.getPlayerId() + ", ";
        MoveType moveType = playerMove.getMoveType();
        int betAmount = playerMove.getBetAmount();
        Integer playerExistingBid = player.getCurrentGameStatus().getCurrentRoundBid();
        int netCurrentBid = Optional.of(playerExistingBid).orElse(0) + betAmount;
        if (moveType == MoveType.FOLD)
            return true;
        if (moveType == MoveType.CHECK) {
            if (betAmount != 0)
                throw new Exception(baseError + "Can't bet an amount > 0 with CHECK");
            if (playerExistingBid != gameState.getHighestCurrentBid()) {
                throw new Exception(baseError + "Can't CHECK, player bid doesn't matches highest bid");
            }
        }
        if (moveType == MoveType.RAISE) {
            // if (betAmount == 0)
            // throw new Exception(baseError + "Can't RAISE with 0 bet");
            if (netCurrentBid < gameState.getHighestCurrentBid())
                throw new Exception(baseError + "Player bet after RAISE needs to exceed current highest bid");
        }
        if (moveType == MoveType.CALL) {
            if (netCurrentBid != gameState.getHighestCurrentBid())
                throw new Exception(baseError + "CALL needs to match the current highest bid");
        }

        return true;
    }

    void notifyAllPlayers(PlayerMove playerMove) {
        for (Player player : players) {
            player.getStrategy().onNewMove(playerMove, gameState, player.getCurrentGameStatus(),
                    player.getAssignedCards());
        }
    }

    boolean hasPlayerFolded(Player currentPlayer) {
        return gameState.getPlayerStatusesMap().get(currentPlayer.getId())
                .getLastMoveTypeInCurrentRound() == MoveType.FOLD;
    }

    void recordAudit(List<Map.Entry<Player, CombinationResult>> allPlayerResultsSorted) {
        List<Map.Entry<Player, CombinationResult>> combinationResultsCopy = allPlayerResultsSorted.stream()
                .map(e -> {
                    Player p = e.getKey();
                    return Map.entry(new Player(p.getId(), p.getCurrentAmount(), null), e.getValue()); // making copy of
                                                                                                       // player to keep
                                                                                                       // audit.
                }).toList();
        GameAudit audit = new GameAudit(combinationResultsCopy, gameState);
        gameAudit.add(audit);
    }

    void prettyPrintAudit() {
        GameAuditPrinter.printAuditReport(gameAudit, startingMoney);
    }
}
