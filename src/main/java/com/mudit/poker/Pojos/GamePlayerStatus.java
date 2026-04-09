package com.mudit.poker.Pojos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GamePlayerStatus {
    
    /** Net money player currently has */
    Integer currentAmount;
    
    int playerId;

    /** null if player has not bid yet in this round*/
    Integer currentRoundBid;

    /** List of size 3 denoting net bids per round. Element would be Null if not bid for a round */
    List<Integer> bidsPerRound;

    /** Last move type in current round. Null if not played yet in this round. <br> If player folded in this round, this value will be FOLD for all further rounds */
    MoveType lastMoveTypeInCurrentRound;
}