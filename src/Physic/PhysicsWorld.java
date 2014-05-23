package Physic;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMesh;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;
import com.chinonbattenonjava.saproject.GameResourceManager;
import com.chinonbattenonjava.saproject.GameState;

class SequentialImpulseConstraintSolver extends
		btSequentialImpulseConstraintSolver {

	@Override
	protected void finalize() throws Throwable {
		this.release();
		this.destroyed = true;
		super.finalize();

	}
}

class RigidBodyConstructionInfo extends btRigidBodyConstructionInfo {

	public RigidBodyConstructionInfo(float mass, btMotionState motionState,
			btCollisionShape collisionShape, Vector3 fallInertia) {
		super(mass, motionState, collisionShape, fallInertia);

	}

	@Override
	protected void finalize() throws Throwable {
		this.release();
		this.destroyed = true;
		super.finalize();
		// Log.i("DC", "RigidBodyConstructionInfo");

	}

}

class DefaultCollisionConfiguration extends btDefaultCollisionConfiguration {

	@Override
	protected void finalize() throws Throwable {
		
		
		// this.delete();
		this.release();
		this.destroyed = true;
		super.finalize();
		// Log.i("DC", "DefaultCollisionConfiguration");

	}
}

class DbvtBroadphase extends btDbvtBroadphase {

	@Override
	protected void finalize() throws Throwable {

		this.release();
		this.destroyed = true;
		super.finalize();
		// Log.i("DC", "DbvtBroadphase");

	}

}

class DefaultMotionState extends btDefaultMotionState {

	public void del() {
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {

		this.release();
		this.destroyed = true;
		super.finalize();
		// Log.i("DC", "DbvtBroadphase");

	}

}

class CollisionDispatcher extends btCollisionDispatcher {

	public CollisionDispatcher(btCollisionConfiguration collisionConfiguration) {
		super(collisionConfiguration);
	}

	@Override
	protected void finalize() throws Throwable {
		// this.delete();
		this.release();
		this.destroyed = true;
		super.finalize();
	
		// Log.i("DC", "Col  lisionDispatcher");

	}

}

class Transform extends btTransform {

	public Transform() {
		super();
	}

	public Transform(Quaternion arg0, Vector3 arg1) {
		super(arg0, arg1);
	}

	@Override
	protected void finalize() throws Throwable {
		// this.del ete();
		this.release();
		this.destroyed = true;
		super.finalize();
		// Log.i("DC", "btTransform");

	}
}

public class PhysicsWorld {
	
	private static HashMap<String, PhysicsWorld> physicsWorld = new HashMap<String, PhysicsWorld>();
	
	private btDiscreteDynamicsWorld dynamicsWorld;

	private PhysicsWorld() {

		//Bullet.init(false, true);

		map = new HashMap<String, btRigidBody>();
		cars = new HashMap<String, PhysicCar>();

		DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		CollisionDispatcher dispatcher = new CollisionDispatcher(
				collisionConfiguration);

		DbvtBroadphase broadphase = new DbvtBroadphase();
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase,
				solver, collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3(0, -9.8f, 0));

	}

	Map<String, btRigidBody> map;
	Map<String, PhysicCar> cars;

	
	public void addSphere(float size,Vector3 position,float mass,String name){
		btCollisionShape fallShape = new btSphereShape(size);
		DefaultMotionState fallMotionState = new DefaultMotionState();
		Transform as = new Transform();
		as.setOrigin(position);

		Matrix4 ama = new Matrix4();
		ama.setToTranslation(position);
		fallMotionState.setWorldTransform(ama);

		Vector3 fallInertia = new Vector3(0, 1, 0);

		fallShape.calculateLocalInertia(mass, fallInertia);

		RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(
				mass, fallMotionState, fallShape, fallInertia);

		btRigidBody myRigidBody = new btRigidBody(fallRigidBodyCI);

		map.put(name, myRigidBody);

		dynamicsWorld.addRigidBody(myRigidBody);
		

		fallMotionState.del();
		
	}
	
