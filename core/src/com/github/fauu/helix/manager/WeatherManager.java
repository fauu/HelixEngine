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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.github.fauu.helix.component.BloomComponent;
import com.github.fauu.helix.component.EnvironmentComponent;
import com.github.fauu.helix.component.ParticleEffectComponent;
import com.github.fauu.helix.graphics.ParticleEffect;
import com.github.fauu.helix.postprocessing.Bloom;

// TODO: Think of a more elegant way to handle this
public class WeatherManager extends Manager {

  private Entity weather;

  public void setType(WeatherType type) {
    Environment environment = new Environment();
    Bloom bloom = null;
    ParticleEffect precipitationEffect = null;

    switch (type) {
      case NONE:
        environment.set(
            new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1));
        break;
      case SUNNY:
        bloom = new Bloom();
        bloom.setBloomIntesity(1.1f);
        bloom.setOriginalIntesity(1);

        environment.set(
            new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1));
        break;
      case OVERCAST:
        bloom = new Bloom();
        bloom.setBloomIntesity(.6f);
        bloom.setOriginalIntesity(.8f);

        environment.set(
            new ColorAttribute(ColorAttribute.AmbientLight, .8f, .8f, .8f, 1));
        break;
      case RAINSTORM:
        bloom = new Bloom();
        bloom.setBloomIntesity(.2f);
        bloom.setOriginalIntesity(.2f);

        environment.set(
            new ColorAttribute(ColorAttribute.AmbientLight, .5f, .5f, .6f, 1));
        environment.set(
            new ColorAttribute(ColorAttribute.Fog, 1, 1, 1, 1));

        precipitationEffect = new ParticleEffect();
        precipitationEffect.load(
            Gdx.files.internal("effect/rain.p"), Gdx.files.internal("effect"));
        precipitationEffect.setPosition(Gdx.graphics.getWidth() / 2,
                                        Gdx.graphics.getHeight() / 2);
        precipitationEffect.start();
        break;
      default: throw new IllegalStateException();
    }

    if (weather == null) {
      weather = world.createEntity()
                     .edit()
                     .add(new EnvironmentComponent(environment))
                     .getEntity();
    }

    if (bloom != null) {
      weather.edit().add(new BloomComponent(bloom));
    }

    if (precipitationEffect != null) {
      weather.edit().add(new ParticleEffectComponent(precipitationEffect));
    }
  }

  public Entity getWeather() {
    return weather;
  }

  public enum WeatherType {
    NONE, SUNNY, OVERCAST, RAINSTORM;
  }

}
