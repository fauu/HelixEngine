/*
 * Copyright (C) 2014-2016 Helix Engine Developers
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

package com.github.fauu.helix.datum;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.github.fauu.helix.graphics.ParticleEffect;
import com.github.fauu.helix.graphics.postprocessing.Bloom;

public class Ambience {

  private Environment environment;

  private Bloom bloom;

  private ParticleEffect particleEffect;

  public Ambience() {
    setDefault();
  }

  public void setDefault() {
    environment = new Environment();
    environment.set(
        new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1));

    bloom = new Bloom();
    bloom.setBloomIntesity(.8f);
    bloom.setOriginalIntesity(1);

    particleEffect = null;
  }

  public Environment getEnvironment() {
    return environment;
  }

  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  public Bloom getBloom() {
    return bloom;
  }

  public void setBloom(Bloom bloom) {
    this.bloom = bloom;
  }

  public ParticleEffect getParticleEffect() {
    return particleEffect;
  }

  public void setParticleEffect(ParticleEffect particleEffect) {
    this.particleEffect = particleEffect;
  }

}
