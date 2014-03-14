package sut.game01.core.all_etc;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;
import sut.game01.core.character.Character;

/**
 * Created by PSG on 3/14/14.
 */
public class HPBarUI {

    private Character focus;
    private GroupLayer AllLayer = graphics().createGroupLayer();

    ImageLayer imgHPBar;
    ImageLayer imgExpBar;

    private float HPBarWidth = 174;
    private float ExpBarWidth = 161;

    public HPBarUI(Character focus,GroupLayer layer)
    {
        this.focus = focus;
        layer.add(AllLayer);

        ImageLayer imgHPBarUI = graphics().createImageLayer(assets().getImage("images/UI/HPUI.png"));
        imgHPBar = graphics().createImageLayer(assets().getImage("images/UI/HPBar.png"));
        imgExpBar = graphics().createImageLayer(assets().getImage("images/UI/ExpBar.png"));

        imgHPBar.setTranslation(67, 29);
        imgExpBar.setTranslation(67,44);

        AllLayer.add(imgHPBarUI);
        AllLayer.add(imgHPBar);
        AllLayer.add(imgExpBar); 
        AllLayer.setTranslation(10,0);
    }

    public void update()
    {
        float multiple = focus.getHp() / focus.getMaxHp();
        imgHPBar.setWidth(HPBarWidth * multiple);
    }





}
