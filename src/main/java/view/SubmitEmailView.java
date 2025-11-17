package view;

import interface_adapter.submit_email.SubmitEmailController;
import interface_adapter.submit_email.SubmitEmailPresenter;
import interface_adapter.submit_email.SubmitEmailViewModel;
import use_case.submit_email.SubmitEmailInteractor;

import javax.swing.*;
import java.awt.*;

public class SubmitEmailView extends JFrame {

    private final JTextArea emailArea = new JTextArea(15, 60);
    private final JLabel metaLabel = new JLabel("Title: —    Sender: —");
    private final JLabel scoreLabel = new JLabel("Score: —");
    private final JTextArea explanationArea = new JTextArea(4, 60);
    private final JButton analyzeButton = new JButton("Analyze");

    private final SubmitEmailViewModel viewModel;
    private final SubmitEmailController controller;

    public SubmitEmailView() {
        super("PhishDetect - Submit Phishing Email");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.viewModel = new SubmitEmailViewModel();
        SubmitEmailPresenter presenter = new SubmitEmailPresenter(viewModel);
        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter);
        this.controller = new SubmitEmailController(interactor);

        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(new JLabel("Paste the email here:"), BorderLayout.NORTH);
        top.add(new JScrollPane(emailArea), BorderLayout.CENTER);

        JPanel mid = new JPanel(new BorderLayout(5, 5));
        mid.add(metaLabel, BorderLayout.NORTH);
        mid.add(scoreLabel, BorderLayout.CENTER);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonRow.add(analyzeButton);
        mid.add(buttonRow, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(new JLabel("Explanation:"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(explanationArea), BorderLayout.CENTER);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(top, BorderLayout.NORTH);
        root.add(mid, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        analyzeButton.addActionListener(e -> onAnalyze());
    }

    private void onAnalyze() {
        String raw = emailArea.getText();

        analyzeButton.setEnabled(false);
        scoreLabel.setText("Score: …");
        explanationArea.setText("");
        metaLabel.setText("Title: —    Sender: —");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                controller.analyze(raw);
                return null;
            }

            @Override
            protected void done() {
                analyzeButton.setEnabled(true);

                if (viewModel.getErrorMessage() != null) {
                    JOptionPane.showMessageDialog(
                            SubmitEmailView.this,
                            viewModel.getErrorMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    scoreLabel.setText("Score: —");
                    explanationArea.setText("");
                    metaLabel.setText("Title: —    Sender: —");
                } else {
                    metaLabel.setText("Title: " + viewModel.getTitle()
                            + "    Sender: " + viewModel.getSender());
                    scoreLabel.setText(viewModel.getScoreText());
                    explanationArea.setText(viewModel.getExplanation());
                }
            }
        };
        worker.execute();
    }
}