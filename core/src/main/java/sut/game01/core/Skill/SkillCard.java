package sut.game01.core.Skill;

import playn.core.GroupLayer;
import playn.core.ImageLayer;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.character.Character;

import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public interface SkillCard {
    public boolean Shoot(Character caster,boolean isLeft,List<DynamicObject> objTemp);
    public ImageLayer getIcon();
    public void update(int delta);
    public void setCover(GroupLayer layer);
}
