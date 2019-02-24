import javax.swing.*;
import java.awt.*;

/**
 * Holds everything from the game within this class.
 * @author Owen Salvage
 */
public class Window {

    private JFrame frame;
    private Board board;
    private SinglePlayerOptions sPOpts;
    private boolean whiteStart;


    public Window(){

    }

    public static void main(String[] args) {
        Window w = new Window();
        w.init();
    }

    public void init(){
        frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800,800));
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        frame.setContentPane(panel);
        panel.setBackground(Color.LIGHT_GRAY);

        JRadioButton singlePlayerB = new JRadioButton("One Player.");
        singlePlayerB.addActionListener(e -> setOptVisibility(true));
        JRadioButton twoPlayer = new JRadioButton("Two Players.");
        twoPlayer.addActionListener(e -> setOptVisibility(false));
        ButtonGroup playersGroup = new ButtonGroup();
        playersGroup.add(singlePlayerB);
        playersGroup.add(twoPlayer);
        singlePlayerB.setSelected(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(singlePlayerB, gbc);

        gbc.gridx = 1;
        panel.add(twoPlayer, gbc);

        setWhiteStart(true);

        JRadioButton whiteStartB = new JRadioButton("White Starts.");
        whiteStartB.addActionListener(e -> setWhiteStart(true));
        JRadioButton blackStartB = new JRadioButton("Black Starts.");
        blackStartB.addActionListener(e -> setWhiteStart(false));
        ButtonGroup startGroup = new ButtonGroup();
        startGroup.add(whiteStartB);
        startGroup.add(blackStartB);
        whiteStartB.setSelected(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(whiteStartB, gbc);

        gbc.gridx = 1;
        panel.add(blackStartB, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        sPOpts = new SinglePlayerOptions();
        panel.add(sPOpts, gbc);

        JButton startB = new JButton("Play");
        startB.addActionListener(e -> {startGame(frame); startB.setEnabled(false);});

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(startB, gbc);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void startGame(JFrame frame){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.setContentPane(panel);
        panel.setBackground(Color.BLACK);

        if (sPOpts.isVisible()){  // if single player
            board = new Board(whiteStart, sPOpts.getPlayerWhite(), sPOpts.getSearchDepth());
        } else {
            board = new Board(whiteStart);
        }

        panel.add(board, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        board.play();
    }

    /**
     * Sets the visibility of the single player options
     * @param visible whether it should be visible or not
     */
    private void setOptVisibility(Boolean visible){
        sPOpts.setVisible(visible);
    }

    private void setWhiteStart(boolean start){
        whiteStart = start;
    }

}