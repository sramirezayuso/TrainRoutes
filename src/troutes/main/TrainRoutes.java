package troutes.main;
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

import troutes.test.CountCondition;



public class TrainRoutes {
	
	private Map<String, Town> towns = new HashMap<String, Town>();
	
	public void openTelnetServer(){
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
					
					pw.println(getNumberOfRoutesExact(source, dest, stops));
				} else if(cmd.equals("MaximumStops")) {
					String source = sc.next();
					String dest = sc.next();
					int stops = sc.nextInt();
					
					pw.println(getNumberOfRoutesMaximum(source, dest, stops));
				} else if(cmd.equals("MaximumDistance")){
					String source = sc.next();
					String dest = sc.next();
					int distance = sc.nextInt();
					
					pw.println(getNumberOfRoutesByDistance(source, dest, distance));
				} else if(cmd.equals("ShortestPath")) {
					String source = sc.next();
					String dest = sc.next();
					pw.println(getShortestPath(source, dest));
				} else if(cmd.equals("GetDistance")){
					
					List<String> nodeList = new ArrayList<String>();
					int len = sc.nextInt();
					for(int i=0; i<len; i++){
						nodeList.add(sc.next());
					}
					
					int res = getDistanceOfRouteByName(nodeList);
					if(res > 0)
						pw.println(res);
					else
						pw.println("NO SUCH ROUTE");
				}
				
				pw.close();
				socket.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public TrainRoutes(File file) {
		Scanner sc;
		try {
			sc = new Scanner(file);
			parseInput(sc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public TrainRoutes(String data){
		Scanner sc;
		sc = new Scanner(data);
		parseInput(sc);
	}
	
	public Integer getDistanceOfRouteByName(List<String> nodeList){
		List<Town> townList = new LinkedList<Town>();
		for(String name : nodeList)
			townList.add(towns.get(name));
		return getDistanceOfRoute(townList);
	}
	
	public Integer getDistanceOfRoute(List<Town> townList){
		Town curr = townList.get(0);
		boolean notFound = false;
		int dist = 0;
		for(int i=1; i< townList.size(); i++){
			if(!curr.getAdjacents().containsKey(townList.get(i))){
				notFound = true;
				break;
			}
			dist += curr.getAdjacents().get(townList.get(i));
			curr = townList.get(i);
		}
		if(notFound)
			return null;
		return dist;
	}
	
	public int getNumberOfRoutesMaximum(String source, String dest, int stops){
		CountCondition c = new CountCondition(){
			public boolean isTrue(int value){
                if(value > 0)
                        return true;
                return false;
			}
		};
			
		return getNumberOfRoutes(towns.get(source), towns.get(dest), stops, c);
	}
	
	public int getNumberOfRoutesExact(String source, String dest, int stops){
		CountCondition c = new CountCondition(){
			public boolean isTrue(int value){
                if(value == 0)
                        return true;
                return false;
			}
		};
		return getNumberOfRoutes(towns.get(source), towns.get(dest), stops, c);

	}
	
	public int getNumberOfRoutesByDistance(String source, String dest, int maxDistance){
		return getNumberOfRoutesByDistance(towns.get(source), towns.get(dest), 0, maxDistance);
	}
	
	public int getShortestPath(String source, String dest){
		return djikstra(towns.get(source), towns.get(dest), 0);
	}
	
	private int djikstra(Town source, Town dest, int dist){
		if(source.equals(dest) && dist!=0)
			return dist;
		int minDist = Integer.MAX_VALUE;
		Town curr = source;
		for(Town adj: curr.getAdjacents().keySet()){
			if(!adj.isMarked()){
				adj.mark();
				minDist = Math.min(minDist, djikstra(adj, dest, dist + curr.getAdjacents().get(adj)));
				adj.unmark();
			}
		}
		return minDist;
	}
	
	private void parseInput(Scanner sc){
		String s;
		
		while(sc.hasNext()){
			s = sc.next();
			String source = String.valueOf(s.charAt(0));
			String dest = String.valueOf(s.charAt(1));
			int dist = Integer.valueOf(String.valueOf(s.charAt(2)));
			if(!towns.containsKey(source))
				towns.put(source, new Town(source));
			if(!towns.containsKey(dest))
				towns.put(dest, new Town(dest));
			towns.get(source).addAjacentTown(towns.get(dest), dist);
		}
	}
	
	private int getNumberOfRoutesByDistance(Town source, Town dest, int distance, int maxDistance){
        int routes = 0;
        if(distance > maxDistance)
                return 0;
        for(Town adj : source.getAdjacents().keySet()){
                if(adj.equals(dest) && distance + source.getAdjacents().get(adj) < maxDistance){
                        routes++;
                }
                routes += getNumberOfRoutesByDistance(adj, dest, distance+source.getAdjacents().get(adj), maxDistance);                                
                
        }
        return routes;
	}
	
	private int getNumberOfRoutes(Town source, Town dest, int stops, CountCondition c){
        int routes = 0;
        if(stops<0)
                return 0;
        for(Town adj : source.getAdjacents().keySet()){
                if(adj.equals(dest) && c.isTrue(stops)){
                        routes++;
                }
                routes += getNumberOfRoutes(adj, dest, stops-1, c);                                
                
        }
        return routes;
	}
}
