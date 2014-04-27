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

package com.github.fauu.helix.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent implements Component {

  Vector3 position;

  public PositionComponent() {
    this(new Vector3(0, 0, 0));
  }

  public PositionComponent(final Vector3 position) {
    this.position = position;
  }

  public PositionComponent(final float x, final float y, final float z) {
    position = new Vector3(x, y, z);
  }

  @Override
  public void reset() {
    position.set(0, 0, 0);
  }

  public Vector3 get() {
    return position;
  }

  public float getX() {
    return position.x;
  }

  public float getY() {
    return position.y;
  }

  public float getZ() {
    return position.z;
  }

  public void set(final Vector3 value){
    this.position.set(value);
  }

  public void set(final float x, final float y, final float z) {
    position.set(x, y, z);
  }

}
