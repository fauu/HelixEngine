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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.editor.displayable.TileHighlightDisplayable;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.util.IntVector2;

public class TileHighlightingSystem extends EntityProcessingSystem {

  @Wire
  private TagManager tagManager;

  @Wire
  private ComponentMapper<DisplayableComponent> displayableMapper;

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire
  private HelixCamera camera;

  private Tile highlightedTile;

  @SuppressWarnings("unchecked")
  public TileHighlightingSystem() {
    super(Aspect.getAspectForAll(TilesComponent.class,
                                 DimensionsComponent.class));
  }

  @Override
  protected void initialize() {
    Entity highlight = world.createEntity()
                            .edit()
                            .add(new PositionComponent())
                            .add(new DimensionsComponent(new IntVector2(1, 1)))
                            .add(new DisplayableComponent(
                                new TileHighlightDisplayable()))
                            .getEntity();
    world.getManager(TagManager.class).register("tileHighlight", highlight);
  }

  @Override
  protected void process(Entity e) {
    if (!mouseMoved()) {
      return;
    }

    Entity highlight = tagManager.getEntity("tileHighlight");

    Tile[][] tiles = tilesMapper.get(e).get();

    Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

    boolean hovering = false;

    for (int y = 0; y < tiles.length; y++) {
      for (int x = 0; x < tiles[y].length; x++) {
        BoundingBox boundingBox = new BoundingBox(new Vector3(0, 0, 0),
                                                  new Vector3(1, 1, 0));

        Matrix4 transformation = new Matrix4();
        transformation.translate(new Vector3(x, y, 0));

        boundingBox.mul(transformation);

        if (Intersector.intersectRayBoundsFast(ray, boundingBox)) {
         if (tiles[y][x] != highlightedTile) {
           // FIXME
//            ModelDisplayable highlightDisplayable
//                = (ModelDisplayable) displayableMapper.get(highlight).get();
//            highlightDisplayable.updateTranslation(new Vector3(x, y, 0));

//            highlight.edit().create(VisibilityComponent.class);

            highlightedTile = tiles[y][x];
          }

          hovering = true;

          break;
        }
      }
    }

    if (!hovering) {
      highlightedTile = null;

      highlight.edit().remove(VisibilityComponent.class);
    }
  }

  private boolean mouseMoved() {
    return Gdx.input.getDeltaX() != 0 || Gdx.input.getDeltaY() != 0;
  }

  public Tile getHighlightedTile() {
    return highlightedTile;
  }

}
