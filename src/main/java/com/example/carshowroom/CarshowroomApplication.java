package com.example.carshowroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
public class CarshowroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarshowroomApplication.class, args);
	}

}
