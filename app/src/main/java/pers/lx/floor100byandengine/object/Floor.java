package pers.lx.floor100byandengine.object;

import android.graphics.Rect;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.manager.ResourcesManager;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class Floor {

    private Body body;
    public  IAreaShape shape;

    public Floor(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld)
    {
        createPhysics(camera, physicsWorld,vbom);
//        camera.setChaseEntity(this);
    }

    private void createPhysics(Camera camera, PhysicsWorld physicsWorld, VertexBufferObjectManager vbom) {
        shape = new Rectangle(0,CAMERA_HEIGHT - 100,CAMERA_WIDTH,100,vbom);
        shape.setColor(Color.GREEN);
        body = PhysicsFactory.createBoxBody(physicsWorld,shape, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
//        PhysicsConnector rectConnector = new PhysicsConnector(rect,rectBody,true,true);
//        this.mPhysicsWorld.registerPhysicsConnector(rectConnector);
        body.setUserData(new UserData("floor",this));
    }
}
