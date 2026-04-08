package com.mudit.poker.Pojos;

import java.util.List;

import com.mudit.poker.Combinations.Combination;

import lombok.Data;

@Data
public class CombinationResult {
    final Combination combination;
    final List<Card> highCards;
    boolean isWinner = false;
}
