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

import com.github.fauu.helix.editor.ToolType;

public class ToolbarState {
  
  private ToolType activeTool;
  
  public ToolbarState() { }
  
  public void initialize(ToolType type) {
    activeTool = type;
  }
  
  public ToolType getActiveTool() {
    return activeTool;
  }
  
  public void setActiveTool(ToolType type) {
    activeTool = type;
  }

}
