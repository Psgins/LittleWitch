package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import sut.game01.core.Skill.Skill;
import sut.game01.core.UI.FloatLabel;
import sut.game01.core.all_etc.DynamicObject;
import sut.game01.core.all_etc.GameEnvirontment;
import sut.game01.core.all_etc.VariableConstant;
import sut.game01.core.screen.Stage1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by PSG on 3/22/14.
 */
public class Crytal1 extends Character {

    public enum State {Idle}

    private ImageLayer crytal;

    List<Character> monsterGen = new ArrayList<Character>();
    List<Character> trash = new ArrayList<Character>();

    private boolean dead = false;

    private Vec2 position;
    private State state = State.Idle;

    public Crytal1(final GameEnvirontment gEnvir, final float x, final float y, Owner own)
    {
        this.gEnvir = gEnvir;

        hp = 2500;
        maxHP = 2500;
        defend = 25;

        position = new Vec2(x * VariableConstant.worldScale, y * VariableConstant.worldScale);

        crytal = PlayN.graphics().createImageLayer(PlayN.assets().getImage("images/CharSprite/Crytal1_img.png"));
        crytal.setOrigin(100f /2f, 200f /2f);
        crytal.setScale(0.5f);

        initPhysicsBody(gEnvir.world, x, y, 30f, 90f, true);
        body.setLinearVelocity(new Vec2(0,-1.2f));

        AllLayer.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);

        createHPbar(crytal.tx(), crytal.ty() - 65, 60);

        AllLayer.add(crytal);
        gEnvir.layer.add(AllLayer);
        ready = true;

        owner = own;
    }

    @Override
    public void update(int delta) {

        if(!alive || !ready || !inScreen(gEnvir.main) || dead) return;

        e += delta;

        if(body.getPosition().y < position.y - 0.5f)
        {
            body.setLinearVelocity(new Vec2(0,1.2f));
        }
        else if(body.getPosition().y > position.y + 0.5f)
        {
            body.setLinearVelocity(new Vec2(0,-1.2f));
        }

        if(e >= 4000)
        {
            if(monsterGen.size() < 4)
                AutoGenerate();
            e = 0;
        }

        for(Character x : monsterGen)
        {
            if(!x.isAlive())
                trash.add(x);
        }

        monsterGen.removeAll(trash);
        trash.clear();

    }

    @Override
    public void paint() {

        if(!alive || !ready || !inScreen(gEnvir.main) || dead) return;
        AllLayer.setTranslation(body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale);
    }

    @Override
    public void initPhysicsBody(World world,float pX,float pY,float width, float height,boolean sensor)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KINEMATIC;
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

    @Override
    public void contact(DynamicObject A, DynamicObject B) {

        if (!alive || dead) return;

        DynamicObject other;

        if(A.getClass() == this.getClass())
            other = B;
        else
            other = A;

        if(other.getBody().isBullet())
        {
            Skill skillObject = (Skill)other;
            if(skillObject.getOwner() != owner)
            {
                float dmg = skillObject.getDamage() - defend;
                if (dmg < 0) dmg = 1;
                hp = (hp - dmg) < 0 ? 0 : (hp - dmg);
                HPBar.setWidth(HPBarWidth * (hp/maxHP));

                gEnvir.fLabel.CreateText((int)dmg,body.getPosition().x / VariableConstant.worldScale,(body.getPosition().y / VariableConstant.worldScale)-15f);

                if (hp <= 0)
                {
                    dead = true;
                    gEnvir.main.gainEXP(80);
                }
                skillObject.destroy();
            }
        }
    }

    public void AutoGenerate()
    {
        int ran = Math.abs((new Random()).nextInt()) % 2;
        Character monster = null;

        switch (ran)
        {
            case 0:
                gEnvir.tmpList.add(monster = new MiniGhost(gEnvir,(body.getPosition().x) / VariableConstant.worldScale, (body.getPosition().y-1) / VariableConstant.worldScale,owner));
                monsterGen.add(monster);
                break;
            case 1:

                gEnvir.tmpList.add(monster = new MiniGhost(gEnvir,(body.getPosition().x-1) / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale,owner));
                monsterGen.add(monster);
                gEnvir.tmpList.add(monster = new pinkporing(gEnvir,body.getPosition().x / VariableConstant.worldScale, body.getPosition().y / VariableConstant.worldScale,owner));
                monsterGen.add(monster);
                break;
        }
    }

    public boolean isDead()
    {
        return dead;
    }
}
