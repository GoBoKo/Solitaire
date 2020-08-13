

import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;


public class Deck {
    //main deck
    private ArrayList<Cards> deckOfCards;
    private Scanner scan;
    private PrintWriter outfile;
    //iterator
    private int i = 0;
    //first player chose
    private int ch1;
    //second player chose
    private int ch2;
    private boolean bError = true;
    private boolean ifDone = true;
    //loaded results from file
    private ArrayList<Integer> loaded;
    //array list with names of card images to display
    private ArrayList<String> images = new ArrayList<>();


    public Deck() {
        this.deckOfCards = new ArrayList<>();
        this.loaded = new ArrayList<>();
    }

    public Cards getCard(int i) {
        return this.deckOfCards.get(i);
    }

    public int getResult(int i) {
        return this.loaded.get(i);
    }
    //remove followed cards from deck
    public void removeCard(int i) {
        this.deckOfCards.remove(i);
    }
    //load data from file
    public void load(String file) throws IndexOutOfBoundsException {
        try {

            scan = new Scanner(new FileReader(file));
            scan.useDelimiter(":|\r?\n|\r");
            //check for errors
        } catch (IOException exc) {
            System.out.println("Error:" + exc.getMessage());
        }
        while (scan.hasNext()) {
            //load data from file and add it to arraylist
            loaded.add(scan.nextInt());
        }
        scan.close();
    }
    //load cards from file to dealer's deck
    public void loadCards() throws IndexOutOfBoundsException {
        try {
            scan = new Scanner(new FileReader("cards.txt"));
            scan.useDelimiter(":|\r?\n|\r");
        //check for errors
        } catch (IOException exc) {
            System.out.println("Error:" + exc.getMessage());
        }
        while (scan.hasNext()) {

            deckOfCards.add(new Cards(scan.next(), scan.next()));
        }
        scan.close();
    }
    //shuffling deck
    public void shuffle() {
    Collections.shuffle(deckOfCards);
    }

