import java.util.ArrayList;

public class Talon {

    private ArrayList<Card> pile = new ArrayList<>();
    private ArrayList<Card> display = new ArrayList<>();

    public ArrayList<Card> getPile() {
        return pile;
    }

    public ArrayList<Card> getDisplay() {
        return display;
    }

    public void placeCard (ArrayList<Card> cards) {
        display.clear();
        for (Card card: cards){
            card.setFaceUp(true);
            card.setTableLocation("talon");
            pile.add(card);
            display.add(card);
        }

        AudioPlayer.playSound(AudioPlayer.DROP);

    }

    public void removeCard(int drawType){

        if(drawType == 1){
            pile.remove(pile.size()-1);
            try{
                display.remove(display.size()-1);
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (drawType == 3){
            pile.remove(pile.size()-1);
            try{
                display.remove(display.size()-1);
            } catch (Exception e){
                e.printStackTrace();
            }
            if (display.size() == 0 & pile.size() > 0){
                for (int i = 0; i < 3; i++){
                    if (!pile.isEmpty()){
                        display.add(pile.get(pile.size()-1+i));
                    }
                }
            }
        }




    }

    public ArrayList<Card> getAndClearPile () {
        ArrayList<Card> copy = new ArrayList<>(pile);

        pile.clear();
        display.clear();

        for (Card card : copy) {
            card.setFaceUp(false);
        }

        AudioPlayer.playSound(AudioPlayer.DROP);

        return copy;
    }
}
