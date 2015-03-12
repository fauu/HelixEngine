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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.github.fauu.helix.Direction;

public class WorldInputSystem extends VoidEntitySystem 
                              implements InputProcessor {
  
  @Wire
  CameraControlSystem cameraControlSystem;

  @Override
  protected void processSystem() { }

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.W:
        cameraControlSystem.setMovementDirection(Direction.NORTH);
      break;
      case Input.Keys.D:
        cameraControlSystem.setMovementDirection(Direction.EAST);
      break;
      case Input.Keys.S:
        cameraControlSystem.setMovementDirection(Direction.SOUTH);
      break;
      case Input.Keys.A:
        cameraControlSystem.setMovementDirection(Direction.WEST);
      break;
    }
    
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Input.Keys.W:
        cameraControlSystem.unsetMovementDirection(Direction.NORTH);
      break;
      case Input.Keys.D:
        cameraControlSystem.unsetMovementDirection(Direction.EAST);
      break;
      case Input.Keys.S:
        cameraControlSystem.unsetMovementDirection(Direction.SOUTH);
      break;
      case Input.Keys.A:
        cameraControlSystem.unsetMovementDirection(Direction.WEST);
      break;
    }

    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    cameraControlSystem.moveVertically(amount);
    
    return false;
  }

}
