package view;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class EmailTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Sender", "Title", "Date Received", "Suspicion Score", "Verified Status"};

    private List<String> senders;
    private List<String> titles;
    private List<String> datesReceived;
    private List<String> suspicionScores;
    private List<String> verifiedStatuses;

    public EmailTableModel(List<String> senders, List<String> titles,
                           List<String> datesReceived, List<String> suspicionScores,
                           List<String> verifiedStatuses) {
        this.senders = senders;
        this.titles = titles;
        this.datesReceived = datesReceived;
        this.suspicionScores = suspicionScores;
        this.verifiedStatuses = verifiedStatuses;
    }

    public void setEmails(List<String> senders, List<String> titles,
                          List<String> datesReceived, List<String> suspicionScores,
                          List<String> verifiedStatuses) {
        this.senders = senders;
        this.titles = titles;
        this.datesReceived = datesReceived;
        this.suspicionScores = suspicionScores;
        this.verifiedStatuses = verifiedStatuses;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return senders != null ? senders.size() : 0;
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
        switch (columnIndex) {
            case 0: return senders.get(rowIndex);
            case 1: return titles.get(rowIndex);
            case 2: return datesReceived.get(rowIndex);
            case 3: return suspicionScores.get(rowIndex);
            case 4: return verifiedStatuses.get(rowIndex);
            default: return null;
        }
    }
}