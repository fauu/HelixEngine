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
import com.badlogic.gdx.utils.Timer;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.PassageAction;
import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.displayable.AreaDisplayable;
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
  private ComponentMapper<MovementSpeedComponent> movementSpeedMapper;

  @Wire
  private ComponentMapper<OrientationComponent> orientationMapper;

  @Wire
  private ComponentMapper<PositionComponent> positionMapper;

  @Wire
  private ComponentMapper<DisplayableComponent> displayableMapper;

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire
  private HelixCamera camera;

  private static final float START_DELAY;

  private static final HashMap<Direction, Integer> DIRECTION_KEYS;

  private static final AnimationType[] WALK_ANIMATION_CYCLE;

  private boolean enabled;

  private boolean moving;

  private Direction direction;

  private float startDelayCounter;

  private float progressCounter;

  private int walkAnimationCycleCounter;

  private LinkedList<Direction> autoMoveQueue;

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
    autoMoveQueue = new LinkedList<Direction>();
  }

  // FIXME: Not responsive enough when changing direction without stopping
  // TODO: Use move queue for everything
  @Override
  protected void processSystem() {
    final Entity player = playerManager.getPlayer();

    final Displayable displayable = displayableMapper.get(player).get();

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
      Direction autoMoveDirection = autoMoveQueue.poll();

      if (autoMoveDirection != null) {
        IntVector3 position = positionMapper.get(player).get();
        IntVector2 targetPosition
            = position.toIntVector2().add(autoMoveDirection.getVector());

        Tile[][] tiles = tilesMapper.get(areaManager.getArea()).get();
        Tile targetTile = tiles[targetPosition.y][targetPosition.x];

        launchMovement(player,
                       displayable,
                       movementDuration,
                       targetTile,
                       targetPosition);

        direction = autoMoveDirection;
      } else {
        if (requestedDirection == null) {
          startDelayCounter = 0;
        } else {
          if (startDelayCounter == 0) {
            orientationMapper.get(player).set(requestedDirection);
            displayable.update(Displayable.UpdateType.ORIENTATION,
                               requestedDirection);

            direction = requestedDirection;
          }

          startDelayCounter += Gdx.graphics.getDeltaTime();

          if (startDelayCounter >= START_DELAY) {
            startDelayCounter = 0;

            IntVector3 position = positionMapper.get(player).get();
            final IntVector2 targetPosition
                = position.toIntVector2().add(direction.getVector());

            Tile[][] tiles = tilesMapper.get(areaManager.getArea()).get();
            Tile currentTile = tiles[position.y][position.x];
            Tile targetTile = tiles[targetPosition.y][targetPosition.x];
            if (targetTile.getPermissions() == currentTile.getPermissions()) {
              launchMovement(player,
                             displayable,
                             movementDuration,
                             targetTile,
                             targetPosition);
            } else if (targetTile.getPermissions() == TilePermission.PASSAGE) {
              final AreaDisplayable areaDisplayable
                  = (AreaDisplayable) displayableMapper.get(areaManager.getArea())
                                                   .get();

              String passageAnimationId
                  = AreaManager.constructPassageAnimationId(targetPosition,
                                                            PassageAction.ENTRY);

              if (areaDisplayable.animationExists(passageAnimationId)) {
                areaDisplayable.update(Displayable.UpdateType.ANIMATION,
                                   passageAnimationId);

                final Direction scheduledMoveDirection = requestedDirection;

                enabled = false;

                Timer.schedule(new Timer.Task() {
                      @Override
                      public void run() {
                        enabled = true;

                        autoMoveQueue.push(scheduledMoveDirection);

                        world.getSystem(ScreenFadingSystem.class)
                             .fade(ScreenFadingSystem.FadeType.FADE_OUT, .3f);
                      }
                    }, 1);

                Timer.schedule(new Timer.Task() {
                      @Override
                      public void run() {
                        areaManager.unloadCurrent();

                        areaManager.load("house1-interior");

                        positionMapper.get(player)
                                      .set(new IntVector3(3, 1, 0));

                        Vector3 translation
                            = new Vector3(-targetPosition.x + 3,
                                          -targetPosition.y - 1 + 1,
                                          0);

                        displayable.update(Displayable.UpdateType.POSITION,
                                           translation);

                        camera.translate(translation);

                        autoMoveQueue.push(scheduledMoveDirection);

                        world.getSystem(ScreenFadingSystem.class)
                             .fade(ScreenFadingSystem.FadeType.FADE_IN, .3f);
                      }
                    }, 1 + 1);
              } // end "if areaDisplayable.animationExists(passageAnimationId)"
            } // end "if targetTile.getPermissions() == TilePermission.PASSAGE"
          } // end "if startDelayCounter >= START_DELAY"
        } // end "if requestedDirection != null"
      } // end "if autoMoveDirection == null"
    } // end "if !moving"

    if (moving) {
      if (progressCounter >= movementDuration) {
        moving = false;
        progressCounter = 0;

        displayable.update(Displayable.UpdateType.ANIMATION,
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

        displayable.update(Displayable.UpdateType.POSITION, translation);

        camera.translate(translation);

        if (movementDuration - progressCounter < 0 &&
            requestedDirection != null) {
          startDelayCounter = START_DELAY;

          displayable.update(Displayable.UpdateType.ORIENTATION,
              requestedDirection);
          orientationMapper.get(player).set(requestedDirection);

          direction = requestedDirection;
        }
      } // end "if progressCounter < movementDuration"
    } // end "if moving"
  }

  private void launchMovement(Entity player,
                              Displayable displayable,
                              float duration,
                              Tile targetTile,
                              IntVector2 targetPosition) {
    moving = true;

    displayable.update(Displayable.UpdateType.ANIMATION,
        new AnimationUpdateDTO(
            WALK_ANIMATION_CYCLE[walkAnimationCycleCounter++],
            direction,
            duration));

    walkAnimationCycleCounter %= 2;

    positionMapper.get(player)
                  .set(new IntVector3(targetPosition.x,
                      targetPosition.y,
                      targetTile.getPermissions()
                          .getElevation()));
  }

}
