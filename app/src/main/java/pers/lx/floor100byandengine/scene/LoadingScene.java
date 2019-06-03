package pers.lx.floor100byandengine.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.base.BaseScene;
import pers.lx.floor100byandengine.manager.SceneManager;

public class LoadingScene extends BaseScene {

    @Override
    public void createScene() {
        setBackground(new Background(Color.WHITE));
        attachChild(new Text(camera.getCenterX(),camera.getCenterY(),resourcesManager.font,"Loading...",vbom));
    }

    @Override
    public void onBackKeyPressed() {
        return;
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene() {

    }

}
