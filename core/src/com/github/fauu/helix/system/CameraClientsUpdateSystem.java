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

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.graphics.ParticleEffect;
import com.github.fauu.helix.manager.LocalAmbienceManager;

public class CameraClientsUpdateSystem extends VoidEntitySystem {

  @Wire
  private LocalAmbienceManager localAmbienceManager;

  @Wire
  private HelixCamera camera;

  public CameraClientsUpdateSystem() { }

  @Override
  protected void processSystem() {
    Vector3 positionDelta = camera.getTargetPositionDelta();

    ParticleEffect localAmbienceParticleEffect
        = localAmbienceManager.getAmbience().getParticleEffect();
    if (localAmbienceParticleEffect != null) {
      localAmbienceParticleEffect.update(-40 * positionDelta.x,
                                         -24 * positionDelta.y,
                                         Gdx.graphics.getDeltaTime());
    }

    camera.resetPositionDelta();
  }

}
