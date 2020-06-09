package com.analitic.repositories;

import com.analitic.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {

    @Query(value = "SELECT SUM(price) FROM qdfSelesToUsers WHERE doctor like :user + '%' AND Course = 0 AND FORMAT(SurveyDate, 'MM-yyyy') = :date", nativeQuery = true)
    Double getSumByUserAndDateWithoutCourse(@Param("user") String user, @Param("date") String date);

    @Query(value = "SELECT SUM(price) FROM qdfSelesToUsers WHERE napr like :user + '%' AND Course = 1 AND FORMAT(SurveyDate, 'MM-yyyy') = :date", nativeQuery = true)
    Double getSumByUserAndDateWithCourse(@Param("user") String user, @Param("date") String date);
}
