import java.util.ArrayList;
import java.util.Random;

public class Stock {

    private ArrayList<Card> deckOfCards = new ArrayList<>();
    private Suits[] suits = Suits.values();
    private int TIMES_SHUFFLE = 1000;

    public Stock() {
        buildDeck();
        shuffleDeck(TIMES_SHUFFLE);
    }

    private void buildDeck(){

        for (Suits suit: suits){

            for (int value = 1; value < 14; value++){
                Card card = new CardBuilder().setSuit(suit).setValue(value).createCard();
                deckOfCards.add(card);
            }

        }
    }

    private void shuffleDeck(int timesShuffle){

        Random random = new Random();

        while (timesShuffle > 0){
            int cardNumber = random.nextInt(52);
            Card selectedCard = deckOfCards.get(cardNumber);
            deckOfCards.remove(cardNumber);
            deckOfCards.add(selectedCard);
            timesShuffle--;
        }

    }

    public Card drawCard(){
        Card card = deckOfCards.get(0);
        deckOfCards.remove(0);
        return card;
    }

    public void placeCards (ArrayList<Card> cards) {
        deckOfCards = new ArrayList<>(cards);
    }

    public ArrayList<Card> getDeckOfCards() {
        return deckOfCards;
    }
}
