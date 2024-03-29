package com.analytic.repositories;

import com.analytic.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Repository
@Table(name = "tblusers")
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    @Query(value = "Select ID, UserFullName, Title, Otdel from tblUsers WHERE Otdel = :Department AND (Dead = 0 OR Dead IS NULL)", nativeQuery = true)
    List<Doctor> findDoctorsByDepartment(@Param("Department") int department);
}
