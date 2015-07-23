package renderEngine;
 
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
 
public class DisplayManger {
     
	// Fields //
	
    private static final int WIDTH = 1280;	// Window width
    private static final int HEIGHT = 720;	// Window height
    private static final int FPS_CAP = 120;	// fps limit
    private static long lastFrameTime;
    private static float delta;
     
    // Methods //
    
    public static void createDisplay(){     
        ContextAttribs attribs = new ContextAttribs(3,2)
        .withForwardCompatible(true)
        .withProfileCore(true);
         
        try {
        	// Configure the display...
            Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Bunniez 'n' Dragoonz");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
         
        GL11.glViewport(0,0, WIDTH, HEIGHT);
        lastFrameTime = getCurrentTime();
    }
     
    public static void updateDisplay(){
         
        Display.sync(FPS_CAP);
        Display.update();
        long currentFrameTime = getCurrentTime();
        delta = (currentFrameTime - lastFrameTime)/1000f;
        lastFrameTime = currentFrameTime;
         
    }
    
    public static float getFrameTimeSecondes(){
    	return delta;
    }
     
    public static void closeDisplay(){
         
        Display.destroy();
         
    }
    
    private static long getCurrentTime(){
    	return Sys.getTime()*1000/Sys.getTimerResolution();
    }
    
}