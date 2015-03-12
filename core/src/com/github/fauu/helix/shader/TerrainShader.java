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

package com.github.fauu.helix.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class TerrainShader extends GeneralShader {

  private int u_texture = register(new Uniform("u_texture"));
  
  public TerrainShader() {
    super();
    
    String vertex 
      = Gdx.files.internal("shader/terrain.vertex.glsl").readString();
    String fragment 
      = Gdx.files.internal("shader/terrain.fragment.glsl").readString();
    
    createProgram(vertex, fragment);
  }
  
  @Override
  public boolean canRender(Renderable renderable) {
    if (renderable.material.has(ShaderAttribute.ID)) {
      if (((ShaderAttribute) renderable.material.get(ShaderAttribute.ID))
              .getValue() == 1) {
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public void render(Renderable renderable) {
    TextureAttribute diffuseTexture
        = (TextureAttribute) renderable.material.get(TextureAttribute.Diffuse);
    
    set(u_texture, 
        context.textureBinder.bind(diffuseTexture.textureDescription));
    
    super.render(renderable);
  }

}
