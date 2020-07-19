package com.analytic.repositories;

import com.analytic.models.SalesServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalesServicesRepository extends JpaRepository<SalesServices, UUID> {

    /**
     * @param user - врач
     * @param date
     * @return Курс(выполненные) по врачу и дате
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "Doctor LIKE '%' + :user + '%' AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 1 AND \n" +
            "Napr LIKE '%' + :user + '%' AND \n" +
            "(Sert <> 'ЗП' OR Sert IS NULL)  AND \n" +
            "(Service NOT LIKE '%УЗИ%' OR Service IS NULL) AND \n" +
            "(Service NOT LIKE '%справ%' OR Service IS NULL)", nativeQuery = true)
    SalesServices getPersonalServices(@Param("user") String user, @Param("date") String date);

    /**
     * @param user - врач
     * @param date
     * @return Курс(направленные) по врачу и дате
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "(Doctor NOT LIKE '%' + :user + '%' OR Doctor IS NULL) AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 1 AND \n" +
            "Napr LIKE '%' + :user + '%' AND \n" +
            "(Sert <> 'ЗП' OR Sert IS NULL)  AND \n" +
            "(Service NOT LIKE '%справ%' OR Service IS NULL)", nativeQuery = true)
    SalesServices getDirectedServices(@Param("user") String user, @Param("date") String date);

    /**
     * @param user - врач
     * @param date
     * @return Оказанные услуги (без курса) по врачу и дате
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "Doctor LIKE '%' + :user + '%' AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 0 AND \n" +
            "(Napr NOT LIKE '%' + :user + '%' OR Napr IS NULL) AND \n" +
            "(Sert <> 'ЗП' OR Sert IS NULL)  AND \n" +
            "(Service NOT LIKE '%УЗИ%' OR Service IS NULL) AND \n" +
            "(Service NOT LIKE '%справ%' OR Service IS NULL)", nativeQuery = true)
    SalesServices getStreetServices(@Param("user") String user, @Param("date") String date);


    /**
     * @param user - врач
     * @param date
     * @return Курс(выполненные) по врачу и дате
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "Doctor LIKE '%' + :user + '%' AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 1 AND \n" +
            "Napr LIKE '%' + :user + '%' AND \n" +
            "Service LIKE '%УЗИ%'", nativeQuery = true)
    SalesServices getPersonalUziServices(@Param("user") String user, @Param("date") String date);

    /**
     * @param user - врач
     * @param date
     * @return Курс(направленные) по врачу и дате
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "(Doctor NOT LIKE '%' + :user + '%' OR Doctor IS NULL) AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 1 AND \n" +
            "Napr LIKE '%' + :user + '%' AND \n" +
            "Service LIKE '%УЗИ%'", nativeQuery = true)
    SalesServices getDirectedUziServices(@Param("user") String user, @Param("date") String date);

    /**
     * @param user - врач
     * @param date
     * @return Оказанные услуги (без курса)
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices \n" +
            "WHERE FORMAT(SurveyDate, 'MM-yyyy') = :date AND \n" +
            "(Doctor LIKE '%' + :user + '%' OR Doctor IS NULL) AND \n" +
            "Service NOT IN (Select Service FROM tblServicePayExeption) AND \n" +
            "course = 0 AND \n" +
            "(Napr NOT LIKE '%' + :user+ '%' OR Napr IS NULL) AND \n" +
            "(Sert <> 'ЗП' OR Sert IS NULL)  AND \n" +
            "Service LIKE '%УЗИ%'", nativeQuery = true)
    SalesServices getStreetUziServices(@Param("user") String user, @Param("date") String date);

    /**
     * @param date
     * @return Все анализы
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices\n" +
            "WHERE Doctor in ('ИНВИТРО', 'СИТИЛАБ', 'Сиблабсервис', 'КДЛ') AND\n" +
            "FORMAT(SurveyDate, 'MM-yyyy') = :date", nativeQuery = true)
    SalesServices getAllAnalyzes(@Param("date") String date);

    /**
     * @param date
     * @return Все анализы с курсов
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices\n" +
            "WHERE Doctor in ('ИНВИТРО', 'СИТИЛАБ', 'Сиблабсервис', 'КДЛ') AND\n" +
            "FORMAT(SurveyDate, 'MM-yyyy') = :date AND\n" +
            "course = 1", nativeQuery = true)
    SalesServices getAllKlAnalyzes(@Param("date") String date);

    /**
     * @param date
     * @return Все анализы с улицы
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices\n" +
            "WHERE Doctor in ('ИНВИТРО', 'СИТИЛАБ', 'Сиблабсервис', 'КДЛ') AND\n" +
            "FORMAT(SurveyDate, 'MM-yyyy') = :date AND\n" +
            "course = 0", nativeQuery = true)
    SalesServices getStreetAnalyzes(@Param("date") String date);

    /**
     * @param date
     * @return Все процедурный
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices\n" +
            "WHERE Doctor like 'Процедурный%' AND\n" +
            "FORMAT(SurveyDate, 'MM-yyyy') = :date", nativeQuery = true)
    SalesServices getAllProcedurniy(@Param("date") String date);

    /**
     * @param date
     * @return Все процедурный по курсам
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices\n" +
            "WHERE Doctor like 'Процедурный%' AND\n" +
            "FORMAT(SurveyDate, 'MM-yyyy') = :date AND\n" +
            "Course = 1", nativeQuery = true)
    SalesServices getAllKlProcedurniy(@Param("date") String date);

    /**
     * @param date
     * @return Процедурный с улицы
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices\n" +
            "WHERE Doctor like 'Процедурный%' AND\n" +
            "FORMAT(SurveyDate, 'MM-yyyy') = :date AND\n" +
            "Course = 0", nativeQuery = true)
    SalesServices getStreetProcedurniy(@Param("date") String date);

    /**
     * @param date
     * @return Процедурный с улицы
     */
    @Query(value = "SELECT iif(max(ID) IS NULL, 0, max(ID)) AS ID, count(id) AS servicesCount, iif(sum(Price2) IS NULL, 0, sum(Price2)) AS sumPrice, COUNT(DISTINCT(PatientID)) AS patientsCount\n" +
            "FROM tblSalesServices\n" +
            "WHERE Doctor like 'Комиссии%' AND\n" +
            "FORMAT(SurveyDate, 'MM-yyyy') = :date", nativeQuery = true)
    SalesServices getStreetKomissii(@Param("date") String date);

    @Query(value = "SELECT iif(sum(summ) IS NULL, 0, sum(summ)) as summ2 \n" +
            "FROM [tblRS] \n" +
            "WHERE FORMAT(Dat2, 'MM-yyyy') = :date AND \n" +
            "[Naz] = 'безналичный расчет'", nativeQuery = true)
    Double getKomissiiOrg(@Param("date") String date);
}