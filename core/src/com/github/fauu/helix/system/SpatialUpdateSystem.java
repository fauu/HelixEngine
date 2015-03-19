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
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.datum.SpatialUpdateRequest;

public class SpatialUpdateSystem extends EntityProcessingSystem {

  @Wire
  private ComponentMapper<PositionComponent> positionMapper;

  @Wire
  private ComponentMapper<DimensionsComponent> dimensionsMapper;

  @Wire
  private ComponentMapper<OrientationComponent> orientationMapper;

  @Wire
  private ComponentMapper<TilesComponent> tileDataMapper;
  
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
      Object updateValue;

      switch (request.getType()) {
        case OPACITY:
          updateValue = request.getValue();
          break;
        case POSITION:
          Vector3 newPosition = (Vector3) request.getValue();

          positionMapper.get(e).set(newPosition);
          updateValue = newPosition;
          break;
        case TILES_PARTIAL:
          updateValue = request.getValue();
          break;
        default: throw new IllegalStateException();
      }

      spatialFormMapper.get(e).get().update(request.getType(), updateValue);
    }
  }

  public class TileDataPartialUpdateDTO {

  }

}
