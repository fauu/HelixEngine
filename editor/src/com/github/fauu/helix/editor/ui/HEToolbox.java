/*
 * Copyright (C) 2014, 2015 Helix Engine Developers
 * (http://github.com/fauu/HelixEngine)
 *
 * This software is licensed under the GNU General Public License
 * (version 3 or later). See the COPYING file in this distribution.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authored by: Piotr Grabowski <fau999@gmail.com>
 */

package com.github.fauu.helix.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.ToolType;
import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class HEToolbox extends VisTable {
  
  public HEToolbox() {
    super(true);
    
//    add(new VisLabel("Toolbox")).center();
//    
//    row().expandX();

    ButtonGroup<Button> toolButtons = new ButtonGroup<>();
    
    for (final ToolType type : ToolType.values()) {
      Button button = new VisTextButton(type.toString(), "toggle");
      button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
              if (((Button) actor).isChecked()) {
                HelixEditor.getInstance().getToolbarState().setActiveTool(type);
              }
            }
          });

      toolButtons.add(button);
      
      add(button).padBottom(-8);

      row();
    }
  }

}
