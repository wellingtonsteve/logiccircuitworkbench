package sim;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Circuit extends Component{

	public Circuit(String type) {
		super(type);
	}

	private List<Component> listComponents = new LinkedList<Component>();
	
	public void addComponent(Component component)
	{
		this.listComponents.add(component);
	}
	
	public boolean removeComponent(Component component)
	{
		return this.listComponents.remove(component);
	}
	
	public Iterator<Component> getComponentIterator()
	{
		return this.listComponents.iterator();
	}
	
}
