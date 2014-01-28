package sut.game01.core.sprite;

import playn.core.Layer;
import playn.core.Pointer;
import playn.core.util.Callback;
import playn.core.*;

/**
 * Created by all user on 27/1/2557.
 */
public class Mytoon {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;

   public enum State{
        IDLE,ATTK,KICK,WITCH
   };

    private State state = State.IDLE;
    private int e =0;
    private int offset =0;
    public Mytoon(final float x, final float y){
        this.sprite = SpriteLoader.getSprite("images/sprite/sprite.json");
        this.sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                sprite.layer().setTranslation(x, y);

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        sprite.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                state = State.ATTK;
                spriteIndex = -1;
                e = 0;
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
                if(event.key()== Key.Z){
                    state = State.KICK;
                    spriteIndex = -1;
                    e = 0;
                }
                else if (event.key()==Key.X){
                    state = State.WITCH;
                    spriteIndex = -1;
                    e = 0;
                }

            }

        });

    }

    public void update(int delta){
        if (!hasLoaded) return;
        e+=delta;
        if (e > 150){
            switch (state){
                case IDLE: offset =0;
                    break;
                case ATTK: offset =4;
                    if (spriteIndex==6){
                        state = State.IDLE;
                    }
                    break;
                case KICK: offset =8;
                    if (spriteIndex==10){
                        state = State.IDLE;
                    }
                    break;
                case WITCH: offset=12;
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
    public Layer layer(){
        return sprite.layer();
    }
}
