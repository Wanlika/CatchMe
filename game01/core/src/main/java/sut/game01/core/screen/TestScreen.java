package sut.game01.core.screen;

/**
 * Created by Yambok on 15/3/2557.
 */
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.*;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.debug.DebugDrawBox2D;
import sut.game01.core.sprite.Cheese;
import sut.game01.core.sprite.Sprite;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;


import java.util.ArrayList;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.mouse;

public class TestScreen extends Screen{

    private final ScreenStack ss;

    //convert px to meter
    public static float M_PER_PIXEL = 1/26.666667f;//size of world
    private static int width = 24;//640px in physics unit(meter)
    private static int height = 18;//480px in physics unit(meter)

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw=true;
    private Body ground;
    private Cheese cheese;
    private Cheese cheese2;
    private DistanceJointDef the_joint= new DistanceJointDef();
    private Body box1;
    private Body box2;

    public TestScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
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
                ss.remove(TestScreen.this);
            }
        });
        layer.add(bgLayer);
        layer.add(backLayer);

        createWorld();


        box1 = createBox(world,8f,14f,25f,50f);

        cheese = new Cheese(world,100f,150f);
        layer.add(cheese.layer());

        cheese2 = new Cheese(world,200f,150f);
        layer.add(cheese2.layer());
        box2 = createBox(world,10f,14f,25f,150f);

//        the_joint.initialize(cheese.getBody(), cheese2.getBody(), new Vec2(cheese.getBody().getPosition().x,
//                cheese.getBody().getPosition().y), new Vec2(cheese2.getBody().getPosition().x,
//                cheese2.getBody().getPosition().y));
//        the_joint.collideConnected = true;
//        DistanceJoint joint = (DistanceJoint)world.createJoint(the_joint);

//        the_joint.initialize(box1, box2, new Vec2(box1.getPosition().x,
//                box1.getPosition().y), new Vec2(box2.getPosition().x,box2.getPosition().y));
//        the_joint.collideConnected = true;
//        DistanceJoint joint = (DistanceJoint)world.createJoint(the_joint);


    }
    public void createWorld(){
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
        addWall();
        ////create ground

    }
    public Body addWall(){
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
        bodyDef.type = BodyType.DYNAMIC;
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

    @Override
    public void update(int delta) {
        super.update(delta);
        cheese.update(delta);
        cheese2.update(delta);
        world.step(0.033f,10,10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        cheese.paint(clock);
        cheese2.paint(clock);
        if (showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
