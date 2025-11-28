package com.telemed.controller;

import com.telemed.dao.AppointmentDAO;
import com.telemed.dto.AppointmentRequest;
import com.telemed.model.Appointment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentDAO appointmentDAO;

    @PostMapping
    public ResponseEntity<?> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        try {
            boolean success = appointmentDAO.book(
                request.getPatientId(),
                request.getDoctorId(),
                request.getDate(),
                request.getTime()
            );
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "message", "Appointment booked successfully"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", "Failed to book appointment"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Server error: " + e.getMessage()));
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable int patientId) {
        try {
            List<Appointment> appointments = appointmentDAO.getByPatient(patientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        try {
            List<Appointment> appointments = appointmentDAO.getAll();
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
        @PathVariable int id,
        @RequestBody Map<String, String> update
    ) {
        try {
            boolean success = appointmentDAO.updateStatus(
                id,
                update.get("status"),
                update.get("remarks")
            );
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Status updated"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "Appointment not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Server error"));
        }
    }
}
