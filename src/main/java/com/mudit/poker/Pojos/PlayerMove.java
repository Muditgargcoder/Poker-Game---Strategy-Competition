package com.mudit.poker.Pojos;

import lombok.Data;

@Data
public class PlayerMove {
    MoveType moveType;
    int betAmount;
    int playerId;

    public PlayerMove(MoveType moveType, int betAmount) {
        this.betAmount = betAmount;
        this.moveType = moveType;
    }
}
