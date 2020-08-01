package io.zipcoder.casino.games;

import io.zipcoder.casino.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class GoingToBostonTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private GoingToBoston testGTB;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testGTB = new GoingToBoston();
        testPlayer = new Player("Lake", 1000);
    }

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));

    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void printIntroduction() {
        testGTB.printIntroduction();
        String expected = "Welcome to Going to Boston!\nHow to play: Each player has three turns.\nEach turn, a six-sided dice are " +
                              "rolled and the number of the highest \ndie is added to the player's total.\nOn the first turn, the " +
                              "player rolls three dice, on the second turn, the player rolls two, and on the last " +
                              "turn, \nthe player rolls only one.\nThe player with the highest total wins!\n" +
                              "< | > | < > | < | > | < > | < | > | < > | < | > | < >\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void getNumberOfOpponents() {
        Integer expected = 3;
        Integer actual = testGTB.getNumberOfOpponents(3);
        assertEquals(expected, actual);
    }

    @Test
    void quitGame() {
        testGTB.quitGame();
        assertFalse(testGTB.playing);
    }

    @Test
    void getGameName() {
        String expected = "Going to Boston";
        String actual = testGTB.getGameName();
        assertEquals(expected, actual);
    }

    @Test
    void createNPCs() {
        testGTB.createNPCs(2);
        Player expected = new Player("Jyothi", 0);
        Player actual = (Player) testGTB.nPCScores.keySet().toArray()[0];
        assertEquals(expected, actual);
    }

    @Test
    void playRound() {
        assertNull(testGTB.highestRolls);
        int actual = testGTB.playRound(testPlayer);
        assertTrue(3 <= actual && actual <=18);
    }

    @Test
    void playRound2() {
        testGTB.playRound(testPlayer);
        assertNotEquals(null, testGTB.highestRolls);
    }

    @Test
    void printRolls() {
        ArrayList<Integer> testRolls = new ArrayList<>(Arrays.asList(1, 2, 3));
        testGTB.printRolls(3, "Lake", testRolls);
        String expected = "Roll 3 | Lake rolled 1 2 3";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void addHighestRoll() {
        testGTB.highestRolls = new int[3];
        ArrayList<Integer> testRolls = new ArrayList<>(Arrays.asList(1, 2, 3));
        testGTB.addHighestRoll(0, testRolls);
        String expected = " | 3 is the highest.\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void addHighestRoll2() {
        testGTB.highestRolls = new int[3];
        ArrayList<Integer> testRolls = new ArrayList<>(Arrays.asList(1, 2, 3));
        testGTB.addHighestRoll(0, testRolls);
        Integer expected = 3;
        Integer actual = testGTB.highestRolls[0];
        assertEquals(expected, actual);
    }

    @Test
    void playNPCRound() {
        testGTB.createNPCs(1);
        assertNull(testGTB.nPCScores.values().toArray()[0]);
        testGTB.playNPCRound();
        assertNotNull(testGTB.nPCScores.values().toArray()[0]);
    }

    @Test
    void isStillPlaying() {
        testGTB.isStillPlaying("No");
        assertFalse(testGTB.playing);
    }

    @Test
    void isStillPlaying2() {
        testGTB.isStillPlaying("Yes");
        assertTrue(testGTB.playing);
    }

    @Test
    void findWinner() {
        testGTB.currentPlayer = testPlayer;
        testGTB.createNPCs(3);
        testGTB.nPCScores.replaceAll((player, score) -> score = 15);
        testGTB.playerScore = 20;
        testGTB.findWinner();
        String expected = "You won!\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void findWinner2() {
        testGTB.currentPlayer = testPlayer;
        testGTB.createNPCs(3);
        testGTB.nPCScores.replaceAll((player, score) -> score = 15);
        testGTB.playerScore = 15;
        testGTB.findWinner();
        String expected = "You tied!\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void findWinner3() {
        testGTB.currentPlayer = testPlayer;
        testGTB.createNPCs(3);
        testGTB.nPCScores.replaceAll((player, score) -> score = 15);
        testGTB.playerScore = 14;
        testGTB.findWinner();
        String expected = "You lost...\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void printScores() {
        testGTB.currentPlayer = testPlayer;
        testGTB.createNPCs(3);
        testGTB.nPCScores.replaceAll((player, score) -> score = 15);
        testGTB.playerScore = 20;
        String expected = "< | > | < > | < | > | < > | < | > | < > | < | > | < >\n" +
                              "You scored 20. \n" + "Jyothi scored 15. \n" + "Lake scored 15. \n" +
                              "Shane scored 15. \n";
        testGTB.printScores();
        String actual = outContent.toString();
        assertEquals(expected, actual);
    }
}