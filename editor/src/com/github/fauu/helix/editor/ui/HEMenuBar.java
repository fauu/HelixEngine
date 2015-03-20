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

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.fauu.helix.editor.HelixEditor;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;

public class HEMenuBar extends MenuBar {

  public HEMenuBar(final Stage stage) {
    super(stage);

    Menu fileMenu = new Menu("File");

    MenuItem openMapRegionMenuItem = new MenuItem("Open Area...",
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            HelixEditor.getInstance().openAreaAction();
          }
        });
    openMapRegionMenuItem.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.O);
    fileMenu.addItem(openMapRegionMenuItem);

    MenuItem saveAreaMenuItem = new MenuItem("Save Area",
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            HelixEditor.getInstance().saveAreaAction();
          }
        });
    saveAreaMenuItem.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.S);
    fileMenu.addItem(saveAreaMenuItem);

    MenuItem closeAreaMenuItem = new MenuItem("Close Area",
        new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            HelixEditor.getInstance().closeCurrentAreaAction();
          }
        });
    closeAreaMenuItem.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.W);
    fileMenu.addItem(closeAreaMenuItem);

    fileMenu.addSeparator();

    MenuItem exitMenuItem = new MenuItem("Exit", new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            HelixEditor.getInstance().exitAction();
          }
        });
    exitMenuItem.setShortcut(Input.Keys.ALT_LEFT, Input.Keys.F4);
    fileMenu.addItem(exitMenuItem);

    this.addMenu(fileMenu);
  }

}
