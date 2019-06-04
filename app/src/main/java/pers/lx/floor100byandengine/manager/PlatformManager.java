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

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class PlatformManager {

    private static final PlatformManager INSTANCE = new PlatformManager();

    //总平台数
    private int platformCount;

    //保存平台Shape和Body的容器
    private LinkedList<IAreaShape> platformShapeList = new LinkedList<>();
    private LinkedList<Body> platformBodyList = new LinkedList<>();

    public void initPlatform(Scene gameScene,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld){
        platformCount = 10;
        for(int i=0;i<platformCount;i++){
            IAreaShape platform = new Rectangle((i%2)*(CAMERA_WIDTH - 400),CAMERA_HEIGHT - (i + 2)*400,400,100,vbom);
            platform.setColor(Color.GREEN);
            Body platformBody = PhysicsFactory.createBoxBody(physicsWorld,platform, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
            platformBody.setUserData("platform"+ i);
            platform.setUserData(platformBody);
            platformShapeList.add(platform);
            gameScene.attachChild(platform);
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
