package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.debug.DebugDrawBox2D;
import sut.game01.core.sprite.GingerBread;
import sut.game01.core.sprite.Mice;
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
    private boolean showDebugDraw=false;
    private Mice m;
    ////////////////////////////

    @Override
    public void wasAdded(){
        super.wasAdded();

        //add bg and back button
        Image bgImage = assets().getImage("images/other/bgRoom.png");
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

        createWorld();

        //add character
        s = new Sasuke(world,200,16/M_PER_PIXEL);
        layer.add(s.layer());
        g = new GingerBread(world,350f,16f/M_PER_PIXEL);
        layer.add(g.layer());

        m = new Mice(100f,100f);
        ////////////////////////////////////

        PlayN.keyboard().setListener(new Keyboard.Listener() {
            @Override
            public void onKeyDown(Keyboard.Event event) {

            }

            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {

            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key()== Key.UP){
                    s.getBody().applyForce(new Vec2(-10f, -500f), s.getBody().getPosition());
                }
                else if (event.key()==Key.LEFT){

                    s.getBody().applyLinearImpulse(new Vec2(-50f, 0f), s.getBody().getPosition());

                }
                else if(event.key()==Key.RIGHT){

                    s.getBody().applyLinearImpulse(new Vec2(50f, 0f), s.getBody().getPosition());
                }

                if (event.key()== Key.S){

                   g.getBody().applyForce(new Vec2(-20f, -500f), g.getBody().getPosition());
                }
                else if (event.key()==Key.Z){

                    g.getBody().applyLinearImpulse(new Vec2(-50f, 0f), g.getBody().getPosition());
                }
                else if (event.key()==Key.C){

                    g.getBody().applyLinearImpulse(new Vec2(50f, 0f), g.getBody().getPosition());
                }
            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        s.update(delta);
        g.update(delta);
        world.step(0.033f,10,10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        s.paint(clock);
        g.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }

    public GameScreen(ScreenStack ss){
        this.ss = ss;
    }

    public void createWorld(){
        //create world
        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity,true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getBody()==g.getBody()||
                        contact.getFixtureB().getBody()==g.getBody()){
                    g.contact(contact);

                }
                if (contact.getFixtureA().getBody()==s.getBody()||
                        contact.getFixtureB().getBody()==s.getBody()){
                    s.contact(contact);

                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });
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
        groundShape.setAsEdge(new Vec2(1f,0f),new Vec2(1f,18f));
        ground.createFixture(groundShape,0f);
        groundShape.setAsEdge(new Vec2(23f,0f),new Vec2(23f,18f));
        ground.createFixture(groundShape,0f);

        groundShape.setAsEdge(new Vec2(0f,0f),new Vec2(24f,0f));
        ground.createFixture(groundShape,0f);
    }
}