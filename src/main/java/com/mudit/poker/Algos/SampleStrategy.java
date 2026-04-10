package com.mudit.poker.Algos;

import java.util.List;
import java.util.Optional;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameState;
import com.mudit.poker.Pojos.MoveType;
import com.mudit.poker.Pojos.PlayerMove;

public class SampleStrategy implements PlayerStrategy {

    @Override
    public PlayerMove makeYourMove(GameState gameState, GamePlayerStatus myStatus, List<Card> myCards) {
        int myCurrentBid = Optional.ofNullable(myStatus.getCurrentRoundBid()).orElse(0);
        int currentHighestBid = gameState.getHighestCurrentBid();
        return new PlayerMove(MoveType.CALL, currentHighestBid - myCurrentBid);
    }

    @Override
    public void onNewMove(PlayerMove otherPlayerMove, GameState newGameState, GamePlayerStatus myStatus, List<Card> myCards) {
        
    }
    
}
