
public class Edge {
	private int edgeId;
	private int startNodeId;
	private int endNodeId;
	private double L2Distance;
	
	public Edge(){ }
	
	public Edge(int edgeId, int startNodeId, int endNodeId,  double L2Distance){
		this.setEdgeId(edgeId);
		this.setStartNodeId(startNodeId);
		this.setEndNodeId(endNodeId);
		this.setL2Distance(L2Distance);
	}

	public int getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(int edgeId) {
		this.edgeId = edgeId;
	}

	public int getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(int startNodeId) {
		this.startNodeId = startNodeId;
	}

	public int getEndNodeId() {
		return endNodeId;
	}

	public void setEndNodeId(int endNodeId) {
		this.endNodeId = endNodeId;
	}

	public double getL2Distance() {
		return L2Distance;
	}

	public void setL2Distance(double l2Distance) {
		L2Distance = l2Distance;
	}
	
	public String toString() {
        return "{EdgeeId: "+ edgeId + " " + "StartNodeId: "  + startNodeId + " "+ "EndNodeId: " + endNodeId + " "+ "L2Distance: " + L2Distance + "}";
	}
}
