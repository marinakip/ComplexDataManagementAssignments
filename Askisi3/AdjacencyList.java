import java.util.LinkedList;


public class AdjacencyList {
	private  LinkedList< Pair<Integer, Double>>[] adjacencyList;
	
	 @SuppressWarnings("unchecked")
	public AdjacencyList(int nodesSize) {
	        adjacencyList = (LinkedList< Pair<Integer, Double>>[]) new LinkedList[nodesSize]; 
	        for (int i = 0; i < adjacencyList.length; ++i) {
	            adjacencyList[i] = new LinkedList<>();
	        }
	 }
	 
	 public void addEdge(int startNodeId, int endNodeId, double l2Distance) {
	        adjacencyList[startNodeId].add(new Pair<>(endNodeId, l2Distance));
	 }
	 
	 public void removeEdge(int startNodeId, Pair<Integer, Integer> edge) {
	        adjacencyList[startNodeId].remove(edge);
	 }
	 
	 public int length() {
	        return adjacencyList.length;
	 }
	 
	 public int getEdgesSizeFromNode(int startNodeId) {
	        return adjacencyList[startNodeId].size();
	 }
	 
	 public LinkedList< Pair<Integer, Double>>  getEdgesListFromNode(int startNodeId) {
	        @SuppressWarnings({ "unchecked", "rawtypes" })
			LinkedList< Pair<Integer, Double>> edgesList = (LinkedList< Pair<Integer, Double>>) new LinkedList(adjacencyList[startNodeId]);   
	        return edgesList;
	 }
	 
	 public void printAdjacencyList() {
		 	for(int i=0; i<adjacencyList.length; i++){
		 		System.out.print("Node[" + i + "] is adjacent to: ");
		 		if(getEdgesSizeFromNode(i) >0){
		 			for(int j=0; j<getEdgesSizeFromNode(i); j++){
		 				System.out.print("[" + getEdgesListFromNode(i).get(j).getInt() + "]");	
		 			}
		 			System.out.println();	
		 		}else{
		 			System.out.print("[" + "]");
		 			System.out.println();
		 		}	
		 	}
	 } 
	 
}
