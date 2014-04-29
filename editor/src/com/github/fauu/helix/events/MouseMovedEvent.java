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

package com.github.fauu.helix.events;

import com.artemis.Entity;
import com.artemis.systems.event.SystemEvent;
import com.badlogic.gdx.math.Vector2;

public class MouseMovedEvent extends SystemEvent {

  public Entity bouncer;
  public Vector2 mouseCoords;

  @Override
  protected void resetForPooling() {
    bouncer = null;
    mouseCoords = null;
  }

}
