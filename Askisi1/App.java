package com.marinakipourou.maven.seismoi;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import com.github.davidmoten.grumpy.core.Position;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;

import com.opencsv.CSVReader;


/**
 * Kipourou Marina 1859
 *
 */
public class App 
{
	private static final Point ioannina = Geometries.point(20.853746600000022, 39.66502880000001);
	
	public static void main( String[] args ) throws IOException{
    	
		
    	CSVReader reader = new CSVReader(new FileReader("C:/Users/MARINA/Desktop/workspace/seismoi/finalcat.csv"), ';', '"', 11);
    	List<String[]> myEntries = reader.readAll();
        reader.close();
        
        RTree<String, Point> tree = RTree.minChildren(3).maxChildren(8).create();
       // RTree<String, Point> tree = RTree.star().maxChildren(4).create();
        RTree<String, Point> earthquakesTree = addEntries(myEntries, tree);
       
      // System.out.println("TREE AS STRING IS:" + earthquakesTree.asString());
      // System.out.println("TREE SIZE IS:" + earthquakesTree.size());
        
        final double distanceInKm = 5;
        earthquakesTree = earthquakesTree.add("Ioannina", ioannina);
        
        long startTime = System.currentTimeMillis();
        List<Entry<String, Point>> searchResults = search(earthquakesTree, ioannina, distanceInKm).toList().toBlocking().single();
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime); 
       
        System.out.println("SEARCH RESULTS");
        System.out.println(Arrays.toString(searchResults.toArray()));
        System.out.println("Duration of search is milliseconds is: " +duration);
           
       // earthquakesTree.visualize(900,900).save("C:/Users/MARINA/Desktop/workspace/seismoi/mytree.png");   
        
    }

	private static RTree<String, Point> addEntries(List<String[]> myEntries, RTree<String, Point> tree) {
		for(String[] entry: myEntries){
      		double lat=Double.parseDouble(entry[4]);
            double lon=Double.parseDouble(entry[5]);
            Point point = Geometries.point(lon, lat);   
            tree = tree.add("entry", point);

        }
		return tree;  
	}

	public static <T> Observable<Entry<String,Point>> search(RTree<String, Point> earthquakesTree, Point lonLat, final double distanceKm) {
        final Position from = Position.create(lonLat.y(), lonLat.x());
        Rectangle bounds = createBounds(from, distanceKm);
       
        return earthquakesTree.search(bounds).filter(new Func1<Entry<String, Point>, Boolean>() {
                    public Boolean call(Entry<String, Point> entry) {
                        Point point = entry.geometry();
                        Position position = Position.create(point.y(), point.x());
                        return from.getDistanceToKm(position) < distanceKm;
                    }
                });
    } 
		
	 private static Rectangle createBounds(final Position from, final double distanceKm) {
	        Position north = from.predict(distanceKm, 0);
	        Position south = from.predict(distanceKm, 180);
	        Position east = from.predict(distanceKm, 90);
	        Position west = from.predict(distanceKm, 270);

	        return Geometries.rectangle(west.getLon(), south.getLat(), east.getLon(), north.getLat());
	  }
	
	
}    
