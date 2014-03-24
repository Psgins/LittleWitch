package sut.game01.core.all_etc;

import sut.game01.core.Skill.Skill;

/**
 * Created by PSG on 3/20/14.
 */
public class VariableConstant {
    public static final float worldScale = 1 / 26.666667f;
    public static final int worldGround = 15;

    public static final int itemIDRange = 2;
    public static final int runeIDRange = 1;

    public static final float[] dmgLVL = new float[]{
            2,
            7,
            15,
            32,
            57,
            86,
            110,
            154,
            223
    };

    public static final float[] hpLVL = new float[]{
            0,
            25,
            60,
            127,
            190,
            441,
            716,
            1064,
            1252,
    };

    public static final float[] defLVL = new float[]{
            0,
            2,
            3,
            5,
            9,
            15,
            30,
            42,
            63
    };

    public static final int[] expRange = new int[] {
            100,
            200,
            400,
            800,
            1600,
            3200,
            6400,
            13800,
            27600,
            55200
    };

    public static final String[] petPath = new String[]{
            "images/Pet/MiniSpirit.json",
            "images/Pet/Cloud.json"
    };

    public static final String[] CharPeth = new String[] {
            "images/CharSprite/witch.json",
            "images/CharSprite/Demon.json"
    };

    public static final String[] ModelPeth = new String[] {
            "images/Model/witch.json",
            "images/Model/Demon.json"
    };
}
