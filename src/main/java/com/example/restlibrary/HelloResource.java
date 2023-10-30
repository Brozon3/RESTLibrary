package com.example.restlibrary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Path("/books")
public class HelloResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {


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
        }

        GenericEntity<ArrayList<Book>> entityList = new GenericEntity<>(bookList) {};
        return Response.ok(entityList).build();
    }


}