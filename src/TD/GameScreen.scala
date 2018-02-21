package TD

import scala.swing._
import javax.imageio.ImageIO
import java.io.File
import java.awt.event.ActionListener
import java.awt.Color
import scala.swing.BorderPanel.Position._
import scala.swing.event.ButtonClicked
import javax.swing.border.MatteBorder
import scala.swing.event.MouseMoved
import scala.swing.event.MouseClicked
import scala.swing.event.MousePressed
import scala.swing.event.KeyPressed
import scala.swing.event.KeyTyped
import scala.collection.mutable.Buffer
import scala.collection.script.Message
import javax.swing.ImageIcon

object GameScreen extends SimpleSwingApplication {

  val width = 605
  val height = 600
  val fullHeight = 740 // 100 verran suurempi, ett� selection ikkuna mahtuu
  var mouseX = 0
  var mouseY = 0
  var start = 0
  var playMusic = true
  var selectedTower: Option[Tower] = None
  var canBuy = true

  // Nopeus, jolla peli py�rii
  val timerSpeed = 25

  val map = ImageIO.read(new File("testifield.png")) // Kartta
  val img = ImageIO.read(new File("4cd.png")) // Game Over Kuva
  val img2 = ImageIO.read(new File("jumalautaeihelvetti.png")) // Aloituskuva

  def top = new MainFrame {

    minimumSize = new Dimension(width, fullHeight)
    preferredSize = new Dimension(width, fullHeight)
    maximumSize = new Dimension(width, fullHeight)

    title = "Dark Souls Tower Defence"
    resizable = false // Voidaan muuttaa my�hemmin tukemaan suurempaa resoluutiota || 19.3

    ////////////////
    //  NAPPULAT  //
    ////////////////

    // Pause-nappula || 23.4
    val pButton = new Button("Pause")
    var pauseState = 0
    val nextWave = new Button("Start Game")

    // Tornin valinta menu
    val menu = new MenuBar {
      val tesla = new TeslaTower
      contents += new Menu("Buy Towers") {
        contents += new MenuItem(Action("Tesla Tower - 100 $") {
          selectedTower = Some(new TeslaTower)
        })
        contents += new MenuItem(Action("Quake Tower - 250 $") {
          selectedTower = Some(new QuakeTower)
        })
        contents += new MenuItem(Action("Double Damage Tower - 500 $") {
          selectedTower = Some(new DoubleDamageTower)
        })
        contents += new MenuItem(Action("Double ROF Tower - 500 $") {
          selectedTower = Some(new DoubleRofTower)
        })
      }
    }

    // Pelaajan HP || 23.4
    val healthBar = new Label
    val separator = new Label
    val cashBar = new Label
    val towerSelectionBar = new Label {
      border = new MatteBorder(1, 1, 1, 1, Color.GRAY)

      def isTowerSelected: Unit = {
        val tower = GameScreen.selectedTower
        if (tower.isEmpty) this.text = "No tower selected"
        else if (Player.CASH < tower.get.price) this.text = "Not enough cash!"
        else this.text = tower.get.name + " selected"
      }
    }

    //  Uusi ikkuna, johon on tarkoitus lis�t� nappulat
    val selection = new FlowPanel {
      minimumSize = new Dimension(width, 100)
      preferredSize = new Dimension(width, 100)
      maximumSize = new Dimension(width, 100)
      background = Color.CYAN

      contents += (pButton, menu, healthBar, separator, cashBar, towerSelectionBar, nextWave)
      border = new MatteBorder(15, 15, 15, 15, Color.GRAY)
    }

    // t�ll� voidaan piirt�� taustakuva || 19.3
    val screen = new BorderPanel {

      layout(selection) = South
      listenTo(mouse.clicks, mouse.moves, keys)

      def towerDraw(g: Graphics2D) = {
        val tower = selectedTower.get
        var canPlace = !(mouseX - 55 - tower.size <= mouseY && mouseY - 55 - tower.size <= mouseX || mouseY >= 600)
        canBuy = canPlace
        canPlace match {
          case true => {
            g.setColor(Color.CYAN)
            g.fillRect(mouseX - tower.size / 2, mouseY - tower.size / 2, tower.size, tower.size)
            g.setColor(Color.GREEN)
            g.drawOval(mouseX - tower.range, mouseY - tower.range, tower.range * 2, tower.range * 2)
          }
          case false => {
            g.setColor(Color.RED)
            g.fillRect(mouseX - tower.size / 2, mouseY - tower.size / 2, tower.size, tower.size)
            g.setColor(Color.RED)
            g.drawOval(mouseX - tower.range, mouseY - tower.range, tower.range * 2, tower.range * 2)
          }
        }
      }

      reactions += {
        case mouseMoved: MouseMoved => {
          requestFocus
          mouseX = mouseMoved.point.x
          mouseY = mouseMoved.point.y
        }
        case mousePressed: MousePressed => {
          if (selectedTower.isDefined && canBuy) {
            selectedTower.get.setCoords
            Game.towerBought(selectedTower.get)
            println("ostettu'd")
          } else if (!canBuy) println("Can't place here!")
          repaint
        }
        case keyTyped: KeyTyped => {
          if (keyTyped.char == ' ') selectedTower = None
        }
      }

      override def paintComponent(g: Graphics2D) = {
        g.drawImage(map, 0, 0, null)
        Game.draw(g)
        if (selectedTower.isDefined && Player.CASH >= selectedTower.get.price) towerDraw(g)
      }
    }

    contents = screen

    // Listeneri, joka py�ritt�� peli�
    val listener = new ActionListener {
      def actionPerformed(e: java.awt.event.ActionEvent) = {
        screen.repaint()
        selection.repaint()
        healthBar.text = if (Player.HP > 1) Player.HP.toString + " lives left";
        else if (Player.HP == 1) Player.HP.toString + " life left";
        else "Game Over!"
        cashBar.text = "Cash: " + Player.CASH.toString + " $"
        towerSelectionBar.isTowerSelected
        Game.update
        if (Player.HP == 0) {
          music.stopMusic
          thread2.start
          Dialog.showMessage(screen, "You have failed. Accept your fate.", ":D    E    F    E    A    T", Dialog.Message.Error, new ImageIcon(img))
          quit
        }
      }
    }

    listenTo(pButton, nextWave)

    reactions += {
      case b: ButtonClicked => {
        if (b.source == pButton && pauseState == 0) {
          pButton.text = "Resume"
          timer.stop
          pauseState = 1
        } else {
          pButton.text = "Pause"
          timer.start
          pauseState = 0
          start = 1
        }
        if (b.source == nextWave) {
          Game.spawn
          nextWave.enabled = false
          nextWave.text = "Good Luck!"
        }
        val sword11 = new MusicPlayer(new StereoSound("sword1"), 1)
        val threadEffect1 = new Thread(sword11).start
      }

    }
    
    val timer = new javax.swing.Timer(timerSpeed, listener)
    timer.start()
    // Soundeffects
    val music = new MusicPlayer(new StereoSound("startsong"), 0)
    val thread = new Thread(music)
    val gameover = new MusicPlayer(new StereoSound("endsong"), 0)
    val thread2 = new Thread(gameover)
    val sword11 = new MusicPlayer(new StereoSound("sword1"), 1)
    val threadEffect1 = new Thread(sword11)
    threadEffect1.start
    thread.start

    Dialog.showMessage(selection, "To unselect tower, press space. You are not allowed to sell your towers. Good luck!",
      "Welcome to Tower Defence", Dialog.Message.Info, new ImageIcon(img2))
  }

}