package ru.neoflex.vacationPayCalculator.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VacationPayCalculatorServiceTest {
    
    private final double AVG_DAYS_IN_MONTH = 29.3;
    private final int MONTHS_IN_YEAR = 12;

    @Test
    void canCalculateCorrectlyWithDuration()
    {
        // GET /calculate?yearlyPay=1200000&daysOfVacation=14

        VacationPayCalculatorService service = new VacationPayCalculatorService();

        double yearlyPay = 1.2e+6;
        long daysOfVacation = 14;
        double taxModifier = 0.13;

        double preTaxPay = yearlyPay / (AVG_DAYS_IN_MONTH * MONTHS_IN_YEAR) * daysOfVacation;
        double taxedPay = preTaxPay - preTaxPay * taxModifier;

        assertEquals(taxedPay, service.calculate(yearlyPay, daysOfVacation, null, null), 0.01);
    }

    @Test
    void canCalculateCorrectlyWithDurationAndStartDate()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        double yearlyPay = 1.2e+6;
        long daysOfVacation = 14;
        double taxModifier = 0.13;
        String startDate = "02.09.2024";

        long realDaysOfVacation = 10;

        double preTaxPay = yearlyPay / (AVG_DAYS_IN_MONTH * MONTHS_IN_YEAR) * realDaysOfVacation;
        double taxedPay = preTaxPay - preTaxPay * taxModifier;

        assertEquals(taxedPay, service.calculate(yearlyPay, daysOfVacation, startDate, null), 0.01);
    }

    @Test
    void canCalculateCorrectlyWithDurationAndEndDate()
    {
        // for some reason, idk
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        double yearlyPay = 1.2e+6;
        long daysOfVacation = 14;
        double taxModifier = 0.13;
        String endDate = "16.09.2024";

        long realDaysOfVacation = 10;

        double preTaxPay = yearlyPay / (AVG_DAYS_IN_MONTH * MONTHS_IN_YEAR) * realDaysOfVacation;
        double taxedPay = preTaxPay - preTaxPay * taxModifier;

        assertEquals(taxedPay, service.calculate(yearlyPay, daysOfVacation, null, endDate), 0.01);
    }

    @Test
    void canCalculateCorrectlyWithStartAndEndDates()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        double yearlyPay = 1.2e+6;
        long daysOfVacation = 14;
        double taxModifier = 0.13;
        String startDate = "02.09.2024";
        String endDate = "16.09.2024";

        long realDaysOfVacation = 10;

        double preTaxPay = yearlyPay / (AVG_DAYS_IN_MONTH * MONTHS_IN_YEAR) * realDaysOfVacation;
        double taxedPay = preTaxPay - preTaxPay * taxModifier;

        assertEquals(taxedPay, service.calculate(yearlyPay, daysOfVacation, startDate, endDate), 0.01);
    }

    @Test 
    void canCalculateCorrectlyWithNewYearHolidays()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        double yearlyPay = 1.2e+6;
        long daysOfVacation = 14;
        double taxModifier = 0.13;
        String startDate = "31.12.2024";
        String endDate = "14.01.2025";

        long realDaysOfVacation = 4;

        double preTaxPay = yearlyPay / (AVG_DAYS_IN_MONTH * MONTHS_IN_YEAR) * realDaysOfVacation;
        double taxedPay = preTaxPay - preTaxPay * taxModifier;

        assertEquals(taxedPay, service.calculate(yearlyPay, daysOfVacation, startDate, endDate), 0.01);
    }

    @Test
    void willThrowAnExceptionWithWrongOrderedDates()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        assertThrowsExactly(IllegalArgumentException.class, () -> service.calculate(1,1, "21.09.2024", "07.09.2024"));
    }

    @Test
    void willThrowAnExceptionWithZeroPay()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        assertThrowsExactly(IllegalArgumentException.class, () -> service.calculate(0, 14, null, null));
    }

    @Test
    void willThrowAnExceptionWithZeroDuration()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        assertThrowsExactly(IllegalArgumentException.class, () -> service.calculate(1.2e+6, 0, null, null));
    }

    @Test
    void willThrowAnExceptionWithNegativePay()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        assertThrowsExactly(IllegalArgumentException.class, () -> service.calculate(-1, 14, null, null));
    }

    @Test
    void willThrowAnExceptionWithNegativeDuration()
    {
        VacationPayCalculatorService service = new VacationPayCalculatorService();

        assertThrowsExactly(IllegalArgumentException.class, () -> service.calculate(1.2e+6, -1, null, null));
    }

}
