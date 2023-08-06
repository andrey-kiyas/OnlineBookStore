package com.onlinebookstore.model;

import java.math.BigDecimal;

public class Book {
    private Long id;                //    id (Long, PK)
    private String title;           //    title (String, not null)
    private String author;          //    author (String, not null)
    private String isbn;            //    isbn (String, not null, unique)
    private BigDecimal price;       //    price (BigDecimal, not null)
    private BigDecimal description;
    private BigDecimal coverImage;
}
