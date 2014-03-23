package sut.game01.core.screen;

import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.sprite.Cat;
import sut.game01.core.sprite.Mice;
import sut.game01.core.sprite.Oldlady;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by Yambok on 21/3/2557.
 */
public class WinLoseScreen extends UIScreen {
    private ScreenStack ss;
    private Canvas canvas;
    private Root root;
    private Cat cat;
    private Mice mice;
    private Oldlady oldlady;
    private boolean win;
    private boolean lose;
    private Sound award = assets().getSound("sounds/award");

    public static final Font TITLE_FONT = graphics().createFont(
            "Cookie",
            Font.Style.PLAIN,
            50);
    private int state;

    public WinLoseScreen(final ScreenStack ss,boolean win,boolean lose,final int state,int point){
        this.ss = ss;
        this.win = win;
        this.lose = lose;
        this.state=state;
        award.play();
        if (win=true&&lose==false){
            if (state==5){
                missioncomplete();
            }else {
                youWin(state,point);
                System.out.println("Win"+state);
            }

        }
        if (lose=true&&win==false){
            youLose(state);
            System.out.println("Lose"+state);
        }
    }

    public void youWin(final int state,int point){

        layer.add(graphics().createImageLayer(assets().getImage("images/other/bgwin-lose.png")));
        layer.add(graphics().createImageLayer(assets().getImage("images/other/you_win_300x230.png")).setOrigin(150f,115f).setTranslation(320f,130f));
        Image nextImage = assets().getImage("images/other/nextbutton86-86.png");
        ImageLayer nextLayer = graphics().createImageLayer(nextImage);
        nextLayer.setOrigin(25f, 25f);
        nextLayer.setTranslation(350, 330);
        nextLayer.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                if (state == 1) {
                    ss.push(new StateTwo(ss));
                } else if (state == 2) {
                    ss.push(new StateThree(ss));
                } else if (state == 3) {
                    ss.push(new StateFour(ss));
                } else if (state == 4) {
                    ss.push(new StateFive(ss));
                }

            }
        });
        layer.add(nextLayer);

        Image backImage = assets().getImage("images/other/backbutton86-86.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);
        backLayer.setOrigin(25f, 25f);
        backLayer.setTranslation(280, 330);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new HomeScreen(ss));
            }
        });
        layer.add(backLayer);

        root =  iface.createRoot(
                AxisLayout.vertical().gap(25),
                SimpleStyles.newSheet(),layer);
        root.setSize(620,550);
        root.add(new Label("Point : "+point)
                .addStyles(Style.FONT.is(TITLE_FONT)).addStyles(Style.COLOR.is(0xFFFF0000)));

        oldlady = new Oldlady(10f,340);
        layer.add(oldlady.layer());
        oldlady.run();
        cat = new Cat(50f,340f);
        layer.add(cat.layer());
        cat.run();

//        CanvasImage canvasImage = graphics().createImage(graphics().width(), graphics().height());
//        ImageLayer imageLayer = graphics().createImageLayer();
//        imageLayer.setImage(canvasImage);
//        layer.add(imageLayer);
//        canvas = canvasImage.canvas();
    }

    private void youLose(final int state) {
        layer.add(graphics().createImageLayer(assets().getImage("images/other/bgwin-lose.png")));
        layer.add(graphics().createImageLayer(assets().getImage("images/other/you_lose_300x230.png")).setOrigin(150f,115f).setTranslation(320f,150f));
        Image playImage = assets().getImage("images/other/playbutton86-86.png");
        ImageLayer playLayer = graphics().createImageLayer(playImage);
        playLayer.setOrigin(25f,25f);
        playLayer.setTranslation(350, 310);
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

        Image backImage = assets().getImage("images/other/backbutton86-86.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);
        backLayer.setOrigin(25f, 25f);
        backLayer.setTranslation(280, 310);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new HomeScreen(ss));
            }
        });
        layer.add(backLayer);

        cat = new Cat(50f,340f);
        layer.add(cat.layer());
        cat.run();
        mice = new Mice(120f,400f);
        layer.add(mice.layer());
        mice.run();
    }

    public void missioncomplete(){
        layer.add(graphics().createImageLayer(assets().getImage("images/other/bgwin-lose.png")));
        layer.add(graphics().createImageLayer(assets().getImage("images/other/missioncomplete.png")).setOrigin(200, 150).setTranslation(320f, 200f));
        Image backImage = assets().getImage("images/other/backbutton86-86.png");
        ImageLayer backLayer = graphics().createImageLayer(backImage);
        backLayer.setOrigin(25f, 25f);
        backLayer.setTranslation(320, 380);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new HomeScreen(ss));
            }
        });
        layer.add(backLayer);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (state!=5){
            if (win==true&&lose==false){
                cat.update(delta);
                oldlady.update(delta);
            }else if (win==false&&lose==true){
                cat.update(delta);
                mice.update(delta);
            }
        }else if (state==5){
            if (win==false&&lose==true){
                cat.update(delta);
                mice.update(delta);
            }
        }
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if (state!=5){
            if (win==true&&lose==false){
                cat.paint(clock);
                oldlady.paint(clock);
            }else if (win==false&&lose==true){
                cat.paint(clock);
                mice.paint(clock);
            }
        }else if (state==5){
            if (win==false&&lose==true){
                cat.paint(clock);
                mice.paint(clock);
            }
        }
//        canvas.clear();
//
//        canvas.drawText("PlayN is cool!",50,100);
//        canvas.drawText("Hello World", 20, 100);
    }
}
