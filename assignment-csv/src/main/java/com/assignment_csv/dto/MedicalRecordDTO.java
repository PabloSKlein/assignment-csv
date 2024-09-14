package com.assignment_csv.dto;

import com.assignment_csv.model.MedicalRecord;

import java.time.LocalDate;

public record MedicalRecordDTO(
        String code,
        String source,
        String otherCode,
        String displayValue,
        String longDescription,
        LocalDate fromDate,
        LocalDate toDate,
        Integer sortingPriority
) {
    public MedicalRecordDTO(MedicalRecord medicalRecord) {
        this(medicalRecord.getCode(),
                medicalRecord.getSource(),
                medicalRecord.getCodeListCode(),
                medicalRecord.getDisplayValue(),
                medicalRecord.getLongDescription(),
                medicalRecord.getFromDate(),
                medicalRecord.getToDate(),
                medicalRecord.getSortingPriority());
    }
}
