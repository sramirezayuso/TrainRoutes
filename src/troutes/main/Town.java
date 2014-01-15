package troutes.main;
import java.util.HashMap;
import java.util.Map;

public class Town{
		
	private String name;
	private Map<Town, Integer> adj;
	
	// Route dest, peso--> List<Route>
	private boolean marked;
	
	public Town(String name){
		this.name = name;
		this.adj = new HashMap<Town, Integer>();
		this.marked = false;
	}
	
	public Map<Town, Integer> getAdjacents(){
		return adj;
	}

	public void addAjacentTown(Town tw, int distance){
		adj.put(tw , distance);
	}
	
	public Integer getAdjacentTownDistance(String townName){
		return adj.get(new Town(townName));
	}
	
	public Integer getAdjacentTownDistance(Town tw){
		return adj.get(tw);
	}
	
	public String getName(){
		return this.name;
	}

	public void mark(){
		this.marked = true;
	}
	
	public void unmark(){
		this.marked = false;
	}
	
	public boolean isMarked(){
		return this.marked;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Town other = (Town) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/*@Override
	public Iterator<Town> iterator() {
		return new TownIterator();
	}
	
	private class TownIterator implements Iterator<Town>{
		
		private boolean hasNext;
		private Town curr;
		private Deque<Town> past;
		
		
		public TownIterator(){
			this.hasNext = true;
			this.curr = Town.this;
			this.past = new LinkedList<Town>();
		}
		
		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public Town next() {
			while(true){
				for(Town adj : curr.getAdjacents().keySet()){
					if(!adj.isMarked()){
						curr.mark();
						past.push(curr);
						curr = adj;
						return curr;
					}
				}
				curr = past.pop();
				curr.unmark();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}*/
}