package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer {

	// Constants //
	
	// Camera related stuff...
	private static final float FOV = 70; // angle in degrees
	private static final float NEAR_PLANE = 0.1f; // closest point
	private static final float FAR_PLANE = 1000; // farthest point
	
	private static final float RED = 0.5f;
	private static final float GREEN = 0.5f;
	private static final float BLUE = 0.5f;
	
	// Fields //
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	// Constructors //
	
	public MasterRenderer() {
		// Cull the faces that aren't visible on the inside of objects...
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
	}
	
	// Methods //
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
    public void prepare(){
    	GL11.glEnable(GL11.GL_DEPTH_TEST);
    	// Clear stuff...
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1); // Determines the sky color...
    }
	
	public void render(Light sun, Camera camera) {
		prepare();
		shader.start();
		// Load up the sky color...
		shader.loadSkyColour(RED,GREEN,BLUE);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		// When done with the render...
		shader.stop();
		terrainShader.start();
		terrainShader.loadSkyColour(RED,GREEN,BLUE);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
			batch.add(entity);
		} else {
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				entities.put(entityModel, newBatch);
		}
	}
	
    private void createProjectionMatrix() {
    	float aspect = Display.getWidth() / Display.getHeight();
    	float y_scale = (float) ((1F / Math.tan(Math.toRadians(FOV / 2F))) * aspect);
    	float x_scale = y_scale / aspect;
    	float frustrum_length = FAR_PLANE - NEAR_PLANE;

    	projectionMatrix = new Matrix4f();
    	projectionMatrix.m00 = x_scale;
    	projectionMatrix.m11 = y_scale;
    	projectionMatrix.m22 = -((FAR_PLANE - NEAR_PLANE) / frustrum_length);
    	projectionMatrix.m23 = -1;
    	projectionMatrix.m32 = -((2 * FAR_PLANE * NEAR_PLANE) / frustrum_length);
    	projectionMatrix.m33 = 0;
    }
}
