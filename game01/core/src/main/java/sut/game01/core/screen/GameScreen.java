package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.debug.DebugDrawBox2D;
import sut.game01.core.sprite.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.*;

import tripleplay.ui.*;


/**
 * Created by BkFamily on 21/1/2557.
 */
public class GameScreen extends Screen {
    private final ScreenStack ss;
    //convert px to meter
    public static float M_PER_PIXEL = 1/26.666667f;//size of world
    private static int width = 24;//640px in physics unit(meter)
    private static int height = 18;//480px in physics unit(meter)

    //world
    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw=false;
    private Body ground;


    private float x_start;
    private float x_end;
    private float y_start;
    private float y_end;
    private ImageLayer powerLayer;
    private Image powerbar;
    private ImageLayer tableLayer;
    private Image tableImage;
    private Cheese c;
    private Vase v;


    @Override
    public void wasAdded(){
        super.wasAdded();


        Image bgImage = assets().getImage("images/other/bgRoom.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        Image backImage = assets().getImage("images/other/backbutton.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);
        backLayer.setSize(100, 100);
        backLayer.setOrigin(backLayer.width() / 2, backLayer.height() / 2);
        backLayer.setTranslation(50, 50);
        backLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(GameScreen.this);

            }
        });
        layer.add(bgLayer);
        layer.add(backLayer);
        powerbar = assets().getImage("images/other/powerbar.png");
        powerLayer = graphics().createImageLayer(powerbar);
        powerLayer.setOrigin(powerbar.width()/2,powerbar.height()/2);
        powerLayer.setTranslation(320f, 10f);
        layer.add(powerLayer);

        tableImage = assets().getImage("images/other/smalltable.png");
        tableLayer = graphics().createImageLayer(tableImage);
        tableLayer.setOrigin(84/2,118/2);
        tableLayer.setTranslation(500f,14f/M_PER_PIXEL);
        layer.add(tableLayer);

        createWorld();
        createBox(world,500f*M_PER_PIXEL,14f);
        //add character

        c = new Cheese(world,100f,16f/M_PER_PIXEL);
        layer.add(c.layer());

        v = new Vase(world,500f,12f/M_PER_PIXEL);
        layer.add(v.layer());
        ////////////////////////////////////

        pointer().setListener(new Pointer.Listener() {
            @Override
            public void onPointerStart(Pointer.Event event) {
                x_start = event.localX();
                y_start = event.localY();
            }

            @Override
            public void onPointerEnd(Pointer.Event event) {
                x_end = event.localX();
                y_end = event.localY();
                powerLayer.setSize(powerbar.width(),powerbar.height());
                c.getBody().applyLinearImpulse(new Vec2((x_start - x_end)/6, (y_start - y_end)/6), c.getBody().getPosition());

            }

            @Override
            public void onPointerDrag(Pointer.Event event) {

                powerLayer.setSize(powerbar.width()+((powerLayer.width()/event.localX())),powerbar.height());


            }

            @Override
            public void onPointerCancel(Pointer.Event event) {

            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        c.update(delta);
        v.update(delta);
        world.step(0.033f,10,10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        c.paint(clock);
        v.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }

    public GameScreen(ScreenStack ss){
        this.ss = ss;
    }

    public Body createWorld(){
        //create world
        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity,true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if ((contact.getFixtureA().getBody()==c.getBody())&&(contact.getFixtureB().getBody()==v.getBody())){
                    v.contact(contact);
                    c.contact(contact);
                }else if ((contact.getFixtureA().getBody()==v.getBody())&&
                        (contact.getFixtureB().getBody()==c.getBody())){
                    v.contact(contact);
                    c.contact(contact);
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
        ground = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        //ground
        groundShape.setAsEdge(new Vec2(0f,16f),new Vec2(24f,16f));
        ground.createFixture(groundShape,0f);
        //left
        groundShape.setAsEdge(new Vec2(1f,0f),new Vec2(1f,18f));
        ground.createFixture(groundShape,0f);
        //right
        groundShape.setAsEdge(new Vec2(23f,0f),new Vec2(23f,18f));
        ground.createFixture(groundShape,0f);
        //top
        groundShape.setAsEdge(new Vec2(0f,0f),new Vec2(24f,0f));
        ground.createFixture(groundShape,0f);
        return ground;
    }

    public Body createBox(World world,float x,float y){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        ///EdgeShape shape = new EdgeShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((84*GameScreen.M_PER_PIXEL/2),
                (118*GameScreen.M_PER_PIXEL/2));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);
        return body;
    }
}