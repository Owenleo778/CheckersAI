import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Holds everything from the game within this class.
 * @author Owen Salvage
 */
public class Window {

    private JFrame frame;
    private Board board;

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

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.setContentPane(panel);
        panel.setBackground(Color.BLACK);

        board = new Board(this);
        panel.add(board, BorderLayout.CENTER);
        JButton startB = new JButton("Start");
        startB.addActionListener(e -> {board.play(); startB.setEnabled(false);});
        panel.add(startB, BorderLayout.NORTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        //board.play();
    }

}