package sut.game01.core.character;

import playn.core.ImageLayer;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.sprite.Sprite;

/**
 * Created by PSG on 3/13/14.
 */
public class Character extends DynamicObject {

    public enum Owner {Enemy,Ally}

    // Character Information
    protected float attack = 0;
    protected float defend = 0;
    protected float maxHP = 0;
    protected float hp = 0;
    protected Owner owner = null;

    // Character Model
    protected Sprite sprite = null;
    protected int SpriteIndex = -1;
    protected int e = 0;
    protected int offset = 0;
    protected int renderSpeed = 150;

    public float getAttack() { return attack; }

    public float getDefend() { return defend;}

    public float getHp() { return hp;}

    public Owner getOwner() {return owner;}

    public ImageLayer layer(){return sprite.layer();}
}
