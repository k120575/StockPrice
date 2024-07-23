package com.example.stockprice.repository;

import com.example.stockprice.model.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {

    @Query(value = "select s from StockPrice s where (:stockCode is null or s.stockCode = :stockCode)")
    List<StockPrice> findByStockCode(String stockCode);
}
