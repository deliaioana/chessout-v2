package eu.chessout.v2.model;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    Long id;

    String title;

    String author;

    int year;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + this.id +
                ", title='" + this.title + '\'' +
                ", author='" + this.author + '\'' +
                ", year=" + this.year +
                '}';
    }


    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}