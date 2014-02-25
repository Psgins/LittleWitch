package sut.game01.core;

import playn.core.Game;

import playn.core.util.Clock;
import sut.game01.core.screen.StartScreen;
import tripleplay.game.ScreenStack;
import tripleplay.game.Screen;

public class MyGame extends Game.Default {

  public static final int UPDATE_RATE = 25;
  private ScreenStack ss = new ScreenStack();
  protected final Clock.Source clock = new Clock.Source(UPDATE_RATE);

  public MyGame() {
    super(UPDATE_RATE); // call update every 33ms (30 times per second)
  }



  @Override
  public void init() {

      final Screen home = new StartScreen(ss);
      ss.push(home);
  }

  @Override
  public void update(int delta) {
      ss.update(delta);
  }

  @Override
  public void paint(float alpha) {
      clock.paint(alpha);
      ss.paint(clock);
      // the background automatically paints itself, so no need to do anything here!
  }
}
