package pers.lx.floor100byandengine.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class NormalPlatform extends Platform {

    public NormalPlatform(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        super( x, y, physicsWorld, camera, gameScene, vbom);
        platformType = "normal";
    }

    @Override
    protected void createPhysics(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        shape = new Rectangle(x,y,width,height,vbom);
        body = PhysicsFactory.createBoxBody(physicsWorld,shape, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
        body.setUserData(new UserData(entityType,this));
        shape.setColor(Color.YELLOW);
        shape.setUserData(body);
        PhysicsConnector platformConnector = new PhysicsConnector(shape,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
            }
        };
        physicsWorld.registerPhysicsConnector(platformConnector);
    }

    @Override
    protected void doEffectToPlayer(Player player) {

    }

    @Override
    protected void clearEffectToPlayer(Player player) {

    }
}
