package sut.game01.java;

import playn.core.Game;
import playn.core.PlayN;
import playn.java.JavaPlatform;

import sut.game01.core.MyGame;

public class MyGameJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    Game.Default gd = new MyGame();
    PlayN.run(gd);
  }
}
