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

package com.github.fauu.helix.system;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.component.MovementSpeedComponent;
import com.github.fauu.helix.component.OrientationComponent;
import com.github.fauu.helix.component.PositionComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.spatial.Spatial;
import com.github.fauu.helix.util.IntVector3;

public class PlayerMovementSystem extends VoidEntitySystem {

  @Wire
  private PerspectiveCamera camera;

  @Wire
  private ComponentMapper<MovementSpeedComponent> movementSpeedMapper;

  @Wire
  private ComponentMapper<OrientationComponent> orientationMapper;

  @Wire
  private ComponentMapper<PositionComponent> positionMapper;

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;

  private Entity player;

  private boolean moving;

  private static final float MOVEMENT_START_DELAY;

  private float movementStartDelayCounter;

  private float walkCounter;

  static {
    MOVEMENT_START_DELAY = 0.1f;
  }

  // TODO: Refactor this a bit
  @Override
  protected void processSystem() {
    if (player == null) {
      player = world.getManager(TagManager.class).getEntity("player");
    }

    float movementSpeed =  movementSpeedMapper.get(player).get();

    float movementDuration = 1 / movementSpeed;

    Direction orientation = orientationMapper.get(player).get();

    Spatial spatial = spatialFormMapper.get(player).get();

    Direction requestedDirection = null;

    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      requestedDirection = Direction.NORTH;
    } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      requestedDirection = Direction.WEST;
    } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      requestedDirection = Direction.SOUTH;
    } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      requestedDirection = Direction.EAST;
    }

    if (!moving) {
      if (requestedDirection == null) {
        movementStartDelayCounter = 0;
      } else {
        if (movementStartDelayCounter == 0) {
          spatial.update(Spatial.UpdateType.ORIENTATION, requestedDirection);

          orientationMapper.get(player).set(requestedDirection);
        }

        movementStartDelayCounter += Gdx.graphics.getDeltaTime();

        if (movementStartDelayCounter >= MOVEMENT_START_DELAY) {
          moving = true;
          movementStartDelayCounter = 0;
        }
      }
    }

    if (moving) {
      if (walkCounter < movementDuration) {
        if (walkCounter == 0) {
          spatial.update(Spatial.UpdateType.PLAY_ANIMATION, orientation);
        }

        walkCounter += Gdx.graphics.getDeltaTime();

        IntVector3 positionDeltaCoefficients;

        switch (orientation) {
          case NORTH:
            positionDeltaCoefficients = new IntVector3(0, 1, 0);
            break;
          case WEST:
            positionDeltaCoefficients = new IntVector3(-1, 0, 0);
            break;
          case SOUTH:
            positionDeltaCoefficients = new IntVector3(0, -1, 0);
            break;
          case EAST:
            positionDeltaCoefficients = new IntVector3(1, 0, 0);
            break;
          default: throw new IllegalStateException();
        }

        Vector3 translation
            = positionDeltaCoefficients.toVector3()
                                       .scl(Gdx.graphics.getDeltaTime() *
                                            movementSpeed);

        spatial.update(Spatial.UpdateType.POSITION, translation);

        camera.translate(translation);

        if (walkCounter >= movementDuration && requestedDirection != null) {
          movementStartDelayCounter = MOVEMENT_START_DELAY;

          spatial.update(Spatial.UpdateType.ORIENTATION, requestedDirection);

          orientationMapper.get(player).set(requestedDirection);
        }
      } else {
        moving = false;
        walkCounter = 0;

        spatial.update(Spatial.UpdateType.IDLE, orientation);
      }
    }
  }

}
