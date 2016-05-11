package towerdefencegame.gui
import towerdf._
import scala.swing._
import scala.swing.Component._
import swing.event._
import scala.swing.BorderPanel.Position._
import java.awt.MouseInfo
import javax.swing.ImageIcon
import java.awt.{ Graphics2D, Color, Font }
import towerdf._


object MuumilaaksoDF extends SimpleSwingApplication  {
  // helposti muutettavia arvoja
  val koko = new Dimension(830, 550)
  val otsikko = "Muumilaakson puolustus"
  
  
  def top = new MainFrame {
    title = otsikko
   
    val canvas = new TDWindow(this) {
    preferredSize = koko
    }
    
   
    contents = new BorderPanel {
      layout(canvas) = Center
   }
    resizable = false
    this.location = new Point(500, 500)
    this.size = koko
    
   
    
    
    //lisaa hiiren liikuttelun kuuntelun canvasille 
    listenTo(canvas.mouse.clicks)
    listenTo(canvas.mouse.moves)
    
    reactions += {
      case MouseClicked(_, point, _, _, _) =>
        if(!TDWindow.startScreen)TDWindow.store.click(MouseInfo.getNumberOfButtons)
        if(TDWindow.startScreen)TDWindow.playbut = true
        if(TDWindow.health < 1)TDWindow.backButton = true
        
      case MouseMoved(_, _, _) =>
        //println("hiirta liikutettu")
        TDWindow.mse = new Point((MouseInfo.getPointerInfo.getLocation.getX.toInt) - ((MuumilaaksoDF.koko.width - TDWindow.myWidth) / 2) -this.location.x, (MouseInfo.getPointerInfo.getLocation.getY.toInt) - 
        ((MuumilaaksoDF.koko.height - (TDWindow.myHeight)) - (MuumilaaksoDF.koko.width - 
        TDWindow.myWidth) / 2) - this.location.y)
        
      case mouseDragged =>
        TDWindow.mse = new Point((MouseInfo.getPointerInfo.getLocation.getX.toInt) - ((MuumilaaksoDF.koko.width - TDWindow.myWidth) / 2) -this.location.x, (MouseInfo.getPointerInfo.getLocation.getY.toInt) - 
        ((MuumilaaksoDF.koko.height - (TDWindow.myHeight)) - (MuumilaaksoDF.koko.width - 
        TDWindow.myWidth) / 2) - this.location.y)
        
    }
  } 
}
  