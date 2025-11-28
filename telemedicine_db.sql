CREATE DATABASE telemedicine_db;
USE telemedicine_db;

CREATE TABLE patients (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  email VARCHAR(50) UNIQUE,
  password VARCHAR(100),
  phone VARCHAR(15),
  age INT,
  gender VARCHAR(10)
);

CREATE TABLE doctors (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50),
  specialization VARCHAR(50),
  email VARCHAR(50),
  password VARCHAR(100)
);

CREATE TABLE appointments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT,
  doctor_id INT,
  date DATE,
  time TIME,
  status VARCHAR(20),
  remarks VARCHAR(255),
  FOREIGN KEY (patient_id) REFERENCES patients(id),
  FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

CREATE TABLE reports (
  id INT AUTO_INCREMENT PRIMARY KEY,
  patient_id INT,
  report_name VARCHAR(100),
  report_date DATE,
  description TEXT,
  FOREIGN KEY (patient_id) REFERENCES patients(id)
);

INSERT INTO doctors(name, specialization, email, password) VALUES
('Dr. Ramesh', 'Cardiology', 'ramesh@telemed.com', 'pass123'),
('Dr. Priya', 'Dermatology', 'priya@telemed.com', 'pass123'),
('Dr. Kiran', 'Neurology', 'kiran@telemed.com', 'pass123');
