package com.example.stockprice.entity.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StockPriceParam {

    private String stockCode;

    private String stockName;

    private BigDecimal price;
}
