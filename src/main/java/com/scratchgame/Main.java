package com.scratchgame;

//import java.util.Scanner;


import com.google.gson.JsonParseException;


public class Main {
    public static void main(String[] args) {



        // Scanner reader = new Scanner(System.in); 
        // System.out.println("Enter Bet Amount: ");
        // int bettingAmount = reader.nextInt(); 
        // reader.close();





        if (args.length != 4 || !args[0].equals("configFile") || !args[2].equals("betAmount")) {
            System.err.println("Usage: java -jar target/scratch-game-1.0-SNAPSHOT-jar-with-dependencies.jar configFile config.json betAmount 100");
            System.exit(1);
        }

// System.out.println(args[0]);
// System.out.println(args[1]);
// System.out.println(args[2]);
// System.out.println(args[3]);
//         System.out.println(args[1]);
        String configPath = args[1];
        
        int bettingAmount = Integer.parseInt(args[3]);

        try {
            Config config = ConfigLoader.load(configPath);
            GameEngine engine = new GameEngine(config);
            engine.play(bettingAmount);
        } catch (JsonParseException e) {
            System.err.println("Invalid JSON.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
