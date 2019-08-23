package pers.lx.floor100byandengine;

import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseSineInOut;

public class MainGameActivity extends BaseGameActivity implements IOnSceneTouchListener {

    private static final int CAMERA_WIDTH = 1080;
    private static final int CAMERA_HEIGHT = 1920;

    private Font mFont;
    private BitmapTextureAtlas mPlayerTexture;
    private ITextureRegion mPlayerTextureRegion;
    private Sprite mPlayer;

    private AnimatedSprite mPlayerRun;
    private ITiledTextureRegion mPlayerRunTiledTextureRegion;
    private BitmapTextureAtlas mPlayerRunTexture;

    private BitmapTextureAtlas mPlatformTexture;
    private ITextureRegion mPlatformTextureRegion;
    private Sprite mPlatform;

    private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
    private PhysicsWorld mPhysicsWorld;
    private Body mPlayerBody;


    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        this.mPlayerTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mPlayerTexture, this, "robot.png", 0, 0, 1, 1);
        this.mPlayerTexture.load();
        this.mPlayerRunTexture = new BitmapTextureAtlas(this.getTextureManager(),10240,10240,TextureOptions.BILINEAR);
        this.mPlayerRunTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mPlayerRunTexture,this,"robot_run_lr.png",0,0,8,2);
        this.mPlayerRunTexture.load();
        this.mPlatformTexture = new BitmapTextureAtlas(this.getTextureManager(),1024,1024,TextureOptions.BILINEAR);
        this.mPlatformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mPlatformTexture,this,"platform.png",0,0);
        this.mPlatformTexture.load();

        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
        Scene mScene = new Scene();

        mScene.setOnSceneTouchListener(this);

        mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

        this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,SensorManager.GRAVITY_EARTH), false);
        final IAreaShape ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, getVertexBufferObjectManager());
        final IAreaShape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2,getVertexBufferObjectManager());
        final IAreaShape left = new Rectangle(0,0,2,CAMERA_HEIGHT,getVertexBufferObjectManager());
        final IAreaShape right = new Rectangle(CAMERA_WIDTH-2,0,2,CAMERA_HEIGHT,getVertexBufferObjectManager());

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        //PhysicsFactory.createBoxBody(mPhysicsWorld, roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyDef.BodyType.StaticBody, wallFixtureDef);

        mScene.attachChild(ground);
        //mScene.attachChild(roof);
        mScene.attachChild(left);
        mScene.attachChild(right);

        this.mPlatform = new Sprite(-300,500,mPlatformTextureRegion,new VertexBufferObjectManager());
        final IAreaShape platform = new Rectangle(-300,500,mPlatform.getWidth(),mPlatform.getHeight(),getVertexBufferObjectManager());
        PhysicsFactory.createBoxBody(mPhysicsWorld, platform, BodyDef.BodyType.StaticBody, wallFixtureDef);

        mScene.attachChild(mPlatform);

        this.mPlayerRun = new AnimatedSprite(0,0,mPlayerRunTiledTextureRegion,new VertexBufferObjectManager());
        this.mPlayerBody = PhysicsFactory.createBoxBody(mPhysicsWorld,mPlayerRun,BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        this.mPlayerBody.setFixedRotation(true);
        mPlayerRun.animate(new long[]{60,60,60,60,60,60,60,60}, 0, 7, true);
        SequenceEntityModifier sequenceEntityModifier = new SequenceEntityModifier(
                 new MoveXModifier(5, 0, CAMERA_WIDTH - mPlayerRun.getWidth(), new IEntityModifier.IEntityModifierListener() {
                @Override
                public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

                }

                @Override
                public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                    mPlayerRun.animate(new long[]{60,60,60,60,60,60,60,60}, 8, 15, true);
                }
            }),
                new MoveXModifier(5, CAMERA_WIDTH - mPlayerRun.getWidth(), 0, new IEntityModifier.IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

                    }

                    @Override
                    public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                        mPlayerRun.animate(new long[]{60,60,60,60,60,60,60,60}, 0, 7, true);
                    }
                })
        );
        LoopEntityModifier loopEntityModifier = new LoopEntityModifier(sequenceEntityModifier,-1);
        mPlayerRun.registerEntityModifier(loopEntityModifier);
        mScene.attachChild(this.mPlayerRun);
        PhysicsConnector connector = new PhysicsConnector(mPlayerRun,mPlayerBody,true,true);
        this.mPhysicsWorld.registerPhysicsConnector(connector);

        mScene.registerUpdateHandler(this.mPhysicsWorld);

        mPlayerRun.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {

            }

            @Override
            public void reset() {

            }
        });



        pOnCreateSceneCallback.onCreateSceneFinished(mScene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if(this.mPhysicsWorld != null){
            if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
                this.runOnUpdateThread(new Runnable(){
                    @Override
                    public void run() {
                        mPlayerBody.setLinearVelocity(0,-20);
                    }
                });
            }
            return true;
        }
        return false;
    }

    //    @Override
//    public void onCreateResources() {
//        this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
//        this.mFont.load();
//        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//        this.mPlayerTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
//        this.mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mPlayerTexture, this, "robot.png", 0, 0, 1, 1);
//        this.mPlayerTexture.load();
//
//        this.mPlayerRunTexture = new BitmapTextureAtlas(this.getTextureManager(),10240,10240,TextureOptions.BILINEAR);
//        this.mPlayerRunTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mPlayerRunTexture,this,"robot_run.png",0,0,8,1);
//        this.mPlayerRunTexture.load();
//    }

//    @Override
//    public Scene onCreateScene() {
//        this.mEngine.registerUpdateHandler(new FPSLogger());
//
//        final Scene scene = new Scene();
//        scene.setBackground(new LoopBackground(0.09804f, 0.6274f, 0.8784f));
//
////        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
////        final Text centerText = new Text(100, 40, this.mFont, "Hello AndEngine!\nYou can even have multilined text!", new TextOptions(HorizontalAlign.CENTER), vertexBufferObjectManager);
////        final Text leftText = new Text(100, 170, this.mFont, "Also left aligned!\nLorem ipsum dolor sit amat...", new TextOptions(HorizontalAlign.LEFT), vertexBufferObjectManager);
////        final Text rightText = new Text(100, 300, this.mFont, "And right aligned!\nLorem ipsum dolor sit amat...", new TextOptions(HorizontalAlign.RIGHT), vertexBufferObjectManager);
////
////        scene.attachChild(centerText);
////        scene.attachChild(leftText);
////        scene.attachChild(rightText);
//
//        this.mPlayer = new Sprite(20,20,500,500,mPlayerTextureRegion,this.getVertexBufferObjectManager());
//        this.mPlayer.setPosition(this.getEngine().getCamera().getCenterX(),this.getEngine().getCamera().getCenterY());
//        scene.attachChild(this.mPlayer);
//
//        this.mPlayerRun = new AnimatedSprite(0,0,mPlayerRunTiledTextureRegion,new VertexBufferObjectManager());
//        scene.attachChild(this.mPlayerRun);
//        mPlayerRun.animate(60);
//        mPlayerRun.registerUpdateHandler(new IUpdateHandler() {
//            @Override
//            public void onUpdate(float pSecondsElapsed) {
//                mPlayerRun.setX(mPlayerRun.getX()+1);
//            }
//
//            @Override
//            public void reset() {
//
//            }
//        });
//        return scene;
//    }

}
