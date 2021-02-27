import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table {

    private static Stock stock = new Stock();
    private static Talon talon = new Talon();
    private static Map<String, Foundation> foundations = new HashMap<>();
    private static Map<String, Tableau> tableaux = new HashMap<>();
    private static int score = 0;

    public void resetTable(){
        foundations.clear();
        tableaux.clear();
        stock = new Stock();
        talon = new Talon();

        foundations.put("foundation1", new Foundation("foundation1"));
        foundations.put("foundation2", new Foundation("foundation2"));
        foundations.put("foundation3", new Foundation("foundation3"));
        foundations.put("foundation4", new Foundation("foundation4"));

        tableaux.put("tableau1", new Tableau("tableau1"));
        tableaux.put("tableau2", new Tableau("tableau2"));
        tableaux.put("tableau3", new Tableau("tableau3"));
        tableaux.put("tableau4", new Tableau("tableau4"));
        tableaux.put("tableau5", new Tableau("tableau5"));
        tableaux.put("tableau6", new Tableau("tableau6"));
        tableaux.put("tableau7", new Tableau("tableau7"));

        buildTableau();
    }

    public void resetScore(){
        score = 0;
    }

    public void buildTableau(){

        boolean tableauDone = true;

        while (tableauDone){

            boolean firstCardPlaced = false;

            for (int i = 1; i < 8; i++){

                if (!tableaux.get("tableau"+(i)).getBuildComplete()){

                    if (firstCardPlaced){
                        tableaux.get("tableau"+(i)).buildPile(stock.drawCard());
                    } else {
                        firstCardPlaced = true;
                        Card card = stock.drawCard();
                        card.setFaceUp(true);
                        tableaux.get("tableau"+(i)).buildPile(card);
                        tableaux.get("tableau"+(i)).setBuildComplete(true);
                    }
                }
            }

            if (!firstCardPlaced){
                tableauDone = false;
            }

        }

    }

    public static Stock getStock () {
        return stock;
    }

    public static Talon getTalon () {
        return talon;
    }

    public Map<String, ArrayList<Card>> updateCardView () {
        Map<String, ArrayList<Card>> testMap = new HashMap<>();

        ArrayList<Card> stock = Table.stock.getDeckOfCards();
        ArrayList<Card> talon = Table.talon.getPile();
        ArrayList<Card> talonThree = Table.talon.getDisplay();

        ArrayList<Card> foundation1 = foundations.get("foundation1").getPile();
        ArrayList<Card> foundation2 = foundations.get("foundation2").getPile();
        ArrayList<Card> foundation3 = foundations.get("foundation3").getPile();
        ArrayList<Card> foundation4 = foundations.get("foundation4").getPile();

        ArrayList<Card> tableau1 = tableaux.get("tableau1").getPile();
        ArrayList<Card> tableau2 = tableaux.get("tableau2").getPile();
        ArrayList<Card> tableau3 = tableaux.get("tableau3").getPile();
        ArrayList<Card> tableau4 = tableaux.get("tableau4").getPile();
        ArrayList<Card> tableau5 = tableaux.get("tableau5").getPile();
        ArrayList<Card> tableau6 = tableaux.get("tableau6").getPile();
        ArrayList<Card> tableau7 = tableaux.get("tableau7").getPile();

        testMap.put("stock", stock);
        testMap.put("talon", talon);
        testMap.put("talonThree", talonThree);
        testMap.put("foundation1", foundation1);
        testMap.put("foundation2", foundation2);
        testMap.put("foundation3", foundation3);
        testMap.put("foundation4", foundation4);
        testMap.put("tableau1", tableau1);
        testMap.put("tableau2", tableau2);
        testMap.put("tableau3", tableau3);
        testMap.put("tableau4", tableau4);
        testMap.put("tableau5", tableau5);
        testMap.put("tableau6", tableau6);
        testMap.put("tableau7", tableau7);

        return testMap;
    }

    public static Map<String, Foundation> getFoundations() {
        return foundations;
    }

    public static Map<String, Tableau> getTableaux() {
        return tableaux;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Table.score = score;
    }

    public static void updateScore(int update) {
        Table.score += update;
    }

}
