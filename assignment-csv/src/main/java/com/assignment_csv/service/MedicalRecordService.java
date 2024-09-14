package com.assignment_csv.service;

import com.assignment_csv.exceptions.InternalErrorException;
import com.assignment_csv.exceptions.NotFoundException;
import com.assignment_csv.model.MedicalRecord;
import com.assignment_csv.repository.MedicalRecordRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.List;

import static com.assignment_csv.common.DateFormats.CSV;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Comparator.comparing;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
@AllArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository repository;

    public void importFromCsv(MultipartFile file) {
        try (var reader = new InputStreamReader(file.getInputStream())) {
            CsvToBean<MedicalRecord> csvToBean = getCsvToBeanParser(reader);

            var medicalRecords = csvToBean.parse();

            repository.saveAll(medicalRecords);

        } catch (Exception e) {
            throw new InternalErrorException("Failed to process CSV file.", e);
        }
    }

    public byte[] exportToCsv() {
        var medicalRecords = repository.findAll();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            csvWriter.writeNext(getHeader());

            for (MedicalRecord medicalRecord : medicalRecords) {
                csvWriter.writeNext(getLine(medicalRecord));
            }

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new InternalErrorException("Failed to export to CSV.", e);
        }
    }

    public List<MedicalRecord> findAll(LocalDate from, LocalDate to) {
        return repository.findAll(from, to).stream()
                .sorted(comparing(MedicalRecord::getSortingPriority).reversed())
                .toList();
    }

    public MedicalRecord findByCode(String code) {
        return repository.findById(code)
                .orElseThrow(() -> new NotFoundException("Medical Record not found."));
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    private CsvToBean<MedicalRecord> getCsvToBeanParser(InputStreamReader reader) {
        return new CsvToBeanBuilder<MedicalRecord>(reader)
                .withType(MedicalRecord.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
    }

    private String[] getLine(MedicalRecord medicalRecord) {
        return new String[]{
                defaultIfNull(medicalRecord.getSource(), EMPTY),
                defaultIfNull(medicalRecord.getCodeListCode(), EMPTY),
                defaultIfNull(medicalRecord.getCode(), EMPTY),
                defaultIfNull(medicalRecord.getDisplayValue(), EMPTY),
                defaultIfNull(medicalRecord.getLongDescription(), EMPTY),
                ofNullable(medicalRecord.getFromDate()).map(date -> date.format(ofPattern(CSV))).orElse(EMPTY),
                ofNullable(medicalRecord.getToDate()).map(date -> date.format(ofPattern(CSV))).orElse(EMPTY),
                valueOf(medicalRecord.getSortingPriority())
        };
    }

    private String[] getHeader() {
        return new String[]{
                "source", "codeListCode",
                "code", "displayValue", "longDescription",
                "fromDate", "toDate", "sortingPriority"
        };
    }

}
