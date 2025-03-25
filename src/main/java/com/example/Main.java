package com.example;

import com.example.model.Car;
import com.example.service.CarService;
import com.example.service.CarAgeCalculator;

public class Main {
    public static void main(String[] args) {
        CarService carService = new CarService();
        
        carService.addCar(new Car("Toyota", "Corolla", 2021));
        carService.addCar(new Car("BMW", "X5", 2018));
        carService.addCar(new Car("Tesla", "Model 3", 2022));

        System.out.println("Sorted Cars:");
        carService.getSortedCars().forEach(car -> System.out.println(car.make() + " " + car.year()));

        System.out.println("Car Categories:");
        carService.getCars().forEach(car -> 
            System.out.println(car.make() + " is " + carService.getCarCategory(car)));

        System.out.println("Car Age:");
        carService.getCars().forEach(car -> 
            System.out.println(car.make() + " is " + CarAgeCalculator.getCarAge(car.year()) + " years old"));
    }
}
