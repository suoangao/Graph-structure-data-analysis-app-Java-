import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JComponent;
/**
 *  Final project
 *  @author  SuoAn Gao
 *  @version CSC 212, 24 April 2017
 * 	
 */
/*The Main Class Graph*/
public class Graph<E,V> extends JComponent{
	
	private static final long serialVersionUID = 1L;
	// fields, a list of nodes and a list of edges
	private ArrayList<Node> nodesList;
	private ArrayList<Edge> edgesList;
	private ArrayList<Edge> paths = new ArrayList<Edge>(); // sorting paths, various based on the traversal method
	/*Constructor of Graph,initialize the node list and edge list*/
	public Graph(){
		nodesList = new ArrayList<Node>();
		edgesList = new ArrayList<Edge>();
	}
	
	/*Accessor of a node within the nodelist
	@ param int pos
	*/ 
	public Node getNode(int i){
		return nodesList.get(i);
	}
	
	// get node from a nodelist base on a given axis
	public Node getNode(int ix,int iy){
		Node thisNode = null;
		for(Node n: nodesList){
			if((n.axis.x == ix) && (n.axis.y == iy)){
				thisNode = n;
			}
		}
		return thisNode;
	}
	
	/*Accessor of an edge within the edgelist
	@ param int pos
	*/ 
	public Edge getEdge(int j){
		return edgesList.get(j);
	}
	
	
	
	/*Accessor of a specific edge within the nodelist
	@ param head and tail of that edge 
	*/ 
	public Edge getEdgeRef(Node head, Node tail){
		Edge demandedEdge = null;
		for(Edge s : edgesList){
			if(s.getHead() == head && s.getTail() == tail){
				demandedEdge = s;
			}
		}
		return demandedEdge;
	}
	
	//Accessor for number of nodes
	public int numNodes(){
		return nodesList.size();
	}
	
	//Accessor for number of edges
	public int numEdges(){
		return edgesList.size();
	}
	
	// add a node into the array list(node)
	public Node addNode(V indata){
		Node newNode = new Node(indata);
		nodesList.add(newNode);
		return newNode;
	}
	
	// add an edge into the array list(Edge)
	public Edge addEdge(E data, Node head, Node tail){
		Edge newEdge = new Edge(data,head,tail);
		edgesList.add(newEdge);
		head.addEdgeRef(newEdge);
		tail.addEdgeRef(newEdge);
		return newEdge;
	}
	
	// remove a specified node from the node list
	public void removeNode(Node nodeToBeRemoved){
		nodeToBeRemoved.lvl1edgeslist.clear();
		nodesList.remove(nodeToBeRemoved);
	}
	
	// remove a specified edge form the edge list
	public void removeEdge(Edge edgeToBeRemoved){
		edgesList.remove(edgeToBeRemoved);
		edgeToBeRemoved.getHead().removeEdgeRef(edgeToBeRemoved);
		edgeToBeRemoved.getTail().removeEdgeRef(edgeToBeRemoved);
	}
	
	public void removeEdge(Node head, Node tail){
		Edge removingThisEdge = getEdgeRef(head,tail);
		head.removeEdgeRef(removingThisEdge);
		tail.removeEdgeRef(removingThisEdge);
		edgesList.remove(removingThisEdge);
	}
	
	//Returns nodes not on a given list
	public HashSet<Node> otherNodes(HashSet<Node> givenSet){
		HashSet<Node> otherNodes = new HashSet<Node>();
		for(Node n: nodesList){
			if(!givenSet.contains(n)){
				otherNodes.add(n);
			}
		}
		return otherNodes;
	}
	
