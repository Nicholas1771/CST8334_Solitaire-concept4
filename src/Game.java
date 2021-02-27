import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Time;
import java.util.ArrayList;

public class Game {

    private Table table = new Table();
    private View view;

    private String gameType;
    private int drawType;
    private int deckPass = 0;

    private Victory victory = new Victory();

    public Game (String gameType, int drawType) {
        table.resetTable();
        view = new View(gameType, drawType);
        this.drawType = drawType;
        this.gameType = gameType;

        view.getGamePanel().getStockButton().addActionListener(e -> {
            AudioPlayer.playSound(AudioPlayer.CLICK);
            stockButtonPressed();
        });
        view.getGamePanel().getNewGameButton().addActionListener(e -> {
            AudioPlayer.playSound(AudioPlayer.CLICK);
            newGameButtonPressed();
        });
        view.getGamePanel().getBackButton().addActionListener(e -> {
            AudioPlayer.playSound(AudioPlayer.CLICK);
            new Launcher();
            view.getWindow().dispose();
        });

        view.getGamePanel().getFrontStyleButton().addActionListener(e -> {
            AudioPlayer.playSound(AudioPlayer.CLICK);
            switchCardStyle();
        });

        view.getGamePanel().getCardBackColourButton().addItemListener(e -> {
            AudioPlayer.playSound(AudioPlayer.CLICK);
            changeCardBackColour(e.getItem().toString());
        });

        view.getGamePanel().getBackgroundImageButton().addItemListener(e -> {
            AudioPlayer.playSound(AudioPlayer.CLICK);
            changeBackground(e.getItem().toString());
        });

        view.getGamePanel().getAutoMoveButton().addActionListener(e -> {
            AudioPlayer.playSound(AudioPlayer.CLICK);
            performBestAutoMove();
        });

        updateView();
        victory.start();
    }

    private void changeCardBackColour (String colour) {

        ArrayList<ArrayList<Card>> cardPiles = new ArrayList<>();

        cardPiles.add(Table.getStock().getDeckOfCards());
        cardPiles.add(Table.getTalon().getPile());
        cardPiles.add(Table.getTableaux().get("tableau1").getPile());
        cardPiles.add(Table.getTableaux().get("tableau2").getPile());
        cardPiles.add(Table.getTableaux().get("tableau3").getPile());
        cardPiles.add(Table.getTableaux().get("tableau4").getPile());
        cardPiles.add(Table.getTableaux().get("tableau5").getPile());
        cardPiles.add(Table.getTableaux().get("tableau6").getPile());
        cardPiles.add(Table.getTableaux().get("tableau7").getPile());
        cardPiles.add(Table.getFoundations().get("foundation1").getPile());
        cardPiles.add(Table.getFoundations().get("foundation2").getPile());
        cardPiles.add(Table.getFoundations().get("foundation3").getPile());
        cardPiles.add(Table.getFoundations().get("foundation4").getPile());

        for (ArrayList<Card> pile : cardPiles) {
            for (Card card : pile) {
                card.setFaceDownImage(colour);
            }
        }
        updateView();
    }

    private void switchCardStyle () {
        ArrayList<ArrayList<Card>> cardPiles = new ArrayList<>();

        cardPiles.add(Table.getStock().getDeckOfCards());
        cardPiles.add(Table.getTalon().getPile());
        cardPiles.add(Table.getTableaux().get("tableau1").getPile());
        cardPiles.add(Table.getTableaux().get("tableau2").getPile());
        cardPiles.add(Table.getTableaux().get("tableau3").getPile());
        cardPiles.add(Table.getTableaux().get("tableau4").getPile());
        cardPiles.add(Table.getTableaux().get("tableau5").getPile());
        cardPiles.add(Table.getTableaux().get("tableau6").getPile());
        cardPiles.add(Table.getTableaux().get("tableau7").getPile());
        cardPiles.add(Table.getFoundations().get("foundation1").getPile());
        cardPiles.add(Table.getFoundations().get("foundation2").getPile());
        cardPiles.add(Table.getFoundations().get("foundation3").getPile());
        cardPiles.add(Table.getFoundations().get("foundation4").getPile());

        for (ArrayList<Card> pile : cardPiles) {
            for (Card card : pile) {
                card.switchStyle();
            }
        }
        updateView();
    }

    private void changeBackground (String background) {
        view.getGamePanel().setBackgroundImage(background);
        updateView();
    }

    private void updateView () {
        view.getGamePanel().setCardMap(table.updateCardView());
        view.getGamePanel().repaint();
    }

