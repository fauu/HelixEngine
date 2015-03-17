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

package com.github.fauu.helix.util;

public class IntVector2 {

  public int x;

  public int y;

  public IntVector2() { }

  public IntVector2(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void set(IntVector2 source) {
    x = source.x;
    y = source.y;
  }

  public IntVector2 cpy() {
    return new IntVector2(x, y);
  }

}
