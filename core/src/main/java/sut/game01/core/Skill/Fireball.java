package sut.game01.core.Skill;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.util.Callback;
import sut.game01.core.character.Witch;
import sut.game01.core.all_etc.Skill;
import sut.game01.core.all_etc.Skills;
import sut.game01.core.screen.Game2D;
import sut.game01.core.all_etc.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.random;

/**
 * Created by PSG on 2/26/14.
 */
public class Fireball extends Skills implements WorldObject,Skill {

    //============ Skill information section ==================
    private float dmgBase = 20f;
    private float dmgAddition = 0;
    private float dmgRange = 5f;
    private SkillOwner owner;
    private int timelife = 500;
    //=========================================================

    private boolean alive = true;

    private int e = 0;
    private float y; // for keep floating

    private GroupLayer layer;
    private Image img;
    private boolean hasLoaded = false;
    private Body body;
    private World world;

    List<Fireball_eff> coll = new ArrayList<Fireball_eff>();
    List<Fireball_eff> tmp = new ArrayList<Fireball_eff>();

    public Fireball(final World world,GroupLayer layer,final float x,final float y,final boolean sideLeft,SkillOwner owner, float dmgAddition)
    {
        this.world = world;
        this.y = y;
        this.layer = layer;
        this.owner = owner;
        this.dmgAddition = dmgAddition;

        img = assets().getImage("images/fireball.png");
        img.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                body = initPhysicsBody(world,x * Game2D.M_PER_PIXEL,y * Game2D.M_PER_PIXEL,result.width()/5f,result.height()/5f);
                body.setFixedRotation(true);
                body.setBullet(true);

                if(sideLeft)
                    body.applyLinearImpulse(new Vec2(-4.7f,0f),body.getPosition());
                else
                    body.applyLinearImpulse(new Vec2(4.7f,0f),body.getPosition());

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
    }

    @Override
    public void update(int delta) {
        if(!alive || !hasLoaded) return;

        e+= delta;
        timelife -= delta;

        // create effect if still alive
        if(e >= 25 && timelife > 0)
        {
            coll.add(new Fireball_eff(img,layer,body.getPosition().x / Game2D.M_PER_PIXEL,body.getPosition().y / Game2D.M_PER_PIXEL));
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
                world.destroyBody(body);
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
    public void paint() {

    }

    @Override
    public boolean Alive()
    {
        return alive;
    }

    @Override
    public Body getBody()
    {
        return body;
    }

    @Override
    public void contact(WorldObject A, WorldObject B) {
        WorldObject other;

        if(A.getClass() == this.getClass())
            other = B;
        else
            other = A;

        if (other.getClass() != Witch.class)
        {
            timelife = 0;
        }

    }

    @Override
    public float getDmg() {
        float ranRange = (new Random()).nextInt() % dmgRange;
        float dmgTotal = (dmgBase+dmgAddition) +  ranRange;
        return dmgTotal;
    }

    @Override
    public SkillOwner getOwner() {
        return owner;
    }
}
