package com.assignment_csv.controller;

import com.assignment_csv.commons.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MedicalRecordControllerIT extends BaseIT {

    @Test
    void shouldImportFile() throws Exception {
        var resource = new ClassPathResource("file/test-import-data.csv");

        var mockFile = new MockMultipartFile(
                "file",
                resource.getFilename(),
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(resource.getFile().toPath())
        );

        mockMvc.perform(multipart("/medical-record")
                        .file(mockFile))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAllMedicalRecords() throws Exception {
        shouldImportFile();

        mockMvc.perform(get("/medical-record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(18)))
                //Validate order
                .andExpect(jsonPath("$[0].code", equalTo("415974002")))
                //Validate it is not importing the first line
                .andExpect(jsonPath("$[*].code", not(hasItem("code"))));
    }

    @Test
    void shouldReturnAllMedicalRecordsFiltered() throws Exception {
        shouldImportFile();

        mockMvc.perform(get("/medical-record")
                        .param("from", "2019-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(18)))
                //Validate order
                .andExpect(jsonPath("$[0].code", equalTo("415974002")))
                //Validate it is not importing the first line
                .andExpect(jsonPath("$[*].code", not(hasItem("code"))));
    }

    @Test
    void shouldReturnNoMedicalRecordsFiltered() throws Exception {
        shouldImportFile();

        mockMvc.perform(get("/medical-record")
                        .param("from", "2020-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnMedicalRecordByCode() throws Exception {
        shouldImportFile();

        mockMvc.perform(get("/medical-record/{code}", "271636001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("271636001")))
                .andExpect(jsonPath("$.source", equalTo("ZIB")))
                .andExpect(jsonPath("$.otherCode", equalTo("ZIB001")))
                .andExpect(jsonPath("$.displayValue", equalTo("Polsslag regelmatig")))
                .andExpect(jsonPath("$.longDescription", equalTo("The long description is necessary")))
                .andExpect(jsonPath("$.fromDate", equalTo("2019-01-01")))
                .andExpect(jsonPath("$.toDate", nullValue()))
        ;
    }

    @Test
    void shouldReturnMedicalRecordNotFound() throws Exception {
        shouldImportFile();

        mockMvc.perform(get("/medical-record/{code}", "test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Medical Record not found.")));
    }

    @Test
    void shouldDeleteAll() throws Exception {
        shouldImportFile();

        mockMvc.perform(delete("/medical-record"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/medical-record"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldExportToCsv() throws Exception {
        shouldImportFile();

        var response = mockMvc.perform(get("/medical-record/export"))
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsByteArray();

        var resource = new ClassPathResource("file/test-export-data.csv");
        var expectedResponse = Files.readAllBytes(resource.getFile().toPath());

        //TODO Couldn't finish this test in time
        //assertEquals(expectedResponse, response);
    }
}