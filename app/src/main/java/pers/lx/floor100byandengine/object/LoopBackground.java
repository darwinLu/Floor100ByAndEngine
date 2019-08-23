package pers.lx.floor100byandengine.object;

import org.andengine.entity.Entity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import pers.lx.floor100byandengine.manager.ResourcesManager;

public class LoopBackground extends Entity {

    public IAreaShape shape1;
    public IAreaShape shape2;
    private int backgroundAcross = 50;

    public LoopBackground(VertexBufferObjectManager vbom){
        shape1 = new Sprite(0,0,ResourcesManager.getInstance().background_region,vbom);
        shape2 = new Sprite(0,-ResourcesManager.getInstance().background_region.getHeight() + backgroundAcross,ResourcesManager.getInstance().background_region,vbom);
    }

}
