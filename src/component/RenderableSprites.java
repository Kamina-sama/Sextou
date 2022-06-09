package component;

import javax.imageio.ImageIO;

import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import gameobject.GameObject;
import rendering.IScreen;

public class RenderableSprites extends Component {
	HashMap<String,Vector<BufferedImage>> animations=new HashMap<String, Vector<BufferedImage>>();
	Vector<BufferedImage> animationSprites=null;
	
	public boolean visible=true;
	public int width=0;
	public int height=0;

	public int millisecondsToNextFrame=125;

	private boolean isPlaying=false;
	private int millisecondsLeft=millisecondsToNextFrame;
	private boolean repeat=false;
	private int currentSpriteIndex=-1;
	private boolean animationCompleted=false;
	
	public RenderableSprites(GameObject mine) {
		super(mine);
	}
	@Override
	public void Update(double deltaTime) {
		if(!isPlaying || animationCompleted) return;
		millisecondsLeft-=deltaTime;
		if(millisecondsLeft<=0) {
			int previousIndex=currentSpriteIndex++;
			if(currentSpriteIndex>=animationSprites.size()) {
				if(!repeat) {
					currentSpriteIndex=previousIndex;
					animationCompleted=true;
				}
				else currentSpriteIndex=0;
			}
			millisecondsLeft=millisecondsToNextFrame;
		}
	}
	public void AddAnimation(String animationName, String[] names) {
		Vector<BufferedImage> animation=new Vector<BufferedImage>();
		for(String name: names) {
			FileInputStream fs;
			try {
				fs = new FileInputStream("sprites/"+name);
				BufferedImage s = ImageIO.read(loadStream(fs));
				animation.add(s);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		animations.put(animationName, animation);
	}
	public void SetAnimation(String animationName) {
		animationSprites=animations.get(animationName);
		currentSpriteIndex=0<animationSprites.size()? 0:-1;
	}
	public void SetAnimation(String animationName, int startingIndex) {
		animationSprites=animations.get(animationName);
		currentSpriteIndex=startingIndex<animationSprites.size()? startingIndex:-1;
	}
	public String GetAnimation() {
		for(Entry<String, Vector<BufferedImage>> entry: animations.entrySet()) {
			String key = entry.getKey();
		    Vector<BufferedImage> value = entry.getValue();
		    if(value==animationSprites) return key;
		}
		return null;
	}
	public void Play(boolean repeat) {
		this.repeat=repeat;
		isPlaying=true;
		animationCompleted=false;
	}
	public void Stop() {
		isPlaying=false;
	}
	public BufferedImage CurrentSprite() {
		if(currentSpriteIndex<0 || currentSpriteIndex>=animationSprites.size()) return null;
		return animationSprites.get(currentSpriteIndex);
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
	@Override
	public final void Accept(IScreen rendererVisitor) {
		rendererVisitor.Visit(this);
	}
}