	public void addBox(Vector3 position, String name, Vector3 size, float mass) {
		long heapSize = Runtime.getRuntime().totalMemory();
		
		Log.i("heap", this.getClass().getName() + " addnjBox " + heapSize);

		btCollisionShape fallShape = new btBoxShape(size);
		DefaultMotionState fallMotionState = new DefaultMotionState();
		Transform as = new Transform();
		as.setOrigin(position);

		Matrix4 ama = new Matrix4();
		ama.setToTranslation(position);
		fallMotionState.setWorldTransform(ama);

		Vector3 fallInertia = new Vector3(0, 0, 0);

		fallShape.calculateLocalInertia(mass, fallInertia);

		RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(
				mass, fallMotionState, fallShape, fallInertia);

		btRigidBody myRigidBody = new btRigidBody(fallRigidBodyCI);

		map.put(name, myRigidBody);

		dynamicsWorld.addRigidBody(myRigidBody);
		

		fallMotionState.del();


	}

	private btTriangleMesh editTriangleMeshes(Vector3 arrayVector[]) {
		btTriangleMesh mTriMesh = new btTriangleMesh();
		for (int i = 0; i < arrayVector.length; i += 3) {
			mTriMesh.addTriangle(arrayVector[i + 0], arrayVector[i + 1],
					arrayVector[i + 2]);

		}
		return mTriMesh;
	}
	
	private btTriangleMesh editTriangleMeshes(float arrayVector[]) {
		btTriangleMesh mTriMesh = new btTriangleMesh();
		for (int i = 0; i < arrayVector.length; i += 9) {
			mTriMesh.addTriangle(new Vector3(arrayVector[i + 0],arrayVector[i + 1],arrayVector[i + 2]),new Vector3(arrayVector[i + 3],arrayVector[i + 4],arrayVector[i + 5]),
					new Vector3(arrayVector[i + 6],arrayVector[i + 7],arrayVector[i + 8]));

		}
		return mTriMesh;
	}

	public void addMeshCollider(Vector3[] vertices, Vector3 position,Quaternion rotation, float mass, String name,short group,short mask) {

		btTriangleMesh mTriMesh = editTriangleMeshes(vertices);
		
	    

		btCollisionShape mTriMeshShape = new btBvhTriangleMeshShape(mTriMesh,true);

		DefaultMotionState fallMotionStateTriangle = new DefaultMotionState();
		fallMotionStateTriangle.setStartWorldTrans(new Transform(rotation,
				position));

		Vector3 fallInertiaT = new Vector3(0, 1, 0);
		mTriMeshShape.calculateLocalInertia(mass, fallInertiaT);

		RigidBodyConstructionInfo fallRigidBodyCIT = new RigidBodyConstructionInfo(
				mass, fallMotionStateTriangle, mTriMeshShape, fallInertiaT);
		fallRigidBodyCIT.setRestitution(1.0f);
		fallRigidBodyCIT.setFriction(100.5f);
		fallRigidBodyCIT.setLinearDamping(100);
		
		btRigidBody myNewBody = new btRigidBody(fallRigidBodyCIT);
		dynamicsWorld.addRigidBody(myNewBody,group,mask);
		map.put(name, myNewBody);
	
		fallMotionStateTriangle.del();
		mTriMesh.release();
		

	}

	public static PhysicsWorld instance(String worldName) {
		if (!physicsWorld.containsKey(worldName)) {
			physicsWorld.put(worldName, new PhysicsWorld());

		}
		return physicsWorld.get(worldName);

	}
	
	public void update() {

		dynamicsWorld.stepSimulation(1.0f / 30.0f, 2, 1.0f / 60.0f);

		for (PhysicCar p : cars.values()) {
			p.updateCar();
		}
	

	}

