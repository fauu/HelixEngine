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

import com.artemis.Entity;
import com.artemis.Manager;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.spatial.CharacterSpatial;
import com.github.fauu.helix.util.IntVector3;

public class PlayerManager extends Manager {

  private Entity player;

  @Override
  protected void initialize() {
    IntVector3 playerPosition = new IntVector3(16, 16, 0);
    Direction playerOrientation = Direction.SOUTH;
    float playerMovementSpeed = 4.5f;

    player = world.createEntity()
                  .edit()
                  .add(new OrientationComponent(playerOrientation))
                  .add(new MovementSpeedComponent(playerMovementSpeed))
                  .add(new PositionComponent(playerPosition))
                  .add(new SpatialFormComponent(
                      new CharacterSpatial(playerPosition, "player")))
                  .add(new VisibilityComponent())
                  .getEntity();
  }

  public Entity getPlayer() {
    return player;
  }

}
