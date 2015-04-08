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
import com.github.fauu.helix.datum.Move;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.displayable.Displayable;
import com.github.fauu.helix.displayable.update.dto.AnimationUpdateDTO;
import com.github.fauu.helix.graphics.AnimationType;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.manager.AreaManager;
import com.github.fauu.helix.manager.PlayerManager;
import com.github.fauu.helix.util.IntVector2;
import com.github.fauu.helix.util.IntVector3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlayerMovementSystem extends VoidEntitySystem {

  @Wire
  private AreaManager areaManager;

  @Wire
  private TagManager tagManager;

  @Wire
  private PlayerManager playerManager;

  @Wire
  private ComponentMapper<DimensionsComponent> dimensionsMapper;

  @Wire
  private ComponentMapper<DisplayableComponent> displayableMapper;

  @Wire
  private ComponentMapper<MovementSpeedComponent> movementSpeedMapper;

  @Wire
  private ComponentMapper<OrientationComponent> orientationMapper;

  @Wire
  private ComponentMapper<PositionComponent> positionMapper;

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire
  private HelixCamera camera;

  private static final float START_DELAY;

  private static final HashMap<Direction, Integer> DIRECTION_KEYS;

  private static final AnimationType[] WALK_ANIMATION_CYCLE;

  private boolean enabled;

  private Move currentMove;

  private float startDelayCounter;

  private int walkAnimationCycleCounter;

  private LinkedList<Move> queue;

  static {
    START_DELAY = 0.1f;

    DIRECTION_KEYS = new HashMap<Direction, Integer>();
    DIRECTION_KEYS.put(Direction.NORTH, Input.Keys.W);
    DIRECTION_KEYS.put(Direction.EAST, Input.Keys.D);
    DIRECTION_KEYS.put(Direction.SOUTH, Input.Keys.S);
    DIRECTION_KEYS.put(Direction.WEST, Input.Keys.A);

    WALK_ANIMATION_CYCLE = new AnimationType[2];
    WALK_ANIMATION_CYCLE[0] = AnimationType.STEP_RIGHT_LEG;
    WALK_ANIMATION_CYCLE[1] = AnimationType.STEP_LEFT_LEG;
  }

  @Override
  public void initialize() {
    enabled = true;
    queue = new LinkedList<Move>();
  }

  @Override
  protected boolean checkProcessing() {
    return enabled;
  }

  // FIXME: Seamless movement is apparently not completely seamless
  @Override
  protected void processSystem() {
    Entity player = playerManager.getPlayer();
    Displayable displayable = displayableMapper.get(player).get();

    if (!isMoving()) {
      Direction pressedDirection = pollPressedDirection();
      if(pressedDirection != null) {
        if (startDelayCounter == 0) {
          orientationMapper.get(player).set(pressedDirection);
          displayable.update(Displayable.UpdateType.ORIENTATION, pressedDirection);
        }

        startDelayCounter += Gdx.graphics.getDeltaTime();

        if (startDelayCounter >= START_DELAY) {
          Move move = new Move();

          move.setDirection(pressedDirection);
          move.setVector(move.getDirection().getVector().toVector3());
          move.setSpeed(movementSpeedMapper.get(player).get());
          move.setDuration(1 / move.getSpeed());

          queue.push(move);
        }
      } else {
        startDelayCounter = 0;
      }

      Move nextMove = queue.poll();
      if (nextMove != null) {
        currentMove = nextMove;
      }
    }

    if (isMoving()) {
      processMove(player, displayable);
    }
  }

  private void processMove(Entity player, Displayable displayable) {
    if (!currentMove.hasFinished()) {
      if (!currentMove.hasStarted()) {
        IntVector3 position = positionMapper.get(player).get();
        IntVector2 targetPosition
            = position.toIntVector2().add(currentMove.getDirection().getVector());

        Tile[][] tiles = tilesMapper.get(areaManager.getArea()).get();
        Tile targetTile = tiles[targetPosition.y][targetPosition.x];

        AnimationUpdateDTO update
            = new AnimationUpdateDTO(
                WALK_ANIMATION_CYCLE[walkAnimationCycleCounter++],
                currentMove.getDirection(),
                currentMove.getDuration());
        displayable.update(Displayable.UpdateType.ANIMATION, update);

        walkAnimationCycleCounter %= 2;

        PositionComponent positionComponent = positionMapper.get(player);
        IntVector3 newPosition
            = new IntVector3(targetPosition.x,
                             targetPosition.y,
                             targetTile.getPermissions().getElevation());
        positionComponent.set(newPosition);
      }

      float delta = currentMove.willHaveBeenFinishedIn(Gdx.graphics.getDeltaTime()) ?
          currentMove.getRemaining() : Gdx.graphics.getDeltaTime();

      Vector3 translation
          = currentMove.getVector().cpy().scl(delta * currentMove.getSpeed());

      displayable.update(Displayable.UpdateType.POSITION, translation);

      // TODO: Camera stuff out of here
      translation.y += translation.z * (-13 / 17);
      camera.translate(translation);

      currentMove.setElapsed(currentMove.getElapsed() + delta);
    } else {
      currentMove = null;
    }
  }

  private Direction pollPressedDirection() {
    Direction pressedDirection = null;

    for (Map.Entry<Direction, Integer> dk : DIRECTION_KEYS.entrySet()) {
      if (Gdx.input.isKeyPressed(dk.getValue())) {
        pressedDirection = dk.getKey();

        break;
      }
    }

    return pressedDirection;
  }

  private boolean isMoving() {
    return currentMove != null;
  }

}
