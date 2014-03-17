package sut.game01.core.Skill;

import static playn.core.PlayN.*;
import playn.core.ImageLayer;
import playn.core.PlayN;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.character.Character;

import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public class Screeming_Card implements SkillCard {

    private ImageLayer Icon;

    public Screeming_Card()
    {
        this.Icon = graphics().createImageLayer(assets().getImage("images/Skill/ScreemingIcon.png"));
        this.Icon.setSize(50,50);
    }

    @Override
    public void Shoot(Character caster,boolean isLeft,List<DynamicObject> objTemp)
    {
        objTemp.add(new Screeming(
                caster.getBody().getWorld(),
                caster.layer().parent(),
                caster.getBody().getPosition().x,
                caster.getBody().getPosition().y,
                caster.getOwner(),
                isLeft,
                caster.getAttack()));
    }

    @Override
    public ImageLayer getIcon() {
        return this.Icon;
    }
}
