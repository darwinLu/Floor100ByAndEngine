package pers.lx.floor100byandengine.manager;

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

import pers.lx.floor100byandengine.object.Player;
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
            IAreaShape platform = new Rectangle(CAMERA_WIDTH / 10 * rand.nextInt(10),CAMERA_HEIGHT - (i + 2)*200,160,30,vbom);
            platform.setColor(Color.GREEN);
            Body platformBody = PhysicsFactory.createBoxBody(physicsWorld,platform, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
            platformBody.setUserData("platform"+ i);
            platform.setUserData(platformBody);
            platformShapeList.add(platform);
            platformBodyList.add(platformBody);
            gameScene.attachChild(platform);
        }
    }

    public void addPlatformToLast(GameScene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
            float lastHeight = platformShapeList.getLast().getY();
            IAreaShape platform = new Rectangle(CAMERA_WIDTH / 10 * rand.nextInt(10),lastHeight - 200,160,30,vbom);
            platform.setColor(Color.PINK);
            Body platformBody = PhysicsFactory.createBoxBody(physicsWorld,platform, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
            platformBody.setUserData("platform");
            platform.setUserData(platformBody);
            platformShapeList.add(platform);
            platformBodyList.add(platformBody);
            gameScene.attachChild(platform);
    }

    public void removePlatformFromFirst(GameScene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
        physicsWorld.destroyBody((Body)platformShapeList.getFirst().getUserData());
        platformShapeList.removeFirst();
        platformBodyList.removeFirst();
    }

    public void checkPlatformIsDead(GameScene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
        if(platformShapeList.getFirst().getY() > gameScene.deadLinePixel){
            removePlatformFromFirst(gameScene, vbom,  camera, physicsWorld);
            addPlatformToLast(gameScene, vbom,  camera, physicsWorld);
//            float lastHeight = platformShapeList.getLast().getY();
//            mPhysicsWorld.destroyBody((Body)platformShapeList.getFirst().getUserData());
//            platformShapeList.removeFirst();
//            IAreaShape platform = new Rectangle(CAMERA_WIDTH - 400,lastHeight - 400,400,100,getVertexBufferObjectManager());
//            platform.setColor(Color.PINK);
//            Body platformBody = PhysicsFactory.createBoxBody(mPhysicsWorld,platform, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
//            platformBody.setUserData("platform");
//            platform.setUserData(platformBody);
//            platformShapeList.add(platform);
//            mScene.attachChild(platform);

        }
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static PlatformManager getInstance()
    {
        return INSTANCE;
    }

}
