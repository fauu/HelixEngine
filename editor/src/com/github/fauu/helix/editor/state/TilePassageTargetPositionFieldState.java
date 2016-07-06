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

package com.github.fauu.helix.editor.state;

import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.event.TilePassageTargetPositionFieldStateChangedEvent;
import com.github.fauu.helix.util.IntVector2;

public class TilePassageTargetPositionFieldState {

  private IntVector2 position;

  public TilePassageTargetPositionFieldState() {
    position = new IntVector2();
  }

  public IntVector2 getPosition() {
    return position;
  }

  public void setPosition(IntVector2 position) {
    this.position.set(position);

    HelixEditor.getInstance()
               .getUIEventBus()
               .post(new TilePassageTargetPositionFieldStateChangedEvent(this));
  }

}
