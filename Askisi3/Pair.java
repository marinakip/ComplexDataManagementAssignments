
@SuppressWarnings("hiding")
public class Pair<Integer, Double> {
	 private Integer integer;
	 private Double doubl;
	 
	    public Pair(Integer integer, Double doubl){
	        this.integer = integer;
	        this.doubl = doubl;
	    }
	    
	    public Integer getInt(){ return integer; }
	    public Double getDouble(){ return doubl; }
	    public void setInt(Integer integer){ this.integer = integer; }
	    public void setDouble(Double doubl){ this.doubl = doubl; }

}
