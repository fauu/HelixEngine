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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.component.GeometryNameComponent;
import com.github.fauu.helix.component.OrientationComponent;
import com.github.fauu.helix.component.PositionComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.TextureNameComponent;
import com.github.fauu.helix.component.TileDataComponent;
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.manager.TextureManager;
import com.github.fauu.helix.spatial.dto.TextureDTO;

public class SpatialUpdateSystem extends EntityProcessingSystem {

  @Wire
  private ComponentMapper<PositionComponent> positionMapper;

  @Wire
  private ComponentMapper<OrientationComponent> orientationMapper;

  @Wire
  private ComponentMapper<GeometryNameComponent> geometryNameMapper;

  @Wire
  private ComponentMapper<TextureNameComponent> textureNameMapper;

  @Wire
  private ComponentMapper<TileDataComponent> tileDataMapper;
  
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
        case POSITION:
          Vector3 newPosition = (Vector3) request.getValue();
          
          positionMapper.get(e).set(newPosition);
          updateValue = newPosition;
          break;
        case ORIENTATION:
          Direction newOrientation = (Direction) request.getValue();
          
          orientationMapper.get(e).set(newOrientation);
          updateValue = newOrientation;
          break;
        case GEOMETRY:
          String newGeometryName = (String) request.getValue(); 
          
          geometryNameMapper.get(e).set(newGeometryName);
          updateValue = world.getManager(GeometryManager.class)
                             .getGeometry(newGeometryName)
                             .getMesh();
          break;
        case TEXTURE:
          String newTextureName = (String) request.getValue();
          
          TextureAtlas set = world.getManager(TextureManager.class)
                                  .getTextureSet();

          // TODO: findRegion result should be cached
          TextureRegion region = set.findRegion(newTextureName);
          Texture setTexture = region.getTexture();

          textureNameMapper.get(e).set(newTextureName);
          updateValue = new TextureDTO(setTexture, region);
          break;
        case TILE_DATA:
          @SuppressWarnings("unchecked")
          Array<Tile> newTileData = (Array<Tile>) request.getValue();
          
          tileDataMapper.get(e).set(newTileData);
          updateValue = newTileData;
          break;
        default: throw new IllegalStateException();
      }

      spatialFormMapper.get(e).get().update(request.getType(), updateValue);
    }
  }

}
