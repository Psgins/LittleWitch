package sut.game01.core.character;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.util.Callback;
import sut.game01.core.Skill.Skill;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.all_etc.FloatLabel;
import sut.game01.core.screen.Stage1;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 3/13/14.
 */
public class MiniGhost extends Character {

    public enum State {idle,die}
    private float y;

    private State state = State.idle;

    public MiniGhost(final World world, final GroupLayer layer, final float x, final float y, Owner own, FloatLabel fLabel)
    {
        this.y = y;
        floatLabel = fLabel;

        hp = 200;
        maxHP = 200;

        sprite = SpriteLoader.getSprite("images/CharSprite/MiniGhost.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(80f /2f, 48f /2f);

                initPhysicsBody(world, x, y, 30f, 35f,false);

                createHPbar(sprite.layer().tx(),sprite.layer().ty()- 25,45);

                AllLayer.add(sprite.layer());
                layer.add(AllLayer);
                ready = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        owner = own;
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if (!alive || !ready) return;

        e+= delta;

        if (e > renderSpeed)
        {
            switch (state)
            {
                case idle:
                    offset = 0;
                    break;
                case die:
                    offset = 4;
                    if (spriteIndex == 7) {
                        AllLayer.parent().remove(AllLayer);
                        body.getWorld().destroyBody(body);
                        alive = false;
                    }
            }

            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e= 0;
        }

        if(body.getPosition().y / Stage1.M_PER_PIXEL > y + 10) body.applyForce(new Vec2(0,-40f),body.getPosition());
    }

    @Override
    public void paint() {
        super.paint();

        if(!alive || !ready) return;

        AllLayer.setTranslation(body.getPosition().x / Stage1.M_PER_PIXEL, body.getPosition().y / Stage1.M_PER_PIXEL);
    }

    @Override
    public void destroy() {
        super.destroy();


    }

    @Override
    public void contact(DynamicObject A, DynamicObject B) {

        if (!alive) return;

        DynamicObject other;

        if(A.getClass() == this.getClass())
            other = B;
        else
            other = A;

        if(other.getBody().isBullet())
        {
            Skill skillObject = (Skill)other;
            if(skillObject.getOwner() != owner)
            {
                float dmg = skillObject.getDamage();
                hp = (hp - dmg) < 0 ? 0 : (hp - dmg);
                HPBar.setWidth(HPBarWidth * (hp/maxHP));

                floatLabel.CreateText((int)dmg,body.getPosition().x / Stage1.M_PER_PIXEL,(body.getPosition().y / Stage1.M_PER_PIXEL)-15f);

                if (hp <= 0)
                {
                    renderSpeed = 50;
                    state = State.die;
                }
                skillObject.destroy();
            }
        }
    }
}
