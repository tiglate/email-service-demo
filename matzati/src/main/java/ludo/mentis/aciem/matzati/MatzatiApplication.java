package ludo.mentis.aciem.matzati;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MatzatiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatzatiApplication.class, args);
	}

}
