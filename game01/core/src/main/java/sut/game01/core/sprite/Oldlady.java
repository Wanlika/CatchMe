package sut.game01.core.sprite;

import org.jbox2d.dynamics.Body;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.Sound;
import playn.core.util.Callback;
import playn.core.util.Clock;

import static playn.core.PlayN.assets;

/**
 * Created by Yambok on 9/3/2557.
 */
public class Oldlady {
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
    private Sound run = assets().getSound("sounds/running");
    private boolean sound=false;
    public enum State{
        IDLE,WAKE,RUN
    };

    private State state = State.IDLE;
    private int e=0;
    private int offset=0;

    public Oldlady(final float x_px,final float y_px){
        this.sprite = SpriteLoader.getSprite("images/sprite/Oldlady.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin((sprite.width()) / 2f, (sprite.height()) / 2f);
                sprite.layer().setTranslation(x_px,y_px);
                hasLoaded = true;
                y = y_px;
                x = x_px;

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

    public void update(int delta){

        if (!hasLoaded) return;

        if (sound){
            if (!run.isPlaying()){
                run.play();
            }
        }
        e+=delta;
        if (e > 150){
            switch (state){
                case IDLE:offset=0;
                    break;
                case RUN: offset =4;
                    sound=true;
                    n = n+60;
                    break;

            }
            spriteIndex = offset + ((spriteIndex+1)%2);
            sprite.setSprite(spriteIndex);
            e=0;

        }
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

    public void run(){
        state = State.RUN;
        spriteIndex=-1;
        sprite.layer().setSize(100f,140f);
    }

    public Body getBody(){
        return this.body;
    }
}
