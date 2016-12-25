package mr.z;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import mr.z.model.Stock;
import mr.z.model.ValueStock;
import mr.z.service.StockServiceApi;
import mr.z.service.StockServiceImpl;

import com.aliyun.api.gateway.demo.Client;
import com.aliyun.api.gateway.demo.Request;
import com.aliyun.api.gateway.demo.Response;

public class Selector {
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception{
		Map<String, List<Stock>> info = new HashMap<>();
		List<ValueStock> result = new ArrayList<>();
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		StockServiceApi api = new StockServiceImpl();
		// load a properties file
		prop.load(input);
		String monitorList = prop.getProperty("MonitorList");
		String[] interest = monitorList.split(",");
		String startDate = prop.getProperty("Date_start");
		String endDate = prop.getProperty("Date_end");
		if(interest.length>0){
			for(int i=0;i<interest.length;i++){
				List<Stock> history = api.getStockHistoryPrice(interest[i], startDate, endDate);
				if(history.size()>0){
					info.put(interest[i], history);
				}
			}
		}
		for(Entry<String, List<Stock>> entry : info.entrySet()){
			result.add(new ValueStock(entry.getKey(), calculate(info.get(entry.getKey()))));
		}
		Collections.sort(result, new Comparator<ValueStock>(){

			@Override
			public int compare(ValueStock stock1, ValueStock stock2) {
				if(stock1.getValue()>= stock2.getValue()){
					return 1;
				}
				return 0;
			}
			
		});
		for(ValueStock stock: result){
			System.out.println("ID: "+stock.getCode()+" value: "+stock.getValue()+"\n");
		}
		
	}


	private static int calculate(List<Stock> list) {
		int count = list.size();
		int grade = 50;
		for(int i= 0; i<list.size()-1;i++){
			if(Float.parseFloat(list.get(i).getClose_price())<=Float.parseFloat(list.get(i+1).getClose_price())){
				grade++;
			}else{
				grade--;
			}
		}
		return grade;
	}


	private static List<Stock> parseResponse(Response response) {
		// TODO Auto-generated method stub
		return null;
	}

	private static List<Stock> Analisis(List<List<Stock>> stocks) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private static List<Response> executeRequests(List<Request> requests) throws Exception {
		List<Response> responses  = new ArrayList<Response>();
		for(Request request : requests){
			responses.add(Client.execute(request));
		}
		return responses;
	}


}
