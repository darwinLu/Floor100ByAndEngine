package pers.lx.floor100byandengine.manager;

import android.graphics.Color;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import pers.lx.floor100byandengine.GameActivity;

public class ResourcesManager {

    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------

    private static final ResourcesManager INSTANCE = new ResourcesManager();

    public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;

    public Font font;

    //---------------------------------------------
    // TEXTURES & TEXTURE REGIONS
    //---------------------------------------------


    private BitmapTextureAtlas splashTextureAtlas;
    public ITextureRegion splash_region;

    private BuildableBitmapTextureAtlas menuTextureAtlas;
    public ITextureRegion menu_background_region;
    public ITextureRegion menu_title_region;
    public ITextureRegion menu_button_region;

    private BuildableBitmapTextureAtlas gameTextureAtlas;
    public ITiledTextureRegion player_region;
    public ITiledTextureRegion normal_lr_ud_platform_region;
    public ITextureRegion floor_region;
    public ITiledTextureRegion roll_platform_region;
    public ITiledTextureRegion spring_platform_region;
    public ITextureRegion background_region;

    //---------------------------------------------
    // CLASS LOGIC
    //---------------------------------------------

    public void loadSplashScreen()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),512,512, TextureOptions.BILINEAR);
        splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas,activity,"logo.png",0,0);
        splashTextureAtlas.load();
    }

    public void unloadSplashScreen()
    {
        splashTextureAtlas.unload();
        splash_region = null;
    }

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuFonts();
        loadMenuAudio();
    }

    private void loadMenuGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024,1024,TextureOptions.BILINEAR);
        menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas,this.activity,"menu_background.png");
        menu_title_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas,this.activity,"menu_title.png");
        menu_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas,this.activity,"menu_button.png");
        try
        {
            this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.menuTextureAtlas.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    private void loadMenuFonts()
    {
        FontFactory.setAssetBasePath("font/");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    private void loadMenuAudio()
    {

    }

    public void loadGameResources() {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

    private void loadGameGraphics()
    {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 10240, 10240, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "robot_run_small.png", 8, 1);
        normal_lr_ud_platform_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas,activity,"normal_lr_ud_platform.png",1,1);
        roll_platform_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "roll_platform.png", 8, 1);
        spring_platform_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "spring_platform.png", 1, 1);
        floor_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity,"floor.png");
        background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas,activity,"background.png");
        try
        {
            this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            this.gameTextureAtlas.load();
        }
        catch (final ITextureAtlasBuilder.TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }

    private void loadGameFonts()
    {

    }

    private void loadGameAudio()
    {

    }

    public void unloadGameTextures()
    {
        // TODO (Since we did not create any textures for game scene yet)
    }

    public void loadMenuTextures()
    {
        menuTextureAtlas.load();
    }

    public void unloadMenuTextures()
    {
        menuTextureAtlas.unload();
    }

    /**
     * @param engine
     * @param activity
     * @param camera
     * @param vbom
     * <br><br>
     * We use this method at beginning of game loading, to prepare Resources Manager properly,
     * setting all needed parameters, so we can latter access them from different classes (eg. scenes)
     */
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom)
    {
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
    }

    //---------------------------------------------
    // GETTERS AND SETTERS
    //---------------------------------------------

    public static ResourcesManager getInstance()
    {
        return INSTANCE;
    }

}
