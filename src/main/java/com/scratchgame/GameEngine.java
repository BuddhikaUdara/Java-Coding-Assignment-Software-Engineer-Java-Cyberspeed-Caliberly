package com.scratchgame;


import com.scratchgame.Config.Symbol;

import com.google.gson.Gson;

import java.util.*;

public class GameEngine {


    private final Config config;
    private final Random random = new Random();

    // INIT engine with game configuration.
    public GameEngine(Config config) {
        this.config = config;
    }

    public void play(int betAmount) {

        // MATRIX Default 3x3 if not specified
        int rows = config.rows != null ? config.rows : 3;
        int columns = config.columns != null ? config.columns : 3;

        String[][] matrix = new String[rows][columns]; // 2D

        Map<String, Integer> symbolCount = new HashMap<>();
        Set<String> bonusSymbols = new HashSet<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                //GET SYMBOLS AND PLACE THEM TO MATRIX.
                String symbol = getSymbol(config.probabilities.standard_symbols, c, r);
                
                if (symbol == null) 
                {
                    symbol = getSymbol(config.probabilities.standard_symbols, 0, 0);
                    }
                matrix[r][c] = symbol;
                // KEEP A COUNT 
                symbolCount.put(symbol, symbolCount.getOrDefault(symbol, 0) + 1);
                
            }
        }

        // Add a bonus symbol in a random position
        int br = random.nextInt(rows);
        int bc = random.nextInt(columns);
        String bonus = getBonusSymbol();
        matrix[br][bc] = bonus;
        bonusSymbols.add(bonus);

        Map<String, List<String>> appliedCombinations = new HashMap<>();
        double totalReward = 0.0;

        for (Map.Entry<String, Integer> entry : symbolCount.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();
            Symbol s = config.symbols.get(symbol);
            if (s != null && "standard".equalsIgnoreCase(s.type)) {
                double symbolReward = betAmount * s.reward_multiplier;
                double comboMultiplier = 1.0;

                for (Map.Entry<String, Config.WinCombination> wc : config.win_combinations.entrySet()) {
                    Config.WinCombination rule = wc.getValue();
                    if ("same_symbols".equals(rule.when) && count >= rule.count) {
                        comboMultiplier *= rule.reward_multiplier;
                        appliedCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(wc.getKey());
                    }
                }

                totalReward += symbolReward * comboMultiplier;
            }
        }

        // Apply bonus symbol
        String appliedBonus = bonusSymbols.iterator().next();
        Symbol bonusSym = config.symbols.get(appliedBonus);
        if (bonusSym != null && "bonus".equalsIgnoreCase(bonusSym.type) && totalReward > 0) {
            if ("multiply_reward".equalsIgnoreCase(bonusSym.impact)) {
                totalReward *= bonusSym.reward_multiplier;
            } else if ("extra_bonus".equalsIgnoreCase(bonusSym.impact)) {
                totalReward += bonusSym.extra;
            }
        } else if (bonusSym == null || "miss".equalsIgnoreCase(bonusSym.impact)) {
            appliedBonus = null;
        }

        Map<String, Object> output = new LinkedHashMap<>();
        output.put("matrix", matrix);
        output.put("reward", (int) totalReward);
        output.put("applied_winning_combinations", appliedCombinations);
        output.put("applied_bonus_symbol", appliedBonus);

        System.out.println(new Gson().toJson(output));
    }

    private String getSymbol(List<Config.StandardSymbolEntry> entries, int col, int row) {
        
        for (Config.StandardSymbolEntry entry : entries) {
            if (entry.column == col && entry.row == row) {
                return getByProbability(entry.symbols);
            }
        }
        return null;
    }

    private String getBonusSymbol() {
        return getByProbability(config.probabilities.bonus_symbols.symbols);
    }

    private String getByProbability(Map<String, Integer> map) {
       
        int total = map.values().stream().mapToInt(i -> i).sum();
        int r = random.nextInt(total);
        int count = 0;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            count += e.getValue();
            if (r < count) return e.getKey();
        }
        return null;
    }
}
