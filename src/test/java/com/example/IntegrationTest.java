package com.example.integration;

import com.example.service.CarService;
import com.example.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class IntegrationTest {

    private CarService carService;

    @BeforeEach
    public void setUp() {
        // Initialize the CarService before each test
        carService = new CarService();
    }

    @Test
    public void testAddCar() {
        // Adding a car and checking if the car is added to the list
        carService.addCar(new Car("Toyota", "Corolla", 2021));
        assertEquals(1, carService.getCars().size(), "The car list should contain 1 car.");
    }

    @Test
    public void testGetSortedCars() {
        // Adding cars to the service
        carService.addCar(new Car("Toyota", "Corolla", 2021));
        carService.addCar(new Car("BMW", "X5", 2018));
        carService.addCar(new Car("Tesla", "Model 3", 2022));

        // Getting the sorted list
        List<Car> sortedCars = carService.getSortedCars();

        // Verifying the sorting order based on the year
        assertEquals("Tesla", sortedCars.get(0).make(), "The first car should be the most recent.");
        assertEquals("Toyota", sortedCars.get(1).make(), "The second car should be the next most recent.");
        assertEquals("BMW", sortedCars.get(2).make(), "The third car should be the oldest.");
    }

    @Test
    public void testGetCarCategory() {
        // Creating car objects
        Car toyota = new Car("Toyota", "Corolla", 2021);
        Car bmw = new Car("BMW", "X5", 2018);

        // Verifying the car category
        assertEquals("Reliable", carService.getCarCategory(toyota), "Toyota should be categorized as 'Reliable'.");
        assertEquals("Luxury", carService.getCarCategory(bmw), "BMW should be categorized as 'Luxury'.");
    }
}
