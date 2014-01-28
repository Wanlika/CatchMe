package sut.game01.core;

import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.screen.HomeScreen;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class MyGame extends Game.Default {
    private static final int UPDATE_RATE=25;
    private ScreenStack ss = new ScreenStack();
    protected final Clock.Source clock = new Clock.Source(UPDATE_RATE);
    public MyGame() {
        super(UPDATE_RATE); // call update every 33ms (30 times per second)
    }

    @Override
    public void init() {
        final Screen home = new HomeScreen(ss);
        ss.push(home);
        PlayN.keyboard().setListener(new Keyboard.Adapter(  ){
            @Override
            public void onKeyUp(Keyboard.Event event){
                if(event.key()== Key.ESCAPE){
                    ss.popTo(home);
                }
            }
        });



//   ss.push(new HomeScreen(ss));
    }

    @Override
    public void update(int delta) {
        ss.update(delta);
    }

    @Override
    public void paint(float alpha) {
        // the background automatically paints itself, so no need to do anything here!
        clock.paint(alpha);
        ss.paint(clock);
    }
}