package sut.game01.core.all_etc;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import sut.game01.core.screen.Game2D;

/**
 * Created by PSG on 3/4/14.
 */
public class Skills {

    public enum SkillOwner {Ally,Enemy}

    protected Body initPhysicsBody(World world, float x, float y,float width, float height)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);

        Body body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * Game2D.M_PER_PIXEL / 2, height * Game2D.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f); // 0.2f
        body.setTransform(new Vec2(x,y), 0f);
        return body;
    }
}
