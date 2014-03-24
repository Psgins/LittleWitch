package sut.game01.core.screen;

import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import sut.game01.core.all_etc.GameContent;
import sut.game01.core.all_etc.GameEnvirontment;
import sut.game01.core.all_etc.SoundStore;
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

        ImageLayer PlayButton = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/ui/play.png"));
        PlayButton.setTranslation(214,172);
        PlayButton.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                ss.push(new CharacterScreen(ss));
            }
        });
        layer.add(PlayButton);

        ImageLayer NewButton = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/ui/NewGame.png"));
        NewButton.setTranslation(214,241);
        NewButton.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                GameContent gContent = new GameContent();
                gContent.create();
                ss.push(new CharacterScreen(ss));
            }
        });
        layer.add(NewButton);

        SoundStore.stage1Music.prepare();
    }
}
