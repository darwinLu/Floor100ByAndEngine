package pers.lx.floor100byandengine.object;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public abstract class Player extends AnimatedSprite {

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    public Body body;
    public IAreaShape shape;

    private GameScene.Direction direction = GameScene.Direction.DIRECTION_RIGHT;

    public boolean onGround = false;
    public boolean jumping;
    public boolean running = false;

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld, GameScene gameScene)
    {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbom);
        createPhysics(camera, physicsWorld,vbom,gameScene);
//        camera.setChaseEntity(this);
    }

//    public Player(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld)
//    {
//        super(pX, pY, ResourcesManager.getInstance().player_region, vbom);
//        createPhysics(camera, physicsWorld,vbom);
//    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld,VertexBufferObjectManager vbom,final GameScene gameScene){
        shape = new Rectangle(mX, mY,50,50,vbom);
        shape.setColor(Color.RED);
        body = PhysicsFactory.createBoxBody(physicsWorld,shape, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        body.setFixedRotation(true);
        body.setUserData("player");
        PhysicsConnector rectConnector = new PhysicsConnector(shape,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(shape.getY() + shape.getHeight()/2 < gameScene.headLinePixel){
                    camera.setCenter(camera.getCenterX(),shape.getY() + shape.getHeight()/2 );
                    gameScene.headLinePixel = shape.getY() + shape.getHeight()/2;
                    gameScene.deadLinePixel = shape.getY() + shape.getHeight()/2 + CAMERA_HEIGHT/2 + 50;
                }
                if(shape.getY() > gameScene.deadLinePixel){
                    gameOver();
                }
            }
        };
        physicsWorld.registerPhysicsConnector(rectConnector);
        //注册碰撞检测
        physicsWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                String idA = (String)bodyA.getUserData();
                String idB = (String)bodyB.getUserData();
                if(idA.equals("player") && idB.equals("floor")){
                    onGround = true;
                    if(!running){
                        body.applyLinearImpulse(new Vector2(10,0),body.getWorldCenter());
                        running = true;
                    }
                }
                if(idA.equals("player") && idB.equals("right")){
                    direction = GameScene.Direction.DIRECTION_LEFT;
                    body.setLinearVelocity(new Vector2(0,body.getLinearVelocity().y));
                    body.applyLinearImpulse(new Vector2(-10,0),body.getWorldCenter());
                }
                if(idA.equals("player") && idB.equals("left")) {
                    direction = GameScene.Direction.DIRECTION_RIGHT;
                    body.setLinearVelocity(new Vector2(0,body.getLinearVelocity().y));
                    body.applyLinearImpulse(new Vector2(10,0),body.getWorldCenter());
                }
            }

            //实现单向平台
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                String idA = (String)bodyA.getUserData();
                String idB = (String)bodyB.getUserData();
                if(idA.equals("player") && idB.equals("platform")){
                    if(shape.getY() + shape.getHeight()  > ){
                        contact.setEnabled(false);
                    }
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }

            @Override
            public void endContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                String idA = (String)bodyA.getUserData();
                String idB = (String)bodyB.getUserData();
                if(idA.equals("player") && idB.equals("floor")){
                    shape.setColor(Color.WHITE);
                }
            }
        });
    }

    public abstract void gameOver();

    public void jump(){
        body.applyLinearImpulse(new Vector2(0,-40),body.getLocalCenter());
    }

}
