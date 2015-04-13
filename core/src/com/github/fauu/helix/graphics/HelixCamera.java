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
import com.github.fauu.helix.util.IntVector3;

public class HelixCamera extends PerspectiveCamera {

  private static final Vector3 INITIAL_POSITION;

  private static final float POSITION_Y_TO_Z_RATIO;

  private static final Vector3 DEFAULT_TARGET_DISPLACEMENT;

  private Vector3 targetPosition;

  private Vector3 targetPositionDelta;

  static {
    INITIAL_POSITION = new Vector3(0, -13, 17);
    POSITION_Y_TO_Z_RATIO = INITIAL_POSITION.y / INITIAL_POSITION.z;
    DEFAULT_TARGET_DISPLACEMENT = new Vector3(.5f, .6f, 0);
  }

  public HelixCamera(float fieldOfView,
                     Vector3 targetPosition,
                     float near,
                     float far) {
    super(fieldOfView, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    this.targetPosition = new Vector3();
    this.near = near;
    this.far = far;
    this.translate(0, -13, 17);
    this.lookAt(0, 0, 0);

    targetPositionDelta = new Vector3();

    translateTargetPosition(targetPosition.add(DEFAULT_TARGET_DISPLACEMENT));
  }

  public void translateTargetPosition(Vector3 translation) {
    translateTargetPosition(translation.x, translation.y, translation.z);
  }

  public void translateTargetPosition(float x, float y, float z) {
    super.translate(x, y, z);

    targetPosition.add(x, y, z);
    targetPositionDelta.set(x, y, z);
  }

  public void updateTargetPosition(IntVector3 logicalPosition) {
    Vector3 translation
        = logicalPosition.cpy().toVector3()
                         .add(DEFAULT_TARGET_DISPLACEMENT)
                         .sub(targetPosition);

    translateTargetPosition(translation);
  }

  public Vector3 getTargetPositionDelta() {
    return targetPositionDelta;
  }

  public void resetPositionDelta() {
    targetPositionDelta.setZero();
  }

}
