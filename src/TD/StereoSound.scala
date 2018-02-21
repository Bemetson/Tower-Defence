package TD

import scala.collection.mutable.ArrayBuffer

import java.io.{ ByteArrayInputStream, File, InputStream }
import javax.sound.sampled.{ AudioFormat, AudioInputStream, AudioSystem, DataLine, SourceDataLine }

import StereoSoundHelper._

/**
 * Representes a stero sound.
 *
 * `StereoSound` can be created eather from file or from to audio data channels.
 *
 * @constructor creates a new sound that models stereo sound
 * @param fileName path to file that contains stereo sound data
 * @param left     sound data for the left channel
 * @param right    sound data for the right channel
 */
class StereoSound(val fileName: String, val left: ArrayBuffer[Int], val right: ArrayBuffer[Int]) {

  var playmusic = true
  /**
   * @constructor creates a new sound that models stereo sound
   * @param fileName path to file that contains stereo sound data
   */
  def this(fileName: String) {
    this(fileName, getLeftChannel(fileName), getRightChannel(fileName))
  }

  /**
   * @constructor creates a new sound that models stereo soun
   * @param left  sound data for the left channel
   * @param right sound data for the right channel
   */
  def this(left: ArrayBuffer[Int], right: ArrayBuffer[Int]) {
    this("", left, right)
  }

  /**
   * Repeats the sound.
   */
  def play(): Unit = {
    val data: Array[Byte] = mixChannels(left, right)

    try {
      // Get an input stream on the byte array containing the data
      val byteArrayInputStream: InputStream = new ByteArrayInputStream(data)

      val sampleRate: Int = 7500//44100 // our sample rate
      val sampleSizeInBits: Int = 16 // how many bits in a sample
      val channels: Int = 2 // only stereo data
      val signed: Boolean = true
      val bigEndian: Boolean = false

      // create a format to have a right kind of audio input stream
      val audioFormat: AudioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian)
      // input stream for our byte data with our audio format and that is specific length in seconds
      val audioInputStream = new AudioInputStream(byteArrayInputStream, audioFormat,
        data.length / (audioFormat.getFrameSize() / channels))
      val dataLineInfo: DataLine.Info = new DataLine.Info(classOf[SourceDataLine], audioFormat)
      // get a data line to which audio data can be written using our needed format
      val sourceDataLine = AudioSystem.getLine(dataLineInfo).asInstanceOf[SourceDataLine]

      // open the line and start it
      sourceDataLine.open(audioFormat)
      sourceDataLine.start()
      // read data contained in the byte buffer in chunks of samples and write (e.g. play back) it
      var bytesBuffer: Array[Byte] = new Array(sampleSizeInBits)
      var bytesRead: Int = audioInputStream.read(bytesBuffer)
      while (bytesRead != -1 && playmusic) {
        sourceDataLine.write(bytesBuffer, 0, bytesRead)
        bytesRead = audioInputStream.read(bytesBuffer)
      }
      // clean everything
      sourceDataLine.drain()
      sourceDataLine.close()
      audioInputStream.close()

    } catch {
      case e: Exception =>
        println("Tästä ei me päästä yli :(")
        e.printStackTrace()
    }
  }

  def stopMusic() = {
    playmusic = false
  }

}
