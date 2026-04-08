package com.mudit.poker.Pojos;

import java.util.Objects;

import lombok.Data;

@Data
public class Card implements Comparable<Card> {
    final Suite suite;
    final int rank;

    @Override
    public int compareTo(Card other) {
        return -Integer.compare(this.getRank(), other.getRank());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        Card otherCard = (Card) other;
        return suite == otherCard.getSuite() && rank == otherCard.getRank();
    }

    @Override
    public int hashCode() {
        return Objects.hash(suite, rank);
    }
}