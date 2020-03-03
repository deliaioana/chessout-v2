package eu.chessout.v2.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import eu.chessout.shared.model.MyPayLoad;

// useful documentation: https://www.baeldung.com/spring-boot-json

@RestController
public class BasicApiController {

    @PutMapping("/api/gameResultUpdated")
    public String gameResultUpdated(@RequestBody MyPayLoad myPayLoad) {
        return "Greetings from gameResultUpdated";
    }
}
