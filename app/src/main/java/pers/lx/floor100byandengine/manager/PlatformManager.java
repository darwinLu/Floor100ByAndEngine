package pers.lx.floor100byandengine.manager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.LinkedList;
import java.util.Random;

import pers.lx.floor100byandengine.object.LrPlatform;
import pers.lx.floor100byandengine.object.NormalPlatform;
import pers.lx.floor100byandengine.object.Platform;
import pers.lx.floor100byandengine.object.Player;
import pers.lx.floor100byandengine.object.RollPlatform;
import pers.lx.floor100byandengine.object.SpringPlatform;
import pers.lx.floor100byandengine.object.UdPlatform;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class PlatformManager {

    private static final PlatformManager INSTANCE = new PlatformManager();

    //总平台数
    private int platformCount = 10;
    //随机数，用来随机分布平台位置
    private Random rand = new Random();

    //保存平台Shape和Body的容器
    private LinkedList<IAreaShape> platformShapeList = new LinkedList<>();
    private LinkedList<Body> platformBodyList = new LinkedList<>();

    public void initPlatform(GameScene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
        for(int i=0;i<platformCount;i++){
            Platform platform = new NormalPlatform(CAMERA_WIDTH / 10 * rand.nextInt(10),CAMERA_HEIGHT - (i + 1)*200,physicsWorld,camera,gameScene,vbom);
            platformShapeList.add(platform);
            platformBodyList.add(platform.body);
            gameScene.attachChild(platform);
        }
    }

    public void addPlatformToLast(GameScene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
        float lastHeight = platformShapeList.getLast().getY();
        Platform platform ;
        int typeNumber = rand.nextInt(10);
        platform = new RollPlatform(CAMERA_WIDTH / 10 * rand.nextInt(10), (int) (lastHeight - 200),physicsWorld,camera,gameScene,vbom);
        ((RollPlatform) platform).setAnimating();
        //        if(typeNumber > 7){
//            platform = new LrPlatform(CAMERA_WIDTH / 10 * rand.nextInt(10), (int) (lastHeight - 200),physicsWorld,camera,gameScene,vbom);
//        }
//        else if(typeNumber>3 && typeNumber<=7){
//            platform = new UdPlatform(CAMERA_WIDTH / 10 * rand.nextInt(10), (int) (lastHeight - 200),physicsWorld,camera,gameScene,vbom);
//        }
//        else if(typeNumber>2 && typeNumber<=3){
//            platform = new RollPlatform(CAMERA_WIDTH / 10 * rand.nextInt(10), (int) (lastHeight - 200),physicsWorld,camera,gameScene,vbom);
//            ((RollPlatform) platform).setAnimating();
//        }
//        else if(typeNumber>1 && typeNumber<=2){
//            platform = new SpringPlatform(CAMERA_WIDTH / 10 * rand.nextInt(10), (int) (lastHeight - 200),physicsWorld,camera,gameScene,vbom);
//        }
//        else {
//            platform = new NormalPlatform(CAMERA_WIDTH / 10 * rand.nextInt(10), (int) (lastHeight - 200),physicsWorld,camera,gameScene,vbom);
//        }
        platformShapeList.add(platform);
        platformBodyList.add(platform.body);
        gameScene.attachChild(platform);
        gameScene.sortChildren();
    }

    public void removePlatformFromFirst(GameScene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
//        physicsWorld.destroyBody((Body)platformShapeList.getFirst().getUserData());
        platformShapeList.removeFirst();
        platformBodyList.removeFirst();
    }

    public void checkPlatformIsDead(GameScene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
        if(platformShapeList.getFirst().getY() > gameScene.deadLinePixel){
            removePlatformFromFirst(gameScene, vbom,  camera, physicsWorld);
            addPlatformToLast(gameScene, vbom,  camera, physicsWorld);
            gameScene.addScore(1);
        }
    }

    public void disposeAllPlatform(GameScene gameScene,PhysicsWorld physicsWorld) {
        int currentShapeSize = platformShapeList.size();
        for(int i=0;i<currentShapeSize;i++){
            gameScene.detachChild(platformShapeList.getFirst());
            platformShapeList.getFirst().dispose();
            platformShapeList.removeFirst();
        }
        platformBodyList.clear();
        int currentBodySize = platformBodyList.size();
        for(int i=0;i<currentBodySize;i++){
            physicsWorld.destroyBody(platformBodyList.getFirst());
            platformBodyList.removeFirst();
        }
        platformBodyList.clear();
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static PlatformManager getInstance()
    {
        return INSTANCE;
    }
}
