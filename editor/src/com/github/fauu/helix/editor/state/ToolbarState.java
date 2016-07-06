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
import com.github.fauu.helix.editor.ToolType;
import com.github.fauu.helix.editor.event.ToolbarStateChangedEvent;

public class ToolbarState {

  private ToolType activeTool;

  public void initialize(ToolType type) {
    setActiveTool(type);
  }

  public ToolType getActiveTool() {
    return activeTool;
  }

  public void setActiveTool(ToolType type) {
    activeTool = type;

    HelixEditor.getInstance()
               .getWorldEventBus()
               .post(new ToolbarStateChangedEvent(type));

    HelixEditor.getInstance().toolSelected(type);
  }

}
