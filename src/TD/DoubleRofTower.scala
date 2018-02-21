package TD

import java.awt.Color

class DoubleRofTower extends Tower {
  var x = GameScreen.mouseX
  var y = GameScreen.mouseY
  var time = 0

  var size = 10
  var price = 500
  val color = Color.getHSBColor(21, 130, 270)
  var rof = 5 // Rate of Fire
  val range = 20
  val name = "Double ROF Tower"
  var damage = 0

  def flash: Color = {
    if (this.shot) return Color.RED else return color
  }

  def update: Unit = {   
    this.time += 1
    val towers = Game.towers.filter { x => Math.sqrt(Math.pow(this.x - x.x.toDouble, 2) + Math.pow(this.y - x.y.toDouble, 2)) <= this.range }
    if (!towers.isEmpty) {
      if (this.time >= this.rof) {
        shot = true
        this.time = 0
        if (!rofUp) {
          towers.foreach { x => x.doubleRof }
          rofUp = true
        }
      } else {
        shot = false
      }
    } else {
      shot = false
    }
  }
}
