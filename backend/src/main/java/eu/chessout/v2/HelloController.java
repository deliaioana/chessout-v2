package eu.chessout.v2;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import javax.inject.Inject;

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

        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);

        // ...
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());
        return "Update time = " + result.get().getUpdateTime();
    }
}