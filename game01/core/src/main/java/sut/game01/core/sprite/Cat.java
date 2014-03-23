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
public class Cat {
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
    private Sound sc = assets().getSound("sounds/cat");

    public enum State{
        HIT,RUN
    };

    private State state = State.RUN;
    private int e=0;
    private int offset=0;

    public Cat(final float x_px,final float y_px){
        this.sprite = SpriteLoader.getSprite("images/sprite/Cat.json");
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
        if (!sc.isPlaying()){
            sc.play();
        }
        e+=delta;
        if (e > 150){
            switch (state){
                case RUN:offset=0;
                    n+=60;
                    break;
                case HIT: offset =0;
                    n = n+50;

                    break;

            }
            spriteIndex = offset + ((spriteIndex+1)%2);
            sprite.setSprite(spriteIndex);
            e=0;

        }
    }

    public void paint(Clock clock) {
        if (!hasLoaded)return;
        if (state == State.RUN){
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

    public void run(){
        state = State.RUN;
        spriteIndex=-1;
        sprite.layer().setSize(100f,140f);
    }
}
