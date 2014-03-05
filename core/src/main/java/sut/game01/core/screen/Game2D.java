package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.Skill.Fireball;
import sut.game01.core.all_etc.FloatLabel;
import sut.game01.core.character.MiniGhost;
import sut.game01.core.all_etc.ObjectDynamic;
import sut.game01.core.character.Witch;
import sut.game01.core.all_etc.Skills;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import java.util.ArrayList;

public class Game2D extends Screen {
    // world variable
    public static float M_PER_PIXEL = 1 / 26.666667f;
    private  static int width = 24;
    private  static int height = 18;
    private World world;

    // showdebug
    private boolean ShowDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    //Screen
    private final ScreenStack ss;

    //FloatLabel
    private FloatLabel fLabel = new FloatLabel(layer);

    // list collection
    private ArrayList<ObjectDynamic> objCollection = new ArrayList<ObjectDynamic>();
    private ArrayList<ObjectDynamic> trash = new ArrayList<ObjectDynamic>();

    public Game2D(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();

        ImageLayer bgLayer = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/GameScreen/GameScreen.png"));
        layer.add(bgLayer);

        // set world
        Vec2 gravity = new Vec2(0.0f,50f);
        world = new World(gravity, true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        if(ShowDebugDraw)
        {
            CanvasImage image = PlayN.graphics().createImage(640,480);
            layer.add(PlayN.graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,1f / Game2D.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                ObjectDynamic A = null;
                ObjectDynamic B = null;

                for(ObjectDynamic x : objCollection)
                {
                    if (x.getBody() == contact.getFixtureA().getBody())
                        A = x;
                    else if (x.getBody() == contact.getFixtureB().getBody())
                        B = x;

                    if (A != null && B != null)
                    {
                        A.contact(A,B);
                        B.contact(A,B);

                        break;
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });

        //Environment

        Body ground = world.createBody(new BodyDef());
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsEdge(new Vec2(0,height-2.5f),new Vec2(width,height-2.5f));
        ground.createFixture(groundShape,0.0f);

        Body blockLeft = world.createBody(new BodyDef());
        PolygonShape blockLeftShape = new PolygonShape();
        blockLeftShape.setAsEdge(new Vec2(0,0),new Vec2(0,height));
        blockLeft.createFixture(blockLeftShape,0.0f);

        Body blockRight = world.createBody(new BodyDef());
        PolygonShape blockRightShape = new PolygonShape();
        blockRightShape.setAsEdge(new Vec2(width,0),new Vec2(width,height));
        blockRight.createFixture(blockRightShape,0.0f);


//      CubeBox box1 = new CubeBox(world,20,height-8,100,20);

        //character

        final Witch main = new Witch(world, 250,400, false);
        layer.add(main.layer());
        objCollection.add(main);

        objCollection.add(new MiniGhost(world,layer,400f,350f, Skills.SkillOwner.Enemy,fLabel));
        objCollection.add(new MiniGhost(world,layer,500f,350f, Skills.SkillOwner.Enemy,fLabel));
        objCollection.add(new MiniGhost(world,layer,400f,250f, Skills.SkillOwner.Enemy,fLabel));

        // controller
        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                super.onKeyDown(event);

                switch (event.key()) {
                    case A:
                    case LEFT:
                        main.setState(Witch.State.runL);
                        break;
                    case D:
                    case RIGHT:
                        main.setState(Witch.State.runR);
                        break;
                    case SPACE:
                        main.jump();
                        break;
                    case ENTER:
                        main.setState(Witch.State.atk1);
                        objCollection.add(new Fireball(world,layer,main.layer().tx() + 25f,main.layer().ty(), Skills.SkillOwner.Ally,0));
                        break;
                }
            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                super.onKeyUp(event);

                switch (event.key()) {
                    case A:
                    case LEFT:
                        main.setState(Witch.State.idleL);
                        break;
                    case D:
                    case RIGHT:
                        main.setState(Witch.State.idleR);
                        break;
                    case ESCAPE:
                        ss.remove(Game2D.this);
                        break;
                }
            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        world.step(0.033f,10,10);

        for(ObjectDynamic cm : objCollection)
        {
            if (cm.Alive())
                cm.update(delta);
            else
                trash.add(cm);
        }

        for (ObjectDynamic x : trash)
            objCollection.remove(x);
        trash.clear();

        fLabel.update(delta);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if(ShowDebugDraw)
        {
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }

        for(ObjectDynamic cm : objCollection) cm.paint();
    }
}
