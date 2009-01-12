package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author matt
 */
public class LogoPanel extends JPanel {
    
    BufferedImage image;
    
    public LogoPanel(){
        try{
            image = javax.imageio.ImageIO.read(getClass().getResource("/ui/images/originals/led-logo.png"));
        } catch (IOException e){

        }
    }

    @Override
    public void paintComponent(Graphics g){
        g.drawImage(image,0,0,null);
    }

}