    public void dealACard(Deck a) {
        //if there are cards in dealer's deck
        if(a.getSizeDeckOfCards()>0) {
            //adding card to player's deck
            deckOfCards.add(a.getCard(0));
            //and remove this card from dealer's deck
            a.removeCard(0);
        }else{
            System.out.println("There are no other cards");
        }
    }
    //check if value or suit of card "a" is similar to card "b"
    //and return true or false
    public boolean checker(int a, int b) {
                //check suit
        return (deckOfCards.get(a).getSuit().equals(deckOfCards.get(b).getSuit()))
                //check value
                || (deckOfCards.get(a).getValue().equals(deckOfCards.get(b).getValue()));
    }
    //put last card on previous
    public void makeAMovePrevious() {
        //check if cards fit
        if (checker(deckOfCards.size() - 1, deckOfCards.size() - 2)) {
            //remove penultimate card
            removeCard(deckOfCards.size() - 2);
        } else {
            //if cards not fit - throws error
            System.out.println("Error: Move cannot be done.");
        }

    }
    //put last card over two previous cards
    public void makeAMoveOverTwo() {
        //check if cards fit
        if (checker(deckOfCards.size() - 1, deckOfCards.size() - 4)) {
            //set fourth from the end card for last card
            deckOfCards.set((deckOfCards.size() - 4), deckOfCards.get((deckOfCards.size() - 1)));
            //remove last card
            removeCard(deckOfCards.size() - 1);
        } else {
            //if cards not fit - throws error
            System.out.println("Error: Move cannot be done.");
        }
    }
    //amalgamate two cards of user's choice
    public void amalgamate() throws InputMismatchException {

        do {
            try {
                scan = new Scanner(System.in);
                System.out.println("Which pile is moving?");
                //save first player's answer
                ch1 = scan.nextInt();
                //check if entered number is correct
                if (ch1 > deckOfCards.size() || ch1 < 1) {
                    System.out.println("Error: You can type only number from 1 to " + deckOfCards.size());
                } else {
                    //if there was no errors in last "if" bError save this
                    bError = false;
                }
                //if user try to enter special character or letter, program displays error
            } catch (Exception exc) {
                System.out.println("Error: You can type only number from 1 to " + deckOfCards.size());
            }
            //if there was an error user can enter value once again
        } while (bError);

        bError = true;

        do {
            try {
                scan = new Scanner(System.in);
                System.out.println("Where is it moving to?");
                //save second player's answer
                ch2 = scan.nextInt();
                //check if chosen card is adjacent to first card
                if (ch2 == ch1 + 1 || ch2 == ch1 - 1) {
                    //if there was no errors in last "if" bError save this
                    bError = false;
                } else {
                    System.out.println("Error: You can type only \"" + (ch1 - 1) + "\" or \"" + (ch1 + 1) + "\"");
                }
                //if user try to enter special character or letter, program displays error
            } catch (Exception exc) {
                System.out.println("Error: You can type only number from 1 to " + deckOfCards.size() + ", different from the previous number");
            }
        } while (bError);
        //check if given cards fit witch value or suit
        if (checker(ch1 - 1, ch2 - 1)) {
            //remove second card
            removeCard(ch2 - 1);
        } else {
            System.out.println("Error: Move cannot be done");
        }
    }
    //make only one move if it is possible
    public void playForMeOnce() {
        ifDone = false;
        //check if there are enough cards to try amalgamate
        if (deckOfCards.size() > 2) {
            for (ch1 = 0; ch1 < deckOfCards.size() - 2 && !ifDone; ch1++) {
                //check if card fit with previous card (impossible for first card)
                if ((ch1 > 0) && (checker(ch1, ch1 - 1))) {
                    //remove one card
                    removeCard(ch1 - 1);
                    //notice that one move was already done
                    ifDone = true;
                }
                //check if card fit with previous card
                if (checker(ch1, ch1 + 1)) {
                    //remove one card
                    removeCard(ch1 + 1);
                    //notice that one move was already done
                    ifDone = true;
                }
            }
        }
        //check if there are enough cards to try makeAMoveOverTwo(), if move was already done and if cards fit
        if ((deckOfCards.size() > 3) && (!ifDone) && (checker(deckOfCards.size() - 1, deckOfCards.size() - 4))) {
            deckOfCards.set((deckOfCards.size() - 4), deckOfCards.get((deckOfCards.size() - 1)));
            //remove one card
            removeCard(deckOfCards.size() - 1);
            //notice that one move was already done
            ifDone = true;
            //check if there are enough cards to try makeAMovePrevious(), if move was already done and if cards fit
        } else if ((deckOfCards.size() > 1) && (!ifDone) && (checker(deckOfCards.size() - 1, deckOfCards.size() - 2))) {
            //remove one card
            removeCard(deckOfCards.size() - 2);
            //notice that one move was already done
            ifDone = true;
        }
    }

    public void printDeck() {
        i = 0;
        //print each card
        for (Cards card : deckOfCards) {
            System.out.print(card + "  ");
            i++;
            //go to next line every 13 cards
            if ((i == 13) || (i == 26) || (i == 39)) {
                System.out.println();
            }
        }
        System.out.println();
    }
    //save result of game in file "result.txt"
    public void save(int a) throws InputMismatchException {
        //add last result to other results
        loaded.add(a);
        //results are sorting
        Collections.sort(loaded);
        try {
            outfile = new PrintWriter(new FileWriter("results.txt"));

        } catch (IOException exc) {
            //check for errors
            System.out.println("Error:" + exc.getMessage());
        }
        //saving each result for ArrayList loaded to file
        for (i = 0; i < 10 && i < loaded.size(); i++) {
            outfile.println(loaded.get(i));
        }
        outfile.close();
    }

    public void displayCards() {
        //this for remove all cards displayed
        for (i = 0; i < images.size(); ) {
            images.remove(i);
        }

        for (i = 0; i < deckOfCards.size(); i++) {
            //change value and suit of cards to name of .gif file
            images.add(deckOfCards.get(i).toString() + ".gif");
        }
    }
    //get size of deck
    public Integer getSizeDeckOfCards() {
        return deckOfCards.size();
    }

    public Integer getSizeLoaded() {
        return loaded.size();
    }
    //get ifDone (this variable was used in playForMeOnce())
    public boolean isIfDone() {
        return ifDone;
    }
    //get ArrayList with names .gifs to display
    public ArrayList<String> getImages() {
        return images;
    }

}
