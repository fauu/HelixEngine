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

package com.github.fauu.helix.editor.state;

import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.event.TilePermissionListStateChangedEvent;

public class TilePermissionListState {

  private TilePermission selectedPermission;

  public void initialize(TilePermission permission) {
    selectedPermission = permission;
  }

  public TilePermission getSelected() {
    return selectedPermission;
  }

  public void setSelected(TilePermission permission) {
    this.selectedPermission = permission;

    HelixEditor.getInstance()
               .getWorldEventBus()
               .post(new TilePermissionListStateChangedEvent(permission));
  }
}
