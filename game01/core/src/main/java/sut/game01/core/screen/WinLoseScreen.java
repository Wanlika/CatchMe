package sut.game01.core.screen;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by Yambok on 21/3/2557.
 */
public class WinLoseScreen extends UIScreen {
    private ScreenStack ss;

    public WinLoseScreen(final ScreenStack ss,boolean win,boolean lose,final int state){
        this.ss = ss;
        if (win=true&&lose==false){
            youWin(state);
            System.out.println("Win"+state);
        }
        if (lose=true&&win==false){
            youLose(state);
            System.out.println("Lose"+state);
        }
    }

    public void youWin(final int state){
        layer.add(graphics().createImageLayer(assets().getImage("images/other/bgwin-lose.png")));
        layer.add(graphics().createImageLayer(assets().getImage("images/other/you_win_300x230.png")).setOrigin(150f,115f).setTranslation(320f,150f));
        Image nextImage = assets().getImage("images/other/nextbutton86-86.png");
        ImageLayer nextLayer = graphics().createImageLayer(nextImage);
        nextLayer.setOrigin(25f,25f);
        nextLayer.setTranslation(320, 310);
        nextLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                if (state==1){
                    ss.push(new StateTwo(ss));
                }
                else if (state==2){
                    ss.push(new StateThree(ss));
                }
                else if (state==3){
                    ss.push(new StateFour(ss));
                }
                else if (state==4){
                    ss.push(new StateFive(ss));
                }

            }
        });
        layer.add(nextLayer);
    }

    private void youLose(final int state) {
        layer.add(graphics().createImageLayer(assets().getImage("images/other/bgwin-lose.png")));
        layer.add(graphics().createImageLayer(assets().getImage("images/other/you_lose_300x230.png")).setOrigin(150f,115f).setTranslation(320f,150f));
        Image playImage = assets().getImage("images/other/playbutton86-86.png");
        ImageLayer playLayer = graphics().createImageLayer(playImage);
        playLayer.setOrigin(25f,25f);
        playLayer.setTranslation(320, 310);
        playLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                if (state==1){
                    ss.push(new StateOne(ss));
                }
                else if (state==2){
                    ss.push(new StateTwo(ss));
                }
                else if (state==3){
                    ss.push(new StateThree(ss));
                }
                else if (state==4){
                    ss.push(new StateFour(ss));
                }
                else if (state==5){
                    ss.push(new StateFive(ss));
                }
            }
        });
        layer.add(playLayer);
    }
}
