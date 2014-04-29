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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class ObjectShader extends GeneralShader {

  final int u_texture = register(new Uniform("u_texture"));
  final int u_color = register(new Uniform("u_color"));

  public ObjectShader() {
    super();

    final String vertex = Gdx.files.internal("shaders/object.vertex.glsl").readString();
    final String fragment = Gdx.files.internal("shaders/object.fragment.glsl").readString();

    createProgram(vertex, fragment);
  }

  @Override
  public boolean canRender(final Renderable instance) {
    return instance.material.has(TextureAttribute.Diffuse);
  }

  @Override
  public void begin(final Camera camera, final RenderContext context) {
    super.begin(camera, context);

    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    context.setCullFace(GL20.GL_BACK);
    context.setDepthTest(GL20.GL_LEQUAL, 0, 1);
    context.setDepthMask(true);
  }

  @Override
  public void render(final Renderable renderable) {
    final TextureAttribute diffuseTexture
        = (TextureAttribute) renderable.material.get(TextureAttribute.Diffuse);
    final ColorAttribute diffuseColor
        = (ColorAttribute) renderable.material.get(ColorAttribute.Diffuse);

    set(u_texture, context.textureBinder.bind(diffuseTexture.textureDescription));
    set(u_color, diffuseColor.color);

    super.render(renderable);
  }

}
