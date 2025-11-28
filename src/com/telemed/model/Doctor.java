package com.telemed.model;

public class Doctor {
    private int id;
    private String name, specialization, email, password;

    public Doctor() {}

    public Doctor(String name, String specialization, String email, String password) {
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.password = password;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
