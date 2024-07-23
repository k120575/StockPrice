package com.example.stockprice.controller;

import com.example.stockprice.entity.param.StockPriceParam;
import com.example.stockprice.model.StockPrice;
import com.example.stockprice.service.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/stockPrice")
public class StockPriceController {

    @Autowired
    private StockPriceService stockPriceService;

    @GetMapping(value = "/getSelfChooseStock")
    public List<StockPrice> getSelfChooseStock(String stockCode){
        return stockPriceService.getSelfChooseStock(stockCode);
    }

    @PostMapping(value = "/addSelfChooseStock")
    public void addSelfChooseStock(StockPriceParam param){
        stockPriceService.addSelfChooseStock(param);
    }

    @DeleteMapping(value = "/delSelfChooseStock")
    public void delSelfChooseStock(String stockCode){
        stockPriceService.delSelfChooseStock(stockCode);
    }
}
