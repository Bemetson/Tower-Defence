package TD

object Player {

 private var hp = 20
 private var cash = 40000
 
 
  
  def HP = this.hp
  def CASH = this.cash
  
  def creepEscape = {
   this.hp -= 1
   val sword22 = new MusicPlayer(new StereoSound("sword2"), 1)
   val threadEffect2 = new Thread(sword22)
   threadEffect2.start
   }
  
  def getCash(sum: Int) = this.cash += sum
  def buy(sum: Int) = this.cash -= sum
}