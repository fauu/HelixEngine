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

package com.github.fauu.helix.util;

import com.badlogic.gdx.math.Vector3;

public class IntVector3 {

  public int x;

  public int y;

  public int z;

  public IntVector3() { }

  public IntVector3(int x, int y, int z) {
    set(x, y, z);
  }

  public IntVector3(IntVector2 source) {
    set(source.x, source.y, 0);
  }

  public void set(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void set(IntVector3 source) {
    set(source.x, source.y, source.z);
  }

  public IntVector3 add(IntVector2 other) {
    set(x + other.x, y + other.y, z);

    return this;
  }

  public IntVector3 add(IntVector3 other) {
    set(x + other.x, y + other.y, z + other.z);

    return this;
  }

  public IntVector3 cpy() {
    return new IntVector3(x, y, z);
  }

  public Vector3 toVector3() {
    return new Vector3(x, y, z);
  }

  public IntVector2 toIntVector2() {
    return new IntVector2(x, y);
  }

}
