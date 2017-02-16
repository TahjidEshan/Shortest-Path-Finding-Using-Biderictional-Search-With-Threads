public class Node{
	public String name;
	public int cost;
	public Node previous;
	public Node(String name,int cost,Node previous){
		this.name=name;
		this.cost=cost;
		this.previous=previous;
	}
} 