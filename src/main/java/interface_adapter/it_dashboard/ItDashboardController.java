package interface_adapter.it_dashboard;

import interface_adapter.ViewManagerModel;
import view.ItDashboardView;
import view.EmailDecisionView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItDashboardController {

    private final ItDashboardView itDashboardView;
    private final EmailDecisionView emailDecisionView;
    private final ViewManagerModel viewManagerModel;

    public ItDashboardController(ItDashboardView itDashboardView,
                                 EmailDecisionView emailDecisionView,
                                 ViewManagerModel viewManagerModel) {
        this.itDashboardView = itDashboardView;
        this.emailDecisionView = emailDecisionView;
        this.viewManagerModel = viewManagerModel;

        // Double-click on table row → open decision screen
        JTable table = itDashboardView.getItEmailTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    openDecisionScreen(table.getSelectedRow());
                }
            }
        });

        // Hook up buttons on the decision screen (for now just demo)
        emailDecisionView.addConfirmListener(e ->
                System.out.println("Confirm phishing clicked"));
        emailDecisionView.addSafeListener(e ->
                System.out.println("Mark safe clicked"));
        emailDecisionView.addPendingListener(e ->
                System.out.println("Pending clicked"));
        emailDecisionView.addBackListener(e -> {
            viewManagerModel.setState(itDashboardView.getViewName());
            viewManagerModel.firePropertyChange();
        });
    }

    private void openDecisionScreen(int row) {
        JTable table = itDashboardView.getItEmailTable();

        String sender = (String) table.getValueAt(row, 0);
        String title  = (String) table.getValueAt(row, 1);
        Object score  = table.getValueAt(row, 2);
        String date   = table.getValueAt(row, 3).toString();

        String text = "Sender: " + sender +
                "\nTitle: " + title +
                "\nSuspicion score: " + score +
                "\nDate: " + date +
                "\n\n[Email body goes here...]";

        emailDecisionView.setEmailText(text);

        viewManagerModel.setState(emailDecisionView.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
