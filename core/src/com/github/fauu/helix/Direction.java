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

package com.github.fauu.helix;

import com.github.fauu.helix.util.IntVector2;

public enum Direction {

  SOUTH(new IntVector2(0, -1)), SOUTHEAST(null),
  EAST(new IntVector2(1, 0)), NORTHEAST(null),
  NORTH(new IntVector2(0, 1)), NORTHWEST(null),
  WEST(new IntVector2(-1, 0)), SOUTHWEST(null);

  private IntVector2 vector;

  private Direction(IntVector2 vector) {
    this.vector = vector;
  }

  public IntVector2 getVector() {
    return vector;
  }

  @Override
  public String toString() {
    switch (this) {
      case SOUTH: return "South";
      case SOUTHEAST: return "Southeast";
      case EAST: return "East";
      case NORTHEAST: return "Northeast";
      case NORTH: return "North"; // by
      case NORTHWEST: return "Northwest";
      case WEST: return "West";
      case SOUTHWEST: return "Southwest";
      default: throw new IllegalArgumentException();
    }
  }

}
