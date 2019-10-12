package pers.lx.floor100byandengine.object;

import android.util.Log;

import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import pers.lx.floor100byandengine.manager.ResourcesManager;

public class ForceBar extends Entity {

    private int max;
    private int current;

    private int width;
    private int height;
    private int column;

    private IAreaShape backShape;
    private IAreaShape frontShape;
    private Text barText;

    private VertexBufferObjectManager vbom;

    private boolean addingForce = false;
    private boolean forceAdded = false;
    private boolean listeningForce = false;
    private float mTimerSecondsElapsed;
    private float mTimerSeconds = 0.1f;

    private HUD hud;

    private Sound powerSound;
    private boolean isPlayingSound = false;

    public ForceBar(int x,int y,int max, int current, int width, int height, int column, VertexBufferObjectManager vbom){
        this.mX = x;
        this.mY = y;
        this.max = max;
        this.current = current;
        this.width = width;
        this.height = height;
        this.column = column;
        this.vbom = vbom;
        createHUD();
        powerSound = ResourcesManager.getInstance().power_sound;
    }

    private void createHUD() {
        backShape = new Rectangle(mX,mY,width,height,vbom);
        backShape.setColor(Color.GREEN);
        frontShape = new Rectangle(mX,mY,width/column*current,height,vbom);
        frontShape.setColor(Color.YELLOW);
        barText = new Text(mX,mY , ResourcesManager.getInstance().font,"力量",vbom);
        barText.setPosition(mX - barText.getWidth(),mY);
    }

    public void attachToHUD(HUD hud) {
        this.hud = hud;
        hud.attachChild(backShape);
        hud.attachChild(frontShape);
        hud.attachChild(this);
        hud.attachChild(barText);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        super.onManagedUpdate(pSecondsElapsed);
        if(listeningForce){
            if(addingForce){
                if(!isPlayingSound){
                    powerSound.setLooping(true);
                    powerSound.play();
                    isPlayingSound = true;
                }
                this.mTimerSecondsElapsed += pSecondsElapsed;
                if(this.mTimerSecondsElapsed >= this.mTimerSeconds) {
                    this.forceAdded = true;
                    if(current<10){
                        current++;
                    }
                    else {
                        current = 0;
                    }
                    hud.detachChild(frontShape);
                    frontShape.dispose();
                    frontShape = null;
                    frontShape = new Rectangle(mX,mY,width/column*current,height,vbom);
                    frontShape.setColor(Color.RED);
                    hud.attachChild(frontShape);
                    this.forceAdded = false;
                    this.mTimerSecondsElapsed = 0;
                }
            }
        }
    }

    public void resetForceBar(){
        current = 0;
        hud.detachChild(frontShape);
    }


    public void startAddForce() {
        addingForce = true;

    }

    public void stopAddForce() {
        addingForce = false;
        powerSound.stop();
        isPlayingSound = false;
    }

    public int getPower() {
        return current;
    }

    public void startListenForce() {
        listeningForce = true;
    }

    public void stopListenForce() {
        listeningForce = false;
    }
}
