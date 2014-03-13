package sut.game01.core.character;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import static playn.core.PlayN.*;
import playn.core.util.Callback;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.screen.Game2D;
import sut.game01.core.sprite.Sprite;

/**
 * Created by PSG on 3/13/14.
 */
public class Character extends DynamicObject {

    // Character Information
    protected float attack = 0;
    protected float defend = 0;
    protected float maxHP = 0;
    protected float hp = 0;
    protected Owner owner = null;

    // Character Model
    protected Sprite sprite = null;
    protected int spriteIndex = -1;
    protected int e = 0;
    protected int offset = 0;
    protected int renderSpeed = 150;

    // HPBar infomation
    protected boolean hasHPBar = false;
    protected float HPBarWidth = 0;

    protected GroupLayer AllLayer = graphics().createGroupLayer();

    public float getAttack() { return attack; }

    public float getDefend() { return defend;}

    public float getHp() { return hp;}

    public Owner getOwner() {return owner;}

    public void createHPbar(float x, float y,float width)
    {
        ImageLayer HPBar = graphics().createImageLayer(Game2D.imageStore.HPBar);
        HPBar.setWidth(width);
        HPBar.setOrigin(HPBar.width() / 2f, HPBar.height() / 2f);
        HPBar.setTranslation(x,y);
        AllLayer.add(HPBar);

        hasHPBar = true;
        HPBarWidth = width;
    }
}
