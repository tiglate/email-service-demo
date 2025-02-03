package ludo.mentis.aciem.tesserarius;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TesserariusApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesserariusApplication.class, args);
    }

}
