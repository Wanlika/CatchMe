package sut.game01.core.screen;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.debug.DebugDrawBox2D;
import sut.game01.core.sprite.GingerBread;
import sut.game01.core.sprite.Sasuke;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.*;


import tripleplay.ui.*;


/**
 * Created by BkFamily on 21/1/2557.
 */
public class GameScreen extends Screen {
    private final ScreenStack ss;
    private Root root;
    private Sasuke s;
    private GingerBread g;
    private float x,y;

    //convert px to meter
    public static float M_PER_PIXEL = 1/26.666667f;//size of world
    private static int width = 24;//640px in physics unit(meter)
    private static int height = 18;//480px in physics unit(meter)

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw=true;
    ////////////////////////////

    @Override
    public void wasAdded(){
        super.wasAdded();

        //add bg and back button
        Image bgImage = assets().getImage("images/other/bg.png");
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {}

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        Image backImage = assets().getImage("images/other/backbutton.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);
        backLayer.setSize(100,100);
        backLayer.setOrigin(backLayer.width()/2,backLayer.height()/2);
        backLayer.setTranslation(50,50);
        backLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(GameScreen.this);

            }
        });
        layer.add(bgLayer);
        layer.add(backLayer);
        /////////////////////////////////////

        //create world
        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity,true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        ////////////////////////////////////

        // set up debug
        if (showDebugDraw){
            CanvasImage image = graphics().createImage(640,480);
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setStrokeWidth(2f);
            debugDraw.setFillAlpha(75);
            debugDraw.setFlags(DebugDraw.e_shapeBit|DebugDraw.e_jointBit|DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,26.66667f);
            world.setDebugDraw(debugDraw);

        }
        ////////////

        ////create ground
        Body ground = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsEdge(new Vec2(0f,16f),new Vec2(24f,16f));
        ground.createFixture(groundShape,0f);
        //////////////

        //add character
        s = new Sasuke(320f,240f);
        layer.add(s.layer());

        g = new GingerBread(world,200f,150f);
        layer.add(g.layer());
        ////////////////////////////////////
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        s.update(delta,320f,240f);
        g.update(delta,200f,150f);
        world.step(0.033f,10,10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }

    public GameScreen(ScreenStack ss){
        this.ss = ss;
    }
}