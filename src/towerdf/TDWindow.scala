package towerdf

import scala.swing._
import scala.swing.Panel
import TDWindow._
import java.awt.{ Graphics2D, Color, Font }
import javax.swing.ImageIcon
import java.io._
import util.control.Breaks._
import towerdefencegame.gui._
import java.awt.MouseInfo

class TDWindow(top: MainFrame) extends Panel with Runnable {
  
  
  val tausta = new ImageIcon("Images/alkuruutu.png").getImage
  var thread: Thread = new Thread(this)
  var isFirst: Boolean = true
  thread.start
  
  def define(){   // maarittelee kaikki tarvittavat 
    gameboard = new GameBoard()
    store = new Store()
    getImages
    gameboard.loadSave(new File("Missions/mission" + level))
    
    for(i <- 0 until mob.length){      //Luo Enemy olioita erilaisten vihollisten verran
      mobs(i) = new Enemy()
    }
  }
  
  def getImages{      // hakee ja muokkaa kuvat pelilautaan sopiviksi
      ground(0) = new ImageIcon("Images/grass3.png").getImage
      ground(1) = new ImageIcon("Images/cobbleroad.png").getImage
      muu(0) = new ImageIcon("Images/Muumitalo.png").getImage
      res(0) = new ImageIcon("Images/cell.png").getImage
      res(1) = new ImageIcon("Images/heart.png").getImage
      res(2) = new ImageIcon("Images/coin.png").getImage
      towers(0) = new ImageIcon("Images/salamakehitin.png").getImage
      towers(1) = new ImageIcon("Images/muumipeikko.png").getImage
      towers(2) = new ImageIcon("Images/nuuskamuikkunen.png").getImage
      mob(0) = new ImageIcon("Images/hattivatti.png").getImage
      mob(1) = new ImageIcon("Images/morko.png").getImage
      mob(2) = new ImageIcon("Images/haisuli.png").getImage
      
      
  }
  
  
  override def paintComponent(g: Graphics2D){         //piirtaa kaiken tarvittavan
    myWidth = size.width
    myHeight = size.height
    
    g.drawImage(tausta, 0, 0, myWidth, myHeight, null )
    // tells if map is not in right format or right
    if(fileCorrupt){
      g.setColor(new Color(0,0,0))
      g.fillRect(0,0,myWidth,myHeight)
      g.setColor(new Color(240,20,20))
      g.setFont(new Font("Courier New",Font.BOLD,60 ))
      g.drawString("Map or File corrupt", 50 ,225 )
    }
    if(startScreen == false && fileCorrupt ==false ){
        if(isFirst){
          g.setColor(new Color(0,160,0))
          g.fillRect(0, 0, myWidth, myHeight)
          g.setColor(Color.lightGray)
          define()
          isFirst = false
        }
        gameboard.draw(g)      //piirtaa pelialusta
        
        for(i <- 0 until mobs.length){      // piirtää viholliset
          if(mobs(i).inGame){
            mobs(i).draw(g)
          }
        }
        
        store.draw(g)          //piirtaa kaupan ja muut naytettavat tiedot
        if(health < 1){
          g.setColor(new Color(0,0,0))
          g.fillRect(0,0,myWidth,myHeight)
          g.setColor(new Color(240,20,20))
          g.setFont(new Font("Courier New",Font.BOLD,60 ))
          g.drawString("Game Over", 250 ,225 )
          g.setColor(new Color(64,64,64))
          var backToStartScreenButton: Rectangle = new Rectangle(650, 450, 160, 60)
          g.drawString("BACK", backToStartScreenButton.x +5, backToStartScreenButton.y+ 45)
          g.draw(backToStartScreenButton) // back button to GAME OVER -screen to get play again.
          if(backToStartScreenButton.contains(mse) && backButton){
            startScreen = true
            playbut = true
            isFirst = true
            coins = 20
            health = 10
            level = 1
            killed = 0
            playbut = false
          }
        }
        
        if(isWin){
          g.setColor(new Color(255,255,255))
          g.fillRect(0, 0, myWidth, myHeight)
          g.setColor(new Color(0,0,0))
          g.setFont(new Font("Courier New",Font.BOLD,60 ))
          if(level == maxLevel){
            g.drawString("You won the GAME!!!!!: ", 150 ,225 )
          }else{
            g.drawString("You won level: "+level, 150 ,225 )
          }
        }
      }else if(startScreen == true){    // ollaan aloitus menussa
        var playButton: Rectangle = new Rectangle(myWidth / 2 - 150, myHeight/2 , 300, 100)
        g.setFont(new Font("Courier New",Font.BOLD,100 ))
        g.setColor(new Color(0,153,0))
        g.drawString("START", playButton.x -5, playButton.y+ 80)
        g.setColor(new Color(255,0,0))
        g.draw(playButton)
        if(playButton.contains(mse) && playbut){
          startScreen = false
          playbut = false
          
        }
      }
  }
  
