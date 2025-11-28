package com.telemed.dto;

import jakarta.validation.constraints.*;

public class AppointmentRequest {
    
    @Min(value = 1, message = "Patient ID is required")
    private int patientId;
    
    @Min(value = 1, message = "Doctor ID is required")
    private int doctorId;
    
    @NotBlank(message = "Date is required (YYYY-MM-DD)")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
    private String date;
    
    @NotBlank(message = "Time is required (HH:MM)")
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "Time must be in HH:MM format")
    private String time;

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
