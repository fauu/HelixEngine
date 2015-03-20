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

package com.github.fauu.helix.editor.system;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.editor.HelixEditor;

public class CameraControlSystem extends VoidEntitySystem {
  
  private static final float FLAT_MOVEMENT_SPEED = 1;

  private static final float VERTICAL_MOVEMENT_STEP = 5;

  private static final int MAX_ZOOM_LEVEL = 3;

  private static final int MIN_ZOOM_LEVEL = -8;
  
  @Wire
  private PerspectiveCamera camera;
  
  private Vector3 translation = new Vector3();

  private int zoomLevel;

  private ViewType currentView;

  @Override
  protected void initialize() {
    currentView = ViewType.PERSPECTIVE;
  }

  @Override
  protected void processSystem() {
    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      translation.y += 1;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      translation.x += 1;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      translation.y -= 1;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      translation.x -= 1;
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.K) &&
        zoomLevel < MAX_ZOOM_LEVEL) {
      translation.z -= 1;

      zoomLevel++;
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.J) &&
        zoomLevel > MIN_ZOOM_LEVEL) {
      translation.z += 1;

      zoomLevel--;
    }

    camera.translate(translation.x * FLAT_MOVEMENT_SPEED,
        translation.y * FLAT_MOVEMENT_SPEED,
        translation.z * VERTICAL_MOVEMENT_STEP);

    translation.setZero();

    if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1) &&
         currentView != ViewType.PERSPECTIVE) {
       camera.rotateAround(camera.position, new Vector3(1, 0, 0), 45);

       HelixEditor.getInstance().centerCameraOnAreaAction();

       currentView = ViewType.PERSPECTIVE;
     }

    if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7) &&
        currentView != ViewType.TOP) {
      camera.rotateAround(camera.position, new Vector3(1, 0, 0), -45);

      HelixEditor.getInstance().centerCameraOnAreaAction();

      currentView = ViewType.TOP;
    }
  }

  public enum ViewType {
    PERSPECTIVE, TOP;
  }

}
