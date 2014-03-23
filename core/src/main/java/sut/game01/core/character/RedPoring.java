package sut.game01.core.character;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.util.Callback;
import sut.game01.core.Skill.FireProject;
import sut.game01.core.Skill.Fireball;
import sut.game01.core.Skill.Skill;
import sut.game01.core.Skill.SwordAttack;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.UI.FloatLabel;
import sut.game01.core.all_etc.GameEnvirontment;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.screen.Stage1;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 3/18/14.
 */
public class RedPoring extends Character {
    public enum State {idleL,idleR,RunL,RunR,AtkL,AtkR,dead}

    private State state = State.RunL;

    private int DelayCount = 0;
    private int AttackDelay = 1000;

    public RedPoring(final GameEnvirontment gEnvir, final float x, final float y, Owner own)
    {
        renderSpeed = 100;
        this.gEnvir = gEnvir;

        hp = 450;
        maxHP = 450;
        attack = 45;
        defend = 5;

        sprite = SpriteLoader.getSprite("images/CharSprite/redPoring.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(50f, 60f);
                sprite.layer().setScale(.7f);

                initPhysicsBody(gEnvir.world, x, y, 42f, 42f, false);
                body.setLinearDamping(1f);

                createHPbar(sprite.layer().tx(),sprite.layer().ty()- 35f,60);

                AllLayer.add(sprite.layer());
                gEnvir.layer.add(AllLayer);
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

        if (!alive || !ready || !inScreen(gEnvir.main)) return;

        e+= delta;

        if (e > renderSpeed)
        {
            switch (state)
            {
                case idleR:
                case idleL:
                    offset = 0;
                    break;
                case RunR:
                case RunL:
                    offset = 4;
                    break;
                case AtkL:
                    offset = 4;
                    if(spriteIndex == 6) setState(State.idleL);
                    break;
                case AtkR:
                    offset = 4;
                    if(spriteIndex == 6) setState(State.idleR);
                    break;
                case dead:
                    offset = 8;
                    if (spriteIndex == 10)
                    {
                        dropItem();
                        AllLayer.parent().remove(AllLayer);
                        body.getWorld().destroyBody(body);
                        alive = false;
                        return;
                    }
            }

            switch (state)
            {
                case idleL:
                case RunL:
                case AtkL:
                    sprite.layer().setWidth(100);
                    sprite.layer().setOrigin(50,60);
                    break;
                case idleR:
                case RunR:
                case AtkR:
                    sprite.layer().setWidth(-100);
                    sprite.layer().setOrigin(-50,60);
                    break;
            }

            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e= 0;
        }

        // reject statements below when dead
        if(state == State.dead) return;

        // SeekMain
        Vec2 tmpSeek = seekMain(gEnvir.main);
        if((tmpSeek.x > 12 || tmpSeek.x < -12) && tmpSeek.x != 999f)
        {
            // do nothing
        }
        else
        {
            DelayCount += delta;
            if (DelayCount >= AttackDelay)
            {
                AttackMain(gEnvir.main,tmpSeek);
                DelayCount = 0;
            }
        }
    }

    @Override
    public void paint() {
        super.paint();

        if(!alive || !ready) return;
        AllLayer.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);
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

                gEnvir.fLabel.CreateText((int)dmg,body.getPosition().x / VariableConstant.worldScale,(body.getPosition().y / VariableConstant.worldScale)-15f);

                if (hp <= 0)
                {
                    setState(State.dead);
                    gEnvir.main.gainEXP(30);
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
            gEnvir.tmpList.add(new FireProject(
                    body.getWorld(),
                    AllLayer.parent(),
                    body.getPosition().x / VariableConstant.worldScale,
                    body.getPosition().y / VariableConstant.worldScale,
                    owner,
                    false,
                    getAttack()));
            setState(State.AtkR);
        }
        else
        {
            gEnvir.tmpList.add(new FireProject(
                    body.getWorld(),
                    AllLayer.parent(),
                    body.getPosition().x / VariableConstant.worldScale,
                    body.getPosition().y / VariableConstant.worldScale,
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
