package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.util.Clock;
import sut.game01.core.debug.DebugDrawBox2D;
import sut.game01.core.sprite.Cheese;
import sut.game01.core.sprite.Mice;
import sut.game01.core.sprite.Oldlady;
import sut.game01.core.sprite.Vase;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;

/**
 * Created by Yambok on 12/3/2557.
 */
public class StateThree extends Screen{
    private final ScreenStack ss;

    public StateThree(ScreenStack ss) {
        this.ss = ss;
    }
    //convert px to meter
    public static float M_PER_PIXEL = 1/26.666667f;//size of world
    private static int width = 24;//640px in physics unit(meter)
    private static int height = 18;//480px in physics unit(meter)

    //world
    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw=true;
    private Body ground;

    private ImageLayer powerLayer;
    private Image powerbar;
    private ImageLayer tableLayer;
    private Image tableImage;
    private Cheese c;
    private Vase v;
    private Oldlady l;
    private Mice m;

    private ArrayList<Cheese> cheeses = new ArrayList<Cheese>();
    private int i=0;

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
                ss.remove(StateThree.this);
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
        tableLayer.setTranslation(500f, 14f / M_PER_PIXEL);
        layer.add(tableLayer);

        createWorld();
        createBox(world,500f*M_PER_PIXEL,14f,84,110);
        createBox(world,250f*M_PER_PIXEL,8f,30,100);
        createBox(world,400f*M_PER_PIXEL,10f,100,30);

        m = new Mice(70f,15f/M_PER_PIXEL);
        layer.add(m.layer());

        v = new Vase(world,500f,12f/M_PER_PIXEL);
        layer.add(v.layer());

        l = new Oldlady(100f,10f/M_PER_PIXEL);
        layer.add(l.layer());
        cheeses.add(new Cheese(world,120f,16f/M_PER_PIXEL));
        for (Cheese nc:cheeses){
            layer.add(nc.layer());
        }

        pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                powerLayer.setSize(powerbar.width(),powerbar.height());
                m.micethrow();
                i+=1;
                if (i<3){
                    cheeses.add(new Cheese(world,120f,16f/M_PER_PIXEL));
                    layer.add(cheeses.get(i).layer());
                }


            }
            @Override
            public void onPointerDrag(Pointer.Event event) {
                powerLayer.setSize(powerbar.width()+((powerLayer.width()/event.localX())),powerbar.height());
            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        for (Cheese nc : cheeses){
            nc.update(delta);
        }
        v.update(delta);
        m.update(delta);
        l.update(delta);
        world.step(0.033f,10,10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        for (Cheese nc : cheeses){
            nc.paint(clock);
        }
        v.paint(clock);
        m.paint(clock);
        l.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
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
                for (Cheese nc:cheeses){
                    if ((contact.getFixtureA().getBody()==nc.getBody())&&(contact.getFixtureB().getBody()==v.getBody())){
                        v.contact(contact);
                        nc.contact(contact);
                    }else if ((contact.getFixtureA().getBody()==v.getBody())&&
                            (contact.getFixtureB().getBody()==nc.getBody())){
                        v.contact(contact);
                        nc.contact(contact);
                    }
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

    public Body createBox(World world,float x,float y,float w,float h){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        ///EdgeShape shape = new EdgeShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((w*GameScreen.M_PER_PIXEL/2),
                (h*GameScreen.M_PER_PIXEL/2));
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
