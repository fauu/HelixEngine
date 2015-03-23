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

package com.github.fauu.helix.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.github.fauu.helix.component.ShadowComponent;
import com.github.fauu.helix.component.ShadowIntensityComponent;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.manager.WeatherManager;
import com.github.fauu.helix.spatial.DecalSpatial;

public class ShadowIntensityUpdateSystem extends EntityProcessingSystem {

  @Wire
  private WeatherManager weatherMan;

  @Wire
  private ComponentMapper<ShadowIntensityComponent> shadowIntensityMapper;

  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;

  private float currentShadowIntensity;

  public ShadowIntensityUpdateSystem() {
    super(Aspect.getAspectForAll(ShadowComponent.class));
  }

  @Override
  protected void process(Entity e) {
    float shadowIntensity = shadowIntensityMapper.get(weatherMan.getWeather())
                                                 .get();
    if (shadowIntensity != currentShadowIntensity) {
      ((DecalSpatial) spatialFormMapper.get(e).get())
          .getShadowDecal()
          .setColor(1, 1, 1, shadowIntensity);

      currentShadowIntensity = shadowIntensity;
    }
  }

}
