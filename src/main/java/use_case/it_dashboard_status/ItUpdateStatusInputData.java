// use_case/it_dashboard_status/ItUpdateStatusInputData.java
package use_case.it_dashboard_status;

public class ItUpdateStatusInputData {
    private final String documentId;
    private final String status;

    public ItUpdateStatusInputData(String documentId, String status) {
        this.documentId = documentId;
        this.status = status;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getStatus() {
        return status;
    }
}
