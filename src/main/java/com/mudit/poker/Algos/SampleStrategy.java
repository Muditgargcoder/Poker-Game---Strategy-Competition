package com.mudit.poker.Algos;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.GameResult;
import com.mudit.poker.Pojos.MoveType;
import com.mudit.poker.Pojos.PlayerMove;
import com.mudit.poker.UserInterfaces.GamePlayerStatusInfo;
import com.mudit.poker.UserInterfaces.GameStateInfo;
import com.mudit.poker.UserInterfaces.PlayerStrategy;

public class SampleStrategy implements PlayerStrategy {

    @Override
    public PlayerMove makeYourMove(GameStateInfo gameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        int myCurrentBid = Optional.ofNullable(myStatus.getCurrentRoundBid()).orElse(0);
        int currentHighestBid = gameState.getHighestCurrentBid();
        return new PlayerMove(MoveType.CALL, currentHighestBid - myCurrentBid);
    }

    @Override
    public void onNewMove(PlayerMove otherPlayerMove, GameStateInfo newGameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        
    }

    @Override
    public void onSingleGameStart(HashMap<Integer, Integer> moneyOnPlayers) {
        
    }

    @Override
    public void onSingleGameEnd(GameResult gameResult) {

    }
    
}
