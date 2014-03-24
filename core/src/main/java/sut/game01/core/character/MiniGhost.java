package sut.game01.core.character;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.util.Callback;
import sut.game01.core.Skill.Bubble;
import sut.game01.core.Skill.ItemCard;
import sut.game01.core.Skill.Skill;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.UI.FloatLabel;
import sut.game01.core.all_etc.GameEnvirontment;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.screen.Stage1;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

import java.util.Random;

/**
 * Created by PSG on 3/13/14.
 */
public class MiniGhost extends Character {

    public enum State {idle,die}
    private float y;

    private int DelayCount = 0;
    private int AttackDelay = 2000;
    private State state = State.idle;

    public MiniGhost(final GameEnvirontment gEnvir, final float x, final float y, Owner own)
    {
        this.y = y;
        this.gEnvir = gEnvir;

        hp = 400;
        maxHP = 400;
        attack = 10;
        defend = 3;

        sprite = SpriteLoader.getSprite("images/CharSprite/MiniGhost.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(80f /2f, 48f /2f);

                initPhysicsBody(gEnvir.world, x, y, 30f, 35f,true);

                createHPbar(sprite.layer().tx(),sprite.layer().ty()- 25,45);

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
                case idle:
                    offset = 0;
                    break;
                case die:
                    offset = 4;
                    if (spriteIndex == 7) {
                        dropItem();
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
        if(state == State.die) return;

        // SeekMain
        Vec2 tmpSeek = seekMain(gEnvir.main);
        if((tmpSeek.x > 7 || tmpSeek.x < -7) && tmpSeek.x != 999f)
        {
            Move2Main(gEnvir.main,tmpSeek,1);
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

        // Keep floating
        if(body.getPosition().y / VariableConstant.worldScale > y + 10) body.applyForce(new Vec2(0,-40f),body.getPosition());
    }

    @Override
    public void paint() {
        super.paint();

        if(!alive || !ready) return;
        AllLayer.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);
    }

    @Override
    public void contact(DynamicObject A, DynamicObject B) {

        if (!alive || state == State.die) return;

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
                    renderSpeed = 50;
                    state = State.die;
                    gEnvir.main.gainEXP(15);
                }
                skillObject.destroy();
            }
        }
    }

    @Override
    protected void AttackMain(Character focus, Vec2 distance) {

        if(focus.getBody() == null) return;

        boolean isLeft = distance.x > 0 ? false:true;

        gEnvir.tmpList.add(new Bubble(
                body.getWorld(),
                AllLayer.parent(),
                body.getPosition().x / VariableConstant.worldScale,
                body.getPosition().y / VariableConstant.worldScale,
                owner,
                isLeft,
                attack));
    }
}
