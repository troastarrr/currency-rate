package com.formedix.currencyrate.controller.impl;

import com.formedix.currencyrate.controller.CurrencyRateCsvController;
import com.formedix.currencyrate.domain.CurrencyRates;
import com.formedix.currencyrate.error.exception.CsvUploadException;
import com.formedix.currencyrate.service.CsvParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CurrencyRateCsvControllerImpl implements CurrencyRateCsvController {

    private final CsvParserService csvParserService;
    @Autowired
    private CurrencyRates currencyRates;

    /**
     * Uploads a CSV file containing currency rate data.
     *
     * @param file the CSV file to upload
     *
     * @return the ResponseEntity containing the uploaded currency rates
     *
     * @throws CsvUploadException if an error occurs while uploading the file
     */
    @Override
    public ResponseEntity<CurrencyRates> uploadCsvFile(MultipartFile file) {
        try {
            CurrencyRates uploadedCurrencyRates = csvParserService.parse(file.getInputStream());
            currencyRates.setRates(uploadedCurrencyRates.getRates());
            return ResponseEntity.ok(currencyRates);
        } catch (Exception e) {
            throw new CsvUploadException("An error occurred while uploading " + file.getOriginalFilename(), e);
        }
    }

    /**
     * Retrieves the currently uploaded CSV file containing currency rate data.
     *
     * @return the ResponseEntity containing the current currency rates
     */
    @Override
    public ResponseEntity<CurrencyRates> getCurrentCsvUpload() {
        return ResponseEntity.ok(currencyRates);
    }
}
