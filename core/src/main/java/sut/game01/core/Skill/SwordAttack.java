package sut.game01.core.Skill;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.screen.Stage1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PSG on 3/18/14.
 */
public class SwordAttack extends Skill {
    private float y;
    private int timelife = 0;

    public SwordAttack(World world, float x, float y, DynamicObject.Owner own, boolean isLeft, float moreDMG)
    {
        owner = own;
        AdditionDamage = moreDMG;

        timelife = 100;

        BaseDamage = 10;
        RangeDamage = 10;

        initPhysicsBody(world,x,y,20,20,true);
        body.setBullet(true);
        body.setFixedRotation(true);

        if(isLeft)
            body.applyLinearImpulse(new Vec2(-5.7f,0f),body.getPosition());
        else
            body.applyLinearImpulse(new Vec2(5.7f,0f),body.getPosition());

        ready = true;
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if(!alive || !ready) return;

        e+= delta;
        timelife -= delta;

        // when out of timelife
        if(timelife <= 0)
        {
            //destroy body when out of time
            if (body != null)
            {
                body.getWorld().destroyBody(body);
                body = null;
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        timelife = 0;
    }
}