    private void newGameButtonPressed(){
        table.resetTable();
        if (gameType.equals("c")){
            table.resetScore();
        } else {
            Table.updateScore(-52);
        }
        deckPass = 0;
        victory.interrupt();
        updateView();
    }

    private void performBestAutoMove () {
        ArrayList<Move> possibleMoves = new ArrayList<>(getPossibleMoves());

        if (getPossibleMoves().size() > 0) {
            printPossibleMoves(possibleMoves);

            Move moveToMake = possibleMoves.get(0);

            if (Table.getTalon().getPile().size() > 0) {
                Card talonCard = Table.getTalon().getPile().get(Table.getTalon().getPile().size() - 1);

                for (int i = possibleMoves.size()-1; i >= 0; i--) {
                    if (possibleMoves.get(i).getFromValue() == talonCard.getValue() && possibleMoves.get(i).getFromSuit().toString() == talonCard.getSuit().toString()) {
                        moveToMake = possibleMoves.get(i);
                    }
                }
            }

            autoMove(moveToMake);
        }

    }

    private void autoMove (Move move) {
        AudioPlayer.playSound(AudioPlayer.DROP);
        ArrayList<Card> cardsToMove = new ArrayList<>();
        boolean reachedTargetCard = false;
        int counter = 0;
        ArrayList<Card> copy;
        switch (move.getFrom()) {

            case "talon":
//                if (Table.getTalon().getPile().size() < 1) {
//                    autoDraw();
//                }

                cardsToMove.add(Table.getTalon().getPile().get(Table.getTalon().getPile().size() - 1));

//                if (! (cardsToMove.get(0).getValue() == move.getFromValue() && cardsToMove.get(0).getSuit().toString().equals(move.getFromSuit()))) {
//                    autoDraw();
//                } else {
                    Table.getTalon().removeCard(drawType);
//                }
                break;
            case "tableau1":

                copy = new ArrayList<>(Table.getTableaux().get("tableau1").getPile());

                for (Card card : copy) {

                    int value = card.getValue();
                    Suits suit = card.getSuit();

                    System.out.println(counter);

                    System.out.println("Target: " + move.getFromValue() + move.getFromSuit() + ", at: " + value + suit);

                    if (move.getFromValue() == value && move.getFromSuit() == suit) {
                        System.out.println("found target");
                        reachedTargetCard = true;

                        if (copy.size() > 1 && !copy.get(counter-1).isFaceUp()) {
                            updateScore(view.getGamePanel().getPointCardFlip());
                        }
                    }

                    if (reachedTargetCard) {
                        cardsToMove.add(copy.get(counter));
                        Table.getTableaux().get("tableau1").removeCard();

                    }

                    counter++;

                }
                break;
            case "tableau2":
                copy = new ArrayList<>(Table.getTableaux().get("tableau2").getPile());

                for (Card card : copy) {
                    int value = card.getValue();
                    Suits suit = card.getSuit();

                    System.out.println(counter);

                    System.out.println("Target: " + move.getFromValue() + move.getFromSuit() + ", at: " + value + suit);

                    if (move.getFromValue() == value && move.getFromSuit() == suit) {
                        System.out.println("found target");
                        reachedTargetCard = true;

                        if (copy.size() > 1 && !copy.get(counter-1).isFaceUp()) {
                            updateScore(view.getGamePanel().getPointCardFlip());
                        }
                    }

                    if (reachedTargetCard) {
                        cardsToMove.add(copy.get(counter));
                        Table.getTableaux().get("tableau2").removeCard();
                    }
                    counter++;
                }
                break;
            case "tableau3":
                copy = new ArrayList<>(Table.getTableaux().get("tableau3").getPile());

                for (Card card : copy) {
                    int value = card.getValue();
                    Suits suit = card.getSuit();
                    System.out.println(counter);
                    System.out.println("Target: " + move.getFromValue() + move.getFromSuit() + ", at: " + value + suit);

                    if (move.getFromValue() == value && move.getFromSuit() == suit) {
                        System.out.println("found target");
                        reachedTargetCard = true;

                        if (copy.size() > 1 && !copy.get(counter-1).isFaceUp()) {
                            updateScore(view.getGamePanel().getPointCardFlip());
                        }
                    }

                    if (reachedTargetCard) {
                        cardsToMove.add(copy.get(counter));
                        Table.getTableaux().get("tableau3").removeCard();
                    }
                    counter++;
                }
                break;
            case "tableau4":
                copy = new ArrayList<>(Table.getTableaux().get("tableau4").getPile());

                for (Card card : copy) {
                    int value = card.getValue();
                    Suits suit = card.getSuit();
                    System.out.println(counter);
                    System.out.println("Target: " + move.getFromValue() + move.getFromSuit() + ", at: " + value + suit);

                    if (move.getFromValue() == value && move.getFromSuit() == suit) {
                        System.out.println("found target");
                        reachedTargetCard = true;

                        if (copy.size() > 1 && !copy.get(counter-1).isFaceUp()) {
                            updateScore(view.getGamePanel().getPointCardFlip());
                        }
                    }

                    if (reachedTargetCard) {
                        cardsToMove.add(copy.get(counter));
                        Table.getTableaux().get("tableau4").removeCard();
                    }
                    counter++;
                }
                break;
            case "tableau5":
                copy = new ArrayList<>(Table.getTableaux().get("tableau5").getPile());

                for (Card card : copy) {
                    int value = card.getValue();
                    Suits suit = card.getSuit();
                    System.out.println(counter);
                    System.out.println("Target: " + move.getFromValue() + move.getFromSuit() + ", at: " + value + suit);

                    if (move.getFromValue() == value && move.getFromSuit() == suit) {
                        System.out.println("found target");
                        reachedTargetCard = true;

                        if (copy.size() > 1 && !copy.get(counter-1).isFaceUp()) {
                            updateScore(view.getGamePanel().getPointCardFlip());
                        }
                    }

                    if (reachedTargetCard) {
                        cardsToMove.add(copy.get(counter));
                        Table.getTableaux().get("tableau5").removeCard();
                    }
                    counter++;
                }
                break;
            case "tableau6":
                copy = new ArrayList<>(Table.getTableaux().get("tableau6").getPile());

                for (Card card : copy) {
                    int value = card.getValue();
                    Suits suit = card.getSuit();
                    System.out.println(counter);
                    System.out.println("Target: " + move.getFromValue() + move.getFromSuit() + ", at: " + value + suit);

                    if (move.getFromValue() == value && move.getFromSuit() == suit) {
                        System.out.println("found target");
                        reachedTargetCard = true;

                        if (copy.size() > 1 && !copy.get(counter-1).isFaceUp()) {
                            updateScore(view.getGamePanel().getPointCardFlip());
                        }
                    }

                    if (reachedTargetCard) {
                        cardsToMove.add(copy.get(counter));
                        Table.getTableaux().get("tableau6").removeCard();
                    }
                    counter++;
                }
                break;
            case "tableau7":
                copy = new ArrayList<>(Table.getTableaux().get("tableau7").getPile());

                for (Card card : copy) {
                    int value = card.getValue();
                    Suits suit = card.getSuit();
                    System.out.println(counter);
                    System.out.println("Target: " + move.getFromValue() + move.getFromSuit() + ", at: " + value + suit);

                    if (move.getFromValue() == value && move.getFromSuit() == suit) {
                        System.out.println("found target");
                        reachedTargetCard = true;

                        if (copy.size() > 1 && !copy.get(counter-1).isFaceUp()) {
                            updateScore(view.getGamePanel().getPointCardFlip());
                        }
                    }

                    if (reachedTargetCard) {
                        cardsToMove.add(copy.get(counter));
                        Table.getTableaux().get("tableau7").removeCard();
                    }
                    counter++;
                }
                break;
            default:
                cardsToMove.add(new Card(Suits.HEART, 1, 0));
                System.out.println("Error with auto move");
        }
        switch (move.getTo()) {
            case "tableau1":
                if (move.getFrom().equals("talon")) {
                    updateScore(view.getGamePanel().getPointTableau());
                }
                for (Card card : cardsToMove) {
                    Table.getTableaux().get("tableau1").placeCard(card);
                }
                break;
            case "tableau2":
                if (move.getFrom().equals("talon")) {
                    updateScore(view.getGamePanel().getPointTableau());
                }
                for (Card card : cardsToMove) {
                    Table.getTableaux().get("tableau2").placeCard(card);
                }
                break;
            case "tableau3":
                if (move.getFrom().equals("talon")) {
                    updateScore(view.getGamePanel().getPointTableau());
                }
                for (Card card : cardsToMove) {
                    Table.getTableaux().get("tableau3").placeCard(card);
                }
                break;
            case "tableau4":
                if (move.getFrom().equals("talon")) {
                    updateScore(view.getGamePanel().getPointTableau());
                }
                for (Card card : cardsToMove) {
                    Table.getTableaux().get("tableau4").placeCard(card);
                }
                break;
            case "tableau5":
                if (move.getFrom().equals("talon")) {
                    updateScore(view.getGamePanel().getPointTableau());
                }
                for (Card card : cardsToMove) {
                    Table.getTableaux().get("tableau5").placeCard(card);
                }
                break;
            case "tableau6":
                if (move.getFrom().equals("talon")) {
                    updateScore(view.getGamePanel().getPointTableau());
                }
                for (Card card : cardsToMove) {
                    Table.getTableaux().get("tableau6").placeCard(card);
                }
                break;
            case "tableau7":
                if (move.getFrom().equals("talon")) {
                    updateScore(view.getGamePanel().getPointTableau());
                }
                for (Card card : cardsToMove) {
                    Table.getTableaux().get("tableau7").placeCard(card);
                }
                break;
            case "foundation1":
                Table.getFoundations().get("foundation1").placeCard(cardsToMove.get(0));
                updateScore(view.getGamePanel().getPointFoundation());
                break;
            case "foundation2":
                Table.getFoundations().get("foundation2").placeCard(cardsToMove.get(0));
                updateScore(view.getGamePanel().getPointFoundation());
                break;
            case "foundation3":
                Table.getFoundations().get("foundation3").placeCard(cardsToMove.get(0));
                updateScore(view.getGamePanel().getPointFoundation());
                break;
            case "foundation4":
                Table.getFoundations().get("foundation4").placeCard(cardsToMove.get(0));
                updateScore(view.getGamePanel().getPointFoundation());
                break;
            default:
                System.out.println("Error with auto move");
        }
        updateView();
    }

