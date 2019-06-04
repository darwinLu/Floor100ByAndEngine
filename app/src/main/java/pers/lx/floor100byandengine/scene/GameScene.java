package pers.lx.floor100byandengine.scene;

import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
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
import pers.lx.floor100byandengine.object.Player;
import pers.lx.floor100byandengine.object.Wall;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private HUD gameHUD;
    private Text scoreText;
    private PhysicsWorld mPhysicsWorld;

    public static float HEAD_LINE_PIXEL = CAMERA_HEIGHT / 2;
    public static float DEAD_LINE_PIXEL = CAMERA_HEIGHT + 50;

    public static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0, 0);

    private Player player;
    private Floor floor;
    private Wall leftWall,rightWall;
    private PlatformManager platformManager;

    private Body rectBody;
    private Body floorBody;
    private Body leftBody;
    private Body rightBody;



    public enum Direction
    {
        DIRECTION_LEFT,
        DIRECTION_RIGHT
    }


    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
        createLevel();
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
        player = new Player(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT/2 - 100, vbom, camera, mPhysicsWorld);
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
        leftWall = new Wall(0,0,2,CAMERA_HEIGHT,vbom,camera,mPhysicsWorld);
        rightWall = new Wall(CAMERA_WIDTH-2,0,2,CAMERA_HEIGHT,vbom,camera,mPhysicsWorld);
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
}
