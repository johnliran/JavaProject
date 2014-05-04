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
    private final int MOUSE_RIGHT = 1;
    private final int MOUSE_UP = 2;
    private final int MOUSE_DOWN = 3;
    private final int MOUSE_LEFT = 4;
    private final int CHEESE = 5;
    private final int MOUSE_AND_CHEESE = 6;


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
                if (value > 0) {
	                Image figure;
	
	                switch (value) {
					case MOUSE_RIGHT: {
	                    figure = new Image(getDisplay(), "images/mazeMouse_right.png");

						break;
					}
					case MOUSE_UP: {
	                    figure = new Image(getDisplay(), "images/mazeMouse_up.png");
						break;
					}
					case MOUSE_LEFT: {
	                    figure = new Image(getDisplay(), "images/mazeMouse_left.png");
						break;
					}
					case MOUSE_DOWN: {
	                    figure = new Image(getDisplay(), "images/mazeMouse_down.png");
						break;
					}
					case CHEESE: {
	                    figure = new Image(getDisplay(), "images/mazeCheese.png");
	                    
						break;
					}
					case MOUSE_AND_CHEESE: {
	                    figure = new Image(getDisplay(), "images/mazeMouseAndCheese.png");
	                    
						break;
					}
					
					 default:
	                    figure = new Image(getDisplay(), "images/mazeMouse.png");
	
					}
				
	                event.gc.drawImage(figure, 0, 0, (figure.getBounds().width), (figure.getBounds().height), 0, 0, getSize().x, getSize().y);
                }
            }
        });
    }

    public void setTileBackground(PaintEvent event) {
        switch (value) {
            case -1:
                event.gc.setBackground(new Color(getDisplay(), 111, 111, 111));
                break;

//            case 5:
//                event.gc.setBackground(new Color(getDisplay(), 150, 150, 150));
//                break;

            default:
                event.gc.setBackground(new Color(getDisplay(), 224, 215, 201));
                break;
        }
    }
}
