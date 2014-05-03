package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class TileMaze extends Canvas {
    private int value;


    public void setValue(int value) {
        this.value = value;
        redraw();
    }

    public TileMaze(Composite parent, int style) {
        super(parent, style);
        Font font = getFont();
        setFont(new Font(getDisplay(), font.getFontData()[0].getName(), 16, SWT.BOLD));
        addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent event) {
                //Set the default bgcolor of the canvas
                setBackground(new Color(getDisplay(), 187, 173, 160));

                //Set the fontMetrics
                FontMetrics fm = event.gc.getFontMetrics();
                int charWidth = fm.getAverageCharWidth();
                int mX = getSize().x / 2 - (value + "").length() * charWidth / 2;
                int mY = getSize().y / 2 - (fm.getHeight() / 2) - fm.getDescent();

                //Set the font color
                setForeground(new Color(getDisplay(), 119, 110, 101));

                //Set the color and Draw the RoundedRectangle shape  (passes the event in order to change the event's specific color)
                setTileBackground(event);
                event.gc.fillRectangle(0, 0, getSize().x, getSize().y);

                // Mouse
                if (value == 1) {
                    Image mouse = new Image(getDisplay(), "images/mazeMouse.png");
                    event.gc.drawImage(mouse, 0, 0, (mouse.getBounds().width), (mouse.getBounds().height), 0, 0, getSize().x, getSize().y);
                }

                // Cheese
                if (value == 2) {
                    Image cheese = new Image(getDisplay(), "images/mazeCheese.png");
                    event.gc.drawImage(cheese, 0, 0, (cheese.getBounds().width), (cheese.getBounds().height), 0, 0, getSize().x, getSize().y);
                }
            }
        });
    }

    public void setTileBackground(PaintEvent event) {
        switch (value) {
            case -1:
                event.gc.setBackground(new Color(getDisplay(), 111, 111, 111));
                break;

            case 1:
            case 2:
                event.gc.setBackground(new Color(getDisplay(), 150, 150, 150));
                break;

            default:
                event.gc.setBackground(new Color(getDisplay(), 224, 215, 201));
                break;
        }
    }
}
