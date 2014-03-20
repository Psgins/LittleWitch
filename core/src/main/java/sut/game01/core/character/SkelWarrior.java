package sut.game01.core.character;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.util.Callback;
import sut.game01.core.Skill.Skill;
import sut.game01.core.Skill.SwordAttack;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.UI.FloatLabel;
import sut.game01.core.screen.Stage1;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 3/18/14.
 */
public class SkelWarrior extends Character {
    public enum State {idleL,idleR,RunL,RunR,AtkL,AtkR,dead}

    private State state = State.RunL;

    private int DelayCount = 0;
    private int AttackDelay = 1000;

    public SkelWarrior(final World world, final GroupLayer layer, final float x, final float y, Owner own, FloatLabel fLabel)
    {
        renderSpeed = 100;
        floatLabel = fLabel;

        hp = 200;
        maxHP = 200;
        attack = 30;
        defend = 10;

        sprite = SpriteLoader.getSprite("images/CharSprite/SkelSprite.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(70f, 75f);

                initPhysicsBody(world, x, y, 50f, 80f,false);

                createHPbar(sprite.layer().tx(),sprite.layer().ty()- 55f,60);

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

        if (!alive || !ready || !inScreen(Stage1.main)) return;

        e+= delta;

        if (e > renderSpeed)
        {
            switch (state)
            {
                case idleR:
                    offset = 0;
                    break;
                case idleL:
                    offset = 4;
                    break;
                case RunR:
                    offset = 8;
                    break;
                case RunL:
                    offset = 12;
                    break;
                case AtkL:
                    offset = 16;
                    if(spriteIndex == 18) setState(State.idleL);
                    break;
                case AtkR:
                    offset = 20;
                    if(spriteIndex == 22) setState(State.idleR);
                    break;
                case dead:
                    offset = 24;
                    if (spriteIndex == 27 )
                    {
                        AllLayer.parent().remove(AllLayer);
                        body.getWorld().destroyBody(body);
                        alive = false;
                        return;
                    }
            }

            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e= 0;
        }

        // reject statements below when dead
        if(state == State.dead) return;

        // SeekMain
        Vec2 tmpSeek = seekMain(Stage1.main);
        if((tmpSeek.x > 3 || tmpSeek.x < -3) && tmpSeek.x != 999f)
        {
            Move2Main(Stage1.main,tmpSeek,13f);
            if(body.getLinearVelocity().x < 0)
            {
                if(state != State.RunL) setState(State.RunL);
            }
            else
            {
                if(state != State.RunR) setState(State.RunR);
            }
        }
        else
        {
            DelayCount += delta;
            if (DelayCount >= AttackDelay)
            {
                AttackMain(Stage1.main,tmpSeek);
                DelayCount = 0;
            }
        }
    }

    @Override
    public void paint() {
        super.paint();

        if(!alive || !ready) return;
        AllLayer.setTranslation(body.getPosition().x / Stage1.M_PER_PIXEL, body.getPosition().y / Stage1.M_PER_PIXEL);
    }

    @Override
    public void contact(DynamicObject A, DynamicObject B) {

        if (!alive || state == State.dead) return;

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
                float dmg = skillObject.getDamage() - defend;
                if (dmg < 0) dmg = 1;
                hp = (hp - dmg) < 0 ? 0 : (hp - dmg);
                HPBar.setWidth(HPBarWidth * (hp/maxHP));

                floatLabel.CreateText((int)dmg,body.getPosition().x / Stage1.M_PER_PIXEL,(body.getPosition().y / Stage1.M_PER_PIXEL)-15f);

                if (hp <= 0)
                {
                    setState(State.dead);
                    Stage1.main.gainEXP(30);
                }
                skillObject.destroy();
            }
        }
    }

    @Override
    protected void AttackMain(Character focus, Vec2 distance) {

        if(focus.getBody() == null) return;

        if(distance.x > 0 )
        {
            Stage1.tmpDynamic.add(new SwordAttack(
                    body.getWorld(),
                    body.getPosition().x / Stage1.M_PER_PIXEL,
                    body.getPosition().y / Stage1.M_PER_PIXEL,
                    owner,
                    false,
                    getAttack()));
            setState(State.AtkR);
        }
        else
        {
            Stage1.tmpDynamic.add(new SwordAttack(
                    body.getWorld(),
                    body.getPosition().x / Stage1.M_PER_PIXEL,
                    body.getPosition().y / Stage1.M_PER_PIXEL,
                    owner,
                    true,
                    getAttack()));
            setState(State.AtkL);
        }
    }

    private void setState(State state)
    {
        switch (state)
        {
            case idleR:
            case idleL:
            case RunL:
            case RunR:
                renderSpeed = 200;
                break;
            case AtkL:
            case AtkR:
                renderSpeed = 100;
                break;
            case dead:
                renderSpeed = 50;
                break;
        }

        this.state = state;
        spriteIndex = -1;
    }
}
