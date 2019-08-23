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
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.GameActivity;
import pers.lx.floor100byandengine.base.BaseScene;
import pers.lx.floor100byandengine.manager.PlatformManager;
import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.manager.SceneManager;
import pers.lx.floor100byandengine.object.Floor;
import pers.lx.floor100byandengine.object.LoopBackground;
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

    private LoopBackground loopBackground;

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
    }

    private void createBackground() {
//        setBackground(new LoopBackground(Color.BLUE));
//        background = new Sprite(0,0,resourcesManager.background_region,vbom);
        loopBackground = new LoopBackground(vbom){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                Log.d("darwin",String.valueOf(deadLinePixel));
                Log.d("darwin",String.valueOf(loopBackground.shape1.getY()));
                if(deadLinePixel < loopBackground.shape1.getY()){
                    loopBackground.shape1.setY(loopBackground.shape2.getY() - loopBackground.shape1.getHeight());
                }
            }
        };
        attachChild(loopBackground);
        attachChild(loopBackground.shape1);
        attachChild(loopBackground.shape2);
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
        //创建主角
        player = new Player(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT - resourcesManager.player_region.getHeight(0) - 100, vbom, camera, mPhysicsWorld,this){
            @Override
            public void gameOver() {
                if (!gameOverDisplayed)
                {
                    displayGameOverText();
                }
            }
        };
        attachChild(player);
        player.setZIndex(1);
        player.setAnimating();
//        attachChild(player.shape);
//        player.shape.setZIndex(1);
        //创建地面
        floor = new Floor(0, CAMERA_HEIGHT - resourcesManager.floor_region.getHeight(), vbom, camera, mPhysicsWorld);
        attachChild(floor);
        //创建两侧墙壁
        leftWall = new Wall(0,0,2,CAMERA_HEIGHT,vbom,camera,mPhysicsWorld,"left");
        rightWall = new Wall(CAMERA_WIDTH-2,0,2,CAMERA_HEIGHT,vbom,camera,mPhysicsWorld,"right");
        PhysicsConnector leftConnector = new PhysicsConnector(leftWall.shape,leftWall.body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                Log.d("darwin","leftConnector update");
                if(player.getY() + player.getTiledTextureRegion().getHeight(0)/2 < CAMERA_HEIGHT/2){
                    leftWall.body.setTransform(new Vector2(leftWall.body.getPosition().x,player.body.getPosition().y),0);
                }
            }
        };
        mPhysicsWorld.registerPhysicsConnector(leftConnector);
        PhysicsConnector rightConnector = new PhysicsConnector(rightWall.shape,rightWall.body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(player.getY() + player.getTiledTextureRegion().getHeight(0)/2 < CAMERA_HEIGHT/2){
                    rightWall.body.setTransform(new Vector2(rightWall.body.getPosition().x,player.body.getPosition().y),0);
                }
            }
        };
        mPhysicsWorld.registerPhysicsConnector(rightConnector);
        attachChild(leftWall.shape);
        attachChild(rightWall.shape);
        //初始化平台
        platformManager = PlatformManager.getInstance();
        platformManager.initPlatform(this,vbom,camera,mPhysicsWorld);
        //注册平台检测
        this.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                platformManager.checkPlatformIsDead(getScene(),vbom,camera,mPhysicsWorld);
            }

            @Override
            public void reset() {

            }
        });
        //精灵排序
        sortChildren();
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
