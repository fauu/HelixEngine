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
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.fauu.helix.component.DimensionsComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.TilesComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.json.wrapper.AreaWrapper;
import com.github.fauu.helix.json.wrapper.TileWrapper;
import com.github.fauu.helix.spatial.AreaSpatial;
import com.github.fauu.helix.util.IntVector2;

public class AreaManager extends Manager {

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;
  
  @Wire
  private AssetManager assetManager;

  public void load(String name) {
    loadFromFile(Gdx.files.internal("area/" + name + ".json"), name);
  }
  
  public void loadFromFile(FileHandle file, String name) {
    Json json = new Json();

    AreaWrapper areaWrapper = json.fromJson(AreaWrapper.class, file);

    Array<Tile> tiles = new Array<Tile>();
    int i = 0;
    for (TileWrapper wrapper : areaWrapper.tiles) {
      Tile tile = new Tile();

      tile.setIndex(i);

      tile.setPermissions(wrapper.permissions);

      tiles.add(tile);

      i++;
    }

    String modelPath = "model/" + name + ".g3db";

    assetManager.load(modelPath, Model.class);
    assetManager.finishLoading();

    Entity area = world.createEntity()
                       .edit()
                       .add(new TilesComponent(tiles))
                       .add(new DimensionsComponent(
                           new IntVector2(areaWrapper.width,
                                          areaWrapper.length)))
                       .add(new SpatialFormComponent(
                           new AreaSpatial(
                               assetManager.get(modelPath, Model.class))))
                       .add(new VisibilityComponent())
                       .getEntity();
    world.getManager(TagManager.class).register("area", area);
   
  }
  
  public void unloadAll() {
    // TODO: Implement me
  }

}
