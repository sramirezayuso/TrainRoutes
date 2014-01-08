import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class TrainRoutes {
	
	private static Map<String, Node> nodes = new HashMap<String, Node>();
	
	public static void main (String[] args){
		parseInput();
		remote();
		
	}
	
	public static void remote(){
		try{
			final int portNumber = 5072;
			System.out.println("Creating server socket on port " + portNumber);
			ServerSocket serverSocket = new ServerSocket(portNumber);
			while (true) {
				Socket socket = serverSocket.accept();
				OutputStream os = socket.getOutputStream();
				PrintWriter pw = new PrintWriter(os, true);
				Scanner sc = new Scanner(new InputStreamReader(socket.getInputStream()));
				
				pw.println("Insert command:");
				String cmd = sc.next();
				
				if(cmd.equals("ExactStops")){
					String source = sc.next();
					String dest = sc.next();
					int stops = sc.nextInt();
					MyCondition exactStops = new MyCondition(){
						public boolean isTrue(int value){
							if(value == 1)
								return true;
							return false;
						}
					};
					pw.println(getNumberOfRoutes(source, dest, stops, exactStops));
					
				} else if(cmd.equals("MaximumStops")) {
					String source = sc.next();
					String dest = sc.next();
					int stops = sc.nextInt();
					MyCondition maximumStops = new MyCondition(){
						public boolean isTrue(int value){
							if(value > 0)
								return true;
							return false;
						}
					};
					pw.println(getNumberOfRoutes(source, dest, stops, maximumStops));
				} else if(cmd.equals("MaximumDistance")){
					String source = sc.next();
					String dest = sc.next();
					int distance = sc.nextInt();
					pw.println(getNumberOfRoutesByDistance(source, dest, distance));
				} else if(cmd.equals("ShortestPath")) {
					String source = sc.next();
					String dest = sc.next();
					pw.println(djikstra(source, dest, 0));
				} else if(cmd.equals("GetDistance")){
					
					List<String> nodeList = new ArrayList<String>();
					int len = sc.nextInt();
					for(int i=0; i<len; i++){
						nodeList.add(sc.next());
					}
					
					int res = getDistance(nodeList);
					if(res > 0)
						pw.println(res);
					else
						pw.println("NO SUCH ROUTE");
				}
				
				pw.close();
				socket.close();
	
				//System.out.println("Just said hello to:" + str);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
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
	
	private static int getDistance(List<String> nodeList){
		String curr = nodeList.get(0);
		Node  currNode = nodes.get(curr);
		String next;
		int distance = 0;
		
		for(int i=1; i<nodeList.size(); i++) {
			next = nodeList.get(i);
			boolean flag = false;
			for(Arc arc : currNode.getArcs()){
				if(arc.getAdj().equals(next)){
					distance += arc.getWeight();
					flag = true;
					break;
				}
			}
			
			if(flag == false){
				return -1;
			}
			curr = next;
			System.out.println(distance);
			currNode = nodes.get(curr);
		}
		
		return distance;
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
