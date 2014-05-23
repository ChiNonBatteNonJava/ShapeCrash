package Physic;




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

	float gEngineForce = 811f;
	float gBreakingForce =20.f;

	float maxEngineForce = 1000.f;// this should be engine/velocity dependent
	float maxBreakingForce = 1000.f;

	public float gVehicleSteering = 0.f;
	float steeringIncrement = 0.26f;
	float steeringClamp = 0.3f;

	public static final float steeringMax = 0.26f; //1.3ff;

	float wheelRadius = 2.0f;
	float wheelWidth = 2.0f;
	float wheelFriction = 1000;// BT_LARGE_FLOAT;
	float suspensionStiffness = 20.f;
	float suspensionDamping = 6.3f;
	float suspensionCompression = 7.4f;
	float suspensionRes=1.0f;
	float rollInfluence = 0.0f;// 1.0f;




	boolean retro=false;

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
      
		m_carChassis = localCreateRigidBody(mass, tr, compound, dynamicsWorld,(short)2);// chassisShape);


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
		
		m_vehicle.addWheel(whellPosition[1], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), suspensionRes, wheelRadius, m_tuning,
				isFrontWheel);

		isFrontWheel = false;
		m_vehicle.addWheel(whellPosition[2], new Vector3(0, -1, 0),
				new Vector3(-1, 0, 0), suspensionRes, wheelRadius, m_tuning,
				isFrontWheel);

		m_vehicle.addWheel(whellPosition[3], new Vector3(0, -1, 0),
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

		
		btCylinderShapeX bt=new btCylinderShapeX(new Vector3(1.5f,2.5f,1.5f));
		tr.setIdentity();
		tr.setOrigin(whellPosition[0]);
		
		PhysicsWorld.instance(world).getWorld().addRigidBody(localCreateRigidBody(10,tr,bt,PhysicsWorld.instance(world).getWorld(),(short)2));
		
		tr.setOrigin(whellPosition[1]);
		PhysicsWorld.instance(world).getWorld().addRigidBody(localCreateRigidBody(10,tr,bt,PhysicsWorld.instance(world).getWorld(),(short)2));
		
		tr.setOrigin(whellPosition[2]);
		PhysicsWorld.instance(world).getWorld().addRigidBody(localCreateRigidBody(10,tr,bt,PhysicsWorld.instance(world).getWorld(),(short)2));

		
		tr.setOrigin(whellPosition[3]);
		PhysicsWorld.instance(world).getWorld().addRigidBody(localCreateRigidBody(10,tr,bt,PhysicsWorld.instance(world).getWorld(),(short)2));

		PhysicsWorld.instance(world).AddVehicle(this, name);

	}

	btRigidBody localCreateRigidBody(float mass, Transform startTransform,
			btCollisionShape shape, btDynamicsWorld world,short group) {

		Vector3 localInertia = new Vector3(0, 0, 0);
		shape.calculateLocalInertia(mass, localInertia);
		DefaultMotionState myMotionState = new DefaultMotionState();
		myMotionState.setStartWorldTrans(startTransform);
		RigidBodyConstructionInfo cInfo = new RigidBodyConstructionInfo(mass, myMotionState, shape, localInertia);
		cInfo.setRestitution(.3f);
		cInfo.setLinearDamping(0);
		cInfo.setAngularDamping(0);
		btRigidBody body = new btRigidBody(cInfo);
		world.addRigidBody(body,(short)1,(short)3);
		myMotionState.del();
		return body;
	}

	public void updateCar() {

		m_vehicle.resetSuspension();
		m_carChassis.setAngularFactor(new Vector3(0.5f,1,0.5f));
		float v1=m_carChassis.getAngularVelocity().x;
		float v2=m_carChassis.getAngularVelocity().z;
		if(Math.abs(v1)>0.8f){
			v1=0;
		}
		if(Math.abs(v2)>0.8f){
			v2=0;
		}

		m_carChassis.setAngularVelocity(new Vector3(v1,m_carChassis.getAngularVelocity().y,v2));


		float gEngineForce1;
		if(Math.abs(gVehicleSteering)>0.4){
			 gEngineForce1=gEngineForce+400; 
		}else{
			 gEngineForce1=gEngineForce;
		}
		if(m_vehicle.getCurrentSpeedKmHour()<150){
			gEngineForce1=gEngineForce+400;
		}else if(m_carChassis.getLinearVelocity().dot(m_carChassis.getLinearVelocity())>=200){
			gEngineForce1=1;
		}

		if(retro==true){
			gEngineForce1=-100000;
		}

		int wheelIndex = 3;
		m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);
		m_vehicle.setSteeringValue(-gVehicleSteering/3.5f, wheelIndex);
		m_vehicle.setBrake(gBreakingForce, wheelIndex);
		wheelIndex = 2;
		m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);
		m_vehicle.setBrake(gBreakingForce, wheelIndex);
		m_vehicle.setSteeringValue(-gVehicleSteering/3.5f, wheelIndex);
		wheelIndex = 0;
		m_vehicle.setSteeringValue(gVehicleSteering, wheelIndex);
		wheelIndex = 1;
		m_vehicle.setSteeringValue(gVehicleSteering, wheelIndex);
		wheelIndex = 1;
		m_vehicle.setSteeringValue(gVehicleSteering, wheelIndex);
		
		
		
		
	//	m_carChassis.setAngularVelocity(new Vector3(0,m_carChassis.getAngularVelocity().y,0));
		//m_vehicle.applyEngineForce(gEngineForce1, wheelIndex);	
	}

	public float[][] getWhellMatrix() {

		float[][] whell = new float[m_vehicle.getNumWheels()][16];
		for (int i = 0; i < m_vehicle.getNumWheels(); i++) {
			m_vehicle.updateWheelTransform(i, true);
			m_vehicle.getWheelInfo(i).getWorldTransform().getOpenGLMatrix(whell[i]);
		}
		return whell;
	}
	public void doRetro(){
		retro=!retro;
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

	public void setCarPositionOrientation(Vector3 pos, Quaternion ori){
		
		DefaultMotionState myMotionState = new DefaultMotionState();
		Matrix4 localTrans = new Matrix4();
		localTrans.idt();
		localTrans.set(pos, ori, new Vector3(1,1,1));
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
		gVehicleSteering= steeringMax;
	}

	public void LeftSteering(){
		gVehicleSteering= -steeringMax;
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

	public void setStatus(PhysicCarStatus carStatus){

		gVehicleSteering=carStatus.steering;
		setCarPositionOrientation(carStatus.position,carStatus.orientation);
		m_carChassis.setLinearVelocity(carStatus.linearVelocity);
		m_carChassis.setAngularVelocity(carStatus.angularVelocity);

	}

	public PhysicCarStatus getStatus(){

		PhysicCarStatus helper=new PhysicCarStatus();
		helper.steering=gVehicleSteering;
		helper.position=this.getCarPosition();
		helper.linearVelocity=m_carChassis.getLinearVelocity();
		helper.angularVelocity=m_carChassis.getAngularVelocity();
		helper.orientation = m_carChassis.getOrientation();
		return helper;
	}
	
	public float getSpeedKMH(){
		
		return m_vehicle.getCurrentSpeedKmHour();
	}

	@Override
	public void finalize(){
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
	
	public int getRetro(){
		if(retro){
			return -1;
		}else{
			return 1;
		}
			
	}
}
