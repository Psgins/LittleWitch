package sut.game01.core.Pet;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.util.Callback;
import sut.game01.core.Rune.Rune;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.character.Character;
import sut.game01.core.screen.Stage1;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 3/16/14.
 */
public class MiniSpirit extends Pet {

    public MiniSpirit(final World world,final GroupLayer layer,final Character focus,int petID,Rune cRune)
    {
        main = focus;
        rune = cRune;

        sprite = SpriteLoader.getSprite(VariableConstant.petPath[petID]);
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {

                sprite.layer().setOrigin(20f,20f);
                sprite.layer().setSize(40f,40f);

                initPhysicsBody(
                        world,
                        320,
                        312,
                        20f,
                        20f,
                        true
                );

                AllLayer.add(sprite.layer());
                layer.add(AllLayer);

                ready = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }

    @Override
    public void update(int delta) {

        if(!alive || !ready) return;

        e += delta;

        AutoBuff(delta);

        if(e >= renderSpeed)
        {
            switch (state)
            {
                case idle:
                    offset = 0;
                    break;
                case buff:
                    offset = 4;
                    if(spriteIndex == 6) state = State.idle;
                    break;
            }

            spriteIndex = offset + (spriteIndex+1)%4;
            sprite.setSprite(spriteIndex);

            e = 0;
        }

        Vec2 tmpDistance = seekMain(main);
        if (tmpDistance.x > 4 || tmpDistance.x < -4)
            Move2Main(main,tmpDistance,3);

        if (body.getPosition().y > 12)
        {
            body.applyForce(new Vec2(0,-13.5f),body.getPosition());
        }
    }

    @Override
    public void paint() {
        if(!alive || !ready) return;
        sprite.layer().setTranslation(body.getPosition().x / VariableConstant.worldScale,body.getPosition().y / VariableConstant.worldScale);
    }
}
