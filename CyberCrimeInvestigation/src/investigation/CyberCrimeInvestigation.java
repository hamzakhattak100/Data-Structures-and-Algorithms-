package investigation;

import java.util.ArrayList;

/*  
 * This class represents a cyber crime investigation.  It contains a directory of hackers, which is a resizing
 * hash table. The hash table is an array of HNode objects, which are linked lists of Hacker objects.  
 * 
 * The class contains methods to add a hacker to the directory, remove a hacker from the directory.
 * You will implement these methods, to create and use the HashTable, as well as analyze the data in the directory.
 * 
 * @author Colin Sullivan
 */
public class CyberCrimeInvestigation {

    private HNode[] hackerDirectory;
    private int numHackers = 0;

    public CyberCrimeInvestigation() {
        hackerDirectory = new HNode[10];
    }

    /**
     * Initializes the hacker directory from a file input.
     * 
     * @param inputFile
     */
    public void initializeTable(String inputFile) {
        // DO NOT EDIT
        StdIn.setFile(inputFile);
        while (!StdIn.isEmpty()) {
            addHacker(readSingleHacker());
        }
    }

    /**
     * Reads a single hackers data from the already set file,
     * Then returns a Hacker object with the data, including
     * the incident data.
     * 
     * StdIn.setFile() has already been called for you.
     * 
     * @param inputFile The name of the file to read hacker data from.
     */
    public Hacker readSingleHacker() {
        // WRITE YOUR CODE HERE

        String name = StdIn.readLine();
        String address = StdIn.readLine();
        String location = StdIn.readLine();
        String os = StdIn.readLine();
        String server = StdIn.readLine();
        String date = StdIn.readLine();
        String url = StdIn.readLine();
        Incident bob = new Incident(os, server, date, location, address, url);
        Hacker bob2 = new Hacker(name);
        bob2.addIncident(bob);
        return bob2;
        // Replace this line
    }

    /**
     * Adds a hacker to the directory. If the hacker already exists in the
     * directory,
     * instead adds the given Hacker's incidents to the existing Hacker's incidents.
     * 
     * After a new insertion (NOT if a hacker already exists), checks if the number
     * of
     * hackers in the table is >= table length divided by 2. If so, calls resize()
     * 
     * @param toAdd
     */
    public void addHacker(Hacker toAdd) {
        // WRITE YOUR CODE HERE
        int index = toAdd.hashCode() % hackerDirectory.length;
        if (hackerDirectory[index] == null) {
            HNode hacker = new HNode(toAdd);
            hackerDirectory[index] = hacker;
            numHackers += 1;
            if (numHackers >= hackerDirectory.length / 2) {
                resize();
            }
            return;
        }
        HNode ptr = hackerDirectory[index];
        while (true) {
            if (ptr.getHacker().getName().equals(toAdd.getName())) {
                ptr.getHacker().getIncidents().addAll(toAdd.getIncidents());
                return;
            }
            if (ptr.getNext() == null) {
                break;
            }
            ptr = ptr.getNext();
        }
        HNode hacker2 = new HNode(toAdd);
        ptr.setNext(hacker2);
        numHackers += 1;
        if (numHackers >= hackerDirectory.length / 2) {
            resize();
        }
    }

    /**
     * Resizes the hacker directory to double its current size. Rehashes all hackers
     * into the new doubled directory.
     * This method resizes the existing hackerDirectory Hash Table, to double the
     * current length.
     * Create a temp HNode[] variable, and set it to the existing hackerDirectory
     * array – this will be the old array that we will reference. Since each Hacker
     * gets reinserted in addHacker(), numHackers will be reset to 0.
     * Then, set the hackerDirectory instance variable to a brand new HNode[]
     * object, with its size as double the old hackerDirectory’s length.
     * Finally, loop through your old hackerDirectory (stored in your temp
     * variable), and call your addHacker() method on each hacker to reinsert them
     * into your larger table.
     */
    private void resize() {
        // WRITE YOUR CODE HERE
        HNode[] temp = hackerDirectory;
        numHackers = 0;
        HNode[] bob = new HNode[hackerDirectory.length * 2];
        hackerDirectory = bob;
        for (int i = 0; i < temp.length; i++) {
            HNode ptr = temp[i];
            while (ptr != null) {
                addHacker(ptr.getHacker());
                ptr = ptr.getNext();
            }
        }
    }

