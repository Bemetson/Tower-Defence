package TD

import java.awt.Graphics2D

trait GameObject {

  def update: Unit
  
  def draw(g: Graphics2D): Unit
  
}