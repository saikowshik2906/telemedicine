package com.telemed.controller;

import com.telemed.dao.DoctorDAO;
import com.telemed.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired
    private DoctorDAO doctorDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorDAO.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctor(@PathVariable int id) {
        try {
            Doctor doctor = doctorDAO.getById(id);
            if (doctor != null) {
                return ResponseEntity.ok(doctor);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Doctor not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Server error"));
        }
    }

    @PostMapping
    public ResponseEntity<?> addDoctor(@RequestBody Doctor doctor) {
        try {
            // Hash password before storing
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            boolean success = doctorDAO.addDoctor(doctor);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "message", "Doctor added successfully"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("success", false, "message", "Failed to add doctor"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            int doctorId = doctorDAO.login(credentials.get("email"), credentials.get("password"));
            if (doctorId != -1) {
                Doctor doctor = doctorDAO.getById(doctorId);
                return ResponseEntity.ok(Map.of("success", true, "doctor", doctor));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Server error"));
        }
    }
}
