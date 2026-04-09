package com.mudit.poker.Algos;

import java.util.List;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameState;
import com.mudit.poker.Pojos.PlayerMove;

public interface PlayerStrategy {
    /**
     * This function is invoked whenver its your turn in the game
     * @param gameState : {@link GameState} Contains all metadata of the game state and player moves
     * @param myStatus : {@link GamePlayerStatus} Whats your current status in the game
     * @param myCards : {@link Card} The two cards assigned to you in the game
     * @return {@link PlayerMove} Your currentMove
     * Note: You can get your playerId = myStatus.getPlayerId()
     */
    PlayerMove makeYourMove(GameState gameState, GamePlayerStatus myStatus, List<Card> myCards);

    /**
     * This function is invoked when any player completes a turn (including yourself)
     */
    void onNewMove(PlayerMove otherPlayerMove, GameState newGameState, GamePlayerStatus myStatus, List<Card> myCards);
}
