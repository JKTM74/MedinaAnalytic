package com.analitic.repositories;

import com.analitic.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@Table(name = "qdfSalesServices")
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    @Query(value = "Select TOP 1 PatientCalc from qdfSalesServices WHERE PatientID = :PatientID", nativeQuery = true)
    String findPatientById(@Param("PatientID") int patientID);
}
