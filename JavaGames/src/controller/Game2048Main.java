package controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import model.Game2048Model;
import view.Game2048View;

public class Game2048Main {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Game2048Model m = new Game2048Model();
        Game2048View ui = new Game2048View();
        Presenter p = new Presenter(m, ui);
        m.addObserver(p);
        ui.addObserver(p);
        ui.getWindowShell().addObserver(p);
        p.startGame();
    }
}

