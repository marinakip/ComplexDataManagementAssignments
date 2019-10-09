package stocks;

import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.fastdtw.timeseries.TimeSeries;
import net.sf.javaml.distance.fastdtw.dtw.FastDTW;

import com.opencsv.CSVReader;

/**
 * Kipourou Marina 1859
 *
 */

public class Stocks {

	public static void main(String[] args) throws IOException, ParseException {
		
		CSVReader JPMorganReader = new CSVReader(new FileReader("C:/Users/MARINA/Desktop/workspace/stocks/JPMorganChase.csv"), ';', '"', 10);
		List<String[]> JPMorganData = JPMorganReader.readAll();
		JPMorganReader.close();
		
		CSVReader GoldmanSachsReader = new CSVReader(new FileReader("C:/Users/MARINA/Desktop/workspace/stocks/GoldmanSachs.csv"), ';', '"', 10);
		List<String[]> GoldmanSachsData = GoldmanSachsReader.readAll();
		GoldmanSachsReader.close();
		
	    double[] JPMorganValues = parseData(JPMorganData);
	    double[] GoldmanSachsValues = parseData(GoldmanSachsData);
	    
	    Instance instanceJPMorgan = new DenseInstance(JPMorganValues);
	    Instance instanceGoldmanSachs = new DenseInstance(GoldmanSachsValues);
	    
	    TimeSeries timeSeriesJPMorgan = new TimeSeries(instanceJPMorgan);
	    TimeSeries timeSeriesGoldmanSachs = new TimeSeries(instanceGoldmanSachs);
	    
	    computeWarpingDistance(timeSeriesJPMorgan, timeSeriesGoldmanSachs, 1);
	    computeWarpingDistance(timeSeriesJPMorgan, timeSeriesGoldmanSachs, 100);
	    computeWarpingDistance(timeSeriesJPMorgan, timeSeriesGoldmanSachs, 500);
	    
	}

	private static double[] parseData(List<String[]> stockData) throws ParseException {
		List<Double> closingValues = new ArrayList<Double>();
		
		for (String[] entry : stockData) {
			StringBuilder entryString = new StringBuilder(entry[4]);
			//System.out.println("EntryStringBefore:"+entryString);
			
			if (entryString.length() == 11){
				entryString.deleteCharAt(7);
			}
			//System.out.println("EntryStringAfter:"+entryString);
			
			if(entryString.length() >= 3){
			    int last3rdPosition = entryString.length() - 3; 
			    if(entryString.charAt(last3rdPosition) == '.'){
			    	entryString.setCharAt(last3rdPosition, ',');
			    }else if (entryString.charAt(0) == '0' && entryString.charAt(1) == '.'){
			         entryString.setCharAt(1, ',');
			    }else if (entryString.charAt(0) == '-' && entryString.charAt(1) == '0' && entryString.charAt(2) == '.'){
			         entryString.setCharAt(2, ',');
			    }else if(entryString.charAt(3) == '.' ){
			    	 entryString.setCharAt(3, ',');
			    }
			}  
			
			//System.out.println("EntryStringAfter2:"+entryString);
			NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALIAN);
			Double parsedNumber = numberFormat.parse(entryString.toString()).doubleValue(); 
			//System.out.println("ParsedNumber: "+parsedNumber);
			closingValues.add(parsedNumber);
		}
		
		double[] values = new double[closingValues.size()];
		for(int i=0; i<values.length; i++){
			values[i] = closingValues.get(i);
		}
		
		return values;
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	private static void computeWarpingDistance(TimeSeries timeSeriesJPMorgan, TimeSeries timeSeriesGoldmanSachs, int radius) {
		FastDTW fastDTW = new FastDTW();	 
		long startTimeWarpingDistance = System.currentTimeMillis();
	    for(int i=0; i<100; i++){
			double warpingDistance = fastDTW.getWarpDistBetween(timeSeriesJPMorgan, timeSeriesGoldmanSachs, radius);
	    }
	    long endTimeWarpingDistance = System.currentTimeMillis();
        long durationWarpingDistance = (endTimeWarpingDistance - startTimeWarpingDistance); 
	    double warpingDistance = fastDTW.getWarpDistBetween(timeSeriesJPMorgan, timeSeriesGoldmanSachs, radius);
    	System.out.println("Warping Distance for radius " +radius+ ": " + warpingDistance);
	    System.out.println("Duration to execute Warping Distance 100 times for radius " +radius+ ": " + durationWarpingDistance +" ms");
		
	}

}
