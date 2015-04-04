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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.component.*;
import com.github.fauu.helix.graphics.HelixCamera;
import com.github.fauu.helix.graphics.HelixRenderableSorter;
import com.github.fauu.helix.graphics.ParticleEffect;
import com.github.fauu.helix.manager.WeatherManager;
import com.github.fauu.helix.postprocessing.Bloom;
import com.github.fauu.helix.spatial.DecalSpatial;
import com.github.fauu.helix.spatial.ModelSpatial;
import com.github.fauu.helix.spatial.Spatial;

import java.util.Iterator;

public class RenderingSystem extends EntitySystem {

  @Wire
  private WeatherManager weatherManager;

  @Wire
  private ComponentMapper<BloomComponent> bloomMapper;

  @Wire
  private ComponentMapper<EnvironmentComponent> environmentMapper;

  @Wire
  private ComponentMapper<ParticleEffectComponent> particleEffectMapper;
  
  @Wire
  private ComponentMapper<SpatialFormComponent> spatialFormMapper;
  
  @Wire
  private HelixCamera camera;
  
  private Array<ModelSpatial> modelSpatials;

  private Array<DecalSpatial> decalSpatials;

  private Array<Array<? extends Spatial>> spatialCollections;
  
  private UuidEntityManager uuidEntityManager;
  
  private RenderContext renderContext;

  private ModelBatch modelBatch;

  private DecalBatch decalBatch;

  private SpriteBatch spriteBatch;

  @SuppressWarnings("unchecked")
  public RenderingSystem() {
    super(Aspect.getAspectForAll(SpatialFormComponent.class,
                                 VisibilityComponent.class));
  }
  
  @Override
  protected void initialize() {
    uuidEntityManager = world.getManager(UuidEntityManager.class);

    spatialCollections = new Array<Array<? extends Spatial>>();

    modelSpatials = new Array<ModelSpatial>();
    spatialCollections.add(modelSpatials);

    decalSpatials = new Array<DecalSpatial>();
    spatialCollections.add(decalSpatials);

    renderContext = new RenderContext(
        new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED));
    renderContext.setCullFace(GL20.GL_BACK);

    modelBatch = new ModelBatch(renderContext,
                                new DefaultShaderProvider(),
                                new HelixRenderableSorter());

    decalBatch = new DecalBatch(new CameraGroupStrategy(camera));

    spriteBatch = new SpriteBatch();
  }

  @Override
  protected void processEntities(IntBag entities) {
    camera.update();

    Environment environment = null;
    Bloom bloom = null;
    ParticleEffect particleEffect = null;

    Entity weather = weatherManager.getWeather();

    if (weather != null) {
      environment = environmentMapper.get(weather).get();

      if (bloomMapper.has(weather)) {
        bloom = bloomMapper.get(weather).get();
      }

      if (particleEffectMapper.has(weather)) {
        particleEffect = particleEffectMapper.get(weather).get();
      }
    } else {
      environment = new Environment();
      environment.set(
          new ColorAttribute(ColorAttribute.AmbientLight, 1, 1, 1, 1));
    }

    GL20 gl = Gdx.graphics.getGL20();

    gl.glClearColor(0, 0, 0, 1);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    if (bloom != null) {
      bloom.capture();
    }

    renderContext.begin();
    modelBatch.begin(camera);

    modelBatch.render(modelSpatials, environment);

    modelBatch.end();
    renderContext.end();

    for (DecalSpatial spatial : decalSpatials) {
      for (Decal d : spatial.getDecals()) {
        decalBatch.add(d);
      }
    }

    decalBatch.flush();

    spriteBatch.begin();
    if (particleEffect != null) {
      particleEffect.draw(spriteBatch, Gdx.graphics.getDeltaTime());
    }
    spriteBatch.end();

    if (bloom != null) {
      bloom.render();
    }

  }
  
  @Override
  protected void inserted(Entity e) {
    Spatial spatial = spatialFormMapper.get(e).get();

    if (spatial instanceof ModelSpatial) {
      modelSpatials.add((ModelSpatial) spatial);
    } else if (spatial instanceof DecalSpatial) {
      decalSpatials.add((DecalSpatial) spatial);
    }
  }
  
  @Override
  protected void removed(Entity e) {
    Spatial spatialToRemove = spatialFormMapper.get(e).get();

    for (Array<? extends Spatial> spatialCollection : spatialCollections) {
      Iterator<? extends Spatial> it = spatialCollection.iterator();

      while (it.hasNext()) {
        if (it.next().equals(spatialToRemove)) {
          it.remove();
        }
      }
    }
  }

}
