package com.example.carshowroom.Controller;

import com.example.carshowroom.Models.Bookings;
import com.example.carshowroom.Models.Cars;
import com.example.carshowroom.Models.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hi")
    public String hello() {
        String hello = "Hello";
        return hello;
    }
    private static final String USERS_FILE_PATH = "C:\\Users\\prajw\\.vscode\\Nisarga's Project\\carshowrrom\\carshowroom\\src\\main\\java\\com\\example\\carshowroom\\Models\\users.json";
    private static final String CARS_FILE_PATH = "C:\\Users\\prajw\\.vscode\\Nisarga's Project\\carshowrrom\\carshowroom\\src\\main\\java\\com\\example\\carshowroom\\Models\\cars.json";
    private static final String BOOKINGS_FILE_PATH = "C:\\Users\\prajw\\.vscode\\Nisarga's Project\\carshowrrom\\carshowroom\\src\\main\\java\\com\\example\\carshowroom\\Models\\bookings.json";

    //---------------------------------------------Login and Signup APIs Start-------------------------------------------//
    private final List<Users> users = new ArrayList<>();

    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody Users newUser) {
        try {
            // Load existing users from the JSON file
            loadUsers();

            // Set the ID for the new user
            Long nextId = getNextUserId();
            newUser.setId(nextId);

            // Add the new user to the list
            users.add(newUser);

            // Save the updated user list to the JSON file
            saveUsers();

            // Create a response payload with the userID and username
            // You can customize the response structure as per your requirements
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("userId", newUser.getId());
            responseBody.put("username", newUser.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during signup");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Users loginUser) {
        try {
            // Load existing users from the JSON file
            loadUsers();

            // Find the user with matching username and password
            for (Users user : users) {
                if (user.getUsername().equals(loginUser.getUsername()) && user.getPassword().equals(Users.hashPassword(loginUser.getPassword()))) {
                    // Create a response payload with the userID and username
                    // You can customize the response structure as per your requirements
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("userId", user.getId());
                    responseBody.put("username", user.getUsername());

                    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login");
        }
    }
    // Helper method to load users from the JSON file
    private void loadUsers() throws IOException {
        File file = new File(USERS_FILE_PATH);

        // If the file exists, load the users
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            users.clear();
            users.addAll(mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Users.class)));
        }
    }

    // Helper method to save users to the JSON file
    private void saveUsers() throws IOException {
        File file = new File(USERS_FILE_PATH);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, users);
    }

    // Helper method to generate the next available user ID
    private Long getNextUserId() {
        Long maxId = 0L;
        for (Users user : users) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        return maxId + 1;
    }

