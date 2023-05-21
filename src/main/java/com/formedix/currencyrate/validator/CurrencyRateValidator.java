package com.formedix.currencyrate.validator;

import com.formedix.currencyrate.error.exception.InvalidDateException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class CurrencyRateValidator {

    /**
     * Checks if the provided start date and end date are valid.
     *
     * @param startDate the start date
     * @param endDate   the end date
     *
     * @throws InvalidDateException if the end date is before the start date
     */
    public static void checkIfValidDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateException(String.format("Invalid date range: Start date `%s` should not be greater than End date `%s`", startDate, endDate));
        }
    }

}
