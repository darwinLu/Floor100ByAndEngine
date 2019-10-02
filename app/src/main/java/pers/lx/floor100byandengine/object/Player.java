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
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public abstract class Player extends AnimatedSprite {

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    public Body body;
    public IAreaShape shape;

    public boolean onGround = false;
    public boolean jumping = false;
    public boolean running = false;
    public boolean animating = false;
    public boolean speedChanged = false;
    public boolean onPlatform = false;
    public boolean pressJumping = false;

    public GameScene.Direction direction;

    private Platform currentPlatform;

//    public boolean needToChange = false;

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld, GameScene gameScene)
    {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbom);
        createPhysics(camera, physicsWorld,vbom,gameScene);
//        camera.setChaseEntity(this);
        setAnimating();
        setZIndex(1);
    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld, VertexBufferObjectManager vbom, final GameScene gameScene){
        body = PhysicsFactory.createBoxBody(physicsWorld,this, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        body.setFixedRotation(true);
        body.setUserData(new UserData("player",this));
        //注册连接器
        PhysicsConnector playerConnector = new PhysicsConnector(this,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(this.getShape().getY() + getTiledTextureRegion().getHeight(0)/2 < gameScene.headLinePixel){
                    camera.setCenter(camera.getCenterX(),this.getShape().getY() + getTiledTextureRegion().getHeight(0)/2 );
                    gameScene.headLinePixel = this.getShape().getY() + getTiledTextureRegion().getHeight(0)/2;
                    gameScene.deadLinePixel = this.getShape().getY() + getTiledTextureRegion().getHeight(0)/2 + CAMERA_HEIGHT/2 + 50;
                }
                if(this.getShape().getY() > gameScene.deadLinePixel){
                    gameOver();
                }
//                if(needToChange){
//                    body.setTransform(body.getPosition().x,body.getPosition().y + currentPlatform.getHeight()/3/32,body.getAngle());
//                    needToChange = false;
//                }
            }
        };
        physicsWorld.registerPhysicsConnector(playerConnector);
        //注册碰撞检测
        physicsWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                UserData userDataA = (UserData)bodyA.getUserData();
                UserData userDataB = (UserData)bodyB.getUserData();
                if(userDataA.type.equals("player") && userDataB.type.equals("floor")){
                    onGround = true;
                    jumping = false;
                    if(!running){
                        body.applyLinearImpulse(new Vector2(30,0),body.getWorldCenter());
                        running = true;
                    }
                    gameScene.getForceBar().resetForceBar();
                    gameScene.getForceBar().startAddForce();
                }
                if(userDataA.type.equals("player") && userDataB.type.equals("right")){
                    direction = GameScene.Direction.DIRECTION_LEFT;
                    ((Player)userDataA.object).setFlippedHorizontal(true);
                    body.setLinearVelocity(new Vector2(0,body.getLinearVelocity().y));
                    body.applyLinearImpulse(new Vector2(-30,0),body.getWorldCenter());
                }
                if(userDataA.type.equals("player") && userDataB.type.equals("left")) {
                    direction = GameScene.Direction.DIRECTION_RIGHT;
                    ((Player)userDataA.object).setFlippedHorizontal(false);
                    body.setLinearVelocity(new Vector2(0,body.getLinearVelocity().y));
                    body.applyLinearImpulse(new Vector2(30,0),body.getWorldCenter());
                }
            }

            //实现单向平台
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();
                UserData userDataA = (UserData) bodyA.getUserData();
                UserData userDataB = (UserData) bodyB.getUserData();
                if (userDataA.type.equals("player") && userDataB.type.equals("platform")) {
                    if (((Player) userDataA.object).getY() + ((Player) userDataA.object).getTiledTextureRegion().getHeight(0) > ((Platform) userDataB.object).getY()) {
                        contact.setEnabled(false);
                    } else {
                        currentPlatform = (Platform)(userDataB.object);
                        if(!onPlatform){
                            gameScene.getForceBar().resetForceBar();
                            if(currentPlatform.getClass() == SpringPlatform.class){
                                ((SpringPlatform)currentPlatform).playSpringAnimate(SpringPlatform.SPRING_ANIMATE_TYPE.PRESS,(Player)(userDataA.object));
//                                ((Player)userDataA.object).changePlayerPositionOnSpring();
//                                needToChange = true;
                            }
                        }
                        onPlatform = true;
                        jumping = false;
                        pressJumping = false;
                        gameScene.getForceBar().startAddForce();
                        ((Platform) userDataB.object).doEffectToPlayer((Player) userDataA.object);
//                        if(currentPlatform.getClass() == SpringPlatform.class){
//                            ((SpringPlatform)currentPlatform).changeSpringStatus(SpringPlatform.SPRING_STATUS.COMPRESS,(Player)(userDataA.object));
//                        }
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
                UserData userDataA = (UserData)bodyA.getUserData();
                UserData userDataB = (UserData)bodyB.getUserData();
                if(userDataA.type.equals("player") && userDataB.type.equals("floor")){
                    onGround = false;
                    jumping = true;
                    gameScene.getForceBar().stopAddForce();
                }
                if (userDataA.type.equals("player") && userDataB.type.equals("platform")) {
                    if(onPlatform) {
                        ((Platform) userDataB.object).clearEffectToPlayer((Player) userDataA.object);
                        onPlatform = false;
                        jumping = true;
//                        speedChanged = false;
                        gameScene.getForceBar().stopAddForce();
                        currentPlatform = (Platform)(userDataB.object);
                        if(!pressJumping){
                            if(currentPlatform.getClass() == SpringPlatform.class){
                                ((SpringPlatform)currentPlatform).playSpringAnimate(SpringPlatform.SPRING_ANIMATE_TYPE.RESET,(Player)(userDataA.object));
                            }
                        }
                    }
                }
            }
        });
    }

    public abstract void gameOver();

    public void jump(int power){
        if(!jumping){
            body.applyLinearImpulse(new Vector2(0,-power*30),body.getLocalCenter());
            if(onPlatform){
                if(currentPlatform.platformType.equals("spring")){
                    body.applyLinearImpulse(new Vector2(0, -80), body.getWorldCenter());
                    ((SpringPlatform)currentPlatform).playSpringAnimate(SpringPlatform.SPRING_ANIMATE_TYPE.RELEASE,this);
                }
            }
            pressJumping = true;
        }
    }

    public void setAnimating(){
        animating = true;
        final long[] PLAYER_ANIMATE = new long[]{100,100,100,100,100,100,100,100};
        animate(PLAYER_ANIMATE,0,7,true);
    }

    public Platform getCurrentPlatform() {
        return currentPlatform;
    }

//    public void changePlayerPositionOnSpring(){
//        setPosition(getX(),getY() - 100);
//    }

}
