package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.Environment.EdgeLine;
import sut.game01.core.Skill.Fireball;
import sut.game01.core.all_etc.FloatLabel;
import sut.game01.core.character.MiniGhost;
import sut.game01.core.all_etc.WorldObject;
import sut.game01.core.character.Witch;
import sut.game01.core.all_etc.Skills;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import java.util.ArrayList;

public class Game2D extends Screen {
    // world variable
    public static float M_PER_PIXEL = 1 / 26.666667f;
    private  static int width = 48;
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
    private ArrayList<WorldObject> objCollection = new ArrayList<WorldObject>();
    private ArrayList<WorldObject> trash = new ArrayList<WorldObject>();
    private Witch main;

    //UIGroup
    private GroupLayer UIGroup = PlayN.graphics().createGroupLayer();

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
            CanvasImage image = PlayN.graphics().createImage(width / M_PER_PIXEL,480);
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

                WorldObject A = null;
                WorldObject B = null;

                for(WorldObject x : objCollection)
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

        objCollection.add(new EdgeLine(world,new Vec2(0,height-2.5f),new Vec2(width,height-2.5f)));
        objCollection.add(new EdgeLine(world,new Vec2(0,0),new Vec2(0,height)));
        objCollection.add(new EdgeLine(world,new Vec2(width,0),new Vec2(width,height)));

//      CubeBox box1 = new CubeBox(world,20,height-8,100,20);

        //character

        main = new Witch(world, 320,400, false);
        layer.add(main.layer());
        objCollection.add(main);

        objCollection.add(new MiniGhost(world,layer,400f,350f, Skills.SkillOwner.Enemy,fLabel));
        objCollection.add(new MiniGhost(world, layer, 500f, 350f, Skills.SkillOwner.Enemy, fLabel));
        objCollection.add(new MiniGhost(world,layer,400f,250f, Skills.SkillOwner.Enemy,fLabel));


        //UI
        ImageLayer HPBarUI = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/UI/hpbar.png"));
        UIGroup.add(HPBarUI);

        layer.add(UIGroup);

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

                        switch (main.getState())
                        {
                            case idleL:
                            case runL:
                            case atkL:
                                objCollection.add(new Fireball(world, layer, main.layer().tx() - 25f, main.layer().ty(),true, Skills.SkillOwner.Ally, 0));
                                main.setState(Witch.State.atkL);
                                break;
                            case idleR:
                            case runR:
                            case atkR:
                                objCollection.add(new Fireball(world, layer, main.layer().tx() + 25f, main.layer().ty(),false, Skills.SkillOwner.Ally, 0));
                                main.setState(Witch.State.atkR);
                                break;
                        }
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

        for(WorldObject cm : objCollection)
        {
            if (cm.Alive())
                cm.update(delta);
            else
                trash.add(cm);
        }

        if(main.isHashLoaded())
        {
            if( main.getBody().getWorldCenter().x < 12 || main.getBody().getWorldCenter().x > width - 12)
            {
                //do nothing
            }
            else
            {
                layer.setTranslation(320 - main.getBody().getWorldCenter().x / M_PER_PIXEL,0);
                UIGroup.setTranslation(0 - layer.tx(),0);
            }
        }

        //Update float label
        fLabel.update(delta);

        //Clear all trash
        for (WorldObject x : trash)
            objCollection.remove(x);
        trash.clear();
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if(ShowDebugDraw)
        {
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }

        for(WorldObject cm : objCollection) cm.paint();
    }
}
