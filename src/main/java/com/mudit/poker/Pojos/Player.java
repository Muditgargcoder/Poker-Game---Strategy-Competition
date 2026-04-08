package com.mudit.poker.Pojos;

import java.util.List;

import com.mudit.poker.Algos.PlayerStrategy;

import lombok.Data;

@Data
public class Player {
    int id;
    Integer currentAmount;
    List<Card> assignedCards; // starting 2 cards to play with.
    PlayerStrategy strategy;
    GamePlayerStatus currentGameStatus;

    public Player(int id, PlayerStrategy strategy) {
        this.id = id;
        this.strategy = strategy;
    }

    public Player(int id, int intialAmount, List<Card> assignedCards) {
        this.id = id;
        this.currentAmount = intialAmount;
        this.assignedCards = assignedCards;
    }

    public boolean reduceMoney(int amount) {
        if (currentAmount >= amount) {
            currentAmount -= amount;
            currentGameStatus.currentAmount -= amount;
            return true;
        }
        return false;
    }

    public void addMoney(int amount) {
        currentAmount += amount;
        currentGameStatus.currentAmount += amount;
    }
}