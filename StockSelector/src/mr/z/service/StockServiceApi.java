package mr.z.service;

import java.util.List;

import mr.z.model.Stock;

public interface StockServiceApi {
	public List<Stock> getStockHistoryPrice(String stockCode, String startDate, String endDate);
	public Stock getStockCurrentPrice(String stockCode);
	
}
