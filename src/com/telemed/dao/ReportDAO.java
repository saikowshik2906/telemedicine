package com.telemed.dao;

import com.telemed.model.Report;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReportDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }

    public boolean uploadReport(int pid, String name, String date, String desc) {
        try (Connection con = getConnection()) {
            String q = "INSERT INTO reports(patient_id, report_name, report_date, description) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, pid);
            ps.setString(2, name);
            LocalDate ld = LocalDate.parse(date);
            ps.setDate(3, java.sql.Date.valueOf(ld));
            ps.setString(4, desc);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Report> getByPatient(int pid) {
        List<Report> reports = new ArrayList<>();
        try (Connection con = getConnection()) {
            String q = "SELECT * FROM reports WHERE patient_id=?";
            PreparedStatement ps = con.prepareStatement(q);
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Report r = new Report();
                r.setId(rs.getInt("id"));
                r.setPatientId(rs.getInt("patient_id"));
                r.setReportName(rs.getString("report_name"));
                r.setReportDate(rs.getDate("report_date").toLocalDate().toString());
                r.setDescription(rs.getString("description"));
                reports.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }
}
