package com.example.service;

import com.example.model.Car;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class CarService {
    private final List<Car> cars = new ArrayList<>();

    public void addCar(Car car) {
        cars.add(car);
    }

    public List<Car> getCars() {
        return new ArrayList<>(cars);
    }

    // ✅ Implementing Streams and Terminal Operations
    public List<Car> getSortedCars() {
        return cars.stream()
                .sorted(Comparator.comparing(Car::year).reversed())
                .toList();
    }

    // ✅ Using Lambdas (Predicate, Consumer, Function)
    public void filterAndPrintNewCars() {
        Predicate<Car> isNewCar = car -> car.year() >= 2020;
        Consumer<Car> carPrinter = car -> System.out.println(car.make() + " " + car.model());
        
        cars.stream()
            .filter(isNewCar)
            .forEach(carPrinter);
    }

    // ✅ Using Switch Expressions & Pattern Matching
    public String getCarCategory(Car car) {
        return switch (car.make()) {
            case "Toyota", "Honda" -> "Reliable";
            case "BMW", "Audi" -> "Luxury";
            case "Tesla" -> "Electric";
            default -> "Unknown";
        };
    }
}
