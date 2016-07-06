/*
 * Copyright (C) 2014-2016 Helix Engine Developers
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
import com.github.fauu.helix.AreaType;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.PassageAction;
import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.datum.Ambience;
import com.github.fauu.helix.datum.Move;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.datum.TileAreaPassage;
import com.github.fauu.helix.displayable.DecalDisplayable;
import com.github.fauu.helix.displayable.ModelDisplayable;
import com.github.fauu.helix.graphics.AnimationType;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.manager.AreaManager;
import com.github.fauu.helix.manager.LocalAmbienceManager;
import com.github.fauu.helix.manager.PlayerManager;
import com.github.fauu.helix.manager.WeatherMan;
import com.github.fauu.helix.util.IntVector2;
import com.github.fauu.helix.util.IntVector3;
import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlayerMovementSystem extends VoidEntitySystem {

  private static final float PASSAGE_BLACKOUT_DURATION = .5f;

  @Wire
  private AreaManager areaManager;

  @Wire
  private TagManager tagManager;

  @Wire
  private PlayerManager playerManager;

  @Wire
  private ComponentMapper<AreaTypeComponent> areaTypeMapper;

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

  private Move lastMove;

  private Entity player;

  private DecalDisplayable displayable;

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
    player = playerManager.getPlayer();
    displayable = (DecalDisplayable) displayableMapper.get(player).get();

    if (!isMoving()) {
      Direction pressedDirection = pollPressedDirection();
      if(pressedDirection != null) {
        if (startDelayCounter == 0) {
          orientationMapper.get(player).set(pressedDirection);
          displayable.orientate(pressedDirection);
        }

        if (queue.size() == 0 && startDelayCounter >= START_DELAY) {
          tryMoving(pressedDirection);
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
      advanceMove();
    }
  }

  private void tryMoving(Direction direction) {
    orientationMapper.get(player).set(direction);
    displayable.orientate(direction);

    IntVector3 position = positionMapper.get(player).get();
    IntVector2 targetCoords = position.toIntVector2().add(direction.getVector());

    Entity area = areaManager.getArea();

    if (!coordsInsideArea(targetCoords, area)) {
      return;
    }

    Tile[][] tiles = tilesMapper.get(area).get();

    Tile currentTile = Tile.get(tiles, position.toIntVector2());
    Tile targetTile = Tile.get(tiles, targetCoords);
    switch (targetTile.getPermissions()) {
      case OBSTACLE:
        break;
      case PASSAGE:
        moveThroughAreaPassage(targetTile.getAreaPassage(),
                               targetCoords,
                               direction);
        break;
      case RAMP:
        moveOnRamp(currentTile,
                   findRampEnd(tiles, targetCoords, direction),
                   direction);
        break;
      default:
        Move move = new Move();

        move.setDirection(direction);
        if (currentTile.getPermissions() == TilePermission.RAMP) {
          float moveVectorZSgn = direction == lastMove.getDirection() ? 1 : -1;

          move.setVectorZ(moveVectorZSgn * lastMove.getVector().z);
        }
        move.setSpeed(movementSpeedMapper.get(player).get());

        queue.push(move);
    }
  }

  private void moveOnRamp(Tile currentTile,
                          Tile rampEnd,
                          Direction direction) {
    float moveVectorZ;
    if (currentTile.getPermissions() == TilePermission.RAMP) {
      moveVectorZ = lastMove.getVector().z;
    } else {
      int currentElevation = currentTile.getPermissions().getElevation();
      int finalElevation = rampEnd.getPermissions().getElevation();

      moveVectorZ = currentElevation < finalElevation ? .5f : -.5f;
    }

    Move move = new Move();

    move.setDirection(direction);
    move.setVectorZ(moveVectorZ);
    move.setSpeed(movementSpeedMapper.get(player).get());

    queue.push(move);
  }

  private Tile findRampEnd(Tile[][] tiles,
                           IntVector2 startCoords,
                           Direction direction) {
    IntVector2 coords = startCoords.cpy();

    Tile end;
    do {
      end = Tile.get(tiles, coords);

      coords.add(direction.getVector());
    } while (end.getPermissions() == TilePermission.RAMP);

    return end;
  }

  private void moveThroughAreaPassage(final TileAreaPassage passage,
                                      final IntVector2 passageCoords,
                                      final Direction direction) {
    float entryDuration = moveIntoAreaPassage(passageCoords, direction);

    Timer.schedule(
        new Timer.Task() {
          @Override
          public void run() {
            areaManager.unloadCurrent();
            areaManager.load(passage.getTargetAreaName());

            // TODO: Get this out of here
            if (areaTypeMapper.get(areaManager.getArea()).get() ==
                AreaType.OUTDOOR) {
              world.getManager(LocalAmbienceManager.class)
                   .setAmbience(world.getManager(WeatherMan.class).getWeather());
            } else {
              world.getManager(LocalAmbienceManager.class)
                   .setAmbience(new Ambience());
            }

            IntVector3 exitPosition = new IntVector3(passage.getTargetCoords());

            positionMapper.get(player).set(exitPosition);
            displayable.updatePosition(exitPosition);
            // TODO: Camera stuff out of here?
            camera.updateTargetPosition(exitPosition);

            moveOutOfAreaPassage(passage, direction);
          }
        }, entryDuration + PASSAGE_BLACKOUT_DURATION);
  }

  private float moveIntoAreaPassage(IntVector2 passageCoords,
                                    Direction direction) {
    final ModelDisplayable areaDisplayable
        = (ModelDisplayable) displayableMapper.get(areaManager.getArea()).get();

    String entryAnimationId
        = AreaManager.constructPassageAnimationId(passageCoords,
                                                  PassageAction.ENTRY);

    float entryDelay
        = areaDisplayable.animateIfAnimationExists(entryAnimationId) ? 1 : 0;

    enabled = false;

    final Move entryMove = new Move();
    entryMove.setDirection(direction);
    entryMove.setSpeed(movementSpeedMapper.get(player).get());
    Timer.schedule(
        new Timer.Task() {
          @Override
          public void run() {
            queue.push(entryMove);

            world.getSystem(ScreenFadingSystem.class)
                .fade(ScreenFadingSystem.FadeType.FADE_OUT, .2f);

            enabled = true;
          }
        }, entryDelay);

    return entryDelay + entryMove.getDuration();
  }

  private void moveOutOfAreaPassage(TileAreaPassage passage,
                                    Direction direction) {
    float exitAnimationWaitTime = 0;

    String exitAnimationId
        = AreaManager.constructPassageAnimationId(
            passage.getTargetCoords(),
            PassageAction.EXIT);

    ModelDisplayable areaDisplayable
        = (ModelDisplayable) displayableMapper.get(areaManager.getArea()).get();

    areaDisplayable.animateIfAnimationExists(exitAnimationId);

    final Move exitMove = new Move();
    exitMove.setDirection(direction);
    exitMove.setSpeed(movementSpeedMapper.get(player).get());
    Timer.schedule(new Timer.Task() {
          @Override
          public void run() {
            world.getSystem(ScreenFadingSystem.class)
                .fade(ScreenFadingSystem.FadeType.FADE_IN, .2f);

            queue.push(exitMove);
          }
        }, exitAnimationWaitTime);
  }

  private void advanceMove() {
    if (!currentMove.hasFinished()) {
      if (!currentMove.hasStarted()) {
        IntVector3 position = positionMapper.get(player).get();
        IntVector2 targetPosition
            = position.toIntVector2()
                      .add(currentMove.getDirection().getVector());

        Tile[][] tiles = tilesMapper.get(areaManager.getArea()).get();
        Tile targetTile = tiles[targetPosition.y][targetPosition.x];

        displayable.animate(WALK_ANIMATION_CYCLE[walkAnimationCycleCounter++],
                            currentMove.getDirection(),
                            currentMove.getDuration());

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

      displayable.move(translation);
      camera.translateTargetPosition(translation);

      currentMove.setElapsed(currentMove.getElapsed() + delta);
    } else {
      lastMove = currentMove;
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
