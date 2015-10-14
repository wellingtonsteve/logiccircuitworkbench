package sim;

import java.util.*;

/**
 * CollectionPriorityQueue's are similar to normal priority queues. An instance of the class will 
 * accept objects of type V, along with a key of type K, where K implements Comparable.  'V's will
 * be ordered in the CollectionPriorityQueue relative to this 'K'.
 *
 * When pollV is called on the CollectionPriorityQueue, a collection of all the 'V's with the 
 * highest priority 'K' will be returned in one go.
 *
 * This is used by the Simulator class to manage simulation events. Events are added, along with the
 * time at which they should be run.  The simulator then 'poll's successive Collections of events as
 * it reaches a particular time in the simulation.
 */
public class CollectionPriorityQueue<K extends Comparable<K>, V> {

    /**
     * An instance of the class uses the following two objects to manage objects (and their
     * corresponding keys) that have been added.
     * queueK is a standard priority queue that contains the keys (of type K) for which a 
     * corresponding 'V' has been added.
     * mapKV is a map from objects of type K to a collection of objects of type V.
     * Invariant: A 'K' exists in queueK if and only if a mapping from that 'K' to a non-empty
     * collection of 'V's exists in mapKV.
     */
    PriorityQueue<K> queueK = new PriorityQueue<K>();
    Map<K, Collection<V>> mapKV = new HashMap<K, Collection<V>>();

    /**
     * Accept a key of type K and a value of type V to add to the queue
     */
    void offer(K key, V value) {
        //Find out whether a V has already been added with this particular K.  If so just add the V 
        //to the collection already in mapKV
        if (mapKV.containsKey(key)) {
            mapKV.get(key).add(value);
        } else {
            //Otherwise add the K to queueK, and the V to a new collection which we add to mapKV.
            Collection<V> newCollection = new ArrayList<V>();
            newCollection.add(value);
            mapKV.put(key, newCollection);
            queueK.add(key);
        }
    }

    /**
     * Peek at the next K in the CollectionPriorityQueue, or null if there isn't one.
     */
    public K peekK() {
        if (!this.isEmpty()) {
            return queueK.peek();
        } else {
            return null;
        }
    }

    /**
     * Return the collection of Vs that is next to come off the CollectionPriorityQueue.
     */
    public Collection<V> pollV() {
        if (!this.isEmpty()) {
            return mapKV.remove(queueK.poll());
        } else {
            return null;
        }
    }

    /**
     * Anything in the CollectionPriorityQueue?
     */
    public boolean isEmpty() {
        return queueK.isEmpty();
    }
}
