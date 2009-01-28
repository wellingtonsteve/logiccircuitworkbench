/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim;

import java.util.*;

/**
 * 
 * There is an invariant for this class that if a K in queueK, it also exists as a key in mapKV
 * 
 * @author Stephen
 */
public class CollectionPriorityQueue<K extends Comparable<K>,V> {
    PriorityQueue<K> queueK = new PriorityQueue<K>();
    Map<K,Collection<V>> mapKV = new HashMap<K, Collection<V>>();
    
    void offer(K key, V value){
        if(mapKV.containsKey(key)) mapKV.get(key).add(value);
        else {
            Collection<V> newCollection = new ArrayList<V>();
            newCollection.add(value);        
            mapKV.put(key, newCollection);
            queueK.add(key);
        }
    }
    
    public K peekK(){
        if(!this.isEmpty()) return queueK.peek();
        else return null;
    }
    
    public Collection<V> peekV(){
        if(!this.isEmpty()) return mapKV.get(queueK.peek());
        else return null;
    }
    
    public Collection<V> pollV(){
        if(!this.isEmpty()) return mapKV.remove(queueK.poll());
        else return null;
    }
    
    public boolean isEmpty(){
        return queueK.isEmpty();
    }
    
}
