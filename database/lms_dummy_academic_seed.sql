CREATE DATABASE IF NOT EXISTS lms_db;
USE lms_db;

CREATE TABLE IF NOT EXISTS department (
    department_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(10) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20),
    profile_picture VARCHAR(255),
    role ENUM('Student','Lecturer','Dean','Tech_Officer','Admin') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS students (
    user_id VARCHAR(10) PRIMARY KEY,
    reg_no VARCHAR(20) UNIQUE NOT NULL,
    batch VARCHAR(20) NOT NULL,
    academic_level INT,
    department_id VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

CREATE TABLE IF NOT EXISTS lecturers (
    user_id VARCHAR(10) PRIMARY KEY,
    specialization VARCHAR(100),
    designation VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tech_officers (
    user_id VARCHAR(10) PRIMARY KEY,
    department_id VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

CREATE TABLE IF NOT EXISTS course (
    course_id VARCHAR(10) PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    course_credit INT NOT NULL,
    academic_level INT,
    semester ENUM('1','2') NOT NULL,
    department_id VARCHAR(10) NOT NULL,
    FOREIGN KEY (department_id) REFERENCES department(department_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course_registration (
    student_id VARCHAR(10),
    course_id VARCHAR(10),
    academic_year INT NOT NULL,
    semester ENUM('1','2') NOT NULL,
    registration_type ENUM('Proper','Repeat','Suspend') DEFAULT 'Proper',
    PRIMARY KEY (student_id, course_id, academic_year, semester),
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assessment_type (
    assessment_type_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(10) NOT NULL,
    name VARCHAR(50) NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    component ENUM('CA','Final') NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student_marks (
    mark_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(10) NOT NULL,
    assessment_type_id INT NOT NULL,
    marks DECIMAL(5,2),
    UNIQUE (student_id, assessment_type_id),
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assessment_type_id) REFERENCES assessment_type(assessment_type_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS session (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(10) NOT NULL,
    session_date DATE NOT NULL,
    session_hours DECIMAL(4,2) NOT NULL,
    type ENUM('Theory','Practical') NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(10) NOT NULL,
    session_id INT NOT NULL,
    status ENUM('Present','Absent') NOT NULL,
    hours_attended DECIMAL(4,2) DEFAULT 0,
    UNIQUE(student_id, session_id),
    FOREIGN KEY(student_id) REFERENCES students(user_id) ON DELETE CASCADE,
    FOREIGN KEY(session_id) REFERENCES session(session_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course_result (
    result_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(10) NOT NULL,
    course_id VARCHAR(10) NOT NULL,
    academic_year INT NOT NULL,
    academic_level INT NOT NULL,
    semester ENUM('1','2') NOT NULL,
    total_marks DECIMAL(5,2),
    grade VARCHAR(5),
    UNIQUE (student_id, course_id, academic_year, semester),
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS semester_result (
    semester_result_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(10) NOT NULL,
    academic_year INT NOT NULL,
    academic_level INT,
    semester ENUM('1','2'),
    total_credits INT,
    sgpa DECIMAL(3,2),
    cgpa DECIMAL(3,2),
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS medical (
    medical_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(10) NOT NULL,
    course_id VARCHAR(10),
    exam_type ENUM('Mid','Final','Attendance') NOT NULL,
    date_submitted DATE NOT NULL,
    medical_copy VARCHAR(255),
    status ENUM('Pending','Approved','Rejected') DEFAULT 'Pending',
    FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS timetable (
    timetable_id INT AUTO_INCREMENT PRIMARY KEY,
    department_id VARCHAR(10) NOT NULL,
    academic_level INT NOT NULL,
    semester ENUM('1','2') NOT NULL,
    title VARCHAR(100),
    pdf_file_path VARCHAR(255) NOT NULL,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (department_id, academic_level, semester),
    FOREIGN KEY (department_id) REFERENCES department(department_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notice (
    notice_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    pdf_file_path VARCHAR(255),
    created_by VARCHAR(10),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Dummy seed for GPA + undergraduate view + full academic report demo.
-- This file is additive and does not modify schema definitions.

INSERT IGNORE INTO department (department_id, name)
VALUES
    ('D01', 'Information Technology'),
    ('D02', 'Engineering Technology'),
    ('D03', 'Bio Systems Technology');

INSERT IGNORE INTO users (user_id, username, email, password, contact_number, profile_picture, role)
VALUES
    ('U1001', 'student_one', 'student.one@example.com', '1234', '0771000001', NULL, 'Student'),
    ('U1002', 'student_two', 'student.two@example.com', '1234', '0771000002', NULL, 'Student'),
    ('U1003', 'student_three', 'student.three@example.com', '1234', '0771000003', NULL, 'Student'),
    ('U1004', 'student_four', 'student.four@example.com', '1234', '0771000004', NULL, 'Student'),
    ('U2001', 'lecturer_one', 'lecturer.one@example.com', '1234', '0772000001', NULL, 'Lecturer'),
    ('U2002', 'lecturer_two', 'lecturer.two@example.com', '1234', '0772000002', NULL, 'Lecturer'),
    ('U3001', 'admin_one', 'admin.one@example.com', '1234', '0773000001', NULL, 'Admin'),
    ('U3002', 'dean_one', 'dean.one@example.com', '1234', '0773000002', NULL, 'Dean'),
    ('U4001', 'tech_one', 'tech.one@example.com', '1234', '0774000001', NULL, 'Tech_Officer');

INSERT IGNORE INTO students (user_id, reg_no, batch, academic_level, department_id)
VALUES
    ('U1001', 'TG/2022/001', '22.1', 2, 'D01'),
    ('U1002', 'TG/2022/002', '22.1', 2, 'D01'),
    ('U1003', 'TG/2022/003', '22.1', 2, 'D02'),
    ('U1004', 'TG/2023/004', '23.1', 1, 'D03');

INSERT IGNORE INTO lecturers (user_id, specialization, designation)
VALUES
    ('U2001', 'Software Engineering', 'Senior Lecturer'),
    ('U2002', 'Database Systems', 'Lecturer');

INSERT IGNORE INTO tech_officers (user_id, department_id)
VALUES
    ('U4001', 'D01');

INSERT IGNORE INTO course (course_id, course_code, name, course_credit, academic_level, semester, department_id)
VALUES
    ('C201', 'ICT2112', 'Object Oriented Programming', 3, 2, '1', 'D01'),
    ('C202', 'ICT2122', 'Database Systems', 3, 2, '1', 'D01'),
    ('C203', 'ICT2132', 'Web Technologies', 3, 2, '1', 'D01'),
    ('C204', 'ET2112', 'Engineering Mathematics', 3, 2, '1', 'D02'),
    ('C205', 'ET2122', 'Electrical Fundamentals', 2, 2, '1', 'D02'),
    ('C101', 'BST1112', 'Introduction to BST', 2, 1, '1', 'D03');

INSERT IGNORE INTO course_registration (student_id, course_id, academic_year, semester, registration_type)
VALUES
    ('U1001', 'C201', 2026, '1', 'Proper'),
    ('U1001', 'C202', 2026, '1', 'Proper'),
    ('U1001', 'C203', 2026, '1', 'Proper'),
    ('U1002', 'C201', 2026, '1', 'Proper'),
    ('U1002', 'C202', 2026, '1', 'Proper'),
    ('U1002', 'C203', 2026, '1', 'Proper'),
    ('U1003', 'C204', 2026, '1', 'Proper'),
    ('U1003', 'C205', 2026, '1', 'Proper'),
    ('U1004', 'C101', 2026, '1', 'Proper');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C201', 'Quiz1', 20.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C201' AND name = 'Quiz1');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C201', 'Mid', 30.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C201' AND name = 'Mid');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C201', 'Final', 50.00, 'Final' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C201' AND name = 'Final');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C202', 'Quiz1', 20.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C202' AND name = 'Quiz1');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C202', 'Mid', 30.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C202' AND name = 'Mid');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C202', 'Final', 50.00, 'Final' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C202' AND name = 'Final');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C203', 'Quiz1', 20.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C203' AND name = 'Quiz1');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C203', 'Mid', 30.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C203' AND name = 'Mid');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C203', 'Final', 50.00, 'Final' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C203' AND name = 'Final');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C204', 'Quiz1', 20.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C204' AND name = 'Quiz1');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C204', 'Mid', 30.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C204' AND name = 'Mid');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C204', 'Final', 50.00, 'Final' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C204' AND name = 'Final');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C205', 'Quiz1', 20.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C205' AND name = 'Quiz1');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C205', 'Mid', 30.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C205' AND name = 'Mid');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C205', 'Final', 50.00, 'Final' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C205' AND name = 'Final');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C101', 'Quiz1', 40.00, 'CA' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C101' AND name = 'Quiz1');

