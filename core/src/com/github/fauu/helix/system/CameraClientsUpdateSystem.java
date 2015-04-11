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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.component.ParticleEffectComponent;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.graphics.ParticleEffect;

public class CameraClientsUpdateSystem extends EntityProcessingSystem {

  @Wire
  private ComponentMapper<ParticleEffectComponent> particleEffectMapper;

  @Wire
  private HelixCamera camera;

  public CameraClientsUpdateSystem() {
    super(Aspect.getAspectForAll(ParticleEffectComponent.class));
  }

  @Override
  protected void process(Entity e) {
    Vector3 positionDelta = camera.getTargetPositionDelta();

    ParticleEffect effect = particleEffectMapper.get(e).get();
    effect.update(-40 * positionDelta.x,
                  -24 * positionDelta.y,
                  Gdx.graphics.getDeltaTime());

    camera.resetPositionDelta();
  }

}
