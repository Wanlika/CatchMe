package sut.game01.core.sprite;

import playn.core.*;
import playn.core.util.Callback;

/**
 * Created by Yambok on 4/2/2557.
 */
public class Sasuke {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private int time,hp=100;

    public enum State{
        IDLE,KICK,JUMP,HIT
    };

    private State state = State.IDLE;
    private int e =0;
    private int offset =0;
    public Sasuke(final float x, final float y){
        this.sprite = SpriteLoader.getSprite("images/sprite/Sasuke.json");
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
                state = State.KICK;
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
                    state = State.JUMP;
                    spriteIndex = -1;
                    e = 0;
                }
                else if (event.key()==Key.X){
                    state = State.HIT;
                    spriteIndex = -1;
                    e = 0;
                }

            }

        });

    }

    public void update(int delta,float x,float y){
//        if(!hasLoaded) return;
//        e+=delta;
//        time+=delta;
//        if (time>2000&&time<4000){
//            hp=50;
//        }
//        if (time>=4500){
//            hp=0;
//        }
//        if(e>250){
//            if (hp==100){
//                offset = 4;
//            }
//            if (hp==50){
//                offset = 8;
//            }
//            if (hp==0){
//                offset=12;
//            }
//            spriteIndex = offset + ((spriteIndex+1)%4);
//            sprite.setSprite(spriteIndex);
//            e=0;
//        }

        if (!hasLoaded) return;
        e+=delta;
        if (e > 150){
            switch (state){
                case IDLE: offset =0;
                    sprite.layer().setTranslation(x,y);
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
    public Layer layer(){
        return sprite.layer();
    }
}
