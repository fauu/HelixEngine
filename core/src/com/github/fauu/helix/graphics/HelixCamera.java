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

package com.github.fauu.helix.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

public class HelixCamera extends PerspectiveCamera {

  private Vector3 positionDelta;

  public HelixCamera(float fieldOfView,
                     Vector3 initialPosition,
                     float near,
                     float far) {
    super(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    positionDelta = new Vector3();

    this.near = near;
    this.far = far;
    this.translate(initialPosition);
    this.lookAt(0, 0, 0);
  }

  @Override
  public void translate(Vector3 translation) {
    translate(translation.x, translation.y, translation.z);
  }

  @Override
  public void translate(float x, float y, float z) {
    super.translate(x, y, z);

    positionDelta.set(x, y, z);
  }

  public Vector3 getPositionDelta() {
    return positionDelta;
  }

  public void resetPositionDelta() {
    positionDelta.setZero();
  }

}
