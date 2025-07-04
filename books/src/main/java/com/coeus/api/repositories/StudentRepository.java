package com.coeus.api.repositories;

import com.coeus.api.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Modifying
    @Query("UPDATE Student s SET s.enabled = false WHERE s.id =:id")
    void disableStudent(@Param("id") Long id);
}