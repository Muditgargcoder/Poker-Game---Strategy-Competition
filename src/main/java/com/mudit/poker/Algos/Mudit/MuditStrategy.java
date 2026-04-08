package com.mudit.poker.Algos.Mudit;

import com.mudit.poker.Algos.PlayerStrategy;
import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameState;
import com.mudit.poker.Pojos.MoveType;
import com.mudit.poker.Pojos.PlayerMove;

public class MuditStrategy implements PlayerStrategy {

    @Override
    public PlayerMove makeYourMove(GameState gameState, GamePlayerStatus myStatus) {
        int myPlayerId = myStatus.getPlayerId();
        int myCurrentBid = myStatus.getCurrentRoundBid();
        int currentHighestBid = gameState.getHighestCurrentBid();
        Double averageBidsOfOtherPlayers = gameState.getPlayerStatuses().stream().mapToInt(player -> player.getCurrentRoundBid()).average().orElse(0);
        return new PlayerMove(MoveType.RAISE, (currentHighestBid - myCurrentBid) + averageBidsOfOtherPlayers.intValue());
    }

    @Override
    public void onNewMove(GameState gameState, GamePlayerStatus myStatus) {
        
    }

}
