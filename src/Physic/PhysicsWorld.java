package Physic;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

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
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btShapeHull;
import com.badlogic.gdx.physics.bullet.collision.btStaticPlaneShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMesh;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.physics.bullet.linearmath.btTransform;

public class PhysicsWorld {
	private static HashMap<String, PhysicsWorld> physicsWorld = new HashMap<String, PhysicsWorld>();
	private btDiscreteDynamicsWorld dynamicsWorld;

	private PhysicsWorld() {

		Bullet.init();
		map = new HashMap<String, btRigidBody>();
		cars = new HashMap<String, PhysicCar>();

		btDefaultCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		btCollisionDispatcher dispatcher = new btCollisionDispatcher(
				collisionConfiguration);
		btDbvtBroadphase broadphase = new btDbvtBroadphase();
		btSequentialImpulseConstraintSolver solver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase,
				solver, collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3(0, -9.8f, 0));

	}

	Map<String, btRigidBody> map;
	Map<String, PhysicCar> cars;

	public void addBox(Vector3 position, String name, Vector3 size, float mass) {
		long heapSize = Runtime.getRuntime().totalMemory();
		Log.i("heap", this.getClass().getName() + " addBox " + heapSize);
		Bullet.init();
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
		heapSize = Runtime.getRuntime().totalMemory();
		Log.i("heap", this.getClass().getName() + "  " + heapSize);
		fallShape.releaseOwnership();
		fallMotionState.releaseOwnership();
		fallRigidBodyCI.releaseOwnership();
		as.releaseOwnership();

	}

	btTriangleMesh editTriangleMeshes(Vector3 arrayVector[]) {
		btTriangleMesh mTriMesh = new btTriangleMesh();

		for (int i = 0; i < arrayVector.length; i += 3) {
			mTriMesh.addTriangle(arrayVector[i + 0], arrayVector[i + 1],
					arrayVector[i + 2]);

		}
		return mTriMesh;
	}

	public void addMeshCollider(Vector3[] vertices, Vector3 position,
			Quaternion rotation, float mass, String name) {

		btTriangleMesh mTriMesh = editTriangleMeshes(vertices);

		btCollisionShape mTriMeshShape = new btBvhTriangleMeshShape(mTriMesh,
				true);

		/*
		 * btConvexShape tmpshape = new btConvexTriangleMeshShape(mTriMesh);
		 * btShapeHull hull = new btShapeHull(tmpshape); float margin =
		 * mTriMeshShape.getMargin(); hull.buildHull(margin);
		 * mTriMeshShape.setUserPointer(hull.getCPointer());
		 */
		btDefaultMotionState fallMotionStateTriangle = new btDefaultMotionState();
		fallMotionStateTriangle.setStartWorldTrans(new btTransform(rotation,
				position));

		Vector3 fallInertiaT = new Vector3(0, 1, 0);
		mTriMeshShape.calculateLocalInertia(mass, fallInertiaT);

		btRigidBodyConstructionInfo fallRigidBodyCIT = new btRigidBodyConstructionInfo(
				mass, fallMotionStateTriangle, mTriMeshShape, fallInertiaT);

		btRigidBody myNewBody = new btRigidBody(fallRigidBodyCIT);
		dynamicsWorld.addRigidBody(myNewBody);
		map.put(name, myNewBody);

		mTriMesh.release();
		mTriMeshShape.release();
		fallRigidBodyCIT.release();
		fallInertiaT = null;
		// System.gc();

	}

	public static PhysicsWorld instance(String worldName) {
		if (!physicsWorld.containsKey(worldName)) {
			physicsWorld.put(worldName, new PhysicsWorld());

		}
		return physicsWorld.get(worldName);

	}

	public void update() {

		
		dynamicsWorld.stepSimulation(1.0f / 30.0f, 5, 1.0f / 60.0f);
		

		for (PhysicCar p : cars.values()) {
			p.updateCar();
		}
		// System.gc();
		dynamicsWorld.updateVehicles(1f / 30.0f);
	

	}

	public void update(float t1) {

		int t2 = 4 * (int) Math.ceil(t1 / (1.0f / 30.0f));
		float t3 = (1.0f / 60.0f);
		for (PhysicCar p : cars.values()) {
			// p.updateCar();
		}
		dynamicsWorld.stepSimulation(t1, t2, t3);

	}

	public float[] getMatrixName(String name) {
		Matrix4 muu1 = new Matrix4();
		map.get(name).getMotionState().getWorldTransform(muu1);
		float[] r = muu1.getValues();
		muu1 = null;
		return r;
	}

	static btRigidBody localCreateRigidBody(float mass,
			btTransform startTransform, btCollisionShape shape,
			btDynamicsWorld world) {

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
		return cars.get(carName).getCarPosition();

	}

	public PhysicCar getVheicle(String carName) {
		return cars.get(carName);

	}

	public int getVehicleCount() {

		return cars.size();
	}

	@Override
	public void finalize() {
		Log.i("Delete", "PhyisicWorld");
		for (btRigidBody b : map.values()) {
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
}