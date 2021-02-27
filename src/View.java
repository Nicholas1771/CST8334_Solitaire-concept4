import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class View {

    private JFrame window;

    private GamePanel gamePanel;

    private final int WINDOW_WIDTH = 1500;
    private final int WINDOW_HEIGHT = 700;

    public View (String gameType, int drawType) {
        initFrame(gameType,drawType);
    }

    private void initFrame (String gameType, int drawType) {
        window = new JFrame();

        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Solitaire");
        window.setVisible(true);

        gamePanel = new GamePanel(gameType, drawType);
        gamePanel.setBackground(Color.DARK_GRAY);
        gamePanel.setPreferredSize(new Dimension(400, 300));

        window.add(gamePanel);
    }

    public JFrame getWindow () {return window;}

    public GamePanel getGamePanel () {
        return gamePanel;
    }

}
