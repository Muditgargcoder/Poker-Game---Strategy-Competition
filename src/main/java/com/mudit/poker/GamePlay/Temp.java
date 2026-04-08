package com.mudit.poker.GamePlay;

import java.util.ArrayList;
import java.util.List;

import com.mudit.poker.Pojos.Card;
import com.mudit.poker.Pojos.Suite;

public class Temp {
    List<Card> list;
    Integer t;
    List<Integer> l;

    void yom() {
        list = new ArrayList<>();
        l = new ArrayList<>();
        list.add(new Card(Suite.Club, 0));
        t = 1;
        l.add(t);
        ok(list);
        System.out.println(list);
        System.out.println(t);
        System.out.println(l);
    }

    void ok(List<Card> list) {
        // list = Arrays.asList(new Card(Suite.Club, 5));
        // list = new ArrayList<>();
        list.add(new Card(Suite.Heart, 5));
        System.out.println(list == this.list);
        
    }

    public static void main(String[] args) {
        Temp t = new Temp();
        t.yom();
    }
}
