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

package com.github.fauu.helix.displayable;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;

public abstract class ModelDisplayable extends Displayable
                                       implements RenderableProvider {

  protected ModelInstance instance;

  protected AnimationController animationController;

  public AnimationController getAnimationController() {
    return animationController;
  }

  public boolean animateIfAnimationExists(String id) {
    for (Animation animation : instance.animations) {
      if (animation.id.equalsIgnoreCase(id)) {
        animationController.setAnimation(id);

        return true;
      }
    }

    return false;
  }

  public void updateTranslation(Vector3 translation) {
    instance.transform.setToTranslation(translation);
  }

  public void updateOpacity(float opacity) {
    for (Material material : instance.materials) {
      BlendingAttribute ba
          = (BlendingAttribute) material.get(BlendingAttribute.Type);

      ba.opacity = opacity;
    }
  }

}
