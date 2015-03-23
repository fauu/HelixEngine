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
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.graphics.AnimationType;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.manager.PlayerManager;
import com.github.fauu.helix.spatial.Spatial;
import com.github.fauu.helix.spatial.update.dto.AnimationUpdateDTO;
import com.github.fauu.helix.util.IntVector2;
import com.github.fauu.helix.util.IntVector3;

import java.util.HashMap;
import java.util.Map;

public class PlayerMovementSystem extends VoidEntitySystem {

  @Wire
  private TagManager tagManager;

  @Wire
  private PlayerManager playerManager;

  @Wire
  private ComponentMapper<MovementSpeedComponent> movementSpeedMapper;

  @Wire
  private ComponentMapper<OrientationComponent> orientationMapper;

  @Wire
  private ComponentMapper<PositionComponent> positionMapper;

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire(injectInherited = true)
  private HelixCamera camera;

  private static final float MOVEMENT_START_DELAY;

  private static final HashMap<Direction, Integer> DIRECTION_KEYS;

  private static final AnimationType[] WALK_ANIMATION_CYCLE;

  private boolean moving;

  private Direction direction;

  private float startDelayCounter;

  private float progressCounter;

  private int walkAnimationCycleCounter;

  static {
    MOVEMENT_START_DELAY = 0.1f;

    DIRECTION_KEYS = new HashMap<Direction, Integer>();
    DIRECTION_KEYS.put(Direction.NORTH, Input.Keys.W);
    DIRECTION_KEYS.put(Direction.EAST, Input.Keys.D);
    DIRECTION_KEYS.put(Direction.SOUTH, Input.Keys.S);
    DIRECTION_KEYS.put(Direction.WEST, Input.Keys.A);

    WALK_ANIMATION_CYCLE = new AnimationType[2];
    WALK_ANIMATION_CYCLE[0] = AnimationType.STEP_RIGHT_LEG;
    WALK_ANIMATION_CYCLE[1] = AnimationType.STEP_LEFT_LEG;
  }

  // FIXME: Not responsive enough when changing direction without stopping
  @Override
  protected void processSystem() {
    Entity player = playerManager.getPlayer();

    Spatial spatial = spatialFormMapper.get(player).get();

    float movementSpeed =  movementSpeedMapper.get(player).get();

    float movementDuration = 1 / movementSpeed;

    Direction requestedDirection = null;

    for (Map.Entry<Direction, Integer> dk : DIRECTION_KEYS.entrySet()) {
      if (Gdx.input.isKeyPressed(dk.getValue())) {
        requestedDirection = dk.getKey();
        break;
      }
    }

    if (!moving) {
      if (requestedDirection == null) {
        startDelayCounter = 0;
      } else {
        if (startDelayCounter == 0) {
          orientationMapper.get(player).set(requestedDirection);
          spatial.update(Spatial.UpdateType.ORIENTATION,
                         requestedDirection);

          direction = requestedDirection;
        }

        startDelayCounter += Gdx.graphics.getDeltaTime();

        if (startDelayCounter >= MOVEMENT_START_DELAY) {
          startDelayCounter = 0;

          IntVector3 position = positionMapper.get(player).get();
          IntVector2 targetPosition
              = position.toIntVector2().add(direction.getVector());

          Tile[][] tiles = tilesMapper.get(tagManager.getEntity("area")).get();
          Tile currentTile = tiles[position.x][position.y];
          Tile targetTile = tiles[targetPosition.x][targetPosition.y];
          if (targetTile.getPermissions() == currentTile.getPermissions()) {
            moving = true;

            spatial.update(Spatial.UpdateType.ANIMATION,
                new AnimationUpdateDTO(
                    WALK_ANIMATION_CYCLE[walkAnimationCycleCounter++],
                    direction,
                    movementDuration));

            walkAnimationCycleCounter %= 2;

            positionMapper.get(player)
                          .set(new IntVector3(targetPosition.x,
                                              targetPosition.y,
                                              targetTile.getPermissions()
                                                        .getElevation()));
          }
        } // end "if startDelayCounter >= MOVEMENT_START_DELAY"
      } // end "if requestedDirection == direction"
    } // end "if !moving"

    if (moving) {
      if (progressCounter >= movementDuration) {
        moving = false;
        progressCounter = 0;

        spatial.update(Spatial.UpdateType.ANIMATION,
            new AnimationUpdateDTO(AnimationType.IDLE,
                                   direction,
                                   movementDuration));
      } else {
        float delta = Gdx.graphics.getDeltaTime();

        if (progressCounter + delta > movementDuration) {
          delta = movementDuration - progressCounter;
        }

        progressCounter += Gdx.graphics.getDeltaTime();

        IntVector2 movementDirectionVector = direction.getVector();

        Vector3 translation = new Vector3(movementDirectionVector.x,
                                          movementDirectionVector.y,
                                          0);
        translation.scl(delta * movementSpeed);

        spatial.update(Spatial.UpdateType.POSITION, translation);

        camera.translate(translation);

        if (movementDuration - progressCounter < 0 &&
            requestedDirection != null) {
          startDelayCounter = MOVEMENT_START_DELAY;

          spatial.update(Spatial.UpdateType.ORIENTATION,
                         requestedDirection);
          orientationMapper.get(player).set(requestedDirection);

          direction = requestedDirection;
        }
      } // end "if progressCounter < movementDuration"
    } // end "if moving"
  }

}
