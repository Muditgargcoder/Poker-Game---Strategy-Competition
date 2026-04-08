package com.mudit.poker.Algos;

import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameState;
import com.mudit.poker.Pojos.MoveType;
import com.mudit.poker.Pojos.PlayerMove;

public class SampleStrategy implements PlayerStrategy {

    @Override
    public PlayerMove makeYourMove(GameState gameState, GamePlayerStatus myStatus) {
        int myCurrentBid = myStatus.getCurrentRoundBid();
        int currentHighestBid = gameState.getHighestCurrentBid();
        return new PlayerMove(MoveType.CALL, currentHighestBid - myCurrentBid);
    }

    @Override
    public void onNewMove(GameState gameState, GamePlayerStatus myStatus) {
        
    }
    
}
