package com.mudit.poker.Pojos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GamePlayerStatus {
    Integer currentAmount;
    int playerId;
    int currentRoundBid;
    List<Integer> bidsPerRound;
    MoveType lastMoveType;
}
