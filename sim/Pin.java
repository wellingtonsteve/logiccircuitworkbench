package sim;

public abstract class Pin
{
    private SimItem owner;
    private String name;

    public Pin(SimItem owner, String name)
    {
        this.owner = owner;
        this.name = name;
    }

    public SimItem getOwner()
    {
        return this.owner;
    }

    public String getName()
    {
        return this.name;
    }
}
