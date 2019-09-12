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

import java.util.Random;

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class RollPlatform extends Platform {

    public boolean animating = false;
    private GameScene.Direction direction;
    private Random rand = new Random();

    public RollPlatform(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        super(x, y, ResourcesManager.getInstance().roll_platform_region,physicsWorld, camera, gameScene, vbom);
        platformType = "roll";
        if(rand.nextInt(1) == 0){
            direction = GameScene.Direction.DIRECTION_LEFT;
        }
        else {
            direction = GameScene.Direction.DIRECTION_RIGHT;
        }
    }

    @Override
    protected void createPhysics(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
//        shape = new Rectangle(x,y,width,height,vbom);
        body = PhysicsFactory.createBoxBody(physicsWorld,this, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
        body.setUserData(new UserData(entityType,this));
//        shape.setColor(Color.RED);
        this.setUserData(body);
        PhysicsConnector platformConnector = new PhysicsConnector(this,body,true,true){
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
        if (!player.speedChanged) {
            if (direction == GameScene.Direction.DIRECTION_LEFT) {
                player.body.applyLinearImpulse(new Vector2(100, 0), player.body.getWorldCenter());
            } else {
                player.body.applyLinearImpulse(new Vector2(-100, 0), player.body.getWorldCenter());
            }
            player.speedChanged = true;
        }
    }

    @Override
    protected void clearEffectToPlayer(Player player) {
        if(direction == GameScene.Direction.DIRECTION_LEFT){
            player.body.applyLinearImpulse(new Vector2(-100,0),player.body.getWorldCenter());
        }
        else{
            player.body.applyLinearImpulse(new Vector2(100,0),player.body.getWorldCenter());
        }
    }

    public void setAnimating(){
        animating = true;
        final long[] ROLL_ANIMATE = new long[]{100,100,100,100,100,100,100,100};
        animate(ROLL_ANIMATE,0,7,true);
        if (direction == GameScene.Direction.DIRECTION_RIGHT) {
            setFlippedHorizontal(true);
        }
    }
}
