// API Base URL (change for production)
const API_URL = '/api';

// Current user state
let currentUser = null;
let userType = null; // 'patient', 'doctor', 'admin'

// Navigation functions
function showMain() {
    hideAll();
    document.getElementById('mainMenu').classList.remove('hidden');
}

function showRegister() {
    hideAll();
    document.getElementById('registerForm').classList.remove('hidden');
}

function showPatientLogin() {
    hideAll();
    document.getElementById('patientLoginForm').classList.remove('hidden');
}

function showDoctorLogin() {
    hideAll();
    document.getElementById('doctorLoginForm').classList.remove('hidden');
}

function showAdminLogin() {
    hideAll();
    document.getElementById('adminLoginForm').classList.remove('hidden');
}

function hideAll() {
    const sections = ['mainMenu', 'registerForm', 'patientLoginForm', 'doctorLoginForm', 
                     'adminLoginForm', 'patientDashboard', 'doctorDashboard', 'adminDashboard'];
    sections.forEach(id => {
        document.getElementById(id).classList.add('hidden');
    });
}

function logout() {
    currentUser = null;
    userType = null;
    showMain();
}

// Patient Registration
document.getElementById('patientRegForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const data = {
        name: document.getElementById('regName').value,
        email: document.getElementById('regEmail').value,
        password: document.getElementById('regPassword').value,
        phone: document.getElementById('regPhone').value,
        age: parseInt(document.getElementById('regAge').value),
        gender: document.getElementById('regGender').value
    };

    try {
        const response = await fetch(`${API_URL}/patients/register`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        const result = await response.json();
        showMessage('regMessage', result.message, response.ok);
        if (response.ok) {
            setTimeout(() => showPatientLogin(), 2000);
        }
    } catch (error) {
        showMessage('regMessage', 'Error: ' + error.message, false);
    }
});

// Patient Login
document.getElementById('patLoginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        email: document.getElementById('patEmail').value,
        password: document.getElementById('patPassword').value
    };

    try {
        const response = await fetch(`${API_URL}/patients/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        const result = await response.json();
        
        if (response.ok && result.success) {
            currentUser = result.patient;
            userType = 'patient';
            document.getElementById('patientName').textContent = currentUser.name;
            hideAll();
            document.getElementById('patientDashboard').classList.remove('hidden');
        } else {
            showMessage('patLoginMessage', result.message || 'Login failed', false);
        }
    } catch (error) {
        showMessage('patLoginMessage', 'Error: ' + error.message, false);
    }
});

// Doctor Login
document.getElementById('docLoginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        email: document.getElementById('docEmail').value,
        password: document.getElementById('docPassword').value
    };

    try {
        const response = await fetch(`${API_URL}/doctors/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        const result = await response.json();
        
        if (response.ok && result.success) {
            currentUser = result.doctor;
            userType = 'doctor';
            document.getElementById('doctorName').textContent = currentUser.name;
            hideAll();
            document.getElementById('doctorDashboard').classList.remove('hidden');
        } else {
            showMessage('docLoginMessage', result.message || 'Login failed', false);
        }
    } catch (error) {
        showMessage('docLoginMessage', 'Error: ' + error.message, false);
    }
});

// Admin Login
document.getElementById('adminLoginFormEl').addEventListener('submit', (e) => {
    e.preventDefault();
    const password = document.getElementById('adminPassword').value;
    
    if (password === 'admin123') {
        userType = 'admin';
        hideAll();
        document.getElementById('adminDashboard').classList.remove('hidden');
    } else {
        showMessage('adminLoginMessage', 'Invalid admin password', false);
    }
});

// View Doctors
async function viewDoctors() {
    try {
        const response = await fetch(`${API_URL}/doctors`);
        const doctors = await response.json();
        
        let html = '<h3>Available Doctors</h3>';
        doctors.forEach(doc => {
            html += `
                <div class="doctor-card">
                    <h3>Dr. ${doc.name}</h3>
                    <p><strong>Specialization:</strong> ${doc.specialization}</p>
                    <p><strong>ID:</strong> ${doc.id}</p>
                </div>
            `;
        });
        document.getElementById('patientContent').innerHTML = html;
    } catch (error) {
        document.getElementById('patientContent').innerHTML = `<p class="error">Error loading doctors</p>`;
    }
}

