import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class TrainRoutes {
	
	private static Map<String, Node> nodes = new HashMap<String, Node>();
	
	public static void main (String[] args){
		parseInput();
		String[] nodeList = {"A", "B", "C"};
		getDistance(nodeList);
		String[] nodeList2 = {"A", "D"};
		getDistance(nodeList2);
		String[] nodeList3 = {"A", "D", "C"};
		getDistance(nodeList3);
		String[] nodeList4 = {"A", "E", "B", "C", "D"};
		getDistance(nodeList4);
		String[] nodeList5 = {"A", "E", "D"};
		getDistance(nodeList5);
		MyCondition exactStops = new MyCondition(){
			public boolean isTrue(int value){
				if(value == 1)
					return true;
				return false;
			}
		};
		
		MyCondition maximumStops = new MyCondition(){
			public boolean isTrue(int value){
				if(value > 0)
					return true;
				return false;
			}
		};
		System.out.println(getNumberOfRoutes("C", "C", 3, maximumStops));
		System.out.println(getNumberOfRoutes("A", "C", 4, exactStops));
		System.out.println(djikstra("A", "C", 0));
		System.out.println(djikstra("B", "B", 0));
		System.out.println(getNumberOfRoutesByDistance("C", "C", 30));
	}
	
	private static void parseInput() {
		Scanner sc;
		try {
			sc = new Scanner(new File("input.in"));
			String s;
			
			while(sc.hasNext()){
				s = sc.next();
				String source = String.valueOf(s.charAt(0));
				String dest = String.valueOf(s.charAt(1));
				int weight = Integer.valueOf(String.valueOf(s.charAt(2)));
				if(!nodes.containsKey(source)){
					nodes.put(source, new Node());
				}
				nodes.get(source).addArc(dest, weight);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void getDistance(String[] nodeList){
		String curr = nodeList[0];
		Node  currNode = nodes.get(curr);
		String next;
		int distance = 0;
		
		for(int i=1; i<nodeList.length; i++) {
			next = nodeList[i];
			boolean flag = false;
			for(Arc arc : currNode.getArcs()){
				if(arc.getAdj().equals(next)){
					distance += arc.getWeight();
					flag = true;
					break;
				}
			}
			
			if(flag == false){
				System.out.println("NO SUCH ROUTE");
				return;
			}
			curr = next;
			currNode = nodes.get(curr);
		}
		
		System.out.println(distance);
		return;
	}
	
	private static int getNumberOfRoutes(String source, String dest, int stops,  MyCondition c){
		return nodes.get(source).getNumberOfRoutes(dest, stops, c);
	}
	
	private static int getNumberOfRoutesByDistance(String source, String dest, int distance){
		return nodes.get(source).getNumberOfRoutesByDistance(dest, distance, 0);
	}
	
	private static int djikstra(String source, String dest, int dist){
		if(source.equals(dest) && dist!=0)
			return dist;
		int minDist = Integer.MAX_VALUE;
		Node curr = nodes.get(source);
		Node tmp;
		for(Arc arc: curr.getArcs()){
			tmp = nodes.get(arc.getAdj());
			if(!tmp.isMarked()){
				tmp.mark();
				minDist = Math.min(minDist, djikstra(arc.getAdj(), dest, dist + arc.getWeight()));
				tmp.unmark();
			}
		}
		return minDist;
	}
	
	private static class Node {
		
		private List<Arc> arcs;
		boolean marked;
		
		Node(){
			arcs = new LinkedList<Arc>();
			marked = false;
		}
		
		List<Arc> getArcs(){
			return arcs;
		}
		
		int getNumberOfRoutes(String dest, int stops, MyCondition c){
			int routes = 0;
			if(stops<0)
				return 0;
			for(Arc arc : arcs){
				if(arc.getAdj().equals(dest) && c.isTrue(stops)){
					routes++;
				}
				routes += nodes.get(arc.getAdj()).getNumberOfRoutes(dest, stops-1, c);				
				
			}
			return routes;
		}
		
		int getNumberOfRoutesByDistance(String dest, int distance, int acumDistance){
			int routes = 0;
			if(acumDistance>distance)
				return 0;
			for(Arc arc : arcs){
				if(arc.getAdj().equals(dest) && acumDistance + arc.getWeight() < distance){
					routes++;
				}
				routes += nodes.get(arc.getAdj()).getNumberOfRoutesByDistance(dest, distance, acumDistance + arc.getWeight());				
				
			}
			return routes;
		}
		
		void addArc(String adj, int weight){
			arcs.add(new Arc(weight, adj));
		}
		
		void mark(){
			this.marked = true;
		}
		
		void unmark(){
			this.marked = false;
		}
		
		boolean isMarked(){
			return this.marked;
		}
	}
	
	private static class Arc {
		private int weight;
		private String adj;
		
		Arc(int weight, String adj){
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

}