INSERT INTO assessment_type (course_id, name, weight, component)
SELECT 'C101', 'Final', 60.00, 'Final' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM assessment_type WHERE course_id = 'C101' AND name = 'Final');

INSERT IGNORE INTO student_marks (student_id, assessment_type_id, marks)
SELECT 'U1001', at.assessment_type_id,
       CASE
           WHEN at.course_id = 'C201' AND at.name = 'Quiz1' THEN 82
           WHEN at.course_id = 'C201' AND at.name = 'Mid' THEN 76
           WHEN at.course_id = 'C201' AND at.name = 'Final' THEN 71
           WHEN at.course_id = 'C202' AND at.name = 'Quiz1' THEN 88
           WHEN at.course_id = 'C202' AND at.name = 'Mid' THEN 79
           WHEN at.course_id = 'C202' AND at.name = 'Final' THEN 74
           WHEN at.course_id = 'C203' AND at.name = 'Quiz1' THEN 91
           WHEN at.course_id = 'C203' AND at.name = 'Mid' THEN 83
           WHEN at.course_id = 'C203' AND at.name = 'Final' THEN 78
           ELSE 0
       END
FROM assessment_type at
WHERE at.course_id IN ('C201','C202','C203');

INSERT IGNORE INTO student_marks (student_id, assessment_type_id, marks)
SELECT 'U1003', at.assessment_type_id,
       CASE
           WHEN at.course_id = 'C204' AND at.name = 'Quiz1' THEN 84
           WHEN at.course_id = 'C204' AND at.name = 'Mid' THEN 72
           WHEN at.course_id = 'C204' AND at.name = 'Final' THEN 76
           WHEN at.course_id = 'C205' AND at.name = 'Quiz1' THEN 81
           WHEN at.course_id = 'C205' AND at.name = 'Mid' THEN 73
           WHEN at.course_id = 'C205' AND at.name = 'Final' THEN 75
           ELSE 0
       END
