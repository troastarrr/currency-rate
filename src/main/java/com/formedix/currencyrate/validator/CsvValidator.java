package com.formedix.currencyrate.validator;

import com.formedix.currencyrate.error.domain.ErrorCode;
import com.formedix.currencyrate.error.exception.CsvFileException;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class CsvValidator {
    private static final String TEXT_CSV = "text/csv";

    public static void validate(@NonNull MultipartFile file) {
        if (!StringUtils.equals(TEXT_CSV, file.getContentType())) {
            throw new CsvFileException("Invalid file or file format. Only CSV files are allowed.", ErrorCode.INVALID_CSV_FILE_ERROR);
        }
    }
}
