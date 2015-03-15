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
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.editor.world.spatial.MatchedTileHighlightSpatial;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.spatial.Spatial;

public class TileHighlightingSystem extends EntityProcessingSystem {
  
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

  private Tile highlightedTile;
  
  @SuppressWarnings("unchecked")
  public TileHighlightingSystem() {
    super(Aspect.getAspectForAll(TileDataComponent.class));
  }
  
  @Override
  protected void initialize() {
    Entity highlight = world.createEntity()
                            .edit()
                            .add(new SpatialFormComponent(
                                new MatchedTileHighlightSpatial()))
                            .add(new PositionComponent())
                            .add(new GeometryNameComponent())
                            .getEntity();
    world.getManager(TagManager.class).register("TILE_HIGHLIGHT", highlight);
  }

  @Override
  protected void process(Entity e) {
    if (Gdx.input.getDeltaX() == 0 && Gdx.input.getDeltaY() == 0) {
      return;
    }
    
    if (!world.getManager(GroupManager.class).isInGroup(e, "MAP_REGION")) {
      return;
    }
    
    Entity highlight = world.getManager(TagManager.class)
                            .getEntity("TILE_HIGHLIGHT");

    Array<Tile> tiles = tileDataMapper.get(e).get();

    Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

    boolean hovering = false;

    for (Tile tile : tiles) {
      BoundingBox boundingBox = world.getManager(GeometryManager.class)
                                     .getGeometry(tile.getGeometryName())
                                     .getMesh()
                                     .calculateBoundingBox();

      Matrix4 transformation = new Matrix4();
      transformation.translate(new Vector3(tile.getPosition().x,
                                           tile.getPosition().y,
                                           0));

      boundingBox.mul(transformation);

      if (Intersector.intersectRayBoundsFast(ray, boundingBox)) {
        if (tile != highlightedTile) {
          spatialFormMapper
              .get(highlight)
              .requestUpdate(
                  new SpatialUpdateRequest(Spatial.UpdateType.POSITION, 
                                           tile.getPosition()));

          spatialFormMapper
              .get(highlight)
              .requestUpdate(
                  new SpatialUpdateRequest(Spatial.UpdateType.GEOMETRY, 
                                           tile.getGeometryName()));

          if (visibilityMapper.getSafe(highlight) == null) {
            highlight.edit().create(VisibilityComponent.class);
          }

          highlightedTile = tile;
        }

        hovering = true;

        break;
      }
    }

    if (!hovering) {
      highlightedTile = null;

      highlight.edit().remove(VisibilityComponent.class);
    }
  }

  public Tile getHighlightedTile() {
    return highlightedTile;
  }

}
