package eu.chessout.v2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import eu.chessout.shared.dao.BasicApiResponse;
import eu.chessout.shared.model.MyPayLoad;
import eu.chessout.v2.utils.MyAuth;

// useful documentation: https://www.baeldung.com/spring-boot-json

@RestController
public class BasicApiController {
    private static final Logger logger = Logger.getLogger(BasicApiController.class.getName());

    @PutMapping("/api/gameResultUpdated")
    public BasicApiResponse gameResultUpdated(@RequestBody MyPayLoad myPayLoad) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String stringPlayLoad = objectMapper.writeValueAsString(myPayLoad);

        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/api/gameResultUpdatedTask").payload(stringPlayLoad));
        System.out.println("Hello gameResultUpdated");

        BasicApiResponse basicApiResponse = new BasicApiResponse();
        basicApiResponse.isSuccessful = true;
        basicApiResponse.message = stringPlayLoad;
        return basicApiResponse;
    }

    @PostMapping(
            value = "/api/gameResultUpdatedTask"
            //consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public String gameResultUpdatedTask(@RequestBody String payLoadBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        MyPayLoad myPayLoad = objectMapper.readValue(payLoadBody, MyPayLoad.class);
        System.out.println("Hello gameResultUpdatedTask");
        logger.info("Decoded tournament id and round id and table id: "
                + myPayLoad.getTournamentId() + ", "
                + myPayLoad.getRoundId() + ", "
                + myPayLoad.getTableId());

        String accessToken = MyAuth.getAccessToken();
        logger.info("Access token: " + accessToken);

        return "Greetings from gameResultUpdatedTask";
    }
}
