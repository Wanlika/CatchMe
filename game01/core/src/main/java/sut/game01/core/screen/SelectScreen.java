package sut.game01.core.screen;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.Sound;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by Yambok on 12/3/2557.
 */
public class SelectScreen extends UIScreen{
    private final ScreenStack ss;
    private Sound intro = assets().getSound("sounds/intro");
    public SelectScreen(ScreenStack ss) {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        Image bgImage = assets().getImage("images/other/bgCatchMe.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        Image backImage = assets().getImage("images/other/backbutton.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);
        backLayer.setSize(50, 50);
        backLayer.setOrigin(backLayer.width() / 2, backLayer.height() / 2);
        backLayer.setTranslation(50, 50);
        backLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(SelectScreen.this);
            }
        });
        layer.add(bgLayer);
        layer.add(backLayer);
        Image oneImage = assets().getImage("images/other/one.png");
        ImageLayer oneLayer = graphics().createImageLayer(oneImage);
        oneLayer.setOrigin(50,50);
        oneLayer.setTranslation(70f,400f);
        oneLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                intro.stop();
                ss.push(new StateOne(ss));
            }
        });

        Image two = assets().getImage("images/other/two.png");
        ImageLayer twoLayer = graphics().createImageLayer(two);
        twoLayer.setOrigin(50,50);
        twoLayer.setTranslation(190f,400f);
        twoLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                intro.stop();
                ss.push(new StateTwo(ss));
            }
        });

        Image three = assets().getImage("images/other/three.png");
        ImageLayer threeLayer = graphics().createImageLayer(three);
        threeLayer.setOrigin(50,50);
        threeLayer.setTranslation(310f,400f);
        threeLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                intro.stop();
                ss.push(new StateThree(ss));
            }
        });

        Image four = assets().getImage("images/other/four.png");
        ImageLayer fourLayer = graphics().createImageLayer(four);
        fourLayer.setOrigin(50,50);
        fourLayer.setTranslation(430f,400f);
        fourLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                intro.stop();
                ss.push(new StateFour(ss));
            }
        });

        Image five = assets().getImage("images/other/five.png");
        ImageLayer fiveLayer = graphics().createImageLayer(five);
        fiveLayer.setOrigin(50,50);
        fiveLayer.setTranslation(550f,400f);
        fiveLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                intro.stop();
                ss.push(new StateFive(ss));
            }
        });

        layer.add(oneLayer);
        layer.add(twoLayer);
        layer.add(threeLayer);
        layer.add(fourLayer);
        layer.add(fiveLayer);

    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (!intro.isPlaying()){
            intro.play();
        }
    }
}