    /**
     * Searches the hacker directory for a hacker with the given name.
     * Returns null if the Hacker is not found
     * This method searches for a given Hacker name in the hackerDirectory, and
     * returns that Hacker object if it is found. We will test this method
     * separately, but this is a helper for other methods.
     * 
     * If it is not found, return null.
     * 
     * You can determine which index the Hacker should be at with:
     * 
     * Math.abs(toSearch.hashCode()) % hackerDirectory.length
     * 
     * 
     * @param toSearch
     * @return The hacker object if found, null otherwise.
     */
    public Hacker search(String toSearch) {
        // WRITE YOUR CODE HERE
        int index = Math.abs(toSearch.hashCode()) % hackerDirectory.length;
        HNode ptr = hackerDirectory[index];
        while (ptr != null) {
            if (ptr.getHacker().getName().equals(toSearch)) {
                return ptr.getHacker();
            }
            ptr = ptr.getNext();
        }
        return null;
    }

    /**
     * Removes a hacker from the directory. Returns the removed hacker object.
     * This method searches for a given Hacker name and removes that Hacker object
     * (and corresponding HNode) from the hackerDirectory if it is found.
     * 
     * You should attempt to locate the specified hacker the same as in search().
     * This time, if you find them perform Singly Linked-List deletion on that
     * HNode.
     * 
     * If you find and succesfully remove the Hacker, decrement numHackers by one.
     * Return the Hacker object for the hacker removed, or if you don’t find the
     * hacker to remove then return null.
     * 
     * 
     * If the hacker is not found, returns null.
     * 
     * @param toRemove
     * @return The removed hacker object, or null if not found.
     */
    public Hacker remove(String toRemove) {
        // WRITE YOUR CODE HERE
        int index = Math.abs(toRemove.hashCode()) % hackerDirectory.length;
        HNode ptr = hackerDirectory[index];
        HNode prev = null;
        while (ptr != null) {
            if (prev == null && ptr.getHacker().getName().equals(toRemove)) {
                hackerDirectory[index] = ptr.getNext();
                numHackers -= 1;
                return ptr.getHacker();
            } else if (ptr.getHacker().getName().equals(toRemove)) {
                if (prev == null) {
                    ptr.setNext(ptr.getNext().getNext());
                    numHackers -= 1;
                    return ptr.getHacker();
                } else if (ptr.getNext() == null) {
                    prev.setNext(null);
                    numHackers -= 1;
                    return ptr.getHacker();
                }
                prev.setNext(ptr.getNext());
                numHackers -= 1;
                return ptr.getHacker();
            }
            prev = ptr;
            ptr = ptr.getNext();
        }
        return null;
    }

    /**
     * Merges two hackers into one based on number of incidents.
     * 
     * @param hacker1 One hacker
     * @param hacker2 Another hacker to attempt merging with
     *                This method takes two Hacker names as parameters, and attempts
     *                to merge them into one Hacker. We would do this if we find out
     *                that both hackers are actually the same.
     * 
     *                You can use your search() method to find both Hacker objects
     *                in hackerDirectory. If either is not found, return false.
     * 
     *                If both hackers are found, attempt to merge the hacker with
     *                LESS incidents into the one with more.
     *                If they have the same amount of incidents, merge hacker2 into
     *                hacker1.
     * 
     *                To merge two hackers, add all incidents from one hacker
     *                (Hacker A) into the hacker you wish to merge into (Hacker B).
     *                Then, add Hacker A’s name as an alias for Hacker B. Finally,
     *                remove Hacker A, using your remove method.
     *                Return true if successful.
     * @return True if the merge was successful, false otherwise.
     */
    public boolean mergeHackers(String hacker1, String hacker2) {
        // WRITE YOUR CODE HERE
        if (search(hacker1) != null && (search(hacker2) != null)) {
            Hacker h1 = search(hacker1);
            Hacker h2 = search(hacker2);
            if (h1.getIncidents().size() > h2.getIncidents().size()
                    || h1.getIncidents().size() == h2.getIncidents().size()) {
                for (int i = 0; i < h2.getIncidents().size(); i++) {
                    h1.getIncidents().add(h2.getIncidents().get(i));
                }
                h1.addAlias(hacker2);
                remove(hacker2);
                return true;
            }
            // else (h1.getIncidents().size() < h2.getIncidents().size())
            else {
                for (int i = 0; i < h1.getIncidents().size(); i++) {
                    h2.getIncidents().add(h1.getIncidents().get(i));
                }
                h2.addAlias(hacker1);
                remove(hacker1);
                return true;
            }
        } else {
            return false;
        }
        // Replace this line
    }

