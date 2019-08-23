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

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class UdPlatform extends Platform {

    public UdPlatform(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        super( x, y, ResourcesManager.getInstance().normal_lr_ud_platform_region,physicsWorld, camera, gameScene, vbom);
        platformType = "ud";
        direction = "up";
    }

    @Override
    protected void createPhysics(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
//        shape = new Rectangle(x,y,width,height,vbom);
        body = PhysicsFactory.createBoxBody(physicsWorld,this, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
        body.setUserData(new UserData(entityType,this));
//        shape.setColor(Color.PINK);
        this.setUserData(body);
        PhysicsConnector platformConnector = new PhysicsConnector(this,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(direction.equals("up")){
                    body.setLinearVelocity(new Vector2(0,-1));
                }
                if(direction.equals("down")){
                    body.setLinearVelocity(new Vector2(0,1));
                }
                if(body.getWorldCenter().y > 20){
                    direction = "up";
                }
                if(body.getWorldCenter().y <10) {
                    direction = "down";
                }
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
