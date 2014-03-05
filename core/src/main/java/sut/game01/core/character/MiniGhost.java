package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Image;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.util.Callback;
import sut.game01.core.screen.Game2D;
import sut.game01.core.sprite.ObjectDynamic;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 3/5/14.
 */
public class MiniGhost extends CharacterObject implements ObjectDynamic {

    public enum State {idle,die}
    private State state = State.idle;

    private float px;
    private float py;

    private Body body;

    private Sprite sprite;
    private int spriteIndex = -1;
    private int offset = 0;
    private int renderspeed = 25;

    private int e = 0;
    private boolean alive = true;
    private boolean hasLoaded = false;
    private boolean barHasLoead  = false;

    private GroupLayer allLayer = PlayN.graphics().createGroupLayer();
    private ImageLayer hpBar;

    //ChatStatus
    private float hp = 200f;

    public MiniGhost(final World world,final GroupLayer layer,final float px,final float py)
    {
        this.px = px;
        this.py = py;

        sprite = SpriteLoader.getSprite("images/CharSprite/MiniGhost.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(80f /2f, 48f /2f);

                body = initPhysicsBody(world, px * Game2D.M_PER_PIXEL, py * Game2D.M_PER_PIXEL, 80f-40f, 48f);

                allLayer.add(sprite.layer());
                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });

        Image hpImg = PlayN.assets().getImage("images/etc/hpbar.png");
        hpImg.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {

                hpBar = PlayN.graphics().createImageLayer(result);

                float width = hpBar.width() * 0.5f;

                hpBar.setWidth(width);
                hpBar.setOrigin(width / 2f, result.height() /2f);
                allLayer.add(hpBar);

                barHasLoead = true;
            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });

        layer.add(allLayer);
    }

    @Override
    public void update(int delta) {
        if (!alive || !hasLoaded || !barHasLoead) return;

        e+= delta;

        if (e > renderspeed)
        {
            switch (state)
            {
                case idle:
                    offset = 0;
                    break;
            }

            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e= 0;
        }

        if(body.getPosition().y / Game2D.M_PER_PIXEL > py + 10) body.applyForce(new Vec2(0,-80f),body.getPosition());
    }

    @Override
    public void paint() {
        if(!alive || !hasLoaded || !barHasLoead) return;

        sprite.layer().setTranslation(body.getPosition().x / Game2D.M_PER_PIXEL,body.getPosition().y / Game2D.M_PER_PIXEL);
        hpBar.setTranslation(body.getPosition().x / Game2D.M_PER_PIXEL,(body.getPosition().y / Game2D.M_PER_PIXEL)- 25f);
    }

    @Override
    public boolean Alive() {
        return alive;
    }

    public void contact(Contact contact)
    {

    }
}
