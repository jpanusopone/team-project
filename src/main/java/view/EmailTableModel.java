package view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for displaying email information in the dashboard.
 */
public class EmailTableModel extends AbstractTableModel {

    private static final int COL_SENDER = 0;
    private static final int COL_TITLE = 1;
    private static final int COL_DATE_RECEIVED = 2;
    private static final int COL_SUSPICION_SCORE = 3;
    private static final int COL_VERIFIED_STATUS = 4;

    private static final String[] COLUMN_NAMES = {
            "Sender", "Title", "Date Received", "Suspicion Score", "Verified Status",
    };

    private List<String> senders;
    private List<String> titles;
    private List<String> datesReceived;
    private List<String> suspicionScores;
    private List<String> verifiedStatuses;

    /**
     * Constructs an EmailTableModel with the given column data.
     *
     * @param initialSenders          the list of senders
     * @param initialTitles           the list of titles
     * @param initialDatesReceived    the list of received dates
     * @param initialSuspicionScores  the list of suspicion scores
     * @param initialVerifiedStatuses the list of verified statuses
     */
    public EmailTableModel(List<String> initialSenders,
                           List<String> initialTitles,
                           List<String> initialDatesReceived,
                           List<String> initialSuspicionScores,
                           List<String> initialVerifiedStatuses) {
        this.senders = initialSenders;
        this.titles = initialTitles;
        this.datesReceived = initialDatesReceived;
        this.suspicionScores = initialSuspicionScores;
        this.verifiedStatuses = initialVerifiedStatuses;
    }

    /**
     * Updates all email columns in the table model and refreshes the view.
     *
     * @param newSenders          the new list of senders
     * @param newTitles           the new list of titles
     * @param newDatesReceived    the new list of received dates
     * @param newSuspicionScores  the new list of suspicion scores
     * @param newVerifiedStatuses the new list of verified statuses
     */
    public void setEmails(List<String> newSenders,
                          List<String> newTitles,
                          List<String> newDatesReceived,
                          List<String> newSuspicionScores,
                          List<String> newVerifiedStatuses) {
        this.senders = newSenders;
        this.titles = newTitles;
        this.datesReceived = newDatesReceived;
        this.suspicionScores = newSuspicionScores;
        this.verifiedStatuses = newVerifiedStatuses;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        int count = 0;
        if (senders != null) {
            count = senders.size();
        }
        return count;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        return switch (columnIndex) {
            case COL_SENDER -> senders.get(rowIndex);
            case COL_TITLE -> titles.get(rowIndex);
            case COL_DATE_RECEIVED -> datesReceived.get(rowIndex);
            case COL_SUSPICION_SCORE -> suspicionScores.get(rowIndex);
            case COL_VERIFIED_STATUS -> verifiedStatuses.get(rowIndex);
            default -> null;
        };
    }

    /**
     * Update the verified status for a single row and notify the table.
     *
     * @param rowIndex  row to update
     * @param newStatus new verified status text
     */
    public void setVerifiedStatusAtRow(int rowIndex, String newStatus) {
        if (verifiedStatuses == null) {
            return;
        }
        if (rowIndex < 0 || rowIndex >= verifiedStatuses.size()) {
            return;
        }

        verifiedStatuses.set(rowIndex, newStatus);
        // Only one cell changed: rowIndex, COL_VERIFIED_STATUS
        fireTableCellUpdated(rowIndex, COL_VERIFIED_STATUS);
    }
}
