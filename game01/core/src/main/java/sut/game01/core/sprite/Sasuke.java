package sut.game01.core.sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.GameScreen;

/**
 * Created by Yambok on 4/2/2557.
 */
public class Sasuke {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private int time,hp=100;

    private Body bodySasuke;
    private Body other;
    private boolean contacted;
    private int contactCheck;

    public enum State{
        IDLE,KICK,JUMP,HIT
    };

    private State state = State.IDLE;
    private int e =0;
    private int offset =0;
    public Sasuke(final World world,final float x_px, final float y_px){
        this.sprite = SpriteLoader.getSprite("images/sprite/Sasuke.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin((sprite.width()-60f) / 2f, sprite.height() / 2f);
                sprite.layer().addListener(new Pointer.Adapter(){
                    @Override
                    public void onPointerEnd(Pointer.Event event) {
                        state = State.KICK;
                        spriteIndex = -1;
                        e = 0;
                    }
                });

                bodySasuke = initPhysicsBody(world,GameScreen.M_PER_PIXEL*x_px,GameScreen.M_PER_PIXEL*y_px);

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

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
                    state = State.JUMP;
                    spriteIndex = -1;
                    e = 0;
                    bodySasuke.applyForce(new Vec2(-10f,-500f),bodySasuke.getPosition());
                }
                else if (event.key()==Key.LEFT){
                    state = State.HIT;
                    spriteIndex = -1;
                    e = 0;
                    bodySasuke.applyLinearImpulse(new Vec2(-50f, 0f), bodySasuke.getPosition());

                }
                else if(event.key()==Key.RIGHT){
                    state = State.HIT;
                    spriteIndex = -1;
                    e=0;
                    bodySasuke.applyLinearImpulse(new Vec2(50f,0f),bodySasuke.getPosition());
                }
            }
        });

    }

    ///////create box in the world
    private Body initPhysicsBody(World world,float x,float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        ///EdgeShape shape = new EdgeShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.layer().width()*GameScreen.M_PER_PIXEL/2)-1.5f,
                (sprite.layer().height())*GameScreen.M_PER_PIXEL/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);

        return body;
    }
    ///////////////////

    public void contact(Contact contact){
        contacted = true;
        contactCheck = 0;
        if (state==State.IDLE){
            state = State.HIT;
        }
        if (contact.getFixtureA().getBody()==bodySasuke){
            other = contact.getFixtureB().getBody();
        }else {
            other = contact.getFixtureA().getBody();
        }
    }

    public void update(int delta){
        if (!hasLoaded) return;
        e+=delta;
        if (e > 150){
            switch (state){
                case IDLE: offset =0;

                    break;
                case KICK: offset =4;
                    if (spriteIndex==6){
                        state = State.IDLE;
                    }
                    break;
                case JUMP: offset =8;
                    if (spriteIndex==10){
                        state = State.IDLE;
                    }
                    break;
                case HIT: offset=12;
                    if (spriteIndex==14){
                        state = State.IDLE;
                    }
                    break;
            }
            spriteIndex = offset + ((spriteIndex+1)%4);
            sprite.setSprite(spriteIndex);
            e=0;

        }

    }

    public void paint(Clock clock) {
        if (!hasLoaded)
            return;

        sprite.layer().setTranslation((bodySasuke.getPosition().x/GameScreen.M_PER_PIXEL),
                (bodySasuke.getPosition().y/GameScreen.M_PER_PIXEL));

        sprite.layer().setRotation(bodySasuke.getAngle());

    }

    public Layer layer(){
        return sprite.layer();
    }

    public Body getBody(){
        return this.bodySasuke;
    }
}
