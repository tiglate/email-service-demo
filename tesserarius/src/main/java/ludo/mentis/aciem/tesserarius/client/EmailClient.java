package ludo.mentis.aciem.tesserarius.client;

import ludo.mentis.aciem.tesserarius.config.EmailClientConfig;
import ludo.mentis.aciem.tesserarius.model.MessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tabellarius", configuration = EmailClientConfig.class)
public interface EmailClient {

    @PostMapping("/api/v1/email/send")
    ResponseEntity<String> send(@RequestBody MessageDTO messageDTO);
}
