package io.zipcoder.casino.games;

import io.zipcoder.casino.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class GoingToBoston extends DiceGame{
    public static String gameName = "Going to Boston";
    int[] highestRolls;
    Map<Player, Integer> nPCScores;
    String[] nPCNames = new String[]{"Jyothi", "Lake", "Shane"};
    Player currentPlayer;
    int playerScore;
    Boolean playing;
    String diceUnicode = "\u2680 \u2681 \u2682 \u2683 \u2684 \u2685 ";
    String diceBorder = IntStream.range(0, 6).mapToObj(i -> diceUnicode).collect(Collectors.joining(""));

    public GoingToBoston() {
        super();
        playing = true;
    }

    void printIntroduction() {
        console.print(diceBorder+
            "\n                     Welcome to Going to Boston!                     \n"+
            diceBorder+
            "\n                             How to play:                              "+
            "\n  \u2680 Each player has three turns."+
            "\n  \u2681 Each turn, six-sided dice are rolled and the number of the highest "+
            "\n    die is added to the player's total."+
            "\n  \u2682 On the first turn, the player rolls three dice. "+
            "\n  \u2683 On the second turn, the player rolls two and on the last turn, "+
            "\n    the player rolls only one."+
            "\n  \u2684 The player with the highest total wins!\n");
    }

    @Override
    public void resetGame() {
        console.println(diceBorder);
        int numOfOpponents = getNumberOfOpponents();
        createNPCs(numOfOpponents);
    }

    int getNumberOfOpponents() {
        int userInput = console.getIntegerInput("Would you like to face 1, 2, or 3 opponents?");
        while(!(userInput >= 1 && userInput <= 3)) {
            userInput = console.getIntegerInput("Choose 1, 2, or 3 opponents to face.");
        }
        return userInput;
    }

    @Override
    public void quitGame() {
        playing = false;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public void startGame(Player player) {
        currentPlayer = player;
        printIntroduction();
        while (playing) {
            playGame();
            String userInput = console.getStringInput("Play again? Yes | No");
            isStillPlaying(userInput);
        }
    }

    void playGame() {
        resetGame();
        playerScore = playRound(currentPlayer);
        playNPCRound();
        printScores();
        findWinner();
    }

    void isStillPlaying(String userInput) {
        while(true) {
            if (userInput.equalsIgnoreCase("Yes")) {
                break;
            } else if (userInput.equalsIgnoreCase("No")) {
                quitGame();
                break;
            } else {
                console.println("We didn't quite catch that.");
                userInput = console.getStringInput("Play again? Yes | No");
            }
        }
    }

    void createNPCs(Integer numOfNPCs) {
        nPCScores = new LinkedHashMap<>(numOfNPCs);
        for (int i = 0; i < numOfNPCs; i++) {
            nPCScores.put(new Player(nPCNames[i], 0), null);
        }
    }

    int playRound(@NotNull Player player) {
        highestRolls = new int[3];
        int numOfDie = 3;
        getRolls(numOfDie, player.getName());
        return IntStream.of(highestRolls).sum();
    }

    void getRolls(int numOfDie, String name) {
        console.println(diceBorder);
        for (int i = 0; i < highestRolls.length; i++) {
            ArrayList<Integer> rolls = rollDice(numOfDie);
            printRolls(numOfDie, name, rolls);
            addHighestRoll(i, rolls);
            numOfDie--;
        }
    }

    void printRolls(int numOfDie, String name, ArrayList<Integer> rolls) {
        String[] dice = new String[]{"    \u2680", "  \u2681 \u2680", "\u2682 \u2681 \u2680"};
        console.print(dice[numOfDie-1]+" | "+ name +" rolled");
        for (Integer roll : rolls) {
            console.print( " "+roll);
        }
        String[] space = new String[]{"    ", "  ", ""};
        console.print(space[numOfDie-1]);
    }

    void addHighestRoll(int i, ArrayList<Integer> rolls) {
        int highest = Collections.max(rolls);
        console.println(" | "+highest+" is the highest.");
        highestRolls[i] = highest;
    }

    void playNPCRound() {
        Set<Player> players = nPCScores.keySet();
        for (Player nPC : players) {
            nPCScores.put(nPC, playRound(nPC));
        }
    }

    void printScores() {
        console.println(diceBorder);
        console.println(currentPlayer.getName()+" scored "+playerScore+". ");
        nPCScores.forEach ((npc, score) -> console.println(npc.getName()+" scored "+score+". "));
    }

    void findWinner() {
        Player nPC = Collections.max(nPCScores.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        printWinner(nPC);
    }

    void printWinner(Player nPC) {
        console.println(diceBorder);
        if (playerScore > nPCScores.get(nPC)) {
            console.println(currentPlayer.getName()+" won!");
        } else if (playerScore == nPCScores.get(nPC)) {
            console.println(currentPlayer.getName()+" tied!");
        } else {
            console.println(currentPlayer.getName()+" lost...");
        }
    }
}
