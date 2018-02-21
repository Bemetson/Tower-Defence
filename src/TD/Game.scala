package TD

import scala.collection.mutable.Buffer
import java.awt.Graphics2D
import scala.util.Random
import scala.collection.mutable.SynchronizedBuffer

object Game {

  var creeps = Buffer[Creep]()
  val towers = Buffer[Tower]()
  var spawned = 0
  var total = 0
  var armor = 1
  var rate = 150
  val incAmror = 100

  private val rand = new Random

  def towerBought(tower: Tower) = {
    if (Player.CASH - tower.price >= 0) {
      this.towers += tower
      Player.buy(tower.price)
      GameScreen.selectedTower = None
      towers.foreach { x => println(x + " - - - " + x.x.toString + " - " + x.y.toString()) }
    }
  } 
  //aika upee peli. Ei voi muuta kyl sanoo ku et gratz. Siis aivan mielettömän koukuttava peli. Tota vois hakkaa viel maailman ääriin, no ehkei ihan niin kauaan, mut pitkälle kummiski. Siis aivan mieletön peli. Tykkään noist robot unicorn attack tunnareist taustal. Ne sopii hyvin peliin. Muutenki äänitehosteet on upeita. Kantsii lähtee kauppaa tätä supercellil tai roviol. Ovat varmasti kiinnostuneita huikeasta pelistä. Tässä yhdistyy monia hyviä puolia, joita pelaajat vaativat peleiltä: Pähkinöitä, joiden purkamiseen tarvitaan hieman ajattelua. Missä näin hieno idea syntyi? En 

  def update: Unit = {
    this.creeps.filter { x => x.alive }.foreach { x => x.update }
    if (canSpawn) this.spawnCreep(spawned match {
      case 10 => {
        new TankCreep
      }
      case 30  => {
         spawned = 0
        new BossCreep
      }
      case _ => new NormalCreep
    })
    this.towers.foreach { x => x.update }
    this.creeps = creeps.filter { x => x.alive }
  }

  def spawnCreep(creep: Creep) = {
    this.creeps += creep
    this.spawned += 1
    this.total += 1
    println(spawned)
  }

  // Piirt�� creepit ja towerit
  def draw(g: Graphics2D) = {
    this.creeps.foreach { x => x.draw(g) }
    this.towers.foreach { x => x.draw(g) }
  }

  def canSpawn: Boolean = {
    val randV = rand.nextInt(10)
    if (this.total == 70)  this.rate = 35
    if (this.total == 150) this.rate = 20
    if (this.total >= 200 && this.total % 20 == 0) creeps.foreach { x => x.setArmor(armor) }
    if (this.total > 200 && this.total % this.incAmror == 0) this.armor += 1000
    if (this.creeps.isEmpty && GameScreen.start == 1) true else if (GameScreen.start == 1 && this.creeps.last.x + randV >= this.rate) true else false
  }

  def spawn = {
    spawnCreep(new NormalCreep)
  }

}