// Book Appointment
function showBookAppointment() {
    const html = `
        <h3>Book Appointment</h3>
        <form id="bookAppointmentForm">
            <input type="number" id="doctorId" placeholder="Doctor ID" required min="1">
            <input type="date" id="appointmentDate" required>
            <input type="time" id="appointmentTime" required>
            <button type="submit" class="btn btn-primary">Book Appointment</button>
        </form>
        <div id="bookMessage" class="message"></div>
    `;
    document.getElementById('patientContent').innerHTML = html;
    
    document.getElementById('bookAppointmentForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            patientId: currentUser.id,
            doctorId: parseInt(document.getElementById('doctorId').value),
            date: document.getElementById('appointmentDate').value,
            time: document.getElementById('appointmentTime').value
        };

        try {
            const response = await fetch(`${API_URL}/appointments`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            });
            const result = await response.json();
            showMessage('bookMessage', result.message, response.ok);
            if (response.ok) {
                setTimeout(() => viewMyAppointments(), 2000);
            }
        } catch (error) {
            showMessage('bookMessage', 'Error: ' + error.message, false);
        }
    });
}

// View My Appointments
async function viewMyAppointments() {
    try {
        const response = await fetch(`${API_URL}/appointments/patient/${currentUser.id}`);
        const appointments = await response.json();
        
        let html = '<h3>My Appointments</h3>';
        if (appointments.length === 0) {
            html += '<p>No appointments found.</p>';
        } else {
            appointments.forEach(apt => {
                html += `
                    <div class="appointment-card">
                        <p><strong>Doctor:</strong> ${apt.doctorName}</p>
                        <p><strong>Date:</strong> ${apt.date}</p>
                        <p><strong>Time:</strong> ${apt.time}</p>
                        <p><strong>Status:</strong> <span class="status-${apt.status.toLowerCase()}">${apt.status}</span></p>
                        ${apt.remarks ? `<p><strong>Remarks:</strong> ${apt.remarks}</p>` : ''}
                    </div>
                `;
            });
        }
        document.getElementById('patientContent').innerHTML = html;
    } catch (error) {
        document.getElementById('patientContent').innerHTML = `<p class="error">Error loading appointments</p>`;
    }
}

// Upload Report
function showUploadReport() {
    const html = `
        <h3>Upload Medical Report</h3>
        <form id="uploadReportForm">
            <input type="text" id="reportName" placeholder="Report Name" required>
            <input type="date" id="reportDate" required>
            <textarea id="reportDescription" placeholder="Description" rows="4" required></textarea>
            <button type="submit" class="btn btn-primary">Upload Report</button>
        </form>
        <div id="reportMessage" class="message"></div>
    `;
    document.getElementById('patientContent').innerHTML = html;
    
    document.getElementById('uploadReportForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            patientId: currentUser.id,
            name: document.getElementById('reportName').value,
            date: document.getElementById('reportDate').value,
            description: document.getElementById('reportDescription').value
        };

        try {
            const response = await fetch(`${API_URL}/reports`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            });
            const result = await response.json();
            showMessage('reportMessage', result.message, response.ok);
            if (response.ok) {
                setTimeout(() => viewMyReports(), 2000);
            }
        } catch (error) {
            showMessage('reportMessage', 'Error: ' + error.message, false);
        }
    });
}

// View My Reports
async function viewMyReports() {
    try {
        const response = await fetch(`${API_URL}/reports/patient/${currentUser.id}`);
        const reports = await response.json();
        
        let html = '<h3>My Medical Reports</h3>';
        if (reports.length === 0) {
            html += '<p>No reports found.</p>';
        } else {
            reports.forEach(rep => {
                html += `
                    <div class="report-card">
                        <h3>${rep.reportName}</h3>
                        <p><strong>Date:</strong> ${rep.reportDate}</p>
                        <p><strong>Description:</strong> ${rep.description}</p>
                    </div>
                `;
            });
        }
        document.getElementById('patientContent').innerHTML = html;
    } catch (error) {
        document.getElementById('patientContent').innerHTML = `<p class="error">Error loading reports</p>`;
    }
}

