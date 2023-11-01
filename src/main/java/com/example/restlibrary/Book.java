package com.example.restlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Book {
    public final String ISBN;
    public final String title;
    public final int editionNumber;
    public final int copyright;
    public final ArrayList<Author> authorList;

    public Book(String ISBN, String title, int editionNumber, int copyright) {
        this.ISBN = ISBN;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<>();
    }

    public String getISBN() { return ISBN; }

    public String getTitle() { return title; }

    public int getEditionNumber() { return editionNumber; }

    public int getCopyright() { return copyright; }

    public String getAuthorList(){

        try (Connection conn = DatabaseConnection.getDatabaseConnection();){
            PreparedStatement authorStatement = conn.prepareStatement(
                    "SELECT * FROM authorisbn " +
                            "INNER JOIN authors ON authorisbn.authorID = authors.authorID " +
                            "WHERE isbn = (?)");
            authorStatement.setString(1, this.ISBN);
            ResultSet authors = authorStatement.executeQuery();

            while (authors.next()) {
                this.addAuthor(new Author(authors.getInt("authorID"), authors.getString("firstName"), authors.getString("lastName")));
            }
        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
        }

        if (this.authorList.isEmpty()){
            return "";
        } else {
            StringBuilder authors = new StringBuilder();
            for (Author a : this.authorList) {
                authors.append(a.getFirstName()).append(" ").append(a.getLastName()).append(", ");
            }
            return authors.substring(0, authors.length() - 2);
        }
    }


    public void addAuthor(Author author){
        boolean found = false;
        for (Author a: this.authorList){
            if (a.getAuthorID() == author.getAuthorID()) {
                found = true;
                break;
            }
        }
        if (!found){
            this.authorList.add(author);
        }
    }
}
