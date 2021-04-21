package pas.szathmary.mastermind;

import pas.szathmary.mastermind.GUI.GUI;
import pas.szathmary.mastermind.buttons.InputButton;
import pas.szathmary.mastermind.buttons.OutputButton;

import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

// Mastermind GUI and game logic
public class Mastermind implements ActionListener
{
    //#region VARIABLES
    // Store guesses and clues, also UI elements.
    InputButton[][] guesses;
    OutputButton[][] clues;
    // Store and display key.
    InputButton[] key;
    // Submit guess or key.
    JButton btnEnter;
    // Counter of position in guess list.
    int counter;
    // Current game mode.
    int mode;
    // String representation of key.
    String keyVal = "";
    // Needed for random key generation.
    Random rand = new Random();
    GUI gui;
    //#endregion

    //#region CONSTRUCTOR
    // Game constructor.
    public Mastermind()
    {
        gui = new GUI();
        gui.setupGUI();
        key = GUI.getKey();
        guesses = GUI.getGuesses();
        btnEnter = GUI.getBtnEnter();
        clues = GUI.getClues();
        initValues();
        registerListeners();
    }
    //#endregion

    //#region METHODS
    // Reset the board, initialize game.
    public void initValues()
    {
        // Ask for and set game mode.
        Object[] options = {"Code breaker", "Code maker"};
        mode = JOptionPane.showOptionDialog(null,
                """
                        HOW TO PLAY
                        Click on any circle to place a peg.
                        To change a peg to a different color,
                        click on it again.
                                                
                        ON THE LEFT SIDE
                        white marker = correct color
                        black marker = correct color and place
                                                
                        Who would you like to play as?""",
                "Game Mode",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // Init counter.
        counter = 9;

        // Init key.
        for (int i = 0; i < 4; i++)
        {
            // Random if in code breaker mode.
            if (mode == 0)
            {
                key[i].setVis(false);
                key[i].setCurrentColor((rand.nextInt(6) + 1));
            }
            else
            {
                // User input if in code maker mode.
                key[i].setCurrentColor(0);
                key[i].setEdit(true);
            }
        }

        // Save key as string if key exists.
        if (mode == 0)
            keyVal = stringValue(key);

        // Clear the guesses section of the board, reset for input.
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 4; j++)
            {
                guesses[i][j].setCurrentColor(0);
                guesses[i][j].setEdit(i == 9 && mode == 0);
                clues[i][j].setCurrColor(0);
            }
    }

    // Get string value of a set of input buttons.
    // This is helpful to have for scoring.
    public String stringValue(InputButton[] buttons)
    {
        StringBuilder value = new StringBuilder();

        // Store color as string of form "XXXX"
        // where X = [1,6]
        for (InputButton button : buttons) value.append(String.format("%d", button.getCurrentColor()));

        System.out.println(value);
        return value.toString();
    }

    // Register listener for enter button.
    public void registerListeners()
    {
        btnEnter.addActionListener(this);
    }

