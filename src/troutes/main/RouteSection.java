package troutes.main;
public class RouteSection {
	private int weight;
	private String adj;
	
	RouteSection(int weight, String adj){
		this.weight = weight;
		this.adj = adj;
	}
	
	int getWeight(){
		return weight;
	}
	
	String getAdj(){
		return adj;
	}
}