package sut.game01.core.screen;

import playn.core.Font;
import playn.core.PlayN;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

/**
 * Created by PSG on 1/22/14.
 */
public class StartScreen extends UIScreen {

    public static  final Font TITLE_FONT = PlayN.graphics().createFont(
            "Helvetica",
            Font.Style.BOLD,
            24
    );

    private Root root;


    private final ScreenStack ss;

    public StartScreen(ScreenStack ss)
    {
        this.ss = ss;
    }

    @Override
    public void wasShown() {
        super.wasShown();

        root = iface.createRoot(AxisLayout.vertical().gap(15), SimpleStyles.newSheet(),layer);
        root.addStyles(Style.BACKGROUND.is(Background.bordered(0xFFCCCCCC,0xFF99CCFF,5).inset(5,10)));
        root.setSize(width(),height());

        root.add(new Label("Start Screen").addStyles(Style.FONT.is(StartScreen.TITLE_FONT)));
        root.add(new Button("Start").onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                ss.push(new Game2D(ss));
            }
        }));
    }
}