    private void autoDraw () {
        stockButtonPressed();
    }

    private void printPossibleMoves (ArrayList<Move> moves) {
        System.out.println();
        System.out.println("POSSIBLE MOVES");
        for (Move move : moves) {
            System.out.println("MOVE: " + move.getFromValue() + move.getFromSuit() + "(" + move.getFrom() + ") -> " + move.getTo());
        }
    }

    public void updateScore(int update) {
        Table.updateScore(update);
        view.getGamePanel().getScoreText().setText("Score: " + Table.getScore());
    }

    private ArrayList<Move> getPossibleMoves () {

        ArrayList<Move> possibleMoves = new ArrayList<>();

        Stock stock = Table.getStock();

        Talon talon = Table.getTalon();

        ArrayList<Tableau> tableaus = new ArrayList<>();
        tableaus.add(Table.getTableaux().get("tableau1"));
        tableaus.add(Table.getTableaux().get("tableau2"));
        tableaus.add(Table.getTableaux().get("tableau3"));
        tableaus.add(Table.getTableaux().get("tableau4"));
        tableaus.add(Table.getTableaux().get("tableau5"));
        tableaus.add(Table.getTableaux().get("tableau6"));
        tableaus.add(Table.getTableaux().get("tableau7"));

        ArrayList<Foundation> foundations = new ArrayList<>();
        foundations.add(Table.getFoundations().get("foundation1"));
        foundations.add(Table.getFoundations().get("foundation2"));
        foundations.add(Table.getFoundations().get("foundation3"));
        foundations.add(Table.getFoundations().get("foundation4"));

        //tableaus to foundations
        for (int i = 0; i < tableaus.size(); i++) {
            if (tableaus.get(i).getPile().size() == 0) {
                continue;
            }
            for (int ii = 0; ii < foundations.size(); ii++) {
                if (foundations.get(ii).canPlaceCard(tableaus.get(i).getBottomCard())) {
                    Move move = new Move("tableau" + (i + 1), "foundation" + (ii + 1));
                    move.setCardDetails(tableaus.get(i).getBottomCard().getValue(), tableaus.get(i).getBottomCard().getSuit());
                    possibleMoves.add(move);
                    break;
                }
            }
        }

        //tableaus to tableaus
        for (int i = 0; i < tableaus.size(); i++) {
            ArrayList<Card> fromPile = new ArrayList<>(tableaus.get(i).getPile());
            for (int iii = 0; iii < tableaus.get(i).getPile().size(); iii++) {
                Card fromCard = fromPile.get(iii);
                if (tableaus.get(i).getPile().size() == 0) {
                    continue;
                }
                for (int ii = 0; ii < tableaus.size(); ii++) {
                    if (!tableaus.get(i).equals(tableaus.get(ii))) {
                        if (fromCard.isFaceUp() && tableaus.get(ii).canPlaceCard(fromCard)) {

                            boolean bothFaceUp = false;
                            boolean uselessMove = false;
                            boolean bothSameValue = false;
                            boolean pilesBigEnough = false;

                            ArrayList<Card> toPile = new ArrayList<>(tableaus.get(ii).getPile());

                                try {
                                    if (fromPile.get(iii - 1).isFaceUp()) {

                                    }
                                } catch (Exception e) {
                                    if (fromPile.get(iii).getValue() == 13) {
                                        uselessMove = true;
                                        System.out.println("USELESS KING MOVE");
                                        continue;
                                    }
                                }

                                if (toPile.size() > 0) {
                                    Card toCard = toPile.get(toPile.size() - 1);

                                    if (fromPile.size() > 1 && toPile.size() > 1) {
                                        pilesBigEnough = true;
                                    }

                                    if (pilesBigEnough) {

                                        Card fromSecondCard = fromPile.get(fromPile.size() - 2);

                                        if (toCard.isFaceUp() && fromSecondCard.isFaceUp()) {
                                            bothFaceUp = true;
                                        }

                                        if (toCard.getValue() == fromSecondCard.getValue()) {
                                            bothSameValue = true;
                                        }
                                    }
                                }

                            if (bothFaceUp && bothSameValue) {
                                uselessMove = true;
                            }

                            if (!uselessMove) {
                                Move move = new Move("tableau" + (i + 1), "tableau" + (ii + 1));
                                move.setCardDetails(fromCard.getValue(), fromCard.getSuit());
                                possibleMoves.add(move);
                            }
                            break;
                        }
                    }
                }
            }
        }

        //talon to foundations
        if (talon.getPile().size() > 0) {
            for (int ii = 0; ii < foundations.size(); ii++) {
                if (foundations.get(ii).canPlaceCard(talon.getPile().get(talon.getPile().size() - 1))) {
                    Move move = new Move("talon", "foundation" + (ii + 1));
                    move.setCardDetails(talon.getPile().get(talon.getPile().size() - 1).getValue(), talon.getPile().get(talon.getPile().size() - 1).getSuit());
                    possibleMoves.add(move);
                    break;
                }
            }
        }

        //talon to tableaus
        if (talon.getPile().size() > 0) {
            for (int ii = 0; ii < tableaus.size(); ii++) {
                if (tableaus.get(ii).canPlaceCard(talon.getPile().get(talon.getPile().size() - 1)) && talon.getPile().get(talon.getPile().size() - 1).getValue() != 1) {
                    Move move = new Move("talon", "tableau" + (ii + 1));
                    move.setCardDetails(talon.getPile().get(talon.getPile().size() - 1).getValue(), talon.getPile().get(talon.getPile().size() - 1).getSuit());
                    possibleMoves.add(move);
                    break;
                }
            }
        }

        return possibleMoves;
    }

