package sut.game01.core.Skill;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.screen.Stage1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PSG on 3/17/14.
 */
public class Screeming extends Skill {

    private float y;
    private int timelife = 0;

    List<Screeming_eff> coll = new ArrayList<Screeming_eff>();
    List<Screeming_eff> tmp = new ArrayList<Screeming_eff>();

    private int Pass = 99;

    public Screeming(World world, GroupLayer layer, float x, float y, Owner own, boolean isLeft, float moreDMG)
    {
        owner = own;
        AdditionDamage = moreDMG;
        layer.add(AllLayer);
        this.y = y;
        timelife = 1000;

        BaseDamage = 70;
        RangeDamage = 10;

        initPhysicsBody(world,x,y,100,100,true);
        body.getFixtureList().setDensity(0.03f);
        body.setBullet(true);
        body.setFixedRotation(true);

        if(isLeft)
            body.applyLinearImpulse(new Vec2(-8.5f,0f),body.getPosition());
        else
            body.applyLinearImpulse(new Vec2(8.5f,0f),body.getPosition());

        ready = true;
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if(!alive || !ready) return;

        e+= delta;
        timelife -= delta;

        // create effect if still alive
        if(e >= 25 && timelife > 0)
        {
            coll.add(new Screeming_eff(ImageStore.WhiteCircle,AllLayer,body.getPosition().x / VariableConstant.worldScale,body.getPosition().y / VariableConstant.worldScale));
            e = 0;
        }

        // update and make effect to trash
        for(Screeming_eff x : coll)
        {
            if(x.Alive())
                x.update(delta);
            else
                tmp.add(x);
        }

        // clear trash
        for(Screeming_eff x : tmp)
            coll.remove(x);
        tmp.clear();

        // when out of timelife
        if(timelife <= 0)
        {
            //destroy body when out of time
            if (body != null)
            {
                body.getWorld().destroyBody(body);
                body = null;
            }

            // keep doing effect if them don't dead and make this to trash when out of effect
            if(coll.size() <= 0)
                alive = false;
            else
                return;
        }

        // keep fireball floating
        if (body != null)
            if(body.getPosition().y / VariableConstant.worldScale > y + 5f) body.applyForce(new Vec2(0f,-40f),body.getPosition());
    }

    @Override
    public void destroy() {
        super.destroy();

        if(--Pass <= 0)
            timelife = 0;
    }

}
