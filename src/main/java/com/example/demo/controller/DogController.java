package com.example.demo.controller;

import com.example.demo.model.Dog;
import com.example.demo.service.DogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dog")
public class DogController {

    DogService dogService;

    DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping
    public Dog postDog(@RequestBody Dog dog) {
        dogService.createDog(dog);
        return dog;
    }

    @GetMapping("/all")
    public List<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }

    @GetMapping("/byid/{id}")
    public Dog getDogById(@PathVariable int id) {
        return dogService.getDogById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDogById(@PathVariable int id) {
        dogService.deleteDogById(id);
        return "Deleted: " + id;
    }

    @GetMapping("/olderthan/{age}")
    public List<Dog> listDogOlderThan(@PathVariable int age) {
        return dogService.listDogOlderThan(age);
    }

    @GetMapping("/olderthanaverage")
    public List<Dog> listDogOlderThanAverage() {
        return dogService.listDogOlderThanAverage();
    }
}
