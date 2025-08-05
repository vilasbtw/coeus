CREATE TABLE IF NOT EXISTS loan (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    book_id INT NOT NULL,
    employee_id INT NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(50) NOT NULL DEFAULT 'ON_GOING',
    notes TEXT,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (book_id) REFERENCES book(id),
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);