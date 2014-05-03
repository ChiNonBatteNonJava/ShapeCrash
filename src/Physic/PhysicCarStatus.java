package Physic;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

import com.badlogic.gdx.math.Vector3;

public class PhysicCarStatus {
	public float steering=0;
	public Vector3 position;
	public Vector3 linearVelocity;
	public Vector3 angularVelocity;
	
	public PhysicCarStatus(){
		position = new Vector3();
		linearVelocity = new Vector3();
		angularVelocity = new Vector3();
	}
	
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
	
	public void fromJSON(JSONObject json) throws ParseException{
		
		Log.i("bnf",json.toJSONString());
		steering =  ((Double) json.get("steering")).floatValue();
		
		JSONObject positionJson = (JSONObject) json.get("position");
		JSONObject linearJson =  (JSONObject) json.get("linear");
		JSONObject angularJson =  (JSONObject) json.get("angular");
		
		Double d = (Double) positionJson.get("x");
		
		Log.i("bnf",d.toString());
		
		position.x = ((Double) positionJson.get("x")).floatValue();
		position.y = ((Double) positionJson.get("y")).floatValue();
		position.z = ((Double) positionJson.get("z")).floatValue();
		
		Log.i("bnf","asd");
		
		linearVelocity.x = ((Double) linearJson.get("x")).floatValue();
		linearVelocity.y = ((Double) linearJson.get("y")).floatValue();
		linearVelocity.z = ((Double) linearJson.get("z")).floatValue();
		
		Log.i("bnf","zxc");
		
		angularVelocity.x = ((Double) angularJson.get("x")).floatValue();
		angularVelocity.y = ((Double) angularJson.get("y")).floatValue();
		angularVelocity.z = ((Double) angularJson.get("z")).floatValue();
	}
	
}
