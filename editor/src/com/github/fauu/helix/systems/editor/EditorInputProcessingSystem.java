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

import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.github.fauu.helix.components.TileDataComponent;
import com.github.fauu.helix.events.MouseMovedEvent;

public class EditorInputProcessingSystem extends EntityProcessingSystem {

  public EditorInputProcessingSystem() {
    super(Filter.allComponents(TileDataComponent.class));
  }

  @Override
  protected void process(final Entity e) {
    if (Gdx.input.getDeltaX() != 0 && Gdx.input.getDeltaY() != 0) {
      final MouseMovedEvent event = world.createEvent(MouseMovedEvent.class);
      event.bouncer = e;
      event.mouseCoords = new Vector2(Gdx.input.getX(), Gdx.input.getY());

      world.postEvent(this, event);
    }
  }
}
