package com.example.stockprice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.stockprice.entity.param.StockPriceParam;
import com.example.stockprice.model.StockPrice;
import com.example.stockprice.model.TwseData;
import com.example.stockprice.repository.StockPriceRepository;
import com.example.stockprice.service.StockPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class StockPriceServiceImpl implements StockPriceService {

    @Autowired
    private StockPriceRepository stockPriceRepository;

    @Override
    public BigDecimal calculateReturnRate(String stockCode) {
        String url = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=&stockNo=" + stockCode;
        String result = null;
        try {
            result = HttpUtil.get(url);
        } catch (Exception e){
            log.info("抓取股票資料出錯:{}", e.getMessage(), e);
        }

        if (Objects.nonNull(result)){


            JSONObject jsonObject = JSONUtil.parseObj(result);
            TwseData twseData = jsonObject.toBean(TwseData.class);
            List<String> stockDataList = twseData.getData().get(twseData.getData().size() - 1);
        }

        return null;
    }

    @Override
    public List<StockPrice> getSelfChooseStock(String stockCode) {
        return stockPriceRepository.findByStockCode(stockCode);
    }

    @Override
    public void addSelfChooseStock(StockPriceParam param) {
        List<StockPrice> stockPrice = stockPriceRepository.findByStockCode(param.getStockCode());

        if (CollUtil.isEmpty(stockPrice)) {
            StockPrice newSelfChoose = new StockPrice();
            newSelfChoose.setStockCode(param.getStockCode());
            newSelfChoose.setStockName(param.getStockName());
            newSelfChoose.setPrice(param.getPrice());
            newSelfChoose.setCreatedDate(new Date());
            stockPriceRepository.saveAndFlush(newSelfChoose);
        }
    }

    @Override
    public void delSelfChooseStock(String stockCode) {
        List<StockPrice> stockPrice = stockPriceRepository.findByStockCode(stockCode);
        if (CollUtil.isNotEmpty(stockPrice)){
            stockPriceRepository.delete(stockPrice.get(0));
        }
    }
}
