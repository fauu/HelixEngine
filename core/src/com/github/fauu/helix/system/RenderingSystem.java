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

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.managers.UuidEntityManager;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.github.fauu.helix.component.SpatialFormComponent;
import com.github.fauu.helix.component.VisibilityComponent;
import com.github.fauu.helix.postprocessing.Bloom;
import com.github.fauu.helix.spatial.DecalSpatial;
import com.github.fauu.helix.spatial.ModelSpatial;
import com.github.fauu.helix.spatial.Spatial;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RenderingSystem extends EntitySystem {
  
  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;
  
  @Wire
  private PerspectiveCamera camera;
  
  private Map<UUID, ModelSpatial> modelSpatials;

  private Map<UUID, DecalSpatial> decalSpatials;
  
  private UuidEntityManager uuidEntityManager;
  
  private RenderContext renderContext;
  
  private ModelBatch modelBatch;

  private DecalBatch decalBatch;
  
  private ModelInstance axes;
  
  private Bloom bloom;

  private static final boolean bloomEnabled = true;

  @SuppressWarnings("unchecked")
  public RenderingSystem() {
    super(Aspect.getAspectForAll(SpatialFormComponent.class,
                                 VisibilityComponent.class));
  }
  
  @Override
  protected void initialize() {
    uuidEntityManager = world.getManager(UuidEntityManager.class);

    modelSpatials = new HashMap<UUID, ModelSpatial>();

    decalSpatials = new HashMap<UUID, DecalSpatial>();

    renderContext = new RenderContext(
        new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED));

    modelBatch = new ModelBatch(renderContext, new DefaultShaderProvider());

    decalBatch = new DecalBatch(new CameraGroupStrategy(camera));

    bloom = new Bloom();
    bloom.setBloomIntesity(0.6f);
    bloom.setOriginalIntesity(1);

    axes = buildAxes();
  };

  @Override
  protected void processEntities(IntBag entities) {
    camera.update();
    
    GL20 gl = Gdx.graphics.getGL20();

    gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    gl.glClearColor(0.53f, 0.8f, 0.92f, 1);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    gl.glEnable(GL20.GL_BLEND);
    gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    if (bloomEnabled) {
      bloom.capture();
    }

    renderContext.begin();
    modelBatch.begin(camera);

    modelBatch.render(axes);

    for (ModelSpatial spatial : modelSpatials.values()) {
      modelBatch.render(spatial);
    }

    modelBatch.end();
    renderContext.end();

    for (DecalSpatial spatial : decalSpatials.values()) {
      decalBatch.add(spatial.getDecal());
    }

    decalBatch.flush();

    if (bloomEnabled) {
      bloom.render();
    }
  }
  
  @Override
  protected void inserted(Entity e) {
    Spatial spatial = spatialFormMapper.get(e).get();

    if (spatial instanceof ModelSpatial) {
      modelSpatials.put(uuidEntityManager.getUuid(e), (ModelSpatial) spatial);
    } else if (spatial instanceof DecalSpatial) {
      decalSpatials.put(uuidEntityManager.getUuid(e), (DecalSpatial) spatial);
    }
  }
  
  @Override
  protected void removed(Entity e) {
    UUID uuid = uuidEntityManager.getUuid(e);

    if (modelSpatials.containsKey(uuid)) {
      modelSpatials.remove(uuid);
    } else {
      decalSpatials.remove(uuid);
    }
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
