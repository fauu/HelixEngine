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
import com.github.fauu.helix.component.*;

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
//    SpatialUpdateRequest request;
//
//    while ((request = spatialFormMapper.get(e).pollUpdateRequest()) != null) {
//      Object updateValue;
//
//      switch (request.getType()) {
//        case POSITION:
//          Vector3 newPosition = (Vector3) request.getValue();
//
//          positionMapper.get(e).set(newPosition);
//          updateValue = newPosition;
//          break;
//        case ORIENTATION:
//          Direction newOrientation = (Direction) request.getValue();
//
//          orientationMapper.get(e).set(newOrientation);
//          updateValue = newOrientation;
//          break;
//        case GEOMETRY:
//          String newGeometryName = (String) request.getValue();
//
//          geometryNameMapper.get(e).set(newGeometryName);
//          updateValue = world.getManager(GeometryManager.class)
//                             .getGeometry(newGeometryName)
//                             .getMesh();
//          break;
//        case TEXTURE:
//          String newTextureName = (String) request.getValue();
//
//          TextureAtlas set = world.getManager(TextureManager.class)
//                                  .getTextureSet();
//
//          // TODO: findRegion result should be cached
//          TextureRegion region = set.findRegion(newTextureName);
//          Texture setTexture = region.getTexture();
//
//          textureNameMapper.get(e).set(newTextureName);
//          updateValue = new TextureDTO(setTexture, region);
//          break;
//        case TILE_DATA:
//          @SuppressWarnings("unchecked")
//          Array<Tile> newTileData = (Array<Tile>) request.getValue();
//
//          tileDataMapper.get(e).set(newTileData);
//          updateValue = newTileData;
//          break;
//        case TILE_DATA_PARTIAL:
//          Entity terrain = world.getManager(TagManager.class)
//                                .getEntity("TERRAIN");
//
//          if (e.equals(terrain)) {
//            newTileData = (Array<Tile>) request.getValue();
//
//            Array<Tile> tileData = tileDataMapper.get(e).get();
//
//            Vector2 terrainDimensions = dimensionsMapper.get(terrain).get();
//
//            for (int i = 0; i < newTileData.size; i++) {
//              Tile updatedTile = newTileData.get(i);
//
//              int tileIndex = (int) (updatedTile.getPosition().x +
//                                     (terrainDimensions.x *
//                                      updatedTile.getPosition().y));
//
//              tileData.set(tileIndex, newTileData.get(i));
//            }
//
//            tileDataMapper.get(e).set(tileData);
//
//            updateValue
//                = new PartialTileUpdateDTO(newTileData, terrainDimensions);
//          } else {
//            throw new UnsupportedOperationException();
//          }
//          break;
//        default: throw new IllegalStateException();
//      }
//
//      spatialFormMapper.get(e).get().update(request.getType(), updateValue);
//    }
  }

}
