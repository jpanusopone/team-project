package view;

import entity.Email;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class EmailTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Sender", "Title", "Date Received", "Suspicion Score", "Verified Status"};

    private List<Email> emails;

    public EmailTableModel(List<Email> emails) {
        this.emails = emails;
    }

    public void setEmails(List<Email> newEmails) {
        this.emails = newEmails;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return emails.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Email email = emails.get(rowIndex);
        switch (columnIndex) {
            case 0: return email.getSender();
            case 1: return email.getTitle();
            case 2: return email.getDateReceived();
            case 3: return email.getSuspicionScore();
            case 4: return email.getVerifiedStatus();
            default: return null;
        }
    }
}

