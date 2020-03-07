package eu.chessout.v2;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.inject.Inject;

import eu.chessout.shared.dao.BasicApiResponse;
import eu.chessout.shared.model.MyPayLoad;
import eu.chessout.v2.model.Book;
import eu.chessout.v2.service.DataService;

@RestController
public class HelloController {

    private static Logger logger = Logger.getLogger(HelloController.class.getName());

    @Inject
    private DataService dataService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/testBookCreateReadAndReturn")
    public @ResponseBody
    Book testBookCreateReadAndReturn() {
        Date date = new Date();
        Book demoBook = new Book("demoBook " + date.toString(), "cezar", 2020);

        return demoBook;
    }

    @GetMapping("/testInjectedService")
    public @ResponseBody
    Book testInjectedService() {

        return dataService.getBook();
    }

    @GetMapping("/quickstartServer")
    public @ResponseBody
    String quickstartServer() throws ExecutionException, InterruptedException {


        DocumentReference docRef = dataService.getFirestoreService()
                .collection("users").document("alovelace");
        // Add document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);
        data.put("altTimeStamp", (new Date().getTime()));

        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);

        // ...
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
        return "Update time = " + result.get().getUpdateTime();
    }


    @PutMapping("/sendNotification")
    public BasicApiResponse sendNotificationToDevice(@RequestBody MyPayLoad myPayLoad) throws JsonProcessingException, FirebaseMessagingException {

        String registrationToken = myPayLoad.getGameLocation();

        Message message = Message.builder()
                .putData("hello", "Hello from server")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .build();

        String responseFirebase = FirebaseMessaging.getInstance().send(message);

        ObjectMapper objectMapper = new ObjectMapper();
        String stringPlayLoad = objectMapper.writeValueAsString(myPayLoad);
        BasicApiResponse response = BasicApiResponse.message(responseFirebase + " -> " + stringPlayLoad);
        return response;
    }

    @GetMapping("/_ah/warmup")
    public void wormup() {

    }
}