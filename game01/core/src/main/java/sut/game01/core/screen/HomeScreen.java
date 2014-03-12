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
    public void wasAdded() {
        super.wasAdded();
        Image bgImage = assets().getImage("images/other/bgCatchMe2.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);
        Image startImage = assets().getImage("images/other/SelectButton.png");
        ImageLayer startLayer = graphics().createImageLayer(startImage);
        startLayer.setTranslation(170f,360f);
        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new SelectScreen(ss));
            }
        });
        layer.add(startLayer);

        Image settingImage = assets().getImage("images/other/SettingButton.png");
        ImageLayer settingLayer = graphics().createImageLayer(settingImage);
        settingLayer.setTranslation(320f,360f);
        settingLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new SettingScreen(ss));
            }
        });
        layer.add(settingLayer);

//        root.add(new Button("Start").onClick(new UnitSlot() {
//            @Override
//            public void onEmit() {
//                ss.push(new GameScreen(ss));
//            }
//        }));
    }

//    @Override
//    public void wasShown() {
//        super.wasShown();
//        root =  iface.createRoot(
//                AxisLayout.vertical().gap(15),
//                SimpleStyles.newSheet(),layer);
//        root.addStyles(Style.BACKGROUND.is(Background.
//                bordered(0xFFCCCCCC, 0xFF99CCFF, 5).
//                inset(5, 10)));
//        root.setSize(width(),height());
//        root.add(new Label("HomeScreen")
//                .addStyles(Style.FONT.is(HomeScreen.TITLE_FONT)));
//        root.add(new Button("Start").onClick(new UnitSlot(){
//            public void onEmit(){
//                ss.push(new GameScreen(ss));
//            }
//        }));
//        root.add(new Button("Setting").onClick(new UnitSlot(){
//            public void onEmit(){
//                ss.push(new SettingScreen(ss));
//            }
//        }));
//    }
}