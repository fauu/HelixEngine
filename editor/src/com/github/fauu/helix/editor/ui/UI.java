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

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kotcrab.vis.ui.VisUI;

public class UI {
  
  private Stage stage;
  
  public UI() {
    VisUI.load();
    
    stage = new Stage(new StretchViewport(1280, 800));
  
    Table root = new Table();
    root.setFillParent(true);
    stage.addActor(root.top());
    
    root.add((new HEMenuBar(stage)).getTable()).fillX().expandX().row();
    root.add(new HESidebar()).minWidth(200).left().fillY().expandY().row();
  }
  
  public void render(float delta) {
    stage.act(delta);
    stage.draw();
  }
  
  public void dispose() {
    stage.dispose();
    VisUI.dispose();
  }

  public Stage getStage() {
    return stage;
  }

}
