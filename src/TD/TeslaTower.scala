package TD

import java.awt.Graphics2D
import java.awt.Color

class TeslaTower extends Tower {

  var x = GameScreen.mouseX
  var y = GameScreen.mouseY
  var time = 0
  
  var size = 20
  var price = 20
  val color = Color.CYAN
  var rof = 60  // Rate of Fire
  val range = 100
  val name = "Tesla Tower"
  var damage = 30
  
  def flash: Color = {
    if (this.shot) return Color.RED else return color
  }
  
  def update: Unit = {   
    this.time += 1
    val creeps = Game.creeps.filter { x => Math.sqrt(Math.pow(this.x - x.x.toDouble, 2) + Math.pow(this.y - x.y.toDouble, 2)) <= this.range }
    if (!creeps.isEmpty) {
      if (this.time >= this.rof) {
        shot = true
        creeps.foreach { x => x.damage(this.damage) }
        this.time = 0
      } else {
        shot = false
      }
    } else {
      shot = false
    }
  }

}
