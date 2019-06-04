package pers.lx.floor100byandengine.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class Wall {

    public Body body;
    public IAreaShape shape;

    public Wall(float pX, float pY, float pWidth,float pHeight,VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld)
    {
        createPhysics(pX,pY,pWidth,pHeight,camera, physicsWorld,vbom);
    }

    private void createPhysics(float pX, float pY, float pWidth,float pHeight,Camera camera, PhysicsWorld physicsWorld, VertexBufferObjectManager vbom) {
        shape = new Rectangle(pX,pY,pWidth,pHeight,vbom);
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0.5f);
        body = PhysicsFactory.createBoxBody(physicsWorld, shape, BodyDef.BodyType.StaticBody, wallFixtureDef);
//        PhysicsConnector connector = new PhysicsConnector(shape,body,true,true){
//            @Override
//            public void onUpdate(float pSecondsElapsed) {
////                super.onUpdate(pSecondsElapsed);
////                if(rect.getY() + rect.getHeight()/2 < CAMERA_HEIGHT/2){
////                    leftBody.setTransform(new Vector2(leftBody.getPosition().x,rectBody.getPosition().y),0);
////                }
//            }
//        };
//        physicsWorld.registerPhysicsConnector(connector);
    }

}
