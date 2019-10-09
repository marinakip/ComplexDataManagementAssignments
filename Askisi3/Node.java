
public class Node {
	private int nodeId;
	private double longtitude;
	private double latitude;
	
	public Node(){ }
	
	public Node(int nodeId, double longtitude, double latitude){
		this.setNodeId(nodeId);
		this.setLongtitude(longtitude);
		this.setLatitude(latitude);
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String toString() {
        return "{NodeId: "+ nodeId + " " + "longtitude: "  + longtitude + " "+ "latitude: " + latitude + "}";
	}


}
