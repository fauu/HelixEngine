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
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;

public class SimpleShader extends GeneralShader {

  private int u_color = register(new Uniform("u_color"));
  
  public SimpleShader() {
    super();
    
    String vertex 
        = Gdx.files.internal("shader/simple.vertex.glsl").readString();
    String fragment 
        = Gdx.files.internal("shader/simple.fragment.glsl").readString();
    
    createProgram(vertex, fragment);
  }
  
  @Override
  public boolean canRender(Renderable renderable) {
    return true;
  }
  
  @Override
  public void begin(Camera camera, RenderContext context) {
    super.begin(camera, context);
    
    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    context.setCullFace(GL20.GL_BACK);
    context.setDepthTest(GL20.GL_LEQUAL, 0, 1);
    context.setDepthMask(true);
  }
  
  @Override
  public void render(Renderable renderable) {
    ColorAttribute diffuseColor
      = (ColorAttribute) renderable.material.get(ColorAttribute.Diffuse);
    
    set(u_color, diffuseColor.color);

    super.render(renderable);
  }

}
