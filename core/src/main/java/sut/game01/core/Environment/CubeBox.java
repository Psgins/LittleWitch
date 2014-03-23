package sut.game01.core.Environment;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import sut.game01.core.all_etc.ImageStore;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.screen.Stage1;

/**
 * Created by PSG on 2/25/14.
 */
public class CubeBox {

    World world;
    Body body;

    float x;
    float y;

    public CubeBox(World world,GroupLayer layer,float x, float y)
    {
        this.world = world;
        this.x = x;
        this.y = y;

        ImageLayer box = PlayN.graphics().createImageLayer(ImageStore.box);
        box.setSize(50,25);
        box.setOrigin(50 / 2f , 25 /2f);

        body = initPhysicsBody(world, x * VariableConstant.worldScale, y * VariableConstant.worldScale,50,25);
        box.setTranslation(body.getPosition().x / VariableConstant.worldScale,body.getPosition().y / VariableConstant.worldScale);

        layer.add(box);
    }

    private Body initPhysicsBody(World world, float x, float y, float width, float height)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0,0);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * VariableConstant.worldScale / 2, height * VariableConstant.worldScale / 2);
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
