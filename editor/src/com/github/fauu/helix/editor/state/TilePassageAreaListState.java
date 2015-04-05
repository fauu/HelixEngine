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

import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.event.TilePassageAreaListStateChangedEvent;

public class TilePassageAreaListState {

  private Array<String> areaNames;

  private String selectedAreaName;

  public TilePassageAreaListState() {
    areaNames = new Array<>();
  }

  public void initialize(Array<String> areaNames, String selectedAreaName) {
    this.areaNames = areaNames;
    this.selectedAreaName = selectedAreaName;

    HelixEditor.getInstance()
               .getUIEventBus()
               .post(new TilePassageAreaListStateChangedEvent(this));
  }

  public Array<String> getItems() {
    return areaNames;
  }

  public String getSelected() {
    return selectedAreaName;
  }

  public void setSelected(String areaName) {
    selectedAreaName = areaName;
  }

}
