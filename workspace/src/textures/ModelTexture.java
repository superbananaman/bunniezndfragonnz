package textures;

public class ModelTexture {

	private int textureID;
	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean hasTrasnsparency = false;
	private boolean useFakeLighting = false;
	private int numberOfRows = 1;
	
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public ModelTexture(int id){
		this.textureID = id;
	}

	public float getShineDamper() {
		return shineDamper;
	}
	
	

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isHasTrasnsparency() {
		return hasTrasnsparency;
	}

	public void setHasTrasnsparency(boolean hasTrasnsparency) {
		this.hasTrasnsparency = hasTrasnsparency;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public int getTextureID() {
		return textureID;
	}

	
}