//---------------------------------------------Login and Signup APIs Finish-------------------------------------------//

    @PostMapping("/add-car")
    public ResponseEntity<Object> createCar(@RequestBody Cars newCar) {
        try {
            // Load existing cars from the JSON file
            loadCars();

            // Set the ID for the new car
            Long nextId = getNextCarId();
            newCar.setId(nextId);

            // Add the new car to the list
            cars.add(newCar);

            // Save the updated car list to the JSON file
            saveCars();

            // Create a response payload with the car ID
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("carId", newCar.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during car creation");
        }
    }

    private final List<Cars> cars = new ArrayList<>();

    // Helper method to load cars from the JSON file
    private void loadCars() throws IOException {
        File file = new File(CARS_FILE_PATH);

        // If the file exists, load the cars
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            cars.clear();
            cars.addAll(mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Cars.class)));
        }
    }

    // Helper method to save cars to the JSON file
    private void saveCars() throws IOException {
        File file = new File(CARS_FILE_PATH);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, cars);
    }

    // Helper method to generate the next available car ID
    private Long getNextCarId() {
        Long maxId = 0L;
        for (Cars car : cars) {
            if (car.getId() > maxId) {
                maxId = car.getId();
            }
        }
        return maxId + 1;
    }


    @GetMapping("/cars/{modelNumber}")
    public ResponseEntity<Object> getCarDetails(@PathVariable Long modelNumber) {
        try {
            // Load existing cars from the JSON file
            loadCars();

            // Find the car with matching model number
            for (Cars car : cars) {
                Long carModel = car.getModel();
                if (carModel != null && carModel.equals(modelNumber)) {
                    // Create a response payload with the car details
                    // You can customize the response structure as per your requirements
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("carId", car.getId());
                    responseBody.put("name", car.getName());
                    responseBody.put("description", car.getDescription());
                    responseBody.put("model", carModel);
                    responseBody.put("price", car.getPrice());
                    responseBody.put("imageUrl", car.getImageUrl());

                    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching car details");
        }
    }


    @PostMapping("/update-car")
    public ResponseEntity<Object> updateCar(@RequestBody Cars updatedCar) {
        try {
            // Load existing cars from the JSON file
            loadCars();

            // Find the car with matching model number
            for (Cars car : cars) {
                Long carModel = car.getModel();
                if (carModel.equals(updatedCar.getModel())) {
                    // Update the car details
                    car.setName(updatedCar.getName());
                    car.setDescription(updatedCar.getDescription());
                    car.setPrice(updatedCar.getPrice());
                    car.setImageUrl(updatedCar.getImageUrl());

                    // Save the updated car list to the JSON file
                    saveCars();

                    // Create a response payload with the updated car details
                    // You can customize the response structure as per your requirements
                    Map<String, Object> responseBody = new HashMap<>();
                    responseBody.put("carId", car.getId());
                    responseBody.put("name", car.getName());
                    responseBody.put("description", car.getDescription());
                    responseBody.put("model", car.getModel());
                    responseBody.put("price", car.getPrice());
                    responseBody.put("imageUrl", car.getImageUrl());

                    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating car details");
        }
    }

    @DeleteMapping("/delete-car/{modelNumber}")
    public ResponseEntity<Object> deleteCar(@PathVariable Long modelNumber) {
        try {
            // Load existing cars from the JSON file
            loadCars();

            // Find the car with matching model number
            for (Cars car : cars) {
                Long carModel = car.getModel();
                if (carModel != null && carModel.equals(modelNumber)) {
                    // Remove the car from the list
                    cars.remove(car);

                    // Save the updated car list to the JSON file
                    saveCars();

                    return ResponseEntity.status(HttpStatus.OK).body("Car deleted successfully");
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting car");
        }
    }


    @GetMapping("/getcars")
    public ResponseEntity<List<Cars>> getAllCars() {
        try {
            // Load existing cars from the JSON file
            loadCars();

            return ResponseEntity.ok(cars);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/bookings/add")
    public ResponseEntity<Object> addBooking(@RequestBody Bookings newBooking) {
        try {
            // Load existing bookings from the JSON file
            loadBookings();

            // Set the ID for the new booking
            Long nextId = getNextBookingId();
            newBooking.setId(nextId);

            // Add the new booking to the list
            bookings.add(newBooking);

            // Save the updated booking list to the JSON file
            saveBookings();

            // Create a response payload with the booking ID
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("bookingId", newBooking.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while adding the booking");
        }
    }

    private final List<Bookings> bookings = new ArrayList<>();
    private void loadBookings() throws IOException {
        File file = new File(BOOKINGS_FILE_PATH);

        // If the file exists, load the bookings
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            bookings.clear();
            bookings.addAll(mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, Bookings.class)));
        }
    }

    private void saveBookings() throws IOException {
        File file = new File(BOOKINGS_FILE_PATH);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, bookings);
    }

    private Long getNextBookingId() {
        Long maxId = 0L;
        for (Bookings booking : bookings) {
            if (booking.getId() > maxId) {
                maxId = booking.getId();
            }
        }
        return maxId + 1;
    }


    @GetMapping("/bookings")
    public ResponseEntity<List<Bookings>> getAllBookings() {
        try {
            // Load existing bookings from the JSON file
            loadBookings();

            // Return the list of bookings
            return ResponseEntity.ok(bookings);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
