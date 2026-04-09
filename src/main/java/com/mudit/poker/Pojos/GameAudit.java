package com.mudit.poker.Pojos;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class GameAudit {
    final List<Map.Entry<Player, CombinationResult>> playerResults;
    final GameState gameState;
}
