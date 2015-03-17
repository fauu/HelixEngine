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
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.component.DimensionsComponent;
import com.github.fauu.helix.component.TilesComponent;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.util.IntVector2;
import com.github.fauu.helix.util.TileUtil;

public class TileHighlightingSystem extends EntityProcessingSystem {

  @Wire
  private ComponentMapper<TilesComponent> tilesMapper;

  @Wire
  private ComponentMapper<DimensionsComponent> dimensionsMapper;

  @Wire
  private PerspectiveCamera camera;

  private Tile highlightedTile;

  @SuppressWarnings("unchecked")
  public TileHighlightingSystem() {
    super(Aspect.getAspectForAll(TilesComponent.class,
                                 DimensionsComponent.class));
  }

  @Override
  protected void initialize() { }

  @Override
  protected void process(Entity e) {
    if (!mouseMoved()) {
      return;
    }

    Array<Tile> tiles = tilesMapper.get(e).get();
    IntVector2 dimensions = dimensionsMapper.get(e).get();

    Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

    boolean hovering = false;

    int i = 0;
    for (Tile tile : tiles) {
      BoundingBox boundingBox = new BoundingBox(new Vector3(0, 0, 0),
                                                new Vector3(1, 1, 0));

      IntVector2 position = TileUtil.calculatePosition(i, dimensions);

      Matrix4 transformation = new Matrix4();
      transformation.translate(new Vector3(position.x, position.y, 0));

      boundingBox.mul(transformation);

      if (Intersector.intersectRayBoundsFast(ray, boundingBox)) {
        if (tile != highlightedTile) {
          ;
        }

        hovering = true;

        highlightedTile = tile;

        break;
      }

      i++;
    }

    if (!hovering) {
      highlightedTile = null;
    }
  }

  private boolean mouseMoved() {
    return Gdx.input.getDeltaX() == 0 && Gdx.input.getDeltaY() == 0;
  }

  public Tile getHighlightedTile() {
    return highlightedTile;
  }

}
