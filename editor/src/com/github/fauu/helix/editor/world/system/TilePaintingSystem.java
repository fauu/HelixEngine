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

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.event.TilePropertiesStateChangedEvent;
import com.github.fauu.helix.editor.world.spatial.TileToPaintSpatial;
import com.github.fauu.helix.spatial.Spatial.UpdateType;
import com.google.common.eventbus.Subscribe;

public class TilePaintingSystem extends VoidEntitySystem {
  
  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;

  @Wire
  private ComponentMapper<VisibilityComponent> visibilityMapper;

  @Wire
  private ComponentMapper<GeometryNameComponent> geometryNameMapper;

  @Wire
  private ComponentMapper<TextureNameComponent> textureNameMapper;
  
  @Wire
  private ComponentMapper<TileDataComponent> tileDataMapper;
  
  @Wire
  private TileMatchingSystem tileMatchingSystem;
  
  private Tile matchedTile;
  
  private boolean tilePropertiesChanged;
  
  private boolean currentTilePainted;
  
  public TilePaintingSystem() {
    super();
  }
  
  @Override
  protected void initialize() {
    HelixEditor.getInstance().getWorldEventBus().register(this);

    TileToPaintSpatial spatial = new TileToPaintSpatial();
    
    Entity tileToPaint
        = world.createEntity()
               .edit()
               .add(new SpatialFormComponent(spatial))
               .add(new PositionComponent())
               .add(new GeometryNameComponent())
               .add(new TextureNameComponent())
               .getEntity();
    world.getManager(TagManager.class).register("TILE_TO_PAINT", tileToPaint);
  }

  @Override
  protected void processSystem() {
    if (tileMatchingSystem.getMatchedTile() != matchedTile || 
        tilePropertiesChanged) {
      
      if (!tilePropertiesChanged) {
        currentTilePainted = false;
      }

      matchedTile = tileMatchingSystem.getMatchedTile();
      
      Entity tileToPaint = world.getManager(TagManager.class)
                                .getEntity("TILE_TO_PAINT");
      
      if (matchedTile == null) {
        tileToPaint.edit().remove(VisibilityComponent.class);
      } else {
        SpatialFormComponent spatialFormComponent
            = spatialFormMapper.get(tileToPaint);
        
        SpatialUpdateRequest request;

        request = new SpatialUpdateRequest(UpdateType.POSITION, 
                                           matchedTile.getPosition());
        spatialFormComponent.requestUpdate(request);
        
        Tile tileProperties = HelixEditor.getInstance()
                                         .getTilePropertiesState()
                                         .getData();
        
        request = new SpatialUpdateRequest(UpdateType.GEOMETRY, 
                                           tileProperties.getGeometryName());
        spatialFormComponent.requestUpdate(request);

        request = new SpatialUpdateRequest(UpdateType.TEXTURE, 
                                           tileProperties.getTextureName());
        spatialFormComponent.requestUpdate(request);

        if (visibilityMapper.getSafe(tileToPaint) == null) {
          tileToPaint.edit().create(VisibilityComponent.class);
        }
      
        tilePropertiesChanged = false;
      }
    }
    
    if (!currentTilePainted && 
        matchedTile != null && 
        Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      Entity terrain = world.getManager(TagManager.class).getEntity("TERRAIN");

      Array<Tile> tiles = tileDataMapper.get(terrain).get();
      
      for (int i = 0; i < tiles.size; i++) {
        if (tiles.get(i).getFlatPosition().equals(
                matchedTile.getFlatPosition())) {
          Tile updatedTile = new Tile();

          Tile tileProperties = HelixEditor.getInstance()
                                           .getTilePropertiesState()
                                           .getData();
          
          updatedTile.setPosition(matchedTile.getPosition());
          updatedTile.setOrientation(tileProperties.getOrientation());
          updatedTile.setGeometryName(tileProperties.getGeometryName());
          updatedTile.setTextureName(tileProperties.getTextureName());
          updatedTile.setElevation(tileProperties.getElevation());
          
          tiles.set(i, updatedTile);
          
          SpatialUpdateRequest request
              = new SpatialUpdateRequest(UpdateType.TILE_DATA, tiles);
          
          spatialFormMapper.get(terrain).requestUpdate(request);
          
          currentTilePainted = true;

          break;
        }
      }
    }
  }

  @Subscribe
  public void tilePropertiesStateChanged(TilePropertiesStateChangedEvent e) {
    tilePropertiesChanged = true;
  }

}
