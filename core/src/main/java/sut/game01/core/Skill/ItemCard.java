package sut.game01.core.Skill;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import sut.game01.core.all_etc.*;
import sut.game01.core.character.Witch;
import sut.game01.core.screen.Stage1;

import javax.swing.*;

/**
 * Created by PSG on 3/20/14.
 */
public class ItemCard extends DynamicObject {

    private GameEnvirontment gEnvir;
    private Vec2 Position;
    private int itemId = -1;
    private ImageLayer itemImage;
    private boolean hasKept = false;
    private int TimeOut = 10000;

    public ItemCard (GameEnvirontment gEnvir, Vec2 Position, int itemId)
    {
        this.gEnvir = gEnvir;
        this.itemId = itemId;
        this.Position = Position;

        itemImage = PlayN.graphics().createImageLayer(ImageStore.itemIcon[itemId]);
        itemImage.setOrigin(15,15);
        itemImage.setSize(30, 30);

        initPhysicsBody(gEnvir.world, Position.x / VariableConstant.worldScale, Position.y / VariableConstant.worldScale, 20, 20, true);
        body.getFixtureList().setDensity(0.1f);
        body.applyLinearImpulse(new Vec2(0,-1),body.getPosition()); 
        itemImage.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);
        gEnvir.layer.add(itemImage);

        ready = true;
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if(!alive || !ready) return;

        if((TimeOut-= delta) <= 0)
            hasKept = true;

        if(hasKept)
        {
            body.applyForce(new Vec2(0,-5),body.getPosition());
            if(itemImage.alpha() <= 0)
            {
                itemImage.parent().remove(itemImage);
                body.getWorld().destroyBody(body);
                alive = false;
            }
            else
            {
                itemImage.setAlpha(itemImage.alpha() - 0.1f);
            }
        }
        else
        {
            if(body.getPosition().y / VariableConstant.worldScale > Position.y / VariableConstant.worldScale + 1f) body.applyForce(new Vec2(0,-20f),body.getPosition());
        }
    }

    @Override
    public void paint() {
        super.paint();

        if(!alive || !ready) return;

        itemImage.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);
    }

    @Override
    public void contact(DynamicObject A, DynamicObject B) {
        if(!alive || !ready) return;

        DynamicObject other;

        if(A.getClass() == this.getClass())
            other = B;
        else
            other = A;

        if(other.getClass() == Witch.class)
        {
            SoundStore.itemkeeping.play();
            gEnvir.gContent.getItem().add(itemId);
            hasKept = true;
        }
    }
}
