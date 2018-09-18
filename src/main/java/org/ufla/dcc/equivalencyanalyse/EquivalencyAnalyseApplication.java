package org.ufla.dcc.equivalencyanalyse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EquivalencyAnalyseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EquivalencyAnalyseApplication.class, args);
	}

}