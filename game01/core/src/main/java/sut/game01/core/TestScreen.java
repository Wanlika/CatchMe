package sut.game01.core;

import playn.core.ImageLayer;
import playn.core.PlayN;
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
        Image back = assets().getImage("images/backbutton.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);


    }

    public TestScreen(ScreenStack ss){
        this.ss = ss;
    }
}