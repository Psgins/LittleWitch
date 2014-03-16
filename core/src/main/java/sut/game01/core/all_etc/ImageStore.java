package sut.game01.core.all_etc;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.util.Callback;

/**
 * Created by PSG on 3/13/14.
 */
public class ImageStore {

    private int count = 3;

    public final Image HPBar;
    public final Image Fireball;
    public final Image WhiteCircle;

    public ImageStore()
    {
        this.HPBar = PlayN.assets().getImage("images/etc/hpbar.png");
        this.HPBar.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                count--;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

        this.Fireball = PlayN.assets().getImage("images/Skill/fireball.png");
        this.Fireball.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                count--;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

        this.WhiteCircle = PlayN.assets().getImage("images/Skill/light.png");
        this.WhiteCircle.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                count--;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

    }

    public boolean isReady()
    {
        if(count <= 0)
            return true;
        else
            return false;
    }
}
