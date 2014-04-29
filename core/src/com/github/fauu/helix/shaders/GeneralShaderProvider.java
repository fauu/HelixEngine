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

package com.github.fauu.helix.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;

public class GeneralShaderProvider extends BaseShaderProvider {

  @Override
  protected Shader createShader(Renderable renderable) {
    // This is kind of dirty
    if (!renderable.material.has(TextureAttribute.Diffuse)) {
      return new DefaultShader(renderable);
    } else if (!renderable.material.has(BlendingAttribute.Type)) {
      return new TerrainShader();
    } else {
      return new ObjectShader();
    }
  }

}
