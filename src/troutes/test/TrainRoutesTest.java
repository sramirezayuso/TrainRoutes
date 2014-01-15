package troutes.test;

import java.io.File;
import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

import troutes.main.Town;
import troutes.main.TrainRoutes;

public class TrainRoutesTest extends TestCase{
	
	private TrainRoutes tr = new TrainRoutes(new File("input.in"));

	
	@Test
	public void testGetAdjacentByName(){
		Town tw = new Town("A");
		tw.addAjacentTown(new Town("B"), 10);
		int dist = tw.getAdjacentTownDistance("B");
		assertEquals(10, dist);
	}
	
	@Test
	public void testGetAdjacent(){
		Town tw = new Town("A");
		Town tw2 = new Town("B");
		tw.addAjacentTown(tw2, 2);
		int dist = tw.getAdjacentTownDistance(tw2);
		assertEquals(2, dist);
	}
	
	@Test
	public void testFailToGetAdjacentByName(){
		Town tw = new Town("A");
		Integer dist = tw.getAdjacentTownDistance("B");
		assertNull(dist);
	}
	
	@Test
	public void testFailToGetAdjacent(){
		Town tw = new Town("A");
		Integer dist = tw.getAdjacentTownDistance(new Town("B"));
		assertNull(dist);
	}
	
	@Test
	public void testGetRouteLength(){
		Integer dist = tr.getDistanceOfRouteByName(Arrays.asList("A", "B", "C")); 
		assertEquals(Integer.valueOf(9), dist);
	}
	
	@Test
	public void testFailToGetRouteLength(){
		Integer dist = tr.getDistanceOfRouteByName(Arrays.asList("A", "E", "D")); 
		assertNull(dist);
	}
	
	@Test
	public void testGetNumberOfRoutesByDistance(){
		int routes = tr.getNumberOfRoutesByDistance("C", "C", 29);
		assertEquals(7, routes);
	}
	
	@Test
	public void testGetNumberOfRoutesExact(){
		int routes = tr.getNumberOfRoutesExact("A", "C", 4);
		assertEquals(3, routes);
	}
	
	@Test
	public void testGetNumberOfRoutesMaximum(){
		int routes = tr.getNumberOfRoutesMaximum("C", "C", 3);
		assertEquals(2, routes);
	}
	
	@Test
	public void testGetShortestRoute(){
		int dist = tr.getShortestPath("A", "C");
		assertEquals(9, dist);
	}
}
