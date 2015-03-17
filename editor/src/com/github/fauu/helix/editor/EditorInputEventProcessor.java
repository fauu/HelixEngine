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

package com.github.fauu.helix.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class EditorInputEventProcessor implements InputProcessor {

  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Input.Keys.O:
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
          HelixEditor.getInstance().openMapRegionAction();
        }
        break;
      case Input.Keys.N:
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
        }
        break;
      case Input.Keys.F4:
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
          HelixEditor.getInstance().exitAction();
        }
        break;
      default: break;
    }

    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
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
    return false;
  }

}
