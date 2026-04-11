package com.mudit.poker.GamePlay;

import java.util.Arrays;
import java.util.List;

import com.mudit.poker.Algos.ManualPlayStrategy;
import com.mudit.poker.Algos.SampleStrategy;
import com.mudit.poker.Algos.Mudit.MuditStrategy;
import com.mudit.poker.Pojos.Player;

public class MainApplication {

    public static void main(String[] args) throws Exception {
        int INITIAL_MONEY = 1000;
        int NUM_GAME_PLAYS = 1;
        List<Player> players = Arrays.asList(
                new Player(1, new SampleStrategy()),
                // new Player(2, new ManualPlayStrategy())
                new Player(2, new SampleStrategy())
            );

        PokerGamePlay game = new PokerGamePlay(INITIAL_MONEY, players, NUM_GAME_PLAYS);
        game.run();
    }

}
