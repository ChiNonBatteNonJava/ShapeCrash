package Physics;



import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;








import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMesh;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;

public class PhysicsWorld {
	private static HashMap<String,PhysicsWorld> physicsWorld=new HashMap<String,PhysicsWorld>();
	private btDiscreteDynamicsWorld dynamicsWorld;
	private static  boolean init=false;
	private PhysicsWorld() {
		Bullet.init();
		init=true;
		map = new HashMap<String, btRigidBody>();
		cars=new HashMap<String, PhysicCar>();
		
		btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(
				collisionConfiguration);
		btDbvtBroadphase broadphase = new btDbvtBroadphase();
		btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase,
				solver, collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3(0, 0, -9.8f));

	
	}

	Map<String, btRigidBody> map;
	Map<String, PhysicCar> cars;
	public void addBox(Vector3 position, String name,Vector3 size,float mass) {

		btCollisionShape fallShape = new btBoxShape(size);
		btDefaultMotionState fallMotionState = new btDefaultMotionState();
		btTransform as = new btTransform();
		as.setOrigin(position);

		Matrix4 ama = new Matrix4();
		ama.setToTranslation(position);
		fallMotionState.setWorldTransform(ama);

		
		Vector3 fallInertia = new Vector3(0, 0, 0);

		fallShape.calculateLocalInertia(mass, fallInertia);

		btRigidBodyConstructionInfo fallRigidBodyCI = new btRigidBodyConstructionInfo(
				mass, fallMotionState, fallShape, fallInertia);

		// fallRigidBodyCI.setLinearDamping(1);
		btRigidBody myRigidBody = new btRigidBody(fallRigidBodyCI);
		
		map.put(name, myRigidBody);

		dynamicsWorld.addRigidBody(myRigidBody);
		fallShape.release();
		fallMotionState.release();
		fallRigidBodyCI.release();
		as.release();
	}

	private static btTriangleMesh editTriangleMeshes(Vector3 arrayVector[]) {
		
		btTriangleMesh mTriMesh = new btTriangleMesh();
		 	 
		for (int i = 0; i < arrayVector.length; i+=3) {
			mTriMesh.addTriangle(arrayVector[i + 0], arrayVector[i + 1],
	                              arrayVector[i + 2]);	
		}
		return mTriMesh;
	}

	public void addMeshCollider(Vector3[] vertices,Vector3 position, Quaternion rotation,float mass,String name){
		 
		btTriangleMesh mTriMesh=editTriangleMeshes(vertices);
		
		
		btCollisionShape mTriMeshShape = new btBvhTriangleMeshShape(mTriMesh,true);
	    
		
		btDefaultMotionState fallMotionStateTriangle = new btDefaultMotionState();
		fallMotionStateTriangle.setStartWorldTrans(new btTransform(rotation, position));
	    
	 
		Vector3 fallInertiaT=new Vector3(0, 1, 0);
		mTriMeshShape.calculateLocalInertia(mass, fallInertiaT);
	    
		btRigidBodyConstructionInfo fallRigidBodyCIT=new btRigidBodyConstructionInfo (mass,
	                                                              fallMotionStateTriangle, mTriMeshShape, fallInertiaT);
	 
	    
		btRigidBody myNewBody = new btRigidBody(fallRigidBodyCIT);
		dynamicsWorld.addRigidBody(myNewBody);
		map.put(name, myNewBody);
	
	}
	
	public static btCollisionShape getTriangleCollisionShape(Vector3[] vertices){
		if(!init){
			Bullet.init();
		}
		btTriangleMesh mTriMesh=editTriangleMeshes(vertices);
		btCollisionShape mTriMeshShape = new btBvhTriangleMeshShape(mTriMesh,true);
		return mTriMeshShape;
		
		
	}
	
	public static PhysicsWorld instance(String worldName) {
		if (!physicsWorld.containsKey(worldName)) {
			physicsWorld.put(worldName,new PhysicsWorld());
		}
		return physicsWorld.get(worldName);
	}

	public void update() {
		dynamicsWorld.stepSimulation(1.0f / 30.0f, 2, 1.0f / 60.0f);
		for(PhysicCar p:cars.values()){	
			p.updateCar();
		}
	//	dynamicsWorld.updateVehicles(1f/30.0f);
	
	}
	public void update(float t1) {

		int t2=4*(int)Math.ceil(t1 /(1.0f/30.0f));
		float t3=(1.0f/60.0f);
		for(PhysicCar p:cars.values()){	
			p.updateCar();
		}
		dynamicsWorld.stepSimulation(t1,t2,t3);
		
	
	}

	public float[] getMatrixName(String name) {
		Matrix4 muu1 = new Matrix4();
		map.get(name).getMotionState().getWorldTransform(muu1);
		float[] r = muu1.getValues();
		muu1 = null;
		return r;
	}

	static btRigidBody localCreateRigidBody(float mass, btTransform startTransform,
			btCollisionShape shape, btDynamicsWorld world) {

		Vector3 localInertia = new Vector3(0, 0, 0);

		shape.calculateLocalInertia(mass, localInertia);

	
		btDefaultMotionState myMotionState = new btDefaultMotionState();
		myMotionState.setStartWorldTrans(startTransform);

		btRigidBodyConstructionInfo cInfo = new btRigidBodyConstructionInfo(
				mass, myMotionState, shape, localInertia);

		btRigidBody body = new btRigidBody(cInfo);

		world.addRigidBody(body);
		myMotionState.dispose();
		cInfo.release();
		myMotionState.release();
		return body;
	}

	
	public btDiscreteDynamicsWorld getWorld(){
		
		return this.dynamicsWorld;
	}
	
  static	public btCollisionShape getBoxCollisionShape(Vector3 size){
		if(!init){
			Bullet.init();
			init=true;
		}
		return  new btBoxShape(size);
	}
	
	
	
	
	public void AddVehicle(PhysicCar car,String name){
		cars.put(name, car);
		
	}
	
	public int getVehicleCount(){
		
		return cars.size();
	}
	public float[][] getVheicleWhells(String carName){
		return cars.get(carName).getWhellMatrix();
		
	}
	public float[] getWheicleChaiss(String carName){
		return cars.get(carName).getMatrixChassisCar();
		
	}
	public Vector3 getVheiclePosition(String carName){
		return cars.get(carName).getCarPosition();
		
		
	}
	public PhysicCar getVheicle(String carName){
		return cars.get(carName);
		
		
	}
	
	@Override
	public void finalize(){
		//Log.i("Delete","PhyisicWorld");
		for(btRigidBody b:map.values()){
			b.release();
			
		}
		dynamicsWorld.release();
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * int rightIndex = 0; int upIndex = 1; int forwardIndex = 2;
	 * 
	 * 
	 * 
	 * 
	 * ///btRaycastVehicle is the interface for the constraint that implements
	 * the raycast vehicle ///notice that for higher-quality slow-moving
	 * vehicles, another approach might be better ///implementing explicit
	 * hinged-wheel constraints with cylinder collision, rather then raycasts
	 * float gEngineForce = 0.f; float gBreakingForce = 0.f;
	 * 
	 * float maxEngineForce = 1000.f;//this should be engine/velocity dependent
	 * float maxBreakingForce = 100.f;
	 * 
	 * public float gVehicleSteering = 0.f; float steeringIncrement = 0.4f;
	 * float steeringClamp = 0.3f; float wheelRadius = 0.5f; float wheelWidth =
	 * 1.0f; float wheelFriction = 1000;//BT_LARGE_FLOAT; float
	 * suspensionStiffness = 20.f; float suspensionDamping = 2.3f; float
	 * suspensionCompression = 4.4f; float rollInfluence = 1.0f;//1.0f;
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * public void lallo(){
	 * 
	 * btTransform tr=new btTransform(); tr.setIdentity();
	 * 
	 * btCollisionShape chassisShape = new btBoxShape(new Vector3(1,0.5f,1));
	 * //m_collisionShapes.push_back(chassisShape);
	 * 
	 * btCompoundShape compound = new btCompoundShape();
	 * 
	 * //m_collisionShapes.push_back(compound); Matrix4 localTrans=new
	 * Matrix4(); localTrans.idt();
	 * 
	 * 
	 * compound.addChildShape(localTrans,chassisShape); compound.setMargin(0);
	 * 
	 * tr.setOrigin(new Vector3(0,4,0));
	 * 
	 * m_carChassis =
	 * localCreateRigidBody(800,tr,compound,dynamicsWorld);//chassisShape);
	 * //m_carChassis->setDamping(0.2,0.2);
	 * 
	 * 
	 * 
	 * //m_carChassis.serializeSingleObject(ase.); m_wheelShape = new
	 * btCylinderShapeX(new Vector3(0.5f,1,1)); m_tuning=new btVehicleTuning();
	 * 
	 * 
	 * /// create vehicle {
	 * 
	 * m_vehicleRayCaster = new btDefaultVehicleRaycaster(dynamicsWorld);
	 * m_vehicle = new
	 * btRaycastVehicle(m_tuning,m_carChassis,m_vehicleRayCaster);
	 * 
	 * ///never deactivate the vehicle
	 * m_carChassis.setActivationState(Collision.DISABLE_DEACTIVATION);
	 * 
	 * dynamicsWorld.addVehicle(m_vehicle);
	 * 
	 * float connectionHeight = 0.2f;
	 * 
	 * 
	 * boolean isFrontWheel=true;
	 * 
	 * //choose coordinate system
	 * m_vehicle.setCoordinateSystem(rightIndex,upIndex,forwardIndex);
	 * 
	 * 
	 * Vector3 connectionPointCS0 =new Vector3(0,connectionHeight,2);
	 * 
	 * 
	 * m_vehicle.addWheel(connectionPointCS0,new Vector3(0,-1,0),new
	 * Vector3(-1,0,0),0.6f,wheelRadius,m_tuning,isFrontWheel);
	 * 
	 * connectionPointCS0 = new Vector3(1,connectionHeight,2);
	 * 
	 * 
	 * //m_vehicle->addWheel(connectionPointCS0,wheelDirectionCS0,wheelAxleCS,
	 * suspensionRestLength,wheelRadius,m_tuning,isFrontWheel);
	 * 
	 * connectionPointCS0 = new Vector3(-1,connectionHeight,-2); isFrontWheel =
	 * false; m_vehicle.addWheel(connectionPointCS0,new Vector3(0,-1,0),new
	 * Vector3(-1,0,0),0.6f,wheelRadius,m_tuning,isFrontWheel);
	 * connectionPointCS0 = new Vector3(1,connectionHeight,-2);
	 * 
	 * m_vehicle.addWheel(connectionPointCS0,new Vector3(0,-1,0),new
	 * Vector3(-1,0,0),0.6f,wheelRadius,m_tuning,isFrontWheel);
	 * 
	 * for (int i=0;i<m_vehicle.getNumWheels();i++) { btWheelInfo wheel =
	 * m_vehicle.getWheelInfo(i); wheel.setSuspensionStiffness(
	 * suspensionStiffness);
	 * wheel.setWheelsDampingRelaxation(suspensionDamping);
	 * wheel.setWheelsDampingCompression(suspensionCompression);
	 * wheel.setFrictionSlip(wheelFriction);
	 * wheel.setRollInfluence(rollInfluence); } btDefaultMotionState
	 * myMotionState = new btDefaultMotionState(); btTransform t1=new
	 * btTransform(); localTrans=new Matrix4(); localTrans.idt();
	 * localTrans.setTranslation(new Vector3(0,0,-50));
	 * myMotionState.setWorldTransform(localTrans);
	 * m_carChassis.setMotionState(myMotionState);
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

}
