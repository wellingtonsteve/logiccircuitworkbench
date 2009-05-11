package ui.clipboard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JComponent;
import ui.components.SelectableComponent;

/** A Selectable Component clipboard. Selections of components are stored on a 
 * stack. A second stack records whether each addition was the result of a 
 * cut/copy action. @author matt */
public class Clipboard {

    private Stack<Collection<SelectableComponent>> clipboard =
            new Stack<Collection<SelectableComponent>>();
    private Stack<ClipboardType> lastAction = new Stack<ClipboardType>();
    private LinkedList<JComponent> selectionlisteners = 
            new LinkedList<JComponent>();
    private LinkedList<JComponent> pastelisteners =new LinkedList<JComponent>();
    
    /** Cut a selection of components */
    public void cut(Collection<SelectableComponent> col){
        lastAction.push(ClipboardType.Cut);
        clipboard.push(col);
        updatePasteListeners();
    }
    
    /** Copy a selection of components */
    public void copy(Collection<SelectableComponent> col){
        lastAction.push(ClipboardType.Copy);
        clipboard.push(col);
        updatePasteListeners();
    }
    
    /** Paste the last cut/copied selection */
    public Collection<SelectableComponent> paste(){
        LinkedList<SelectableComponent> retval =
                new LinkedList<SelectableComponent>();
        for(SelectableComponent sc: clipboard.peek()){
            retval.add(sc.copy());
        }
        return retval;                
    }
       
    /** @return The last selection of items that was added to the clipboard */
    public ClipboardType getNextAction(){
        return lastAction.peek();
    }
    
    /** Remove the last selection of items that was added to the clipboard. 
     * Occurs when a cut/copy action is undone, or a cut item is pasted.*/
    public void removeLastClipboardItem(){
        clipboard.pop();
        lastAction.pop();
        if(!canPaste()){
            for(JComponent c: pastelisteners){
                c.setEnabled(false);
            }
        }
    }   
    
    /** @return true if and only if, the clipboard contains any selections.*/
    private boolean canPaste(){
        return !lastAction.isEmpty();
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

    /** Re-enable any paste listeners after a collection has been added */
    private void updatePasteListeners() {
        if (canPaste()) {
            for (JComponent c : pastelisteners) {
                c.setEnabled(true);
            }
        }
    }
}
