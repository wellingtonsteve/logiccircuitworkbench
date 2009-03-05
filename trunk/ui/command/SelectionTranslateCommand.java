package ui.command;

import java.util.LinkedList;
import ui.Editor;
import ui.components.SelectableComponent;

/**
 *
 * @author matt
 */
public class SelectionTranslateCommand extends Command {
    private LinkedList<Integer> dxs = new LinkedList<Integer>();
    private LinkedList<Integer> dys = new LinkedList<Integer>();
    private LinkedList<SelectableComponent> selection = new LinkedList<SelectableComponent>();
    private boolean first = true;
    
    public void translate(SelectableComponent sc, int dx, int dy){
        selection.add(sc);
        dxs.add(dx);
        dys.add(dy);
    }
    
    @Override
    protected void perform(Editor editor) {
        for(int i=0; i<selection.size(); i++){
            if(first){
                SelectableComponent sc = selection.get(i);
                sc.translate(dxs.get(i), dys.get(i), true);     
                dxs.add(i, sc.getOrigin().x-sc.getUnfixedOrigin().x+dxs.get(i));
                dxs.remove(i+1);
                dys.add(i, sc.getOrigin().y-sc.getUnfixedOrigin().y+dys.get(i));
                dys.remove(i+1);
            } else {
                SelectableComponent sc = selection.get(i);
                sc.translate(dxs.get(i), dys.get(i), true);
            }
        }
        first=false;
        canUndo = true;
    }

    @Override
    protected void undoEffect(Editor editor) {
        for(int i=0; i<selection.size(); i++){
            SelectableComponent sc = selection.get(i);
            sc.translate(-dxs.get(i), -dys.get(i), true);
        }
        canUndo = false;
    }
    
    @Override
    public String getName() {
        return "Move Selection";
    }

}
