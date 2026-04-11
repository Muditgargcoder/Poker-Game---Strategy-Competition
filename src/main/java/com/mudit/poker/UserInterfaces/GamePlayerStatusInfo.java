package com.mudit.poker.UserInterfaces;

import java.util.List;

import com.mudit.poker.Pojos.MoveType;

public interface GamePlayerStatusInfo {
        /** Net money player currently has */
    public Integer getCurrentAmount();
    
    public int getPlayerId();

    /** null if player has not bid yet in this round*/
    public Integer getCurrentRoundBid();

    /** List of size 3 denoting net bids per round. Element would be Null if not bid for a round */
    public List<Integer> getBidsPerRound();

    /** Last move type in current round. Null if not played yet in this round. <br> If player folded in this round, this value will be FOLD for all further rounds */
    public MoveType getLastMoveTypeInCurrentRound();
}
