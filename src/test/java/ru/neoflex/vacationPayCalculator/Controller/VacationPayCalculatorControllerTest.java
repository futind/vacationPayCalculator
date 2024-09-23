package ru.neoflex.vacationPayCalculator.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import ru.neoflex.vacationPayCalculator.Service.VacationPayCalculatorService;

@WebMvcTest
public class VacationPayCalculatorControllerTest {
    
    @Autowired
    private MockMvc mockMVC;

    @MockBean
    private VacationPayCalculatorService service;

    @Test
    void cantWorkWithoutAnyParams() throws Exception
    {
        this.mockMVC.perform(get("/calculate"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    void canWorkWithDuration() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&duration=14"))
                    .andDo(print())
                    .andExpect(status().isOk());
    }

    @Test
    void canWorkWithDurationAndStartDate() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&duration=14&startDate=02.09.2024"))
                    .andDo(print())
                    .andExpect(status().isOk());
    }

    @Test
    void canWorkWithDurationAndEndDate() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&duration=14&endDate=16.09.2024"))
                    .andDo(print())
                    .andExpect(status().isOk());
    }

    @Test
    void canWorkWithBothDates() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&duration=14&startDate=02.09.2024&endDate=16.09.2024"))
                    .andDo(print())
                    .andExpect(status().isOk());
    }

    @Test
    void cantWorkWithNegativePay() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=-1"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    void cantWorkWithNegativeDuration() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&duration=-1"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    void cantWorkWithZeroDurationDates() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&startDate=01.01.2024&endDate=01.01.2024"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    void cantWorkWithWronglyOrderedDates() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&duration=14&startDate=16.09.2024&endDate=02.09.2024"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }

    @Test
    void cantWorkWithWronglyFormattedDates() throws Exception
    {
        this.mockMVC.perform(get("/calculate?yearlyPay=1200000&duration=14&startDate=2024-09-02"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }


}
