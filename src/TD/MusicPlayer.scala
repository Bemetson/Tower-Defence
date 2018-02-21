package TD

class MusicPlayer(music: StereoSound, mode: Int) extends Runnable {
  def run: Unit = {
    if (mode == 1) music.play else while (true) music.play
  }
  def stopMusic = music.stopMusic
}