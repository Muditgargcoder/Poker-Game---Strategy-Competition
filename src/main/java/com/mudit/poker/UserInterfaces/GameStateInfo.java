package com.mudit.poker.UserInterfaces;

import java.util.List;
import java.util.Map;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.PlayerMove;

public interface GameStateInfo {
    // Game MetaData
    /**
     * Round of a single Poker Game. Total 3 rounds.
     * In 1st round 3 cards are revealed. Then 1 card each for next 2 rounds.
     */
    public int getRound();

    /** First player to play in this game */
    public int getFirstPlayer();

    /** Total money poured in the pot */
    public int getTotalPot();

    /** Highest current bid in this round. Resets to zero in new round. */
    public Integer getHighestCurrentBid();

    /** In first round 3 cards are revealed, then 1 in each further 2 rounds. */
    public List<Card> getRevealedCommunityCards();

    // Player Moves Metadata
    /**
     * 1. playerMoves stores all moves played in all 3 rounds by all players <br>
     * 2. playerMoves.size() = 3 = num of rounds <br>
     * 3. playerMoves.get(0) -> Moves in round 1 in order of players playing starting
     * from the GameState.firstPlayer <br>
     * 4. Note: playerMoves.get(0).size() != no. of players. (A player can make more
     * than 1 move in a round, as round finishes only if non-folded players have
     * matched their bet)
     */
    public List<List<PlayerMove>> getPlayerMoves();

    /** Map with key as player Id and value as GamePlayerStatus */
    public Map<Integer, ? extends GamePlayerStatusInfo> getPlayerStatusesMap();

    /** Gameplay status of all players */
    public List<? extends GamePlayerStatusInfo> getPlayerStatuses();
}
