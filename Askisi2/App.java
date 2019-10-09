package com.marinakipourou.maven.seismoi;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;

import rx.Observable;
import rx.functions.Func1;

import com.github.davidmoten.grumpy.core.Position;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.github.davidmoten.rtree.internal.util.PriorityQueue;
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
        
        List<Point> earthquakes = createEarthquakesList(myEntries);
        List<Point> DRandom = createRandomEarthquakes(earthquakes);
        
        long startTimeExhaustiveSearch = System.currentTimeMillis();
        List<Point> nearestNeighborsExhaustiveSearch = exhaustiveSearch(earthquakes, DRandom);
        long endTimeExhaustiveSearch = System.currentTimeMillis();
        long durationExhaustiveSearch = (endTimeExhaustiveSearch - startTimeExhaustiveSearch); 
        
       // System.out.println("Exhaustive Search Nearest Neighbors");
       // System.out.println(nearestNeighborsExhaustiveSearch.toString());
        System.out.println("Duration of Exhaustive Search for Nearest Neighbors in milliseconds is: " +durationExhaustiveSearch);
   
        
        RTree<String, Point> tree = RTree.star().maxChildren(4).create();
        RTree<String, Point> earthquakesTree = addEntries(myEntries, tree);
        long startTimeRTree = System.currentTimeMillis();
        ArrayList<List<Entry<String,Point>>> nearestNeighborsRTree = findNearestNeighbors(earthquakesTree, DRandom);
        long endTimeRTree = System.currentTimeMillis();
        long durationRTree = (endTimeRTree - startTimeRTree); 
        //System.out.println("RTree Nearest Neighbors");
        //System.out.println(nearestNeighborsRTree.toString());
        System.out.println("Duration of RTree Nearest Neighbors in milliseconds is: " +durationRTree);
        
    }


	private static List<Point> createEarthquakesList(List<String[]> myEntries) {
		List<Point> pointsList = new ArrayList<Point>();
		for(String[] entry: myEntries){
      		double lat=Double.parseDouble(entry[4]);
            double lon=Double.parseDouble(entry[5]);
            Point point = Geometries.point(lon, lat);
            pointsList.add(point);
		}
		return pointsList;
	}
	
	private static List<Point> createRandomEarthquakes(List<Point> earthquakes) {
		MersenneTwister randomNumbers = new MersenneTwister(new Date().getTime());
		List<Point> DRandom = new ArrayList<Point>();
		for(int i=0; i<5000; i++){ 
	        int entryNumber = randomNumbers.nextInt(earthquakes.size());
	        Point randomEarthquake = earthquakes.get(entryNumber);
	        DRandom.add(randomEarthquake);
        }
        return DRandom;
	}
	
	private static ArrayList<List<Entry<String,Point>>> findNearestNeighbors(RTree<String, Point> earthquakesTree, List<Point> DRandom) {
		List<Entry<String,Point>> nearestNeighborsForDRandom = new ArrayList<Entry<String,Point>>();
		ArrayList<List<Entry<String, Point>>> allNearestNeighbors = new ArrayList<List<Entry<String, Point>>>();
		double maxDistance = 10;
		int maxCount = 3;
		for(int i=0; i<DRandom.size(); i++){
			Point point = DRandom.get(i);
			nearestNeighborsForDRandom = earthquakesTree.nearest(point, maxDistance, maxCount+1).toList().toBlocking().single();
			allNearestNeighbors.add(nearestNeighborsForDRandom);
		}
		return allNearestNeighbors;
	}
	
	
	private static List<Point> exhaustiveSearch(List<Point> earthquakes, List<Point> DRandom) {
		List<Point> allNeighbors = new ArrayList<Point>();
		for(int i=0; i<DRandom.size(); i++){
			Point randomPoint = DRandom.get(i);
			for(int j=0; j<earthquakes.size(); j++){
				if(randomPoint != earthquakes.get(j)){
					Position randomPointPosition = Position.create(randomPoint.y(), randomPoint.x());
					Position earthquakePosition = Position.create(earthquakes.get(j).y(), earthquakes.get(j).x());
					double pointEarthquakeDistance = randomPointPosition.getDistanceToKm(earthquakePosition);
					if(pointEarthquakeDistance < 10){
						allNeighbors.add(earthquakes.get(j)); //gia to sugkekrimeno randomPoint
					}
					
				}
			}
		}
		
		return allNeighbors;
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

}    
