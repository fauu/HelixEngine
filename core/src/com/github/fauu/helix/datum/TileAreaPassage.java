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

package com.github.fauu.helix.datum;

import com.github.fauu.helix.util.IntVector2;

public class TileAreaPassage {

  private String targetAreaName;

  private IntVector2 targetCoords;

  public TileAreaPassage() { }

  public TileAreaPassage(String targetAreaName, IntVector2 targetCoords) {
    this.targetAreaName = targetAreaName;
    this.targetCoords = targetCoords;
  }

  public String getTargetAreaName() {
    return targetAreaName;
  }

  public void setTargetAreaName(String targetAreaName) {
    this.targetAreaName = targetAreaName;
  }

  public IntVector2 getTargetCoords() {
    return targetCoords;
  }

  public void setTargetCoords(IntVector2 targetCoords) {
    this.targetCoords = targetCoords;
  }

}
