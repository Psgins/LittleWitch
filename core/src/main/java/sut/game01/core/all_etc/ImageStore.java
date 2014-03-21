package sut.game01.core.all_etc;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.util.Callback;

import static playn.core.PlayN.assets;

/**
 * Created by PSG on 3/13/14.
 */
public class ImageStore {

    public static final Image HPBar = PlayN.assets().getImage("images/etc/hpbar.png");;
    public static final Image Fireball = PlayN.assets().getImage("images/Skill/fireball.png");
    public static final Image WhiteCircle = PlayN.assets().getImage("images/Skill/light.png");
    public static final Image[] number = new Image[]{
        assets().getImage("images/Number/0.png"),
        assets().getImage("images/Number/1.png"),
        assets().getImage("images/Number/2.png"),
        assets().getImage("images/Number/3.png"),
        assets().getImage("images/Number/4.png"),
        assets().getImage("images/Number/5.png"),
        assets().getImage("images/Number/6.png"),
        assets().getImage("images/Number/7.png"),
        assets().getImage("images/Number/8.png"),
        assets().getImage("images/Number/9.png")
    };
    public static final  Image[] numberO = new Image[]{
            assets().getImage("images/Number/O0.png"),
            assets().getImage("images/Number/O1.png"),
            assets().getImage("images/Number/O2.png"),
            assets().getImage("images/Number/O3.png"),
            assets().getImage("images/Number/O4.png"),
            assets().getImage("images/Number/O5.png"),
            assets().getImage("images/Number/O6.png"),
            assets().getImage("images/Number/O7.png"),
            assets().getImage("images/Number/O8.png"),
            assets().getImage("images/Number/O9.png")
    };
    public static final Image Cover = PlayN.assets().getImage("images/UI/CooldownCover.png");
    public static final Image NullSkill = PlayN.assets().getImage("images/UI/nullSkill.png");
    public static final Image[] itemIcon = new Image[]
    {
            PlayN.assets().getImage("images/Skill/ScreemingIcon.png"),
            PlayN.assets().getImage("images/Skill/BalloonIcon.png")
    };
    public static final Image[] RuneIcon = new Image[]
    {
            PlayN.assets().getImage("images/Rune/psHPSmallIcon.png")
    };
}
