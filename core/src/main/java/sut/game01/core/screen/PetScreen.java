package sut.game01.core.screen;

import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

/**
 * Created by PSG on 3/2/14.
 */
public class PetScreen extends UIScreen {

    final ScreenStack ss;

    public PetScreen(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
    }
}
