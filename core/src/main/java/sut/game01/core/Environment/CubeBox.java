package sut.game01.core.Environment;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import sut.game01.core.screen.Game2D;

/**
 * Created by PSG on 2/25/14.
 */
public class CubeBox {

    World world;
    Body body;

    float x;
    float y;

    public CubeBox(World world,float x, float y,float width, float height)
    {
        this.world = world;
        this.x = x;
        this.y = y;

        body = initPhysicsBody(world, x, y,width,height);
    }

    private Body initPhysicsBody(World world, float x, float y, float width, float height)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * Game2D.M_PER_PIXEL / 2, height * Game2D.M_PER_PIXEL / 2);
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
