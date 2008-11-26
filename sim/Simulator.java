package sim;

public class Simulator
{
    // The circuit we are going to simulate - created by the constructor. All 
    // SimItems (components, sub-circuits etc..) are added to this.
    private Circuit mainCircuit;
    // The time we are in nanoseconds in the simulation - will let us run the 
    // simulation for about 290 years (probably long enough!)
    private long simulationTime;
    
    //some priority queue object here

    public Simulator()
    {
        // Create the circuit that the we'll be simulating - this simulation
        // object is passed to the 
        this.mainCircuit = new Circuit(this);
    }

    public void addOutputChange(OutputPin output, long changeTime, State newValue)
    {
        //add these details to the priority queue
    }
    
    public long getSimulationTime()
    {
        return simulationTime;
    }
}
