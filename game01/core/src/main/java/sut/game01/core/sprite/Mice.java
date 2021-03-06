package sut.game01.core.sprite;

import org.jbox2d.dynamics.Body;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.GameScreen;
/**
 * Created by Yambok on 3/3/2557.
 */
public class Mice {
    private Sprite sprite;
    private int spriteIndex=0;
    private boolean hasLoaded = false;

    public Body body;
    private Body other;
    private boolean contacted;
    private int contactCheck;
    private float y;
    private float x;
    private int n;

    public enum State{
        IDLE,RUN,THROW
    };

    private State state = State.IDLE;
    private int e=0;
    private int offset=0;

    public Mice(final float x_px,final float y_px){
        this.sprite = SpriteLoader.getSprite("images/sprite/mice.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin((sprite.width()) / 2f, (sprite.height()) / 2f);

                sprite.layer().setTranslation(x_px, y_px);
                hasLoaded = true;
                y = y_px;
                x = x_px;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
    }

    public Layer layer(){
        return sprite.layer();
    }

    public void update(int delta){

        if (!hasLoaded) return;

        e+=delta;
        if (e > 150){
            switch (state){
                case IDLE: offset=0;
                    spriteIndex=-1;
                    break;
                case THROW:offset=2;
                    state = State.IDLE;
                    break;
                case RUN: offset =4;
                    n = n+60;
                    break;

            }
            spriteIndex = offset + ((spriteIndex+1)%2);
            sprite.setSprite(spriteIndex);
            e=0;

        }
    } 
    public void micethrow(){
        state = State.THROW;
        spriteIndex = 0;

    }

    public void run(){
        state = State.RUN;
        spriteIndex = -1;
        sprite.layer().setSize(120f,80f);
    }
    public void paint(Clock clock) {
        if (!hasLoaded)return;
        if (state==State.RUN){
            if ((x+n)>=640+sprite.layer().width()){
                sprite.layer().setTranslation(x,y);
                offset = 0;
                spriteIndex=-1;
                n = 0;
            }
            else {
                sprite.layer().setTranslation(x+n,y);
            }
        }

    }

    public Body getBody(){
        return this.body;
    }
}

