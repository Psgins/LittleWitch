package sut.game01.core.character;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.util.Callback;
import sut.game01.core.screen.Stage1;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 3/13/14.
 */
public class Witch extends Character {

    public enum State {idleL,idleR,runL,runR,dead,atkR,atkL}
    private State state = State.idleR;

    public Witch(final World world, final GroupLayer layer, final float x, final float y)
    {
        sprite = SpriteLoader.getSprite("images/CharSprite/witch.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(75f / 2f, 84f / 2f);
                initPhysicsBody(world,x,y,75f - 40f,84f - 10f,false);

                AllLayer.add(sprite.layer());
                createHPbar(sprite.layer().tx(),sprite.layer().ty() - 50f,70f);

                layer.add(AllLayer);

                ready = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        owner = Owner.Ally;
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if (!ready || !alive) return;

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
                case runR:
                    offset = 8;
                    if(body.getLinearVelocity().x < 10) body.applyForce(new Vec2(40f,0),body.getPosition());
                    break;
                case  runL:
                    offset = 12;
                    if(body.getLinearVelocity().x > -10) body.applyForce(new Vec2(-40f, 0), body.getPosition());
                    break;
                case dead:
                    offset = 16;
                    if(spriteIndex == 18) alive = false;
                    break;
                case atkR:
                    offset = 20;
                    if(spriteIndex == 22) setState(State.idleR);
                    break;
                case atkL:
                    offset = 24;
                    if(spriteIndex == 26) setState(State.idleL);
                    break;
            }

            spriteIndex = offset + ((spriteIndex+1)%4);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }

    @Override
    public void paint() {
        super.paint();

        if (!ready) return;
            AllLayer.setTranslation(body.getPosition().x / Stage1.M_PER_PIXEL, body.getPosition().y / Stage1.M_PER_PIXEL);
    }

    public void setState (State state)
    {
        this.state = state;

        switch (state)
        {
            case idleL:
            case idleR:
            case dead:
                renderSpeed = 150;
                break;
            case runL:
            case runR:
                renderSpeed = 50;
                break;
            case atkR:
            case atkL:
                renderSpeed = 25;
                break;
        }

        spriteIndex = -1;
    }

    public State getState(){ return state;}

    public void jump()
    {
        if(body.getLinearVelocity().y == 0)
            body.applyLinearImpulse(new Vec2(0f,-45f),body.getPosition());
    }
}
