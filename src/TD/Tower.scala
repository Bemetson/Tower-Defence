package TD

import java.awt.Color
import java.awt.Graphics2D

trait Tower extends GameObject {

  var rofUp = false
  var dmgUp = false
  var shot = false
  var x: Int
  var y: Int
  var price: Int
  var size: Int
  var time: Int
  val color: Color
  var rof: Int
  val range: Int
  val name: String
  var damage: Int
  
  def bought: Unit = {
    Game.towerBought(this)
  }
  
  def draw(g: Graphics2D) = {
    g.setColor(this.flash)
    g.fillRect(x - this.size / 2, y - this.size / 2, this.size, this.size)
  }
  
  def setCoords = {
    this.x = GameScreen.mouseX
    this.y = GameScreen.mouseY
  }
  
  def update: Unit
  
  def flash: Color
  
  def doubleDmg = this.damage = this.damage * 2
  
  def doubleRof = this.rof = this.rof / 2
  
}