FROM assessment_type at
WHERE at.course_id IN ('C204','C205');

INSERT IGNORE INTO student_marks (student_id, assessment_type_id, marks)
SELECT 'U1004', at.assessment_type_id,
       CASE
           WHEN at.course_id = 'C101' AND at.name = 'Quiz1' THEN 79
           WHEN at.course_id = 'C101' AND at.name = 'Final' THEN 74
           ELSE 0
       END
FROM assessment_type at
WHERE at.course_id IN ('C101');

INSERT IGNORE INTO student_marks (student_id, assessment_type_id, marks)
SELECT 'U1002', at.assessment_type_id,
       CASE
           WHEN at.course_id = 'C201' AND at.name = 'Quiz1' THEN 72
           WHEN at.course_id = 'C201' AND at.name = 'Mid' THEN 68
           WHEN at.course_id = 'C201' AND at.name = 'Final' THEN 65
           WHEN at.course_id = 'C202' AND at.name = 'Quiz1' THEN 77
           WHEN at.course_id = 'C202' AND at.name = 'Mid' THEN 70
           WHEN at.course_id = 'C202' AND at.name = 'Final' THEN 67
           WHEN at.course_id = 'C203' AND at.name = 'Quiz1' THEN 80
           WHEN at.course_id = 'C203' AND at.name = 'Mid' THEN 74
           WHEN at.course_id = 'C203' AND at.name = 'Final' THEN 69
           ELSE 0
       END
