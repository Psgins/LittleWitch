package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import sut.game01.core.screen.Game2D;
import sut.game01.core.all_etc.WorldObject;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by PSG on 2/6/14.
 */
public class Witch implements WorldObject {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hashLoaded = false;

    public enum State {idleL,idleR,runL,runR,dead,atk1}

    private int e = 0;
    private int offset = 0;
    private int renderSpeed = 150;

    private Body body;

    private State state = State.idleR;
    private boolean alive = true;

    public Witch(final World world, final float px, final float py,final boolean justShow)
    {
        String spritePath = justShow ? "images/Model/witch.json" : "images/CharSprite/witch.json";
        sprite = SpriteLoader.getSprite(spritePath);
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(px,py);

                hashLoaded = true;

                if(!justShow)
                    body = initPhysicsBody(world,px * Game2D.M_PER_PIXEL,py * Game2D.M_PER_PIXEL);
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
    }

    public void update(int delta)
    {
        if (!hashLoaded || !alive) return;

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
                    body.applyForce(new Vec2(50f,0),body.getPosition());
                    break;
                case  runL:
                    offset = 12;
                    body.applyForce(new Vec2(-50f, 0), body.getPosition());
                    break;
                case dead:
                    offset = 16;
                    if(spriteIndex == 18) alive = false;
                    break;
                case atk1:
                    offset = 20;
                    if(spriteIndex == 22) setState(State.idleR);
                    break;
            }

            spriteIndex = offset + ((spriteIndex+1)%4);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }

    public void paint()
    {
        if (!hashLoaded) return;
        sprite.layer().setTranslation(body.getPosition().x / Game2D.M_PER_PIXEL,body.getPosition().y / Game2D.M_PER_PIXEL);
    }

    public Layer layer()
    {
        return this.sprite.layer();
    }

    public void jump()
    {
        body.applyLinearImpulse(new Vec2(0f,-35f),body.getPosition());
    }

    @Override
    public boolean Alive() {
        return alive;
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public void contact(WorldObject A, WorldObject B) {

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
            case atk1:
                renderSpeed = 25;
                break;
        }

        spriteIndex = -1;
    }

    private Body initPhysicsBody(World world, float x, float y)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);

        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.layer().width()-40f) * Game2D.M_PER_PIXEL / 2, (sprite.layer().height()-10f) * Game2D.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f); // 0.2f
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }

    public boolean isHashLoaded()
    {
        return hashLoaded;
    }
}