// Doctor: View All Appointments
async function viewAllAppointments() {
    try {
        const response = await fetch(`${API_URL}/appointments`);
        const appointments = await response.json();
        
        let html = '<h3>All Appointments</h3>';
        if (appointments.length === 0) {
            html += '<p>No appointments found.</p>';
        } else {
            html += '<table><tr><th>ID</th><th>Patient</th><th>Doctor</th><th>Date</th><th>Time</th><th>Status</th><th>Action</th></tr>';
            appointments.forEach(apt => {
                html += `
                    <tr>
                        <td>${apt.id}</td>
                        <td>${apt.patientName}</td>
                        <td>${apt.doctorName}</td>
                        <td>${apt.date}</td>
                        <td>${apt.time}</td>
                        <td class="status-${apt.status.toLowerCase()}">${apt.status}</td>
                        <td><button onclick="updateAppointmentStatus(${apt.id})" class="btn btn-primary">Update</button></td>
                    </tr>
                `;
            });
            html += '</table>';
        }
        document.getElementById('doctorContent').innerHTML = html;
    } catch (error) {
        document.getElementById('doctorContent').innerHTML = `<p class="error">Error loading appointments</p>`;
    }
}

// Update Appointment Status
function updateAppointmentStatus(appointmentId) {
    const status = prompt('Enter status (Confirmed/Cancelled):');
    const remarks = prompt('Enter remarks (optional):') || '';
    
    if (status) {
        fetch(`${API_URL}/appointments/${appointmentId}/status`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ status, remarks })
        })
        .then(response => response.json())
        .then(result => {
            alert(result.message);
            viewAllAppointments();
        })
        .catch(error => alert('Error updating status'));
    }
}

// Admin: View Doctors
async function adminViewDoctors() {
    try {
        const response = await fetch(`${API_URL}/doctors`);
        const doctors = await response.json();
        
        let html = '<h3>All Doctors</h3>';
        html += '<table><tr><th>ID</th><th>Name</th><th>Specialization</th><th>Email</th></tr>';
        doctors.forEach(doc => {
            html += `
                <tr>
                    <td>${doc.id}</td>
                    <td>Dr. ${doc.name}</td>
                    <td>${doc.specialization}</td>
                    <td>${doc.email}</td>
                </tr>
            `;
        });
        html += '</table>';
        document.getElementById('adminContent').innerHTML = html;
    } catch (error) {
        document.getElementById('adminContent').innerHTML = `<p class="error">Error loading doctors</p>`;
    }
}

// Admin: Add Doctor
function showAddDoctor() {
    const html = `
        <h3>Add New Doctor</h3>
        <form id="addDoctorForm">
            <input type="text" id="docName" placeholder="Doctor Name" required>
            <input type="text" id="docSpec" placeholder="Specialization" required>
            <input type="email" id="docEmailAdd" placeholder="Email" required>
            <input type="password" id="docPasswordAdd" placeholder="Password" required>
            <button type="submit" class="btn btn-primary">Add Doctor</button>
        </form>
        <div id="addDocMessage" class="message"></div>
    `;
    document.getElementById('adminContent').innerHTML = html;
    
    document.getElementById('addDoctorForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            name: document.getElementById('docName').value,
            specialization: document.getElementById('docSpec').value,
            email: document.getElementById('docEmailAdd').value,
            password: document.getElementById('docPasswordAdd').value
        };

        try {
            const response = await fetch(`${API_URL}/doctors`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify(data)
            });
            const result = await response.json();
            showMessage('addDocMessage', result.message, response.ok);
            if (response.ok) {
                setTimeout(() => adminViewDoctors(), 2000);
            }
        } catch (error) {
            showMessage('addDocMessage', 'Error: ' + error.message, false);
        }
    });
}

// Admin: View All Appointments
async function adminViewAppointments() {
    try {
        const response = await fetch(`${API_URL}/appointments`);
        const appointments = await response.json();
        
        let html = '<h3>All Appointments</h3>';
        if (appointments.length === 0) {
            html += '<p>No appointments found.</p>';
        } else {
            html += '<table><tr><th>ID</th><th>Patient</th><th>Doctor</th><th>Date</th><th>Time</th><th>Status</th></tr>';
            appointments.forEach(apt => {
                html += `
                    <tr>
                        <td>${apt.id}</td>
                        <td>${apt.patientName}</td>
                        <td>${apt.doctorName}</td>
                        <td>${apt.date}</td>
                        <td>${apt.time}</td>
                        <td class="status-${apt.status.toLowerCase()}">${apt.status}</td>
                    </tr>
                `;
            });
            html += '</table>';
        }
        document.getElementById('adminContent').innerHTML = html;
    } catch (error) {
        document.getElementById('adminContent').innerHTML = `<p class="error">Error loading appointments</p>`;
    }
}

// Helper function to show messages
function showMessage(elementId, message, success) {
    const messageEl = document.getElementById(elementId);
    messageEl.textContent = message;
    messageEl.className = 'message ' + (success ? 'success' : 'error');
    messageEl.style.display = 'block';
}

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    showMain();
});
