package com.telemed.dao;

import com.telemed.model.Doctor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DoctorDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public int login(String email, String password) {
        try (Connection con = getConnection()) {
            String q = "SELECT id FROM doctors WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setEmail(rs.getString("email"));
                doctors.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public Doctor getById(int id) {
        try (Connection con = getConnection()) {
            String q = "SELECT * FROM doctors WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setEmail(rs.getString("email"));
                return d;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addDoctor(Doctor d) {
        try (Connection con = getConnection()) {
            String q = "INSERT INTO doctors(name,specialization,email,password) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, d.getName());
            ps.setString(2, d.getSpecialization());
            ps.setString(3, d.getEmail());
            ps.setString(4, d.getPassword());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
