package sut.game01.core.screen;

import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import sut.game01.core.ModelShow.WitchModel;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

/**
 * Created by PSG on 3/2/14.
 */
public class CharacterScreen extends UIScreen {

    private final ScreenStack ss;
    private WitchModel model;

    public CharacterScreen(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        ImageLayer bgLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/CharacterScreen.png"));
        layer.add(bgLayer);

        ImageLayer startLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/MapButton.png"));
        startLayer.setTranslation(640f - 220f,480f - 170f);

        startLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new MapScreen(ss));
            }
        });
        layer.add(startLayer);

        ImageLayer backLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/BackButton.png"));
        backLayer.setTranslation(640f - 220f,480f - 100f);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.remove(CharacterScreen.this);
            }
        });
        layer.add(backLayer);

        ImageLayer upLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/Up.png"));
        upLayer.setTranslation(640f - 275f,480f - 170f);
        layer.add(upLayer);

        ImageLayer downLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharacterScreen/Down.png"));
        downLayer.setTranslation(640f - 275f,480f - 60f);
        layer.add(downLayer);


        // Add model for show
        model = new WitchModel(layer,125f,150f);
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        model.update(delta);
    }
}
