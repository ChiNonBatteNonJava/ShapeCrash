package Physic;

import org.json.simple.JSONObject;

import com.badlogic.gdx.math.Vector3;

public class PhysicCarStatus {
	public float steering=0;
	public Vector3 position;
	public Vector3 linearVelocity;
	public Vector3 angularVelocity;

	public JSONObject toJson(){
		JSONObject json = new JSONObject();
		JSONObject positionJson = new JSONObject();
		JSONObject linearJson = new JSONObject();
		JSONObject angularJson = new JSONObject();
		
		positionJson.put("x",position.x);	
		positionJson.put("y",position.y);
		positionJson.put("z",position.z);
		
		linearJson.put("x",linearVelocity.x);
		linearJson.put("y",linearVelocity.y);
		linearJson.put("z",linearVelocity.z);
		
		angularJson.put("x",angularVelocity.x);
		angularJson.put("y",angularVelocity.y);
		angularJson.put("z",angularVelocity.z);
		
		json.put("steering", steering);
		json.put("position", positionJson);
		json.put("linear", linearJson);
		json.put("angular", angularJson);
		
		return json;
	}
	
}
