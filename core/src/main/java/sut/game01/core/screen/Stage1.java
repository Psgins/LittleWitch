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
import sut.game01.core.Environment.CubeBox;
import sut.game01.core.Environment.EdgeLine;
import sut.game01.core.Pet.MiniSpirit;
import sut.game01.core.Rune.Rune;
import sut.game01.core.Skill.*;
import sut.game01.core.UI.FloatLabel;
import sut.game01.core.UI.HPBarUI;
import sut.game01.core.UI.SkillCardUI;
import sut.game01.core.all_etc.*;
import sut.game01.core.character.*;
import sut.game01.core.character.Character;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import java.util.ArrayList;
import java.util.List;

public class Stage1 extends Screen {
    public enum GameState {load,run,end}

    // world variable
    private  static int width = 96;
    private  static int height = 18;
    private World world;

    // showdebug
    private boolean ShowDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    //Screen
    private final ScreenStack ss;

    //FloatLabel
    private FloatLabel fLabel = new FloatLabel(layer);

    // countdown
    Countdown cd;

    // list collection
    private List<DynamicObject> tmpDynamic = new ArrayList<DynamicObject>();
    private List<DynamicObject> objDynamic = new ArrayList<DynamicObject>();
    private List<DynamicObject> trash = new ArrayList<DynamicObject>();
    private Witch main;
    private Crytal1 boss;
    private HPBarUI hpBarUI;
    private GameContent gContent;
    private GameEnvirontment gEnvir = new GameEnvirontment();

    private SkillCardUI SkillUI;
    private SkillCard[] skill = new SkillCard[4];

    //Game State
    private GameState gameState = GameState.run;

    //UIGroup
    private GroupLayer UIGroup = PlayN.graphics().createGroupLayer();

    public Stage1(ScreenStack ss,GameContent gContent)
    {
        this.ss = ss;
        this.gContent = gContent;
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
            CanvasImage image = PlayN.graphics().createImage(width / VariableConstant.worldScale,480);
            layer.add(PlayN.graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
            debugDraw.setCamera(0,0,1f / VariableConstant.worldScale);
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
        new EdgeLine(world,new Vec2(0,height-3f),new Vec2(width,height-3f));
        new EdgeLine(world,new Vec2(0,0),new Vec2(0,height));
        new EdgeLine(world,new Vec2(width,0),new Vec2(width,height));
        new CubeBox(world,layer,600,387);
        new CubeBox(world,layer,650,387);
        new CubeBox(world,layer,700,387);
        new CubeBox(world,layer,625,362);
        new CubeBox(world,layer,675,362);

        //Setup Game Environtment
        gEnvir.world = world;
        gEnvir.layer = layer;
        gEnvir.fLabel = fLabel;
        gEnvir.tmpList = tmpDynamic;
        gEnvir.gContent = gContent;

        //character
        // - Main
        main = new Witch(gEnvir, 320,312);
        objDynamic.add(main);
        gEnvir.main = main;

        // - Pet
        Rune rune = ContentLoader.RuneLoader(gContent.getRune());
        if(rune == null)
        {
            ImageLayer runeIcon = PlayN.graphics().createImageLayer(ImageStore.NullSkill);
            runeIcon.setTranslation(565,15);
            UIGroup.add(runeIcon);
        }
        else
        {
            UIGroup.add(rune.getIcon().setTranslation(565,15));
        }

        objDynamic.add(new MiniSpirit(world,layer,main,rune));

        // - Monster
//        objDynamic.add(new MiniGhost(gEnvir,20 / VariableConstant.worldScale,325f, Character.Owner.Enemy));
//        objDynamic.add(new MiniGhost(gEnvir,40 / VariableConstant.worldScale,325f, Character.Owner.Enemy));
//        objDynamic.add(new MiniGhost(gEnvir,60 / VariableConstant.worldScale,325f, Character.Owner.Enemy));
//        objDynamic.add(new MiniGhost(gEnvir,80 / VariableConstant.worldScale,275f, Character.Owner.Enemy));
//        objDynamic.add(new pinkporing(gEnvir,20 / VariableConstant.worldScale, 330, Character.Owner.Enemy));
        objDynamic.add(new RedPoring(gEnvir,650f,337,Character.Owner.Enemy));

        // - Boss
        boss = new Crytal1(gEnvir,(width-12) / VariableConstant.worldScale,325,Character.Owner.Enemy);
        objDynamic.add(boss);

        //UI
        hpBarUI = new HPBarUI(main,UIGroup);
        gEnvir.hpBarUI = hpBarUI;
        skill = ContentLoader.SkillCardLoader(gContent.getSkill());
        SkillUI = new SkillCardUI(main,UIGroup,tmpDynamic,skill);
        layer.add(UIGroup);
        cd = new Countdown(UIGroup,10,0);

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
                                objDynamic.add(new Fireball(world,layer,(main.getBody().getPosition().x / VariableConstant.worldScale) - 25f,(main.getBody().getPosition().y / VariableConstant.worldScale), DynamicObject.Owner.Ally,true,main.getAttack()));
                                main.setState(Witch.State.atkL);
                                break;
                            case idleR:
                            case runR:
                            case atkR:
                                objDynamic.add(new Fireball(world,layer,(main.getBody().getPosition().x / VariableConstant.worldScale) + 25f,(main.getBody().getPosition().y / VariableConstant.worldScale), DynamicObject.Owner.Ally,false,main.getAttack()));
                                main.setState(Witch.State.atkR);
                                break;
                        }
                        break;

                    case K1:
                        switch (main.getState())
                        {
                            case idleL:
                            case runL:
                            case atkL:
                                if(SkillUI.Shoot(true, 0))
                                    main.setState(Witch.State.atkL);
                                break;
                            case idleR:
                            case runR:
                            case atkR:
                                if(SkillUI.Shoot(false, 0))
                                    main.setState(Witch.State.atkR);
                                break;
                        }
                        break;
                    case K2:
                        switch (main.getState())
                        {
                            case idleL:
                            case runL:
                            case atkL:
                                if(SkillUI.Shoot(true, 1))
                                    main.setState(Witch.State.atkL);
                                break;
                            case idleR:
                            case runR:
                            case atkR:
                                if(SkillUI.Shoot(false, 1))
                                    main.setState(Witch.State.atkR);
                                break;
                        }
                        break;
                    case K3:
                        switch (main.getState())
                        {
                            case idleL:
                            case runL:
                            case atkL:
                                if(SkillUI.Shoot(true, 2))
                                    main.setState(Witch.State.atkL);
                                break;
                            case idleR:
                            case runR:
                            case atkR:
                                if(SkillUI.Shoot(false, 2))
                                    main.setState(Witch.State.atkR);
                                break;
                        }
                        break;
                    case K4:
                        switch (main.getState())
                        {
                            case idleL:
                            case runL:
                            case atkL:
                                if(SkillUI.Shoot(true, 3))
                                    main.setState(Witch.State.atkL);
                                break;
                            case idleR:
                            case runR:
                            case atkR:
                                if(SkillUI.Shoot(false, 3))
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
                        gContent.setLevel(main.getLevel());
                        gContent.setExp(main.getExp());
                        gContent.save();
                        ss.remove(Stage1.this);
                        break;
                }
            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        if(gameState != GameState.run) return;

        world.step(0.033f,10,10);

        // Update all object in list
        updateColection(delta);

        if(main.isReady())
        {
            if( main.getBody().getWorldCenter().x < 12 || main.getBody().getWorldCenter().x > width - 12)
            {
                //do nothing
            }
            else
            {
                layer.setTranslation(320 - main.getBody().getWorldCenter().x / VariableConstant.worldScale,0);
                UIGroup.setTranslation(0 - layer.tx(),0);
            }
        }

        // Check Game Condition
        if(main.isDead() || cd.isTimeout())
        {
            GameOver();
            gContent.setExp(main.getExp());
            gContent.setLevel(main.getLevel());
            gContent.save();
        }
        if(boss.isDead())
        {
            GameClear();
            gContent.setExp(main.getExp());
            gContent.setLevel(main.getLevel());
            gContent.save();
        }

        //Update Component
        cd.update(delta);
        fLabel.update(delta);
        SkillUI.update(delta);
        hpBarUI.update();

    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);

