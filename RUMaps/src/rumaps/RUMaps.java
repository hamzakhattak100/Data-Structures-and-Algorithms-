package rumaps;

import java.util.*;

/**
 * This class represents the information that can be attained from the Rutgers
 * University Map.
 * 
 * The RUMaps class is responsible for initializing the network, streets,
 * blocks, and intersections in the map.
 * 
 * You will complete methods to initialize blocks and intersections, calculate
 * block lengths, find reachable intersections,
 * minimize intersections between two points, find the fastest path between two
 * points, and calculate a path's information.
 * 
 * Provided is a Network object that contains all the streets and intersections
 * in the map
 * 
 * @author Vian Miranda
 * @author Anna Lu
 */
public class RUMaps {

    private Network rutgers;

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Constructor for the RUMaps class. Initializes the streets and intersections
     * in the map.
     * For each block in every street, sets the block's length, traffic factor, and
     * traffic value.
     * 
     * @param mapPanel The map panel to display the map
     * @param filename The name of the file containing the street information
     */
    public RUMaps(MapPanel mapPanel, String filename) {
        StdIn.setFile(filename);
        int numIntersections = StdIn.readInt();
        int numStreets = StdIn.readInt();
        StdIn.readLine();
        rutgers = new Network(numIntersections, mapPanel);
        ArrayList<Block> blocks = initializeBlocks(numStreets);
        initializeIntersections(blocks);

        for (Block block : rutgers.getAdjacencyList()) {
            Block ptr = block;
            while (ptr != null) {
                ptr.setLength(blockLength(ptr));
                ptr.setTrafficFactor(blockTrafficFactor(ptr));
                ptr.setTraffic(blockTraffic(ptr));
                ptr = ptr.getNext();
            }
        }
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     * 
     * @param filename The name of the file containing the street information
     */
    public RUMaps(String filename) {
        this(null, filename);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Overloaded constructor for testing.
     */
    public RUMaps() {

    }

    /**
     * Initializes all blocks, given a number of streets.
     * the file was opened by the constructor - use StdIn to continue reading the
     * file
     * 
     * @param numStreets the number of streets
     * @return an ArrayList of blocks
     *         Create an ArrayList of blocks.
     *         For each street:
     *         Read the street name and its number of blocks.
     *         For each block:
     *         Read its block number (as an int), number of points (as an int), and
     *         road size (as a double), and initialize a Block object.
     *         For each point, read its x and y coordinates and create a new
     *         Coordinate object.
     *         If this is the first point, call block.startPoint on the coordinate;
     *         if it’s not, call block.nextPoint on the coordinate.
     *         If StdIn.readInt is followed by StdIn.readLine,
     *         read the remaining newline character (as .readInt() leaves trailing
     *         newline characters unread).
     *         Then, add the block to the resulting blocks ArrayList.
     *         Return the ArrayList of blocks that now stores all blocks you read
     *         from the input file.
     */
    public ArrayList<Block> initializeBlocks(int numStreets) {
        // WRITE YOUR CODE HERE
        ArrayList<Block> bob = new ArrayList<>();
        for (int i = 0; i < numStreets; i++) {
            String name = StdIn.readLine();
            int blockNum = StdIn.readInt();
            StdIn.readLine();
            for (int i2 = 0; i2 < blockNum; i2++) {
                int blockNumber = StdIn.readInt();
                int numPoints = StdIn.readInt();
                double roadSize = StdIn.readDouble();
                StdIn.readLine();
                Block block = new Block(roadSize, name, blockNumber);
                for (int i3 = 0; i3 < numPoints; i3++) {
                    int x = StdIn.readInt();
                    int y = StdIn.readInt();
                    StdIn.readLine();
                    Coordinate bob3 = new Coordinate(x, y);
                    if (i3 == 0) {
                        block.startPoint(bob3);
                    } else {
                        block.nextPoint(bob3);
                    }
                }
                bob.add(block);
            }
        }
        return bob; // Replace this line, it is provided so the code compiles
    }

    /**
     * This method traverses through each block and finds
     * the block's start and end points to create intersections.
     * 
     * It then adds intersections as vertices to the "rutgers" graph if
     * they are not already present, and adds UNDIRECTED edges to the adjacency
     * list.
     * For each block, grab its starting point (given in index 0 of its coordinate
     * list),
     * and its ending point (given in index (size – 1) of its coordinate list).
     * Identify if an intersection exists for BOTH the start and end points:
     * use the findIntersection method on your network instance and pass in the
     * start and end coordinates. It’ll return its index.
     * If the starting intersection doesn’t exist,
     * create a new Intersection object with the starting coordinate and set the
     * block’s first endpoint to the intersection.
     * Then, add the intersection to the network.
     * If the ending intersection doesn’t exist (if any),
     * create a new Intersection object with the ending coordinate and set the
     * block’s LAST endpoint to the intersection.
     * Then, add the intersection to the network.
     * If an intersection DOES exist, find the intersection in the intersections
     * array and update its first (OR last) endpoint accordingly.
     * Use the index of the intersections you just added (or found) to
     * determine the index its edges should go into – you can use findIntersection
     * to do this.
     * USE the block.copy() method in order to create a deep copy of the current
     * block.
     * Block a stores an edge from first -> last.
     * Block b stores an edge from last -> first. You should REVERSE the endpoints
     * from block a.
     * Then, call addEdge to 1) add block A to its corresponding index, and 2) add
     * block B to its corresponding index.
     * There are TWO calls to addEdge.
     * Note that .addEdge(__) ONLY adds edges in one direction (a -> b).
     */
    public void initializeIntersections(ArrayList<Block> blocks) {
        // WRITE YOUR CODE HERE
        for (int i = 0; i < blocks.size(); i++) {
            Coordinate first = blocks.get(i).getCoordinatePoints().get(0);
            Coordinate last = blocks.get(i).getCoordinatePoints().get(blocks.get(i).getCoordinatePoints().size() - 1);
            int firstIndex = rutgers.findIntersection(blocks.get(i).getCoordinatePoints().get(0));
            int lastIndex = rutgers.findIntersection(
                    blocks.get(i).getCoordinatePoints().get(blocks.get(i).getCoordinatePoints().size() - 1));
            Intersection start = new Intersection(first);
            Intersection end = new Intersection(last);
            if (firstIndex == -1) {
                rutgers.addIntersection(start);
            }
            if (lastIndex == -1) {

                rutgers.addIntersection(end);
            }
            firstIndex = rutgers.findIntersection(blocks.get(i).getCoordinatePoints().get(0));
            lastIndex = rutgers.findIntersection(
                    blocks.get(i).getCoordinatePoints().get(blocks.get(i).getCoordinatePoints().size() - 1));
            blocks.get(i).setFirstEndpoint(start);
            blocks.get(i).setLastEndpoint(end);
            Block b3 = blocks.get(i).copy();
            b3.setFirstEndpoint(blocks.get(i).getLastEndpoint());
            b3.setLastEndpoint(blocks.get(i).getFirstEndpoint());
            rutgers.addEdge(firstIndex, blocks.get(i));
            rutgers.addEdge(lastIndex, b3);

        }
    }

    /**
     * Calculates the length of a block by summing the distances between consecutive
     * points for all points in the block.
     * 
     * @param block The block whose length is being calculated
     * @return The total length of the block
     *         In this method, you’re given a block, and you’ll need to find the
     *         total length of this block (in terms of distance).
     *         Do not simply calculate the distance between vertex0 and vertex1 as
     *         this won’t give you an accurate representation
     *         of the length of a block (apart from straight lines) –
     *         USE the coordinateDistance method which takes in two coordinate
     *         objects.
     * 
     * 
     *         Grab the block’s coordinate points, and then iterate through each
     *         pair of points. Sum the distance between each pair.
     *         Then, return the total length of this block.
     *         This will be given in the driver (shown below for Busch.in) when you
     *         hover over an edge,
     *         as seen below. Sutphen Road is to the left of SHI Stadium and
     *         intersects with River Road (which isn’t on this input file).
     */
    public double blockLength(Block block) {
        // WRITE YOUR CODE HERE
        double length = 0;
        ArrayList<Coordinate> bob = block.getCoordinatePoints();
        for (int i = 0; i < bob.size() - 1; i++) {

            length += coordinateDistance(bob.get(i), bob.get(i + 1));

        }
        return length; // Replace this line, it is provided so the code compiles
    }

    /**
     * Use a DFS to traverse through blocks, and find the order of intersections
     * traversed starting from a given intersection (as source).
     * 
     * Implement this method recursively, using a helper method.
     * e’s how to complete this method:
     * This method uses a DFS from a given source intersection to find the order of
     * all vertices visited,
     * starting from (and including the source).
     * You’ll return the order of all intersections visited inside an ArrayList of
     * Intersections.
     * 
     * Order matters since DFS produces a specific ordering.
     * Implement this method recursively as discussed in class;
     * you can use helper methods to do so provided that they are private.
     *
     * 
     */
    private ArrayList<Intersection> dfs(int current, boolean[] visted, ArrayList<Intersection> bob) {
        visted[current] = true;
        Block ptr = rutgers.getAdjacencyList()[current];
        bob.add(rutgers.getIntersections()[current]);
        Intersection ptr2;
        while (ptr != null) {
            ptr2 = ptr.other(rutgers.getIntersections()[current]);
            int index2 = rutgers.findIntersection(ptr2.getCoordinate());
            if (!visted[index2]) {
                dfs(index2, visted, bob);
            }
            ptr = ptr.getNext();
        }
        return bob;
    }

    public ArrayList<Intersection> reachableIntersections(Intersection source) {
        // WRITE YOUR CODE HERE
        boolean[] track = new boolean[rutgers.getIntersections().length];
        int index = rutgers.findIntersection(source.getCoordinate());
        ArrayList<Intersection> result = new ArrayList<>();
        return dfs(index, track, result); // Replace this line, it is provided so the code compiles
    }

    /**
     * Finds and returns the path with the least number of intersections (nodes)
     * from the start to the end intersection.
     * 
     * - If no path exists, return an empty ArrayList.
     * - This graph is large. Find a way to eliminate searching through
     * intersections that have already been visited.
     * 
     * @param start The starting intersection
     * @param end   The destination intersection
     * @return The path with the least number of turns, or an empty ArrayList if no
     *         path exists
     *         In this method, you’ll use a BFS search to find a path from the
     *         source intersection to a target intersection.
     *         In your algorithm, use .equals when comparing intersections, not ==.
     * 
     *         Use the edgeTo array (or other equivalent structure) to store
     *         predecessors of each vertex (intersection) — t
     *         he graph structure differs from class as Blocks represent edges and
     *         Intersections represent vertices.
     *         You’ll need to implement the edgeTo data structure yourself.
     * 
     *         You will have to return a path of Intersections. You can store for
     *         each Intersection the preceding Intersection (or intersection index),
     *         or a Block that represents a “predecessor intersection ->
     *         intersection” edge.
     *         To get the path, you will follow an algorithm similar to
     *         quick-union’s find() method,
     *         chasing up from the target until the source is in your path. Take a
     *         look at the pseudocode below.
     * 
     * 
     *         Reverse the path so that it starts from the source — you can use
     *         Collections.reverse(___)
     *         to do this since the path is an ArrayList. DO NOT use other methods
     *         to reverse the ArrayList,
     *         as they may not exist in Java 17 which Autolab uses.
     *         v = target
     *         path = []
     *         while v is not null:
     *         add v to path
     *         v = edgeTo[v];
     *         reverse path to start from source
     */

    public ArrayList<Intersection> minimizeIntersections(Intersection start, Intersection end) {
        // WRITE YOUR CODE HERE

        ArrayList<Intersection> path = new ArrayList<>();
        int index = rutgers.findIntersection(start.getCoordinate());
        int edgeTo[] = new int[rutgers.getIntersections().length];
        boolean[] marked = new boolean[rutgers.getIntersections().length];
        Queue<Integer> q = new Queue<Integer>();
        q.enqueue(index);
        marked[index] = true;
        // path.add(rutgers.getIntersections()[index]);
        Intersection ptr2;
        while (!q.isEmpty()) {
            index = q.dequeue();
            Block ptr = rutgers.getAdjacencyList()[index];
            while (ptr != null) {
                ptr2 = ptr.other(rutgers.getIntersections()[index]);
                int index2 = rutgers.findIntersection(ptr2.getCoordinate());
                if (marked[index2] == false) {
                    q.enqueue(index2);
                    marked[index2] = true;
                    edgeTo[index2] = index;
                }
                ptr = ptr.getNext();
            }
        }
        int index3 = rutgers.findIntersection(end.getCoordinate());
        int sindex = rutgers.findIntersection(start.getCoordinate());
        while (index3 != sindex) {
            path.add(rutgers.getIntersections()[index3]);
            index3 = edgeTo[index3];
        }
        path.add(rutgers.getIntersections()[index3]);
        Collections.reverse(path);
        return path; // Replace this line, it is provided so the code compiles
    }

    /**
     * Finds the path with the least traffic from the start to the end intersection
     * using a variant of Dijkstra's algorithm.
     * The traffic is calculated as the sum of traffic of the blocks along the path.
     * 
     * What is this variant of Dijkstra?
     * - We are using traffic as a cost - we extract the lowest cost intersection
     * from the fringe.
     * - Once we add the target to the done set, we're done.
     * 
     * @param start The starting intersection
     * @param end   The destination intersection
     * @return The path with the least traffic, or an empty ArrayList if no path
     *         exists
     */
    public ArrayList<Intersection> fastestPath(Intersection start, Intersection end) {
        // WRITE YOUR CODE HERE
       // Coordinate s = start.getCoordinate();
     /* *  ArrayList <Intersection> done = new ArrayList<>();
        ArrayList <Intersection> fringe = new ArrayList<>();
       Intersection [] bob = rutgers.getIntersections();
       ArrayList <Coordinate> visited = new ArrayList<>();
       ArrayList<Double> distance = new ArrayList<>();
       ArrayList <Intersection> all = new ArrayList<>();
       ArrayList <Intersection> prev = new ArrayList<>();
       for(int i =0; i< bob.length; i++)
       {
        Intersection ptr = bob[i];
        all.add(ptr);
        distance.add(Double.POSITIVE_INFINITY);
       prev.add(null);
       }
       int sindex = all.indexOf(start);
       distance.set(sindex,0.0);
         fringe.add(start);
        while(!fringe.isEmpty())
        {
           int minIndex = 0;
           double minDistance = Double.POSITIVE_INFINITY;
           for(int i = 0; i < fringe.size();i++)
           {
            int idx = all.indexOf(fringe.get(i));
            if(distance.get(idx) < minDistance)
            {
                minDistance = distance.get(idx);
                minIndex = i;
            }
           }
           Intersection m = fringe.remove(minIndex);
           int mIndex = all.indexOf(m);
           done.add(m);
           Block ptr = rutgers.adj(rutgers.findIntersection(m.getCoordinate()));
           while(ptr!= null)
           {
            Intersection w = ptr.other(m);
            if(w!= null && !done.contains(w))
            {
                int wIndex = all.indexOf(w);
                double weight = ptr.getTraffic();
                double alt = distance.get(mIndex) + weight;
                if(distance.get(wIndex) == Double.POSITIVE_INFINITY)
                {
                    distance.set(wIndex,alt);
                    fringe.add(w);
                }
                else if( alt< distance.get(wIndex))
                {
                    distance.set(wIndex,alt);
                }
                prev.set(wIndex,m);
            
            }
            ptr = ptr.getNext();
           }
        }
           ArrayList<Intersection> path = new ArrayList<>();
           Intersection ptr2 = end;
           while(ptr2!=null)
           {
            path.add(ptr2);
            int index = all.indexOf(ptr2);
            ptr2 = prev.get(index);
           }
           Collections.reverse(path);
           return path;
            */
          /* */
            Intersection done[] = new Intersection[rutgers.getIntersections().length];
            ArrayList <Intersection> fringe = new ArrayList<>();
             Intersection [] prev = new Intersection[rutgers.getIntersections().length];
             double distance[] = new double[rutgers.getIntersections().length];
             for(int i =0;i < rutgers.getAdjacencyList().length;i++)
             {
                distance[i] = Double.POSITIVE_INFINITY;
                prev[i] = null;
             }
             distance[ rutgers.findIntersection(start.getCoordinate())] = 0;
             fringe.add(start);
             while(!fringe.isEmpty())
             {
                double temp = Double.MAX_VALUE;
                Intersection m = null;
                int index2 = -1;
                int index = 0;
                for(int i = 0; i< fringe.size();i++)
                {
                   index2 = rutgers.findIntersection(fringe.get(i).getCoordinate());
                   if(distance[index2] < temp)
                   {
                    index = index2;
                    temp = distance[index];
                    m = fringe.get(i);
                   }
                }
             

            fringe.remove(m);
            if(m == end)
            {
                break;
            }
            done[index] = m;
            Block ptr = rutgers.adj(index);
            
            while(ptr !=null)
            {
            
                Intersection target = ptr.other(m);
                int itarget  = rutgers.findIntersection(target.getCoordinate());
                if(done[itarget] == null)
                {
                    if(distance[itarget] == Double.POSITIVE_INFINITY)
                    {
                        distance[itarget] = distance[index] + ptr.getTraffic()* ptr.getTrafficFactor();
                        fringe.add(target);
                        prev[itarget] = m;
                    }
                    else if(distance[itarget] > (distance[index] + ptr.getTraffic()* ptr.getTrafficFactor()))
                    {
                        distance[itarget] = distance[index] + ptr.getTraffic()* ptr.getTrafficFactor();
                        prev[itarget] = m;
                    }
                }
                ptr = ptr.getNext();
            }
            
             }
             ArrayList<Intersection> path = new ArrayList<>();
             int eindex = rutgers.findIntersection(end.getCoordinate());
             Intersection eptr = end;
             while(eptr!= null)
             {
            path.add(0,eptr);
            eindex = rutgers.findIntersection(eptr.getCoordinate());
            eptr = prev[eindex];
             }
             Collections.reverse(path);
             return path;
        }
        
 // Replace this line, it is provided so the code compiles

    /**
     * Calculates the total length, average experienced traffic factor, and total
     * traffic for a given path of blocks.
     * 
     * You're given a list of intersections (vertices); you'll need to find the edge
     * in between each pair.
     * 
     * Compute the average experienced traffic factor by dividing total traffic by
     * total length.
     * 
     * @param path The list of intersections representing the path
     * @return A double array containing the total length, average experienced
     *         traffic factor, and total traffic of the path (in that order)
     * In this method, you will be given a path of Intersections – we are going from intersection to intersection. However, this involves traversing an edge (which adds on traffic and length to this path).  In your algorithm, use .equals when comparing intersections, not ==. 

For each pair of intersections on this incident path: 

Remember that we can find where an intersection lies by accessing a block’s first and last endpoints to compare against. 
You can then use the .adj method on the intersection you found to traverse.
Find the incident edge that matches where this intersection lies, and increment the total length and total traffic.
You’ll return an array of 3 doubles: index 0 stores the total length, index 1 the total traffic divided by the total length, 
and index 2 the total traffic. 
     */
    public double[] pathInformation(ArrayList<Intersection> path) {
        // WRITE YOUR CODE HERE
        double length = 0.0;
        double traffic = 0.0;
        for(int i = 0;i < path.size()-1;i++)
        {
           Intersection a = path.get(i);
           Intersection b = path.get(i+1);
           int index = rutgers.findIntersection(a.getCoordinate());
           Block ptr = rutgers.adj(index);
           while (ptr!= null) {
            Intersection other = ptr.other(a);
            if(other.equals(b))
            {
                length+=ptr.getLength();
                traffic +=ptr.getTraffic();
                
                break;
            }
            ptr = ptr.getNext();
           }
        }
        return new double[] { length, traffic/length, traffic }; // Replace this line, it is provided so the code compiles
    }

    /**
     * Calculates the Euclidean distance between two coordinates.
     * PROVIDED - do not modify
     * 
     * @param a The first coordinate
     * @param b The second coordinate
     * @return The Euclidean distance between the two coordinates
     */
    private double coordinateDistance(Coordinate a, Coordinate b) {
        // PROVIDED METHOD

        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * **DO NOT MODIFY THIS METHOD**
     * 
     * Calculates and returns a randomized traffic factor for the block based on a
     * Gaussian distribution.
     * 
     * This method generates a random traffic factor to simulate varying traffic
     * conditions for each block:
     * - < 1 for good (faster) conditions
     * - = 1 for normal conditions
     * - > 1 for bad (slower) conditions
     * 
     * The traffic factor is generated with a Gaussian distribution centered at 1,
     * with a standard deviation of 0.2.
     * 
     * Constraints:
     * - The traffic factor is capped between a minimum of 0.5 and a maximum of 1.5
     * to avoid extreme values.
     * 
     * @param block The block for which the traffic factor is calculated
     * @return A randomized traffic factor for the block
     */
    public double blockTrafficFactor(Block block) {
        double rand = StdRandom.gaussian(1, 0.2);
        rand = Math.max(rand, 0.5);
        rand = Math.min(rand, 1.5);
        return rand;
    }

    /**
     * Calculates the traffic on a block by the product of its length and its
     * traffic factor.
     * 
     * @param block The block for which traffic is being calculated
     * @return The calculated traffic value on the block
     */
    public double blockTraffic(Block block) {
        // PROVIDED METHOD

        return block.getTrafficFactor() * block.getLength();
    }

    public Network getRutgers() {
        return rutgers;
    }

}
