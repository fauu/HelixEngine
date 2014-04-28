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

package com.github.fauu.helix;

import com.artemis.Entity;
import com.artemis.World;
import com.github.fauu.helix.components.OrientationComponent;
import com.github.fauu.helix.components.PositionComponent;
import com.github.fauu.helix.components.SpatialFormComponent;
import com.github.fauu.helix.components.TileDataComponent;

public class EntityFactory {

  public static Entity createMapRegion(final World world) {
    final Entity mapRegion = world.createEntity();

    mapRegion.addComponent(world.createComponent(PositionComponent.class));

    return mapRegion;
  }

  public static Entity createTerrain(final World world) {
    final Entity terrain = world.createEntity();

    terrain.addComponent(world.createComponent(TileDataComponent.class));
    terrain.addComponent(world.createComponent(SpatialFormComponent.class));
//    terrain.addComponent(world.createComponent(BelongsToMapRegionComponent.class));

    return terrain;
  }

  public static Entity createObject(final World world) {
    final Entity object = world.createEntity();

    object.addComponent(world.createComponent(PositionComponent.class));
    object.addComponent(world.createComponent(SpatialFormComponent.class));
    object.addComponent(world.createComponent(OrientationComponent.class));
//    terrain.addComponent(world.createComponent(BelongsToMapRegionComponent.class));

    return object;
  }

}
