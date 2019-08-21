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

public class LrPlatform extends Platform {

    public LrPlatform(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        super( x, y, physicsWorld, camera, gameScene, vbom);
        platformType = "lr";
        direction = "right";
    }

    @Override
    protected void createPhysics(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        shape = new Rectangle(x,y,width,height,vbom);
        body = PhysicsFactory.createBoxBody(physicsWorld,shape, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
        body.setUserData(new UserData(entityType,this));
        shape.setColor(Color.GREEN);
        shape.setUserData(body);
        PhysicsConnector platformConnector = new PhysicsConnector(shape,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(direction.equals("right")){
                    body.setLinearVelocity(new Vector2(5,0));
                }
                if(direction.equals("left")){
                    body.setLinearVelocity(new Vector2(-5,0));
                }
                if(body.getWorldCenter().x>15){
                    direction = "left";
                }
                if(body.getWorldCenter().x<0){
                    direction = "right";
                }
            }
        };
        physicsWorld.registerPhysicsConnector(platformConnector);
    }

    @Override
    protected void doEffectToPlayer(Player player) {
        if (!player.speedChanged) {
            if (direction.equals("left")) {
                player.body.applyLinearImpulse(new Vector2(30, 0), player.body.getWorldCenter());
            } else {
                player.body.applyLinearImpulse(new Vector2(-30, 0), player.body.getWorldCenter());
            }
            player.speedChanged = true;
        }
    }

    @Override
    protected void clearEffectToPlayer(Player player) {
        if(direction.equals("left")){
            player.body.applyLinearImpulse(new Vector2(-30,0),player.body.getWorldCenter());
        }
        else{
            player.body.applyLinearImpulse(new Vector2(30,0),player.body.getWorldCenter());
        }
    }
}
