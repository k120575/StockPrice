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
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class StockPriceServiceImpl implements StockPriceService {

    @Autowired
    private StockPriceRepository stockPriceRepository;

    @Override
    public String calculateReturnRate() {
        List<StockPrice> selfChooseStock = getSelfChooseStock(null);
        if (CollUtil.isNotEmpty(selfChooseStock)) {
            BigDecimal totalReturnRate = BigDecimal.ZERO;
            for (StockPrice stock : selfChooseStock){
                String url = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=&stockNo=" + stock.getStockCode();
                String result = null;
                try {
                    result = HttpUtil.get(url);
                } catch (Exception e) {
                    log.info("抓取股票資料出錯:{}", e.getMessage(), e);
                }

                if (Objects.nonNull(result)) {
                    JSONObject jsonObject = JSONUtil.parseObj(result);
                    TwseData twseData = jsonObject.toBean(TwseData.class);
                    List<String> stockDataList = twseData.getData().get(twseData.getData().size() - 1);
                    BigDecimal closePrice = new BigDecimal(stockDataList.get(6));
                    BigDecimal originPrice = stock.getPrice();
                    BigDecimal returnRate = (closePrice.subtract(originPrice)).divide(originPrice, RoundingMode.HALF_UP);
                    totalReturnRate = totalReturnRate.add(returnRate);
                    log.info("投報率計算 股票代碼:{}, 股票名稱:{}, 初始價格:{}, 今日收盤價:{}, 投報率:{}", stock.getStockCode(), stock.getStockName(), originPrice, closePrice, returnRate);
                }
            }
            return totalReturnRate.divide(BigDecimal.valueOf(selfChooseStock.size()), RoundingMode.HALF_UP).multiply(new BigDecimal(100)) + "%";
        }
        return BigDecimal.ZERO.stripTrailingZeros().toPlainString() + "%";
    }

    @Override
    public List<StockPrice> getSelfChooseStock(String stockCode) {
        return stockPriceRepository.findByStockCode(stockCode);
    }

    @Override
    public void addSelfChooseStock(StockPriceParam param) throws Exception {
        String url = "https://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=&stockNo=" + param.getStockCode();
        String result = null;
        try {
            result = HttpUtil.get(url);
        } catch (Exception e) {
            log.info("抓取股票資料出錯:{}", e.getMessage(), e);
            throw new Exception("抓取股票資料出錯");
        }

        BigDecimal closePrice = BigDecimal.ZERO;
        if (Objects.nonNull(result)){
            JSONObject jsonObject = JSONUtil.parseObj(result);
            TwseData twseData = jsonObject.toBean(TwseData.class);
            List<String> stockDataList = twseData.getData().get(twseData.getData().size() - 1);
            closePrice = new BigDecimal(stockDataList.get(6));
        }

        List<StockPrice> stockPriceList = stockPriceRepository.findByStockCode(param.getStockCode());
        if (CollUtil.isEmpty(stockPriceList)) {
            StockPrice newSelfChoose = new StockPrice();
            newSelfChoose.setStockCode(param.getStockCode().trim());
            newSelfChoose.setStockName(param.getStockName().trim());
            newSelfChoose.setPrice(closePrice);
            newSelfChoose.setCreatedDate(new Date());
            stockPriceRepository.saveAndFlush(newSelfChoose);
        } else {
            StockPrice stockPrice = stockPriceList.get(0);
            stockPrice.setPrice(closePrice);
            stockPriceRepository.saveAndFlush(stockPrice);
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
