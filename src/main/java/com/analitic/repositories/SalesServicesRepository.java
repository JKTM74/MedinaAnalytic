package com.analitic.repositories;

import com.analitic.models.SalesServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalesServicesRepository extends JpaRepository<SalesServices, UUID> {

    /*@Query(value = "SELECT SUM(price) FROM qdfSelesToUsers WHERE doctor like :user + '%' AND Course = 0 AND FORMAT(SurveyDate, 'MM-yyyy') = :date", nativeQuery = true)
    Double getSumByUserAndDateWithoutCourse(@Param("user") String user, @Param("date") String date);

    @Query(value = "SELECT SUM(price) FROM qdfSelesToUsers WHERE napr like :user + '%' AND Course = 1 AND FORMAT(SurveyDate, 'MM-yyyy') = :date", nativeQuery = true)
    Double getSumByUserAndDateWithCourse(@Param("user") String user, @Param("date") String date);*/

    /**
     *
     * @param user - врач
     * @param date
     * @return Курс(выполненные) по врачу и дате
     */
    @Query(value = "SELECT count(id) AS servicesCount, sum(Price2) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "Doctor LIKE '%' + :user + '%' AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 1 AND \n" +
            "Napr LIKE '%' + :user+ '%' AND \n" +
            "(Sert <> 'ЗП' OR Sert IS NULL)  AND \n" +
            "Service NOT LIKE '%УЗИ%' AND \n" +
            "Service NOT LIKE '%справ%'", nativeQuery = true)
    SalesServices getPersonalServices(@Param("user") String user, @Param("date") String date);

    /**
     *
     * @param user - врач
     * @param date
     * @return Курс(направленные) по врачу и дате
     */
    @Query(value = "SELECT count(id) AS servicesCount, sum(Price2) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "Doctor NOT LIKE '%' + :user + '%' AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 1 AND \n" +
            "Napr LIKE '%' + :user+ '%' AND \n" +
            "(Sert <> 'ЗП' OR Sert IS NULL)  AND \n" +
            "Service NOT LIKE '%справ%'", nativeQuery = true)
    SalesServices getDirectedServices(@Param("user") String user, @Param("date") String date);

    /**
     *
     * @param user
     * @param date
     * @return Оказанные услуги (без курса)
     */
    @Query(value = "SELECT count(id) AS servicesCount, sum(Price2) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "Doctor LIKE '%' + :user + '%' AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "Napr NOT LIKE '%' + :user+ '%' AND \n" +
            "(Sert <> 'ЗП' OR Sert IS NULL)  AND \n" +
            "Service NOT LIKE '%УЗИ%' AND \n" +
            "Service NOT LIKE '%справ%'", nativeQuery = true)
    SalesServices getStreetServices(@Param("user") String user, @Param("date") String date);
}
