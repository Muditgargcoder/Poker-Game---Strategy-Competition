package com.mudit.poker.Algos.Mudit;

import java.util.List;
import java.util.Optional;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.MoveType;
import com.mudit.poker.Pojos.PlayerMove;
import com.mudit.poker.UserInterfaces.GamePlayerStatusInfo;
import com.mudit.poker.UserInterfaces.GameStateInfo;
import com.mudit.poker.UserInterfaces.PlayerStrategy;

public class MuditStrategy implements PlayerStrategy {

    @Override
    public PlayerMove makeYourMove(GameStateInfo gameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        int myPlayerId = myStatus.getPlayerId();
        int myCurrentBid = Optional.ofNullable(myStatus.getCurrentRoundBid()).orElse(0);
        int currentHighestBid = gameState.getHighestCurrentBid();
        Double averageBidsOfOtherPlayers = gameState.getPlayerStatuses().stream().mapToInt(player -> player.getCurrentRoundBid()).average().orElse(0);
        return new PlayerMove(MoveType.RAISE, (currentHighestBid - myCurrentBid) + averageBidsOfOtherPlayers.intValue());
    }

    @Override
    public void onNewMove(PlayerMove otherPlayerMove, GameStateInfo newGameState, GamePlayerStatusInfo myStatus, List<Card> myCards) {
        
    }

}
