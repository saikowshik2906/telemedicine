package com.telemed.controller;

import com.telemed.dao.ReportDAO;
import com.telemed.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportDAO reportDAO;

    @PostMapping
    public ResponseEntity<?> uploadReport(@RequestBody Map<String, Object> request) {
        try {
            int patientId = (Integer) request.get("patientId");
            String name = (String) request.get("name");
            String date = (String) request.get("date");
            String description = (String) request.get("description");

            boolean success = reportDAO.uploadReport(patientId, name, date, description);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "message", "Report uploaded successfully"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", "Failed to upload report"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Server error: " + e.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Report>> getPatientReports(@PathVariable int patientId) {
        try {
            List<Report> reports = reportDAO.getByPatient(patientId);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
