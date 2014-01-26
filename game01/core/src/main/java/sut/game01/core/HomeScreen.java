package sut.game01.core;

import playn.core.Font;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.game.Screen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;



import static playn.core.PlayN.*;

/**
 * Created by BkFamily on 21/1/2557.
 */
public class HomeScreen extends UIScreen {
    public static final Font TITLE_FONT = graphics().createFont(
            "Helvetiva",
            Font.Style.PLAIN,
            24 );

    private final  ScreenStack  ss;
    private Root root;
    public HomeScreen(ScreenStack ss){
        this.ss =ss;
    }

    @Override
    public void wasShown() {
        super.wasShown();
        root =  iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(),layer);
        root.addStyles(Style.BACKGROUND.is(Background.
                bordered(0xFFCCCCCC, 0xFF99CCFF, 5).
                inset(5, 10)));
        root.setSize(width(),height());
        root.add(new Label("WelCome To MyGame")
                .addStyles(Style.FONT.is(HomeScreen.TITLE_FONT)));
        root.add(new Button("Start").onClick(new UnitSlot(){
            public void onEmit(){
                ss.push(new TestScreen(ss));
            }
        }));

    }
}