  var spawnTime: Int = 1500
  var spawnTick: Int = 0
  var enemytypeTick: Int = 0
 
 def sleep(duration: Long) { Thread.sleep(duration) }
 def mobSpawner() {                  // Luo erityyppisia vihollisia spawnTick/spawnTime 
    if (spawnTick >= spawnTime) {
      breakable {
      for (i <- 0 until mobs.length)  {
        if (!mobs(i).inGame){
          if(enemytypeTick%6==1){
            mobs(i).spawnMob(TDWindow.morko)
            mobs(i).health = 2*52
            mobs(i).walkSpeed = 15
            mobs(i).damage = 4
            enemytypeTick += 1
          
           }else{
            if(enemytypeTick%3==1){
            mobs(i).spawnMob(TDWindow.haisuli)
            mobs(i).health = 52
            mobs(i).walkSpeed = 5
            mobs(i).damage = 2
            enemytypeTick += 1
            }else{
            mobs(i).spawnMob(TDWindow.hattivatti)
            mobs(i).health = 52
            mobs(i).walkSpeed = 10          
            mobs(i).damage = 2
            enemytypeTick += 1
          }
           }
          break
        }
        }
      }
      spawnTick = 0
    } else {
      spawnTick += 1
    }
  }
  // Run metodi tarkistaa etta onko peli voitettu vai kesken ja antaa vihollisille ja towereille niiden tekoalyn ja kutsuu mobSpawner -metodia
   def run(){
      while(true){
        if(!isFirst && health > 0 && !isWin){
          gameboard.abilities()
          mobSpawner()
          for (i <- 0 until mobs.length) {
            if (mobs(i).inGame){
              mobs(i).aiMob()
            }
          }
        }else{
          if(isWin){
            if(winFrame>=winTime){
              if(level == maxLevel){
                System.exit(0)
              }else{
                level += 1
                coins += 5
                health += 5
                define()
                isWin = false
              }
              winFrame = 0
            }else{
              winFrame+=1
            }
          }
        }
        repaint()
        
        try{
        Thread.sleep(0,1)
        }catch{
         case e: Exception => println("jotain pielessä Runnablessa")
        }
  }
  }
}

object TDWindow {
  import TDWindow._
  // Objectissa koska nain voidaan hyodyntaa muaalla muutujia
  var coins: Int = 20
  var health:Int = 10
  var level: Int = 1
  
  var isFirst: Boolean = true
  var gameboard: GameBoard = _
  var store: Store = _
  var shootingLinesOn: Boolean = true
  var myWidth: Int = _
  var myHeight: Int = _
  var ground: Array[Image] = new Array[Image](10)
  var muu: Array[Image] = new Array[Image](10)
  var res: Array[Image] = new Array[Image](10)
  var towers: Array[Image] = new Array[Image](10)
  var mob: Array[Image] = new Array[Image](10)
  var mobs: Array[Enemy] = new Array[Enemy](10)
  
  var mse: Point = new Point()
  var playbut = false
  var backButton = false
  var startScreen:Boolean = true
  var fileCorrupt:Boolean = false
  
  var killsToWin:Int =_
  var killed:Int = 0
  var isWin: Boolean = false
  var maxLevel: Int = 5
  var winTime: Int = 4000
  var winFrame: Int = 0
  
  // voiton tarkistus
  def hasWon(){
    if(killed >= killsToWin){
      isWin = true
      killed = 0
    }
  }
 
  // Helposti muutettavia arvoja. Mission karttojen piirtamiseen.
  val groundGrass: Int = 0
  val groundRoad: Int = 1
  val airAir: Int = -1
  val mobAir: Int = -1
  
  // arvot eri vihollisten tai towerien kutsuntaan
  val hattivatti: Int = 0
  val morko: Int = 1
  val haisuli: Int = 2
  
  val Muumitalo = 8
  val Salamakehitin:Int = 0
  val Muumipeikko:Int = 1
  val Nuuskamuikkunen: Int = 2
}