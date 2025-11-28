package com.telemed.dao;

import com.telemed.model.Appointment;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AppointmentDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public boolean book(int pid, int did, String date, String time) {
        try (Connection con = getConnection()) {
            String q = "INSERT INTO appointments(patient_id,doctor_id,date,time,status) VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, pid);
            ps.setInt(2, did);
            LocalDate ld = LocalDate.parse(date);
            LocalTime lt = LocalTime.parse(time);
            ps.setDate(3, java.sql.Date.valueOf(ld));
            ps.setTime(4, java.sql.Time.valueOf(lt));
            ps.setString(5, "Pending");
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Appointment> getByPatient(int pid) {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection con = getConnection()) {
            String q = "SELECT a.id,a.patient_id,a.doctor_id,d.name as doctor_name,a.date,a.time,a.status,a.remarks " +
                      "FROM appointments a JOIN doctors d ON a.doctor_id=d.id WHERE patient_id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment apt = new Appointment();
                apt.setId(rs.getInt("id"));
                apt.setPatientId(rs.getInt("patient_id"));
                apt.setDoctorId(rs.getInt("doctor_id"));
                apt.setDoctorName(rs.getString("doctor_name"));
                apt.setDate(rs.getDate("date").toLocalDate().toString());
                apt.setTime(rs.getTime("time").toLocalTime().toString());
                apt.setStatus(rs.getString("status"));
                apt.setRemarks(rs.getString("remarks"));
                appointments.add(apt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public List<Appointment> getAll() {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT a.id, a.patient_id, a.doctor_id, p.name AS patient_name, d.name AS doctor_name, " +
                "a.date, a.time, a.status, a.remarks " +
                "FROM appointments a JOIN patients p ON a.patient_id=p.id JOIN doctors d ON a.doctor_id=d.id"
            );
            while (rs.next()) {
                Appointment apt = new Appointment();
                apt.setId(rs.getInt("id"));
                apt.setPatientId(rs.getInt("patient_id"));
                apt.setDoctorId(rs.getInt("doctor_id"));
                apt.setPatientName(rs.getString("patient_name"));
                apt.setDoctorName(rs.getString("doctor_name"));
                apt.setDate(rs.getDate("date").toLocalDate().toString());
                apt.setTime(rs.getTime("time").toLocalTime().toString());
                apt.setStatus(rs.getString("status"));
                apt.setRemarks(rs.getString("remarks"));
                appointments.add(apt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public boolean updateStatus(int aid, String status, String remarks) {
        try (Connection con = getConnection()) {
            String q = "UPDATE appointments SET status=?, remarks=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setString(1, status);
            ps.setString(2, remarks);
            ps.setInt(3, aid);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
