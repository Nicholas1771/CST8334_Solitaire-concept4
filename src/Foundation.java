import java.util.ArrayList;

public class Foundation {

    private ArrayList<Card> pile = new ArrayList<>();
    private String name;

    Foundation(String name){
        this.name = name;
    }

    public ArrayList<Card> getPile() {
        return pile;
    }

    public void removeCard(){
        pile.remove(pile.size()-1);
    }

    public boolean placeCard (Card card) {
        if (pile.isEmpty()){
            if (card.getValue()==1){
                card.setTableLocation(name);
                pile.add(card);
                AudioPlayer.playSound(AudioPlayer.DROP);
                return true;
            }
        } else if (card.getValue() == pile.get(pile.size()-1).getValue()+1 && card.getSuit().toString().equals(pile.get(0).getSuit().toString())) {
            card.setTableLocation(name);
            pile.add(card);
            AudioPlayer.playSound(AudioPlayer.DROP);
            return true;
        }

        return false;
    }

    public boolean canPlaceCard (Card card) {
        if (pile.isEmpty()){
            if (card.getValue()==1){
                return true;
            }
        } else if (card.getValue() == pile.get(pile.size()-1).getValue()+1 && card.getSuit().toString().equals(pile.get(0).getSuit().toString())) {
            return true;
        }

        return false;
    }

    public String getName() {
        return name;
    }
}
