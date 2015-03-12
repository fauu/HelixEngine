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

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GeneralShader extends BaseShader {
  
  protected final int u_projTrans = register(new Uniform("u_projTrans"));

  protected final int u_worldTrans = register(new Uniform("u_worldTrans"));
  
  public GeneralShader() {
    super();
  }
  
  @Override
  public void init() {
    super.init(program, null);
  }
  
  @Override
  public int compareTo(Shader other) {
    return 0;
  }
  
  @Override
  public boolean canRender(Renderable instance) {
    return true;
  }
  
  @Override
  public void begin(Camera camera, RenderContext context) {
    this.camera = camera;
    this.context = context;
    
    program.begin();

    set(u_projTrans, camera.combined);
  }
  
  @Override
  public void render(Renderable renderable) {
    set(u_worldTrans, renderable.worldTransform);
    
    renderable.mesh.render(program, 
                           renderable.primitiveType, 
                           renderable.meshPartOffset, 
                           renderable.meshPartSize, 
                           true);
  }
  
  @Override
  public void end() {
    program.end();
  }
  
  @Override
  public void dispose() {
    super.dispose();
    program.dispose();
  }
  
  public void createProgram(String vertex, String fragment) {
    program = new ShaderProgram(vertex, fragment);

    if (!program.isCompiled()) {
      throw new GdxRuntimeException("Could not compile shader " +
                                    program.getLog());
    }
  }

}
