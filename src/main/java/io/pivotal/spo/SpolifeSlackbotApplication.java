package io.pivotal.spo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "io.pivotal.spo"})
@EnableScheduling
public class SpolifeSlackbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpolifeSlackbotApplication.class, args);
	}
}
