import java.io.File;
import java.io.IOException;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BombSound implements Runnable,LineListener{
	 boolean playCompleted;
   public String audioFilePath;
   public int duration;
		
		@Override
    public void run() {
      try {
				File audioFile = new File(audioFilePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        AudioFormat format = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        Clip audioClip = (Clip) AudioSystem.getLine(info);
        audioClip.addLineListener(this);
        audioClip.open(audioStream);
				audioClip.start();
        while (!playCompleted) {
          try {
            Thread.sleep(duration*1000);
            } catch (InterruptedException ex) {
            ex.printStackTrace();
            }		
        }			
			audioClip.close();
      }catch(Exception e){e.printStackTrace();}    
    }
     
    @Override
    public void update(LineEvent event) {}
		
    public BombSound(String audioFilePath,int duration) {
        this.audioFilePath = audioFilePath;
				this.duration = duration;
    }
}