	//Returns nodes that are end points of a list of edges
	public HashSet<Node> endPoints(HashSet<Edge> givenEdgesList){
		HashSet<Node> endPoints = new HashSet<Node>();
		for(Edge e : givenEdgesList){
			for(Node v: nodesList){
				if(e.getTail() == v || e.getHead() ==v){
					endPoints.add(v);
				}
			}
		}
		return endPoints;
	}
/////////////////////////////////////////////Algorithm/////////////////////////////////////////////	
	//Breadth-first traversal of graph
	public ArrayList<Edge> BFT(Node start){
		Queue<Node> Q = new LinkedList<Node>();
		
		Q.add(start);
		
		while(Q.size()!=0){
			Node node = Q.poll();
			System.out.println("node " + node.data);
			for(Node n:node.getNeighbors() ){
			System.out.println("neibs " + n.data);
			}
			Iterator<Node> i = node.getNeighbors().iterator();
			while(i.hasNext()){
				Node ToAdtoQ = i.next();
				System.out.println("ToAdtoQ "+ToAdtoQ.getData());
				if(getEdgeRef(node,ToAdtoQ)!=null && !paths.contains(getEdgeRef(node,ToAdtoQ)) ){ // if this path existsts ++ hashset doesnot contain this path     
					paths.add(getEdgeRef(node,ToAdtoQ));
					for(Edge p:paths){
						System.out.println(p.data);
						}
					
					Q.add(ToAdtoQ);
					for(Node q : Q){
					System.out.println("Q: " + q.getData());
					}
					
					
				}
			}
		}
		return paths;
	}
		
	//Depth-first traversal of graph -- public interface
	public ArrayList<Edge> DFT(Node start){
		Node recurNode = start;
		System.out.println("now visiting from " + recurNode.data);
		for(Node nei:recurNode.getNeighbors()){
			System.out.println("Neibours: " + nei.getData());
		}
		Iterator<Node> j = recurNode.getNeighbors().iterator();
		
		while(j.hasNext()){
			Node nextNode = j.next();
			System.out.println("Next Step " + nextNode.data);
			if(getEdgeRef(recurNode,nextNode)!= null && !paths.contains(getEdgeRef(recurNode,nextNode))){
				System.out.println("Path is " + getEdgeRef(recurNode,nextNode).data);
				paths.add(getEdgeRef(recurNode,nextNode));
				for(Edge o: paths){
				System.out.println("in the pathjs: " + o.data);
				}
				DFT(nextNode);
			}
		}
		
		return paths;
	}

	//Dijkstra's shortest-path algorithm to compute distances to nodes
	/*public double[] distances(Node start){
		
	}
	*/
//////////////////////////////////////////////////////////////////////////////////////////	
	
	//Prints a representation of the graph
	public void print(){
		for(Node n: nodesList){
			System.out.println("Node " + n);
			System.out.println(" Neighbours " + n.getNeighbors());
		}
		
		for(Edge e: edgesList){
			System.out.println("Edge" + e);
			System.out.println(" Connect from " + e.getHead() + " to " + e.getTail());
		}
	}
	
	//Checks the consistency of the graph
	public void check(){
		System.out.println("Node Check Start...Please Wait...");
		for(Node n: nodesList){
			for(Edge e : n.lvl1edgeslist){
				if(n == e.getHead() || n == e.getTail()){
					System.out.println("Node Check #1 Success...");
				}else{
					System.out.println("ERROR AT NODE CHECK #1: UNSTEADY DATA STRUCTURE, PROCESSING WITH CAUTION!");
				}
			
				if(edgesList.contains(e)){
					System.out.println("Node Check #2 Success...");
				}else{
					System.out.println("ERROR AT NODE CHECK #2: UNSTEADY DATA STRUCTURE, PROCESSING WITH CAUTION!");
				}
			}
		}
		
		for(Edge e: edgesList){
			if((e.getHead()==e.head) && (e.getTail() ==e.tail)){
				System.out.println("Edge Check #1 Success...");
			}else{
				System.out.println("ERROR AT EDGE CHECK #1: UNSTEADY DATA STRUCTURE, PROCESSING WITH CAUTION!");
			}
			
			if(nodesList.contains(e.head) && nodesList.contains(e.tail)){
				System.out.println("Edge Check #2 Success...");
			}else{
				System.out.println("ERROR AT EDGE CHECK #2: UNSTEADY DATA STRUCTURE, PROCESSING WITH CAUTION!");
			}
		}
	}

////////////////////////////Graphics////////////////////////////////////////////////
	
	/*
    *  Paints a red circle ten pixels in diameter at each node.
    *  @param g The graphics object to draw with
    */
	public void paintComponent(Graphics g) {
        g.setColor(Color.RED); // node:RED
        for(Node n : this.nodesList){
        	g.drawOval(n.axis.x,n.axis.y, 10, 10);
        	repaint();
        }
        
        g.setColor(Color.BLUE); // edge: BLUE
        for(Edge e: this.edgesList){
        	g.drawLine(e.head.axis.x, e.head.axis.y, e.tail.axis.x, e.tail.axis.y);
        	repaint();
        }
    }
	
