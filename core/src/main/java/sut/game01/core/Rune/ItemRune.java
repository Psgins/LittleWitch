package sut.game01.core.Rune;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import sut.game01.core.all_etc.*;
import sut.game01.core.character.Witch;
import sut.game01.core.screen.Stage1;

/**
 * Created by PSG on 3/21/14.
 */
public class ItemRune extends DynamicObject {

    private GameEnvirontment gEnvir;
    private Vec2 Position;
    private int runeId = -1;
    private ImageLayer runeImage;
    private boolean hasKept = false;
    private int TimeOut = 10000;

    public ItemRune(GameEnvirontment gEnvir, Vec2 Position, int runeId)
    {
        this.gEnvir = gEnvir;
        this.runeId = runeId;
        this.Position = Position;

        runeImage = PlayN.graphics().createImageLayer(ImageStore.RuneIcon[runeId]);
        runeImage.setOrigin(15,15);
        runeImage.setSize(30, 30);

        initPhysicsBody(gEnvir.world, Position.x / VariableConstant.worldScale, Position.y / VariableConstant.worldScale, 20, 20, true);
        body.getFixtureList().setDensity(0.1f);
        body.applyLinearImpulse(new Vec2(0,-1),body.getPosition());
        runeImage.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);
        gEnvir.layer.add(runeImage);

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
            if(runeImage.alpha() <= 0)
            {
                runeImage.parent().remove(runeImage);
                body.getWorld().destroyBody(body);
                alive = false;
            }
            else
            {
                runeImage.setAlpha(runeImage.alpha() - 0.1f);
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

        runeImage.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);
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
            gEnvir.gContent.getRuneList().add(runeId);
            SoundStore.itemkeeping.play();
            hasKept = true;
        }
    }
}
