package sut.game01.core.sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Graphics;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.GameScreen;
import static playn.core.PlayN.*;
/**
 * Created by Yambok on 8/3/2557.
 */
public class Vase {
    private Sprite sprite;
    private int spriteIndex=0;
    private boolean hasLoaded = false;

    public Body body;
    private Body other;
    private boolean contacted;
    private int contactCheck;
    private java.awt.Graphics g;

    public enum State{
        IDLE,BROKE
    };

    private State state = State.IDLE;
    private int e=0;
    private int offset=0;

    public Vase(final World world,final float x_px,final float y_px){
        this.sprite = SpriteLoader.getSprite("images/sprite/smallvase.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin((sprite.width()) / 2f, (sprite.height()) / 2f);

                body = initPhysicsBody(world,x_px* GameScreen.M_PER_PIXEL,y_px*GameScreen.M_PER_PIXEL);
                hasLoaded = true;


            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!",cause);
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
    ///////////////////

    public void contact(Contact contact){
        contacted = true;
        contactCheck = 0;
        if (state==State.IDLE){
            state = State.BROKE;
            spriteIndex = -1;
            e=0;
        }
        if (contact.getFixtureA().getBody()==body){
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
                case BROKE:offset=2;
                    if (spriteIndex==(-1)){
                    }else {
                        spriteIndex=2;
                    }
                    break;
            }
            spriteIndex = offset + ((spriteIndex+1)%2);
            sprite.setSprite(spriteIndex);
            e=0;
        }
    }

    public void paint(Clock clock) {
        if (!hasLoaded)return;
        sprite.layer().setTranslation((body.getPosition().x/ GameScreen.M_PER_PIXEL),
                (body.getPosition().y/GameScreen.M_PER_PIXEL));
        sprite.layer().setRotation(body.getAngle());

    }

    public Body getBody(){
        return this.body;
    }
}
