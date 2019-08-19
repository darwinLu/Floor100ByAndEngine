package pers.lx.floor100byandengine.scene;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.GameActivity;
import pers.lx.floor100byandengine.base.BaseScene;
import pers.lx.floor100byandengine.manager.PlatformManager;
import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.manager.SceneManager;
import pers.lx.floor100byandengine.object.Floor;
import pers.lx.floor100byandengine.object.Platform;
import pers.lx.floor100byandengine.object.Player;
import pers.lx.floor100byandengine.object.UserData;
import pers.lx.floor100byandengine.object.Wall;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private HUD gameHUD;
    private Text scoreText;
    private PhysicsWorld mPhysicsWorld;

    public float headLinePixel = CAMERA_HEIGHT / 2;
    public float deadLinePixel = CAMERA_HEIGHT + 50;

    public static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0, 0);

    private Player player;
    private Floor floor;
    private Wall leftWall,rightWall;
    private PlatformManager platformManager;

    public enum Direction
    {
        DIRECTION_LEFT,
        DIRECTION_RIGHT
    }

    private Text gameOverText;
    private boolean gameOverDisplayed = false;

    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
        createLevel();
        createGameOverText();
        setOnSceneTouchListener(this);
//        loadLevel(1);
//        createGameOverText();
//        levelCompleteWindow = new LevelCompleteWindow(vbom);
//        setOnSceneTouchListener(this);
    }

    private void createBackground() {
        setBackground(new Background(Color.BLUE));
    }

    private void createHUD() {
        gameHUD = new HUD();
        scoreText = new Text(0,0, ResourcesManager.getInstance().font,"score",vbom);
        gameHUD.attachChild(scoreText);
        camera.setHUD(gameHUD);
    }

    private void createPhysics(){
        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH),false);
        registerUpdateHandler(this.mPhysicsWorld);
    }

    private void createLevel(){
        player = new Player(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT - 150, vbom, camera, mPhysicsWorld,this){
            @Override
            public void gameOver() {
                if (!gameOverDisplayed)
                {
                    displayGameOverText();
                }
            }
        };
        attachChild(player.shape);
        //创建主角
//        final IAreaShape rect = new Rectangle(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT/2 - 100,200,200,vbom);
//        rect.setColor(Color.RED);
//        this.rectBody = PhysicsFactory.createBoxBody(mPhysicsWorld,rect, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
//        rectBody.setFixedRotation(true);
//        rectBody.setUserData("rect");
//        PhysicsConnector rectConnector = new PhysicsConnector(rect,rectBody,true,true){
//            @Override
//            public void onUpdate(float pSecondsElapsed) {
//                super.onUpdate(pSecondsElapsed);
//                if(rect.getY() + rect.getHeight()/2 < headLinePixel){
//                    camera.setCenter(camera.getCenterX(),rect.getY() + rect.getHeight()/2 );
//                    headLinePixel = rect.getY() + rect.getHeight()/2;
//                    deadLinePixel = rect.getY() + rect.getHeight()/2 + CAMERA_HEIGHT/2 + 50;
//                }
//                if(rect.getY() > deadLinePixel){
////                    finish();
//                }
//            }
//        };
//        this.mPhysicsWorld.registerPhysicsConnector(rectConnector);
//        attachChild(rect);
//        this.getEngine().getCamera().setChaseEntity(rect);

        //创建地面
//        final IAreaShape floor = new Rectangle(0,CAMERA_HEIGHT - 100,CAMERA_WIDTH,100,vbom);
//        floor.setColor(Color.GREEN);
//        this.floorBody = PhysicsFactory.createBoxBody(mPhysicsWorld,floor, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
////        PhysicsConnector rectConnector = new PhysicsConnector(rect,rectBody,true,true);
////        this.mPhysicsWorld.registerPhysicsConnector(rectConnector);
//        floorBody.setUserData("floor");
        floor = new Floor(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT/2 - 100, vbom, camera, mPhysicsWorld);
        attachChild(floor.shape);
//
//        //生成多个平台
//        platformNumber = 10;
//        for(int i=0;i<platformNumber;i++){
//            IAreaShape platform = new Rectangle((i%2)*(CAMERA_WIDTH - 400),CAMERA_HEIGHT - (i + 2)*400,400,100,vobm);
//            platform.setColor(Color.GREEN);
//            Body platformBody = PhysicsFactory.createBoxBody(mPhysicsWorld,platform, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
//            platformBody.setUserData("platform"+ i);
//            platform.setUserData(platformBody);
//            platformShapeList.add(platform);
//            mScene.attachChild(platform);
//        }
//
        //创建两侧墙壁
        leftWall = new Wall(0,0,2,CAMERA_HEIGHT,vbom,camera,mPhysicsWorld,"left");
        rightWall = new Wall(CAMERA_WIDTH-2,0,2,CAMERA_HEIGHT,vbom,camera,mPhysicsWorld,"right");
        PhysicsConnector leftConnector = new PhysicsConnector(leftWall.shape,leftWall.body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                Log.d("darwin","leftConnector update");
                if(player.shape.getY() + player.shape.getHeight()/2 < CAMERA_HEIGHT/2){
                    leftWall.body.setTransform(new Vector2(leftWall.body.getPosition().x,player.body.getPosition().y),0);
                }
            }
        };
        mPhysicsWorld.registerPhysicsConnector(leftConnector);
        PhysicsConnector rightConnector = new PhysicsConnector(rightWall.shape,rightWall.body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(player.shape.getY() + player.shape.getHeight()/2 < CAMERA_HEIGHT/2){
                    rightWall.body.setTransform(new Vector2(rightWall.body.getPosition().x,player.body.getPosition().y),0);
                }
            }
        };
        mPhysicsWorld.registerPhysicsConnector(rightConnector);
