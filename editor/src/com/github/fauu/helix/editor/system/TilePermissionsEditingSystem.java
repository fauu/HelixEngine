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
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.fauu.helix.component.DimensionsComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.TilesComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.editor.spatial.TilePermissionsGridSpatial;

public class TilePermissionsEditingSystem extends EntityProcessingSystem {

  private static final String atlasPath = "texture-atlas/tile-permissions.atlas";

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire
  private ComponentMapper<DimensionsComponent> dimensionsMapper;

  @Wire
  private TileHighlightingSystem tileHighlightingSystem;

  @Wire
  private AssetManager assetManager;

  private boolean ready;

  @SuppressWarnings("unchecked")
  public TilePermissionsEditingSystem() {
    super(Aspect.getAspectForAll(TilesComponent.class,
                                 DimensionsComponent.class));
  }

  @Override
  protected void initialize() {
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
                dimensionsMapper.get(area).get(),
                assetManager.get(atlasPath, TextureAtlas.class))))
        .add(new VisibilityComponent());
  }

  @Override
  protected void process(Entity e) {
    if (!ready) {
      consumeArea();

      ready = true;
    }
  }
}
