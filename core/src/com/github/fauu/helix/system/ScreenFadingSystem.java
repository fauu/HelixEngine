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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.fauu.helix.graphics.HelixCamera;

public class ScreenFadingSystem extends VoidEntitySystem {

  private ShapeRenderer shapeRenderer;

  private FadeType fadeType;

  private float fadeDuration;

  private float fadeLevel;

  @Wire
  private HelixCamera camera;

  @Override
  protected void initialize() {
    this.shapeRenderer = new ShapeRenderer();
  }

  @Override
  protected void processSystem() {
    if (fadeLevel >= fadeDuration) {
      fadeLevel = fadeDuration;
    } else if (fadeLevel <= 0) {
      fadeLevel = 0;
    }

    Gdx.gl.glEnable(GL20.GL_BLEND);

    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      shapeRenderer.setColor(0, 0, 0, fadeLevel / fadeDuration);
      shapeRenderer.rect(0, 0,
          Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    shapeRenderer.end();

    Gdx.gl.glDisable(GL20.GL_BLEND);

    switch (fadeType) {
      case FADE_OUT:
          fadeLevel += Gdx.graphics.getDeltaTime();
        break;
      case FADE_IN:
          fadeLevel -= Gdx.graphics.getDeltaTime();
        break;
      default: throw new IllegalStateException();
    }
  }

  @Override
  protected boolean checkProcessing() {
    return fadeLevel != 0;
  }

  public void fade(FadeType type, float duration) {
    fadeType = type;
    fadeDuration = duration;

    switch (fadeType) {
      case FADE_OUT:
        fadeLevel = Gdx.graphics.getDeltaTime();
        break;
      case FADE_IN:
        fadeLevel = fadeDuration - Gdx.graphics.getDeltaTime();
        break;
      default: throw new IllegalStateException();
    }
  }

  public enum FadeType {
    FADE_IN, FADE_OUT;
  }

}
