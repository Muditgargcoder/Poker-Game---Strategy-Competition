package com.mudit.poker.Pojos;

import lombok.Data;

@Data
public class PlayerMove implements Cloneable {
    final MoveType moveType;
    final int betAmount;
    int playerId;

    public PlayerMove(MoveType moveType, int betAmount) {
        this.betAmount = betAmount;
        this.moveType = moveType;
    }

    @Override
    public PlayerMove clone() {
        try {
            return (PlayerMove) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
