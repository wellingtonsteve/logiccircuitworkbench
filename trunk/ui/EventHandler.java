package ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JInternalFrame;
import javax.swing.tree.TreePath;
import ui.command.*;
import ui.components.SelectableComponent;
import ui.components.VisualComponent;

/**
 *
 * @author Matt
 */
public class EventHandler {
    public Editor view;
    private boolean playPause = false;
    private CommandHistory cmdHist;
    private WindowAdapter loggerWindowListener = new WindowAdapter(){
        @Override
        public void windowClosed(WindowEvent e) {
            view.RecordButton.setSelected(view.getActiveCircuit().getLoggerWindow().isShowing()); 
        }
    };
    
    public EventHandler(Editor view){
        this.view = view;
        this.cmdHist = new CommandHistory(view);
    }

    public void SelectionToolClicked(java.awt.event.MouseEvent evt){
         if(view.getActiveCircuit()!=null){
            view.toggleToolboxButton(view.Selection);
            view.getActiveCircuit().setCurrentTool("Select");
        }
    }

    public void WireMouseClicked(java.awt.event.MouseEvent evt) {                                  
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){ 
            view.toggleToolboxButton(view.Wire);
            view.getActiveCircuit().removeUnfixedComponents();
            String componentName = "Wire";

            CreateComponentCommand ccc = new CreateComponentCommand(
                    view.getActiveCircuit(), 
                    componentName,
                    view.getOptionsPanelComponentRotation(),
                    SelectableComponent.getDefaultOrigin());
            view.getActiveCircuit().doCommand(ccc);

            view.optionsPanel.setVisible(false);
            view.getActiveCircuit().setCurrentTool(componentName);
            view.RotateRight.setEnabled(false);
            view.RotateLeft.setEnabled(false);
        }
    }                                 

    public void ComponentSelectionTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {                                                    
        if(view.getActiveCircuit()!=null){
            view.RotateRight.setEnabled(true);
            view.RotateLeft.setEnabled(true);    
            view.toggleToolboxButton(view.Selection);
            view.Selection.setSelected(false);

            TreePath currentSelection = view.ComponentSelectionTree.getSelectionPath();
            if(currentSelection != null){
                // Implode from array path to string delimited by periods.
                Object[] nameArray = currentSelection.getPath();
                String componentName = new String();
                for(int i = 0; i<nameArray.length; i++){
                    componentName += nameArray[i] + ".";
                }
                componentName = componentName.substring(0, componentName.length() - 1);

                if(view.isValidComponent(componentName)){
                    view.getActiveCircuit().resetActiveComponents();
                    view.getActiveCircuit().setCurrentTool(componentName);
                    CreateComponentCommand ccc = new CreateComponentCommand(
                            view.getActiveCircuit(),
                            componentName,
                            view.getOptionsPanelComponentRotation(),
                            SelectableComponent.getDefaultOrigin());
                    view.getActiveCircuit().doCommand(ccc);
                    ((VisualComponent)ccc.getComponent()).addLogicalComponentToCircuit();

                    // Set Options panel (Preview, Component Specific Options etc.)
                    view.setOptionsPanelComponent(ccc.getComponent());
                    view.optionsPanel.setVisible(true);
                    view.optionsPanel.repaint();         
                    view.repaint();
                }       
            }   
        }
    }                                                   

    public void UndoActionPerformed(java.awt.event.ActionEvent evt) {                                     
        if(view.getActiveCircuit()!=null){ view.getActiveCircuit().getCommandHistory().undo();}
    }                                    

    public void RedoActionPerformed(java.awt.event.ActionEvent evt) {                                     
        if(view.getActiveCircuit()!=null){ view.getActiveCircuit().getCommandHistory().redo();}
    }                                    

    public void CutActionPerformed(java.awt.event.ActionEvent evt) {                                    
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new SelectionCutCommand());
        }    
    }                                   

    public void CopyActionPerformed(java.awt.event.ActionEvent evt) {                                     
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new SelectionCopyCommand());
        }
    }                                    

    public void PasteActionPerformed(java.awt.event.ActionEvent evt) {                                      
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new SelectionPasteCommand());
        }
    }                                     

    public void SelectAllActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().selectAllComponents();
        }
    }                                         

    public void ExitActionPerformed(java.awt.event.ActionEvent evt) {                                     
        // Perform close action for each window
        for(JInternalFrame jif: view.DesktopPane.getAllFrames()){
                jif.doDefaultCloseAction();
        }    
        // Check if any closes were cancelled
        int openCircuits = view.DesktopPane.getAllFrames().length;  
        if(openCircuits == 0){
            System.exit(0);
        }
    }                                    

    public void ClearCircuitMouseClicked(java.awt.event.MouseEvent evt) {                                          
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new ClearCircuitCommand());
        }
    }                                         

    public void OpenActionPerformed(java.awt.event.ActionEvent evt) {                                     
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new FileOpenCommand());
        }
    }                                    

    public void SaveAsActionPerformed(java.awt.event.ActionEvent evt) {                                       
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new FileSaveAsCommand());
        }
    }                                      

    public void UndoButtonMouseClicked(java.awt.event.MouseEvent evt) {                                        
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().getCommandHistory().undo();
        }
    }                                       

    public void RedoButtonMouseClicked(java.awt.event.MouseEvent evt) {                                        
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().getCommandHistory().redo();
        }
    }                                       

    public void RotateLeftMouseClicked(java.awt.event.MouseEvent evt) {                                        
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new RotateLeftCommand());
        }
    }                                       

    public void RotateRightMouseClicked(java.awt.event.MouseEvent evt) {                                         
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new RotateRightCommand());
        }
    }                                        

    public void DeleteSelectionMouseClicked(java.awt.event.MouseEvent evt) {                                             
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new SelectionDeleteCommand());
        }
    }                                            

    public void DeleteActionPerformed(java.awt.event.ActionEvent evt) {                                       
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new SelectionDeleteCommand());
        }
    }                                      

    public void OpenFileButtonMouseClicked(java.awt.event.MouseEvent evt) {                                            
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new FileOpenCommand());
            SelectionToolClicked(null);
        }    
    }                                           

    public void SaveAsButtonMouseClicked(java.awt.event.MouseEvent evt) {                                          
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new FileSaveAsCommand());
        }
    }                                         

    public void SaveButtonMouseClicked(java.awt.event.MouseEvent evt) {                                        
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new FileSaveCommand());
        }
    }                                       

    public void SaveActionPerformed(java.awt.event.ActionEvent evt) {                                     
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new FileSaveCommand());
        }
    }                                    

    public void AboutActionPerformed(java.awt.event.ActionEvent evt) {                                      
        view.aboutBox.setLocationRelativeTo(view);
        view.aboutBox.setVisible(true);
    }                                     

    public void NewButtonMouseClicked(java.awt.event.MouseEvent evt) {                                       
        cmdHist.doCommand(new NewCircuitCommand());
    }                                      

    public void MakeImageButtonMouseClicked(java.awt.event.MouseEvent evt) {                                             
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new MakeImageCommand());
        }
    }                                            

    public void RecordButtonMouseClicked(java.awt.event.MouseEvent evt) {                                          
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new SimulationRecordCommand());    
            view.getActiveCircuit().getLoggerWindow().addWindowListener(loggerWindowListener);
            view.RecordButton.setSelected(view.getActiveCircuit().getLoggerWindow().isShowing()); 
        }
    }                                         

    public void StopButtonMouseClicked(java.awt.event.MouseEvent evt) {                                        
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new SimulationStopCommand());
            view.StartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/toolbar/media-playback-start.png")));
            playPause = false;
            view.StopButton.setEnabled(false);
            view.Stop.setEnabled(false);
            view.RecordButton.setSelected(false);
            view.StepForward.setEnabled(false);
            view.StepForwardButton.setEnabled(false);
        }
    }                                       

    public void StartButtonMouseClicked(java.awt.event.MouseEvent evt) {                                         
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            if(playPause){ 
                view.getActiveCircuit().doCommand(new SimulationPauseCommand());
                view.StartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/toolbar/media-playback-start.png")));
                view.StepForward.setEnabled(true);
                view.StepForwardButton.setEnabled(true);
            } else {
                view.getActiveCircuit().doCommand(new SimulationStartCommand());
                view.StartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/toolbar/media-playback-pause.png")));
                view.StepForward.setEnabled(false);
                view.StepForwardButton.setEnabled(false);
            }
            view.StopButton.setEnabled(true);
            view.Stop.setEnabled(true);
            playPause = !playPause;    
            SelectionToolClicked(null);
        }
    }                                        

    public void CutButtonMouseClicked(java.awt.event.MouseEvent evt) {                                       
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new SelectionCutCommand());
        }
    }                                      

    public void PasteButtonMouseClicked(java.awt.event.MouseEvent evt) {                                         
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new SelectionPasteCommand());
        }
    }                                        

    public void CopyButtonMouseClicked(java.awt.event.MouseEvent evt) {                                        
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new SelectionCopyCommand());
        }
    }                                       

    public void ComponentSelectionTreeFocusGained(java.awt.event.FocusEvent evt) {                                                   
        view.setOptionsPanelComponentRotation(0);
        ComponentSelectionTreeValueChanged(null);
    }                                                  

    public void NewActionPerformed(java.awt.event.ActionEvent evt) {                                    
        cmdHist.doCommand(new NewCircuitCommand()); 
    }                                   

    public void RunActionPerformed(java.awt.event.ActionEvent evt) {                                    
        if(view.getActiveCircuit()!=null){
            if(playPause){ 
                view.getActiveCircuit().doCommand(new SimulationPauseCommand());
                view.StartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/toolbar/media-playback-start.png")));
                view.StepForward.setEnabled(true);
                view.StepForwardButton.setEnabled(true);
            } else {
                view.getActiveCircuit().doCommand(new SimulationStartCommand());
                view.StartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/toolbar/media-playback-pause.png")));
                view.StepForward.setEnabled(false);
                view.StepForwardButton.setEnabled(false);
            }
            view.StopButton.setEnabled(true);
            view.Stop.setEnabled(true);
            playPause = !playPause;    
            SelectionToolClicked(null);
        }
    }                                   

    public void StopActionPerformed(java.awt.event.ActionEvent evt) {                                     
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new SimulationStopCommand());
            view.StartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/buttons/toolbar/media-playback-start.png")));
            playPause = false;
            view.StopButton.setEnabled(false);
            view.Stop.setEnabled(false);
            view.Record.setSelected(false);
            view.Record.setEnabled(true);
            view.RecordButton.setSelected(true);
            view.RecordButton.setEnabled(false);
            view.StepForward.setEnabled(false);
            view.StepForwardButton.setEnabled(false);
        }
    }                                    

    public void RecordActionPerformed(java.awt.event.ActionEvent evt) {                                       
        if(view.getActiveCircuit()!=null && !view.Record.isSelected()){
            view.getActiveCircuit().doCommand(new SimulationRecordCommand());
            view.Record.setSelected(true);
            view.RecordButton.setSelected(true);
        } else if(view.Record.isSelected()){
            view.Record.setSelected(false);
            view.RecordButton.setSelected(false); 
        }
    }                                      

    public void StepForwardButtonMouseClicked(java.awt.event.MouseEvent evt) {                                               
        if(view.getActiveCircuit()!=null && evt.getComponent().isEnabled()){
            view.getActiveCircuit().doCommand(new SimulationStepCommand());
        }
    }                                              

    public void StepForwardActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().doCommand(new SimulationStepCommand());
        }
    }                                           

    public void ToggleGridActionPerformed(java.awt.event.ActionEvent evt) {                                           
        UIConstants.DRAW_GRID_DOTS = !UIConstants.DRAW_GRID_DOTS;
        view.repaint();
    }                                          

    public void PreferencesActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if(view.getActiveCircuit()!=null){
            view.getActiveCircuit().getParentFrame().openCircuitPreferencesDialog();
        }
    }                                           

    public void SimulatorSpeedStateChanged(javax.swing.event.ChangeEvent evt) {                                            
        if(view.getActiveCircuit() != null && !view.SimulatorSpeed.getValueIsAdjusting()){
            view.getActiveCircuit().getSimulator().setSimulatorSpeed(view.SimulatorSpeed.getValue());
        }
    }                                           

    public void InsertSubComponentActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        if(view.getActiveCircuit()!=null){
            view.toggleToolboxButton(view.InsertSubComponent);
            view.getActiveCircuit().doCommand(new SubcircuitOpenCommand());
        }
    }
    
    public void fixComponent(SelectableComponent sc) {
        view.getActiveCircuit().doCommand(
                new FixComponentCommand(sc));
    }
    
    public CommandHistory getCommandHistory() {
        return cmdHist;
    }
    
}