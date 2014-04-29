package Physic;


import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShapeX;
import com.badlogic.gdx.physics.bullet.dynamics.btDefaultVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleTuning;
import com.badlogic.gdx.physics.bullet.dynamics.btWheelInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;


public class PhysicCar {
	btVehicleTuning m_tuning;
	btVehicleRaycaster m_vehicleRayCaster;
	btRaycastVehicle m_vehicle;
	btCollisionShape m_wheelShape;
	btRigidBody m_carChassis;

	int rightIndex = 0;
	int upIndex = 1;
	int forwardIndex = 2;

	float gEngineForce = 950.f;
	float gBreakingForce =0.f;

	float maxEngineForce = 1000.f;// this should be engine/velocity dependent
	float maxBreakingForce = 1000.f;

	public float gVehicleSteering = 0.f;
	float steeringIncrement = 0.8f;
	float steeringClamp = 0.3f;
	float steeringMax = 1.3f;
	float wheelRadius = 0.5f;
	float wheelWidth = 1.0f;
	float wheelFriction = 1000;// BT_LARGE_FLOAT;
	float suspensionStiffness = 20.f;
	float suspensionDamping = 2.3f;
	float suspensionCompression = 4.4f;
	float rollInfluence = 0.4f;// 1.0f;

	
	public void createCar(btCollisionShape chassis, float mass, Vector3[] whellPosition,String name,String world) {

		btDiscreteDynamicsWorld dynamicsWorld=PhysicsWorld.instance(world).getWorld();
		btCompoundShape compound = new btCompoundShape();

		Matrix4 localTrans = new Matrix4();
		localTrans.idt();

		compound.addChildShape(localTrans, chassis);
		compound.setMargin(0);

		btTransform tr = new btTransform();
		tr.setIdentity();
		tr.setOrigin(new Vector3(0, 4, 0));
	//	tr.release();
		m_carChassis = localCreateRigidBody(mass, tr, compound, dynamicsWorld);// chassisShape);

		m_wheelShape = new btCylinderShapeX(new Vector3(wheelWidth,
				wheelRadius, wheelRadius));
		m_tuning = new btVehicleTuning();

		m_vehicleRayCaster = new btDefaultVehicleRaycaster(dynamicsWorld);
		m_vehicle = new btRaycastVehicle(m_tuning, m_carChassis,
				m_vehicleRayCaster);

		m_carChassis.setActivationState(Collision.DISABLE_DEACTIVATION);

		dynamicsWorld.addVehicle(m_vehicle);

		boolean isFrontWheel = true;

		m_vehicle.setCoordinateSystem(rightIndex, upIndex, forwardIndex);

		m_vehicle.addWheel(whellPosition[0], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), 0.6f, wheelRadius, m_tuning,
				isFrontWheel);

		isFrontWheel = false;
		m_vehicle.addWheel(whellPosition[1], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), 0.6f, wheelRadius, m_tuning,
				isFrontWheel);

		m_vehicle.addWheel(whellPosition[2], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), 0.6f, wheelRadius, m_tuning,
				isFrontWheel);

		for (int i = 0; i < m_vehicle.getNumWheels(); i++) {
			btWheelInfo wheel = m_vehicle.getWheelInfo(i);
			wheel.setSuspensionStiffness(suspensionStiffness);
			wheel.setWheelsDampingRelaxation(suspensionDamping);
			wheel.setWheelsDampingCompression(suspensionCompression);
			wheel.setFrictionSlip(wheelFriction);
			wheel.setRollInfluence(rollInfluence);
			wheel.release();
			
		}
		
		PhysicsWorld.instance(world).AddVehicle(this, name);
		
		
		compound.release();
		tr.release();
	
	}

	btRigidBody localCreateRigidBody(float mass, btTransform startTransform,
			btCollisionShape shape, btDynamicsWorld world) {

		Vector3 localInertia = new Vector3(0, 0, 0);

		shape.calculateLocalInertia(mass, localInertia);

		btDefaultMotionState myMotionState = new btDefaultMotionState();
		myMotionState.setStartWorldTrans(startTransform);

		btRigidBodyConstructionInfo cInfo = new btRigidBodyConstructionInfo(
				mass, myMotionState, shape, localInertia);

		btRigidBody body = new btRigidBody(cInfo);

		world.addRigidBody(body);
		myMotionState.release();
		 cInfo.release();
	
		return body;
	}

	public void updateCar() {
		
		m_carChassis.setAngularVelocity(new Vector3(0,m_carChassis.getAngularVelocity().y,0));
		float gEngineForce1;
		if(Math.abs(gVehicleSteering)>0.5){
			 gEngineForce1=gEngineForce+850;
		}else{
			
			 gEngineForce1=gEngineForce;
		}
		if(gEngineForce<0){
			gEngineForce1=gEngineForce*10;
		}
		int wheelIndex = 2;
		m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);
		m_vehicle.setSteeringValue(-gVehicleSteering, wheelIndex);
		m_vehicle.setBrake(gBreakingForce, wheelIndex);
		wheelIndex = 1;
		m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);
		m_vehicle.setBrake(gBreakingForce, wheelIndex);
		m_vehicle.setSteeringValue(-gVehicleSteering, wheelIndex);

		wheelIndex = 0;
		m_vehicle.setSteeringValue(gVehicleSteering, wheelIndex);
		m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);

		System.out.print(" "+m_carChassis.getLinearVelocity().z+'\n');
		
	}

	public float[][] getWhellMatrix() {
		float[][] whell = new float[m_vehicle.getNumWheels()][16];
		for (int i = 0; i < m_vehicle.getNumWheels(); i++) {

			m_vehicle.updateWheelTransform(i, true);

			m_vehicle.getWheelInfo(i).getWorldTransform()
					.getOpenGLMatrix(whell[i]);

		}
		return whell;

	}

	public float[] getMatrixChassisCar(){
		Matrix4 worldTr=new Matrix4();
		m_carChassis.getMotionState().getWorldTransform(worldTr);
		return worldTr.getValues();
	}
	
	public void setCarPosition(Vector3 pos) {
		btDefaultMotionState myMotionState = new btDefaultMotionState();
		Matrix4 localTrans = new Matrix4();
		localTrans.idt();
		localTrans.setTranslation(pos);
		myMotionState.setWorldTransform(localTrans);
		m_carChassis.setMotionState(myMotionState);
		
		localTrans=null;
		myMotionState.release();
		
	}
	public Vector3 getCarPosition(){
		Matrix4 worldTrans=new Matrix4();
		m_carChassis.getMotionState().getWorldTransform(worldTrans);
		Vector3 pos=new Vector3();
		return worldTrans.getTranslation(pos);
		
	}
	public void RightSteering(){
		
		if(Math.abs(gVehicleSteering+steeringIncrement)<steeringMax){
			
			gVehicleSteering+=steeringIncrement;
		}
	}
	public void LeftSteering(){
		
		if(Math.abs(gVehicleSteering-steeringIncrement)<steeringMax){
			
			gVehicleSteering-=steeringIncrement;
		}
	}

	public void SetEngineForce(float force){
		gEngineForce=force;
		
	}
	public float GetEngineForce(){
		return gEngineForce;
		
	}

	public void serialize(){

		
	}
	
	public void SetSteering(float f){
		gVehicleSteering=f;
		
	}
	
	@Override
	public void finalize(){
		//Log.i("Delete", "Car");
		 m_tuning.release();;
		 m_vehicleRayCaster.release();
		 m_vehicle.release();
		 m_wheelShape.release();
		 m_carChassis.release();
		
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
