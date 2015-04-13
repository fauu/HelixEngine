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

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IntVector2 {

  public int x;

  public int y;

  public IntVector2() { }

  public IntVector2(int x, int y) {
    set(x, y);
  }

  public IntVector2(IntVector2 source) {
    set(source);
  }

  public void set(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void set(IntVector2 source) {
    set(source.x, source.y);
  }

  public IntVector2 add(IntVector2 other) {
    set(x + other.x, y + other.y);

    return this;
  }

  public IntVector2 sub(IntVector2 other) {
    set(x - other.x, y - other.y);

    return this;
  }

  public IntVector2 cpy() {
    return new IntVector2(x, y);
  }

  public IntVector2 scl(int value) {
    x *= value;
    y  *= value;

    return this;
  }

  public Vector2 toVector2() {
    return new Vector2(x, y);
  }

  public Vector3 toVector3() {
    return new Vector3(x, y, 0);
  }

  @Override
  public String toString() {
    return "[" + x + "," + y + "]";
  }

}
