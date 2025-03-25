package com.example;

import com.example.model.Car;
import com.example.service.CarService;
import com.example.service.CarAgeCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CarServiceE2ETest {

    @Test
    public void testFullFlow() {
        // Create the service and add cars
        CarService carService = new CarService();
        carService.addCar(new Car("Toyota", "Corolla", 2021));
        carService.addCar(new Car("BMW", "X5", 2018));
        carService.addCar(new Car("Tesla", "Model 3", 2022));

        // Sorting Cars
        var sortedCars = carService.getSortedCars();
        assertEquals("Tesla", sortedCars.get(0).make());  // Tesla is most recent
        assertEquals("Toyota", sortedCars.get(1).make()); // Next
        assertEquals("BMW", sortedCars.get(2).make());    // Oldest

        // Checking Car Categories
        assertEquals("Reliable", carService.getCarCategory(new Car("Toyota", "Corolla", 2021)));
        assertEquals("Luxury", carService.getCarCategory(new Car("BMW", "X5", 2018)));
        assertEquals("Electric", carService.getCarCategory(new Car("Tesla", "Model 3", 2022)));

        // Checking Car Age
        int carAgeToyota = CarAgeCalculator.getCarAge(2021);
        int carAgeBMW = CarAgeCalculator.getCarAge(2018);
        int carAgeTesla = CarAgeCalculator.getCarAge(2022);
        
        assertTrue(carAgeToyota >= 0);  // Car age should be non-negative
        assertTrue(carAgeBMW >= 0);
        assertTrue(carAgeTesla >= 0);
    }
}
