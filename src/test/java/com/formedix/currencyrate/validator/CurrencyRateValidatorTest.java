package com.formedix.currencyrate.validator;

import com.formedix.currencyrate.error.exception.InvalidDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CurrencyRateValidatorTest {

    @Test
    @DisplayName("Should not throw InvalidDateException when end date is after start date")
    void shouldNotThrowInvalidDateExceptionWhenEndDateIsAfterStartDate() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 1);

        // When & Then
        assertThatCode(() -> CurrencyRateValidator.checkIfValidDates(startDate, endDate))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Should throw InvalidDateException when end date is before start date")
    void shouldThrowInvalidDateExceptionWhenEndDateIsBeforeStartDate() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 2, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 1);

        // When & Then
        assertThatThrownBy(() -> CurrencyRateValidator.checkIfValidDates(startDate, endDate))
                .isInstanceOf(InvalidDateException.class)
                .hasMessage("Invalid date range: Start date `2023-02-01` should not be greater than End date `2023-01-01`");
    }
}
