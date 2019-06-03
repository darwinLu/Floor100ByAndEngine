package pers.lx.floor100byandengine.scene;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
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
import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.manager.SceneManager;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private AnimatedSprite mPlayerRun;

    private HUD gameHUD;
    private Text scoreText;
    private PhysicsWorld mPhysicsWorld;

    private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0, 0);
    private Body rectBody;
    private float headLinePixel = CAMERA_HEIGHT/2;
    private float deadLinePixel = CAMERA_HEIGHT + 50;
    private Body floorBody;
    private Body leftBody;
    private Body rightBody;

    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
        createLevel();
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
        //创建正方体
        final IAreaShape rect = new Rectangle(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT/2 - 100,200,200,vbom);
        rect.setColor(Color.RED);
        this.rectBody = PhysicsFactory.createBoxBody(mPhysicsWorld,rect, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        rectBody.setFixedRotation(true);
        rectBody.setUserData("rect");
        PhysicsConnector rectConnector = new PhysicsConnector(rect,rectBody,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(rect.getY() + rect.getHeight()/2 < headLinePixel){
                    camera.setCenter(camera.getCenterX(),rect.getY() + rect.getHeight()/2 );
                    headLinePixel = rect.getY() + rect.getHeight()/2;
                    deadLinePixel = rect.getY() + rect.getHeight()/2 + CAMERA_HEIGHT/2 + 50;
                }
                if(rect.getY() > deadLinePixel){
//                    finish();
                }
            }
        };
        this.mPhysicsWorld.registerPhysicsConnector(rectConnector);
        attachChild(rect);
//        this.getEngine().getCamera().setChaseEntity(rect);

        //创建地面
        final IAreaShape floor = new Rectangle(0,CAMERA_HEIGHT - 100,CAMERA_WIDTH,100,vbom);
        floor.setColor(Color.GREEN);
        this.floorBody = PhysicsFactory.createBoxBody(mPhysicsWorld,floor, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
//        PhysicsConnector rectConnector = new PhysicsConnector(rect,rectBody,true,true);
//        this.mPhysicsWorld.registerPhysicsConnector(rectConnector);
        floorBody.setUserData("floor");
        attachChild(floor);
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
        final IAreaShape left = new Rectangle(0,0,2,CAMERA_HEIGHT,vbom);
        final IAreaShape right = new Rectangle(CAMERA_WIDTH-2,0,2,CAMERA_HEIGHT,vbom);
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
        this.leftBody = PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
        this.rightBody = PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);
        leftBody.setUserData("left");
        rightBody.setUserData("right");
        PhysicsConnector leftConnector = new PhysicsConnector(left,leftBody,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(rect.getY() + rect.getHeight()/2 < CAMERA_HEIGHT/2){
                    leftBody.setTransform(new Vector2(leftBody.getPosition().x,rectBody.getPosition().y),0);
                }
            }
        };
        this.mPhysicsWorld.registerPhysicsConnector(leftConnector);
        PhysicsConnector rightConnector = new PhysicsConnector(right,rightBody,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(rect.getY() + rect.getHeight()/2 < CAMERA_HEIGHT/2){
                    rightBody.setTransform(new Vector2(rightBody.getPosition().x,rectBody.getPosition().y),0);
                }
            }
        };
        this.mPhysicsWorld.registerPhysicsConnector(rightConnector);
        attachChild(left);
        attachChild(right);
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

    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {

        return false;
    }
}
