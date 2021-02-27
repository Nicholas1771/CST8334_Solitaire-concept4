public class CardBuilder {

    private Suits suit;
    private int value;
    private int colour;

    public CardBuilder setSuit(Suits suit) {
        this.suit = suit;
        if (suit.toString().equals("HEART") || suit.toString().equals("DIAMOND")) {
            this.colour = 0;
        } else {
            this.colour = 1;
        }
        return this;
    }

    public CardBuilder setValue(int value) {
        this.value = value;
        return this;
    }

    public Card createCard() {
        return new Card(suit, value, colour);
    }
}