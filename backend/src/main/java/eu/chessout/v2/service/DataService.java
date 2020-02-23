package eu.chessout.v2.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

import eu.chessout.v2.model.Book;

@Service
public class DataService {

    @Value("${server.projectId}")
    private String projectId;

    private Firestore db;

    public Book getBook() {
        Book book = new Book(
                "data-book" + (new Date()).toString(), "bogdan + " + projectId, 2020);
        return book;
    }

    public Firestore getFirestoreService() {
        if (null == db) {
            FirestoreOptions firestoreOptions =
                    FirestoreOptions.getDefaultInstance().toBuilder()
                            .setProjectId(projectId)
                            .build();
            Firestore firestore = firestoreOptions.getService();
            db = firestore;
        }
        return db;
    }
}
