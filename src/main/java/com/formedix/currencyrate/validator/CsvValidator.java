package com.formedix.currencyrate.validator;

import com.formedix.currencyrate.error.domain.ErrorCode;
import com.formedix.currencyrate.error.exception.CsvFileException;
import com.formedix.currencyrate.error.exception.CsvParsingException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@UtilityClass
public class CsvValidator {

    private static final String TEXT_CSV = "text/csv";

    /**
     * Validates the provided CSV file.
     *
     * @param file the CSV file to validate
     *
     * @throws CsvFileException if the file is invalid or has an unsupported format
     */
    public static void validate(@NonNull MultipartFile file) throws CsvFileException {
        try {
            if (!StringUtils.equals(TEXT_CSV, file.getContentType())) {
                throw new CsvFileException("Invalid file or file format. Only CSV files are allowed.", ErrorCode.INVALID_CSV_FILE_ERROR);
            }
            if (ArrayUtils.isEmpty(file.getBytes())) {
                throw new CsvFileException("Empty csv file is not allowed.", ErrorCode.INVALID_CSV_FILE_ERROR);
            }
        } catch (Exception e) {
            throw new CsvFileException(e.getMessage(), ErrorCode.INVALID_CSV_FILE_ERROR);
        }

    }

    /**
     * Validates the headers of the CSV file.
     *
     * @param rows the rows of the CSV file
     *
     * @throws CsvParsingException if the CSV file does not have at least 2 columns
     */
    public static void validateHeaders(List<String[]> rows) throws CsvParsingException {
        if (rows.isEmpty()) {
            throw new CsvParsingException("CSV file is empty", ErrorCode.CSV_PARSING_ERROR);
        }
        String[] headers = rows.get(0);
        if (headers != null && headers.length < 2) {
            throw new CsvParsingException("CSV file must have at least 2 columns", ErrorCode.CSV_PARSING_ERROR);
        }
    }
}