package com.formedix.currencyrate.validator;

import com.formedix.currencyrate.error.exception.CsvFileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;


class CsvValidatorTest {

    @Test
    @DisplayName("Should throw CsvFileException for invalid file format")
    void shouldThrowCsvFileExceptionForInvalidFileFormat() {
        // Given
        MultipartFile file = new MockMultipartFile("test.txt", "test.txt", "text/plain", "file content".getBytes());

        // When & Then
        assertThatExceptionOfType(CsvFileException.class)
                .isThrownBy(() -> CsvValidator.validate(file))
                .withMessage("Invalid file or file format. Only CSV files are allowed.");
    }

    @Test
    @DisplayName("Should throw CsvFileException for empty file")
    void shouldThrowCsvFileExceptionForEmptyFile() {
        // Given
        MultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/csv", "".getBytes());

        // When & Then
        assertThatExceptionOfType(CsvFileException.class)
                .isThrownBy(() -> CsvValidator.validate(file))
                .withMessage("Empty csv file is not allowed.");
    }

    @Test
    @DisplayName("Should validate CSV file")
    void shouldValidateCsvFile() {
        // Given
        MultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/csv", "file content".getBytes());

        // When & Then
        assertThatCode(() -> CsvValidator.validate(file))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should throw CsvFileException for null file content")
    void shouldThrowCsvFileExceptionForNullFileContent() {
        // Given
        MultipartFile file = new MockMultipartFile("test.csv", "test.csv", "text/csv", (byte[]) null);

        // When & Then
        assertThatExceptionOfType(CsvFileException.class)
                .isThrownBy(() -> CsvValidator.validate(file))
                .withMessage("Empty csv file is not allowed.");
    }
}
