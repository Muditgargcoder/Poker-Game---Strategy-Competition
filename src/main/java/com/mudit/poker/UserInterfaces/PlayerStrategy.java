package com.mudit.poker.UserInterfaces;

import java.util.List;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameResult;
import com.mudit.poker.Pojos.PlayerMove;

public interface PlayerStrategy {
    /**
     * This function is invoked whenver its your turn in the game
     * @param gameState : {@link GameStateInfo} Contains all metadata of the game state and player moves
     * @param myStatus : {@link GamePlayerStatus} Whats your current status in the game
     * @param myCards : {@link Card} The two cards assigned to you in the game
     * @return {@link PlayerMove} Your currentMove
     * Note: 
     * You can get your playerId = myStatus.getPlayerId()
     * You get copies of parameters. Modifying them won't disturb the game engine.
     */
    PlayerMove makeYourMove(GameStateInfo gameState, GamePlayerStatusInfo myStatus, List<Card> myCards);

    /**
     * This function is invoked when any player completes a turn (including yourself)
     */
    void onNewMove(PlayerMove otherPlayerMove, GameStateInfo newGameState, GamePlayerStatusInfo myStatus, List<Card> myCards);

    /**
     * @param gameResult Stores 
     * Note: Don't modify it as its used for Audit Purpose
     */
    void onSingleGameEnd(GameResult gameResult);
}
