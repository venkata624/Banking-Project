package com.example.demo;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title="The Bank App",
				description = "Backend Rest API'S for TJA Bank...",
				version="v1.0",
				contact = @Contact(
						name="Guna",
						email="trigunakar143@gmail.com",
						url = "https://github.com/Guna-24-Goru/Junit-Mockito-Demo"
				),
				license=@License(
						name ="The TJA Bank",
						url=" https://github.com/Guna-24-Goru/Junit-Mockito-Demo"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The Bank APP",
				url="https://github.com/Guna-24-Goru/Junit-Mockito-Demo"
		)
)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
