package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	

	private Vector3f position = new Vector3f(0,0,0);
	private float pitch;
	private float yaw;
	private float roll;
	
	private Player player;
	
	
	public Camera(Player player){
		this.player = player;
		pitch = 30;
		distanceFromPlayer = 40;
	}

public void move(){
	calculateZoom();
	calculateAngleAroundPlayer();
	calcluatePitch();
	float horizontalDistance = calculateHorizontalDistance();
	float verticleDistance = calculateVeritcalDistance();
	calculateCameraPosition(horizontalDistance, verticleDistance);
	this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	
}
	public Vector3f getPosition() {
		return position;
	}


	public float getPitch() {
		return pitch;
	}


	public float getYaw() {
		return yaw;
	}


	public float getRoll() {
		return roll;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVeritcalDistance(){
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateCameraPosition(float horDistance, float vertDistance){
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + vertDistance;
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel() * 0.1f;
				distanceFromPlayer -= zoomLevel;
	}
	
	private void calcluatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
}
