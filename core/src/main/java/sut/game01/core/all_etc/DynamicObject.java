package sut.game01.core.all_etc;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import sut.game01.core.screen.Stage1;

/**
 * Created by PSG on 3/13/14.
 */
public class DynamicObject {

    public enum Owner {Enemy,Ally}

    protected boolean alive = true;
    protected boolean ready = false;
    protected Body body = null;

    public void initPhysicsBody(World world,float pX,float pY,float width, float height,boolean sensor)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0,0);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * VariableConstant.worldScale / 2, height * VariableConstant.worldScale / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.05f;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = sensor;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f); // 0.2f
        body.setTransform(new Vec2(pX * VariableConstant.worldScale, pY * VariableConstant.worldScale), 0f);
    }

    public Body getBody()
    {
        return body;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public boolean isReady()
    {
        return ready;
    }

    public void update(int delta){}

    public void paint(){}

    public void destroy(){}

    public void contact(DynamicObject A, DynamicObject B){}
}
