package app;

import view.SubmitEmailView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SubmitEmailView().setVisible(true));
    }
}