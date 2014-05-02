package Physic;


import android.util.Log;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShapeX;
import com.badlogic.gdx.physics.bullet.dynamics.btDefaultVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRaycastVehicle;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleRaycaster;
import com.badlogic.gdx.physics.bullet.dynamics.btVehicleTuning;
import com.badlogic.gdx.physics.bullet.dynamics.btWheelInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;




public class PhysicCar {

	btVehicleTuning m_tuning;
	btVehicleRaycaster m_vehicleRayCaster;
	btRaycastVehicle m_vehicle;
	btCollisionShape m_wheelShape;
	btRigidBody m_carChassis;

	int rightIndex = 0;
	int upIndex = 1;
	int forwardIndex = 2;

	float gEngineForce = 850f;
	float gBreakingForce =20.f;

	float maxEngineForce = 1000.f;// this should be engine/velocity dependent
	float maxBreakingForce = 1000.f;

	public float gVehicleSteering = 0.f;
	float steeringIncrement = 0.56f;
	float steeringClamp = 0.3f;
	float steeringMax = 1.3f;
	float wheelRadius = 2.0f;
	float wheelWidth = 2.0f;
	float wheelFriction = 1000;// BT_LARGE_FLOAT;
	float suspensionStiffness = 200.f;
	float suspensionDamping = 100.3f;
	float suspensionCompression = 2.4f;
	float suspensionRes=1.0f;
	float rollInfluence = 0.0f;// 1.0f;

	
	public void createCar(btCollisionShape chassis, float mass, Vector3[] whellPosition,String name,String world) {

		btDiscreteDynamicsWorld dynamicsWorld=PhysicsWorld.instance(world).getWorld();
		btCompoundShape compound = new btCompoundShape();

		Matrix4 localTrans = new Matrix4();
		localTrans.idt();

		compound.addChildShape(localTrans, chassis);
		compound.setMargin(0);


		Transform tr = new Transform();
		tr.setIdentity();
		tr.setOrigin(new Vector3(0, 0, 0));
      
		m_carChassis = localCreateRigidBody(mass, tr, compound, dynamicsWorld);// chassisShape);

	
		m_tuning = new btVehicleTuning();

		m_vehicleRayCaster = new btDefaultVehicleRaycaster(dynamicsWorld);
		m_vehicle = new btRaycastVehicle(m_tuning, m_carChassis,
				m_vehicleRayCaster);

		m_carChassis.setActivationState(Collision.DISABLE_DEACTIVATION);

		dynamicsWorld.addVehicle(m_vehicle);

		boolean isFrontWheel = true;

		m_vehicle.setCoordinateSystem(rightIndex, upIndex, forwardIndex);

		m_vehicle.addWheel(whellPosition[0], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), suspensionRes, wheelRadius, m_tuning,
				isFrontWheel);

