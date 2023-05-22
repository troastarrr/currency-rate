package com.formedix.currencyrate.controller;

import com.formedix.currencyrate.controller.impl.CurrencyRateCsvControllerImpl;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.service.CurrencyRateCsvService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class CurrencyRateCsvControllerImplTest {

    private MockMvc mockMvc;

    @Mock
    private CurrencyRateCsvService currencyRateCsvService;

    @BeforeEach
    void setUp() {
        CurrencyRateCsvControllerImpl controller = new CurrencyRateCsvControllerImpl(currencyRateCsvService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Should retrieve current CSV rates")
    void shouldRetrieveCurrentCsvRates() throws Exception {
        // Given
        List<GetCurrencyRateDto> currencyRates = Collections.singletonList(new GetCurrencyRateDto());
        when(currencyRateCsvService.getCurrentCurrencyRates()).thenReturn(currencyRates);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/csv/v1/current-upload"))
                .andExpect(status().isOk());
    }

}