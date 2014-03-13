package sut.game01.core.Skill;

import playn.core.GroupLayer;
import playn.core.PlayN;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.character.*;
import sut.game01.core.character.Character;

import java.util.Random;

/**
 * Created by PSG on 3/13/14.
 */
public class Skill extends DynamicObject {
    protected float BaseDamage = 0;
    protected int RangeDamage = 0;
    protected float AdditionDamage = 0;

    protected int e = 0;
    protected Owner owner = null;

    protected GroupLayer AllLayer = PlayN.graphics().createGroupLayer();

    public float getDamage(){
        float range = (new Random()).nextInt() % RangeDamage;
        float result = (BaseDamage + AdditionDamage) + range;
        return result;
    }

    public Owner getOwner(){return owner;}
    public GroupLayer layer(){return AllLayer;}
}
