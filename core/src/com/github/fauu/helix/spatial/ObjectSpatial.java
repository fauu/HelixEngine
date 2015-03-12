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

package com.github.fauu.helix.spatial;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.Direction;
import com.github.fauu.helix.shader.ShaderAttribute;

public class ObjectSpatial extends Spatial implements RenderableProvider {
  
  private ModelInstance modelInstance;
  
  public ObjectSpatial(Model model) {
    modelInstance = new ModelInstance(model);
    
    TextureAttribute textureAttribute
        = (TextureAttribute) modelInstance.materials
                                          .first()
                                          .get(TextureAttribute.Diffuse);

    textureAttribute.textureDescription.magFilter 
        = Texture.TextureFilter.Nearest;
    textureAttribute.textureDescription.minFilter 
        = Texture.TextureFilter.Nearest;

    modelInstance.materials.first().set(textureAttribute);
    
    modelInstance.materials.first()
        .set(ColorAttribute.createDiffuse(Color.WHITE));
    
    modelInstance.materials.first()
        .set(new BlendingAttribute(GL20.GL_SRC_ALPHA, 
                                   GL20.GL_ONE_MINUS_SRC_ALPHA));
    
    modelInstance.materials.first().set(new ShaderAttribute(2));
    
    ready = true;
  }
  
  @Override
  public void update(Spatial.UpdateType type, Object value) {
    switch (type) {
      case POSITION:
        // TODO: Implement me
        break;
      case ORIENTATION:
        modelInstance.transform.translate(0.5f, 0.5f, 0)
                               .rotate(0, 0, 1, ((Direction) value).getAngle())
                               .translate(-0.5f, -0.5f, 0);
        break;
      default: throw new UnsupportedOperationException();
    }
  }
  
  @Override
  public void getRenderables(Array<Renderable> renderables, 
                             Pool<Renderable> pool) {
    modelInstance.getRenderables(renderables, pool);
  }

}
