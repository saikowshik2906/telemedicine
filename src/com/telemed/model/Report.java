package com.telemed.model;

public class Report {
    private int id;
    private int patientId;
    private String reportName;
    private String reportDate;
    private String description;

    public Report() {}

    public Report(int patientId, String reportName, String reportDate, String description) {
        this.patientId = patientId;
        this.reportName = reportName;
        this.reportDate = reportDate;
        this.description = description;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public String getReportName() { return reportName; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
