package sut.game01.core.Skill;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.character.*;
import sut.game01.core.screen.Stage1;

import java.util.List;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by PSG on 3/17/14.
 */
public class Screeming_Card implements SkillCard {
    private ImageLayer Icon;
    private ImageLayer Cover = graphics().createImageLayer(ImageStore.Cover);
    private int cooldown = 20000;
    private int e = 0;

    public Screeming_Card()
    {
        this.Icon = graphics().createImageLayer(ImageStore.itemIcon[0]);
        this.Icon.setSize(50,50);
        Cover.setHeight(0);
    }

    @Override
    public boolean Shoot(sut.game01.core.character.Character caster,boolean isLeft,List<DynamicObject> objTemp)
    {
        if(e > 0)
        {
            return false;
        }
        else
        {
            objTemp.add(new Screeming(
                    caster.getBody().getWorld(),
                    caster.layer().parent(),
                    caster.getBody().getPosition().x / Stage1.M_PER_PIXEL,
                    caster.getBody().getPosition().y / Stage1.M_PER_PIXEL,
                    caster.getOwner(),
                    isLeft,
                    caster.getAttack()));

            e = cooldown;
            Cover.setHeight(50f);
            return true;
        }
    }

    @Override
    public void update(int delta) {

        if (e <= 0){
            return;
        }

        e -= delta;

        float percent = (float)e/(float)cooldown;
        Cover.setHeight(50f*percent);
    }

    @Override
    public ImageLayer getIcon() {
        return this.Icon;
    }

    @Override
    public void setCover(GroupLayer layer)
    {
        layer.add(Cover);
    }
}
