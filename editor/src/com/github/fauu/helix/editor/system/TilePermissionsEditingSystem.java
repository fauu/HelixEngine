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

package com.github.fauu.helix.editor.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.component.DimensionsComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.TilesComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.event.TilePermissionListStateChangedEvent;
import com.github.fauu.helix.editor.spatial.TilePermissionsGridSpatial;
import com.github.fauu.helix.spatial.Spatial;
import com.google.common.eventbus.Subscribe;

import java.util.HashMap;

public class TilePermissionsEditingSystem extends EntityProcessingSystem {

  private static final String atlasPath = "texture-atlas/tile-permissions.atlas";

  @Wire
  private ComponentMapper<DimensionsComponent> dimensionsMapper;

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire
  private TileHighlightingSystem tileHighlightingSystem;

  @Wire
  private AssetManager assetManager;

  private boolean ready;

  private Tile highlightedTile;

  private Tile lastUpdatedTile;

  private TilePermission selectedTilePermission;

  @SuppressWarnings("unchecked")
  public TilePermissionsEditingSystem() {
    super(Aspect.getAspectForAll(TilesComponent.class,
                                 DimensionsComponent.class));
  }

  @Override
  protected void initialize() {
    HelixEditor.getInstance().getWorldEventBus().register(this);

    assetManager.load(atlasPath, TextureAtlas.class);
    assetManager.finishLoading();

    Entity grid = world.createEntity()
                       .edit()
                       .getEntity();

    world.getManager(TagManager.class)
         .register("tilePermissionsGrid", grid);
  }

  private void consumeArea() {
    Entity area = world.getManager(TagManager.class).getEntity("area");

    Entity grid = world.getManager(TagManager.class)
                       .getEntity("tilePermissionsGrid");
    grid.edit()
        .add(new DimensionsComponent(dimensionsMapper.get(area).get()))
        .add(new SpatialFormComponent(
            new TilePermissionsGridSpatial(
                tilesMapper.get(area).get(),
                assetManager.get(atlasPath, TextureAtlas.class))))
        .add(new VisibilityComponent());
  }

  @Override
  protected void process(Entity e) {
    if (!ready) {
      consumeArea();

      ready = true;
    }

    Tile highlightedTile = tileHighlightingSystem.getHighlightedTile();

    if (highlightedTile != this.highlightedTile) {
      this.highlightedTile = highlightedTile;
    }

    if (lastUpdatedTile != highlightedTile &&
        highlightedTile != null &&
        Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      Entity area = world.getManager(TagManager.class).getEntity("area");

      Tile[][] tiles = tilesMapper.get(area).get();

      for (int y = 0; y < tiles.length; y++) {
        for (int x = 0; x < tiles[y].length; x++) {
          Tile tile = tiles[x][y];

          if (tile == highlightedTile) {
            tile.setPermissions(selectedTilePermission);

            HashMap<Integer, Tile> updatedTilesWithIndex = new HashMap<>();
            updatedTilesWithIndex.put(x + y * tiles.length, tile);

            Entity grid = world.getManager(TagManager.class)
                               .getEntity("tilePermissionsGrid");

            spatialFormMapper
                .get(grid)
                .requestUpdate(
                    new SpatialUpdateRequest(Spatial.UpdateType.TILES_PARTIAL,
                                             updatedTilesWithIndex));

            lastUpdatedTile = tile;

            break;
          }
        }
      }
    }
  }

  @Subscribe
  public void TilePermissionListStateChanged(
      TilePermissionListStateChangedEvent e) {
    selectedTilePermission = e.getMessage();
  }

}
