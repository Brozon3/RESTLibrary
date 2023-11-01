package com.example.restlibrary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Author {
    public final int authorID;
    public final String firstName;
    public final String lastName;
    public final ArrayList<Book> bookList;

    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookList = new ArrayList<>();
    }

    public int getAuthorID() {
        return authorID;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }


    public String getBookList(){

        try (Connection conn = DatabaseConnection.getDatabaseConnection();){
            PreparedStatement authorStatement = conn.prepareStatement(
                    "SELECT * FROM authorisbn " +
                            "INNER JOIN titles ON authorisbn.isbn = titles.isbn " +
                            "WHERE authorID = (?)");
            authorStatement.setInt(1, this.authorID);
            ResultSet books = authorStatement.executeQuery();

            while (books.next()) {
                this.addBook(new Book(books.getString("titles.isbn"), books.getString("title"), books.getInt("editionNumber"), Integer.parseInt(books.getString("copyright"))));
            }
        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
        }

        if (bookList.isEmpty()){
            return "";
        } else {
            StringBuilder books = new StringBuilder();
            for (Book b : this.bookList) {
                books.append(b.getTitle()).append(", ");
            }
            return books.substring(0, books.length() - 2);
        }
    }

    public void addBook(Book book){
        boolean found = false;
        for (Book b: bookList){
            if (Objects.equals(b.getISBN(), book.getISBN())) {
                found = true;
                break;
            }
        }
        if (!found){
            bookList.add(book);
        }
    }

}
