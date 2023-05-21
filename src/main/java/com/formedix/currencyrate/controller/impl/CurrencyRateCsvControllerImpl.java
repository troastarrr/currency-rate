package com.formedix.currencyrate.controller.impl;

import com.formedix.currencyrate.controller.CurrencyRateCsvController;
import com.formedix.currencyrate.domain.CurrencyRates;
import com.formedix.currencyrate.error.domain.ErrorCode;
import com.formedix.currencyrate.error.exception.CsvFileException;
import com.formedix.currencyrate.service.CurrencyRateCsvService;
import com.formedix.currencyrate.validator.CsvValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CurrencyRateCsvControllerImpl implements CurrencyRateCsvController {

    private final CurrencyRateCsvService currencyRateCsvService;

    /**
     * Uploads a CSV file containing currency rate data.
     *
     * @param file the CSV file to upload
     *
     * @return the ResponseEntity containing the uploaded currency rates
     *
     * @throws CsvFileException if an error occurs while uploading the file
     */
    @Override
    public ResponseEntity<CurrencyRates> uploadCsvFile(MultipartFile file) {
        try {
            CsvValidator.validate(file);
            return ResponseEntity.ok(currencyRateCsvService.updateCurrencyRates(file.getInputStream()));
        } catch (CsvFileException e) {
            throw e;
        } catch (Exception e) {
            throw new CsvFileException("An error occurred while uploading " + file.getOriginalFilename(), ErrorCode.CSV_UPLOAD_ERROR, e);
        }
    }

    /**
     * Retrieves the currently uploaded CSV file containing currency rate data.
     *
     * @return the ResponseEntity containing the current currency rates
     */
    @Override
    public ResponseEntity<CurrencyRates> getCurrentCsvRates() {
        return ResponseEntity.ok(currencyRateCsvService.getCurrentCurrencyRates());
    }
}
