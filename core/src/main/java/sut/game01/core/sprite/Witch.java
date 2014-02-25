package sut.game01.core.sprite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.Game2D;

/**
 * Created by PSG on 2/6/14.
 */
public class Witch implements GameCharacter {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hashLoaded = false;

    public enum State {idleL,idleR,runL,runR,dead}

    private int e = 0;
    private int offset = 0;
    private int renderSpeed = 150;

    private Body body;

    private State state = State.idleR;

    public Witch(final World world, final float px, final float py)
    {
        sprite = SpriteLoader.getSprite("images/witch.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(px,py);

                hashLoaded = true;

                body = initPhysicsBody(world,px * Game2D.M_PER_PIXEL,py * Game2D.M_PER_PIXEL);

                sprite.layer().addListener(new Pointer.Adapter(){
                    @Override
                    public void onPointerEnd(Pointer.Event event) {
                        //body.applyLinearImpulse(new Vec2(100f,0f), body.getPosition());
                        setState(State.dead);
                    }
                });
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });


    }

    public void update(int delta)
    {
        if (!hashLoaded) return;

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
                    body.applyForce(new Vec2(20f,0),body.getPosition());
                    break;
                case  runL:
                    offset = 12;
                    body.applyForce(new Vec2(-20f, 0), body.getPosition());
                    break;
                case dead:
                    offset = 16;
                    if(spriteIndex == 18) hashLoaded = false;
                    break;
            }

            spriteIndex = offset + ((spriteIndex+1)%4);
            sprite.setSprite(spriteIndex);
            e = 0;
        }
    }

    public void paint(Clock clock)
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
        body.applyLinearImpulse(new Vec2(0f,-20f),body.getPosition());
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
        }
    }

    private Body initPhysicsBody(World world, float x, float y)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);

        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((sprite.layer().width()-40f) * Game2D.M_PER_PIXEL / 2, sprite.layer().height() * Game2D.M_PER_PIXEL / 2);
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
}
