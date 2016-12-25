package mr.z.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mr.z.model.ResponseObject;
import mr.z.model.Stock;

import com.aliyun.api.gateway.demo.Client;
import com.aliyun.api.gateway.demo.Request;
import com.aliyun.api.gateway.demo.Response;
import com.aliyun.api.gateway.demo.constant.HttpHeader;
import com.aliyun.api.gateway.demo.enums.Method;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockServiceImpl implements StockServiceApi {
	String app_key = "23485133"; // APP KEY
	String app_secret = "d359046dc1fd52a59e4bdb16c0b09fdf";// APP密钥
	int connectTimeout = 10000;// 3秒
	int readTimeout = 15000;// 15秒
	String url = "http://ali-stock.showapi.com/sz-sh-stock-history";
	
	@Override
	public List<Stock> getStockHistoryPrice(String stockCode, String startDate,
			String endDate) {

		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeader.HTTP_HEADER_ACCEPT, "application/json");
		int ind=url.lastIndexOf("/");
		String host=url.substring(0,ind);
		String path=url.substring(ind);
		Response response = null;
		Request request = new Request(Method.GET, host,path, app_key, app_secret, connectTimeout);
		Map <String, String>textMap = new HashMap<String, String>();
		textMap.put("code", stockCode);
		textMap.put("begin", startDate);
		textMap.put("end", endDate);
		request.setQuerys(textMap);
		request.setHeaders(headers);
		List<Stock> stocks = new ArrayList<Stock>();
		try {
			response =  Client.execute(request);
			byte b[] = response.getContent_byte();
			String str = new String(b, "utf-8");
			ObjectMapper mapper = new ObjectMapper(); 
			ResponseObject responseObject = mapper.readValue(str, mr.z.model.ResponseObject.class);
			if((responseObject != null && responseObject.getShowapi_res_body()!=null)){
				stocks = responseObject.getShowapi_res_body().getList();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
		return stocks;
	}

	@Override
	public Stock getStockCurrentPrice(String stockCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
