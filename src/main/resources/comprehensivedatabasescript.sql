-- Drop existing tables if they exist
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS tithe_and_offering;
DROP TABLE IF EXISTS payment_method;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS family_relations;
DROP TABLE IF EXISTS fund_type;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS volunteer_activities;
DROP TABLE IF EXISTS registrations;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS communications;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS church_information;
DROP TABLE IF EXISTS address;

-- Create Address Table
CREATE TABLE address (
  id_addr BIGINT(20) NOT NULL AUTO_INCREMENT,
  apt_no VARCHAR(255) DEFAULT NULL,
  city VARCHAR(255) DEFAULT NULL,
  state VARCHAR(255) DEFAULT NULL,
  street VARCHAR(255) DEFAULT NULL,
  zip VARCHAR(255) DEFAULT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (id_addr)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Users Table
CREATE TABLE users (
  user_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Roles Table
CREATE TABLE roles (
  role_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  role_name VARCHAR(255) NOT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create User Roles Table
CREATE TABLE user_roles (
  user_id BIGINT(20) NOT NULL,
  role_id BIGINT(20) NOT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Members Table with CHAR(8) member_id
CREATE TABLE members (
  member_id CHAR(8) NOT NULL,
  user_id BIGINT(20) DEFAULT NULL,
  email_id VARCHAR(255) DEFAULT NULL,
  first_name VARCHAR(255) NOT NULL,
  gender VARCHAR(255) DEFAULT NULL,
  last_name VARCHAR(255) NOT NULL,
  marital_status VARCHAR(255) DEFAULT NULL,
  member_dob DATE DEFAULT NULL,
  member_exptn DATE DEFAULT NULL,
  middle_name VARCHAR(255) DEFAULT NULL,
  phone VARCHAR(255) DEFAULT NULL,
  id_addr BIGINT(20) DEFAULT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (member_id),
  KEY FK_member_address (id_addr),
  KEY FK_member_user (user_id),
  FOREIGN KEY (id_addr) REFERENCES address(id_addr) ON DELETE SET NULL,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Church Information Table
CREATE TABLE church_information (
  church_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  chur_denomination VARCHAR(255) NOT NULL,
  chur_email VARCHAR(255) NOT NULL,
  chur_name VARCHAR(255) NOT NULL,
  chur_phone VARCHAR(255) NOT NULL,
  chur_website VARCHAR(255) NOT NULL,
  church_exptn DATE DEFAULT NULL,
  id_addr BIGINT(20) DEFAULT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (church_id),
  KEY FK_church_address (id_addr),
  FOREIGN KEY (id_addr) REFERENCES address(id_addr) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Fund Type Table
CREATE TABLE fund_type (
  fund_typeid BIGINT(20) NOT NULL AUTO_INCREMENT,
  fund_type_name VARCHAR(255) NOT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (fund_typeid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Payment Method Table
CREATE TABLE payment_method (
  payment_method_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  payment_method_name VARCHAR(255) NOT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (payment_method_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Tithe and Offering Table
CREATE TABLE tithe_and_offering (
  tithe_offering_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  amount_contributed DOUBLE DEFAULT NULL,
  contributed_date DATE NOT NULL,
  fund_note VARCHAR(255) DEFAULT NULL,
  fund_type_id BIGINT(20) DEFAULT NULL,
  member_id CHAR(8) DEFAULT NULL,
  payment_method_id BIGINT(20) DEFAULT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (tithe_offering_id),
  KEY FK_fund_type_id (fund_type_id),
  KEY FK_member_id (member_id),
  KEY FK_payment_method_id (payment_method_id),
  FOREIGN KEY (fund_type_id) REFERENCES fund_type(fund_typeid) ON DELETE SET NULL,
  FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE SET NULL,
  FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Logs Table
CREATE TABLE logs (
  log_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  user_id BIGINT(20) NOT NULL,
  action VARCHAR(255) NOT NULL,
  log_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (log_id),
  KEY FK_user_id (user_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Events Table
CREATE TABLE events (
  event_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  event_name VARCHAR(100) NOT NULL,
  event_date DATETIME NOT NULL,
  event_location VARCHAR(255) NOT NULL,
  event_description TEXT,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (event_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Registrations Table
CREATE TABLE registrations (
  registration_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  event_id BIGINT(20) NOT NULL,
  member_id CHAR(8) NOT NULL,
  registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (registration_id),
  KEY FK_event_id (event_id),
  KEY FK_member_id (member_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
  FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Volunteer Activities Table
CREATE TABLE volunteer_activities (
  activity_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  event_id BIGINT(20) NOT NULL,
  activity_name VARCHAR(100) NOT NULL,
  activity_date DATETIME NOT NULL,
  volunteer_id CHAR(8),
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (activity_id),
  KEY FK_event_id (event_id),
  KEY FK_volunteer_id (volunteer_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
  FOREIGN KEY (volunteer_id) REFERENCES members(member_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Budgets Table
CREATE TABLE budgets (
  budget_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  year YEAR NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  allocated_amount DECIMAL(10,2),
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (budget_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Communications Table
CREATE TABLE communications (
  communication_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  sender_id BIGINT(20),
  receiver_id BIGINT(20),
  message TEXT NOT NULL,
  sent_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (communication_id),
  KEY FK_sender_id (sender_id),
  KEY FK_receiver_id (receiver_id),
  FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE SET NULL,
  FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Reports Table
CREATE TABLE reports (
  report_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  report_type VARCHAR(20) NOT NULL,
  generated_by BIGINT(20),
  generated_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  content TEXT NOT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (report_id),
  KEY FK_generated_by (generated_by),
  FOREIGN KEY (generated_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Family Relations Table
CREATE TABLE family_relations (
  relation_id BIGINT(20) NOT NULL AUTO_INCREMENT,
  member_id CHAR(8) NOT NULL,
  related_member_id CHAR(8) NOT NULL,
  relationship VARCHAR(20) NOT NULL,
  id_user_create varchar(255) DEFAULT NULL,
  dttm_create datetime(6) DEFAULT NULL,
  id_user_lst_updt varchar(255) DEFAULT NULL,
  dttm_lst_updt datetime(6) DEFAULT NULL,
  PRIMARY KEY (relation_id),
  KEY FK_member_id (member_id),
  KEY FK_related_member_id (related_member_id),
  FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
  FOREIGN KEY (related_member_id) REFERENCES members(member_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert Data into Address Table
INSERT INTO address (apt_no, city, state, street, zip) VALUES
('101', 'Springfield', 'IL', 'Main St', '62701'),
('202', 'Shelbyville', 'IL', 'Elm St', '62702'),
('303', 'Capital City', 'IL', 'Oak St', '62703');

-- Insert Data into Users Table
INSERT INTO users (username, password, email) VALUES
('john_doe', 'hashed_password1', 'john@example.com'),
('jane_smith', 'hashed_password2', 'jane@example.com');

-- Insert Data into Roles Table
INSERT INTO roles (role_name) VALUES
('Admin'),
('Member'),
('Volunteer');

-- Insert Data into User Roles Table
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),  -- John Doe is an Admin
(1, 2),  -- John Doe is also a Member
(2, 2),  -- Jane Smith is a Member
(2, 3);  -- Jane Smith is also a Volunteer

-- Insert Data into Members Table with 8-digit member_id
INSERT INTO members (member_id, user_id, email_id, first_name, gender, last_name, marital_status, member_dob, member_exptn, middle_name, phone, id_addr) VALUES
('00000001', 1, 'john@example.com', 'John', 'Male', 'Doe', 'Single', '1980-01-01', '2023-12-31', 'A', '123-456-7890', 1),
('00000002', 2, 'jane@example.com', 'Jane', 'Female', 'Smith', 'Married', '1985-02-02', '2023-12-31', 'B', '987-654-3210', 2);

-- Insert Data into Church Information Table
INSERT INTO church_information (chur_denomination, chur_email, chur_name, chur_phone, chur_website, church_exptn, id_addr) VALUES
('Non-Denominational', 'contact@church1.com', 'First Church', '123-456-7890', 'www.church1.com', '2023-12-31', 1),
('Baptist', 'info@church2.com', 'Second Church', '987-654-3210', 'www.church2.com', '2024-01-31', 2);

-- Insert Data into Fund Type Table
INSERT INTO fund_type (fund_type_name) VALUES
('Tithes'),
('Offerings'),
('Missions');

-- Insert Data into Payment Method Table
INSERT INTO payment_method (payment_method_name) VALUES
('Cash'),
('Credit Card'),
('Check');

-- Insert Data into Tithe and Offering Table
INSERT INTO tithe_and_offering (amount_contributed, contributed_date, fund_note, fund_type_id, member_id, payment_method_id) VALUES
(100.00, '2023-06-15', 'Monthly Tithe', 1, '00000001', 2),  -- John Doe's contribution
(50.00, '2023-06-16', 'Weekly Offering', 2, '00000002', 1);  -- Jane Smith's contribution

-- Insert Data into Logs Table
INSERT INTO logs (user_id, action, log_date) VALUES
(1, 'User logged in', NOW()),
(2, 'User updated profile', NOW());

-- Insert Data into Events Table
INSERT INTO events (event_name, event_date, event_location, event_description) VALUES
('Sunday Service', '2023-06-15 10:00:00', 'Main Hall', 'Weekly Sunday Service'),
('Youth Meeting', '2023-06-16 18:00:00', 'Youth Room', 'Monthly Youth Gathering');

-- Insert Data into Registrations Table
INSERT INTO registrations (event_id, member_id, registration_date) VALUES
(1, '00000001', NOW()),  -- John Doe registered for Sunday Service
(2, '00000002', NOW());  -- Jane Smith registered for Youth Meeting

-- Insert Data into Volunteer Activities Table
INSERT INTO volunteer_activities (event_id, activity_name, activity_date, volunteer_id) VALUES
(1, 'Setup Chairs', '2023-06-15 08:00:00', '00000002'),  -- Jane Smith volunteering
(2, 'Organize Snacks', '2023-06-16 17:00:00', '00000001');  -- John Doe volunteering

-- Insert Data into Budgets Table
INSERT INTO budgets (year, amount, allocated_amount) VALUES
(2023, 50000.00, 20000.00),
(2024, 60000.00, 30000.00);

-- Insert Data into Communications Table
INSERT INTO communications (sender_id, receiver_id, message, sent_date) VALUES
(1, 2, 'Hello, how are you?', NOW()),
(2, 1, 'I am fine, thank you!', NOW());

-- Insert Data into Reports Table
INSERT INTO reports (report_type, generated_by, generated_date, content) VALUES
('Financial', 1, NOW(), 'Financial report content...'),
('Attendance', 2, NOW(), 'Attendance report content...');

-- Insert Data into Family Relations Table
INSERT INTO family_relations (member_id, related_member_id, relationship) VALUES
('00000001', '00000002', 'Spouse'),  -- John Doe and Jane Smith are spouses
('00000002', '00000001', 'Spouse');  -- Reciprocal relationship