    /**
     * Gets the top n most wanted Hackers from the directory, and
     * returns them in an arraylist.
     * 
     * You should use the provided MaxPQ class to do this. You can
     * add all hackers, then delMax() n times, to get the top n hackers.
     * In this method, you’ll find the most wanted hackers in the hackerDirectory,
     * and return them in an ArrayList.
     * The number of Hackers in your list is determined by the parameter “n”.
     * (i.e. return the Top N hackers, such as Top 10, Top 50, Top 100, etc…).
     * Hacker ranking is determined by number of incidents, and ties are broken by a
     * Hacker’s name
     * (this is done for you in the Hacker compareTo() method).
     * 
     * To do this, we can use a Priority Queue, specifically a Max Heap. We provide
     * a MaxPQ.java class for this purpose,
     * which you can use.
     * 
     * Create a new MaxPQ object, which holds Hacker objects (MaxPQ uses generics,
     * so just like with ArrayLists you’ll need to specify a type). USE the MaxPQ
     * class from lecture that’s provided to you,
     * DO NOT use other implementations or implement a priority queue from scratch.
     * 
     * Insert every Hacker in the hackerDirectory HashTable into your MaxPQ object.
     * 
     * Create an ArrayList to hold the top N hackers.
     * 
     * Then, call delMax() N times, and add each deleted hacker to your ArrayList.
     * Since a MaxPQ orders largest to smallest, this will delete and return the top
     * N hackers.
     * You don’t need to handle comparisons – the compareTo method in the Hacker
     * class compares by incidents.
     * 
     * Finally, return your Most Wanted ArrayList.
     * 
     * @param n
     * @return Arraylist containing top n hackers
     */
    public ArrayList<Hacker> getNMostWanted(int n) {
        // WRITE YOUR CODE HERE
        MaxPQ<Hacker> bob = new MaxPQ<Hacker>();
        // hackerDirectory
        for (int i = 0; i < hackerDirectory.length; i++) {
            if (hackerDirectory[i] != null) {
                HNode ptr = hackerDirectory[i];
                while (ptr != null) {
                    bob.insert(ptr.getHacker());
                    ptr = ptr.getNext();
                }

                // bob.insert(hackerDirectory[i].getHacker());
            }
        }
        ArrayList<Hacker> bob2 = new ArrayList<>();
        {
            while (n > 0) {
                bob2.add(bob.delMax());
                n -= 1;
            }
        }
        return bob2; // Replace this line
    }

    /**
     * Gets all hackers that have been involved in incidents at the given location.
     * 
     * You should check all hackers, and ALL of each hackers incidents.
     * You should not add a single hacker more than once.
     * In this method, you’ll find all Hackers with a given location,
     * and return them in an ArrayList. Create an ArrayList of Hackers, to store any
     * matching Hackers.
     * Iterate through all Hackers in the hackerDirectory,
     * if their location matches the given location,
     * then add them to your list. Finally, return your list of hackers for the
     * given location.
     * 
     * 
     * 
     * @param location
     * @return Arraylist containing all hackers who have been involved in incidents
     *         at the given location.
     */
    public ArrayList<Hacker> getHackersByLocation(String location) {
        // WRITE YOUR CODE HERE
        ArrayList<Hacker> bob = new ArrayList<>();
        for (int i = 0; i < hackerDirectory.length; i++) {
            if (hackerDirectory[i] != null) {
                HNode ptr = hackerDirectory[i];
                while (ptr != null) {
                    for (int i2 = 0; i2 < ptr.getHacker().getIncidents().size(); i2++) {

                        if (ptr.getHacker().getIncidents().get(i2).getLocation().equals(location)) {

                            bob.add(ptr.getHacker());
                            break;
                        }
                    }
                    ptr = ptr.getNext();
                }

            }
        }
        return bob; // Replace this line
    }

    /**
     * PROVIDED--DO NOT MODIFY!
     * Outputs the entire hacker directory to the terminal.
     */
    public void printHackerDirectory() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.hackerDirectory.length; i++) {
            HNode headHackerNode = hackerDirectory[i];
            while (headHackerNode != null) {
                if (headHackerNode.getHacker() != null) {
                    sb.append(headHackerNode.getHacker().toString()).append("\n");
                    ArrayList<Incident> incidents = headHackerNode.getHacker().getIncidents();
                    for (Incident incident : incidents) {
                        sb.append("\t" + incident.toString()).append("\n");
                    }
                }
                headHackerNode = headHackerNode.getNext();
            }
        }
        return sb.toString();
    }

    public HNode[] getHackerDirectory() {
        return hackerDirectory;
    }
}
