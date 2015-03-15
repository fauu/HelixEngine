package com.github.fauu.helix.editor.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.editor.HelixEditor;
import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTextButton;

// TODO: Abstract out Popup
public class HEOrientationSelectionPopout extends VisTable {
  
  private static final int MARGIN_LEFT = -1;
  
  private Array<TextButton> buttons;

  public HEOrientationSelectionPopout(final Actor origin) {
    super(true);
    
    setBackground(VisUI.getSkin().getDrawable("tooltip-bg"));
    
    final Array<Direction> directions = new Array<Direction>();
    directions.add(Direction.NORTH);
    directions.add(Direction.WEST);
    directions.add(Direction.EAST);
    directions.add(Direction.SOUTH);

    buttons = new Array<TextButton>();
    
    TextButton button;
    
    button = new VisTextButton(directions.get(0).toString());
    buttons.add(button);
    add(button).colspan(3);
    
    row();

    button = new VisTextButton(directions.get(1).toString());
    buttons.add(button);
    add(button);
    
    add();

    button = new VisTextButton(directions.get(2).toString());
    buttons.add(button);
    add(button);
    
    row();
    
    button = new VisTextButton(directions.get(3).toString());
    buttons.add(button);
    add(button).colspan(3);
    
    for (int i = 0; i < buttons.size; i++) {
      Button b = buttons.get(i);

      final int index = i;
      b.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              HelixEditor.getInstance()
                         .getTilePropertiesState()
                         .setOrientation(directions.get(index));

              fadeOut();
            }
          });
    }
    
    pack();

    Vector2 originPosition = origin.localToStageCoordinates(new Vector2(0, 0));
    setX(originPosition.x + origin.getWidth() + MARGIN_LEFT);
    setY(originPosition.y - getWidth() / 2 + origin.getHeight() / 2);
  }

  public Table fadeIn() {
    clearActions();

    setColor(1, 1, 1, 0);

    addAction(Actions.sequence(Actions.fadeIn(0.3f, Interpolation.fade)));

    return this;
  }
  
  public void fadeOut() {
    clearActions();

    addAction(Actions.sequence(Actions.fadeOut(0.3f, Interpolation.fade), 
                               Actions.removeActor()));
  }

}
