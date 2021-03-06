package sut.game01.core.sprite;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.*;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.GameScreen;


import static playn.core.PlayN.*;

public class Cheese {

    private Sprite sprite;
    private int spriteIndex=0;
    private boolean hasLoaded = false;

    public Body body;
    private Body other;
    private boolean contacted=false;
    private int contactCheck;
    private float x_start;
    private float y_start;
    private float x_end;
    private float y_end;
    private int time=0;
    private MouseJoint mouseJoint;
    private float startpointx;
    private float startpointy;
    private Body box;


    public enum State{
        IDLE
    };

    private State state = State.IDLE;
    private int e=0;
    private int offset=0;

    public Cheese(final World world,final float x_px,final float y_px){
        this.sprite = SpriteLoader.getSprite("images/sprite/Cheese.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin((sprite.width()) / 2f, (sprite.height()) / 2f);

                body = initPhysicsBody(world, x_px * GameScreen.M_PER_PIXEL, y_px * GameScreen.M_PER_PIXEL);

                hasLoaded = true;

            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
//        sprite.layer().addListener(new Pointer.Adapter() {
//            @Override
//            public void onPointerStart(Pointer.Event event) {
//
//                x_start = event.localX();
//                y_start = event.localY();
//
//            }
//
//            @Override
//            public void onPointerEnd(Pointer.Event event) {
//                x_end = event.localX();
//                y_end = event.localY();
//                body.applyForce(new Vec2((x_start - x_end) * (x_start - x_end) / 6, (y_start - y_end) * (y_start - y_end) / 4), body.getPosition());
//            }
//
//        });

        sprite.layer().addListener(new Pointer.Listener() {
            @Override
            public void onPointerStart(Pointer.Event event) {
//                MouseJointDef mouseJointDef =new MouseJointDef();
////                mouseJointDef.bodyA= world.getBodyList();
//                mouseJointDef.bodyA= box;
//                mouseJointDef.bodyB=body;
//                mouseJointDef.target.set(event.x(), event.y());
//                mouseJointDef.maxForce=100;
//
//                mouseJoint=(MouseJoint)world.createJoint(mouseJointDef);
//                startpointx = event.x();
//                startpointy = event.y();
                x_start = event.localX();
                y_start = event.localY();

            }

            @Override
            public void onPointerEnd(Pointer.Event event) {
//                world.destroyJoint(mouseJoint);
//                mouseJoint=null;
                x_end = event.localX();
                y_end = event.localY();
                //body.applyForce(new Vec2((x_start - x_end) * (x_start - x_end) / 6, (y_start - y_end) * (y_start - y_end) / 4), body.getPosition());
                body.applyLinearImpulse(new Vec2((x_start - x_end) / 6, (y_start - y_end) / 6), body.getPosition());
            }

            @Override
            public void onPointerDrag(Pointer.Event event) {
               // mouseJoint.setTarget(new Vec2(event.x(),event.y()));
            }

            @Override
            public void onPointerCancel(Pointer.Event event) {

            }
        });
    }

    public Layer layer(){
        return sprite.layer();
    }

    ///////create box in the world
    private Body initPhysicsBody(World world,float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        ///EdgeShape shape = new EdgeShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.layer().width()*GameScreen.M_PER_PIXEL/2),
                (sprite.layer().height()*GameScreen.M_PER_PIXEL/2));
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

    private Body createBox(World world,float x,float y,float w,float h){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        ///EdgeShape shape = new EdgeShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w*GameScreen.M_PER_PIXEL/2,
                h*GameScreen.M_PER_PIXEL/2);
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
    
    public void contact(Contact contact){
        contacted = true;
        contactCheck = 0;

        if (contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        }else {
            other = contact.getFixtureA().getBody();
        }
    }

    public void update(int delta){
        if (!hasLoaded) return;
        e+=delta;
        if (contacted==true){
            time+=20;
        }
        if (e > 150){
            switch (state){
                case IDLE: offset =0;

                    break;
            }
            spriteIndex = offset + ((spriteIndex+1)%1);
            sprite.setSprite(spriteIndex);
            e=0;
        }
    }

    public void paint(Clock clock) {
        if (!hasLoaded)return;
        sprite.layer().setTranslation((body.getPosition().x/ GameScreen.M_PER_PIXEL),
                (body.getPosition().y/GameScreen.M_PER_PIXEL));
        sprite.layer().setRotation(body.getAngle());
        if (time>=2500){
            sprite.layer().destroy();
            body.getWorld().destroyBody(body);
        }
    }

    public Body getBody(){
        return this.body;
    }
}
