package pers.lx.floor100byandengine.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import java.util.Random;

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.GameActivity.CAMERA_HEIGHT;
import static pers.lx.floor100byandengine.GameActivity.CAMERA_WIDTH;
import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public abstract class Platform extends Entity {

    public String entityType = "platform";
    public String platformType;
    public String direction;
    public String effectForce;

    public int width = 160;
    public int height = 30;

    public IAreaShape shape;
    public Body body;

    //For Test
    public Color color;

    public Platform(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom){
        createPhysics(x, y, physicsWorld, camera, gameScene, vbom);
    }

    protected abstract void createPhysics( int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom);

    protected abstract void doEffectToPlayer(Player player);

    protected abstract void clearEffectToPlayer(Player player);

}