    private void stockButtonPressed () {
        if (Table.getStock().getDeckOfCards().size() > 0) {
            int draw = 0;

            ArrayList<Card> drawnCards = new ArrayList<>();

            while (draw < drawType){
                if (!Table.getStock().getDeckOfCards().isEmpty()){
                    drawnCards.add(Table.getStock().drawCard());
                }
                draw++;
            }
            Table.getTalon().placeCard(drawnCards);
        } else {
            if(gameType.equals("c")){
                if(drawType == 1){
                    Table.getStock().placeCards(Table.getTalon().getAndClearPile());
                    Table.updateScore(-100);
                    updateView();
                } else if (drawType == 3) {
                    deckPass++;
                    Table.getStock().placeCards(Table.getTalon().getAndClearPile());
                    if (deckPass > 3){
                        Table.updateScore(-20);
                        updateView();
                    }
                }
            } else if (gameType.equals("v")){
                if(drawType == 1){
                    return;
                } else if (drawType == 3) {
                    deckPass++;
                    if (deckPass < 3){
                        Table.getStock().placeCards(Table.getTalon().getAndClearPile());
                    }
                }
            }
        }
        updateView();
    }



    class Victory extends Thread {

        int seconds  = 0;

        @Override
        public void run() {

            while (!victory()){
                try {
                    seconds += 1;
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                view.getGamePanel().updateTime(seconds);
            }
            System.out.println("Victory");
        }

        @Override
        public void interrupt() {
            seconds  = 0;
        }

        private boolean victory(){
            int cardCount = 0;
            cardCount += Table.getFoundations().get("foundation1").getPile().size();
            cardCount += Table.getFoundations().get("foundation2").getPile().size();
            cardCount += Table.getFoundations().get("foundation3").getPile().size();
            cardCount += Table.getFoundations().get("foundation4").getPile().size();

            return cardCount == 52;
        }

    }


}
