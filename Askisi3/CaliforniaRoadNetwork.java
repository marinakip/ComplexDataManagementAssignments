import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;

/**
 * Kipourou Marina 1859
 *
 */


public class CaliforniaRoadNetwork {
	private static List<Node> nodes;
	private static List<Edge> edges;
	private static List<Node> visitedNodes;
    private static List<Node> notVisitedNodes;
    private static HashMap<Node, Node> previousNodes;
    private static HashMap<Node, Double> distance;
    private static AdjacencyList adjacencyList;

	public static void main(String[] args) throws IOException {
		CSVReader nodesReader = new CSVReader(new FileReader("C:/Users/MARINA/Desktop/workspace/californiaRoadNetwork/CaliforniaRoadNetworkNodes.csv"), ';', '"');
		List<String[]> nodesData = nodesReader.readAll();
        nodesReader.close();
        
        CSVReader edgesReader = new CSVReader(new FileReader("C:/Users/MARINA/Desktop/workspace/californiaRoadNetwork/CaliforniaRoadNetworkEdges.csv"), ';', '"');
		List<String[]> edgesData = edgesReader.readAll();
        edgesReader.close();
        
        nodes = createNodesList(nodesData);
        edges = createEdgesList(edgesData);
        
       // System.out.println(nodes.toString());
       // System.out.println(edges.toString());
        
        adjacencyList = createAdjacencyList(); 
      //adjacencyList.printAdjacencyList();

       System.out.println("Please give identifier for source and target nodes to compute Dijkstra Algorithm");
       System.out.print(" Source nodeID: ");
       Scanner scannerSourceNodeId = new Scanner(System.in);
       int sourceNodeId = scannerSourceNodeId.nextInt();
       System.out.print(" Target nodeID: ");
       Scanner scannerTargetNodeId = new Scanner(System.in);
       int targetNodeId = scannerTargetNodeId.nextInt();    
       scannerSourceNodeId.close();
       scannerTargetNodeId.close();
       
       LinkedList<Node> path = computeDijkstra(sourceNodeId, targetNodeId);
       
       for (Node node : path) {
    	   System.out.println("PATH IS:");
    	   System.out.println(node.getNodeId());
       }

        
	}


	private static LinkedList<Node> computeDijkstra(int sourceNodeId, int targetNodeId) {
		distance = new HashMap<Node, Double>();
		notVisitedNodes = new ArrayList<Node>();
		visitedNodes = new ArrayList<Node>();
		Node sourceNode = nodes.get(sourceNodeId);
		Node targetNode = nodes.get(targetNodeId);  
        distance.put(sourceNode, 0.0);
        notVisitedNodes.add(sourceNode);
        while (notVisitedNodes.size() > 0) {
                Node node = getMinNode(notVisitedNodes);
                visitedNodes.add(node);
                notVisitedNodes.remove(node);
                computeMinDistances(node);
        }
        LinkedList<Node> path = getPath(targetNode);
        return path;
		
	}


	private static LinkedList<Node> getPath(Node targetNode) {
		 previousNodes = new HashMap<Node, Node>();
		 LinkedList<Node> path = new LinkedList<Node>();
         Node step = targetNode;
         if (previousNodes.get(step) == null) {
                 return null;
         }
         path.add(step);
         while (previousNodes.get(step) != null) {
                 step = previousNodes.get(step);
                 path.add(step);
         }
         Collections.reverse(path);
         return path;
	}


	private static void computeMinDistances(Node node) {
		 	 previousNodes = new HashMap<Node, Node>();
        	 for(int i=0; i<adjacencyList.getEdgesSizeFromNode(node.getNodeId()); i++){
        		 int targetId = adjacencyList.getEdgesListFromNode(node.getNodeId()).get(i).getInt();
        		 Node target = nodes.get(targetId);
        		 if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                     distance.put(target, getShortestDistance(node) + getDistance(node,target));
                     previousNodes.put(target, node);
                     notVisitedNodes.add(target);
        		 } 
        	 
        	 }     
         
		
	}

	private static Double getDistance(Node node, Node target) {
            for (Edge edge : edges) {
                    if (edge.getStartNodeId() == node.getNodeId() && edge.getEndNodeId() == target.getNodeId()) {
                        return edge.getL2Distance();
                    }else{
                    	return 0.0;
                    }
                   
            }
            throw new RuntimeException("DOESN'T RETURN DISTANCE");
    
		
	}


	private static Node getMinNode(List<Node> notVisitedNodes) {
		Node minNode = null;
        for (Node node : nodes) {
                if (minNode == null) {
                        minNode = node;
                } else {
                        if (getShortestDistance(node) < getShortestDistance(minNode)) {
                                minNode = node;
                        }
                }
        }
        return minNode;
	}

	private static Double getShortestDistance(Node destination) {
		 Double destinationDistance = distance.get(destination);
         if (destinationDistance == null) {
                 return Double.MAX_VALUE;
         } else {
                 return destinationDistance;
         }
	}

	private static List<Node> createNodesList(List<String[]> nodesData) {
		List<Node> nodesList = new ArrayList<Node>();
		for(String[] entry: nodesData){
			int nodeId = Integer.parseInt(entry[0]);
      		double longtitude = Double.parseDouble(entry[1]);
            double latitude = Double.parseDouble(entry[2]);
            Node node = new Node(nodeId, longtitude, latitude);
            nodesList.add(node);
		}
		return nodesList;
	}
	
	private static List<Edge> createEdgesList(List<String[]> edgesData) {
		List<Edge> edgesList = new ArrayList<Edge>();
		for(String[] entry: edgesData){
			int edgeId = Integer.parseInt(entry[0]);
			int startNodeId = Integer.parseInt(entry[1]);
			int endNodeId = Integer.parseInt(entry[2]);
      		double L2Distance = Double.parseDouble(entry[3]);
            Edge edge = new Edge(edgeId, startNodeId, endNodeId, L2Distance);
            edgesList.add(edge);
		}
		return edgesList;
	}
	
	private static AdjacencyList createAdjacencyList() {
		AdjacencyList adjacencyList = new AdjacencyList(nodes.size());
	    for(int i=0; i<edges.size(); i++){
	    	adjacencyList.addEdge(edges.get(i).getStartNodeId(), edges.get(i).getEndNodeId(), edges.get(i).getL2Distance());
	    }
		return adjacencyList;
	}
	
}
