package towerdf
import TDWindow._
import scala.swing._

class Enemy extends Rectangle{
  var xC: Int = _
  var yC: Int = _
  var mobSize: Int = 52
  var health: Int = _
  var damage: Int = 2
  var inGame: Boolean = false
  
  var mobID:Int =  TDWindow.mobAir
  // luo vihollisen oikeaan paikkaan pelilaudalla
  def spawnMob(mobID: Int){
    for(y <- 0 until TDWindow.gameboard.block.length ){
      if(TDWindow.gameboard.block(y)(0).maaID == TDWindow.groundRoad){
        setBounds(TDWindow.gameboard.block(y)(0).getX.toInt, TDWindow.gameboard.block(y)(0).getY.toInt, mobSize, mobSize)
        xC=0
        yC=y
      }
    }
    this.mobID = mobID
    inGame = true
  }
  
  def deleteMob(){
    inGame = false
    direction = right
    TDWindow.killed += 1
    mobWalk = 0
    coins += 1
  }
  def looseHealth(){
    TDWindow.health -= damage
  }
  
  def loseHealth(amo: Int){
    health -= amo
    checkDeath
  }
  def checkDeath(){
    if(health <= 0) deleteMob()
  }
  def isDead() = {
    if(inGame){
      false
    }else{
      true
    }
  }
  
  var upward: Int = 0
  var downward: Int = 1
  var right: Int = 2
  var left: Int = 3
  var direction: Int = right
  var hasUpward = false
  var hasDownward = false
  var hasLeft = false
  var hasRight = false
  
  var mobWalk: Int = 0
  var walkFrame: Int = 0
  var walkSpeed: Int = 10        // pienempi luku tekee vihollisista nopeampia kavelijoita
  
  def aiMob(){                  //Vihollisen aly hahmottaa tie jota kulkea
    if(walkFrame >= walkSpeed){    // askeltaja
      whatWay
      mobWalk += 1
      mobWalking          // suoritetaan vasta kun blockin koko on kavelty
     }else{
      walkFrame +=1        
    }
  }
  
  def whatWay{
    if(direction == right){
        x +=1
      }else if(direction == upward){
        y -= 1
      }else if(direction == downward){
        y +=1
      }else if(direction == left){
        x -=1
      }
  }
  
  def mobWalking{                // vihollinen kavelee 
    if (mobWalk == TDWindow.gameboard.blockSize){
        if(direction == right){
          xC +=1
          hasRight = true
        }else if(direction == upward){
          yC -= 1
          hasUpward = true
        }else if(direction == downward){
          yC +=1
          hasDownward = true
        }else if(direction == left){
          xC -= 1
          hasLeft = true
        }
        whereIsTheRoad        // kutsuu metodia whereIsTheRoad
        // nollaa kaikki kaydyt suunnat
        hasUpward = false
        hasDownward = false
        hasLeft = false
        hasRight = false
        mobWalk = 0
        }
        walkFrame = 0
       
  }
  
  
  def whereIsTheRoad{        // tarkistaa missa tie blockit gameboardilla sijaitsevat
    if(!hasUpward){
            try{
            if(TDWindow.gameboard.block(yC+1)(xC).maaID == TDWindow.groundRoad){
              direction = downward
            }
            }catch{
             case e: Exception => 
            }
        }
        if(!hasDownward){
            try{
            if(TDWindow.gameboard.block(yC-1)(xC).maaID == TDWindow.groundRoad){
              direction = upward
            }
            }catch{
             case e: Exception => 
            }
        }
        if(!hasLeft){
            try{
            if(TDWindow.gameboard.block(yC)(xC+1).maaID == TDWindow.groundRoad){
              direction = right
            }
            }catch{
             case e: Exception => 
            }
        }
        if(!hasRight){
            try{
            if(TDWindow.gameboard.block(yC)(xC-1).maaID == TDWindow.groundRoad){
              direction = left
            }
            }catch{
             case e: Exception => 
            }
        }
        baseUnderAttack
  }
  
  def baseUnderAttack{    // tarkistaa onko vihollinen päässyt muumitalolle asti ja jos on tekee tarvittavat metodit
    if(TDWindow.gameboard.block(yC)(xC).ilmaID == TDWindow.Muumitalo){
          deleteMob()
          looseHealth
        }
  }
  
  def draw(g: Graphics2D) {
      g.drawImage(TDWindow.mob(mobID), x, y, width, height, null)
      //elama mittari
      g.setColor(new Color(180,0,0))
      g.fillRect(x -(health/2-mobSize/2), y - 9, health, 3)
      
  }
}