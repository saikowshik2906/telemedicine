package com.telemed.main;
import com.telemed.dao.*;
import com.telemed.model.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
    Pattern emailPat = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        PatientDAO pdao = new PatientDAO();
        DoctorDAO ddao = new DoctorDAO();
        AppointmentDAO adao = new AppointmentDAO();
        ReportDAO rdao = new ReportDAO();

        while (true) {
            System.out.println("\n====== Global Telemedicine System ======");
            System.out.println("1. Register as Patient");
            System.out.println("2. Patient Login");
            System.out.println("3. Doctor Login");
            System.out.println("4. Admin Panel");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int ch = -1;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input, try again.");
                continue;
            }

            switch (ch) {
                case 1:
                {
                    // Registration with validation
                    String n="";
                    while (n.trim().isEmpty()) { System.out.print("Name: "); n = sc.nextLine(); if (n.trim().isEmpty()) System.out.println("Name cannot be empty."); }

                    String e="";
                    while (true) {
                        System.out.print("Email: "); e = sc.nextLine();
                        if (!emailPat.matcher(e).matches()) System.out.println("Invalid email format, try again."); else break;
                    }

                    String p="";
                    while (p.trim().length() < 4) { System.out.print("Password (min 4 chars): "); p = sc.nextLine(); if (p.trim().length() < 4) System.out.println("Password too short."); }

                    String ph="";
                    while (true) {
                        System.out.print("Phone (10 digits): "); ph = sc.nextLine();
                        String digits = ph.replaceAll("\\D", "");
                        if (digits.length() != 10) System.out.println("Phone must contain exactly 10 digits."); else { ph = digits; break; }
                    }

                    int age = -1;
                    while (age <= 0) {
                        System.out.print("Age: ");
                        try { age = Integer.parseInt(sc.nextLine()); if (age <= 0) System.out.println("Enter a valid positive age."); } catch (Exception ex) { System.out.println("Invalid number, try again."); }
                    }

                    String g="";
                    while (g.trim().isEmpty()) { System.out.print("Gender: "); g = sc.nextLine(); if (g.trim().isEmpty()) System.out.println("Gender cannot be empty."); }

                    if (pdao.register(new Patient(n,e,p,ph,age,g)))
                        System.out.println(" Registered successfully!");
                    else System.out.println(" Registration failed!");
                    break;
                }

                case 2:
                {
                    System.out.print("Email: "); String e = sc.nextLine();
                    System.out.print("Password: "); String p = sc.nextLine();
                    int pid = pdao.login(e,p);
                    if (pid != -1) {
                        System.out.println(" Welcome Patient!");
                        patientMenu(sc, pdao, ddao, adao, rdao, pid);
                    } else System.out.println(" Invalid credentials!");
                    break;
                }

                case 3:
                {
                    System.out.print("Email: "); String e = sc.nextLine();
                    System.out.print("Password: "); String p = sc.nextLine();
                    int did = ddao.login(e,p);
                    if (did != -1) {
                        System.out.println(" Welcome Doctor!");
                        doctorMenu(sc, adao, did);
                    } else System.out.println(" Invalid credentials!");
                    break;
                }

                case 4:
                {
                    System.out.print("Admin Password: ");
                    String adm = sc.nextLine();
                    if (adm.equals("admin123")) {
                        System.out.println(" Admin Access Granted");
                        // simple admin sub-menu
                        while (true) {
                            System.out.println("\n--- Admin Panel ---");
                            System.out.println("1. View All Appointments");
                            System.out.println("2. Add Doctor");
                            System.out.println("3. View All Doctors");
                            System.out.println("4. Back");
                            System.out.print("Enter: ");
                            int aopt = -1;
                            try { aopt = Integer.parseInt(sc.nextLine()); } catch (Exception ex) { System.out.println("Invalid"); continue; }
                            switch (aopt) {
                                case 1:
                                    adao.viewAll();
                                    break;
                                case 2: {
                                    // Admin add doctor with validation
                                    String dn = "";
                                    while (dn.trim().isEmpty()) { System.out.print("Doctor Name: "); dn = sc.nextLine(); if (dn.trim().isEmpty()) System.out.println("Name cannot be empty."); }

                                    String spec = "";
                                    while (spec.trim().isEmpty()) { System.out.print("Specialization: "); spec = sc.nextLine(); if (spec.trim().isEmpty()) System.out.println("Specialization cannot be empty."); }

                                    String dem = "";
                                    while (true) { System.out.print("Email: "); dem = sc.nextLine(); if (!emailPat.matcher(dem).matches()) System.out.println("Invalid email format, try again."); else break; }

                                    String dpw = "";
                                    while (dpw.trim().length() < 4) { System.out.print("Password (min 4 chars): "); dpw = sc.nextLine(); if (dpw.trim().length() < 4) System.out.println("Password too short."); }

                                    Doctor doc = new Doctor(dn, spec, dem, dpw);
                                    if (ddao.addDoctor(doc)) System.out.println("✅ Doctor added successfully!");
                                    else System.out.println("Failed to add doctor.");
                                    break;
                                }
                                case 3:
                                    ddao.viewAllDoctors();
                                    break;
                                case 4:
                                    System.out.println("Exiting Admin Panel");
                                    break;
                                default:
                                    System.out.println("Invalid option.");
                                    break;
                            }
                            if (aopt == 4) break;
                        }
                        System.out.println("\n(Use Doctor Login to update appointment status)");
                    } else System.out.println(" Access denied!");
                    break;
                }

                case 5:
                {
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    System.exit(0);
                }

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static void patientMenu(Scanner sc, PatientDAO pdao, DoctorDAO ddao, AppointmentDAO adao, ReportDAO rdao, int pid) {
        while (true) {
            System.out.println("\n--- Patient Menu ---");
            System.out.println("1. View Profile\n2. View Doctors\n3. Book Appointment\n4. My Appointments\n5. Upload Report\n6. View Reports\n7. Logout");
            System.out.print("Enter: ");
            int opt = -1;
            try { opt = Integer.parseInt(sc.nextLine()); } catch (Exception e) { System.out.println("Invalid"); continue; }

            switch (opt) {
                case 1:
                    pdao.viewProfile(pid);
                    break;
                case 2:
                    ddao.viewAllDoctors();
                    break;
                case 3: {
                    System.out.print("Doctor ID: "); int did = Integer.parseInt(sc.nextLine());
                    // validate doctor id
                    while (did <= 0) {
                        System.out.print("Doctor ID: ");
                        try { did = Integer.parseInt(sc.nextLine()); if (did <= 0) System.out.println("Enter a valid doctor id."); } catch (Exception ex) { System.out.println("Invalid id, try again."); }
                    }

                    String d="";
                    while (true) {
                        System.out.print("Date (YYYY-MM-DD): "); d = sc.nextLine();
                        try { LocalDate.parse(d); break; } catch (DateTimeParseException ex) { System.out.println("Invalid date format, use YYYY-MM-DD."); }
                    }

                    String t="";
                    while (true) {
                        System.out.print("Time (HH:MM): "); t = sc.nextLine();
                        try { LocalTime.parse(t); break; } catch (DateTimeParseException ex) { System.out.println("Invalid time format, use HH:MM (24-hour)."); }
                    }

                    if (adao.book(pid, did, d, t)) System.out.println("📅 Appointment booked!");
                    else System.out.println("Failed to book.");
                    break;
                }
                case 4:
                    adao.viewByPatient(pid);
                    break;
                case 5: {
                    System.out.print("Report Name: "); String rn = sc.nextLine();
                    String rd="";
                    while (true) {
                        System.out.print("Date (YYYY-MM-DD): "); rd = sc.nextLine();
                        try { LocalDate.parse(rd); break; } catch (DateTimeParseException ex) { System.out.println("Invalid date format, use YYYY-MM-DD."); }
                    }
                    System.out.print("Description: "); String desc = sc.nextLine();
                    if (rdao.uploadReport(pid, rn, rd, desc)) System.out.println("✅ Report uploaded!");
                    else System.out.println("Failed to upload.");
                    break;
                }
                case 6:
                    rdao.viewReports(pid);
                    break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    private static void doctorMenu(Scanner sc, AppointmentDAO adao, int did) {
        while (true) {
            System.out.println("\n--- Doctor Menu ---");
            System.out.println("1. View All Appointments\n2. Update Appointment Status\n3. Logout");
            System.out.print("Enter: ");
            int opt = -1;
            try { opt = Integer.parseInt(sc.nextLine()); } catch (Exception e) { System.out.println("Invalid"); continue; }

            switch (opt) {
                case 1:
                    adao.viewAll();
                    break;
                case 2: {
                    System.out.print("Appointment ID: "); int aid = Integer.parseInt(sc.nextLine());
                    System.out.print("Status (Confirmed/Cancelled): "); String s = sc.nextLine();
                    System.out.print("Remarks: "); String r = sc.nextLine();
                    adao.updateStatus(aid, s, r);
                    break;
                }
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
}
