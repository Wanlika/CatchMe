package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
//import org.jbox2d.common.Vec2;
import org.jbox2d.common.*;
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

import java.util.ArrayList;


/**
 * Created by BkFamily on 21/1/2557.
 */
public class StateFive extends Screen {
    private final ScreenStack ss;

    private final static int state = 5;
    //convert px to meter
    public static float M_PER_PIXEL = 1/26.666667f;//size of world
    private static int width = 24;//640px in physics unit(meter)
    private static int height = 18;//480px in physics unit(meter)

    //world
    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw=true;
    private boolean hasLoaded = false;
    private boolean win=false;
    private boolean lose=false;
    private Body ground;
    private int point=5;
    private ImageLayer timeLayer;
    private Vase v;
    private Oldlady l;
    private Mice m;
    private ArrayList<Cheese> cheeses = new ArrayList<Cheese>();
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private int i=0;
    private Sound snore = assets().getSound("sounds/snore");
    private boolean sound=true;


    public StateFive(ScreenStack ss){
        this.ss = ss;
    }

    @Override
    public void wasAdded(){
        super.wasAdded();
        layer.add(graphics().createImageLayer(assets().getImage("images/other/bgRoom.png")));
        ImageLayer backLayer = graphics().createImageLayer(assets().getImage("images/other/backbutton.png"));
        backLayer.setSize(50, 50);
        backLayer.setOrigin(25,25).setTranslation(50f, 30);
        backLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                //ss.remove(GameScreen.this);
                snore.stop();
                ss.push(new HomeScreen(ss));
            }
        });
        layer.add(backLayer);

        timeLayer = graphics().createImageLayer(assets().getImage("images/other/bar.png"));
        timeLayer.setSize(200,40);
        layer.add(timeLayer.setTranslation(150f, 10f));
        layer.add(graphics().createImageLayer(assets().getImage("images/other/Time-icon.png")).setTranslation(100f,10f));

        ImageLayer cheeseLayer = graphics().createImageLayer(assets().getImage("images/sprite/cheese.png"));
        cheeseLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                if (i < 3) {
                    cheeses.add(new Cheese(world, 120f, 16f / M_PER_PIXEL));
                    layer.add(cheeses.get(i).layer());
                    i++;
                    point-=5;
                }
            }
        });
        layer.add(cheeseLayer.setOrigin(26,21).setTranslation(400,30));

        ImageLayer speaker = graphics().createImageLayer(assets().getImage("images/other/soundbutton.png"));

        speaker.setOrigin(25f,25f).setTranslation(470f, 30f);
        speaker.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                if (sound==true){
                    sound=false;
                }else if (sound==false){
                    sound=true;
                }
            }
        });
        layer.add(speaker);

        ImageLayer replay = graphics().createImageLayer(assets().getImage("images/other/replaybutton.png"));

        replay.setOrigin(25f,25f).setTranslation(530f, 30f);
        replay.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                snore.stop();
                ss.push(new StateFive(ss));
            }
        });
        layer.add(replay);

        layer.add(graphics().createImageLayer(assets().getImage("images/other/smalltable.png"))
                .setOrigin(84/2,118/2).setTranslation(500f,14f/M_PER_PIXEL));

        createWorld();

        createBox(world, 500f * M_PER_PIXEL, 14f, 84, 110);//table

        blocks.add(new Block(world,400f,400f,50f,50f));
        blocks.add(new Block(world,400f,350f,50f,50f));
        blocks.add(new Block(world,400f,300f,50f,50f));
        blocks.add(new Block(world,400f,250f,50f,50f));
        blocks.add(new Block(world,400f,200f,50f,50f));

        blocks.add(new Block(world,600f,400f,50f,50f));
        blocks.add(new Block(world,600f,350f,50f,50f));
        blocks.add(new Block(world,600f,300f,50f,50f));
        blocks.add(new Block(world,600f,250f,50f,50f));
        blocks.add(new Block(world,600f,200f,50f,50f));

        blocks.add(new Block(world,480f,180f,250f,20f));

        //blocks.add(new Block(world,475f,250f,150f,20f));
        for (Block nb:blocks){
            layer.add(nb.layer());
        }

        m = new Mice(70f,15f/M_PER_PIXEL);
        layer.add(m.layer());

        v = new Vase(world,500f,12f/M_PER_PIXEL);
        layer.add(v.layer());

        l = new Oldlady(100f,10f/M_PER_PIXEL);
        layer.add(l.layer());

        pointer().setListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                m.micethrow();
            }
        });

        hasLoaded=true;
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (!hasLoaded)return;
        if (sound){
            if (!snore.isPlaying()){
                snore.play();
            }
        }else if (!sound){
            snore.stop();
        }
        for (Block nb: blocks){
            nb.update(delta);
        }
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
        if (!hasLoaded)return;
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
        ///time out
        if(timeLayer.width()>=1f){

            timeLayer.setSize(timeLayer.width() - (0.1f), timeLayer.height());
            for (Block nb: blocks){
                nb.paint(clock);
            }
            for (Cheese nc : cheeses){
                nc.paint(clock);

            }
            v.paint(clock);
            m.paint(clock);
            l.paint(clock);
        }else if (timeLayer.width()<1f){
            snore.stop();
            win=false;
            lose=true;
            ss.push(new WinLoseScreen(ss,win,lose,state,point));
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
            }

            @Override
            public void endContact(Contact contact) {
                if (!hasLoaded)return;
                for (Cheese nc:cheeses){
                    if ((contact.getFixtureA().getBody()==nc.getBody())&&(contact.getFixtureB().getBody()==v.getBody())){
                        point=point+(int)timeLayer.width();
                        System.out.println("Win point : " + point);
                        snore.stop();
                        v.contact(contact);
                        win=true;
                        ss.push(new WinLoseScreen(ss,win,lose,state,point));
                    }else if ((contact.getFixtureA().getBody()==v.getBody())&&
                            (contact.getFixtureB().getBody()==nc.getBody())){
                        point=point+(int)timeLayer.width();
                        System.out.println("Win point : " + point);
                        snore.stop();
                        v.contact(contact);
                        win=true;
                        ss.push(new WinLoseScreen(ss,win,lose,state,point));
                    }
                    if ((contact.getFixtureA().getBody()==nc.getBody())&&(contact.getFixtureB().getBody()!=ground)){
                        nc.contact(contact);
                        System.out.println("Cheese contracted");
                    }else if ((contact.getFixtureA().getBody()!=ground)&&(contact.getFixtureB().getBody()==nc.getBody())){
                        nc.contact(contact);
                        System.out.println("Cheese contracted");
                    }
                }

                for (Block nb:blocks){
                    if ((contact.getFixtureA().getBody()==nb.getBody())||(contact.getFixtureB().getBody()==nb.getBody())){
                        point+=1;
                        System.out.println("Boom Point : "+point);
                    }
                    if ((contact.getFixtureA().getBody()==nb.getBody())&&(contact.getFixtureB().getBody()==v.getBody())){
                        point=point+(int)timeLayer.width();
                        System.out.println("Win point : "+point);
                        snore.stop();
                        v.contact(contact);
                        win=true;
                        ss.push(new WinLoseScreen(ss,win,lose,state,point));
                    }else if ((contact.getFixtureA().getBody()==v.getBody())&&(contact.getFixtureB().getBody())==nb.getBody()){
                        point=point+(int)timeLayer.width();
                        System.out.println("Win point : "+point);
                        snore.stop();
                        v.contact(contact);
                        win=true;
                        ss.push(new WinLoseScreen(ss,win,lose,state,point));
                    }
                }
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

        ground = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsEdge(new Vec2(0f,16f),new Vec2(24f,16f));
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