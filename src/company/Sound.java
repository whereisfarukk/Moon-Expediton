package company;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    File soundURL[] = new File[40];

    public Sound() {
//        soundURL[0]=getClass().getResource("src/assets/music/start.wav");
//        soundURL[1]=getClass().getResource("src/assets/music/shot.wav");
//        soundURL[2]=getClass().getResource("src/assets/music/bulletimpact.wav");
//        soundURL[3]=getClass().getResource("src/assets/music/meteorhit.wav");
//        soundURL[4]=getClass().getResource("src/assets/music/music.wav");
//        soundURL[5]=getClass().getResource("src/assets/music/hitnode.wav");
//        soundURL[6]=getClass().getResource("src/assets/music/end.wav");
        soundURL[0] = new File("src/assets/music/start.wav");
        soundURL[1] = new File("src/assets/music/shot.wav");
        soundURL[2] = new File("src/assets/music/bulletimpact.wav");
        soundURL[3] = new File("src/assets/music/meteorhit.wav");
        soundURL[4] = new File("src/assets/music/music.wav");
        soundURL[5] = new File("src/assets/music/hitnode.wav");
        soundURL[6] = new File("src/assets/music/end.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            /**
             * when i = 4 means background music ,and background music need to reduce
             */
            if(i==4) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-20.0f);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void play() {
//        FloatControl gainControl =
//                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//        gainControl.setValue(-25.0f);
        clip.start();
    }

    public void loop() {
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

}
