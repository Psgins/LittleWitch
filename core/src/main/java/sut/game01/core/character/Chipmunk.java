package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.Game2D;
import sut.game01.core.sprite.ObjectDynamic;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

public class Chipmunk implements ObjectDynamic {

    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hashLoaded = false;

    public enum State {idle,left,right,die}

    private int e = 0;
    private int renderSpeed = 75;
    private boolean working = true;
    private int offset = 0;

    private Body body;

    private State state = State.idle;
    private boolean alive = true;

    private float x=0;
    private float y=0;
    private float hp = 100;
    private float decreasetime = 1000;
    private float regen = 0;

    private GroupLayer ac = PlayN.graphics().createGroupLayer();
    private ImageLayer hpbar;

    public Chipmunk(final World world,final float x,final float y)
    {
        sprite = SpriteLoader.getSprite("images/chipmunk.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x,y);

                ac.add(sprite.layer());

                hpbar = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/hpbar.png"));
                hpbar.setOrigin(50f,7.5f);
                hpbar.setTranslation(x, y - 60);

                ac.add(hpbar);

                hashLoaded = true;
                working = true;

                body = initPhysicsBody(world,x * Game2D.M_PER_PIXEL,y * Game2D.M_PER_PIXEL);

                sprite.layer().addListener(new Pointer.Adapter(){
                    @Override
                    public void onPointerEnd(Pointer.Event event) {
                    }
                });
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });

        //this.x = x;
        //this.y = y;
    }

    public void update(int delta)
    {
        if (!hashLoaded || !working || !alive) return;

        e+= delta;
        decreasetime += delta;

        if(decreasetime >= 1000)
        {
            hp += regen;
            if(hp <= 0) state = State.die;

            decreasetime = 0;
            hpbar.setWidth(hpbar.width()*(hp/100f));
        }

        if (e > renderSpeed)
        {
            switch (state)
            {
                case idle:
                    offset = 0;
                    break;
                case left:
                    offset = 6;
                    x = x-5;
                    ac.setTranslation(x, y);
                    break;
                case  right:
                    offset = 6;
                    x = x+5;
                    ac.setTranslation(x, y);
                    break;
                case die:
                    renderSpeed = 150;
                    offset = 12;
                    if(spriteIndex == 16) working = false;
                    break;
            }

            spriteIndex = offset + ((spriteIndex+1)%6);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }

    public void paint()
    {
        if (!hashLoaded) return;
        sprite.layer().setTranslation(body.getPosition().x / Game2D.M_PER_PIXEL,body.getPosition().y / Game2D.M_PER_PIXEL);
    }

    @Override
    public boolean Alive() {
        return alive;
    }

    public void setState(State state)
    {
        this.state = state;
        e=0;
    }

    public Layer layer()
    {
        return this.ac;
    }

    private Body initPhysicsBody(World world, float x, float y)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90 * Game2D.M_PER_PIXEL / 2, sprite.layer().height() * Game2D.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }
}
