package towerdf

import java.awt.{ Graphics, Color,Rectangle, Font }
import TDWindow._

class Store {
  var shopWidth: Int = 4
  var buttonSize: Int = 72
  var iconSize: Int = 33
  var button: Array[Rectangle] = new Array[Rectangle](shopWidth)
  var buttonID: Array[Int] = Array(TDWindow.Salamakehitin, TDWindow.Muumipeikko, TDWindow.Nuuskamuikkunen, TDWindow.airAir)
  var buttonPrice: Array[Int] =Array(5,7,10,0)
  var buttonHealth: Rectangle = _
  var buttonCoins: Rectangle = _
  var itemIn: Int = 4
  var heldID: Int = -1
  var priceID: Int = -1
  var holdsItem: Boolean = false
  
  define()
  
  def define() {
    for (i <- 0 until button.length) {
      button(i) = new Rectangle(TDWindow.myWidth / 2 - shopWidth*60  + 
        ((buttonSize + 4) * i), 430, buttonSize, buttonSize)
    }
    buttonHealth = new Rectangle(35 - 1, button(0).y, iconSize, iconSize)
    buttonCoins = new Rectangle(35 - 1, button(0).y+button(0).height-iconSize, iconSize, iconSize)
  }
  
  def click(mouseButton: Int){
    if(mouseButton == 5){
      for(i <- 0 until button.length){
        if(button(i).contains(TDWindow.mse)){ // otetaan toweri
          if(buttonID(i) == TDWindow.airAir){ // poistaa turhan towerit kadesta kun koittaa klikata tyhjaa toweri paikkaa
            holdsItem = false
          }else{
            heldID = buttonID(i)
            priceID = i
            holdsItem = true
          }
        }
      }
      if(holdsItem){
        if(TDWindow.coins >= buttonPrice(priceID)){
          for(y <- 0 until TDWindow.gameboard.block.length){
            for(x <- 0 until TDWindow.gameboard.block(0).length){
              if(TDWindow.gameboard.block(y)(x).contains(TDWindow.mse)){
                if(TDWindow.gameboard.block(y)(x).maaID != TDWindow.groundRoad && TDWindow.gameboard.block(y)(x).ilmaID == TDWindow.airAir ){
                  //laittaa towerin haluttuun paikkaan
                  TDWindow.gameboard.block(y)(x).ilmaID = heldID
                  TDWindow.coins -= buttonPrice(priceID)
                }
              }
            }
          }
        }
      }
    }
  }
  
  def draw(g: Graphics) {        //Piirtaa tornien kauppapaikat ja elama ja raha maarat ja symbolit
    for (i <- 0 until button.length) {
      if(button(i).contains(TDWindow.mse)){
        g.setColor(new Color(255,255,255,255))
        g.fillRect(button(i).x, button(i).y, button(i).width, button(i).height)
      }
      
      g.drawImage(TDWindow.res(0), button(i).x, button(i).y, button(i).width, button(i).height, null)
      if(buttonID(i)!= TDWindow.airAir) g.drawImage(TDWindow.towers(buttonID(i)), button(i).x + itemIn, button(i).y, button(i).width, button(i).height, null)
      if(buttonPrice(i)>0){
        g.setColor(new Color(255,255,255))
        g.setFont(new Font("Courier New", Font.BOLD,16))
        g.drawString(buttonPrice(i) + "", button(i).x + itemIn, button(i).y+ itemIn +10)
      }
    }
    g.drawImage(TDWindow.res(1),buttonHealth.x, buttonHealth.y, buttonHealth.width, buttonHealth.height, null)
    g.drawImage(TDWindow.res(2),buttonCoins.x, buttonCoins.y, buttonCoins.width, buttonCoins.height, null)
    g.setFont(new Font("Courier New", Font.BOLD, 20))
    g.setColor(new Color(255,255,255))
    g.drawString(""+ TDWindow.health, buttonHealth.x + buttonHealth.width + 6, buttonHealth.y + 20)
    g.drawString(""+ TDWindow.coins, buttonCoins.x + buttonCoins.width + 6, buttonCoins.y + 20)
    g.drawString("LEVEL: "+ TDWindow.level, 620, buttonHealth.y + 20)
    g.drawString("VIHOLLISIA:"+ TDWindow.killed + "/" + TDWindow.killsToWin, 620, buttonCoins.y + 20)
    
    if(holdsItem){      //piirtaa pidettavan towerin joka on kadessa
      g.drawImage(TDWindow.towers(heldID), TDWindow.mse.getX.toInt- ((button(0).width- (itemIn*2))/2) + itemIn, TDWindow.mse.getY.toInt - ((button(0).width- (itemIn*2))/2) + itemIn, button(0).width- (itemIn*2), button(0).height - (itemIn*2), null)
       if(TDWindow.shootingLinesOn){ //piirtelee ampumaetaisyytta towereille kun ne ovat kadessa
          g.drawRect(TDWindow.mse.getX.toInt- ((button(0).width- (itemIn*2))/2) + itemIn -50 , TDWindow.mse.getY.toInt - ((button(0).width- (itemIn*2))/2) + itemIn -50, button(0).width- (itemIn*2)+ 100, button(0).height - (itemIn*2)+ 100)
      }
    }
  }
}