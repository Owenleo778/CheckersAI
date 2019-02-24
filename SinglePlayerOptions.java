import javax.swing.*;
import java.awt.*;

public class SinglePlayerOptions extends JPanel{

    private Boolean playerWhite;
    private int searchDepth;

    public SinglePlayerOptions(){
        super();
        init();
    }

    public void init(){
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());

        setPlayerWhite(true);
        setSearchDepth(5);

        JRadioButton playerWhite = new JRadioButton("Play as White.");
        playerWhite.addActionListener(e -> setPlayerWhite(true));
        JRadioButton playerBlack = new JRadioButton("Play as Black.");
        playerBlack.addActionListener(e -> setPlayerWhite(false));
        ButtonGroup playersGroup = new ButtonGroup();
        playersGroup.add(playerWhite);
        playersGroup.add(playerBlack);
        playerWhite.setSelected(true);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(playerWhite, gbc);

        gbc.gridx = 1;
        add(playerBlack, gbc);

        JLabel difficultyLabel = new JLabel("Depth of AI search (Difficulty):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(difficultyLabel, gbc);


        JSlider difficulty = new JSlider(2, 8);
        difficulty.setMinorTickSpacing(1);
        difficulty.setMajorTickSpacing(2);
        difficulty.setPaintTicks(true);
        difficulty.setPaintLabels(true);
        difficulty.addChangeListener(e -> setSearchDepth(difficulty.getValue()));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(difficulty, gbc);

        //setVisible(false);

    }

    /**
     * Sets the depth in which the ai should search to
     * @param depth the depth
     */
    private void setSearchDepth(int depth){
        searchDepth = depth;
    }

    /**
     * Returns the depth value
     * @return the depth value
     */
    public int getSearchDepth() {
        return searchDepth;
    }

    /**
     * Sets if the player is white or not (black)
     * @param player true if the player is white
     */
    private void setPlayerWhite(Boolean player){
        playerWhite = player;
    }

    public Boolean getPlayerWhite(){
        return playerWhite;
    }
}
