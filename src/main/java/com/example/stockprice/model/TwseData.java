package com.example.stockprice.model;

import lombok.Data;

import java.util.List;

@Data
public class TwseData {

    private String stat;

    private String date;

    private String title;

    private List<String> fields;

    private List<List<String>> data;

    private List<String> notes;

    private Integer total;
}
