/*
 * Copyright (C) 2014-2016 Helix Engine Developers 
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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport; 
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.ui.dialog.NewAreaDialog;
import com.github.fauu.helix.editor.util.FileExtensionFilter;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class UI {
  
  private Stage stage;

  private HESidebar sidebar;
  
  public UI() {
    VisUI.load();
    
    stage = new Stage(new StretchViewport(1280, 800));
  
    Table root = new Table();
    root.setFillParent(true);
    stage.addActor(root.top());
    
    root.add((new HEMenuBar(stage)).getTable()).fillX().expandX().row();
    root.add(sidebar = new HESidebar()).minWidth(200)
                                       .left()
                                       .fillY()
                                       .expandY()
                                       .row();
    sidebar.setVisible(false);
  }
  
  public void render(float delta) {
    stage.act(delta);
    stage.draw();
  }
  
  public void dispose() {
    stage.dispose();
    VisUI.dispose();
  }

  public void showNewAreaDialog() {
    stage.addActor((new NewAreaDialog()).fadeIn());
  }

  public void showOpenAreaFileChooser() {
    final FileChooser regionFileChooser = new FileChooser(FileChooser.Mode.OPEN);
    regionFileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
    regionFileChooser.setFileFilter(new FileExtensionFilter("json"));
    regionFileChooser.setListener(new FileChooserAdapter() {
      @Override
      public void selected(Array<FileHandle> files) {
        HelixEditor.getInstance().loadAreaAction(files.get(0).nameWithoutExtension());
      }
    });

    stage.addActor(regionFileChooser.fadeIn());
  }

  public void setSidebarVisibility(boolean visible) {
    sidebar.setVisible(visible);
  }

  public Stage getStage() {
    return stage;
  }

}
