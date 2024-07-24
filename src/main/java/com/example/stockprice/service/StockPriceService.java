package com.example.stockprice.service;

import com.example.stockprice.entity.param.StockPriceParam;
import com.example.stockprice.model.StockPrice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

public interface StockPriceService {

    List<StockPrice> getSelfChooseStock(String stockCode);

    void addSelfChooseStock(StockPriceParam param) throws Exception;

    void delSelfChooseStock(String stockCode);

    String calculateReturnRate();
}
