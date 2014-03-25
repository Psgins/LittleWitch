package sut.game01.core.screen;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Pointer;
import sut.game01.core.ModelShow.WitchModel;
import sut.game01.core.all_etc.GameContent;
import sut.game01.core.all_etc.SoundStore;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import static playn.core.PlayN.*;

/**
 * Created by PSG on 3/24/14.
 */
public class CharacterSelect extends Screen {

    private final ScreenStack ss;
    private GameContent gContent;
    private WitchModel model;
    private GroupLayer charPosition = graphics().createGroupLayer();

    public CharacterSelect(ScreenStack ss, GameContent gContent)
    {
        this.ss = ss;
        this.gContent = gContent;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        ImageLayer bgLayer = graphics().createImageLayer(assets().getImage("images/CharacterSelect/CharBack.png"));
        layer.add(bgLayer);

        ImageLayer backLayer =graphics().createImageLayer(assets().getImage("images/MapScreen/back.png"));
        backLayer.setTranslation(500,15);
        layer.add(backLayer);
        backLayer.addListener(new Pointer.Adapter()
        {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gContent.save();
                ss.remove(ss.top());
            }
        });

        charPosition.setTranslation(600,350);
        layer.add(charPosition);

        model = new WitchModel(charPosition,0,0,gContent.getCharID());
//        model.layer().setOrigin(-150,84);
        model.layer().setWidth(-150);

        ImageLayer wChoice = graphics().createImageLayer(assets().getImage("images/Model/witchIcon.png"));
        wChoice.setScale(0.8f);
        wChoice.setTranslation(25,15);
        layer.add(wChoice);
        wChoice.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                charPosition.removeAll();
                model = new WitchModel(charPosition,0,0,0);
                model.layer().setOrigin(-150,84);
                model.layer().setWidth(-150);
                gContent.setCharID(0);
            }
        });

        ImageLayer dChoice = graphics().createImageLayer(assets().getImage("images/Model/DemonIcon.png"));
        dChoice.setScale(0.8f);
        dChoice.setTranslation(125,15);
        layer.add(dChoice);
        dChoice.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                charPosition.removeAll();
                model = new WitchModel(charPosition,0,0,1);
                model.layer().setOrigin(-150,84);
                model.layer().setWidth(-150);
                gContent.setCharID(1);
            }
        });

    }

    @Override
    public void wasShown() {
        if(SoundStore.stage1Music.isPlaying())
            SoundStore.stage1Music.stop();

        if(!SoundStore.UIScreen.isPlaying())
            SoundStore.UIScreen.play();
    }

    @Override
    public void update(int delta) {
        model.update(delta);
    }
}
