package sut.game01.core.Skill;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.omg.DynamicAny._DynFixedStub;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import sut.game01.core.screen.Game2D;
import sut.game01.core.sprite.ObjectDynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static playn.core.PlayN.assets;

/**
 * Created by PSG on 2/26/14.
 */
public class Fireball extends Skill implements ObjectDynamic {

    private boolean alive = true;
    private int timelife = 0;
    private int e = 0;

    private float x;
    private float y;

    private float angle = 0;

    private GroupLayer layer;
    private Image img;
    private boolean hasLoaded = false;
    private Body body;
    private World world;

    List<Fireball_eff> coll = new ArrayList<Fireball_eff>();
    List<Fireball_eff> tmp = new ArrayList<Fireball_eff>();

    public Fireball(final World world,GroupLayer layer,final float x,final float y)
    {
        this.world = world;
        this.x = x;
        this.y = y;
        this.layer = layer;

        img = assets().getImage("images/fireball.png");
        img.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
                body = initPhysicsBody(world,x * Game2D.M_PER_PIXEL,y * Game2D.M_PER_PIXEL,result.width()/2.5f,result.height()/2.5f);
                body.setFixedRotation(true);
                body.setBullet(true);

                body.applyLinearImpulse(new Vec2(30f,0f),body.getPosition());

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
        timelife += delta;

        if(e >= 25)
        {
            coll.add(new Fireball_eff(img,layer,body.getPosition().x / Game2D.M_PER_PIXEL,body.getPosition().y / Game2D.M_PER_PIXEL));
            e = 0;
        }

        if(body.getPosition().y / Game2D.M_PER_PIXEL < y + 20f) body.applyLinearImpulse(new Vec2(0f,-1.6f),body.getPosition());

        for(Fireball_eff x : coll)
        {
            if(x.Alive())
                x.update(delta);
            else
                tmp.add(x);
        }

        for(Fireball_eff x : tmp)
            coll.remove(x);

        tmp.clear();

        if(timelife >500)
        {
            alive = false;

            for(Fireball_eff x : coll)
                if(x.Alive()) x.destroy();
            coll.clear();

            world.destroyBody(body);
        }
    }

    @Override
    public void paint() {

    }

    public boolean Alive()
    {
        return alive;
    }
}
