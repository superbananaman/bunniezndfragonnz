package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import terrain.Terrain;

public class Dragon extends Entity {

	public Dragon(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void newPosition(Terrain terrain) {
		super.resetPosition();
		int x = (int) (Math.random()*800);
        int z = - (int)(Math.random() * 800);
        int y = (int) terrain.getHeightOfTerrain(x, z);
        
		this.increasePosition(x,y,z);		
	}

}
