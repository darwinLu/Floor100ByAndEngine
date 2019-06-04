package pers.lx.floor100byandengine.object;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.DEAD_LINE_PIXEL;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;
import static pers.lx.floor100byandengine.scene.GameScene.HEAD_LINE_PIXEL;

public class Player extends AnimatedSprite {

    // ---------------------------------------------
    // VARIABLES
    // ---------------------------------------------

    public Body body;
    public IAreaShape shape;

    private GameScene.Direction direction = GameScene.Direction.DIRECTION_RIGHT;

    public boolean onGround;
    public boolean jumping;

    // ---------------------------------------------
    // CONSTRUCTOR
    // ---------------------------------------------

    public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbom);
        createPhysics(camera, physicsWorld,vbom);
//        camera.setChaseEntity(this);
    }

//    public Player(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld)
//    {
//        super(pX, pY, ResourcesManager.getInstance().player_region, vbom);
//        createPhysics(camera, physicsWorld,vbom);
//    }

    // ---------------------------------------------
    // CLASS LOGIC
    // ---------------------------------------------

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld,VertexBufferObjectManager vbom){
        shape = new Rectangle(CAMERA_WIDTH/2 - 100, CAMERA_HEIGHT/2 - 100,200,200,vbom);
        shape.setColor(Color.RED);
        body = PhysicsFactory.createBoxBody(physicsWorld,shape, BodyDef.BodyType.DynamicBody,FIXTURE_DEF);
        body.setFixedRotation(true);
        body.setUserData("player");
        PhysicsConnector rectConnector = new PhysicsConnector(shape,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                if(shape.getY() + shape.getHeight()/2 < HEAD_LINE_PIXEL){
                    camera.setCenter(camera.getCenterX(),shape.getY() + shape.getHeight()/2 );
                    HEAD_LINE_PIXEL = shape.getY() + shape.getHeight()/2;
                    DEAD_LINE_PIXEL = shape.getY() + shape.getHeight()/2 + CAMERA_HEIGHT/2 + 50;
                }
                if(shape.getY() > DEAD_LINE_PIXEL){
//                    gameOver();
                }
            }
        };
        physicsWorld.registerPhysicsConnector(rectConnector);
    }

    public void jump(){
        body.applyLinearImpulse(new Vector2(0,-800),body.getLocalCenter());
    }

}
