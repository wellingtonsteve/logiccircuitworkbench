package ui.clipboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JComponent;
import ui.components.SelectableComponent;

/**
 * A Selectable Component clipboard. Selections of components are stored on a stack
 * and groups are identified by pointers on a seperate stack. A third stack is also
 * maintained to record whether each addition was the result of a copy/paste action.
 * 
 * @author matt
 */
public class Clipboard {

    private Stack<SelectableComponent> clipboard = new Stack<SelectableComponent>();
    private Stack<Integer> clipboardPointer = new Stack<Integer>();
    private Stack<ClipboardType> clipboardTypes = new Stack<ClipboardType>();
    private LinkedList<JComponent> selectionlisteners = new LinkedList<JComponent>();
    private LinkedList<JComponent> pastelisteners = new LinkedList<JComponent>();
    
    public Clipboard(){
        clipboardPointer.push(0);
    }
    
    /** Add a selection of components to the clipboard for later use.
     * @param col The components to be added
     * @param ct The type of the clipboard action */
    public void addSetToClipboard(Collection<SelectableComponent> col, ClipboardType ct){
        clipboardPointer.push(clipboard.size());
        clipboardTypes.push(ct);
        for(SelectableComponent sc: col){
            clipboard.add(sc);
        }
        if(canPaste()){
            for(JComponent c: pastelisteners){
                c.setEnabled(true);
            }
        }
    }
    
    /** @return The last selection of items that was added to the clipboard */
    public Collection<SelectableComponent> getLastClipboardItem(){
        int n = clipboard.size();
        LinkedList<SelectableComponent> retval = new LinkedList<SelectableComponent>();
        for(SelectableComponent sc: clipboard.subList(clipboardPointer.peek(), n)){
            retval.add(sc.copy());
        }
        if(clipboardTypes.peek().equals(ClipboardType.Cut)){
            removeLastClipboardItem();
        }
        return retval;
    }
    
    /** Remove the last selection of items that was added to the clipboard. Occurs
     * when a cut/copy action is undone, or a cut item is pasted.*/
    public void removeLastClipboardItem(){
        int n = clipboard.size();
        Collection<SelectableComponent> lastSet = new LinkedList<SelectableComponent>();
        lastSet.addAll(clipboard.subList(clipboardPointer.peek(), n));
        clipboard.removeAll(lastSet);
        clipboardPointer.pop();
        clipboardTypes.pop();
        if(!canPaste()){
            for(JComponent c: pastelisteners){
                c.setEnabled(false);
            }
        }
    }   
    
    /** @return true if and only if, the clipboard contains any selections.*/
    private boolean canPaste(){
        return !clipboardTypes.isEmpty();
    }
     
    /** Tell the clipboard when a selection is made, so that it can enable/disable 
     * any clipboard actions that require a selection. */
    public void setHasSelection(boolean hasSelection){
        for(JComponent c: selectionlisteners){
            c.setEnabled(hasSelection);
        }
    }
    
    /** Add a #JComponent which can call a cut/copy/paste/delete operation. 
     * Selection Listeners (Cut/Copy/Delete) are disabled when there is no 
     * active selection. */
    public void addSelectionListener(JComponent selectionlistener){
        selectionlisteners.add(selectionlistener);
    }
    
    /** Add a #JComponent which can call a paste operation. 
     * Paste listeners are disabled when the clipboard is empty. */
    public void addPasteListener(JComponent pastelistener){
        pastelisteners.add(pastelistener);
    }
}
