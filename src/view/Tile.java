package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Tile extends Canvas {
private int value;


public void setValue(int value) {
	this.value = value;
	redraw();
}

public Tile(Composite parent, int style) {
	super(parent, style);
	//this.value = 0;
	 	Font font = getFont();
	 	setFont(new Font(getDisplay(),font.getFontData()[0].getName(),16,SWT.BOLD));
	 	addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent event) {
				//Set the default bgcolor of the canvas
				setBackground(new Color(getDisplay(), 187, 173, 160));
				
				//Set the fontMetrics
				FontMetrics fm = event.gc.getFontMetrics();
				int charWidth = fm.getAverageCharWidth();
				int mX = getSize().x/2 - (value + "").length()*charWidth/2;
				int mY = getSize().y/2 - (fm.getHeight()/2) - fm.getDescent();

				//Set the font color
				setForeground(new Color(getDisplay(), 119, 110, 101));
				
				//Set the color and Draw the RoundedRectangle shape  (passes the event in order to change the event's specific color)
				setTileBackground(event);
				event.gc.fillRoundRectangle(0, 0, getSize().x, getSize().y, 30, 30);
				
				//Set another font color for numbers higher than 8
				if (value>8)
 					event.gc.setForeground(new Color(getDisplay(), 255, 255, 255));
				
				//Set the text
				if (value>0) {
					event.gc.drawString(value + "", mX, mY);
				}
			}
		}); 		
}



public void setTileBackground(PaintEvent event){
	switch (value) {
	case 0: 
		event.gc.setBackground(new Color(getDisplay(), 205, 193, 180)); 	
		break;

	case 2: 
		event.gc.setBackground(new Color(getDisplay(), 238, 228, 218));	 
		break;

	case 4: 
		event.gc.setBackground(new Color(getDisplay(), 237, 224, 200));
		break;

	case 8:  
		event.gc.setBackground(new Color(getDisplay(), 242, 177, 121));
		break;

	case 16:  
		event.gc.setBackground(new Color(getDisplay(), 245, 149, 99));
		break;

	case 32:  
		event.gc.setBackground(new Color(getDisplay(), 246, 124, 95));
		break;

	case 64:  
		event.gc.setBackground(new Color(getDisplay(), 246, 94, 59));
		break;

	case 128:  
		event.gc.setBackground(new Color(getDisplay(), 237, 207, 114));
		break;

	case 256:  
		event.gc.setBackground(new Color(getDisplay(), 237, 204, 97));
		break;

	case 512: 
		event.gc.setBackground(new Color(getDisplay(), 237, 200, 80));	 
		break;

	case 1024:  
		event.gc.setBackground(new Color(getDisplay(), 237, 197, 63));
		break;

	case 2048: 
		event.gc.setBackground(new Color(getDisplay(), 237, 194, 46));
		break;

	default:
		break;
	}




}


}
