package TD

import java.awt.Color
import java.awt.Graphics2D

trait Creep extends GameObject {
  
  var hp: Int
  var x: Int
  var y: Int
  var size: (Int, Int)
  var alive: Boolean
  var armor: Int
  
  val HP: Int
  val value: Int
  val speed: Int
  val color: Color
  
  def damage(dmg : Int) = {
    if (armor <= 0) this.hp -= dmg else this.armor -= dmg
  }
  
  def update: Unit = {
    this.x += 1 * this.speed
    this.y += 1 * this.speed
    this.willItDie
  }
  
  def willItDie: Unit = {
    if (this.x == 600 && this.y == 600) {
     Player.creepEscape
     this.alive = false 
    } else if (this.hp <= 0) {
      this.alive = false
      Player.getCash(this.value)
    }
  }
  
  def setArmor(amount: Int) = this.armor = amount
  
  def draw(g: Graphics2D) = {
    if (this.armor <= 0) g.setColor(this.color) else g.setColor(Color.PINK)
    g.fillOval(this.x - this.size._1 / 2,
               this.y - this.size._2 / 2, 
              (this.size._1 * (this.hp / this.HP.toDouble)).toInt, 
              (this.size._2 * (this.hp / this.HP.toDouble)).toInt)
  }

}