FROM assessment_type at
WHERE at.course_id IN ('C201','C202','C203');

INSERT INTO session (course_id, session_date, session_hours, type)
SELECT 'C201', '2026-03-01', 2.00, 'Theory' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM session WHERE course_id = 'C201' AND session_date = '2026-03-01' AND type = 'Theory');

INSERT INTO session (course_id, session_date, session_hours, type)
SELECT 'C202', '2026-03-02', 2.00, 'Theory' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM session WHERE course_id = 'C202' AND session_date = '2026-03-02' AND type = 'Theory');

INSERT INTO session (course_id, session_date, session_hours, type)
SELECT 'C203', '2026-03-03', 2.00, 'Practical' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM session WHERE course_id = 'C203' AND session_date = '2026-03-03' AND type = 'Practical');

INSERT INTO session (course_id, session_date, session_hours, type)
SELECT 'C204', '2026-03-04', 2.00, 'Theory' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM session WHERE course_id = 'C204' AND session_date = '2026-03-04' AND type = 'Theory');

INSERT INTO session (course_id, session_date, session_hours, type)
SELECT 'C205', '2026-03-05', 2.00, 'Practical' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM session WHERE course_id = 'C205' AND session_date = '2026-03-05' AND type = 'Practical');

INSERT INTO session (course_id, session_date, session_hours, type)
SELECT 'C101', '2026-03-06', 2.00, 'Theory' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM session WHERE course_id = 'C101' AND session_date = '2026-03-06' AND type = 'Theory');

INSERT IGNORE INTO attendance (student_id, session_id, status, hours_attended)
SELECT 'U1001', s.session_id, 'Present', 2.00
FROM session s
WHERE s.course_id IN ('C201','C202','C203') AND s.session_date IN ('2026-03-01','2026-03-02','2026-03-03');

INSERT IGNORE INTO attendance (student_id, session_id, status, hours_attended)
SELECT 'U1003', s.session_id,
       CASE WHEN s.course_id = 'C205' THEN 'Absent' ELSE 'Present' END,
       CASE WHEN s.course_id = 'C205' THEN 0.00 ELSE 2.00 END
FROM session s
WHERE s.course_id IN ('C204','C205') AND s.session_date IN ('2026-03-04','2026-03-05');

INSERT IGNORE INTO attendance (student_id, session_id, status, hours_attended)
SELECT 'U1004', s.session_id, 'Present', 2.00
FROM session s
WHERE s.course_id IN ('C101') AND s.session_date IN ('2026-03-06');

INSERT IGNORE INTO attendance (student_id, session_id, status, hours_attended)
SELECT 'U1002', s.session_id,
       CASE WHEN s.course_id = 'C202' THEN 'Absent' ELSE 'Present' END,
       CASE WHEN s.course_id = 'C202' THEN 0.00 ELSE 2.00 END
FROM session s
WHERE s.course_id IN ('C201','C202','C203') AND s.session_date IN ('2026-03-01','2026-03-02','2026-03-03');

INSERT IGNORE INTO course_result (student_id, course_id, academic_year, academic_level, semester, total_marks, grade)
VALUES
    ('U1001', 'C201', 2026, 2, '1', 75.00, 'B+'),
    ('U1001', 'C202', 2026, 2, '1', 78.00, 'A-'),
    ('U1001', 'C203', 2026, 2, '1', 82.00, 'A-'),
    ('U1002', 'C201', 2026, 2, '1', 67.00, 'B-'),
    ('U1002', 'C202', 2026, 2, '1', 69.00, 'B-'),
    ('U1002', 'C203', 2026, 2, '1', 72.00, 'B'),
    ('U1003', 'C204', 2026, 2, '1', 76.00, 'B+'),
    ('U1003', 'C205', 2026, 2, '1', 74.00, 'B'),
    ('U1004', 'C101', 2026, 1, '1', 76.00, 'B+');

