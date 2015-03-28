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

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.spatial.ModelSpatial;
import com.github.fauu.helix.spatial.Spatial;

// TODO: Cache animation controllers
public class SpatialAnimationUpdateSystem extends EntityProcessingSystem {

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;

  public SpatialAnimationUpdateSystem() {
    super(Aspect.getAspectForAll(SpatialFormComponent.class));
  }

  @Override
  protected void process(Entity e) {
    Spatial spatial = spatialFormMapper.get(e).get();

    if (spatial instanceof ModelSpatial) {
      ((ModelSpatial) spatial).getAnimationController()
                              .update(Gdx.graphics.getDeltaTime());
    }
  }

}