//        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
//        this.leftBody = PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
//        this.rightBody = PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);
//        leftBody.setUserData("left");
//        rightBody.setUserData("right");
//        PhysicsConnector leftConnector = new PhysicsConnector(left,leftBody,true,true){
//            @Override
//            public void onUpdate(float pSecondsElapsed) {
//                super.onUpdate(pSecondsElapsed);
//                if(rect.getY() + rect.getHeight()/2 < CAMERA_HEIGHT/2){
//                    leftBody.setTransform(new Vector2(leftBody.getPosition().x,rectBody.getPosition().y),0);
//                }
//            }
//        };
//        this.mPhysicsWorld.registerPhysicsConnector(leftConnector);
//        PhysicsConnector rightConnector = new PhysicsConnector(right,rightBody,true,true){
//            @Override
//            public void onUpdate(float pSecondsElapsed) {
//                super.onUpdate(pSecondsElapsed);
//                if(rect.getY() + rect.getHeight()/2 < CAMERA_HEIGHT/2){
//                    rightBody.setTransform(new Vector2(rightBody.getPosition().x,rectBody.getPosition().y),0);
//                }
//            }
//        };
//        this.mPhysicsWorld.registerPhysicsConnector(rightConnector);
        attachChild(leftWall.shape);
        attachChild(rightWall.shape);

        platformManager = PlatformManager.getInstance();
        platformManager.initPlatform(this,vbom,camera,mPhysicsWorld);

        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                platformManager.checkPlatformIsDead(getScene(),vbom,camera,mPhysicsWorld);
            }

            @Override
            public void reset() {

            }

        });
