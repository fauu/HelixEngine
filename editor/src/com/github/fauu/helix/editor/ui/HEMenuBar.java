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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.ui.dialog.NewMapRegionDialog;
import com.github.fauu.helix.editor.util.FileExtensionFilter;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class HEMenuBar extends MenuBar {

  public HEMenuBar(final Stage stage) {
    super(stage);
    
    final FileChooser regionFileChooser = new FileChooser(Mode.OPEN);
    regionFileChooser.setSelectionMode(SelectionMode.FILES);
    regionFileChooser.setFileFilter(new FileExtensionFilter("json"));
    regionFileChooser.setListener(new FileChooserAdapter() {
      @Override
      public void selected(FileHandle file) {
        HelixEditor.getInstance().closeCurrentMapRegion();
        HelixEditor.getInstance().loadMapRegion(file);
      }
    });

    Menu fileMenu = new Menu("File");

    MenuItem newRegionMenuItem = new MenuItem("New Map Region...",
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            stage.addActor((new NewMapRegionDialog()).fadeIn());
          }
        });
    fileMenu.addItem(newRegionMenuItem);

    MenuItem openRegionMenuItem = new MenuItem("Open Map Region...", 
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            stage.addActor(regionFileChooser.fadeIn());
          }
        });
    fileMenu.addItem(openRegionMenuItem);

    fileMenu.addSeparator();

    MenuItem exitMenuItem = new MenuItem("Exit", new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            Gdx.app.exit();
          }
        });
    fileMenu.addItem(exitMenuItem);

    this.addMenu(fileMenu);
  }

}
