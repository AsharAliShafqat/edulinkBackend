package com.shad.edulink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.shad.edulink")
public class EdulinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdulinkApplication.class, args);
	}

}
