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

import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class SpringPlatform extends Platform {

    public SpringPlatform(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        super(x, y, physicsWorld, camera, gameScene, vbom);
        platformType = "spring";
    }

    @Override
    protected void createPhysics(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        shape = new Rectangle(x,y,width,height,vbom);
        body = PhysicsFactory.createBoxBody(physicsWorld,shape, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
        body.setUserData(new UserData(entityType,this));
        shape.setColor(Color.CYAN);
        shape.setUserData(body);
        PhysicsConnector platformConnector = new PhysicsConnector(shape,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
//                if(direction.equals("up")){
//                    body.setLinearVelocity(new Vector2(0,-1));
//                }
//                if(direction.equals("down")){
//                    body.setLinearVelocity(new Vector2(0,1));
//                }
//                if(body.getWorldCenter().y > 20){
//                    direction = "up";
//                }
//                if(body.getWorldCenter().y <10) {
//                    direction = "down";
//                }
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
