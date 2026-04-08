package com.mudit.poker.Algos;

import com.mudit.poker.Pojos.GamePlayerStatus;
import com.mudit.poker.Pojos.GameState;
import com.mudit.poker.Pojos.PlayerMove;

public interface PlayerStrategy {
    PlayerMove makeYourMove(GameState gameState, GamePlayerStatus myStatus);
    void onNewMove(GameState gameState, GamePlayerStatus myStatus);
}
