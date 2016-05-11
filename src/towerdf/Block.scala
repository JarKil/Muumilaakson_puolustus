package towerdf
import TDWindow._
import java.awt.{ Graphics2D, Color,Rectangle }

class Block (x:Int, y: Int, width: Int, height: Int, var maaID: Int, var ilmaID: Int) extends Rectangle{
  var towerSquare1: Rectangle = _
  var shootDis1:Int = 100
  
  var shotMob:Int = -1
  var isShooting: Boolean = false
  var loseTime: Int =100
  var loseFrame: Int =0
  
  setBounds(x, y, width, height)
  towerSquare1 = new Rectangle(x - (shootDis1/2), y- (shootDis1/2), width + (shootDis1), height +(shootDis1))
  //towerSquare2 = new Rectangle(x - (shootDis2/2), y- (shootDis2/2), width + (shootDis2), height +(shootDis2)) // voisi toimia eriampumaetaisyydelle
  
  def draw(g: Graphics2D) {              // piirtaa blockit
    g.drawImage(TDWindow.ground(maaID), x, y, width, height, null)
    if (ilmaID != TDWindow.airAir) {
      g.drawImage(TDWindow.towers(ilmaID), x, y, width, height, null)
    }
  }
  
  def shoot(){
    if(shotMob != -1 && towerSquare1.intersects(TDWindow.mobs(shotMob))){
            isShooting = true 
          }else{
            isShooting = false
          }
    if(!isShooting){
      if(ilmaID == TDWindow.Salamakehitin || ilmaID == TDWindow.Muumipeikko || ilmaID == TDWindow.Nuuskamuikkunen){
        for(i <- 0 until TDWindow.mobs.length){
          if(TDWindow.mobs(i).inGame){
            if(towerSquare1.intersects(TDWindow.mobs(i))){
              isShooting = true 
              shotMob = i
            }
          }
        }
      }
    }
    if(isShooting){
      if(loseFrame>=loseTime){
        if(ilmaID == TDWindow.Salamakehitin) TDWindow.mobs(shotMob).loseHealth(1)
        if(ilmaID == TDWindow.Muumipeikko) TDWindow.mobs(shotMob).loseHealth(2)
        if(ilmaID == TDWindow.Nuuskamuikkunen) TDWindow.mobs(shotMob).loseHealth(3)
        loseFrame = 0
      }else{
        loseFrame +=1
      }
      
      
      if(TDWindow.mobs(shotMob).isDead()){         // Tarkistaa onko vihollinen kuollut
        isShooting = false
        shotMob = -1
        TDWindow.hasWon()
      }
    }
  }
  
  
  def animate(g: Graphics2D){
      // piirtaa ampumaetaisyytta towereille shootingLinesOn moodi pitaa olla true
      if(TDWindow.shootingLinesOn){
        g.setColor(new Color(255,255,255,80))
        if(ilmaID == TDWindow.Salamakehitin) g.drawRect(towerSquare1.x, towerSquare1.y, towerSquare1.width, towerSquare1.height)
        if(ilmaID == TDWindow.Muumipeikko) g.drawRect(towerSquare1.x, towerSquare1.y, towerSquare1.width, towerSquare1.height)
        if(ilmaID == TDWindow.Nuuskamuikkunen) g.drawRect(towerSquare1.x, towerSquare1.y, towerSquare1.width, towerSquare1.height)
      } 
      if(isShooting){
          g.setColor(new Color(255,255,0))
          g.drawLine(x + width/2, y + height/2, TDWindow.mobs(shotMob).x + (TDWindow.mobs(shotMob).width/2), TDWindow.mobs(shotMob).y+ (TDWindow.mobs(shotMob).height/2))
        }
    }
  
}