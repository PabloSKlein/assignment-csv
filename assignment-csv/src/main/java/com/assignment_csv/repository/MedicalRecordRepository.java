package com.assignment_csv.repository;

import com.assignment_csv.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String> {
    @Query("select mr from MedicalRecord mr where " +
            "(:from is null or mr.fromDate >= :from) " +
            "and (:to is null or mr.toDate <= :to)")
    List<MedicalRecord> findAll(LocalDate from, LocalDate to);
}
