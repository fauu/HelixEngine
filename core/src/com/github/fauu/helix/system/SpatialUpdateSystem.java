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
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.datum.SpatialUpdateRequest;

public class SpatialUpdateSystem extends EntityProcessingSystem {

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;

  @SuppressWarnings("unchecked")
  public SpatialUpdateSystem() {
    super(Aspect.getAspectForOne(SpatialFormComponent.class));
  }

  @Override
  protected void process(Entity e) {
    SpatialUpdateRequest request;

    while ((request = spatialFormMapper.get(e).pollUpdateRequest()) != null) {
      spatialFormMapper.get(e)
                       .get()
                       .update(request.getType(), request.getValue());
    }
  }

}
