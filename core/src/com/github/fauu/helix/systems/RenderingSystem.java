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

package com.github.fauu.helix.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Filter;
import com.artemis.systems.EntitySystem;
import com.artemis.utils.SafeArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.*;
import com.badlogic.gdx.utils.Array;
import com.github.fauu.helix.datums.TileDatum;
import com.github.fauu.helix.components.PositionComponent;
import com.github.fauu.helix.components.SpatialFormComponent;
import com.github.fauu.helix.components.TileDataComponent;
import com.github.fauu.helix.spatials.Spatial;
import com.github.fauu.helix.spatials.TerrainSpatial;

public class RenderingSystem extends EntitySystem {

  ComponentMapper<PositionComponent> positionMapper;
  ComponentMapper<SpatialFormComponent> spatialFormMapper;
  ComponentMapper<TileDataComponent> tileDataMapper;

  PerspectiveCamera camera;
  SafeArray<Spatial> spatials;
  ModelBatch modelBatch;
  RenderContext renderContext;

  ModelInstance axes;

  public RenderingSystem(final PerspectiveCamera camera) {
    super(Filter.allComponents(SpatialFormComponent.class));

    this.camera = camera;
    spatials = new SafeArray<Spatial>();

    renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));

    modelBatch = new ModelBatch(renderContext, new DefaultShaderProvider());

    buildAxes();
  }

  @Override
  public void initialize() {
    positionMapper = world.getMapper(PositionComponent.class);
    spatialFormMapper = world.getMapper(SpatialFormComponent.class);
    tileDataMapper = world.getMapper(TileDataComponent.class);
  }

  @Override
  protected void processEntities(final Array<Entity> entities) {
    camera.update();

    final GL20 gl = Gdx.graphics.getGL20();

    gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
    gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    gl.glEnable(GL20.GL_BLEND);
    gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    renderContext.begin();
    modelBatch.begin(camera);

    modelBatch.render(axes);

    for (final Entity e : entities) {
      final Spatial spatial = spatials.get(e.id);

      if (spatial.isReady()) {
        modelBatch.render(spatial);
      }
    }

    modelBatch.end();
    renderContext.end();
  }

  @Override
  protected void inserted(final Entity e) {
    final Spatial spatial = spatialFormMapper.get(e).getSpatial();

    spatials.set(e.id, spatial);
  }

  private void buildAxes() {
    final ModelBuilder builder = new ModelBuilder();
    builder.begin();
    final MeshPartBuilder partBuilder = builder.part("axes", GL20.GL_LINES,
        VertexAttributes.Usage.Position | VertexAttributes.Usage.Color, new Material());
    partBuilder.setColor(Color.RED);
    partBuilder.line(0, 0, 0, 100, 0, 0);
    partBuilder.setColor(Color.GREEN);
    partBuilder.line(0, 0, 0, 0, 100, 0);
    partBuilder.setColor(Color.BLUE);
    partBuilder.line(0, 0, 0, 0, 0, 100);
    final Model axesModel = builder.end();

    axes = new ModelInstance(axesModel);
  }

}
