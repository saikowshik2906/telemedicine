package com.telemed.dao;

import com.telemed.model.Patient;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class PatientDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public boolean register(Patient p) {
        try (Connection con = getConnection()) {
            String q = "INSERT INTO patients(name,email,password,phone,age,gender) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, p.getName());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getPassword());
            ps.setString(4, p.getPhone());
            ps.setInt(5, p.getAge());
            ps.setString(6, p.getGender());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Email already registered!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int login(String email, String password) {
        try (Connection con = getConnection()) {
            String q = "SELECT id FROM patients WHERE email=? AND password=?";
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

    public Patient getById(int id) {
        try (Connection con = getConnection()) {
            String q = "SELECT * FROM patients WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setEmail(rs.getString("email"));
                p.setPhone(rs.getString("phone"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
