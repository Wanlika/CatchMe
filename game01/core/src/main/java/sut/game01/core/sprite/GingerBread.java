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
public class GingerBread {
    private Sprite sprite;
    private int spriteIndex=0;
    private boolean hasLoaded = false;

    public Body body;
    private Body other;
    private boolean contacted;
    private int contactCheck;
    private int hp;


    public enum State{
        IDLE,WALK,ATK,DIE
    };

    private State state = State.IDLE;
    private int e=0;
    private int offset=0;

    public GingerBread(final World world,final float x_px,final float y_px){
        this.sprite = SpriteLoader.getSprite("images/sprite/Gingerbread.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin((sprite.width())/2f,(sprite.height()+40)/2f);
                sprite.layer().addListener(new Pointer.Adapter(){
                    @Override
                    public void onPointerEnd(Pointer.Event event) {
                        state = State.ATK;
                        spriteIndex = -1;
                        e = 0;
                    }
                });

                ////////create body after sprite loaded
                body = initPhysicsBody(world,GameScreen.M_PER_PIXEL*x_px,GameScreen.M_PER_PIXEL*y_px);
                /////////////

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
        shape.setAsBox((sprite.layer().width()*GameScreen.M_PER_PIXEL/2)-2f,
                (sprite.layer().height()*GameScreen.M_PER_PIXEL/2)-1.5f);
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
            state = State.ATK;
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
                case WALK: offset =4;
                    if (spriteIndex==6){
                        state = State.IDLE;
                    }
                    break;
                case ATK: offset =8;
                    if (spriteIndex==10){
                        state = State.IDLE;
                    }
                    break;
                case DIE: offset=12;
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

        sprite.layer().setTranslation((body.getPosition().x/GameScreen.M_PER_PIXEL),
                (body.getPosition().y/GameScreen.M_PER_PIXEL));
        sprite.layer().setRotation(body.getAngle());
    }

    public Body getBody(){
        return this.body;
    }
}