        if(gameState != GameState.run) return;

        if(ShowDebugDraw)
        {
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }

        for(DynamicObject x : objDynamic) x.paint();
    }

    public void updateColection(int delta)
    {
        // Move object from temp to Collection
        objDynamic.addAll(tmpDynamic);
        tmpDynamic.clear();

        // Update All object in Collection and clear trash
        for(DynamicObject x : objDynamic){
            if(x.isAlive())
                x.update(delta);
            else
                trash.add(x);
        }

        //Clear all trash
        objDynamic.removeAll(trash);
        trash.clear();
    }

    public void GameOver()
    {
        gameState = GameState.end;

        GroupLayer overGroup = PlayN.graphics().createGroupLayer();
        overGroup.setTranslation(320,200);
        UIGroup.add(overGroup);

        ImageLayer overIMG = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/ui/gameover.png"));
        overIMG.setOrigin(180,50);
        overGroup.add(overIMG);

        ImageLayer backIMG = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapScreen/back.png"));
        backIMG.setOrigin(60,25);
        backIMG.setTranslation(0,70);
        overGroup.add(backIMG);

        backIMG.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gContent.setLevel(main.getLevel());
                gContent.setExp(main.getExp());
                ss.remove(ss.top());
            }
        });
    }

    public void GameClear()
    {
        gameState = GameState.end;

        GroupLayer overGroup = PlayN.graphics().createGroupLayer();
        overGroup.setTranslation(320,200);
        UIGroup.add(overGroup);

        ImageLayer overIMG = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/ui/clear.png"));
        overIMG.setOrigin(180,50);
        overGroup.add(overIMG);

        ImageLayer backIMG = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/MapScreen/back.png"));
        backIMG.setOrigin(60,25);
        backIMG.setTranslation(0,70);
        overGroup.add(backIMG);

        backIMG.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                gContent.setLevel(main.getLevel());
                gContent.setExp(main.getExp());
                ss.remove(ss.top());
            }
        });
    }
}
