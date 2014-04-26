package controller;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;

import model.Model;
import view.View;

public class Presenter implements Observer {
	View ui;
	Model m;
	
	@Override
	public void update(Observable observable, Object object) {
		if (observable instanceof Model) {		
			ui.displayScore(((Model) observable).getScore());
			ui.displayData(((Model) observable).getData());	
		}
		if (observable instanceof View) {
				switch (ui.getUserCommand()) {
				case SWT.ARROW_UP: {
					m.moveUp();
					break;
				}
	
				case SWT.ARROW_DOWN: {
					m.moveDown();
					break;
				}
					
				case SWT.ARROW_RIGHT: {
					m.moveRight();
					break;
				}
					
				case SWT.ARROW_LEFT: {
					m.moveLeft();
					break;
				}
				case 1:
					startGame();
	
				default:
					break;
				} 
			}
		}
			
		
	

	public Presenter(Model m, View ui) {
		this.m = m;
		this.ui = ui;
	}
	public void startGame(){
		m.initializeBoard();
	}
}
	
