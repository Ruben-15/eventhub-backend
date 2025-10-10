package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // 🛑 CRITICAL IMPORT ADDED

@SpringBootApplication
// 🛑 CRITICAL ANNOTATION: Tells Spring to scan for components (Controllers, Repositories, Configs)
// in all sub-packages starting from com.example.demo.
@ComponentScan(basePackages = "com.example.demo") 
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}