		isFrontWheel = false;
		m_vehicle.addWheel(whellPosition[1], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), suspensionRes, wheelRadius, m_tuning,
				isFrontWheel);

		m_vehicle.addWheel(whellPosition[2], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), suspensionRes, wheelRadius, m_tuning,
				isFrontWheel);

	
		
		for (int i = 0; i < m_vehicle.getNumWheels(); i++) {
			btWheelInfo wheel = m_vehicle.getWheelInfo(i);
			wheel.setSuspensionStiffness(suspensionStiffness);
			wheel.setWheelsDampingRelaxation(suspensionDamping);
			wheel.setWheelsDampingCompression(suspensionCompression);
			wheel.setFrictionSlip(wheelFriction);
			wheel.setRollInfluence(rollInfluence);	
		}
		
		PhysicsWorld.instance(world).AddVehicle(this, name);

		
	
	}

	btRigidBody localCreateRigidBody(float mass, Transform startTransform,
			btCollisionShape shape, btDynamicsWorld world) {

		Vector3 localInertia = new Vector3(0, 0, 0);

		shape.calculateLocalInertia(mass, localInertia);

		DefaultMotionState myMotionState = new DefaultMotionState();
		myMotionState.setStartWorldTrans(startTransform);

		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(
				mass, myMotionState, shape, localInertia);
		//cInfo.setFriction(100);
		cInfo.setRestitution(1.0f);
		cInfo.setLinearDamping(0);
		cInfo.setAngularDamping(0);
		
		btRigidBody body = new btRigidBody(cInfo);

		world.addRigidBody(body);
		myMotionState.del();
	//	 cInfo.dispose();
	//	 startTransform.dispose();
		return body;
	}


	public void updateCar() {
		
		
		m_vehicle.resetSuspension();
		m_carChassis.setAngularVelocity(new Vector3(0,m_carChassis.getAngularVelocity().y,0));
		m_carChassis.setAngularFactor(new Vector3(0.5f,1,0.5f));
		
		Log.i("speed",""+m_carChassis.getLinearVelocity().dot(m_carChassis.getLinearVelocity()));
		float gEngineForce1;
		if(Math.abs(gVehicleSteering)>0.5){
			 gEngineForce1=gEngineForce; 
		}else{
			
			 gEngineForce1=gEngineForce;
		}
		if(m_carChassis.getLinearVelocity().dot(m_carChassis.getLinearVelocity())<150){
			gEngineForce1=gEngineForce+1400;
		}else if(m_carChassis.getLinearVelocity().dot(m_carChassis.getLinearVelocity())>=500){
			gEngineForce1=1;
		}
		int wheelIndex = 2;
		m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);
		//m_vehicle.setSteeringValue(-gVehicleSteering/2, wheelIndex);
		m_vehicle.setBrake(gBreakingForce, wheelIndex);
		wheelIndex = 1;
		m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);
		m_vehicle.setBrake(gBreakingForce, wheelIndex);
		//m_vehicle.setSteeringValue(-gVehicleSteering/2, wheelIndex);

		wheelIndex = 0;
		m_vehicle.setSteeringValue(gVehicleSteering, wheelIndex);
		m_carChassis.setAngularVelocity(new Vector3(0,m_carChassis.getAngularVelocity().y,0));
		//m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);

		//System.out.print(" "+m_carChassis.getLinearVelocity().z+'\n');
		
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
		btMotionState myMotion=m_carChassis.getMotionState();
		myMotion.getWorldTransform(worldTr);
		myMotion.release();
		myMotion=null;
		return worldTr.getValues();
	}
	
	public void setCarPosition(Vector3 pos) {
		DefaultMotionState myMotionState = new DefaultMotionState();
		Matrix4 localTrans = new Matrix4();
		localTrans.idt();
		localTrans.setTranslation(pos);
		myMotionState.setWorldTransform(localTrans);
		m_carChassis.setMotionState(myMotionState);
		
		localTrans=null;
		myMotionState.del();
		
	}
	
	public Vector3 getCarPosition(){
		Matrix4 worldTrans=new Matrix4();
		btMotionState myMotion=m_carChassis.getMotionState();
		myMotion.getWorldTransform(worldTrans);
		Vector3 pos=new Vector3();
		pos=worldTrans.getTranslation(pos);
		return pos;
		
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

	public Vector3 getVectorForward(){
		return m_vehicle.getForwardVector();
	}
	
	
	
	
	public void SetSteering(float f){
		gVehicleSteering=f;
		
	}
	
	public float GetSteering(){
		return gVehicleSteering;
		
	}
	
	public btWheelInfo getWhellInfo(int index){
		return m_vehicle.getWheelInfo(index);	
	}
	public void setWhellInfo(btWheelInfo info,int index){
		btWheelInfo wheel = m_vehicle.getWheelInfo(index);
		wheel=info;
		
	}
	
	
	@Override
	public void finalize(){
		Log.i("Delete", "C1ar");
		 m_tuning.dispose();;
		 m_vehicleRayCaster.dispose();
		 m_vehicle.dispose();
		 m_wheelShape.dispose();
		 m_carChassis.dispose();
		
		try {
			super.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
