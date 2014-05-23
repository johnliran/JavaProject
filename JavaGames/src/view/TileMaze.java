package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import controller.Constants;

/**
 * Maze Tiles
 */
public class TileMaze extends Canvas  {
    private int value;

    public void setValue(int value) {
        this.value = value;
        redraw();
    }

    public TileMaze(Composite parent, int style, final MouseCommand mouseCommand) {
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
                if (value > 0) {
                    addMouseListener(mouseListener(mouseCommand));
                    Image figure;
                    switch (value) {
                        case Constants.MOUSE_UP:
                            figure = new Image(getDisplay(), Constants.IMAGE_MAZE_MOUSE_UP);
                            break;

                        case Constants.MOUSE_DOWN:
                            figure = new Image(getDisplay(), Constants.IMAGE_MAZE_MOUSE_DOWN);
                            break;

                        case Constants.MOUSE_RIGHT:
                            figure = new Image(getDisplay(), Constants.IMAGE_MAZE_MOUSE_RIGHT);
                            break;

                        case Constants.MOUSE_LEFT:
                            figure = new Image(getDisplay(), Constants.IMAGE_MAZE_MOUSE_LEFT);
                            break;

                        case Constants.CHEESE:
                            figure = new Image(getDisplay(), Constants.IMAGE_MAZE_CHEESE);
                            break;

                        case Constants.MOUSE_AND_CHEESE:
                            figure = new Image(getDisplay(), Constants.IMAGE_MAZE_MOUSE_AND_CHEESE);
                            break;

                        default:
                            figure = new Image(getDisplay(), Constants.IMAGE_MAZE_MOUSE);
                            break;
                    }
                    event.gc.drawImage(figure, 0, 0, (figure.getBounds().width), (figure.getBounds().height), 0, 0, getSize().x, getSize().y);
                }
            }
        });
    }

    public void setTileBackground(PaintEvent event) {
        switch (value) {
            case Constants.WALL:
                event.gc.setBackground(new Color(getDisplay(), 111, 111, 111));
                break;

            default:
                event.gc.setBackground(new Color(getDisplay(), 224, 215, 201));
                break;
        }
    }

    private MouseListener mouseListener(final MouseCommand mouseCommand) {
        return new MouseListener() {
            public void mouseDown(MouseEvent event) {
            }

            public void mouseUp(MouseEvent event) {
                mouseCommand.setMouseCommand(new Point(event.x, event.y), new Point(getSize().x, getSize().y));
            }

            public void mouseDoubleClick(MouseEvent event) {
            }
        };
    }
}
