import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Launcher {

    private JFrame window;

    private JButton gameXButton;
    private JButton solitaireButton;

    private final int WINDOW_WIDTH = 1500;
    private final int WINDOW_HEIGHT = 700;

    public Launcher () {
        initFrame();
    }

    private void initFrame () {
        window = new JFrame("Game Launcher");

        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Game Selection");
        window.setVisible(true);

        JPanel selectionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    g.drawImage(ImageIO.read(new File("images/backgrounds/background_launcher.jpg")), 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        gameXButton = new JButton("Game X (In development)");
        solitaireButton = new JButton("Solitaire");

        gameXButton.setPreferredSize(new Dimension(200, 100));
        gameXButton.setEnabled(false);

        solitaireButton.setPreferredSize(new Dimension(200, 100));

        solitaireButton.addActionListener(e -> {
            JPopupMenu gameModeMenu = new JPopupMenu("Choose game mode");

            gameModeMenu.isBorderPainted();

            JMenuItem vegas3Button = new JMenuItem("Vegas Rules - 3 draw");
            JMenuItem vegas1Button = new JMenuItem("Vegas Rules - 1 draw");
            JMenuItem classic3Button = new JMenuItem("Classic - 3 draw");
            JMenuItem classic1Button = new JMenuItem("Classic - 1 draw");
            JMenuItem backButton = new JMenuItem("Back");

            gameModeMenu.add(vegas3Button);
            gameModeMenu.add(vegas1Button);
            gameModeMenu.add(classic3Button);
            gameModeMenu.add(classic1Button);
            gameModeMenu.add(backButton);

            vegas3Button.addActionListener(ee -> {
                AudioPlayer.playSound(AudioPlayer.CLICK);
                new Game("v",3);
                window.dispose();
            });

            vegas1Button.addActionListener(ee -> {
                AudioPlayer.playSound(AudioPlayer.CLICK);
                new Game("v",1);
                window.dispose();
            });

            classic3Button.addActionListener(ee -> {
                AudioPlayer.playSound(AudioPlayer.CLICK);
                new Game("c",3);
                window.dispose();
            });

            classic1Button.addActionListener(ee -> {
                AudioPlayer.playSound(AudioPlayer.CLICK);
                new Game("c",1);
                window.dispose();
            });

            backButton.addActionListener(ee -> {
                AudioPlayer.playSound(AudioPlayer.CLICK);
                gameModeMenu.setVisible(false);
            });

            gameModeMenu.show(solitaireButton, 200, 0);

        });

        selectionPanel.add(gameXButton);
        selectionPanel.add(solitaireButton);

        window.add(selectionPanel);
    }

}
