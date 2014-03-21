package sut.game01.core.Rune;

import playn.core.ImageLayer;
import playn.core.PlayN;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.character.*;
import sut.game01.core.character.Character;

/**
 * Created by PSG on 3/18/14.
 */
public class Rune {

    protected int RuneID = -1;

    public void Buff(Character focus) {}
    public ImageLayer getIcon(){
        ImageLayer Icon = PlayN.graphics().createImageLayer(ImageStore.RuneIcon[RuneID]);
        return Icon;
    }
}
