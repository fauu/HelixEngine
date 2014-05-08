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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class TerrainShader extends GeneralShader {

  private final int u_texture = register(new Uniform("u_texture"));

  public TerrainShader() {
    super();

    final String vertex = Gdx.files.internal("shaders/terrain.vertex.glsl").readString();
    final String fragment = Gdx.files.internal("shaders/terrain.fragment.glsl").readString();

    createProgram(vertex, fragment);
  }

  @Override
  public boolean canRender(final Renderable instance) {
    return !instance.material.has(ColorAttribute.Diffuse);
  }

  @Override
  public void begin(final Camera camera, final RenderContext context) {
    super.begin(camera, context);
  }

  @Override
  public void render(final Renderable renderable) {
    final TextureAttribute diffuseTexture
        = (TextureAttribute) renderable.material.get(TextureAttribute.Diffuse);

    set(u_texture, context.textureBinder.bind(diffuseTexture.textureDescription));

    super.render(renderable);
  }

}
