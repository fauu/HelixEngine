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
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.component.GeometryNameComponent;
import com.github.fauu.helix.component.PositionComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.TileDataComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.editor.world.spatial.MatchedTileHighlightSpatial;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.spatial.Spatial;

public class TileMatchingSystem extends EntityProcessingSystem {
  
  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;
  
  @Wire
  private ComponentMapper<TileDataComponent> tileDataMapper;

  @Wire
  private ComponentMapper<VisibilityComponent> visibilityMapper;
  
  @Wire
  private ComponentMapper<GeometryNameComponent> geometryNameMapper;

  @Wire
  private PerspectiveCamera camera;

  private Tile matchedTile;
  
  @SuppressWarnings("unchecked")
  public TileMatchingSystem() {
    super(Aspect.getAspectForAll(TileDataComponent.class));
  }
  
  @Override
  protected void initialize() { }

  @Override
  protected void process(Entity e) {
    if (Gdx.input.getDeltaX() == 0 && Gdx.input.getDeltaY() == 0) {
      return;
    }
    
    if (!world.getManager(GroupManager.class).isInGroup(e, "MAP_REGION")) {
      return;
    }
    
    Entity matchedTileHighlight = world.getManager(TagManager.class)
                                       .getEntity("MATCHED_TILE_HIGHLIGHT");

    if (matchedTileHighlight == null) {
      matchedTileHighlight 
          = world.createEntity()
                 .edit()
                 .add(new SpatialFormComponent(
                      new MatchedTileHighlightSpatial()))
                 .add(new PositionComponent())
                 .add(new GeometryNameComponent())
                 .getEntity();
      world.getManager(TagManager.class)
           .register("MATCHED_TILE_HIGHLIGHT", matchedTileHighlight);
    }

    Array<Tile> tiles = tileDataMapper.get(e).get();

    Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

    boolean foundMatch = false;

    for (Tile tile : tiles) {
      BoundingBox boundingBox = world.getManager(GeometryManager.class)
                                     .getGeometry(tile.getGeometryName())
                                     .getMesh()
                                     .calculateBoundingBox();
      Matrix4 transformationMatrix = new Matrix4();
      transformationMatrix.translate(new Vector3(tile.getPosition().x,
                                                 tile.getPosition().y,
                                                 0));
      boundingBox.mul(transformationMatrix);

      if (Intersector.intersectRayBoundsFast(ray, boundingBox)) {
        if (tile != matchedTile) {
          spatialFormMapper
              .get(matchedTileHighlight)
              .requestUpdate(
                  new SpatialUpdateRequest(Spatial.UpdateType.POSITION, 
                                           tile.getPosition()));

          spatialFormMapper
              .get(matchedTileHighlight)
              .requestUpdate(
                  new SpatialUpdateRequest(Spatial.UpdateType.GEOMETRY, 
                                           tile.getGeometryName()));

          if (visibilityMapper.getSafe(matchedTileHighlight) == null) {
            matchedTileHighlight.edit().create(VisibilityComponent.class);
          }

          matchedTile = tile;
        }

        foundMatch = true;

        break;
      }
    }

    if (!foundMatch) {
      matchedTile = null;

      matchedTileHighlight.edit().remove(VisibilityComponent.class);
    }
  }

  public Tile getPrevousMatchedTile() {
    return matchedTile;
  }
  
  public Tile getMatchedTile() {
    return matchedTile;
  }

}
