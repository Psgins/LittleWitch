package sut.game01.core.UI;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;

import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.character.Character;
import sut.game01.core.character.Witch;
import sut.game01.core.screen.Stage1;

/**
 * Created by PSG on 3/14/14.
 */
public class HPBarUI {

    private Witch focus;
    private GroupLayer AllLayer = graphics().createGroupLayer();

    private ImageLayer imgHPBar;
    private ImageLayer imgExpBar;
    private GroupLayer lvlPosition;

    private float HPBarWidth = 174;
    private float ExpBarWidth = 161;

    public HPBarUI(Witch focus,GroupLayer layer)
    {
        this.focus = focus;
        layer.add(AllLayer);

        ImageLayer imgHPBarUI = graphics().createImageLayer(assets().getImage("images/UI/HPUI.png"));
        imgHPBar = graphics().createImageLayer(assets().getImage("images/UI/HPBar.png"));
        imgExpBar = graphics().createImageLayer(assets().getImage("images/UI/ExpBar.png"));
        lvlPosition = graphics().createGroupLayer();

        imgHPBar.setTranslation(67, 29);
        imgExpBar.setTranslation(67,44);
        lvlPosition.setTranslation(10,25);

        lvlPosition.add(Level2Layer(focus.getLevel()));

        AllLayer.add(imgHPBarUI);
        AllLayer.add(imgHPBar);
        AllLayer.add(imgExpBar);
        AllLayer.add(lvlPosition);
        AllLayer.setTranslation(10,0);
    }

    public void update()
    {
        float hpPercent = focus.getHp() / focus.getMaxHp();
        imgHPBar.setWidth(HPBarWidth * hpPercent);

        float expPercent = (float)focus.getExp() / (float)VariableConstant.expRange[focus.getLevel()-1];
        imgExpBar.setWidth(ExpBarWidth * expPercent);

        lvlPosition.removeAll();
        lvlPosition.add(Level2Layer(focus.getLevel()));
    }

    public GroupLayer Level2Layer(int lvl)
    {
        String[] strLVL = String.valueOf(lvl).split("");
        GroupLayer group = graphics().createGroupLayer();
        int space = 0;

        for(int i=1;i < strLVL.length;i++)
        {
            ImageLayer number = graphics().createImageLayer(ImageStore.numberO[Integer.valueOf(strLVL[i])]);
            number.setScale(0.7f);
            number.setTranslation(space+=10,0);
            group.add(number);
        }

        return group;
    }
}
