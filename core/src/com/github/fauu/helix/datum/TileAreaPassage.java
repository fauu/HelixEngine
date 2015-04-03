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

package com.github.fauu.helix.datum;

import com.github.fauu.helix.util.IntVector2;

public class TileAreaPassage {

  private String targetAreaName;

  private IntVector2 targetTileCoords;

  public TileAreaPassage(String targetAreaName, IntVector2 targetTileCoords) {
    this.targetAreaName = targetAreaName;
    this.targetTileCoords = targetTileCoords;
  }

  public String getTargetAreaName() {
    return targetAreaName;
  }

  public void setTargetAreaName(String targetAreaName) {
    this.targetAreaName = targetAreaName;
  }

  public IntVector2 getTargetTileCoords() {
    return targetTileCoords;
  }

  public void setTargetTileCoords(IntVector2 targetTileCoords) {
    this.targetTileCoords = targetTileCoords;
  }

}