    // Set action for enter button.
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(e.getActionCommand());
        if (e.getSource() == btnEnter && mode == 0)
        {
            // Code breaker mode.
            submitGuess();
        }
        else if (e.getSource() == btnEnter && mode == 1)
        {
            // Code maker mode.
            playComp();
        }
        else
        {
            System.out.println("ERROR");
        }
    }

    // Handles submission of input.
    public void submitGuess()
    {
        boolean win;
        // Check for valid input.
        if (validGuess(guesses[counter]))
        {
            // Score the entry and check for winning.
            win = returnClue();
            if (win)
            {
                System.out.println("winner");
                gameOver(true);
                return;
            }

            // Decrement counter to the next guess.
            counter--;

            // Check if out of guesses, player loses.
            if (counter < 0)
            {
                gameOver(false);
                return;
            }

            // Set last to no edit, set next guess to edit.
            for (int i = 0; i < 4; i++)
            {
                guesses[counter][i].setEdit(true);
                guesses[counter + 1][i].setEdit(false);
            }
        }
    }

    // Checks for valid input (all circles have a color).
    public boolean validGuess(InputButton[] button)
    {
        for (int i = 0; i < 4; i++)
        {
            if (button[i].getCurrentColor() == 0)
            {
                return false;
            }
        }
        return true;
    }

    // Handles end of game.
    public void gameOver(boolean winner)
    {
        // Reveal the key.
        for (int i = 0; i < 4; i++)
        {
            key[i].setVis(true);
        }

        // Set message to player.
        String msg;
        if (winner)
        {
            msg = "You win!";
        }
        else
        {
            msg = "You lose!";
        }

        // Display message and ask for new game.
        int ans =
                JOptionPane.showConfirmDialog(null, "Do you want to play again?",
                        msg, JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

        // Reset game, or end.
        if (ans == 0)
            initValues();
        else if (ans == 1)
            System.exit(0);
        else
            System.out.println("ERROR");
    }

    // Plays for the computer.
    public void playComp()
    {
        // Make sure user has entered a valid key.
        if (validGuess(key))
        {
            // Holds computer guess and score of guess.
            String guess;
            String[] sc;

            // Holds all possible key.
            ArrayList<String> possible = new ArrayList<>();

            // Hold keys that have not been ruled out.
            ArrayList<String> sol = new ArrayList<>();

            // Seeds arraylists - to start both hold all 1296 possible keys.
            // Possible values are 1111-6666, using 1-6 only.
            for (int i = 1111; i < 6667; i++)
            {
                if (!String.format("%d", i).contains("0") &&
                        !String.format("%d", i).contains("7") &&
                        !String.format("%d", i).contains("8") &&
                        !String.format("%d", i).contains("9"))
                {
                    possible.add(String.format("%d", i));
                    sol.add(String.format("%d", i));
                }
            }

            // Save string version of user-provided key.
            keyVal = stringValue(key);

            // Computer's initial guess.
            // Display guess, check for a win and display clue.
            // Calculate guess score.
            guess = "1122";
            guesses[counter][0].setCurrentColor(1);
            guesses[counter][1].setCurrentColor(1);
            guesses[counter][2].setCurrentColor(2);
            guesses[counter][3].setCurrentColor(2);
            submitGuess();
            sc = score(keyVal, guess).split(",");

            // Try until out of guesses or we win.
            while (counter >= 0 && !sc[0].equals("4"))
            {
                // Rule out impossible guesses from the remaining guesses.
                for (int i = 0; i < sol.size(); i++)
                {
                    // Score our guess versus a possible key
                    // if the score does not match the actual score,
                    // then this must not be a possible key.
                    while (i != sol.size() &&
                            !Arrays.deepEquals(sc,
                                    score(sol.get(i),
                                            guess).split(",")))
                        sol.remove(i);
                }

                // If we only have one possibility left
                // it must be the key. Guess that..
                if (sol.size() == 1)
                    guess = sol.get(0);
                else
                {
                    // Otherwise we must calculate the best possible
                    // guess: The guess that minimizes the
                    // maximum number of remaining possibilities.
                    int minimalValue = 1296;
                    for (String s : possible)
                    {
                        // For any possible guess calculate the max number
                        // of possibilities left after that guess.
                        int tmp = getMax(s, sol);
                        if (tmp < minimalValue)
                        {
                            // Then keep the lowest of those.
                            minimalValue = tmp;
                            guess = s;
                        }
                    }
                }

                // We have our guess, so input it.
                for (int i = 0; i < 4; i++)
                {
                    guesses[counter][i].setCurrentColor(Integer.parseInt(guess.substring(i, i + 1)));
                }
                sc = score(keyVal, guess).split(",");
                submitGuess();
            }
        }
    }

    // Calculate the maximum possible number of remaining possibilities.
    // That could be eliminate by a potential guess.
    public int getMax(String attempt, ArrayList<String> set)
    {
        // Possible scores.
        String[] scores = {"0,0", "0,1", "0,2", "0,3", "0,4", "1,0", "1,1",
                "1,2", "1,3", "2,0", "2,1", "2,2", "3,0", "4,0"};
        // Counter.
        int max = 0;

        // For each possible score.
        for (String score : scores)
        {
            int cnt = 0;
            // Compare guess to remaining possible keys.
            // Keep the maximum eliminated by a potential score.
            for (String s : set)
            {
                if (score(s, attempt).equals(score))
                {
                    cnt++;
                }
            }
            // And keep the biggest out of that set.
            if (cnt > max)
            {
                max = cnt;
            }
        }
        // And now we'll have the true max.
        return max;
    }

    // Show the clue for the current guess.
    public boolean returnClue()
    {
        // Exact matches.
        int exactMatch = 0;

        // Color matches.
        int colorMatch = 0;

        // Current button.
        int result = 0;

        // String representation of guess.
        String guessVal;

        // Guess score.
        String[] sc;

        // Get string of guess, then score it.
        guessVal = stringValue(guesses[counter]);
        sc = score(keyVal, guessVal).split(",");
        System.out.println(sc[0] + "," + sc[1]);

        // Light up the exact matches.
        for (int i = Integer.parseInt(sc[0]); i > 0; i--)
        {
            clues[counter][result].setCurrColor(2);
            result++;
        }

        // Light up the color matches.
        for (int i = Integer.parseInt(sc[1]); i > 0; i--)
        {
            clues[counter][result].setCurrColor(1);
            result++;
        }

        // Tell the caller if we've won or not.
        return sc[0].equals("4");
    }

    // Calculate a score for a guess as a pair of ints.
    public String score(String key, String guess)
    {
        int exactMatch = 0;
        int colorMatch = 0;

        // First find the exact matches.
        for (int i = 0; i < 4; i++)
        {
            if (key.charAt(i) == guess.charAt(i))
            {
                exactMatch++;
            }
        }

        // Then find the color matches.
        for (int i = 1; i < 7; i++)
        {
            colorMatch += Math.min(count(key, Character.forDigit(i, 10)),
                    count(guess, Character.forDigit(i, 10)));
        }

        // Since these overlap, remove the exact matches from the
        // color matches so we do not duplicate.
        colorMatch -= exactMatch;

        // And return the pair of numbers.
        return exactMatch + "," + colorMatch;
    }

    // Counts the number of a given color in a guess.
    // Used for scoring.
    public int count(String value, char x)
    {
        int cnt = 0;
        for (int i = 0; i < value.length(); i++)
        {
            if (value.charAt(i) == x)
            {
                cnt++;
            }
        }
        return cnt;
    }
    //#endregion
}
