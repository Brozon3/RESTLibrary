package com.example.restlibrary;

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

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Edition: " + editionNumber + "\n" +
                "Copyright: " + copyright + "\n" +
                "Authors: " + getAuthorList();
    }

    public String getISBN() { return ISBN; }

    public String getTitle() { return title; }

    public int getEditionNumber() { return editionNumber; }

    public int getCopyright() { return copyright; }

    public String getAuthorList() {
        if (this.authorList.isEmpty()){
            return "";
        } else {
            StringBuilder authors = new StringBuilder();
            for (Author a : this.authorList) {
                authors.append(a.getName()).append(", ");
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
