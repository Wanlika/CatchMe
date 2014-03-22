package sut.game01.core.sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.ImageLayer;
import playn.core.Layer;
import sut.game01.core.screen.GameScreen;
import org.jbox2d.dynamics.Body;
import playn.core.util.Clock;
import tripleplay.game.Screen;

import static playn.core.PlayN.*;

/**
 * Created by Yambok on 22/3/2557.
 */
public class Block extends Screen{

    private boolean hasLoaded=false;
    private ImageLayer block;
    private Body body;
    private boolean contacted;
    private int contactCheck;
    private Body other;

    public Block(World world,float x_px,float y_px,float w_px,float h_px){
        block = graphics().createImageLayer(assets().getImage("images/other/block82-82.png"));
        block.setSize(w_px,h_px);
        block.setOrigin(w_px/2,h_px/2);
        block.setTranslation(x_px,y_px);
        body = createBoxDynamic(world,x_px,y_px,w_px,h_px);

        hasLoaded=true;
    }

    public Layer layer(){
        return block;
    }

    public Body createBoxDynamic(World world,float x_px,float y_px,float w_px,float h_px){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        ///EdgeShape shape = new EdgeShape();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((w_px*GameScreen.M_PER_PIXEL/2),
                (h_px*GameScreen.M_PER_PIXEL/2));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.7f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x_px*GameScreen.M_PER_PIXEL,y_px*GameScreen.M_PER_PIXEL),0f);
        return body;
    }

    public void update(int delta){
        if (!hasLoaded)return;
    }

    public void paint(Clock clock){
        if (!hasLoaded)return;
        block.setTranslation(body.getPosition().x/GameScreen.M_PER_PIXEL,body.getPosition().y/GameScreen.M_PER_PIXEL);
        block.setRotation(body.getAngle());

    }

    public Body getBody(){
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
}
