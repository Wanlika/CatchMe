package sut.game01.core;

import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.util.Callback;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.*;
import playn.core.Image;


/**
 * Created by BkFamily on 21/1/2557.
 */
public class TestScreen extends Screen {
    private final ScreenStack ss;

    @Override
    public void wasAdded(){
        super.wasAdded();
        Image bgImage = assets().getImage("images/bg.png");
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {}

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        Image backbutton = assets().getImage("images/backbutton.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        ImageLayer backLayer = graphics().createImageLayer(backbutton);

        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(TestScreen.this);
            }
        });
        layer.add(bgLayer);
        layer.add(backLayer);


    }

    public TestScreen(ScreenStack ss){
        this.ss = ss;
    }
}