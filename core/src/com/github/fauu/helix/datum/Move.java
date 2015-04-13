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

import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;

public class Move {

  private Direction direction;

  private Vector3 vector;

  private float speed;

  private float elapsed;

  private float duration;

  public Move() { }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
    this.vector = direction.getVector().toVector3();
  }

  public Vector3 getVector() {
    return vector;
  }

  public void setVectorZ (float value) {
    vector.z = value;
  }

  public float getSpeed() {
    return speed;
  }

  public void setSpeed(float speed) {
    this.speed = speed;
    this.duration = 1 / speed;
  }

  public float getElapsed() {
    return elapsed;
  }

  public void setElapsed(float elapsed) {
    this.elapsed = elapsed;
  }

  public float getDuration() {
    return duration;
  }

  public boolean hasStarted() {
    return elapsed > 0;
  }

  public boolean hasFinished() {
    return elapsed >= duration;
  }

  public float getRemaining() {
    return duration - elapsed;
  }

  public boolean willHaveBeenFinishedIn(float time) {
    return elapsed + time >= duration;
  }

}
