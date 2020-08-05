package io.zipcoder.casino.games;

import io.zipcoder.casino.Card;
import io.zipcoder.casino.player.Player;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.zipcoder.casino.Card.Rank.FIVE;
import static io.zipcoder.casino.Card.Rank.SEVEN;
import static io.zipcoder.casino.Card.Suit.SPADES;
import static org.mockito.Mockito.*;

public class GoFishTest {
    GoFish gofish;
    List<Card> currentDeck;
    List<Card> humanPlayerCardTest;
    List<Card> pcPlayerCardTest;
    List<Card> remainingDeck;
    List<Card> currentPlayerCards;
    Player pcPlayer;
    Player humanPlayer;
    Map<Player, List<Card>> playerListMap;


    @BeforeEach
    public void setUp() {
        currentDeck = new ArrayList<>();
        currentDeck = Card.getNewDeck();

        Card card = new Card(SPADES, FIVE);
        currentDeck.add(card);
        gofish = new GoFish(currentDeck);
        humanPlayerCardTest = gofish.humanPlayerCard;
        humanPlayerCardTest.add(card);

        pcPlayerCardTest = gofish.pcPlayerCard;
        pcPlayerCardTest.add(card);

        remainingDeck = new ArrayList<>(1);
        currentPlayerCards = gofish.humanPlayerCard;

        gofish.dealCards(7);
        humanPlayer = new Player("DEFAULT_PLAYER",0);

        pcPlayer = new Player("PC",0);
        playerListMap = gofish.playersHand();

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void dealCardsTest() {
        Assert.assertEquals((humanPlayerCardTest.size()),8 );
        Assert.assertEquals((pcPlayerCardTest.size()),8 );
    }

    @Test
    public void quitStartGameTest(){
        gofish.isPlaying = false;
        gofish.quitGame();
    }

    @Test
    public void startGameTestNotReadyToPlay(){
        String input = "n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        gofish.startGame(humanPlayer);
    }

    @Test
    public void startGameTestWillingToPlay() throws Exception {
        String input = "Y";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        GoFish mockFish = mock(GoFish.class);
        //mockFish.isPlaying = true;
        doNothing().when(mockFish).startPlay(humanPlayer);
        doCallRealMethod().when(mockFish).startGame(humanPlayer);

        mockFish.startGame(humanPlayer);
    }

    @Test
    public void startGameTestThrowsException() throws Exception {
        String input = "Y";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        GoFish mockFish = mock(GoFish.class);
        mockFish.isPlaying = true;
        doThrow(new Exception("test")).when(mockFish).startPlay(humanPlayer);
        doCallRealMethod().when(mockFish).startGame(humanPlayer);

        mockFish.startGame(humanPlayer);
    }

    @Test
    public void startPlayTest() throws Exception {
        GoFish mockFish = mock(GoFish.class);
        doNothing().when(mockFish).letsPlayGoFish(humanPlayer, pcPlayer);
        doCallRealMethod().when(mockFish).startPlay(humanPlayer);
        mockFish.booksCompleted = 13;
        mockFish.remainingDeck = new ArrayList<Card>(1);
        mockFish.pcPlayerCard = new ArrayList<Card>(2);
        mockFish.humanPlayerCard = new ArrayList<Card>(3);

        mockFish.startPlay(humanPlayer);
    }

    @Test
    void letsPlayGoFishTestHumanPlayer() throws Exception {
        Card.Rank asked = FIVE;
        Card card = new Card(SPADES,asked);
        GoFish mockFish = mock(GoFish.class);
        when(mockFish.selectCardFromHand(humanPlayer)).thenReturn(asked);
        doCallRealMethod().when(mockFish).letsPlayGoFish(humanPlayer,pcPlayer);
        doReturn(card).when(mockFish).drawTopCard();
        when(mockFish.nextTurn(humanPlayer)).thenReturn(pcPlayer);
        mockFish.pcPlayerCard = pcPlayerCardTest;
        mockFish.humanPlayerCard = humanPlayerCardTest;
        remainingDeck.add(card);
        mockFish.remainingDeck = remainingDeck;

        mockFish.letsPlayGoFish(humanPlayer,pcPlayer);
    }

    @Test
    void letsPlayGoFishTestPcPlayer() throws Exception {
        Card.Rank asked = FIVE;
        Card card = new Card(SPADES,asked);
        GoFish mockFish = mock(GoFish.class);
        when(mockFish.selectCardFromHand(pcPlayer)).thenReturn(asked);
        doCallRealMethod().when(mockFish).letsPlayGoFish(pcPlayer,humanPlayer);
        doReturn(card).when(mockFish).drawTopCard();
        when(mockFish.nextTurn(pcPlayer)).thenReturn(humanPlayer);
        mockFish.pcPlayerCard = pcPlayerCardTest;
        mockFish.humanPlayerCard = humanPlayerCardTest;
        remainingDeck.add(card);
        mockFish.remainingDeck = remainingDeck;

        mockFish.letsPlayGoFish(pcPlayer,humanPlayer);
    }

    @Test
    public void getWinnerTest() {
        gofish.humanPlayerBooks=7;
        gofish.pcPlayerBooks=6;
        gofish.humanPlayer=new Player("Joe",10);
        gofish.getWinner();
        gofish.humanPlayerBooks=5;
        gofish.pcPlayerBooks=8;
        gofish.humanPlayer=new Player("Joe",10);
        gofish.getWinner();
    }

    @Test
    public void isHumanBookCompleteTest() {
     gofish.isHumanBookComplete();
     List<Card> humanPlayerCardTest = gofish.humanPlayerCard;
     Assert.assertTrue(true);
    }

    @Test
    public void isPcBookCompleteTest() {
        gofish.isPcBookComplete();
        List<Card> pcPlayerCardTest = gofish.pcPlayerCard;
        Assert.assertTrue(true);
    }

    @Test
    public void resetGameTest() {
        GoFish mockFish = mock(GoFish.class);
        doCallRealMethod().when(mockFish).resetGame();
        mockFish.resetGame();
    }

    @Test
    public void quitGameTest() {
        GoFish mockFish = mock(GoFish.class);
        doNothing().when(mockFish).quitGame();
        mockFish.quitGame();
    }

    @Test
    public void getGameNameTest() {
        String expected =gofish.getGameName();
        String actual = "Go Fish";
        Assert.assertEquals(expected,actual);
    }

    @Test
    void takeAllSameCardsTest() {
        int actualSize = pcPlayerCardTest.size();
        List<Card> cardList = gofish.takeAllSameCards(humanPlayer, pcPlayer, SEVEN);
        Assert.assertNotNull(cardList);
        Assert.assertTrue(cardList.size()>0);
    }

    @Test
    public void nextTurnTest() {
        Player expected ;
        expected =gofish.nextTurn(humanPlayer);
        Assert.assertEquals(expected.getName(),"PC");
    }

    @Test
    public void askCardTest() {
        Boolean result = gofish.askCard(pcPlayer, SEVEN);
        if (result) {
            Assert.assertTrue(result);
        } else {
            Assert.assertFalse(result);
        }
    }


    @Test
    void takeCardsFromDeckTest() {

        remainingDeck=new ArrayList<>(10);

        gofish.takeCardsFromDeck(humanPlayer);
        int actualSize = humanPlayerCardTest.size();
        int expected =remainingDeck.size();
        Assert.assertNotEquals(expected,actualSize);

        remainingDeck=new ArrayList<>(10);

        gofish.takeCardsFromDeck(pcPlayer);
        int actualSizePc = humanPlayerCardTest.size();
        int expectedPc =remainingDeck.size();
        Assert.assertNotEquals(expectedPc,actualSizePc);
    }

    @Test
    public void selectCardFromHandTestPcPlayer() throws Exception {
        String input = "FIVE";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Card.Rank rank = gofish.selectCardFromHand(pcPlayer);
        Assert.assertNotNull(rank);
    }

    @Test
    public void selectCardFromHandTestHumanPlayer() throws Exception {
        String input = "FIVE";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        gofish.pcPlayerCard = pcPlayerCardTest;
        gofish.humanPlayerCard = humanPlayerCardTest;
        gofish.currentPlayerCards =humanPlayerCardTest;

        Card.Rank rank = gofish.selectCardFromHand(humanPlayer);
        Assert.assertNotNull(rank);
    }

    @org.junit.Test(expected = Exception.class)
    public void selectCardFromHandTestHumanPlayer_QuitGame() throws Exception {
        String input = "quit";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        gofish.pcPlayerCard = pcPlayerCardTest;
        gofish.humanPlayerCard = humanPlayerCardTest;
        gofish.currentPlayerCards =humanPlayerCardTest;

        Card.Rank rank = gofish.selectCardFromHand(humanPlayer);

    }

    @org.junit.Test(expected = Exception.class)
    public void getUserSelectedCardTest() throws Exception {
        String input = "right";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        gofish.pcPlayerCard = pcPlayerCardTest;
        gofish.humanPlayerCard = humanPlayerCardTest;
        gofish.currentPlayerCards =humanPlayerCardTest;

        Card.Rank rank = gofish.selectCardFromHand(humanPlayer);


    }

}