//
//        //注册碰撞检测
//        this.mPhysicsWorld.setContactListener(new ContactListener() {
//            @Override
//            public void beginContact(Contact contact) {
//                Body bodyA = contact.getFixtureA().getBody();
//                Body bodyB = contact.getFixtureB().getBody();
//
//
//            }
//
//            @Override
//            public void endContact(Contact contact) {
//                Body bodyA = contact.getFixtureA().getBody();
//                Body bodyB = contact.getFixtureB().getBody();
//
////                if(idA.equals("player") && idB.equals("lrPlatform")) {
////                    onLrPlatform = false;
////                    if(lrDirection.equals("left")){
////                        rectBody.applyLinearImpulse(new Vector2(-30,0),rectBody.getWorldCenter());
////                    }
////                    else{
////                        rectBody.applyLinearImpulse(new Vector2(30,0),rectBody.getWorldCenter());
////                    }
////                    speedChanged = false;
//////                    allowRoll = true;
//////                    rectBody.applyLinearImpulse(new Vector2(-200,0),rectBody.getWorldCenter());
////                }
////                if(idA.equals("rect") && idB.equals("rollPlatform")) {
////                    onRollPlatform = false;
////                    if(direction.equals("left")){
////                        rectBody.applyLinearImpulse(new Vector2(-100,0),rectBody.getWorldCenter());
////                    }
////                    else{
////                        rectBody.applyLinearImpulse(new Vector2(-100,0),rectBody.getWorldCenter());
////                    }
////                    speedChanged = false;
//////                    allowRoll = true;
//////                    rectBody.applyLinearImpulse(new Vector2(-200,0),rectBody.getWorldCenter());
////                }
////                if(idA.equals("rect") && idB.equals("springPlatform")) {
////                    onSpringPlatform = false;
//////                    rectBody.applyLinearImpulse(new Vector2(600, 0), rectBody.getLocalCenter());
////                }
//////                if(idA.equals("rect") && idB.equals("right")){
//////                    touchWall = false;
////////                    rectBody.applyLinearImpulse(new Vector2(-600,0),rectBody.getLocalCenter());
//////                }
//////                if(idA.equals("rect") && idB.equals("left")) {
//////                    touchWall = false;
////////                    rectBody.applyLinearImpulse(new Vector2(600, 0), rectBody.getLocalCenter());
//////                }
//            }
//
//            //实现单向平台
//            @Override
//            public void preSolve(Contact contact, Manifold oldManifold) {
//                Body bodyA = contact.getFixtureA().getBody();
//                Body bodyB = contact.getFixtureB().getBody();
//                UserData userDataA = (UserData)bodyA.getUserData();
//                UserData userDataB = (UserData)bodyB.getUserData();
//
//                if(userDataA.type.equals("player") && userDataB.type.equals("platform")){
//                    if(((Player)userDataA.object).getY() + ((Player)userDataA.object).getHeight()  > ((Platform)userDataB.object).getY()){
//                        contact.setEnabled(false);
//                    }
//                }
////                if(idA.equals("rect") && idB.equals("platform3")){
////                    if(rect.getY() + rect.getHeight()  > platform3.getY()){
////                        contact.setEnabled(false);
////                    }
////                }
////                if(idA.equals("rect") && idB.equals("udPlatform")){
////                    if(rect.getY() + rect.getHeight()  > udPlatform.getY()){
////                        contact.setEnabled(false);
////                    }
////                }
////                if(idA.equals("rect") && idB.equals("lrPlatform")){
////                    if(rect.getY() + rect.getHeight()  > lrPlatform.getY()){
////                        contact.setEnabled(false);
////                        onLrPlatform = false;
////                    }
////                    else{
////                        onLrPlatform = true;
////                        if(!speedChanged){
////                            if(lrDirection.equals("left")){
////                                rectBody.applyLinearImpulse(new Vector2(30,0),rectBody.getWorldCenter());
////                            }
////                            else{
////                                rectBody.applyLinearImpulse(new Vector2(-30,0),rectBody.getWorldCenter());
////                            }
////                            speedChanged = true;
////                        }
//////                        if(touchWall){
//////                            rectBody.applyLinearImpulse(new Vector2(200,0),rectBody.getWorldCenter());
//////                            touchWall = false;
//////                        }
////                    }
////                }
////                if(idA.equals("rect") && idB.equals("rollPlatform")){
////                    if(rect.getY() + rect.getHeight()  > rollPlatform.getY()){
////                        contact.setEnabled(false);
////                        onRollPlatform = false;
////                    }
////                    else{
////                        onRollPlatform = true;
////                        if(!speedChanged){
////                            if(direction.equals("left")){
////                                rectBody.applyLinearImpulse(new Vector2(100,0),rectBody.getWorldCenter());
////                            }
////                            else{
////                                rectBody.applyLinearImpulse(new Vector2(100,0),rectBody.getWorldCenter());
////                            }
////                            speedChanged = true;
////                        }
//////                        if(touchWall){
//////                            rectBody.applyLinearImpulse(new Vector2(200,0),rectBody.getWorldCenter());
//////                            touchWall = false;
//////                        }
////                    }
//////                    else{
//////                        if(rectBody.getLinearVelocity().y < 0 ){
//////                            contact.setEnabled(false);
//////                        }
//////                        else{
//////                            if(allowRoll){
//////                                rectBody.applyLinearImpulse(new Vector2(200,0),rectBody.getWorldCenter());
//////                                allowRoll = false;
//////                            }
//////                        }
//////                    }
////                }
////                if(idA.equals("rect") && idB.equals("springPlatform")){
////                    if(rect.getY() + rect.getHeight()  > springPlatform.getY()){
////                        contact.setEnabled(false);
////                        onSpringPlatform = false;
////                    }
////                }
//            }
//
//            @Override
//            public void postSolve(Contact contact, ContactImpulse impulse) {
//
//            }
//        });
//
//        //世界注册到场景
//        this.registerUpdateHandler(this.mPhysicsWorld);
    }

    private void createGameOverText() {
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {

    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
        if(this.mPhysicsWorld != null){
            if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
               player.jump();
            }
            return true;
        }
        return false;
    }

    private GameScene getScene(){
        return this;
    }

    private void displayGameOverText()
    {
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        attachChild(gameOverText);
        gameOverDisplayed = true;
    }
}
