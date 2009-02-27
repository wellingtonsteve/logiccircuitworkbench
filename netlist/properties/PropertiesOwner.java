package netlist.properties;

/**
 *
 * @author matt
 */
public interface PropertiesOwner {

    public Properties getProperties();
    public void setProperties(Properties properties);
    public String getKeyName();
    
}