INSERT INTO semester_result (student_id, academic_year, academic_level, semester, total_credits, sgpa, cgpa)
SELECT 'U1001', 2026, 2, '1', 9, 3.67, 3.67 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM semester_result WHERE student_id = 'U1001' AND academic_year = 2026 AND semester = '1'
);

INSERT INTO semester_result (student_id, academic_year, academic_level, semester, total_credits, sgpa, cgpa)
SELECT 'U1002', 2026, 2, '1', 9, 2.90, 2.90 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM semester_result WHERE student_id = 'U1002' AND academic_year = 2026 AND semester = '1'
);

INSERT INTO semester_result (student_id, academic_year, academic_level, semester, total_credits, sgpa, cgpa)
SELECT 'U1003', 2026, 2, '1', 5, 3.15, 3.15 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM semester_result WHERE student_id = 'U1003' AND academic_year = 2026 AND semester = '1'
);

INSERT INTO semester_result (student_id, academic_year, academic_level, semester, total_credits, sgpa, cgpa)
SELECT 'U1004', 2026, 1, '1', 2, 3.30, 3.30 FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM semester_result WHERE student_id = 'U1004' AND academic_year = 2026 AND semester = '1'
);

INSERT INTO medical (student_id, course_id, exam_type, date_submitted, medical_copy, status)
SELECT 'U1002', 'C202', 'Attendance', '2026-03-04', 'dummy-medical.pdf', 'Approved' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM medical
    WHERE student_id = 'U1002' AND course_id = 'C202' AND exam_type = 'Attendance' AND date_submitted = '2026-03-04'
);

INSERT INTO medical (student_id, course_id, exam_type, date_submitted, medical_copy, status)
SELECT 'U1003', 'C205', 'Final', '2026-03-10', 'dummy-medical-2.pdf', 'Pending' FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM medical
    WHERE student_id = 'U1003' AND course_id = 'C205' AND exam_type = 'Final' AND date_submitted = '2026-03-10'
);

INSERT IGNORE INTO timetable (department_id, academic_level, semester, title, pdf_file_path)
VALUES ('D01', 2, '1', 'Level 2 Semester 1 Timetable', 'files/timetable-level2-sem1.pdf');

INSERT IGNORE INTO timetable (department_id, academic_level, semester, title, pdf_file_path)
VALUES ('D02', 2, '1', 'ET Level 2 Semester 1 Timetable', 'files/timetable-et-level2-sem1.pdf');

INSERT IGNORE INTO timetable (department_id, academic_level, semester, title, pdf_file_path)
VALUES ('D03', 1, '1', 'BST Level 1 Semester 1 Timetable', 'files/timetable-bst-level1-sem1.pdf');

INSERT INTO notice (title, description, pdf_file_path, created_by)
SELECT 'Mid Semester Exam Announcement', 'Mid semester exam schedule has been published.', 'files/notice-mid-sem.pdf', NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM notice WHERE title = 'Mid Semester Exam Announcement');

INSERT INTO notice (title, description, pdf_file_path, created_by)
SELECT 'Project Demo Guidelines', 'Final project demo guideline document uploaded.', 'files/notice-demo-guidelines.pdf', NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM notice WHERE title = 'Project Demo Guidelines');

INSERT INTO notice (title, description, pdf_file_path, created_by)
SELECT 'Course Registration Open', 'Registration portal is open for semester 1.', 'files/notice-registration.pdf', NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM notice WHERE title = 'Course Registration Open');

INSERT INTO notice (title, description, pdf_file_path, created_by)
SELECT 'Attendance Policy Update', 'Minimum 80% attendance is required for exam eligibility.', 'files/notice-attendance-policy.pdf', NULL FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM notice WHERE title = 'Attendance Policy Update');
