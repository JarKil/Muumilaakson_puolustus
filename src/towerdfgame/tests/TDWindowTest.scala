package towerdfgame.tests


import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.Assertions._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import towerdf._


@RunWith(classOf[JUnitRunner])
class TDWindowTest extends FlatSpec with Matchers {
  val gameboardTest = new GameBoard()
  val storeTest = new Store()

  "TDWindow" should "allow removing coins" in {
    TDWindow.coins -= 1
    assert(TDWindow.coins == 19, "There should be 19 coins left")
  }
  
  "TDWindow" should "allow gameboard.loadSave load new mission which has right amount of linest" in{
    import java.io._
    var fw: FileWriter  = new FileWriter("samplemap.txt")
    fw.write("10 \n"+
          "0  0  0  0  0  0  0  0  0  0  0  0 \n"+
          "1  1  1  0  1  1  1  1  1  1  1  0 \n"+
          "0  0  1  0  1  0  0  0  0  0  1  0 \n"+
          "0  0  1  0  1  1  1  1  0  0  1  0 \n"+
          "0  0  1  0  0  0  0  1  0  0  1  0 \n"+
          "0  0  1  0  0  0  0  1  0  0  1  0 \n"+
          "0  0  1  1  1  1  1  1  0  0  1  0 \n"+
          "0  0  0  0  0  0  0  0  0  0  1  1 \n"+
          " \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1  8")
          
    fw.close()     
    gameboardTest.loadSave(new File("samplemap.txt"))
    assert(TDWindow.killsToWin == 10, "too many lines or too few on the map")
  }
  
  "TDWindow" should "allow gameboard.loadSave load new mission which has one starting point and one ending point" in{
    import java.io._
    var fw: FileWriter  = new FileWriter("samplemap.txt")
    fw.write("10 \n"+
          "0  0  0  0  0  0  0  0  0  0  0  0 \n"+
          "0  1  1  0  1  1  1  1  1  1  1  0 \n"+
          "0  0  1  0  1  0  0  0  0  0  1  0 \n"+
          "0  0  1  0  1  1  1  1  0  0  1  0 \n"+
          "0  0  1  0  0  0  0  1  0  0  1  0 \n"+
          "0  0  1  0  0  0  0  1  0  0  1  0 \n"+
          "0  0  1  1  1  1  1  1  0  0  1  0 \n"+
          "0  0  0  0  0  0  0  0  0  0  1  1 \n"+
          " \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 \n"+
          "-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1  8")
          
    fw.close()     
    gameboardTest.loadSave(new File("samplemap.txt"))
    assert(TDWindow.fileCorrupt == true, "too many lines or too few on the map")
  }
  
}