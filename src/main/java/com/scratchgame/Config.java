package com.scratchgame;

import java.util.List;
import java.util.Map;

public class Config {
    public Integer columns;
    public Integer rows;
    public Map<String, Symbol> symbols;

    
    public Probabilities probabilities;
    public Map<String, WinCombination> win_combinations;

    public static class Symbol {
        public double reward_multiplier;  //"reward_multiplier": 5,
        public String type;  // "type": "standard"  "type": "bonus",
        public String impact; // "impact": "multiply_reward"  "impact": "extra_bonus" "impact": "miss"
        public int extra;  // "extra": 500,
    }

    // Probabilities = standard_symbols + bonus_symbols
    public static class Probabilities {
        public List<StandardSymbolEntry> standard_symbols;
        public BonusSymbols bonus_symbols;
    }


    // standard_symbols = column + row + symbols
    public static class StandardSymbolEntry {
        public int column;
        public int row;
        public Map<String, Integer> symbols;
    }

    // bonus_symbols = "symbols": {"10x": 1,"MISS": 5 }
    public static class BonusSymbols {
        public Map<String, Integer> symbols;
    }

    // win_combinations = reward_multiplier + when +  count +  group +  covered_areas
    public static class WinCombination {
        public double reward_multiplier;
        public String when;
        public int count;
        public String group;
        public List<List<String>> covered_areas;
    }
}
