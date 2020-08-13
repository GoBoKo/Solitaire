/**
 * The Game of patience main class
 *
 * @author Albert Gabrychowicz
 */

import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.InputMismatchException;
import java.util.Scanner;

import uk.ac.aber.dcs.cs12320.cards.gui.javafx.CardTable;

public class Game extends Application {

    private CardTable cardTable;
    private String choice;
    int choiceNumb;
    private int i;

    @Override
    public void start(Stage stage) {
        cardTable = new CardTable(stage);

        // The interaction with this game is from a command line
        // menu. We need to create a separate non-GUI thread
        // to run this in. DO NOT REMOVE THIS.
        Runnable commandLineTask = () -> {
            //run main menu
            runApp();
        };
        Thread commandLineThread = new Thread(commandLineTask);
        // This is how we start the thread.
        // This causes the run method to execute.
        commandLineThread.start();
    }
    //main menu
    private void runApp() throws InputMismatchException {
        //dealer's deck
        Deck pack = new Deck();
        //player's deck
        Deck playerDeck = new Deck();
        Scanner in = new Scanner(System.in);
        pack.loadCards();
        playerDeck.load("results.txt");

        do {
            playerDeck.displayCards();
            cardTable.cardDisplay(playerDeck.getImages());

            System.out.println("1 - Print the pack out\n2 - Shuffle\n3 - Deal a card\n" +
                    "4 - Make a move, move last pile onto previous one\n" +
                    "5 - Make a move, move last pile back over two piles\n" +
                    "6 - Amalgamate piles in the middle (by giving their numbers)\n" +
                    "7 - Print the displayed cards on the command line\n" +
                    "8 - Play for me once (if two possible moves, makes the \"furthest\" move)\n" +
                    "9 - Play for me many times\n10- Display top 10 results\nQ - Quit");
            choice = in.nextLine().toUpperCase();
            switch (choice) {

                case "1":
                    pack.printDeck();
                    break;
                case "2":
                    pack.shuffle();
                    break;
                case "3":
                    playerDeck.dealACard(pack);
                    break;
                case "4":
                    //check if there are enough cards to try makeAMovePrevious()
                    if (playerDeck.getSizeDeckOfCards() > 1) {
                        playerDeck.makeAMovePrevious();
                    } else {
                        System.out.println("You have not enough cards");
                    }
                    break;
                case "5":
                    //check if there are enough cards to try makeAMoveOverTwo()
                    if (playerDeck.getSizeDeckOfCards() > 3) {
                        playerDeck.makeAMoveOverTwo();
                    } else {
                        System.out.println("You have not enough cards");
                    }
                    break;
                case "6":
                    //check if there are enough cards to try amalgamate()
                    if (playerDeck.getSizeDeckOfCards() > 2) {
                        playerDeck.amalgamate();
                    } else {
                        System.out.println("You have not enough cards");
                    }
                    break;
                case "7":
                    //print player's cards
                    playerDeck.printDeck();
                    System.out.println();
                    break;
                case "8":
                    //check if there are enough cards to try makeAMovePrevious()
                    if (playerDeck.getSizeDeckOfCards() > 1) {
                        playerDeck.playForMeOnce();
                        //if no move was made, application throws message
                        if (!playerDeck.isIfDone()) {
                            System.out.println("No possible moves.");
                        }
                    } else {
                        System.out.println("You have not enough cards");
                    }
                    break;
                case "9":
                    if(playerDeck.getSizeDeckOfCards()>1) {
                        do {
                            //How many times playForMeOnce() should be make?
                            System.out.println("How many times? (0 = maximum)");
                            choice = in.nextLine();
                            choiceNumb = Integer.parseInt(choice);

                            //check if entered number is at least 0
                        } while (choiceNumb < 0);
                        //if you answer 0 - move will be made maximum times
                        if (choiceNumb == 0) {
                            choiceNumb = 51;
                        }

                        i = 0;
                        do {
                            playerDeck.playForMeOnce();
                            i++;
                        } while ((i < choiceNumb) && (playerDeck.isIfDone()));
                    }else{
                        System.out.println("You have not enough cards");
                    }
                    break;
                case "10":
                    //if there are some results print them
                    if (playerDeck.getSizeLoaded() != 0) {
                        System.out.println("Top 10 results:");
                        for (i = 0; i < playerDeck.getSizeLoaded(); i++) {
                            System.out.println(i + 1 + ".  " + playerDeck.getResult(i));
                        }
                    } else {
                        System.out.println("There are no results");
                    }
                    break;
                    //this case is for easier testing,
                // instead entering 10 times "deal a card", you can enter 753
                case "753":
                    for (i = 0; i < 10; i++) {
                        playerDeck.dealACard(pack);
                    }
                    break;
                default:
                    //if you enter wrong input, application throws message
                    if (!choice.equals("Q")) {
                        System.out.println("Not a valid choice");
                    }
            }
            //when you quit the game by enter "q" or "Q"
        } while (!choice.equals("Q"));

        //save result of a game
        int result = playerDeck.getSizeDeckOfCards() + pack.getSizeDeckOfCards();
        playerDeck.save(result);
        System.out.println("Thank you for playing, your score is " + result +
                "\nThis is TOP 10 RESULTS:\n");
        //display top 10 results
        for (i = 0; i < playerDeck.getSizeLoaded()-1; i++) {
            System.out.println(i + 1 + ".  " + playerDeck.getResult(i));
        }
        //Close window with images
        System.exit(0);
    }

    // //////////////////////////////////////////////////////////////
    public static void main(String args[]) {
        //Game game = new Game();
        //game.playGame();
        Application.launch(args);
    }
}
