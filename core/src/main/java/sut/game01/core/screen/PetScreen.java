package sut.game01.core.screen;

import playn.core.ImageLayer;
import playn.core.Pointer;
import sut.game01.core.ModelShow.MiniSpiritModel;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import static playn.core.PlayN.*;

/**
 * Created by PSG on 3/2/14.
 */
public class PetScreen extends UIScreen {

    final ScreenStack ss;

    private MiniSpiritModel pet;

    public PetScreen(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        ImageLayer bgLayer = graphics().createImageLayer(assets().getImage("images/PetScreen/petScreen.png"));
        layer.add(bgLayer);

        ImageLayer backLayer = graphics().createImageLayer(assets().getImage("images/MapScreen/back.png"));
        backLayer.setTranslation(500,15);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.remove(PetScreen.this);
            }
        });
        layer.add(backLayer);

        pet = new MiniSpiritModel(layer,200f,260f);
        pet.layer().setScale(1.5f);
    }

    @Override
    public void update(int delta) {
        pet.update(delta);
    }
}
