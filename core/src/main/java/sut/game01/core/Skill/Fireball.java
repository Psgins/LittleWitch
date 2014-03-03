package sut.game01.core.Skill;

import org.omg.DynamicAny._DynFixedStub;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import sut.game01.core.sprite.ObjectDynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static playn.core.PlayN.assets;

/**
 * Created by PSG on 2/26/14.
 */
public class Fireball implements ObjectDynamic {

    private boolean alive = true;
    private int timelife = 0;
    private int e = 0;

    private float x;
    private float y;

    private float angle = 0;

    private GroupLayer layer;
    private Image img;
    private boolean hasLoaded = false;

    List<Fireball_eff> coll = new ArrayList<Fireball_eff>();
    List<Fireball_eff> tmp = new ArrayList<Fireball_eff>();

    public Fireball(GroupLayer layer,float x, float y)
    {
        this.x = x;
        this.y = y;
        this.layer = layer;

        img = assets().getImage("images/fireball.png");
        img.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {
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
            coll.add(new Fireball_eff(img,layer,x,y));
            e = 0;
        }

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

        x+= 20;

        if(timelife >500)
        {
            alive = false;

            for(Fireball_eff x : coll)
                if(x.Alive()) x.destroy();
            coll.clear();
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
