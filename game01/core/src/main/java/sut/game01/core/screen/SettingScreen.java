package sut.game01.core.screen;

import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.sprite.Mice;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import static playn.core.PlayN.*;

/**
 * Created by all user on 24/2/2557.
 */
public class SettingScreen extends UIScreen{

    private final ScreenStack ss;
    private Mice m;


    public SettingScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        Image bgImage = assets().getImage("images/other/bgCatchMe.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        Image backImage = assets().getImage("images/other/backbutton.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);
        backLayer.setSize(100,100);
        backLayer.setOrigin(backLayer.width()/2,backLayer.height()/2);
        backLayer.setTranslation(50,50);
        backLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(SettingScreen.this);

            }
        });
        layer.add(bgLayer);
        layer.add(backLayer);

        m = new Mice(20f,400f);
        layer.add(m.layer());

    }

    @Override
    public void update(int delta) {
        super.update(delta);
        m.update(delta);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        m.paint(clock);
    }
}
