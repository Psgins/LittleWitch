package sut.game01.core.Skill;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.screen.Game2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PSG on 3/13/14.
 */
public class Fireball2 extends Skill {

    private float y;
    private int timelife = 0;

    List<Fireball_eff> coll = new ArrayList<Fireball_eff>();
    List<Fireball_eff> tmp = new ArrayList<Fireball_eff>();

    public Fireball2(World world,GroupLayer layer,float x, float y,Owner own,boolean isLeft,float moreDMG)
    {
        owner = own;
        AdditionDamage = moreDMG;
        layer.add(AllLayer);
        this.y = y;
        timelife = 500;

        initPhysicsBody(world,x,y,20,20,true);
        body.setBullet(true);
        body.setFixedRotation(true);

        if(isLeft)
            body.applyLinearImpulse(new Vec2(-4.7f,0f),body.getPosition());
        else
            body.applyLinearImpulse(new Vec2(4.7f,0f),body.getPosition());

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
            coll.add(new Fireball_eff(Game2D.imageStore.Fireball,AllLayer,body.getPosition().x / Game2D.M_PER_PIXEL,body.getPosition().y / Game2D.M_PER_PIXEL));
            e = 0;
        }

        // update and make effect to trash
        for(Fireball_eff x : coll)
        {
            if(x.Alive())
                x.update(delta);
            else
                tmp.add(x);
        }

        // clear trash
        for(Fireball_eff x : tmp)
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
            if(body.getPosition().y / Game2D.M_PER_PIXEL > y + 10f) body.applyForce(new Vec2(0f,-30f),body.getPosition());
    }

    @Override
    public void destroy() {
        super.destroy();

        timelife = 0;
    }
}
