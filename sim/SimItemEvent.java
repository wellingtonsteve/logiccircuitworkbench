/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sim;

/**
 *
 * @author Stephen
 */
public abstract class SimItemEvent implements Comparable<SimItemEvent> {
    abstract void RunEvent();
    private long time;

    public long getTime() {
        return time;
    }

    public SimItemEvent(long time) {
        this.time = time;
    }

    public int compareTo(SimItemEvent s) {
        if(time < s.getTime()) return -1;
        else if(time > s.getTime()) return 1;
        else return 0;
    }
}
