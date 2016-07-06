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

package com.github.fauu.helix.editor.manager;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.component.DimensionsComponent;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.util.IntVector2;

public class CameraIntermediary extends Manager {

  @Wire
  private ComponentMapper<DimensionsComponent> dimensionsMapper;

  @Wire
  private HelixCamera camera;

  public void centerCameraOnArea() {
    Entity area = world.getManager(TagManager.class).getEntity("area");

    IntVector2 dimensions = dimensionsMapper.get(area).get();

    Vector3 position = camera.position;

    float zDirection = camera.direction.z;

    float yTranslation
        = (float) (Math.sqrt((1 - (zDirection * zDirection))) / zDirection) *
                  camera.position.z;

    camera.position.set(new Vector3(dimensions.x / 2,
                                    dimensions.y / 2 + yTranslation,
                                    position.z));
  }

}
