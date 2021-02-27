public class Move {
    private String from;
    private String to;

    private int fromValue;
    private Suits fromSuit;

    public Move (String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom () {
        return from;
    }

    public String getTo () {
        return to;
    }

    public void setCardDetails (int value, Suits suit) {
        this.fromValue = value;
        this.fromSuit = suit;
    }

    public int getFromValue () {return fromValue;}

    public Suits getFromSuit () {return fromSuit;}
}
