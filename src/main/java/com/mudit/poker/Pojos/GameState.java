package com.mudit.poker.Pojos;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class GameState {
    int round = 1; // 1 to 3
    List<Card> revealedCommunityCards; // In first round 3 cards are revealed, then 1 in each further 2 rounds.
    int totalPot = 0; // Total money poured in the pot
    int highestCurrentBid = 0; // resets to zero in each round
    PlayerMove lastPlayerMove;
    Map<Integer, GamePlayerStatus> playerStatusesMap;
    List<GamePlayerStatus> playerStatuses;

    public GameState(List<Player> players) {
        resetGameState(players);
        playerStatuses = playerStatusesMap.values().stream().toList();
    }

    public void resetGameState(List<Player> players) {
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

    }

    public void resetBidsOnRoundStart() {
        playerStatusesMap.forEach((playerId, playerStatus) -> {
            playerStatus.setCurrentRoundBid(-1);
            playerStatus.setLastMoveType(playerStatus.getLastMoveType() == MoveType.FOLD ? MoveType.FOLD : null);
        });
    }

    @Deprecated
    public void removeFoldedPlayersAtRoundEnd() {
        playerStatusesMap.forEach((playerId, playerStatus) -> {
            if (playerStatus.getLastMoveType() == MoveType.FOLD) {
                playerStatusesMap.remove(playerId);
            }
        });
    }
}