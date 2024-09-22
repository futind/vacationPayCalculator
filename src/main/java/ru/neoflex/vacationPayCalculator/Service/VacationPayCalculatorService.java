package ru.neoflex.vacationPayCalculator.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class VacationPayCalculatorService {
    
    public double calculate(double yearlyPay, long daysOfVacation, 
                            String vacationStartDate, String vacationEndDate) throws DateTimeParseException, IllegalArgumentException
    {
        if (yearlyPay <= 0) throw new IllegalArgumentException("yearlyPay has to be positive.");
        if (daysOfVacation < 0) throw new IllegalArgumentException("daysOfVacation has to be positive.");

        double vacationPay = -1;

        if (vacationStartDate == null && vacationEndDate == null)
        {
            vacationPay = calculatePay(yearlyPay, daysOfVacation);
        }
        else if (vacationStartDate != null && vacationEndDate != null)
        {
            LocalDate startDate = LocalDate.parse(vacationStartDate, RU_FORMATTER);
            LocalDate endDate = LocalDate.parse(vacationEndDate, RU_FORMATTER);

            if (!startDate.isBefore(endDate)) throw new IllegalArgumentException("startDate has to be before endDate.");

            vacationPay = calculatePay(yearlyPay, startDate, endDate);
        }
        else if (vacationStartDate != null && vacationEndDate == null)
        {
            LocalDate startDate = LocalDate.parse(vacationStartDate, RU_FORMATTER);
            vacationPay = calculatePay(yearlyPay, startDate, daysOfVacation);
        }
        else 
        {
            LocalDate endDate = LocalDate.parse(vacationEndDate, RU_FORMATTER);
            LocalDate startDate = endDate.minusDays(daysOfVacation);
            vacationPay = calculatePay(yearlyPay, startDate, endDate);
        }

        return vacationPay;
    }

    private boolean isHoliday(LocalDate date)
    {
        for (LocalDate holiday : HOLIDAYS)
        {
            if (holiday.getMonth() == date.getMonth() && 
                holiday.getDayOfMonth() == date.getDayOfMonth()) 
            {
                return true;
            }
        }

        return false;
    }

    private long countUnpaidDays(LocalDate vacationStartDate, LocalDate vacationEndDate)
    {
        long unpaidVacationDays = 0;

        for (LocalDate vacationIterator = vacationStartDate; 
                       vacationIterator.isBefore(vacationEndDate); 
                       vacationIterator = vacationIterator.plusDays(1))
        {
            boolean isWeekend = vacationIterator.getDayOfWeek() == DayOfWeek.SATURDAY ||
                                vacationIterator.getDayOfWeek() == DayOfWeek.SUNDAY;
            
            if (isWeekend || isHoliday(vacationIterator))
            {
                unpaidVacationDays++;
            }
        }

        return unpaidVacationDays;
    }

    private double getTaxModifier(double yearlyPay)
    {
        return yearlyPay >= PROGRESSIVE_TAX_BORDER ? 0.15 : 0.13;
    }

    private double calculateAverageDailyPay(double yearlyPay)
    {
        return yearlyPay / (AVG_DAYS_IN_MONTH * MONTHS_IN_YEAR);
    }

    private double calculatePay(double yearlyPay, long daysOfVacation)
    {
        double preTaxPay = daysOfVacation * calculateAverageDailyPay(yearlyPay);
        double TaxedPay = preTaxPay - preTaxPay * getTaxModifier(yearlyPay);
        return TaxedPay;
    }

    private double calculatePay(double yearlyPay, LocalDate vacationStartDate, LocalDate vacationEndDate)
    {
        long daysOfVacation = ChronoUnit.DAYS.between(vacationStartDate, vacationEndDate);
        return calculatePay(yearlyPay, daysOfVacation - countUnpaidDays(vacationStartDate, vacationEndDate));
    }

    private double calculatePay(double yearlyPay, LocalDate vacationStartDate, long daysOfVacation)
    {
        LocalDate vacationEndDate = vacationStartDate.plusDays(daysOfVacation);
        return calculatePay(yearlyPay, daysOfVacation - countUnpaidDays(vacationStartDate, vacationEndDate));
    }

    private final double AVG_DAYS_IN_MONTH = 29.3;
    private final int MONTHS_IN_YEAR = 12;
    private final int CURRENT_YEAR = LocalDate.now().getYear();
    private final double PROGRESSIVE_TAX_BORDER = 5e+6;
    private final DateTimeFormatter RU_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    private final Set<LocalDate> HOLIDAYS = Set.of(LocalDate.of(CURRENT_YEAR, 1, 1), 
                                                   LocalDate.of(CURRENT_YEAR, 1, 2),
                                                   LocalDate.of(CURRENT_YEAR, 1, 3), 
                                                   LocalDate.of(CURRENT_YEAR, 1, 4), 
                                                   LocalDate.of(CURRENT_YEAR, 1, 5), 
                                                   LocalDate.of(CURRENT_YEAR, 1, 6), 
                                                   LocalDate.of(CURRENT_YEAR, 1, 7),  
                                                   LocalDate.of(CURRENT_YEAR, 1, 8), 
                                                   LocalDate.of(CURRENT_YEAR, 2, 23), 
                                                   LocalDate.of(CURRENT_YEAR, 3, 8), 
                                                   LocalDate.of(CURRENT_YEAR, 5, 1), 
                                                   LocalDate.of(CURRENT_YEAR, 5, 9), 
                                                   LocalDate.of(CURRENT_YEAR, 6, 12), 
                                                   LocalDate.of(CURRENT_YEAR, 11, 4)
                                                   );

}
