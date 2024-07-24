package com.example.stockprice.model;

import lombok.Data;

import java.util.List;

@Data
public class TpexData {

    private String stkNo;

    private String stkName;

    private Boolean showListPriceNote;

    private Boolean showListPriceLink;

    private String reportDate;

    private Integer iTotalRecords;

    List<List<String>> aaData;

}
