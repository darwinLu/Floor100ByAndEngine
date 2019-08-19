package pers.lx.floor100byandengine.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.Random;

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class Platform extends Entity {

    public IAreaShape shape;
    public Body body;
    public String type = "";
    private Color color;

    private String lrDirection = "right";
    private String udDirection = "up";

    public Platform(String typeString, Random rand, int y, VertexBufferObjectManager vbom, PhysicsWorld physicsWorld,Camera camera,GameScene gameScene){
        createPhysics(typeString, rand, y,camera, physicsWorld,vbom,gameScene);

    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(String typeString, Random rand, int y,final Camera camera, PhysicsWorld physicsWorld, VertexBufferObjectManager vbom, final GameScene gameScene){
        //super(CAMERA_WIDTH / 10 * rand.nextInt(10),y,160,30, ResourcesManager.getInstance().platform_region,vbom);
        shape = new Rectangle(CAMERA_WIDTH / 10 * rand.nextInt(10),y,160,30,vbom);
        body = PhysicsFactory.createBoxBody(physicsWorld,shape, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
        body.setUserData(new UserData("platform",this));
        type = typeString;
        if(type.equals("lrPlatform")){
            shape.setColor(Color.GREEN);
        }
        if(type.equals("udPlatform")){
            shape.setColor(Color.PINK);
        }
        shape.setUserData(body);
        PhysicsConnector platformConnector = new PhysicsConnector(shape,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                String flag;
                if(type.equals("lrPlatform")){
                    //左右平台移动
                    flag = "lr";
                    if(lrDirection.equals("right")){
                        body.setLinearVelocity(new Vector2(5,0));
                    }
                    if(lrDirection.equals("left")){
                        body.setLinearVelocity(new Vector2(-5,0));
                    }
                    if(body.getWorldCenter().x>15){
                        lrDirection = "left";
                    }
                    if(body.getWorldCenter().x<0){
                        lrDirection = "right";
                    }
                }
                if(type.equals("udPlatform")){
                    //上下平台移动
                    flag = "ud";
                    if(udDirection.equals("up")){
                        body.setLinearVelocity(new Vector2(0,-1));
                    }
                    if(udDirection.equals("down")){
                        body.setLinearVelocity(new Vector2(0,1));
                    }
                    if(body.getWorldCenter().y > 20){
                        udDirection = "up";
                    }
                    if(body.getWorldCenter().y <10){
                        udDirection = "down";
                    }
                }

            }
        };
        physicsWorld.registerPhysicsConnector(platformConnector);
    }
//    @Override
//    protected void onManagedUpdate(float pSecondsElapsed) {
//        super.onManagedUpdate(pSecondsElapsed);
//        if(this.type.equals("lrPlatform")){
//            //左右平台移动
//            if(lrDirection.equals("right")){
//                body.setLinearVelocity(new Vector2(5,0));
//            }
//            if(lrDirection.equals("left")){
//                body.setLinearVelocity(new Vector2(-5,0));
//            }
//            if(body.getWorldCenter().x>15){
//                lrDirection = "left";
//            }
//            if(body.getWorldCenter().x<0){
//                lrDirection = "right";
//            }
//        }
//
//    }
}
