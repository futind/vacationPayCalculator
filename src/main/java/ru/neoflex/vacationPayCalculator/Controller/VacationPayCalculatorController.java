package ru.neoflex.vacationPayCalculator.Controller;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ru.neoflex.vacationPayCalculator.Service.VacationPayCalculatorService;


@RestController
@RequestMapping
public class VacationPayCalculatorController 
{
    private final VacationPayCalculatorService service;

    public VacationPayCalculatorController(VacationPayCalculatorService service)
    {
        this.service = service;
    }

    @GetMapping("/calculate")
    public double calculateVacationPay(@RequestParam(name = "yearlyPay") double yearlyPay, 
                                       @RequestParam(name = "duration", defaultValue = "1", required = false) long daysOfVacation,
                                       @RequestParam(name = "startDate", required = false) String vacationStartDate,
                                       @RequestParam(name = "endDate", required = false) String vacationEndDate
                                       )
    {
        return service.calculate(yearlyPay, daysOfVacation, vacationStartDate, vacationEndDate);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDTPException(DateTimeParseException DTPException)
    {
        return "Wrong date format!\nThe error is: " + DTPException.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIAException(IllegalArgumentException IAException)
    {
        return "Illegal Argument!\nThe error is: " + IAException.getMessage();
    }

}