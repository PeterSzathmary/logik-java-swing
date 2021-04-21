package pas.szathmary.mastermind.GUI;

import pas.szathmary.mastermind.buttons.InputButton;
import pas.szathmary.mastermind.buttons.OutputButton;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame
{
    private static InputButton[] key;
    private static InputButton[][] guesses;
    private static JButton btnEnter;
    private static OutputButton[][] clues;

    public GUI()
    {
        super("Mastermind");
    }

    // Draw GUI.
    public void setupGUI()
    {
        // Create main panels, set color and layout.
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new GridLayout(11, 2, 0, 15));

        JPanel keyPanel = new JPanel();
        keyPanel.setBackground(Color.LIGHT_GRAY);
        keyPanel.setLayout(new FlowLayout());

        // Create buttons to store key, add to panel.
        key = new InputButton[4];
        for (int i = 0; i < 4; i++)
        {
            key[i] = new InputButton();
            keyPanel.add(key[i]);
        }

        // Break between key and guesses.
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        // Create guess panels and corresponding clue panels.
        JPanel[] guessPanels = new JPanel[10];
        JPanel[] cluePanels = new JPanel[10];
        guesses = new InputButton[10][4];
        clues = new OutputButton[10][4];
        for (int i = 0; i < 10; i++)
        {
            cluePanels[i] = new JPanel();
            cluePanels[i].setBackground(Color.LIGHT_GRAY);
            cluePanels[i].setLayout(new GridLayout(2, 2));
            guessPanels[i] = new JPanel();
            guessPanels[i].setBackground(Color.LIGHT_GRAY);
            for (int j = 0; j < 4; j++)
            {
                clues[i][j] = new OutputButton();
                cluePanels[i].add(clues[i][j]);
                guesses[i][j] = new InputButton();
                guessPanels[i].add(guesses[i][j]);
            }
            mainPanel.add(cluePanels[i]);
            mainPanel.add(guessPanels[i]);
        }

        // Input button.
        btnEnter = new JButton("Enter");
        JPanel enterPanel = new JPanel();
        enterPanel.setBackground(Color.LIGHT_GRAY);
        enterPanel.add(btnEnter);

        // Main container.
        Container window = this.getContentPane();
        window.setLayout(new BorderLayout());
        window.add(keyPanel, BorderLayout.NORTH);
        window.add(mainPanel, BorderLayout.CENTER);
        window.add(enterPanel, BorderLayout.SOUTH);

        // Housekeeping.
        this.setDefaultCloseOperation(GUI.EXIT_ON_CLOSE);
        this.setSize(325, 700);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static InputButton[] getKey()
    {
        return key;
    }

    public static InputButton[][] getGuesses()
    {
        return guesses;
    }

    public static JButton getBtnEnter()
    {
        return btnEnter;
    }

    public static OutputButton[][] getClues()
    {
        return clues;
    }

    public static void setKey(InputButton[] key)
    {
        GUI.key = key;
    }

    public static void setGuesses(InputButton[][] guesses)
    {
        GUI.guesses = guesses;
    }

    public static void setBtnEnter(JButton btnEnter)
    {
        GUI.btnEnter = btnEnter;
    }

    public void setClues(OutputButton[][] clues)
    {
        GUI.clues = clues;
    }
}
