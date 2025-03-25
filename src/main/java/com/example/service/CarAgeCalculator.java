package com.example.service;

import java.time.LocalDate;
import java.time.Period;

public class CarAgeCalculator {
    public static int getCarAge(int year) {
        return Period.between(LocalDate.of(year, 1, 1), LocalDate.now()).getYears();
    }
}
