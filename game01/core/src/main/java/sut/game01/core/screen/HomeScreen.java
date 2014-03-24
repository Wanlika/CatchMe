package sut.game01.core.screen;

import playn.core.Font;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;
import playn.core.*;



import static playn.core.PlayN.*;

/**
 * Created by BkFamily on 21/1/2557.
 */
public class HomeScreen extends UIScreen {
    private Sound intro = assets().getSound("sounds/intro");
    private final  ScreenStack  ss;
    public HomeScreen(ScreenStack ss){
        this.ss =ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        Image bgImage = assets().getImage("images/other/bgCatchMe2.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);
        Image startImage = assets().getImage("images/other/StartButton.png");
        ImageLayer startLayer = graphics().createImageLayer(startImage);
        startLayer.setTranslation(170f,360f);
        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                intro.stop();
                ss.push(new StateOne(ss));
            }
        });
        layer.add(startLayer);

        Image settingImage = assets().getImage("images/other/SelectButton.png");
        ImageLayer settingLayer = graphics().createImageLayer(settingImage);
        settingLayer.setTranslation(320f,360f);
        settingLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                intro.stop();
                ss.push(new SelectScreen(ss));
            }
        });
        layer.add(settingLayer);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (!intro.isPlaying()){
            intro.play();
        }
    }
}