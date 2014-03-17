package sut.game01.core.all_etc;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.util.Callback;

import static playn.core.PlayN.assets;

/**
 * Created by PSG on 3/13/14.
 */
public class ImageStore {

    private int count = 15;

    public final Image HPBar;
    public final Image Fireball;
    public final Image WhiteCircle;
    public final Image[] number = new Image[10];
    public final Image Cover;
    public final Image NullSkill;

    public ImageStore()
    {
        HPBar = PlayN.assets().getImage("images/etc/hpbar.png");
        HPBar.addCallback(new Callback<Image>() {
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

        WhiteCircle = PlayN.assets().getImage("images/Skill/light.png");
        WhiteCircle.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                count--;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

        for(int i=0;i<10;i++)
        {
            number[i] = assets().getImage("images/Number/"+i+".png");
            number[i].addCallback(new Callback<Image>() {
                @Override
                public void onSuccess(Image result) {
                    count--;
                }

                @Override
                public void onFailure(Throwable cause) {

                }
            });
        }

        Cover = PlayN.assets().getImage("images/UI/CooldownCover.png");
        Cover.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                count--;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

        NullSkill = PlayN.assets().getImage("images/UI/nullSkill.png");
        NullSkill.addCallback(new Callback<Image>() {
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
