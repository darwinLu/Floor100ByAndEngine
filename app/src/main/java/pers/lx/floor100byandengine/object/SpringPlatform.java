package pers.lx.floor100byandengine.object;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.scene.GameScene;

import static pers.lx.floor100byandengine.scene.GameScene.FIXTURE_DEF;

public class SpringPlatform extends Platform {

    public enum SPRING_STATUS {
        NORMAL,COMPRESS,UNCOMPRESS
    }
    public enum SPRING_ANIMATE_TYPE {
        PRESS,RESET,RELEASE
    }


    public SPRING_STATUS springStatus = SPRING_STATUS.NORMAL;
//    public SPRING_ANIMATE_TYPE springAnimateType;
    private boolean pressProcessing = false;
    private boolean resetProcessing = false;
    private boolean releaseProcessing = false;
    public boolean statusChangedPerUpdate = false;

    private float mTimerSecondsElapsed;
    private float mTimerSeconds = 2.0f;

    private Player player;

    public SpringPlatform(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
        super(x, y, ResourcesManager.getInstance().spring_platform_tile_region,physicsWorld, camera, gameScene, vbom);
        height = 45;
        platformType = "spring";
    }

    @Override
    protected void createPhysics(int x, int y, PhysicsWorld physicsWorld, Camera camera, GameScene gameScene, VertexBufferObjectManager vbom) {
//        shape = new Rectangle(x,y,width,height,vbom);
        body = PhysicsFactory.createBoxBody(physicsWorld,this, BodyDef.BodyType.KinematicBody,FIXTURE_DEF);
        body.setUserData(new UserData(entityType,this));

//        shape.setColor(Color.CYAN);
        this.setUserData(body);
        PhysicsConnector platformConnector = new PhysicsConnector(this,body,true,true){
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
//                mTimerSecondsElapsed += pSecondsElapsed;
//                if(mTimerSecondsElapsed >= mTimerSeconds) {
//                    if(pressProcessing){
//                        if(!statusChangedPerUpdate){
//                            if(springStatus == SPRING_STATUS.NORMAL){
//                                setCurrentTileIndex(1);
//                                body.setTransform(body.getPosition().x,body.getPosition().y + getHeight()/3/32,body.getAngle());
//                                springStatus = SPRING_STATUS.COMPRESS;
//                                statusChangedPerUpdate = true;
//                                pressProcessing = false;
//                            }
//                        }
//                    }
//                    if(resetProcessing){
//                        if(!statusChangedPerUpdate){
//                            if(springStatus == SPRING_STATUS.COMPRESS){
//                                setCurrentTileIndex(0);
//                                body.setTransform(body.getPosition().x,body.getPosition().y - getHeight()/3/32,body.getAngle());
//                                springStatus = SPRING_STATUS.NORMAL;
//                                statusChangedPerUpdate = true;
//                                resetProcessing = false;
//                            }
//                        }
//                    }
//                    if(releaseProcessing){
//                        if(!statusChangedPerUpdate){
//                            if(springStatus == SPRING_STATUS.COMPRESS){
//                                setCurrentTileIndex(0);
//                                body.setTransform(body.getPosition().x,body.getPosition().y - getHeight()/3/32,body.getAngle());
//                                springStatus = SPRING_STATUS.NORMAL;
//                                statusChangedPerUpdate = true;
//                            }
//                        }
//                        if(!statusChangedPerUpdate){
//                            if(springStatus == SPRING_STATUS.NORMAL){
//                                setCurrentTileIndex(2);
//                                body.setTransform(body.getPosition().x,body.getPosition().y - getHeight()/3/32,body.getAngle());
//                                springStatus = SPRING_STATUS.UNCOMPRESS;
//                                statusChangedPerUpdate = true;
//                            }
//                        }
//                        if(!statusChangedPerUpdate){
//                            if(springStatus == SPRING_STATUS.UNCOMPRESS){
//                                setCurrentTileIndex(0);
//                                body.setTransform(body.getPosition().x,body.getPosition().y + getHeight()/3/32,body.getAngle());
//                                springStatus = SPRING_STATUS.NORMAL;
//                                statusChangedPerUpdate = true;
//                                releaseProcessing = false;
//                            }
//                        }
//                    }
//                    statusChangedPerUpdate = false;
////                if(statusNeedToChange){
////                    if(springStatus == SPRING_STATUS.COMPRESS){
////                        setCurrentTileIndex(1);
////                        body.setTransform(body.getPosition().x,body.getPosition().y + getHeight()/3/32,body.getAngle());
////                    }
////                    else if(springStatus == SPRING_STATUS.NORMAL){
////
////                    }
////                    statusNeedToChange = false;
////                }
//
////                if(direction.equals("up")){
////                    body.setLinearVelocity(new Vector2(0,-1));
////                }
////                if(direction.equals("down")){
////                    body.setLinearVelocity(new Vector2(0,1));
////                }
////                if(body.getWorldCenter().y > 20){
////                    direction = "up";
////                }
////                if(body.getWorldCenter().y <10) {
////                    direction = "down";
////                }
//                }
            }
        };
        physicsWorld.registerPhysicsConnector(platformConnector);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        mTimerSecondsElapsed += pSecondsElapsed;
        if(mTimerSecondsElapsed >= mTimerSeconds) {
            if(pressProcessing){
                if(!statusChangedPerUpdate){
                    if(springStatus == SPRING_STATUS.NORMAL){
                        setCurrentTileIndex(1);
                        body.setTransform(body.getPosition().x,body.getPosition().y + getHeight()/3/32,body.getAngle());
                        springStatus = SPRING_STATUS.COMPRESS;
                        statusChangedPerUpdate = true;
                        pressProcessing = false;
                        player.body.setTransform(player.body.getPosition().x,player.body.getPosition().y + getHeight()/3/32,player.body.getAngle());
                    }
                }
            }
            if(resetProcessing){
                if(!statusChangedPerUpdate){
                    if(springStatus == SPRING_STATUS.COMPRESS){
                        setCurrentTileIndex(0);
                        body.setTransform(body.getPosition().x,body.getPosition().y - getHeight()/3/32,body.getAngle());
                        springStatus = SPRING_STATUS.NORMAL;
                        statusChangedPerUpdate = true;
                        resetProcessing = false;
                        player.body.setTransform(player.body.getPosition().x,player.body.getPosition().y - getHeight()/3/32,player.body.getAngle());
                    }
                }
            }
            if(releaseProcessing){
                if(!statusChangedPerUpdate){
                    if(springStatus == SPRING_STATUS.COMPRESS){
                        setCurrentTileIndex(0);
                        body.setTransform(body.getPosition().x,body.getPosition().y - getHeight()/3/32,body.getAngle());
                        springStatus = SPRING_STATUS.NORMAL;
                        statusChangedPerUpdate = true;
                        player.body.setTransform(player.body.getPosition().x,player.body.getPosition().y - getHeight()/3/32,player.body.getAngle());
                    }
                }
                if(!statusChangedPerUpdate){
                    if(springStatus == SPRING_STATUS.NORMAL){
                        setCurrentTileIndex(2);
                        body.setTransform(body.getPosition().x,body.getPosition().y - getHeight()/3/32,body.getAngle());
                        springStatus = SPRING_STATUS.UNCOMPRESS;
                        statusChangedPerUpdate = true;
                        player.body.setTransform(player.body.getPosition().x,player.body.getPosition().y - getHeight()/3/32,player.body.getAngle());
                    }
                }
                if(!statusChangedPerUpdate){
                    if(springStatus == SPRING_STATUS.UNCOMPRESS){
                        setCurrentTileIndex(0);
                        body.setTransform(body.getPosition().x,body.getPosition().y + getHeight()/3/32,body.getAngle());
                        springStatus = SPRING_STATUS.NORMAL;
                        statusChangedPerUpdate = true;
                        releaseProcessing = false;
                        player.body.setTransform(player.body.getPosition().x,player.body.getPosition().y + getHeight()/3/32,player.body.getAngle());
                    }
                }
            }
            statusChangedPerUpdate = false;
//                if(statusNeedToChange){
//                    if(springStatus == SPRING_STATUS.COMPRESS){
//                        setCurrentTileIndex(1);
//                        body.setTransform(body.getPosition().x,body.getPosition().y + getHeight()/3/32,body.getAngle());
//                    }
//                    else if(springStatus == SPRING_STATUS.NORMAL){
//
//                    }
//                    statusNeedToChange = false;
//                }

//                if(direction.equals("up")){
//                    body.setLinearVelocity(new Vector2(0,-1));
//                }
//                if(direction.equals("down")){
//                    body.setLinearVelocity(new Vector2(0,1));
//                }
//                if(body.getWorldCenter().y > 20){
//                    direction = "up";
//                }
//                if(body.getWorldCenter().y <10) {
//                    direction = "down";
//                }
        }
    }

    @Override
    protected void doEffectToPlayer(Player player) {

    }

    @Override
    protected void clearEffectToPlayer(Player player) {

    }

//    public void changeTile(int tileIndex){
//        setCurrentTileIndex(tileIndex);
//    }

//    public void changeSpringStatus(SPRING_STATUS springStatus){
//        this.springStatus = springStatus;
//        statusNeedToChange = true;
//    }

    public void playSpringAnimate(SPRING_ANIMATE_TYPE springAnimateType,Player player){
        if(springAnimateType == SPRING_ANIMATE_TYPE.PRESS){
            pressProcessing = true;
        }
        if(springAnimateType == SPRING_ANIMATE_TYPE.RELEASE){
            releaseProcessing = true;
        }
        if(springAnimateType == SPRING_ANIMATE_TYPE.RESET){
            resetProcessing = true;
        }
        this.player = player;
    }
}
