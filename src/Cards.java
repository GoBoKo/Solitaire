public class Cards {
    private String value;
    private String suit;

    public Cards (String v, String s){
        value = v;
        suit = s;
    }
    //get suit of card
    public String getSuit() {
        return suit;
    }
    //get value of card
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value + suit;
    }


}
