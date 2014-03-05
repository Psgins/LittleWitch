package sut.game01.core.screen;

import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

/**
 * Created by PSG on 3/2/14.
 */
public class StartScreen extends UIScreen {

    final ScreenStack ss;

    public StartScreen(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        ImageLayer bgLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/bg.png"));
        layer.add(bgLayer);

        ImageLayer startLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/startbutton.png"));
        startLayer.setWidth(100f);
        startLayer.setHeight(100f);
        startLayer.setTranslation(640f/2f-50f,480/2f-50f);

        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);

                ss.push(new CharacterScreen(ss));
            }
        });
        layer.add(startLayer);
    }


}
