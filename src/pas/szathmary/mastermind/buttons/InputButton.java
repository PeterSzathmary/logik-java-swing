package pas.szathmary.mastermind.buttons;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

// Custom button to take input for Mastermind game.
public class InputButton extends JComponent implements MouseListener
{
    // Properties of button.
    private int currentColor;
    private final Color[] colors;
    private boolean mousePressed;
    private boolean editable;
    private boolean visible;

    // Constructor - initialize values.
    public InputButton()
    {
        this.addMouseListener(this);

        currentColor = 0;
        mousePressed = false;
        editable = false;
        visible = true;
        colors = new Color[7];
        colors[0] = Color.BLACK;
        colors[1] = Color.RED;
        colors[2] = Color.BLUE;
        colors[3] = Color.GREEN;
        colors[4] = Color.YELLOW;
        colors[5] = Color.ORANGE;
        colors[6] = Color.MAGENTA;
    }

    // Redraw button.
    public void paint(Graphics g)
    {
        // If mouse was clicked, go to next color.
        if (mousePressed)
            if (currentColor == 6)
                // And if at end, return to beginning of color list.
                currentColor = 1;
            else
                currentColor++;

        // If visible, show color change.
        if (visible)
            g.setColor(colors[currentColor]);

        // Then redraw.
        if (currentColor == 0 || !visible)
            g.drawOval(0, 0, 30, 30);
        else
            g.fillOval(0, 0, 30, 30);
    }

    // Get current color.
    public int getCurrentColor()
    {
        return currentColor;
    }

    // Set color and then redraw.
    public void setCurrentColor(int newColor)
    {
        currentColor = newColor;
        repaint();
    }

    // Set edit state.
    public void setEdit(boolean edit)
    {
        editable = edit;
    }

    // Set visible state and redraw.
    public void setVis(boolean visible)
    {
        this.visible = visible;
        repaint();
    }

    // Set default size of component.
    public Dimension getMinimumSize()
    {
        return new Dimension(30, 30);
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(30, 30);
    }

    // If mouse pressed and editable, then redraw.
    public void mousePressed(MouseEvent e)
    {
        if (editable)
        {
            mousePressed = true;
            repaint();
        }
    }

    // Keep track of mouse being released.
    public void mouseReleased(MouseEvent e)
    {
        mousePressed = false;
    }

    // Required when implementing MouseListener.
    public void mouseClicked(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }
}
