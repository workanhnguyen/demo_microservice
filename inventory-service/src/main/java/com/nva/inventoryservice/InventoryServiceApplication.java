package com.nva.inventoryservice;

import com.nva.inventoryservice.models.Inventory;
import com.nva.inventoryservice.repositories.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory1 = Inventory.builder()
					.skuCode("iphone_15")
					.quantity(100)
					.build();
			Inventory inventory2 = Inventory.builder()
					.skuCode("iphone_16")
					.quantity(0)
					.build();
			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
		};
	}
}
