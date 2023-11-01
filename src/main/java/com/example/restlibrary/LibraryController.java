package com.example.restlibrary;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.*;
import java.util.ArrayList;

@Path("/library")
public class LibraryController {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response welcomeMessage(){
        String welcomeMessage = "Welcome to my Library API. To see a list of books or authors already in the library, " +
                "you can use a GET method on /books or /authors. To add a book or an author to the library you can use " +
                "the same routes but use a POST method instead. To delete a book or an author, use a DELETE method and " +
                "/books/isbn or /authors/id. You can use the same routes as the DELETE method with a GET request to see" +
                "a specific book or author";
        return Response.ok(welcomeMessage).build();
    }

    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {

        ArrayList<Book> bookList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            String query = "SELECT * FROM titles";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                Book book = new Book(rs.getString(1), rs.getString(2),
                        rs.getInt(3), Integer.parseInt(rs.getString(4)));
                bookList.add(book);
            }

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to retrieve books from the database."
                    + e.getMessage()).build();
        }

        GenericEntity<ArrayList<Book>> entityList = new GenericEntity<>(bookList) {};
        return Response.ok(entityList).build();
    }

    @GET
    @Path("/book/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn){

        Book searchBook = null;

        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM titles WHERE isbn = (?)");
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                searchBook = new Book(rs.getString(1), rs.getString(2),
                        rs.getInt(3), Integer.parseInt(rs.getString(4)));
            }

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to find a book with that ISBN in the database."
                    + e.getMessage()).build();
        }

        return Response.ok(searchBook).build();
    }

    @POST
    @Path("/addbook")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(Book book){

        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO titles VALUES (?, ?, ?, ?)");
            pstmt.setString(1, book.ISBN);
            pstmt.setString(2, book.title);
            pstmt.setInt(3, book.editionNumber);
            pstmt.setString(4, Integer.toString(book.copyright));
            pstmt.executeQuery();

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to add that book to the database."
                    + e.getMessage()).build();
        }

        return Response.ok(book).build();
    }

    @PUT
    @Path("/modbook")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modBook(Book book){

        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("UPDATE titles " +
                    "SET isbn = (?), title = (?), editionNumber = (?), copyright = (?) WHERE isbn = (?)");
            pstmt.setString(1, book.ISBN);
            pstmt.setString(2, book.title);
            pstmt.setInt(3, book.editionNumber);
            pstmt.setString(4, Integer.toString(book.copyright));
            pstmt.setString(5, book.ISBN);
            pstmt.executeQuery();

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to edit that book in the database."
                    + e.getMessage()).build();
        }

        return Response.ok(book).build();
    }

    @DELETE
    @Path("/delbook/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delBook(@PathParam("isbn") String isbn){
        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM titles WHERE isbn = (?)");
            pstmt.setString(1, isbn);
            pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to delete that book from the database."
                    + e.getMessage()).build();
        }

        return Response.ok("The book with an ISBN of " + isbn + " was successfully deleted.").build();
    }

    @GET
    @Path("/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthors() {

        ArrayList<Author> authorList = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            String query = "SELECT * FROM authors";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()){
                Author author = new Author(rs.getInt(1), rs.getString(2),
                        rs.getString(3));
                authorList.add(author);
            }

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to retrieve authors from the database."
                    + e.getMessage()).build();
        }

        GenericEntity<ArrayList<Author>> entityList = new GenericEntity<>(authorList) {};
        return Response.ok(entityList).build();
    }

    @GET
    @Path("/author/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") String id){

        Author searchAuthor = null;

        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM authors WHERE authorID = (?)");
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                searchAuthor = new Author(rs.getInt(1), rs.getString(2),
                        rs.getString(3));
            }

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to find an author with that ID in the database."
                    + e.getMessage()).build();
        }

        return Response.ok(searchAuthor).build();
    }

    @POST
    @Path("/addauthor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAuthor(Author author){

        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO authors VALUES (?, ?, ?)");
            pstmt.setInt(1, author.authorID);
            pstmt.setString(2, author.firstName);
            pstmt.setString(3, author.lastName);
            pstmt.executeQuery();

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to add that author to the database."
                    + e.getMessage()).build();
        }

        return Response.ok(author).build();
    }

    @PUT
    @Path("/modauthor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modAuthor(Author author){

        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("UPDATE authors " +
                    "SET authorID = (?), firstName = (?), lastName = (?) WHERE authorID = (?)");
            pstmt.setInt(1, author.authorID);
            pstmt.setString(2, author.firstName);
            pstmt.setString(3, author.lastName);
            pstmt.setInt(4, author.authorID);
            pstmt.executeQuery();

        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to edit author in database." + e.getMessage()).build();
        }

        return Response.ok(author).build();
    }

    @DELETE
    @Path("/delauthor/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delAuthor(@PathParam("id") int id){
        try (Connection conn = DatabaseConnection.getDatabaseConnection()){
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM authors WHERE authorID = (?)");
            pstmt.setInt(1, id);
            pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.format("Error: %s\n%s", e.getCause(), e.getMessage());
            return Response.status(400, "Unable to delete that author from database."
                    + e.getMessage()).build();
        }

        return Response.ok("The author with an ID of " + id + " was successfully deleted.").build();
    }

}