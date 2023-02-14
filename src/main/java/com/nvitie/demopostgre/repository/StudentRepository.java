package com.nvitie.demopostgre.repository;

import com.nvitie.demopostgre.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where s.email = ?1")
    Optional<Student> findByEmail(String email);

    @Query("select s from Student s where s.phone = ?1")
    Optional<Student> findByPhone(String phone);
    @Query("" +
            "SELECT CASE WHEN COUNT(s) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Student s " +
            "WHERE s.email = ?1"
    )
    Boolean checkExistsByEmail(String email);
}
