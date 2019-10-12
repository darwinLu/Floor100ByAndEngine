package pers.lx.floor100byandengine.scene;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.audio.music.Music;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;

import pers.lx.floor100byandengine.base.BaseScene;
import pers.lx.floor100byandengine.manager.PlatformManager;
import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.manager.SceneManager;
import pers.lx.floor100byandengine.object.Floor;
import pers.lx.floor100byandengine.object.ForceBar;
import pers.lx.floor100byandengine.object.LoopBackground;
import pers.lx.floor100byandengine.object.Player;
import pers.lx.floor100byandengine.object.Wall;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private Engine mEngine;

    private HUD gameHUD;
    private Text scoreText;
    private PhysicsWorld mPhysicsWorld;

    public float headLinePixel = CAMERA_HEIGHT / 2;
    public float deadLinePixel = CAMERA_HEIGHT + 50;

    public static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0, 0);

    public Player player;
    private Floor floor;
    private Wall leftWall,rightWall;
    private PlatformManager platformManager;

    private ForceBar forceBar;
    private int score;

    private LoopBackground loopBackground;

    private Music mMusic;

    public enum Direction
    {
        DIRECTION_LEFT,
        DIRECTION_RIGHT
    }

    private Text gameOverText;
    private boolean gameOverDisplayed = false;

    public GameScene(Engine mEngine){
        super();
        this.mEngine = mEngine;
    }

    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
        createLevel();
        createGameOverText();
        setOnSceneTouchListener(this);
        playMusic();
    }

    private void playMusic() {
        mMusic = ResourcesManager.getInstance().mMusic;
        mMusic.play();
    }

    private void createBackground() {
        loopBackground = new LoopBackground(vbom){
            @Override
            protected void onManagedUpdate(float pSecondsElapsed) {
                super.onManagedUpdate(pSecondsElapsed);
                if(deadLinePixel < loopBackground.shape1.getY()){
                    loopBackground.shape1.setY(loopBackground.shape2.getY() - loopBackground.shape1.getHeight() + 1);
                }
                if(deadLinePixel < loopBackground.shape2.getY()){
                    loopBackground.shape2.setY(loopBackground.shape1.getY() - loopBackground.shape2.getHeight() + 1);
                }
            }
        };
        attachChild(loopBackground);
        attachChild(loopBackground.shape1);
        attachChild(loopBackground.shape2);
    }

    private void createHUD() {
        gameHUD = new HUD();
        scoreText = new Text(0,0, ResourcesManager.getInstance().font,"层数：0123456789",vbom);
        scoreText.setText("层数：0");
        gameHUD.attachChild(scoreText);
        forceBar = new ForceBar(CAMERA_WIDTH  - 205 , 5,100,0,200,50,10,vbom);
        forceBar.attachToHUD(gameHUD);
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
                    ResourcesManager.getInstance().game_over_sound.play();
                    displayGameOverText();
                }
            }
        };
        attachChild(player);
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
        mMusic.stop();
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        final Engine.EngineLock engineLock = mEngine.getEngineLock();
        engineLock.lock();
        detachChild(loopBackground.shape1);
        loopBackground.shape1.dispose();
        loopBackground.shape1 = null;
        detachChild(loopBackground.shape2);
        loopBackground.shape2.dispose();
        loopBackground.shape2 = null;
        detachChild(loopBackground);
        loopBackground.dispose();
        loopBackground = null;
        detachChild(player);
        player.dispose();
        player = null;
        detachChild(floor);
        floor.dispose();
        floor = null;
        detachChild(leftWall.shape);
        leftWall.shape.dispose();
        leftWall.shape = null;
        detachChild(rightWall.shape);
        rightWall.shape.dispose();
        rightWall.shape = null;
        platformManager.disposeAllPlatform(this,mPhysicsWorld);
        mPhysicsWorld.dispose();
        mPhysicsWorld = null;
        gameHUD.detachChild(scoreText);
        gameHUD = null;
        camera.setHUD(null);
        camera.setCenter(270,480);
        engineLock.unlock();
        mMusic.stop();
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
    {
        if(this.mPhysicsWorld != null){
            if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
                forceBar.startListenForce();
            }
            else if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP){
                if(!player.jumping){
                    forceBar.stopListenForce();
                    player.jump(forceBar.getPower());
                }
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
//        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX() - gameOverText.getWidth()/2 , camera.getCenterY() - gameOverText.getHeight()/2);
        attachChild(gameOverText);
        gameOverDisplayed = true;
        setChildScene(pauseScene(), false, true, true);
    }

    // Menu Scene with Play Button

    private MenuScene pauseScene(){
        final MenuScene pauseGame= new MenuScene(camera);
        pauseGame.setPosition(0, 0);

        final SpriteMenuItem btnPlay = new SpriteMenuItem(1, resourcesManager.replay_region,vbom);
//        btnPlay.setPosition(0, 0);
        btnPlay.setPosition(CAMERA_WIDTH/2 - btnPlay.getWidth()/2, CAMERA_HEIGHT/2 + 50);
//        btnPlay.setScale(2);
        pauseGame.addMenuItem(btnPlay);

        pauseGame.setBackgroundEnabled(false);
        pauseGame.setOnMenuItemClickListener(new MenuScene.IOnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
                switch(pMenuItem.getID()){
                    case 1:
                        disposeScene();
                        SceneManager.getInstance().loadGameScene(engine);
                        return true;
                    default:
                        return false;
                }
            }
        });
        return pauseGame;
    }

    public ForceBar getForceBar() {
        return forceBar;
    }

    public void addScore(int i){
        score += i;
        scoreText.setText("层数：" + score);
    }
}
