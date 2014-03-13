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
import sut.game01.core.Skill.Fireball2;
import sut.game01.core.all_etc.*;
import sut.game01.core.character.*;
import sut.game01.core.character.Character;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import java.util.ArrayList;
import java.util.List;

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
    //private ArrayList<WorldObject> objCollection = new ArrayList<WorldObject>();

    private List<DynamicObject> objDynamic = new ArrayList<DynamicObject>();
    private ArrayList<DynamicObject> trash = new ArrayList<DynamicObject>();
    private Witch2 main;

    public static ImageStore imageStore = new ImageStore();

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

                DynamicObject A = null;
                DynamicObject B = null;

                for(DynamicObject x : objDynamic)
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

        new EdgeLine(world,new Vec2(0,height-2.5f),new Vec2(width,height-2.5f));
        new EdgeLine(world,new Vec2(0,0),new Vec2(0,height));
        new EdgeLine(world,new Vec2(width,0),new Vec2(width,height));

//      CubeBox box1 = new CubeBox(world,20,height-8,100,20);

        //character

        main = new Witch2(world,layer, 320,0);
        objDynamic.add(main);

        objDynamic.add(new MiniGhost2(world,layer,600f,350f, Character.Owner.Enemy,fLabel));


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
                        main.setState(Witch2.State.runL);
                        break;
                    case D:
                    case RIGHT:
                        main.setState(Witch2.State.runR);
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
                                objDynamic.add(new Fireball2(world,layer,(main.getBody().getPosition().x / M_PER_PIXEL) - 25f,(main.getBody().getPosition().y / M_PER_PIXEL), DynamicObject.Owner.Ally,true,main.getAttack()));
                                main.setState(Witch2.State.atkL);
                                break;
                            case idleR:
                            case runR:
                            case atkR:
                                objDynamic.add(new Fireball2(world,layer,(main.getBody().getPosition().x / M_PER_PIXEL) + 25f,(main.getBody().getPosition().y / M_PER_PIXEL), DynamicObject.Owner.Ally,false,main.getAttack()));
                                main.setState(Witch2.State.atkR);
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
                        main.setState(Witch2.State.idleL);
                        break;
                    case D:
                    case RIGHT:
                        main.setState(Witch2.State.idleR);
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

        if(!imageStore.isReady()) return;

        world.step(0.033f,10,10);

        for(DynamicObject x : objDynamic){
            if(x.isAlive())
                x.update(delta);
            else
                trash.add(x);
        }

        if(main.isReady())
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
        for (DynamicObject x : trash)
            objDynamic.remove(x);
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

        for(DynamicObject x : objDynamic) x.paint();
    }
}
