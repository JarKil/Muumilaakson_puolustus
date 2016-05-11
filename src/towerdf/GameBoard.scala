package towerdf

import towerdefencegame.gui._
import TDWindow._
import java.awt.{ Graphics2D, Color }
import java.io._
import java.awt._
import java.util._
import scala.collection.mutable.Buffer

class GameBoard {
  var worldWidth: Int = 12
  var worldHeight: Int = 8
  var blockSize: Int = 52
  var block: Array[Array[Block]] = _
  define()

  
  
  //maarittelee blockit omille ja paikoilleen ja 2 ulotteisen arrayn johon ne tallennetaan.
  def define() {   
    block = Array.ofDim[Block](worldHeight, worldWidth)
    for (y <- 0 until block.length; x <- 0 until block(0).length) {
      block(y)(x) = new Block(x * blockSize, y * blockSize, blockSize, blockSize, 0, 0)
    }
  }
  def draw(g: Graphics2D) {        // tekee blockeista piirrettavia
    for (y <- 0 until block.length; x <- 0 until block(0).length) {
      block(y)(x).draw(g)
    }
    // piirtaa ampumis animaation ja ampumisetaisyyden
    for (y <- 0 until block.length; x <- 0 until block(0).length) {
      block(y)(x).animate(g)
    }
    g.drawImage(TDWindow.muu(0), blockSize*worldWidth-30, 0, 300, 416, null)      // piirtaa muumitalon 
  }
  
  def abilities() {        // antaa taidot/fysiikat pelilaudalla oleville
    for(y<-0 until block.length){
      for(x<-0 until block(0).length){
        block(y)(x).shoot()
      }
    }
  }
  
  // Kayttaa javan scanneria tulkitsemaan mission tekstitiodoston
  def loadSave(loadPath: File) { 
      try{
        val loadScanner = new Scanner(loadPath)
        while (loadScanner.hasNext()) {
          TDWindow.killsToWin = loadScanner.nextInt()
          for (y <- 0 until block.length; x <- 0 until block(0).length) {
            block(y)(x).maaID = loadScanner.nextInt()
          }
          for (y <- 0 until block.length; x <- 0 until block(0).length) {
            block(y)(x).ilmaID = loadScanner.nextInt()
          }
      }
      // Tarkistaa että alku ja loppupelilaudan rivillä on vain yksi mahdollinen lähtö ja lopetuspiste
      var beginPointTest = 0
      var endingPointTest = 0
      for (t <- 0 until worldHeight){
        if(block(t)(0).maaID == 0) beginPointTest += 1 
      }
      for (tz <- 0 until worldHeight){
        if(block(tz)(11).maaID == 0) endingPointTest += 1 
      }
      if(!(beginPointTest == 7 && endingPointTest == 7)){
        println("ei aloitus tai lopetuspisteet oikein")
        throw new Exception()
        
      }
      loadScanner.close()
      }catch{
        case e: Exception => println("Scanner ei pelita")
        TDWindow.fileCorrupt = true
        
              
      }
  
}
}