package com.formedix.currencyrate.controller;

import com.formedix.currencyrate.domain.CurrencyRates;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/csv/v1")
@Tag(name = "CSV File Management", description = "Manage csv files")
public interface CurrencyRateCsvController {

    @PostMapping("/upload")
    @Operation(
            summary = "Upload Currency Rate CSV",
            description = "Upload and store the file.",
            tags = "CSV File Management"
    )
    ResponseEntity<CurrencyRates> uploadCsvFile(@Valid @RequestBody final MultipartFile file);

    @GetMapping("/current-upload")
    @Operation(
            summary = "Get the current Currency Rate CSV",
            description = "Retrieves th current uploaded csv",
            tags = "CSV File Management"
    )
    ResponseEntity<CurrencyRates> getCurrentCsvRates();
}
