package sound;

import java.io.*;
import java.net.URL;
import java.util.*;
import javax.sound.sampled.*;

public class SoundManager
{
	private javax.sound.sampled.Line.Info lineInfo;

    private Vector afs;
    private Vector sizes;
    private Vector infos;
    private Vector audios;
    private int num=0;
    public Vector<Clip> clips=new Vector<Clip>();

    public SoundManager()
    {
            afs=new Vector();
            sizes=new Vector();
            infos=new Vector();
            audios=new Vector();
    }
    
    public Clip clip(int i) {
    	return clips.elementAt(i);
    }
    public boolean clipEnded(int i) {
    	return clip(i).getMicrosecondLength()==clip(i).getMicrosecondPosition();
    }
    public float getVolume(int clipIndex) {
        FloatControl gainControl = (FloatControl) clip(clipIndex).getControl(FloatControl.Type.MASTER_GAIN);        
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(double d, int clipIndex) {
        FloatControl gainControl = (FloatControl) clip(clipIndex).getControl(FloatControl.Type.MASTER_GAIN);        
        gainControl.setValue(20f * (float) Math.log10(d));
    }

    public void addClip(String s)
        throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        FileInputStream fs=new FileInputStream(s);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(loadStream(fs));
            AudioFormat af = audioInputStream.getFormat();
            int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
            byte[] audio = new byte[size];
            DataLine.Info info = new DataLine.Info(Clip.class, af, size);
            audioInputStream.read(audio, 0, size);

            afs.add(af);
            sizes.add(new Integer(size));
            infos.add(info);
            audios.add(audio);

            num++;
            
            clips.add((Clip) AudioSystem.getLine((DataLine.Info)infos.lastElement()));
            clips.lastElement().open((AudioFormat)afs.lastElement(), (byte[])audios.lastElement(), 0, ((Integer)sizes.lastElement()).intValue());
    }

    private ByteArrayInputStream loadStream(InputStream inputstream)
              throws IOException
      {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            byte data[] = new byte[1024];
            for(int i = inputstream.read(data); i != -1; i = inputstream.read(data))
                  bytearrayoutputstream.write(data, 0, i);

            inputstream.close();
            bytearrayoutputstream.close();
            data = bytearrayoutputstream.toByteArray();
            return new ByteArrayInputStream(data);
    }
}
