/*
 * Copyright (C) 2014-2016 Helix Engine Developers
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
import com.github.fauu.helix.component.DisplayableComponent;
import com.github.fauu.helix.component.TilesComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.datum.TileAreaPassage;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.displayable.TilePermissionsGridDisplayable;
import com.github.fauu.helix.editor.event.AreaLoadedEvent;
import com.github.fauu.helix.editor.event.AreaUnloadedEvent;
import com.github.fauu.helix.manager.AreaManager;
import com.github.fauu.helix.util.IntVector2;
import com.google.common.eventbus.Subscribe;

import java.util.HashMap;

public class TilePermissionsEditingSystem extends EntityProcessingSystem {

  private static final String atlasPath = "texture-atlas/tile-permissions.atlas";

  @Wire
  private AreaManager areaManager;

  @Wire
  private TagManager tagManager;

  @Wire
  private ComponentMapper<DimensionsComponent> dimensionsMapper;

  @Wire
  private ComponentMapper<DisplayableComponent> displayableMapper;

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire
  private TileHighlightingSystem tileHighlightingSystem;

  @Wire
  private AssetManager assetManager;

  private Tile highlightedTile;

  private Tile lastUpdatedTile;

  private boolean running;

  @SuppressWarnings("unchecked")
  public TilePermissionsEditingSystem() {
    super(Aspect.getAspectForAll(TilesComponent.class));
  }

  @Override
  protected void initialize() {
    HelixEditor.getInstance().getWorldEventBus().register(this);

    areaManager = world.getManager(AreaManager.class);
    tagManager = world.getManager(TagManager.class);

    assetManager.load(atlasPath, TextureAtlas.class);
    assetManager.finishLoading();
  }

  private void createGrid() {
    Entity area = areaManager.getArea();

    Entity grid = world.createEntity()
        .edit()
        .add(new DimensionsComponent(dimensionsMapper.get(area).get()))
        .add(new DisplayableComponent(
            new TilePermissionsGridDisplayable(
                tilesMapper.get(area).get(),
                assetManager.get(atlasPath, TextureAtlas.class))))
        .add(new VisibilityComponent())
        .getEntity();

    tagManager.register("tilePermissionsGrid", grid);
  }

  @Override
  protected void process(Entity e) {
    Tile highlightedTile = tileHighlightingSystem.getHighlightedTile();

    if (highlightedTile != this.highlightedTile) {
      this.highlightedTile = highlightedTile;
    }

    if (lastUpdatedTile != highlightedTile &&
        highlightedTile != null &&
        Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
      Entity area = areaManager.getArea();

      Tile[][] tiles = tilesMapper.get(area).get();

      for (int y = 0; y < tiles.length; y++) {
        for (int x = 0; x < tiles[y].length; x++) {
          Tile tile = tiles[y][x];

          if (tile == highlightedTile) {
            HelixEditor editor = HelixEditor.getInstance();

            TilePermission selectedTilePermission
                = editor.getTilePermissionListState().getSelected();
            tile.setPermissions(selectedTilePermission);

            if (selectedTilePermission == TilePermission.PASSAGE) {
              String selectedAreaName
                  = editor.getTilePassageAreaListState().getSelected();

              IntVector2 targetPosition
                  = editor.getTilePassageTargetPositionFieldState()
                          .getPosition();

              tile.setAreaPassage(
                  new TileAreaPassage(selectedAreaName, targetPosition));
            }

            HashMap<Integer, Tile> updatedTilesWithIndex = new HashMap<>();
            updatedTilesWithIndex.put(x + y * tiles.length, tile);

            Entity grid = tagManager.getEntity("tilePermissionsGrid");

            TilePermissionsGridDisplayable gridDisplayable
                = (TilePermissionsGridDisplayable)
                    displayableMapper.get(grid).get();
            gridDisplayable.updateTiles(updatedTilesWithIndex);

            lastUpdatedTile = tile;

            break;
          }
        }
      }
    }
  }

  @Override
  public boolean checkProcessing() {
    return running;
  }

  @Subscribe
  public void areaLodaded(AreaLoadedEvent e) {
    createGrid();

    running = true;
  }

  @Subscribe
  public void areaUnloaded(AreaUnloadedEvent e) {
    running = false;

    tagManager.getEntity("tilePermissionsGrid").deleteFromWorld();
  }

}
