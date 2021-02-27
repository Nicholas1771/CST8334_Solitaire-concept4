import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class GamePanel extends JPanel {

    Image backgroundImage;

    private final double WIDTH_HEIGHT_RATIO = 0.6543560606;
    private final double EDGE_PADDING_PERCENT = 0.1;
    private int CARD_WIDTH;
    private int CARD_HEIGHT;

    private Map<String, Point> tableLocationPoints = new HashMap<>();
    private Map<String, ArrayList<Card>> tableLocationCardLists = new HashMap<>();

    private JButton stockButton;
    private JButton newGameButton;

    private JButton autoMoveButton;

    private JButton backButton;

    private JButton mute;

    private JComboBox<String> cardBackColourButton;

    private JComboBox<String> backgroundImageButton;

    private JButton frontStyleButton;

    private JTextArea scoreText;
    private JTextArea timeCounter;

    private MyMouseListener myMouseListener = new MyMouseListener();
    private ArrayList<Card> attachedCards = new ArrayList<>();

    private int pointFoundation;
    private int pointTableau;
    private int pointCardFlip;
    private int pointFromFoundation;

    public int getPointFoundation () {
        return pointFoundation;
    }

    public int getPointTableau () {
        return pointTableau;
    }

    public int getPointCardFlip () {
        return pointCardFlip;
    }

    public int getPointFromFoundation () {
        return pointFromFoundation;
    }

    private String gameType;
    private int drawType;

    public GamePanel (String gameType, int drawType) {

        {
            try {
                backgroundImage = ImageIO.read(new File("images/backgrounds/background_green.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.gameType = gameType;
        this.drawType = drawType;



        backButton = new JButton("Back to Launcher");
        add(backButton);

        add(Box.createRigidArea(new Dimension(30, 0)));

        autoMoveButton = new JButton("Auto Move");
        add(autoMoveButton);

        stockButton = new JButton();
        stockButton.setOpaque(false);
        stockButton.setContentAreaFilled(false);
        add(stockButton);

        newGameButton = new JButton("New Game");
        add(newGameButton);


        timeCounter = new JTextArea("Time: 0");
        timeCounter.setEditable(false);
        timeCounter.setOpaque(false);
        timeCounter.setForeground(Color.white);
        timeCounter.setFont(timeCounter.getFont().deriveFont(40f));
        add(timeCounter);

        add(Box.createRigidArea(new Dimension(20, 0)));

        scoreText = new JTextArea();
        scoreText.setEditable(false);
        scoreText.setOpaque(false);
        scoreText.setForeground(Color.white);
        scoreText.setFont(scoreText.getFont().deriveFont(40f));
        add(scoreText);
        updateScore(0);

        add(Box.createRigidArea(new Dimension(50, 0)));

        frontStyleButton = new JButton("Change Style");
        add(frontStyleButton);

        if (gameType.equals("c")) {
            classicGame();
        }else if (gameType.equals("v")){
            vegasGame();
        }

        cardBackColourButton = new JComboBox<>(new String[]{"Red", "Blue", "Orange", "Green", "Purple", "Black"});
        JLabel backColourLabel = new JLabel("Card Colour:");
        backColourLabel.setForeground(Color.white);
        add(backColourLabel);
        add(cardBackColourButton);

        backgroundImageButton = new JComboBox<>(new String[]{"Green", "Blue", "Red","Wood"});
        JLabel backgroundColourLabel = new JLabel("Background:");
        backgroundColourLabel.setForeground(Color.white);
        add(backgroundColourLabel);
        add(backgroundImageButton);

        mute = new JButton("Sound\nON");
        mute.addActionListener(e -> {
            AudioPlayer.muted = !AudioPlayer.muted;
            if (AudioPlayer.muted) {
                mute.setText("Sound OFF");
            } else {
                mute.setText("Sound ON");
            }
            AudioPlayer.playSound(AudioPlayer.CLICK);
        });
        add(mute);

        addMouseListener(myMouseListener);
        addMouseMotionListener(myMouseListener);
    }

    public void classicGame(){
        this.pointFoundation = 10;
        this.pointTableau = 5;
        this.pointCardFlip = 5;
        this.pointFromFoundation = -15;
    }

    public void vegasGame(){
        this.pointFoundation = 5;
        Table.setScore(-52);
    }

    public void updateTime(int time){

        timeCounter.setText("Time: " + time);
    }

    public void updateScore(int update) {
        Table.updateScore(update);
        scoreText.setText("Score: " + Table.getScore());
    }

    public JTextArea getScoreText() {
        return scoreText;
    }

    public JButton getStockButton () {
        return stockButton;
    }

    public JButton getNewGameButton () {
        return newGameButton;
    }

    public JButton getBackButton () {return backButton;}

    public JButton getAutoMoveButton () {
        return autoMoveButton;
    }

    public JComboBox<String> getCardBackColourButton () {return cardBackColourButton;}

    public JComboBox<String> getBackgroundImageButton () {return backgroundImageButton;}

    public JButton getFrontStyleButton () {return frontStyleButton;}

    public void setBackgroundImage(String background) {
        {
            try {
                backgroundImage = ImageIO.read(new File("images/backgrounds/background_" + background + ".jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setTableLocationPoints(Dimension dimension) {

        int width = dimension.width;
        int height = dimension.height;

        int padding = Math.min((int) (width * EDGE_PADDING_PERCENT), (int) (height * EDGE_PADDING_PERCENT));

        CARD_HEIGHT = height / 7;
        CARD_WIDTH = (int) (CARD_HEIGHT * WIDTH_HEIGHT_RATIO);

        stockButton.setSize(CARD_WIDTH, CARD_HEIGHT);

        stockButton.setLocation(padding, padding);

        int HORIZONTAL_GAP = (width - padding * 2 - CARD_WIDTH * 7) / 6;

        tableLocationPoints.put("stock", new Point(padding, padding));

        tableLocationPoints.put("talon", new Point(padding + CARD_WIDTH + HORIZONTAL_GAP, padding));
        tableLocationPoints.put("talonThree", new Point(padding + CARD_WIDTH + HORIZONTAL_GAP, padding));

        tableLocationPoints.put("foundation1", new Point(padding + HORIZONTAL_GAP * 3 + CARD_WIDTH * 3, padding));
        tableLocationPoints.put("foundation2", new Point(padding + HORIZONTAL_GAP * 4 + CARD_WIDTH * 4, padding));
        tableLocationPoints.put("foundation3", new Point(padding + HORIZONTAL_GAP * 5 + CARD_WIDTH * 5, padding));
        tableLocationPoints.put("foundation4", new Point(padding + HORIZONTAL_GAP * 6 + CARD_WIDTH * 6, padding));

        tableLocationPoints.put("tableau1", new Point(padding, padding + CARD_HEIGHT + padding));
        tableLocationPoints.put("tableau2", new Point(padding + HORIZONTAL_GAP + CARD_WIDTH, padding + CARD_HEIGHT + padding));
        tableLocationPoints.put("tableau3", new Point(padding + HORIZONTAL_GAP * 2 + CARD_WIDTH * 2, padding + CARD_HEIGHT + padding));
        tableLocationPoints.put("tableau4", new Point(padding + HORIZONTAL_GAP * 3 + CARD_WIDTH * 3, padding + CARD_HEIGHT + padding));
        tableLocationPoints.put("tableau5", new Point(padding + HORIZONTAL_GAP * 4 + CARD_WIDTH * 4, padding + CARD_HEIGHT + padding));
        tableLocationPoints.put("tableau6", new Point(padding + HORIZONTAL_GAP * 5 + CARD_WIDTH * 5, padding + CARD_HEIGHT + padding));
        tableLocationPoints.put("tableau7", new Point(padding + HORIZONTAL_GAP * 6 + CARD_WIDTH * 6, padding + CARD_HEIGHT + padding));
    }

    public void setCardMap (Map<String, ArrayList<Card>> cards) {
        this.tableLocationCardLists = cards;
    }

    @Override
    protected void paintComponent (Graphics g) {
        updateScore(0);
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);

        setTableLocationPoints(getSize());

        Graphics2D g2 = (Graphics2D) g;

        for (Map.Entry<String, Point> entry : tableLocationPoints.entrySet()) {
            Point point = entry.getValue();
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(point.x, point.y, CARD_WIDTH, CARD_HEIGHT, 5, 5);
        }

        for (Map.Entry<String, ArrayList<Card>> locationCardList : tableLocationCardLists.entrySet()) {
            ArrayList<Card> cardList = locationCardList.getValue();
            if (!cardList.isEmpty()){
                Point point = tableLocationPoints.get(locationCardList.getKey());

                switch (locationCardList.getKey()) {
                    case "stock":
                    case "foundation1":
                    case "foundation2":
                    case "foundation3":
                    case "foundation4":
                        for (Card card : cardList) {
                            if (!card.isFocused()){
                                g.drawImage(card.getImage().getImage(), point.x, point.y , CARD_WIDTH, CARD_HEIGHT, this);
                                card.setLocation(point.x,point.y,CARD_WIDTH,CARD_HEIGHT);
                            }
                        }
                        break;
                    case "talon":
                        if (drawType == 1){
                            for (Card card : cardList) {
                                if (!card.isFocused()){
                                    g.drawImage(card.getImage().getImage(), point.x, point.y , CARD_WIDTH, CARD_HEIGHT, this);
                                    card.setLocation(point.x,point.y,CARD_WIDTH,CARD_HEIGHT);
                                }
                            }
                        }
                        break;
                    case "talonThree":
                        if (drawType == 3){
                            int offsetX = 0;
                            for (Card card : cardList) {
                                if (!card.isFocused()){
                                    g.drawImage(card.getImage().getImage(), point.x + offsetX, point.y, CARD_WIDTH, CARD_HEIGHT, this);
                                    card.setLocation(point.x + offsetX,point.y,CARD_WIDTH,CARD_HEIGHT);
                                    offsetX += CARD_WIDTH/5;
                                }
                            }
                        }
                        break;
                    case "tableau1":
                    case "tableau2":
                    case "tableau3":
                    case "tableau4":
                    case "tableau5":
                    case "tableau6":
                    case "tableau7":
                        int offsetY = 0;
                        for (Card card : cardList) {

                            if (!card.isFocused()){
                                g.drawImage(card.getImage().getImage(), point.x, point.y + offsetY, CARD_WIDTH, CARD_HEIGHT, this);
                                card.setLocation(point.x,point.y + offsetY,CARD_WIDTH,CARD_HEIGHT);
                            }
                            if (card.isFaceUp()) {
                                offsetY += CARD_HEIGHT/6;
                            }
                            offsetY += CARD_HEIGHT/10;
                        }
                        break;
                }
            }
        }

        if (!attachedCards.isEmpty()){
            for (Card card: attachedCards){
                g.drawImage(card.getImage().getImage(), (int) card.getX(), (int) card.getY(), CARD_WIDTH, CARD_HEIGHT, this);
                g2.setColor(Color.GREEN);
                g2.drawRoundRect((int) card.getX(), (int) card.getY(), CARD_WIDTH, CARD_HEIGHT, 5, 5);
            }
        }
    }

    private class MyMouseListener extends MouseAdapter {

        private double x;
        private double y;

        public void mouseClicked(MouseEvent event) {

            boolean foundSpot = false;

            if (event.getClickCount() == 2) {

                for (Map.Entry<String, ArrayList<Card>> entryTableCardList : tableLocationCardLists.entrySet()) {

                    if (drawType == 1){
                        if(entryTableCardList.getKey().equals("talonThree")) {
                            continue;
                        }
                    } else if (drawType == 3) {
                        if(entryTableCardList.getKey().equals("talon")) {
                            continue;
                        }
                    }

                    for (int j = entryTableCardList.getValue().size() - 1; j >= 0; j--) {
                        Card card = entryTableCardList.getValue().get(j);
                        if (card.isFaceUp()){
                            Shape cardShape = new Rectangle2D.Double(card.getX(), card.getY(), card.getW(), card.getH());
                            if (cardShape.contains(event.getPoint())) {
                                while (j<=entryTableCardList.getValue().size() - 1) {
                                    Card focusedCard = entryTableCardList.getValue().get(j);
                                    System.out.println(focusedCard.getTableLocation());
                                    attachedCards.add(focusedCard);
                                    j += 1;
                                    if (focusedCard.getTableLocation().equals("foundation1")
                                            || focusedCard.getTableLocation().equals("foundation2")
                                            || focusedCard.getTableLocation().equals("foundation3")
                                            || focusedCard.getTableLocation().equals("foundation4")
                                            || focusedCard.getTableLocation().equals("talon")
                                            || focusedCard.getTableLocation().equals("talonThree")
                                    ) {
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

                if (attachedCards != null){

                    if (attachedCards.size()==1){
                        for (Map.Entry<String, Foundation> foundationEntry : Table.getFoundations().entrySet()){
                            for (Card card: attachedCards){
                                if(placeCardFoundation(card.getTableLocation(),card,foundationEntry.getValue())){
                                    foundSpot = true;
                                }
                            }
                            if (foundSpot){
                                resetCards();
                                return;
                            }
                        }
                    }



                    for (Map.Entry<String, Tableau>  tableauEntry: Table.getTableaux().entrySet()){
                        for (Card card: attachedCards){
                            if(placeCardTableau(card.getTableLocation(),card,tableauEntry.getValue())){
                                foundSpot = true;
                            }
                        }
                        if (foundSpot){
                            resetCards();
                            return;
                        }
                    }

                    resetCards();
                }
                resetCards();


            }
        }

        @Override
        public void mousePressed(MouseEvent event) {

            attachedCards.clear();

            for (Map.Entry<String, ArrayList<Card>> entry : tableLocationCardLists.entrySet()) {
                for (int j = entry.getValue().size() - 1; j >= 0; j--) {
                    Card card = entry.getValue().get(j);
                    if (card.isFaceUp()){
                        Shape local = new Rectangle2D.Double(card.getX(), card.getY(), card.getW(), card.getH());
                        if (local.contains(event.getPoint())) {
                            AudioPlayer.playSound(AudioPlayer.DRAG);
                            while (j<=entry.getValue().size() - 1){
                                Card focusedCard = entry.getValue().get(j);
                                focusedCard.setFocused(true);
                                attachedCards.add(focusedCard);
                                j+=1;
                                if (focusedCard.getTableLocation().equals("foundation1")
                                        || focusedCard.getTableLocation().equals("foundation2")
                                        || focusedCard.getTableLocation().equals("foundation3")
                                        || focusedCard.getTableLocation().equals("foundation4")
                                        || focusedCard.getTableLocation().equals("talon")
                                ){
                                    break;
                                }
                            }
                            card.setFocused(true);
                            x = event.getX();
                            y = event.getY();
                            return;
                        }
                    }
                }
            }



        }

        @Override
        public void mouseDragged(MouseEvent event) {

            double dx = event.getX() - x;
            double dy = event.getY() - y;

            for (Card card: attachedCards){
                if (card != null) {
                    card.setX(card.getX() + dx);
                    card.setY(card.getY() + dy);
                    repaint();
                }
            }

            x += dx;
            y += dy;
        }

        @Override
        public void mouseReleased(MouseEvent event) {

            if (attachedCards.isEmpty()){
                return;
            }

            Shape foundation1 = new Rectangle2D.Double(tableLocationPoints.get("foundation1").x, tableLocationPoints.get("foundation1").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape foundation2 = new Rectangle2D.Double(tableLocationPoints.get("foundation2").x, tableLocationPoints.get("foundation1").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape foundation3 = new Rectangle2D.Double(tableLocationPoints.get("foundation3").x, tableLocationPoints.get("foundation1").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape foundation4 = new Rectangle2D.Double(tableLocationPoints.get("foundation4").x, tableLocationPoints.get("foundation1").y,
                    CARD_HEIGHT, CARD_WIDTH);

            if (foundation1.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Foundation locationTo = Table.getFoundations().get("foundation1");

                placeCardFoundation(locationFrom,attachedCards.get(0),locationTo);
                resetCards();
                return;
            }
            if (foundation2.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Foundation locationTo = Table.getFoundations().get("foundation2");

                placeCardFoundation(locationFrom,attachedCards.get(0),locationTo);
                resetCards();
                return;
            }
            if (foundation3.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Foundation locationTo = Table.getFoundations().get("foundation3");

                placeCardFoundation(locationFrom,attachedCards.get(0),locationTo);
                resetCards();
                return;
            }
            if (foundation4.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Foundation locationTo = Table.getFoundations().get("foundation4");

                placeCardFoundation(locationFrom,attachedCards.get(0),locationTo);
                resetCards();
                return;
            }

            Shape tableau1 = new Rectangle2D.Double(tableLocationPoints.get("tableau1").x, tableLocationPoints.get("tableau1").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape tableau2 = new Rectangle2D.Double(tableLocationPoints.get("tableau2").x, tableLocationPoints.get("tableau2").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape tableau3 = new Rectangle2D.Double(tableLocationPoints.get("tableau3").x, tableLocationPoints.get("tableau3").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape tableau4 = new Rectangle2D.Double(tableLocationPoints.get("tableau4").x, tableLocationPoints.get("tableau4").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape tableau5 = new Rectangle2D.Double(tableLocationPoints.get("tableau5").x, tableLocationPoints.get("tableau5").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape tableau6 = new Rectangle2D.Double(tableLocationPoints.get("tableau6").x, tableLocationPoints.get("tableau6").y,
                    CARD_HEIGHT, CARD_WIDTH);
            Shape tableau7 = new Rectangle2D.Double(tableLocationPoints.get("tableau7").x, tableLocationPoints.get("tableau7").y,
                    CARD_HEIGHT, CARD_WIDTH);


            if (tableau1.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Tableau locationTo = Table.getTableaux().get("tableau1");

                for (Card movedCard: attachedCards) {
                    placeCardTableau(locationFrom,movedCard,locationTo);
                }
                resetCards();
                return;
            }
            if (tableau2.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Tableau locationTo = Table.getTableaux().get("tableau2");

                for (Card movedCard: attachedCards) {
                    placeCardTableau(locationFrom,movedCard,locationTo);
                }
                resetCards();
                return;
            }
            if (tableau3.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Tableau locationTo = Table.getTableaux().get("tableau3");

                for (Card movedCard: attachedCards) {
                    placeCardTableau(locationFrom,movedCard,locationTo);
                }
                resetCards();
                return;
            }
            if (tableau4.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Tableau locationTo = Table.getTableaux().get("tableau4");

                for (Card movedCard: attachedCards) {
                    placeCardTableau(locationFrom,movedCard,locationTo);
                }
                resetCards();
                return;
            }
            if (tableau5.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Tableau locationTo = Table.getTableaux().get("tableau5");

                for (Card movedCard: attachedCards) {
                    placeCardTableau(locationFrom,movedCard,locationTo);
                }
                resetCards();
                return;
            }
            if (tableau6.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Tableau locationTo = Table.getTableaux().get("tableau6");

                for (Card movedCard: attachedCards) {
                    placeCardTableau(locationFrom,movedCard,locationTo);
                }
                resetCards();
                return;
            }
            if (tableau7.contains(event.getPoint())){
                new Table();
                String locationFrom = attachedCards.get(0).getTableLocation();
                Tableau locationTo = Table.getTableaux().get("tableau7");

                for (Card movedCard: attachedCards) {
                    placeCardTableau(locationFrom,movedCard,locationTo);
                }
                resetCards();
                return;
            }

            AudioPlayer.playSound(AudioPlayer.MISS);

            for (Map.Entry<String, ArrayList<Card>> entry : tableLocationCardLists.entrySet()) {
                for (Card card: entry.getValue()){

                    if (card.isFaceUp() && !card.isFocused() && !card.getTableLocation().equals("talon")
                            && !card.getTableLocation().equals("foundation1")
                            && !card.getTableLocation().equals("foundation2")
                            && !card.getTableLocation().equals("foundation3")
                            && !card.getTableLocation().equals("foundation4")){
                        Shape local = new Rectangle2D.Double(card.getX(), card.getY(), card.getW(), card.getH());

                        if (local.contains(event.getPoint())) {
                            new Table();
                            String locationFrom = attachedCards.get(0).getTableLocation();
                            Tableau locationTo = Table.getTableaux().get(card.getTableLocation());

                            for (Card movedCard: attachedCards) {
                                placeCardTableau(locationFrom,movedCard,locationTo);
                            }
                            resetCards();
                            return;
                        }
                    }
                }
            }

            resetCards();
        }

        private void resetCards(){

            if (!attachedCards.isEmpty()) {
                for (Card card: attachedCards){
                    card.setFocused(false);
                }
                attachedCards.clear();
                repaint();
            }
        }

        private boolean placeCardTableau(String locationFrom, Card heldCard, Tableau locationTo){
            if (locationTo.placeCard(heldCard)) {
                switch (locationFrom) {
                    case "foundation1":
                    case "foundation2":
                    case "foundation3":
                    case "foundation4":
                        Foundation foundationFrom = Table.getFoundations().get(locationFrom);
                        foundationFrom.removeCard();
                        updateScore(pointFromFoundation);
                        break;
                    case "tableau1":
                    case "tableau2":
                    case "tableau3":
                    case "tableau4":
                    case "tableau5":
                    case "tableau6":
                    case "tableau7":
                        Tableau tableauFrom = Table.getTableaux().get(locationFrom);
                        if(tableauFrom.removeCard()){
                            updateScore(pointCardFlip);
                        }
                        break;
                    case "talon":
                        Talon talonFrom = Table.getTalon();
                        talonFrom.removeCard(drawType);
                        updateScore(pointTableau);
                        break;
                }
                return true;
            }
            return false;
        }

        private boolean placeCardFoundation(String locationFrom, Card heldCard, Foundation locationTo){
            if (locationTo.placeCard(heldCard)) {
                switch (locationFrom) {
                    case "foundation1":
                    case "foundation2":
                    case "foundation3":
                    case "foundation4":
                        Foundation foundationFrom = Table.getFoundations().get(locationFrom);
                        foundationFrom.removeCard();
                        break;
                    case "tableau1":
                    case "tableau2":
                    case "tableau3":
                    case "tableau4":
                    case "tableau5":
                    case "tableau6":
                    case "tableau7":
                        Tableau tableauFrom = Table.getTableaux().get(locationFrom);
                        if(tableauFrom.removeCard()){
                            updateScore(pointCardFlip);
                        }
                        updateScore(pointFoundation);
                        break;
                    case "talon":
                        Talon talonFrom = Table.getTalon();
                        talonFrom.removeCard(drawType);
                        updateScore(pointFoundation);
                        break;
                }
                return true;
            }
            return false;
        }



    }



}
