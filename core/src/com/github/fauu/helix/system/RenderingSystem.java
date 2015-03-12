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

package com.github.fauu.helix.system;

import java.util.Iterator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.postprocessing.Bloom;
import com.github.fauu.helix.shader.GeneralShaderProvider;
import com.github.fauu.helix.spatial.Spatial;

public class RenderingSystem extends EntitySystem {
  
  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;
  
  @Wire
  private PerspectiveCamera camera;
  
  private Bag<Spatial> spatials = new Bag<Spatial>();
  
  private RenderContext renderContext;
  
  private ModelBatch modelBatch;
  
  private ModelInstance axes;
  
  private Bloom bloom;

  @SuppressWarnings("unchecked")
  public RenderingSystem() {
    super(Aspect.getAspectForAll(SpatialFormComponent.class, 
                                 VisibilityComponent.class));

    renderContext = new RenderContext(
        new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED));
    
    modelBatch = new ModelBatch(renderContext, new GeneralShaderProvider());
    
    axes = buildAxes();
    
    bloom = new Bloom();
  }

  @Override
  protected void processEntities(IntBag entities) {
    camera.update();
    
    GL20 gl = Gdx.graphics.getGL20();
    
    //bloom.capture();
    
    gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    gl.glEnable(GL20.GL_BLEND);
    gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    
    renderContext.begin();
    modelBatch.begin(camera);
    
    modelBatch.render(axes);
    
    for (Iterator<Spatial> it = spatials.iterator(); it.hasNext();) {
      Spatial spatial = it.next();

      if (spatial != null && spatial.isReady()) {
        modelBatch.render(spatial);
      }
    }
    
    modelBatch.end();
    renderContext.end();
    
    //bloom.render();
  }
  
  @Override
  protected void inserted(Entity e) { 
    spatials.set(e.getId(), spatialFormMapper.get(e).get());
  }
  
  @Override
  protected void removed(Entity e) {
    spatials.remove(spatialFormMapper.get(e).get());
  }
  
  private ModelInstance buildAxes() {
    ModelBuilder builder = new ModelBuilder();

    builder.begin();

    MeshPartBuilder partBuilder 
        = builder.part("axes", 
                       GL20.GL_LINES,
                       VertexAttributes.Usage.Position | 
                       VertexAttributes.Usage.ColorUnpacked, 
                       new Material());
    partBuilder.setColor(Color.RED);
    partBuilder.line(0, 0, 0, 100, 0, 0);
    partBuilder.setColor(Color.GREEN);
    partBuilder.line(0, 0, 0, 0, 100, 0);
    partBuilder.setColor(Color.BLUE);
    partBuilder.line(0, 0, 0, 0, 0, 100);

    return new ModelInstance(builder.end());
  }

}
