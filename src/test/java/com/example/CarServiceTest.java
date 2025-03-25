package com.example.service;

import com.example.model.Car;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class CarServiceTest {

    @Test
    public void testAddCar() {
        CarService carService = new CarService();
        carService.addCar(new Car("Toyota", "Corolla", 2021));
        
        assertEquals(1, carService.getCars().size());
    }

    @Test
    public void testGetSortedCars() {
        CarService carService = new CarService();
        carService.addCar(new Car("Toyota", "Corolla", 2021));
        carService.addCar(new Car("BMW", "X5", 2018));
        carService.addCar(new Car("Tesla", "Model 3", 2022));
        
        List<Car> sortedCars = carService.getSortedCars();
        
        // Check if cars are sorted in descending order of year
        assertEquals("Tesla", sortedCars.get(0).make());  // Most recent year (2022)
        assertEquals("Toyota", sortedCars.get(1).make()); // Next (2021)
        assertEquals("BMW", sortedCars.get(2).make());    // Oldest (2018)
    }

    @Test
    public void testGetCarCategory() {
        CarService carService = new CarService();
        Car toyota = new Car("Toyota", "Corolla", 2021);
        Car bmw = new Car("BMW", "X5", 2018);
        
        assertEquals("Reliable", carService.getCarCategory(toyota));
        assertEquals("Luxury", carService.getCarCategory(bmw));
    }
}
