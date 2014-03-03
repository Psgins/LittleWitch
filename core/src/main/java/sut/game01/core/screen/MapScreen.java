package sut.game01.core.screen;

import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

/**
 * Created by PSG on 3/2/14.
 */
public class MapScreen extends UIScreen {

    final ScreenStack ss;

    public MapScreen(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        ImageLayer bgLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapBG.jpg"));
        bgLayer.setSize(640f,480f);
        layer.add(bgLayer);

        ImageLayer startLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/startbutton.png"));
        startLayer.setSize(100f,100f);
        startLayer.setTranslation(640f/2f-50f,480/2f-50f);
        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);

                ss.push(new Game2D(ss));
            }
        });
        layer.add(startLayer);

        ImageLayer backLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/back_button.png"));
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.remove(MapScreen.this);
            }
        });
        layer.add(backLayer);
    }
}
