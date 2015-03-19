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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.editor.HelixEditor;
import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;

public class HETilePermissionPanel extends VisTable {

  public HETilePermissionPanel() {
    HelixEditor.getInstance().getUIEventBus().register(this);

    add(new VisLabel("Tile permission type:")).left().padLeft(10);

    row();

    final VisList<String> permissionsList = new VisList<>();
    permissionsList.addListener(new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          HelixEditor.getInstance()
                     .getTilePermissionListState()
                     .setSelected(
                         TilePermission.fromString(permissionsList.getSelected()));
        }
      });

    Array<String> permissionNames = new Array<>();
    for (TilePermission permission : TilePermission.values()) {
      permissionNames.add(permission.getName());
    }
    permissionsList.setItems(permissionNames);

    ScrollPane permissionsListScrollPane = new VisScrollPane(permissionsList);

    add(permissionsListScrollPane);
  }

}