    /**
     *  The component will look bad if it is sized smaller than this
     *  @returns The minimum dimension
     */
    public Dimension getMinimumSize() {
        return new Dimension(500,3000);
    }

    /**
     *  The component will look best at this size
     *  @returns The preferred dimension
     */
    public Dimension getPreferredSize() {
        return new Dimension(500,300);
    }

/*//////////////////////////NESTED CLASS EDGE///////////////////////////////////////*/
public class Edge {
// field, data
	private E data;
	private Node head;
	private Node tail;
/*Constructor of the Edge Class, @param inputdata, head node, tail node*/
	public Edge(E indata,Node startingNode, Node endingNode){
		data = indata;
		head = startingNode;
		tail = endingNode;
	}
//Accessor for data within an edge
	public E getData(){	
		return data;
	}
//Accessor for head
	public Node getHead(){
		return head;
	}
//Accessor for tail
	public Node getTail(){
		return tail;
	}	
//Given an edge and one node it connects to, returns the other node attached to the edge.	
	public Node oppositeTo(Node aNode){
		Node theOppositeNode = null;
		if(this.getHead() == aNode){
			theOppositeNode = this.getTail();
		}else if(this.getTail() == aNode){
			theOppositeNode = this.getHead();
		}
		return theOppositeNode;
	}

//Manipulator of data
	public void setData(E Newdata){
		data = Newdata;
	}	

//Two edges are equal if they connect the same endpoints regardless of the data they carry. 
	public boolean equals(Edge edge1, Edge edge2){
		boolean resu = false;
		if((edge1.getHead() == edge2.getHead()) && (edge1.getTail()==edge2.getTail())){
			resu = true;
		}
		return resu;
	}
	
}
//--------------The End of NESTED CLASS EDGE------------------------//

/*//////////////////////////The Nested Class NODE//////////////////////////////////////*/
public class Node {
	// Attributes for Node
	private V data;
	private ArrayList<Edge> lvl1edgeslist;
	private Point axis = new Point(0,0);
	/*Constructor of the Node Class, @param inputdata, the data within the disconnected node*/
	public Node(V inputdata){
		data = inputdata;
		lvl1edgeslist = new ArrayList<Edge>();
	}
	//Accessor for data within a node
	public V getData(){
		return data;
	}
	
	//Manipulator of the data, we could set new data value to the node
	public void setData(V newData){
		data = newData;
	}
	// get the edge list inside the node
	public ArrayList<Edge> getLvl1edgesList(){
		return this.lvl1edgeslist;
	}
	
	// get the axis of a node
	public Point getAxis(){
		return axis;
	}
	
	// set the axis for a node
	public void setAxis(int inputx, int inputy){
		this.axis.x = inputx;
		this.axis.y = inputy;
	}
	
	//Returns a list of neighbors
	public LinkedList<Node>getNeighbors(){
		LinkedList<Node> listOfNeighbours = new LinkedList<Node>();
		for(Node n: nodesList){
			if(this.isNeighbors(n) == true){
				listOfNeighbours.add(n);
			}
		}
		return listOfNeighbours;
	}
	//Returns true if there is an edge to the node in question
	public boolean isNeighbors(Node testNode){
		boolean resu = false;
		for(Edge e: testNode.lvl1edgeslist){
			if(this.lvl1edgeslist.contains(e) && !this.equals(testNode)){
				resu = true;
			}
		}
		return resu;
	}
	
	//Returns the edge to a specified node, or null if there is none
	public Edge edgeTo(Node NeighbNode){
		Edge returnedEdge = getEdgeRef(this,NeighbNode);
		return returnedEdge;
	}
	//Adds an edge to the edge list
	protected void addEdgeRef(Edge edgeToBeAdded){
		this.lvl1edgeslist.add(edgeToBeAdded);
	}

	//Removes an edge from the edge list
	protected void removeEdgeRef(Edge edgeToBeRemoved){
		if(!this.lvl1edgeslist.isEmpty()){
		this.lvl1edgeslist.remove(edgeToBeRemoved);
		}
		if(!edgesList.isEmpty()){
		edgesList.remove(edgeToBeRemoved);
		}
	}
		
}
/////////////////////////////The End of NESTED CLASS NODE///////////////////////////////////////
}
