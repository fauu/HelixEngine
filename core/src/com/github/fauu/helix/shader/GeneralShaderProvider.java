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

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;

public class GeneralShaderProvider extends BaseShaderProvider {
  
  @Override
  protected Shader createShader(Renderable renderable) {
    // Meh
    if (renderable.material.has(ShaderAttribute.ID)) {
      ShaderAttribute shaderAttribute = 
          (ShaderAttribute) renderable.material.get(ShaderAttribute.ID);

      if (shaderAttribute.getValue() == 1) {
        return new TerrainShader();
      } else if (shaderAttribute.getValue() == 2) {
        return new ObjectShader();
      } else if (shaderAttribute.getValue() == 3) {
        return new SimpleShader();
      }
    }
    
    return new DefaultShader(renderable);
  }

}
