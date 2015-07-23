package models;

public class RawModel {
private int vaoID;

private int vertexcount;

public RawModel(int vaoID, int vertexount){
	this.vaoID=vaoID;
	this.vertexcount = vertexount;
}

public int getVaoID() {
	return vaoID;
}

public int getVertexcount() {
	return vertexcount;
}

}
