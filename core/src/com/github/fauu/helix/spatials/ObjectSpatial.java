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

package com.github.fauu.helix.spatials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import javax.swing.text.AttributeSet;

public class ObjectSpatial extends Spatial implements RenderableProvider {

  ModelInstance modelInstance;

  public ObjectSpatial(final Model model, final Matrix4 initialTransform) {
    modelInstance = new ModelInstance(model);
    modelInstance.transform.set(initialTransform);

    final TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.first()
        .get(TextureAttribute.Diffuse);
    textureAttribute.textureDescription.magFilter = Texture.TextureFilter.Nearest;
    textureAttribute.textureDescription.minFilter = Texture.TextureFilter.Nearest;
    modelInstance.materials.first().set(textureAttribute);

    modelInstance.materials.first().set(ColorAttribute.createDiffuse(Color.WHITE));
    modelInstance.materials.first().set(
        new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));

    ready = true;
  }

  @Override
  public void getRenderables(final Array<Renderable> renderables, final Pool<Renderable> pool) {
    modelInstance.getRenderables(renderables, pool);
  }

  public void setTransform(Matrix4 transform) {
    modelInstance.transform.set(transform);
  }

}
