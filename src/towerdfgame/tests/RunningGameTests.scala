package towerdfgame.tests

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.Assertions._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import towerdf._
import towerdefencegame.gui.MuumilaaksoDF


@RunWith(classOf[JUnitRunner])
class RunningGameTests extends FlatSpec with Matchers {
  
  var coins: Int = 20
  var health:Int = 10
  var level: Int = 1
  TDWindow.isFirst = false
  
  val p = MuumilaaksoDF.top
  val TDWindowTest = new TDWindow(p)
  TDWindowTest.define()
  val gameboardTest = new GameBoard()
  val storeTest = new Store()
  import java.io._
  var fw: FileWriter  = new FileWriter("samplemap.txt")
    fw.write("10 \n"+
          "0  0  0  0  0  0  0  0  0  0  0  0 \n"+                  //yC = 0
          "1  1  1  0  1  1  1  1  1  1  1  0 \n"+
          "0  0  1  0  1  0  0  0  0  0  1  0 \n"+
          "0  0  1  0  1  1  1  1  0  0  1  0 \n"+
          "0  0  1  0  0  0  0  1  0  0  1  0 \n"+
          "0  0  1  0  0  0  0  1  0  0  1  0 \n"+
          "0  0  1  1  1  1  1  1  0  0  1  0 \n"+
          "0  0  0  0  0  0  0  0  0  0  1  1 \n"+                 //yC = 7
          " \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1  8")
          
       //xC=0                               //xC=11
          
  fw.close()     
  gameboardTest.loadSave(new File("samplemap.txt"))
  gameboardTest.abilities()
    
    
  "Mob" should "exist in game after spawning it" in {
    TDWindow.mobs(0) = new Enemy
    assume(!(TDWindow.mobs(0).inGame), "There should be no enemies yet in game")
    TDWindowTest.mobSpawner()
    TDWindow.mobs(0).spawnMob(TDWindow.hattivatti)
    TDWindow.mobs(0).health = 5
    TDWindow.mobs(0).walkSpeed = 1
    assert(TDWindow.mobs(0).inGame, "There should be an enemy in game")
    TDWindow.mobs(0).deleteMob()
    assert(TDWindow.coins == 20+1, "deleted enemy give you one coin")
    TDWindow.coins -= 1
    assert(!(TDWindow.mobs(0).inGame), "There should not be an enemy in game anymore")
    }
  "Mob" should "be in first block after spawning it and have given specs" in {
    TDWindow.mobs(0).spawnMob(TDWindow.hattivatti)
    TDWindow.mobs(0).health = 5
    TDWindow.mobs(0).walkSpeed = 1
    assume(TDWindow.mobs(0).inGame, "There should be an enemy in game")
    assume(gameboardTest.block(1)(0).maaID== TDWindow.groundRoad, "This block is road starting place")
    assert(gameboardTest.block(1)(0).contains(TDWindow.mobs(0)))
    assert(TDWindow.mobs(0).health ==5 && TDWindow.mobs(0).walkSpeed == 1, "Specs arent updated right")
  }
  "Mob" should "when mod has walked 8 blocks distance it is in block xC = 2 yC = 6" in {
    //One block distance is 52 pixels so mod have to walk 416 pixels and it is time two so (416*2=832)
    assume(TDWindow.mobs(0).inGame, "There should be an enemy in game")
    assume(gameboardTest.block(1)(0).contains(TDWindow.mobs(0)))
    var x = 0
    while(x != 832 - 1){
      x +=1
      TDWindow.mobs(0).aiMob()
    }
    assert(TDWindow.mobs(0).yC == 6 && TDWindow.mobs(0).xC == 2, " Mob shoud be in yC = 6 xC = 2 ")
  }
  "Tower" should "be able to hold when you click tower button to get it" in {
    storeTest.define()
    TDWindow.mse.setLocation(storeTest.button(0).x, storeTest.button(0).y) // salamakehitin
    storeTest.click(5)
    assert(storeTest.holdsItem, "Check if you are holding tower")
  }
  "Tower" should "not be allowed put on the road only on the side of the road or everywhere else in the playground blocks" in {
    TDWindow.mse.setLocation(TDWindow.mobs(0).x, TDWindow.mobs(0).y) //koitetaan laittaa toweri tielle ja vielä mobin paalle
    storeTest.click(5)
    assert(storeTest.holdsItem && TDWindow.coins == 20 , "This should still be true when tower isnt placed and no coins spended")
    TDWindow.mse.setLocation(TDWindow.mobs(0).x, TDWindow.mobs(0).y + TDWindow.mobs(0).mobSize + 1)
    storeTest.click(5)
    assert(TDWindow.coins ==15 &&  TDWindow.gameboard.block(7)(2).ilmaID == storeTest.heldID, "Tower should be placed if this error pops up mean that it didnt set up")
  }
  "Tower" should "be able to make damage nearby enemy" in {
    var pp = 0
    assert(TDWindow.mobs(0).inGame)
    assert(TDWindow.mobs(0).yC == 6 && TDWindow.mobs(0).xC == 2, " Mob shoud be in yC = 6 xC = 2 ")
    assert(TDWindow.gameboard.block(7)(2).ilmaID == 0, "salamakehitin tower is placed here")
    
    while(pp != 2){
      TDWindow.gameboard.abilities()
      TDWindow.gameboard.block(7)(2).shoot()
      TDWindow.mobs(0).loseHealth(1)
      pp +=1
    }
    assert(TDWindow.gameboard.block(7)(2).isShooting && TDWindow.gameboard.block(7)(2).shotMob == 0, "Tower is shooting and shooting target is 0")
    assert( TDWindow.mobs(0).health == 3)
  }
  
}