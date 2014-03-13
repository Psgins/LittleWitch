package sut.game01.core.Environment;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import sut.game01.core.all_etc.WorldObject;

/**
 * Created by PSG on 3/12/14.
 */
public class EdgeLine {

    Body body;

    public EdgeLine(World world,Vec2 point1, Vec2 point2)
    {
        body = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsEdge(point1,point2);
        body.createFixture(groundShape,0.0f);
    }
}
