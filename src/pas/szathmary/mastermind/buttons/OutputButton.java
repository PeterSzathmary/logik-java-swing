package pas.szathmary.mastermind.buttons;

import java.awt.*;
import javax.swing.*;

// Custom button to display clues for Mastermind.
public class OutputButton extends JComponent
{
    // Data member.
    private int currColor;

    // Constructor.
    public OutputButton()
    {
        currColor = 0;
    }

    // Draw component.
    public void paint(Graphics g)
    {
        if (currColor == 1)
        {
            g.setColor(Color.WHITE);
            g.fillOval(0, 0, 10, 10);
        }
        else if (currColor == 2)
        {
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, 10, 10);
        }
        else
        {
            g.setColor(Color.BLACK);
            g.drawOval(0, 0, 10, 10);
        }
    }

    // Get color.
    public int getCurrColor()
    {
        return currColor;
    }

    // Set color and redraw.
    public void setCurrColor(int newColor)
    {
        currColor = newColor;
        repaint();
    }

    // Set default size.
    public Dimension getMinimumSize()
    {
        return new Dimension(10, 10);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(10, 10);
    }
}