	public void update(float t1) {
		
		t1=t1*2.5f;
		int t2 = (int) Math.ceil(t1/0.005f); //* (int) Math.ceil(t1 / (1.0f / 30.0f));
		float t3 = t1/t2;
		for (PhysicCar p : cars.values()) {
			p.updateCar();
		}
		dynamicsWorld.stepSimulation(t1, t2, t3);
		//Log.i("fr",""+t1);

	}

	
	public float[] getMatrixName(String name) {
		Matrix4 muu1 = new Matrix4();
		if(map.containsKey(name)){
		map.get(name).getMotionState().getWorldTransform(muu1);
		float[] r = muu1.getValues();
		muu1 = null;
		return r;
		}
		return new float[]{1,0,0,0
						  ,0,1,0,0,
						   0,0,1,0,
						   0,0,0,1};
	}

	static btRigidBody localCreateRigidBody(float mass,
			Transform startTransform, btCollisionShape shape,
			btDynamicsWorld world) {

		Vector3 localInertia = new Vector3(0, 0, 0);

		shape.calculateLocalInertia(mass, localInertia);

		DefaultMotionState myMotionState = new DefaultMotionState();
		myMotionState.setStartWorldTrans(startTransform);

		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass,
				myMotionState, shape, localInertia);

		btRigidBody body = new btRigidBody(cInfo);

		world.addRigidBody(body);

		myMotionState.del();
		// cInfo.dispose();
		// myMotionState.dispose();
		// startTransform.dispose();
		// localInertia=null;
		return body;
	}

	public btDiscreteDynamicsWorld getWorld() {

		return this.dynamicsWorld;
	}

	static public btCollisionShape getBoxCollisionShape(Vector3 size) {

		return new btBoxShape(size);
	}

	public void AddVehicle(PhysicCar car, String name) {
		cars.put(name, car);

	}

	public float[][] getVheicleWhells(String carName) {
		return cars.get(carName).getWhellMatrix();

	}

	public float[] getWheicleChaiss(String carName) {
		return cars.get(carName).getMatrixChassisCar();

	}

	public Vector3 getVheiclePosition(String carName) {
		if(cars.containsKey(carName)){
			return cars.get(carName).getCarPosition();
		}
		return new Vector3();

	}

	public PhysicCar getVheicle(String carName) {
		if(cars.containsKey(carName)){
			return cars.get(carName);
		}
		return null;

	}

	public int getVehicleCount() {

		return cars.size();
	}

	public void addShape(float[] vertices,Vector3 position,float mass,String name,short group,short mask){
	
		btTriangleMesh mTriMesh = editTriangleMeshes(vertices);

		btCollisionShape mTriMeshShape = new btConvexTriangleMeshShape(mTriMesh);

		DefaultMotionState fallMotionStateTriangle = new DefaultMotionState();
		fallMotionStateTriangle.setStartWorldTrans(new Transform(new Quaternion(0,0,0,1),
				position));

		Vector3 fallInertiaT = new Vector3(0, 1, 0);
		mTriMeshShape.calculateLocalInertia(mass, fallInertiaT);

		RigidBodyConstructionInfo fallRigidBodyCIT = new RigidBodyConstructionInfo(mass, fallMotionStateTriangle, mTriMeshShape, fallInertiaT);
	
		fallRigidBodyCIT.setRestitution(1.0f);
		fallRigidBodyCIT.setFriction(100.5f);
		fallRigidBodyCIT.setLinearDamping(100);
		
		btRigidBody myNewBody = new btRigidBody(fallRigidBodyCIT);
		dynamicsWorld.addRigidBody(myNewBody,group,mask);
		map.put(name, myNewBody);
		
		fallMotionStateTriangle.del();
		mTriMesh.release();
		
	}

	public void delete(String name){
		if (cars.containsKey(name)){
			
			
			
			cars.remove(name);
		}
		else if (map.containsKey(name)){
			map.remove(name);
			
			
		}
		
	}
	
	
	public static void reset(){
		
		physicsWorld= new HashMap<String, PhysicsWorld>();
	}
	
	
	@Override
	public void finalize() {
		Log.i("Delete", "PhyisicWorld");
		for (btRigidBody b : map.values()) {
			b.dispose();

		}
		dynamicsWorld.dispose();
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}