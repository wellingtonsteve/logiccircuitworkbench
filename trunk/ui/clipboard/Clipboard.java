package ui.clipboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class Clipboard {

    private Stack<SelectableComponent> clipboard = new Stack<SelectableComponent>();
    private Stack<Integer> clipboardPointer = new Stack<Integer>();
    private Stack<ClipboardType> clipboardTypes = new Stack<ClipboardType>();
    
    public Clipboard(){
        clipboardPointer.push(0);
    }
    
    /**
     * Add a selection of components to the clipboard for later use.
     * @param col The components to be added.
     */
    public void addSetToClipboard(Collection<SelectableComponent> col, ClipboardType ct){
        clipboardPointer.push(clipboard.size());
        clipboardTypes.push(ct);
        for(SelectableComponent sc: col){
            clipboard.add(sc.copy());
        }
    }
    
    /**
     * @return The last selection of items that was added to the clipboard
     */
    public Collection<SelectableComponent> getLastClipboardItem(){
        int n = clipboard.size();
        Collection<SelectableComponent> retval = new LinkedList<SelectableComponent>();
        for(SelectableComponent sc: clipboard.subList(clipboardPointer.peek(), n)){
            retval.add(sc.copy());
        }
        if(clipboardTypes.peek().equals(ClipboardType.Cut)){
            removeLastClipboardItem();
        }
        return retval;
    }
    
    /**
     * Remove the last selection of items that was added to the clipboard. Occurs
     * when a cut/copy action is undone, or a cut item is pasted.
     */
    public void removeLastClipboardItem(){
        int n = clipboard.size();
        Collection<SelectableComponent> lastSet = new LinkedList<SelectableComponent>();
        lastSet.addAll(clipboard.subList(clipboardPointer.peek(), n));
        clipboard.removeAll(lastSet);
        clipboardPointer.pop();
        clipboardTypes.pop();
    }   
}
