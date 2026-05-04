package main;

import interfaces.ParkInterface;
import ui.UserInterface;

public class Park implements ParkInterface {
    private final UserInterface ui;

    public Park() {
        this.ui = new UserInterface();
    }

    @Override
    public void run() {
        ui.start();
    }

    public static void main(String[] args) {
        new Park().run();
    }
}