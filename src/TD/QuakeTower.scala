package TD

import java.awt.Color

class QuakeTower extends Tower {
  var x = GameScreen.mouseX
  var y = GameScreen.mouseY
  var time = 0

  var size = 30
  var price = 250
  val color = Color.getHSBColor(161, 130, 70)
  var rof = 160 // Rate of Fire
  val range = 160
  val name = "Quake Tower"
  var damage = 70

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