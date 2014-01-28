package sut.game01.core.screen;

import playn.core.*;
import playn.core.util.Callback;
import sut.game01.core.sprite.Mytoon;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.*;


import tripleplay.game.UIScreen;
import tripleplay.ui.*;


/**
 * Created by BkFamily on 21/1/2557.
 */
public class TestScreen extends UIScreen {
    private final ScreenStack ss;
    private Root root;
    private Mytoon m;


    @Override
    public void wasAdded(){
        super.wasAdded();
        Image bgImage = assets().getImage("images/other/bg.png");
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {}

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);


        Image backImage = assets().getImage("images/other/backbutton.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);

        backLayer.setSize(100,100);
        backLayer.setOrigin(backLayer.width()/2,backLayer.height()/2);
        backLayer.setTranslation(50,50);
        backLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(TestScreen.this);

            }
        });

        layer.add(bgLayer);
        layer.add(backLayer);
         m = new Mytoon(320f,240f);
        layer.add(m.layer());
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        m.update(delta);
    }



    public TestScreen(ScreenStack ss){
        this.ss = ss;
    }
}