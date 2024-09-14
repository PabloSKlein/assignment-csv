package com.assignment_csv.controller;

import com.assignment_csv.dto.MedicalRecordDTO;
import com.assignment_csv.service.MedicalRecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void importFromCsv(@RequestBody MultipartFile file) {
        service.importFromCsv(file);
    }

    @GetMapping("/export")
    byte[] exportToCsvAll() {
        return service.exportToCsv();
    }

    @GetMapping
    List<MedicalRecordDTO> findAll(@RequestParam(required = false) LocalDate from,
                                   @RequestParam(required = false) LocalDate to) {
        return service.findAll(from, to).stream()
                .map(MedicalRecordDTO::new)
                .toList();
    }

    @GetMapping("/{code}")
    MedicalRecordDTO findByCode(@PathVariable String code) {
        return new MedicalRecordDTO(service.findByCode(code));
    }

    @DeleteMapping
    void deleteAll() {
        service.deleteAll();
    }
}
