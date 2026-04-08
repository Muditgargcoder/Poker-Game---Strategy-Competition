package com.mudit.poker.Pojos;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameResult {
    List<Map.Entry<Player, CombinationResult>> winners;
    List<Map.Entry<Player, CombinationResult>> allPlayerResult;
}
