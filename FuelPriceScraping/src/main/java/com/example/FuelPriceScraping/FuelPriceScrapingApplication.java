package com.example.FuelPriceScraping;

import com.example.FuelPriceScraping.service.FuelPriceService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FuelPriceScrapingApplication implements ApplicationRunner {


	private final FuelPriceService fuelPriceService;

	public FuelPriceScrapingApplication(FuelPriceService fuelPriceService) {
		this.fuelPriceService = fuelPriceService;
	}



	public static void main(String[] args) {
		SpringApplication.run(FuelPriceScrapingApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
      fuelPriceService.getMainPage();
	}
}
