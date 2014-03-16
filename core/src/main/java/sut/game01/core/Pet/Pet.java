package sut.game01.core.Pet;

import org.jbox2d.common.Vec2;
import playn.core.GroupLayer;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.character.Character;
import sut.game01.core.sprite.Sprite;

import static playn.core.PlayN.graphics;

/**
 * Created by PSG on 3/16/14.
 */
public class Pet extends DynamicObject {

    protected Character main;

    // Pet Model
    protected Sprite sprite = null;
    protected int spriteIndex = -1;
    protected int e = 0;
    protected int offset = 0;
    protected int renderSpeed = 150;
    protected GroupLayer AllLayer = graphics().createGroupLayer();

    protected Vec2 seekMain(Character focus)
    {
        if(focus.getBody() != null)
        {
            Vec2 distance = body.getLocalPoint(focus.getBody().getPosition());
            return distance;
        }
        else
        {
            return new Vec2(999f,0f);
        }
    }

    protected void  Move2Main(Character focus,Vec2 distance,float moveSpeed)
    {
        if(focus.getBody() == null) return;

        if(body.getLinearVelocity().x > -moveSpeed && body.getLinearVelocity().x < moveSpeed )

            if (distance.x > 0)
                body.applyForce(new Vec2(moveSpeed,0f),body.getPosition());
            else
                body.applyForce(new Vec2(-moveSpeed,0f),body.getPosition());
    }
}
