package com.example.stockprice.model;

import lombok.Data;

import java.util.List;

@Data
public class TwseData {

    private String stat;

    private String date;

    private String title;

    List<String> fields;

    List<List<String>> data;

    List<String> notes;

    Integer total;
}
