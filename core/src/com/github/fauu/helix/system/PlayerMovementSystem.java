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
import com.github.fauu.helix.datum.Move;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.datum.TileAreaPassage;
import com.github.fauu.helix.displayable.AreaDisplayable;
import com.github.fauu.helix.displayable.Displayable;
import com.github.fauu.helix.displayable.update.dto.AnimationUpdateDTO;
import com.github.fauu.helix.graphics.AnimationType;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.manager.AreaManager;
import com.github.fauu.helix.manager.PlayerManager;
import com.github.fauu.helix.util.IntVector2;
import com.github.fauu.helix.util.IntVector3;
import com.google.common.collect.Range;

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

        if (queue.size() == 0 && startDelayCounter >= START_DELAY) {
          tryMoving(player, displayable, pressedDirection);
        } else {
          startDelayCounter += Gdx.graphics.getDeltaTime();
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

  private void tryMoving(Entity player,
                         Displayable displayable,
                         final Direction direction) {
    IntVector3 position = positionMapper.get(player).get();
    IntVector2 targetCoords = position.toIntVector2().add(direction.getVector());

    Entity area = areaManager.getArea();

    if (!coordsInsideArea(targetCoords, area)) {
      return;
    }

    Tile[][] tiles = tilesMapper.get(area).get();

    Tile currentTile = Tile.get(tiles, position.toIntVector2());
    Tile targetTile = Tile.get(tiles, targetCoords);

    if (targetTile.getPermissions() == TilePermission.OBSTACLE) {
      return;
    } else if (targetTile.getPermissions() == TilePermission.PASSAGE) {
      moveThroughAreaPassage(targetTile.getAreaPassage(),
          player,
          displayable,
          area,
          direction,
          targetCoords);
    } else {
      Move move = new Move();

      move.setDirection(direction);
      move.setSpeed(movementSpeedMapper.get(player).get());

      queue.push(move);
    }
  }

  private void moveThroughAreaPassage(final TileAreaPassage passage,
                                      final Entity player,
                                      final Displayable displayable,
                                      final Entity area,
                                      final Direction direction,
                                      final IntVector2 targetCoords) {
    final AreaDisplayable areaDisplayable
        = (AreaDisplayable) displayableMapper.get(area).get();

    String entryAnimationId
        = AreaManager.constructPassageAnimationId(targetCoords,
                                                  PassageAction.ENTRY);

    float entryAnimationWaitTime = 0;

    if (areaDisplayable.animationExists(entryAnimationId)) {
      areaDisplayable.update(Displayable.UpdateType.ANIMATION,
                             entryAnimationId);

      entryAnimationWaitTime = 1;
    }

    enabled = false;

    final Move entryMove = new Move();
    entryMove.setDirection(direction);
    entryMove.setSpeed(movementSpeedMapper.get(player).get());
    Timer.schedule(new Timer.Task() {
      @Override
      public void run() {
        queue.push(entryMove);

        world.getSystem(ScreenFadingSystem.class)
             .fade(ScreenFadingSystem.FadeType.FADE_OUT, .3f);

        enabled = true;
      }
    }, entryAnimationWaitTime);

    Timer.schedule(new Timer.Task() {
          @Override
          public void run() {
            areaManager.unloadCurrent();
            areaManager.load(passage.getTargetAreaName());
            Entity area = areaManager.getArea();
            AreaDisplayable areaDisplayable
                = (AreaDisplayable) displayableMapper.get(area).get();

            IntVector3 exitPosition
                = new IntVector3(passage.getTargetPosition());

            positionMapper.get(player).set(exitPosition);

            Vector3 translation
                = exitPosition.toVector3()
                              .sub(targetCoords.x, targetCoords.y, 0);

            float exitAnimationWaitTime = 0;

            String exitAnimationId
                = AreaManager.constructPassageAnimationId(
                    passage.getTargetPosition(),
                    PassageAction.EXIT);

            if (areaDisplayable.animationExists(exitAnimationId)) {
              areaDisplayable.update(Displayable.UpdateType.ANIMATION,
                                     exitAnimationId);
            }

            displayable.update(Displayable.UpdateType.POSITION, translation);

            // TODO: Camera stuff out of here?
            camera.translate(translation);

            world.getSystem(ScreenFadingSystem.class)
                 .fade(ScreenFadingSystem.FadeType.FADE_IN, .3f);

            final Move exitMove = new Move();
            exitMove.setDirection(direction);
            exitMove.setSpeed(movementSpeedMapper.get(player).get());
            Timer.schedule(new Timer.Task() {
                  @Override
                  public void run() {
                    queue.push(exitMove);
                  }
                }, exitAnimationWaitTime);
          }
        }, entryAnimationWaitTime + entryMove.getDuration());
  }

  private void processMove(Entity player, Displayable displayable) {
    if (!currentMove.hasFinished()) {
      if (!currentMove.hasStarted()) {
        IntVector3 position = positionMapper.get(player).get();
        IntVector2 targetPosition
            = position.toIntVector2()
                      .add(currentMove.getDirection().getVector());

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

      // TODO: Find a way for the movement to be less choppy while remaining accurate
      float delta
          = currentMove.willHaveBeenFinishedIn(Gdx.graphics.getDeltaTime()) ?
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

  private boolean coordsInsideArea(IntVector2 coords, Entity area) {
    IntVector2 areaDimensions = dimensionsMapper.get(area).get();

    return (Range.closedOpen(0, areaDimensions.x).contains(coords.x) &&
            Range.closedOpen(0, areaDimensions.y).contains(coords.y));
  }

  private boolean isMoving() {
    return currentMove != null;
  }

}
