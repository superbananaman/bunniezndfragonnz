package renderEngine;
 
import java.util.List;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import textures.ModelTexture;
import toolBox.Maths;
import entities.Entity;
 
public class EntityRenderer {
	
	// Fields //
	
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	// Constructors //
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		// Only have to do this matrix stuff the one time
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	// Methods //
    
    public void render(Map<TexturedModel,List<Entity>> entities) {
    	// Foreach model in the list...
    	for(TexturedModel model:entities.keySet()) {
    		// Prepare it...
    		prepareTexturedModel(model);
    		// Create a batch...
    		List<Entity> batch = entities.get(model);
    		// For each entity in the batch...
    		for(Entity entity:batch) {
    			// Prepare the entity...
    			prepareInstance(entity);
    	        // Draw the elements...
    	        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexcount(), GL11.GL_UNSIGNED_INT, 0);
    		}
    		// Now unbind...
    		unbindTexturedModel();
    	}
    }
    
    private void prepareTexturedModel(TexturedModel model) {
    	// Get the rawModel from the textured model
    	RawModel rawModel = model.getRawModel();
    	// Bind the vertex array to the VAO
        GL30.glBindVertexArray(rawModel.getVaoID());
        // Enable the attribute arrays for the VAO
        GL20.glEnableVertexAttribArray(0); // slot 1
        GL20.glEnableVertexAttribArray(1); // slot 2
        GL20.glEnableVertexAttribArray(2); // slot 3
        ModelTexture texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if(texture.isHasTrasnsparency()) {
        	MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        // Set the active texture...
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        // Bind the texture...
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,model.getTexture().getTextureID());
    }
    
    private void unbindTexturedModel() {
    	MasterRenderer.enableCulling();
        // Disable after rendering....
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        // Bind the vertex array. (not sure why here)
        GL30.glBindVertexArray(0);
    }
    
    private void prepareInstance(Entity entity) {
        // Create a 4 x 4 matrix for transformations to the render
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
        		entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        // Load the transformation matrix into the shader...
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }
 
}