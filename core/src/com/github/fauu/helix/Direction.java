/*
 * Copyright (C) 2014 Helix Engine Developers (http://github.com/fauu/HelixEngine)
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

public enum Direction {

  SOUTH(0), SOUTHEAST(0),
  EAST(90), NORTHEAST(90),
  NORTH(180), NORTHWEST(180),
  WEST(270), SOUTHWEST(270);

  int angle;

  private Direction(final int angle) {
    this.angle = angle;
  }

  public int getAngle() {
    return angle;
  }

}
