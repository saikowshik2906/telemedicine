package com.telemed.controller;

import com.telemed.dao.PatientDAO;
import com.telemed.dto.*;
import com.telemed.model.Patient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@Validated
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientDAO patientDAO;

    // PasswordEncoder removed: store passwords as-is

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody PatientRequest request) {
        try {
            // Store the password as provided (no hashing)
            Patient patient = new Patient(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getAge(),
                request.getGender()
            );
            
            boolean success = patientDAO.register(patient);
            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Registration successful");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", "Registration failed. Email may already exist."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Server error: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            int patientId = patientDAO.login(request.getEmail(), request.getPassword());
            
            if (patientId != -1) {
                Patient patient = patientDAO.getById(patientId);
                PatientResponse response = new PatientResponse(
                    patient.getId(),
                    patient.getName(),
                    patient.getEmail(),
                    patient.getPhone(),
                    patient.getAge(),
                    patient.getGender()
                );
                return ResponseEntity.ok(Map.of("success", true, "patient", response));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Server error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable int id) {
        try {
            Patient patient = patientDAO.getById(id);
            if (patient != null) {
                PatientResponse response = new PatientResponse(
                    patient.getId(),
                    patient.getName(),
                    patient.getEmail(),
                    patient.getPhone(),
                    patient.getAge(),
                    patient.getGender()
                );
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Patient not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error: " + e.getMessage()));
        }
    }
}
