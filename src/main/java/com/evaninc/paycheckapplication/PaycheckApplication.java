package com.evaninc.paycheckapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class PaycheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaycheckApplication.class, args);
	}
}
