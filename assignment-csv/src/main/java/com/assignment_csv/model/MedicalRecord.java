package com.assignment_csv.model;

import com.opencsv.bean.CsvDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDate;

import static com.assignment_csv.common.DateFormats.CSV;

@Getter
@Entity
@Table(name = "MEDICAL_RECORD")
public class MedicalRecord {

    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "CODE_LIST_CODE")
    private String codeListCode;

    @Column(name = "DISPLAY_VALUE")
    private String displayValue;

    @Column(name = "LONG_DESCRIPTION")
    private String longDescription;

    @Column(name = "FROM_DATE")
    @CsvDate(value = CSV)
    private LocalDate fromDate;

    @Column(name = "TO_DATE")
    @CsvDate(value = CSV)
    private LocalDate toDate;

    @Column(name = "SORTING_PRIORITY")
    private int sortingPriority;
}
