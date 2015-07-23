package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManger;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Dragon;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiTexture;
import guis.guiRenderer;

public class MainGameLoop {

	public static void main(String[] args) {
		
		// Create the display.
		DisplayManger.createDisplay();
		
		// Instantiate the loader class
        Loader loader = new Loader();
        
        // ******* TERRAIN TEXTURE STUFF ************
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
        
        
        // Load up data from obj files...
        ModelData dragonData = OBJFileLoader.loadOBJ("dragon");
        ModelData treeData = OBJFileLoader.loadOBJ("lowPolyTree");
        ModelData fernData = OBJFileLoader.loadOBJ("fern");
        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        // End data loading...
        
        // Create rawmodel objects from the data...
        RawModel treeRawModel = 
        		loader.loadToVAO(
        				treeData.getVertices(), 
        				treeData.getTextureCoords(), 
        				treeData.getNormals(), 
        				treeData.getIndices()
        				);
        RawModel dragonRawModel = 
        		loader.loadToVAO(
        				dragonData.getVertices(), 
        				dragonData.getTextureCoords(), 
        				dragonData.getNormals(), 
        				dragonData.getIndices()
        			);
        RawModel fernRawModel = 
        		loader.loadToVAO(
        				fernData.getVertices(), 
        				fernData.getTextureCoords(), 
        				fernData.getNormals(), 
        				fernData.getIndices()
        			);
        RawModel grassRawModel = 
        		loader.loadToVAO(
        				grassData.getVertices(), 
        				grassData.getTextureCoords(), 
        				grassData.getNormals(), 
        				grassData.getIndices()
        			);
        // ... End rawModel creation.
           
        // Create textureModel objects with the RawModels...
        TexturedModel dragonModel =       		new TexturedModel(dragonRawModel, new ModelTexture(loader.loadTexture("dragonScales")));
        TexturedModel treeModel =         		new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("treeTexture")));
        TexturedModel fernModel =         		new TexturedModel(fernRawModel, new ModelTexture(loader.loadTexture("fernTexture")));
        TexturedModel grassModel =         		new TexturedModel(grassRawModel, new ModelTexture(loader.loadTexture("grassObjTexture")));
        // ...End texturedModel creation.
        
        // Configure the textures for models...
        
        ModelTexture dragonTexture = dragonModel.getTexture();
        dragonTexture.setShineDamper(10);
        dragonTexture.setReflectivity(1);
        
        ModelTexture treeTexture = treeModel.getTexture();
        treeTexture.setReflectivity(0);
        
        ModelTexture fernTextureAtlas = fernModel.getTexture();
        fernTextureAtlas.setReflectivity(0);
        fernTextureAtlas.setHasTrasnsparency(true);
        fernTextureAtlas.setNumberOfRows(2);
        fernTextureAtlas.setUseFakeLighting(true);
        
        ModelTexture grassTexture = grassModel.getTexture();
        grassTexture.setShineDamper(10);
        grassTexture.setReflectivity(0);
        grassTexture.setHasTrasnsparency(true);
        grassTexture.setUseFakeLighting(true);
        
       
        // ... End texture config.
        
        // Create terrain for testing
        Terrain terrain = new Terrain(0,-1,loader,texturePack,blendMap,"heightMap");
        //Terrain terrain2 = new Terrain(-1,-1,loader,texturePack,blendMap,"heightMap");

        
        // Create Entity for testing
        
        //Create the DRAGGGOOON
        int dragX = (int) (Math.random()*250);
        int dragZ = - (int) (Math.random() * 250);
        int dragY = (int) terrain.getHeightOfTerrain(dragX, dragZ);
        Dragon dragon = new Dragon(dragonModel, new Vector3f(dragX,dragY,dragZ),0,0,0,1);
        
        Entity tree1 = new Entity(treeModel, new Vector3f(80,0,-75),0,0,0,0.5f);
        Entity tree2 = new Entity(treeModel, new Vector3f(110,0,-60),0,0,0,0.5f);
        Entity fern1 = new Entity(fernModel,3, new Vector3f(100,0,-58),0,0,0,1);
        Entity grass = new Entity(grassModel, new Vector3f(10,0,-10),0,0,0,1);
        
        // Create a light source...
        Light light = new Light(new Vector3f(500,500,-80),new Vector3f(1,1,1));
        
       
        //Randomised grass Entities
  		ArrayList<Entity> ferns = new ArrayList<Entity>();
  		for (int i = 0; i < 400; i++) {
  			int x = (int) (Math.random() * 800);
  			int z = - (int) (Math.random() * 800);
  			int y = (int) terrain.getHeightOfTerrain(x, z);
  			ferns.add(new Entity(fernModel,(int)(Math.random()*4), new Vector3f(x,y,z), 0, 0,
  					0, 2.3f));
  		}
        
       
        // Instantiate the renderer class...
        MasterRenderer renderer = new MasterRenderer();
        
        // Player Creation
        RawModel  bunnyModel = OBJLoader.loadOBJModel("stanfordBunny", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel,new ModelTexture(loader.loadTexture("white")));
        Player player = new Player(stanfordBunny, new Vector3f(100,0,-50), 0, 0, 0, 0.7f);
        //Turn the bunny around
        player.increaseRotation(0,180, 0);
        // Create a camera...
        Camera camera = new Camera(player);
        
        //GUI Renderering
        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("title"), new Vector2f(-0.75f,0.75f), new Vector2f(0.25f,0.25f));
        guis.add(gui);
        
        guiRenderer guiRenderer = new guiRenderer(loader);
		// Main game loop:
		while(!Display.isCloseRequested()) { // Run until close request

			// Move the camera....
			camera.move();
			
			// Move player
			player.move(terrain);
			
			//check if bunny has found dragon
			player.checkWinCondition(dragon,terrain);
			if(Keyboard.isKeyDown(Keyboard.KEY_P)){
				System.out.println("Player "+player.getPosition());
				System.out.println("Dragon "+dragon.getPosition());
			}

			// Process terrains...
			renderer.processTerrain(terrain);
			//renderer.processTerrain(terrain2);

			
			// Process Entities...
			renderer.processEntity(dragon);
			dragon.increaseRotation(0, 0.5f, 0);
			renderer.processEntity(tree1);
			renderer.processEntity(tree2);
			renderer.processEntity(fern1);
			renderer.processEntity(grass);
			// Render player entity
			renderer.processEntity(player);
			
			//render all ferns
			for(Entity e : ferns)
				renderer.processEntity(e);
		
			// Render the scene...
			renderer.render(light, camera);
			guiRenderer.render(guis);
			DisplayManger.updateDisplay();
			
		}

		// Clean up resources...
		guiRenderer.cleanUP();
		renderer.cleanUp();
        loader.cleanUp();
        
		// Close display when loop is over
		DisplayManger.closeDisplay();
		
	}

	
}
