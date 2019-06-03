package pers.lx.floor100byandengine.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import pers.lx.floor100byandengine.base.BaseScene;
import pers.lx.floor100byandengine.manager.ResourcesManager;
import pers.lx.floor100byandengine.manager.SceneManager;

public class SplashScene extends BaseScene {

    private Sprite splash;

    @Override
    public void createScene() {
        this.setBackground(new Background(1,1,1));
        splash = new Sprite(0,0, resourcesManager.splash_region,vbom){
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera) {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };
        splash.setPosition(camera.getCenterX() - splash.getWidth()/2,camera.getCenterY() - splash.getHeight()/2);
        attachChild(splash);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {
        splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }
}
