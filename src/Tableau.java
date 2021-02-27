import java.util.ArrayList;

public class Tableau {

    private ArrayList<Card> pile = new ArrayList<>();
    private boolean buildComplete = false;
    private boolean isEmpty = false;
    private String name;

    Tableau(String name){
        this.name = name;
    }

    public void buildPile(Card card){
        card.setTableLocation(name);
        pile.add(card);
    }

    public String getName() {
        return name;
    }

    public boolean canPlaceCard (Card card) {
        if (isEmpty && card.getValue() == 13){
            return true;
        }


        if (!isEmpty && getBottomCard().getColour() != card.getColour() &&
                getBottomCard().getValue()-1 == card.getValue()){
            return true;
        }

        return false;
    }

    public boolean placeCard(Card card){

        if (isEmpty && card.getValue() == 13){
            card.setTableLocation(name);
            pile.add(card);
            isEmpty = false;
            AudioPlayer.playSound(AudioPlayer.DROP);
            return true;
        }


        if (!isEmpty && getBottomCard().getColour() != card.getColour() &&
                getBottomCard().getValue()-1 == card.getValue()){
            card.setTableLocation(name);
            pile.add(card);
            isEmpty = false;
            AudioPlayer.playSound(AudioPlayer.DROP);
            return true;
        }

        return false;
    }

    public boolean addPile (ArrayList<Card> cardsToAdd) {

        if (isEmpty) {
            if (cardsToAdd.get(0).getValue() == 13) {
                pile.addAll(cardsToAdd);
                isEmpty = false;
                AudioPlayer.playSound(AudioPlayer.DROP);
                return true;
            } else {
                return false;
            }
        }

        if (pile.get(pile.size()-1).getColour() != cardsToAdd.get(cardsToAdd.size()-1).getColour() &&
                pile.get(pile.size()-1).getValue()-1 == cardsToAdd.get(cardsToAdd.size()-1).getValue()){

            while (!cardsToAdd.isEmpty()){
                pile.add(cardsToAdd.get(cardsToAdd.size()-1));
                cardsToAdd.remove(cardsToAdd.size()-1);
            }

            isEmpty = false;
            AudioPlayer.playSound(AudioPlayer.DROP);
            return true;
        }

        return false;
    }

    public boolean removeCard(){
        pile.remove(pile.size()-1);
        if(pile.size()==0){
            isEmpty = true;
            return false;
        } else {
            pile.get(pile.size()-1).setFaceUp(true);
            return true;
        }
    }

    public boolean removeCardIndex(int index) {
        System.out.println("Removing: " + pile.get(index).getValue() + pile.get(index).getSuit());
        pile.remove(index);
        if(pile.size()==0){
            isEmpty = true;
            return false;
        } else {
            pile.get(index-1).setFaceUp(true);
            return true;
        }
    }

    public Card getBottomCard() {
        if (pile.size() > 0) {
            return pile.get(pile.size() - 1);
        } else {
            return null;
        }
    }

    public ArrayList<Card> getPile() {
        return pile;
    }

    public Boolean getBuildComplete() {
        return buildComplete;
    }

    public void setBuildComplete(Boolean buildComplete) {
        this.buildComplete = buildComplete;
    }

    public Boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(Boolean empty) {
        isEmpty = empty;
    }
}
