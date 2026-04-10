package com.mudit.poker.Pojos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class GameState implements Cloneable {

    // Game MetaData
    /**
     * Round of a single Poker Game. Total 3 rounds.
     * In 1st round 3 cards are revealed. Then 1 card each for next 2 rounds.
     */
    int round = 1;

    /** First player to play in this game */
    int firstPlayer;

    /** Total money poured in the pot */
    int totalPot = 0;

    /** Highest current bid in this round. Resets to zero in new round. */
    Integer highestCurrentBid = 0;

    /** In first round 3 cards are revealed, then 1 in each further 2 rounds. */
    List<Card> revealedCommunityCards;

    // Player Moves Metadata
    /**
     * 1. playerMoves stores all moves played in all 3 rounds by all players <br>
     * 2. playerMoves.size() = 3 = num of rounds <br>
     * 3. playerMoves.get(0) -> Moves in round 1 in order of players playing starting
     * from the GameState.firstPlayer <br>
     * 4. Note: playerMoves.get(0).size() != no. of players. (A player can make more
     * than 1 move in a round, as round finishes only if non-folded players have
     * matched their bet)
     */
    List<List<PlayerMove>> playerMoves = new ArrayList<>();

    /** Map with key as player Id and value as GamePlayerStatus */
    Map<Integer, GamePlayerStatus> playerStatusesMap;

    /** Gameplay status of all players */
    List<GamePlayerStatus> playerStatuses;

    public GameState(List<Player> players, int firstPlayer) {
        playerStatusesMap = players.stream().collect(
                Collectors.toMap(
                        Player::getId,
                        player -> {
                            GamePlayerStatus playerStatus = new GamePlayerStatus(player.getCurrentAmount(),
                                    player.getId(), 0,
                                    Arrays.asList(new Integer[3]), null);
                            player.setCurrentGameStatus(playerStatus);
                            return playerStatus;
                        }));

        playerStatuses = playerStatusesMap.values().stream().toList();
        this.firstPlayer = firstPlayer;
        for (int i = 0; i < 3; i++) {
            this.playerMoves.add(new ArrayList<>());
        }
    }

    public void resetBidsOnRoundStart() {
        playerStatusesMap.forEach((playerId, playerStatus) -> {
            playerStatus.setCurrentRoundBid(null);
            playerStatus.setLastMoveTypeInCurrentRound(
                    playerStatus.getLastMoveTypeInCurrentRound() == MoveType.FOLD ? MoveType.FOLD : null);
        });
    }

    @Deprecated
    public void removeFoldedPlayersAtRoundEnd() {
        playerStatusesMap.forEach((playerId, playerStatus) -> {
            if (playerStatus.getLastMoveTypeInCurrentRound() == MoveType.FOLD) {
                playerStatusesMap.remove(playerId);
            }
        });
    }
}