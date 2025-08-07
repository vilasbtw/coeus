CREATE TABLE warning (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    loan_id INT,
    employee_id INT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    details TEXT,
    status VARCHAR(20) NOT NULL,
    issued_at DATETIME NOT NULL,
    resolved_at DATETIME,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (loan_id) REFERENCES loan(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    UNIQUE KEY uq_warning_loan (loan_id)
);