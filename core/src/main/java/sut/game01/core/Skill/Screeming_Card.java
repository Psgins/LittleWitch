package sut.game01.core.Skill;

import static playn.core.PlayN.*;
import playn.core.ImageLayer;
import playn.core.PlayN;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.character.Character;
import sut.game01.core.screen.Stage1;

import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public class Screeming_Card implements SkillCard {

    private ImageLayer Icon;
    private int cooldown = 0;

    public Screeming_Card()
    {
        this.Icon = graphics().createImageLayer(assets().getImage("images/Skill/ScreemingIcon.png"));
        this.Icon.setSize(50,50);
    }

    @Override
    public boolean Shoot(Character caster,boolean isLeft,List<DynamicObject> objTemp)
    {
        if(cooldown > 0)
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

            cooldown = 2000;
            return true;
        }
    }

    @Override
    public void update(int delta) {
        if (cooldown <= 0) return;
        cooldown -= delta;
    }

    @Override
    public ImageLayer getIcon() {
        return this.Icon;
    }
}
