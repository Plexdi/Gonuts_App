package com.gonuts.gonutsbackend.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonuts.gonutsbackend.Service.DrinkService;
import com.gonuts.gonutsbackend.model.Drink;



@RestController
@RequestMapping("/api/menu")
public class DrinkController {
    
    private final DrinkService DrinkService;

    public DrinkController(DrinkService DrinkService){
        this.DrinkService = DrinkService;
    }

    @GetMapping("/getDrink/{drink}")
    public ResponseEntity<Object> getDrink(@PathVariable String drink) {
        Drink foundDrink = DrinkService.getDrinkByName(drink);
        if (foundDrink != null) {
            return ResponseEntity.ok(foundDrink);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drink not found");
        }
    }

    @GetMapping("/getMenu/{category}")
    public ResponseEntity<Object> getMenu(@PathVariable String category){
        List<Drink> drinks = DrinkService.getCategory(category);
        if (drinks.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No drinks found in this category");
        } else {
            return ResponseEntity.ok(drinks);
        }
    }

    

    @PostMapping("/addDrink")
    public ResponseEntity<?> addDrink(@RequestBody List<Drink> drink){
        try {
            for (Drink d : drink){
                DrinkService.addDrink(d);
            }
            return ResponseEntity.ok("Drink added successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add drink");
        }
    }

}
