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

package com.github.fauu.helix.editor.world.system;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;

public class CameraControlSystem extends VoidEntitySystem {
  
  private static final float FLAT_MOVEMENT_SPEED = 1;

  private static final float VERTICAL_MOVEMENT_SPEED = 4;
  
  @Wire
  private PerspectiveCamera camera;
  
  private Direction flatMovementDirection;

  private Vector3 translation = new Vector3();

  @Override
  protected void processSystem() {
    if (flatMovementDirection != null) {
      switch (flatMovementDirection) {
        case NORTH:
          translation.y = 1;
          break;
        case EAST:
          translation.x = 1;
          break;
        case SOUTH:
          translation.y = -1;
          break;
        case WEST:
          translation.x = -1;
          break;
        default: break;
      }
      
      camera.translate(translation.scl(FLAT_MOVEMENT_SPEED));
      
      translation.setZero();
    }
  }
  
  public void setMovementDirection(Direction direction) {
    flatMovementDirection = direction;
  }

  public void unsetMovementDirection(Direction direction) {
    if (flatMovementDirection == direction) {
      flatMovementDirection = null;
    }
  }
  
  public void moveVertically(int amount) {
    camera.translate(new Vector3(0, 0, amount).scl(VERTICAL_MOVEMENT_SPEED));
  }

}
