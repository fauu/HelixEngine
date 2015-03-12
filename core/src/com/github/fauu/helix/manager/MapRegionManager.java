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

package com.github.fauu.helix.manager;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.TileDataComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.json.wrapper.MapRegionWrapper;
import com.github.fauu.helix.json.wrapper.ObjectWrapper;
import com.github.fauu.helix.json.wrapper.TileWrapper;
import com.github.fauu.helix.spatial.Spatial;
import com.github.fauu.helix.spatial.TerrainSpatial;

public class MapRegionManager extends Manager {
  
  private static final String DEFAULT_TILE_TEXTURE = "grass1";
  
  private static final String DEFAULT_TILE_GEOMETRY = "flat";

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;
  
  @Wire
  private AssetManager assetManager;

  public void load(String name) {
    loadFromFile(Gdx.files.internal("map-region/" + name + ".json"));
  }
  
  public void loadFromFile(FileHandle file) {
    Json json = new Json();

    MapRegionWrapper mapRegionWrapper
        = json.fromJson(MapRegionWrapper.class, file);
    
    /* Creating Map Region entity */
    Entity mapRegion = world.createEntity()
                            .edit()
                            .getEntity();
    world.getManager(GroupManager.class).add(mapRegion, "MAP_REGION");
   
    /* Loading tile geometries */
    for (TileWrapper tileWrapper : mapRegionWrapper.tiles) {
      world.getManager(GeometryManager.class).load(tileWrapper.geometry);
    }
    
    /* Loading tiles */
    Array<Tile> tiles = new Array<Tile>();
    for (TileWrapper tileWrapper : mapRegionWrapper.tiles) {
      Tile tile = new Tile();

      tile.setPosition(new Vector3(tileWrapper.x, 
                                   tileWrapper.y, 
                                   tileWrapper.z));
      tile.setOrientation(Direction.valueOf(tileWrapper.orientation
                                                       .toUpperCase()
                                                       .trim())); 
      tile.setGeometryName(tileWrapper.geometry);
      tile.setTextureName(tileWrapper.texture);
      
      tiles.add(tile);
    }

    // FIXME: temporary
    assetManager.load("texture-set/1.atlas", TextureAtlas.class);
    assetManager.finishLoading();
    TextureAtlas textureSet = assetManager.get("texture-set/1.atlas");
    
    /* Creating terrain */
    TerrainSpatial terrainSpatial = new TerrainSpatial(textureSet);
    terrainSpatial.setGeometryManager(world.getManager(GeometryManager.class));
    terrainSpatial.initialize(tiles);

    Entity terrain = world.createEntity()
                          .edit()
                          .add(new TileDataComponent(tiles))
                          .add(new SpatialFormComponent(terrainSpatial))
                          .add(new VisibilityComponent())
                          .getEntity();
    world.getManager(GroupManager.class).add(terrain, "MAP_REGION");

    /* Loading objects */
    for (ObjectWrapper objectWrapper : mapRegionWrapper.objects) {
      Entity object = world.getManager(ObjectManager.class)
                           .load(objectWrapper.model);
      
      spatialFormMapper
          .get(object)
          .requestUpdate(new SpatialUpdateRequest(Spatial.UpdateType.POSITION, 
                                         new Vector3(objectWrapper.x, 
                                                     objectWrapper.y, 
                                                     objectWrapper.z)));

      spatialFormMapper
          .get(object)
          .requestUpdate(new SpatialUpdateRequest(Spatial.UpdateType.ORIENTATION, 
                                                  Direction.valueOf(
                                                      objectWrapper.orientation
                                                                   .toUpperCase()
                                                                   .trim())));

      world.getManager(GroupManager.class).add(object, "MAP_REGION");
    }
  }
  
  public void unloadAll() {
    ImmutableBag<Entity> mapRegionElements
        = world.getManager(GroupManager.class).getEntities("MAP_REGION");
    
    for (Entity e : mapRegionElements) {
      e.deleteFromWorld();
    }
  }
  
  public void create(int width, int length) {
    /* Creating Map Region entity */
    Entity mapRegion = world.createEntity()
                            .edit()
                            .getEntity();
    world.getManager(GroupManager.class).add(mapRegion, "MAP_REGION");

    Array<Tile> tiles = new Array<Tile>();
    
    /* Creating tiles */
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < length; y++) {
        Tile tile = new Tile();

        tile.setPosition(new Vector3(x, y, 0));
        tile.setOrientation(Direction.SOUTH);
        tile.setGeometryName(DEFAULT_TILE_GEOMETRY);
        tile.setTextureName(DEFAULT_TILE_TEXTURE);
        
        tiles.add(tile);
      }
    }

    // FIXME: temporary
    assetManager.load("texture-set/1.atlas", TextureAtlas.class);
    assetManager.finishLoading();
    TextureAtlas textureSet = assetManager.get("texture-set/1.atlas");

    /* Loading default tile geometry */
    world.getManager(GeometryManager.class).load(DEFAULT_TILE_GEOMETRY);

    /* Creating terrain */
    TerrainSpatial terrainSpatial = new TerrainSpatial(textureSet);
    terrainSpatial.setGeometryManager(world.getManager(GeometryManager.class));
    terrainSpatial.initialize(tiles);

    Entity terrain = world.createEntity()
                          .edit()
                          .add(new TileDataComponent(tiles))
                          .add(new SpatialFormComponent(terrainSpatial))
                          .add(new VisibilityComponent())
                          .getEntity();
    world.getManager(GroupManager.class).add(terrain, "MAP_REGION");
  }
  
}
