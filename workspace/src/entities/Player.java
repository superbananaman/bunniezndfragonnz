package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManger;
import terrain.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 50;
	private static final float TURN_SPEED = 90;
	private static final float GRAVITY = -60;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HRIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed =0;
	
	private boolean isInAir = false;

	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain terrain){
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManger.getFrameTimeSecondes(), 0);
		float distance = currentSpeed * DisplayManger.getFrameTimeSecondes();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed += GRAVITY * DisplayManger.getFrameTimeSecondes();
		super.increasePosition(0, upwardsSpeed * DisplayManger.getFrameTimeSecondes(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight)
		{
			upwardsSpeed = 0 ;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	private void jump(){
		if(!isInAir){
		this.upwardsSpeed = JUMP_POWER;
		isInAir = true;
		}
		
	}
	
	private void checkInputs(){
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			this.currentSpeed = RUN_SPEED;
		else if(Keyboard.isKeyDown(Keyboard.KEY_S))
			this.currentSpeed = - RUN_SPEED;
		else
			this.currentSpeed = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			this.currentTurnSpeed = -TURN_SPEED;
		else if(Keyboard.isKeyDown(Keyboard.KEY_A))
			this.currentTurnSpeed = TURN_SPEED;
		else
			this.currentTurnSpeed = 0;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
	}

	public void checkWinCondition(Dragon dragon, Terrain terrain) {
		float x1 = this.getPosition().getX();
		float z1 = this.getPosition().getZ();
		float x2 = dragon.getPosition().getX();
		float z2 = dragon.getPosition().getZ();

		if (Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((z2 - z1), 2)) < 5) {
			dragon.newPosition(terrain);
			
		}

	}

}
