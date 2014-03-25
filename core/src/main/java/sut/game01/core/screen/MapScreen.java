package sut.game01.core.screen;

import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import sut.game01.core.all_etc.GameContent;
import sut.game01.core.all_etc.SoundStore;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

/**
 * Created by PSG on 3/2/14.
 */
public class MapScreen extends UIScreen {

    private final ScreenStack ss;
    private GameContent gContent;

    public MapScreen(ScreenStack ss,GameContent gContent)
    {
        this.ss = ss;
        this.gContent = gContent;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        ImageLayer bgLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapScreen/Mapscreen.png"));
        layer.add(bgLayer);

        ImageLayer backLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapScreen/back.png"));
        backLayer.setTranslation(640f-200f,480f - 430f);
        backLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.remove(MapScreen.this);
            }
        });
        layer.add(backLayer);

        ImageLayer btnStage1 = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapScreen/play.png"));
        btnStage1.setOrigin(22.5f,22.5f);
        btnStage1.setTranslation(640f - 230f, 480f - 120f);
        btnStage1.addListener(new Pointer.Adapter() {
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new Stage1(ss,gContent));
            }
        });
        layer.add(btnStage1);

        ImageLayer btnStage2 = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapScreen/lock.png"));
        btnStage2.setOrigin(22.5f,22.5f);
        btnStage2.setTranslation(640f - 430f, 480f - 225f);
        layer.add(btnStage2);

        ImageLayer btnStage3 = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapScreen/lock.png"));
        btnStage3.setOrigin(22.5f,22.5f);
        btnStage3.setTranslation(640f - 210f, 480f - 290f);
        layer.add(btnStage3);
    }

    @Override
    public void wasShown() {
        if(SoundStore.stage1Music.isPlaying())
            SoundStore.stage1Music.stop();

        if(!SoundStore.UIScreen.isPlaying())
            SoundStore.UIScreen.play();
    }
}
