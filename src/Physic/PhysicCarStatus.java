package Physic;

import org.json.simple.JSONObject;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class PhysicCarStatus {
	public float steering=0;
	public Vector3 position;
	public Vector3 linearVelocity;
	public Vector3 angularVelocity;
	public Quaternion orientation;
	
	public PhysicCarStatus(){
		position = new Vector3();
		linearVelocity = new Vector3();
		angularVelocity = new Vector3();
		orientation = new Quaternion();
	}
	
	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		JSONObject positionJson = new JSONObject();
		JSONObject linearJson = new JSONObject();
		JSONObject angularJson = new JSONObject();
		JSONObject orientationJson = new JSONObject();
		
		positionJson.put("x",position.x);	
		positionJson.put("y",position.y);
		positionJson.put("z",position.z);
		
		linearJson.put("x",linearVelocity.x);
		linearJson.put("y",linearVelocity.y);
		linearJson.put("z",linearVelocity.z);
		
		angularJson.put("x",angularVelocity.x);
		angularJson.put("y",angularVelocity.y);
		angularJson.put("z",angularVelocity.z);
		
		orientationJson.put("x",orientation.x);
		orientationJson.put("y",orientation.y);
		orientationJson.put("z",orientation.z);
		orientationJson.put("w",orientation.w);
		
		json.put("steering", steering);
		json.put("position", positionJson);
		json.put("linear", linearJson);
		json.put("angular", angularJson);
		json.put("orientation", orientationJson);
		
		return json;
	}
	
	public void fromJSON(JSONObject json){
		steering = ((Double) json.get("steering")).floatValue();
		JSONObject positionJson = (JSONObject) json.get("position");
		JSONObject linearJson =  (JSONObject) json.get("linear");
		JSONObject angularJson =  (JSONObject) json.get("angular");
		JSONObject orientationJson = (JSONObject) json.get("orientation");
		
		position.x = ((Double) positionJson.get("x")).floatValue();
		position.y = ((Double) positionJson.get("y")).floatValue();
		position.z = ((Double) positionJson.get("z")).floatValue();
		
		linearVelocity.x = ((Double) linearJson.get("x")).floatValue();
		linearVelocity.y = ((Double) linearJson.get("y")).floatValue();
		linearVelocity.z = ((Double) linearJson.get("z")).floatValue();
		
		angularVelocity.x = ((Double) angularJson.get("x")).floatValue();
		angularVelocity.y = ((Double) angularJson.get("y")).floatValue();
		angularVelocity.z = ((Double) angularJson.get("z")).floatValue();

		orientation.x = ((Double) orientationJson.get("x")).floatValue();
		orientation.x = ((Double) orientationJson.get("y")).floatValue();
		orientation.x = ((Double) orientationJson.get("z")).floatValue();
		orientation.x = ((Double) orientationJson.get("w")).floatValue();
	}
	
}