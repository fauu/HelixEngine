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

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.component.PositionComponent;
import com.github.fauu.helix.component.SizeComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.TileDataComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.editor.world.spatial.TileSelectionHighlightSpatial;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.spatial.Spatial.UpdateType;

public class TileSelectionSystem extends EntityProcessingSystem {
  
  @Wire
  private ComponentMapper<TileDataComponent> tileDataMapper;
  
  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;
  
  @Wire
  private ComponentMapper<VisibilityComponent> visibilityMapper;
  
  @Wire
  private TileMatchingSystem tileMatchingSystem;
  
  private Vector2 startCoords;

  private Vector2 endCoords;

  boolean finished;
  
  private Tile matchedTile;
  
  private Tile newMatchedTile;
  
  @SuppressWarnings("unchecked")
  public TileSelectionSystem() {
    super(Aspect.getAspectForAll(TileDataComponent.class));

    finished = true;
  }

  @Override
  protected void initialize() {
    TileSelectionHighlightSpatial spatial = new TileSelectionHighlightSpatial();
    spatial.setGeometryManager(world.getManager(GeometryManager.class));

    Entity selectionHighlight 
        = world.createEntity()
               .edit()
               .add(new SpatialFormComponent(spatial))
               .add(new PositionComponent())
               .add(new SizeComponent())
               .add(new TileDataComponent())
               .getEntity();
    world.getManager(TagManager.class)
         .register("SELECTION_HIGHLIGHT", selectionHighlight);
  }

  @Override
  protected void process(Entity e) {
    if (!world.getManager(GroupManager.class).isInGroup(e, "MAP_REGION")) {
      return;
    }
    
    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      Array<Tile> selectedTilesForUpdate = null;
      
      if (finished) {
        matchedTile = tileMatchingSystem.getMatchedTile();
        
        if (matchedTile != null) {
          finished = false;
        
          startCoords = matchedTile.getFlatPosition();
          endCoords = matchedTile.getFlatPosition();
          
          selectedTilesForUpdate = getSelectedTiles(tileDataMapper.get(e).get(),  
                                                    startCoords, 
                                                    endCoords);
          
          Entity selectionHighlight = world.getManager(TagManager.class)
                                           .getEntity("SELECTION_HIGHLIGHT");
          
          if (!visibilityMapper.has(selectionHighlight)) {
            selectionHighlight.edit().create(VisibilityComponent.class);
          }
          
          Gdx.app.debug("msg", "Started selecting: " + startCoords + endCoords);
        } else {
          startCoords = null;
          endCoords = null;

          world.getManager(TagManager.class)
               .getEntity("SELECTION_HIGHLIGHT")
               .edit()
               .remove(VisibilityComponent.class);
        }
      } else {
        newMatchedTile = tileMatchingSystem.getMatchedTile();
        
        if (newMatchedTile != null && newMatchedTile != matchedTile) {
          endCoords = newMatchedTile.getFlatPosition();

          selectedTilesForUpdate = getSelectedTiles(tileDataMapper.get(e).get(),  
                                                    startCoords, 
                                                    endCoords);
          
          matchedTile = newMatchedTile;

          Gdx.app.debug("msg", "Updated selection: " + startCoords + endCoords);
        }
      }
      
      if (selectedTilesForUpdate != null) {
        Entity selectionHighlight = world.getManager(TagManager.class)
                                         .getEntity("SELECTION_HIGHLIGHT");
        spatialFormMapper.get(selectionHighlight)
                         .requestUpdate(new SpatialUpdateRequest(
                             UpdateType.TILE_DATA, selectedTilesForUpdate));
      }
    } else if (!finished) {
      finished = true;

      Gdx.app.debug("msg", "Finished selection: " + startCoords + endCoords);
    }
  }
  
  private Array<Tile> getSelectedTiles(Array<Tile> tiles, 
                                       Vector2 startCoords, 
                                       Vector2 endCoords) {
    Rectangle selectionArea = constructRectangle(startCoords, endCoords);

    Array<Tile> selectedTiles = new Array<Tile>();

    for (Tile tile : tiles) {
      if (selectionArea.contains(tile.getFlatPosition())) {
        selectedTiles.add(tile);
      }
    }
    
    return selectedTiles;
  }
  
  private Rectangle constructRectangle(Vector2 corner1, Vector2 corner2) {
    float x;
    float w;
    
    if (corner1.x <= corner2.x) {
      x = corner1.x;
      w = corner2.x - corner1.x;
    } else {
      x = corner2.x;
      w = corner1.x - corner2.x;
    }

    float y;
    float h;

    if (corner1.y <= corner2.y) {
      y = corner1.y;
      h = corner2.y - corner1.y;
    } else {
      y = corner2.y;
      h = corner1.y - corner2.y;
    }

    return new Rectangle(x, y, w, h);
  }

}
