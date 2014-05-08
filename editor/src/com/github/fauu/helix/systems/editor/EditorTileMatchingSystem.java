/*
 * Copyright (C) 2014 Helix Engine Developers (http://github.com/fauu/HelixEngine)
 *
 * This software is licensed under the GNU General Public License
 * (version 3 or later). See the COPYING file in this distribution.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this software. If not, see <http://www.gnu.org/licenses/>.
 *
 * Authored by: Piotr Grabowski <fau999@gmail.com>
 */

package com.github.fauu.helix.systems.editor;

import com.artemis.ComponentMapper;
import com.artemis.systems.event.EventVoidSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.components.TileDataComponent;
import com.github.fauu.helix.datums.TileDatum;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.events.MouseMovedEvent;

public class EditorTileMatchingSystem extends EventVoidSystem<MouseMovedEvent> {

  private ComponentMapper<TileDataComponent> tileDataMapper;
  private Array<MouseMovedEvent> events;
  private HelixEditor editor;
  private Camera camera;
  private TileDatum matchedTileDatum;

  public EditorTileMatchingSystem(final HelixEditor editor, final Camera camera) {
    super(MouseMovedEvent.class);

    events = new Array<MouseMovedEvent>();
    this.camera = camera;
    this.editor = editor;
  }

  @Override
  public void initialize() {
    tileDataMapper = world.getMapper(TileDataComponent.class);
  }

  @Override
  protected void processEvent(final MouseMovedEvent event) {
    final Ray ray = camera.getPickRay(event.mouseCoords.x, event.mouseCoords.y);
    final Array<TileDatum> tileData = tileDataMapper.get(event.bouncer).get();
    TileDatum newMatchedTileDatum = null;

    for (final TileDatum datum : tileData) {
      final Matrix4 transform = new Matrix4().translate(datum.getPosition());
      final BoundingBox boundingBox = datum.getGeometry().calculateBoundingBox().mul(transform);

      if (Intersector.intersectRayBoundsFast(ray, boundingBox)) {
        newMatchedTileDatum = datum;

        break;
      }
    }

    if (newMatchedTileDatum != matchedTileDatum) {
      editor.setMatchedTileInfo(newMatchedTileDatum);

      matchedTileDatum = newMatchedTileDatum;
    }
  }

}
