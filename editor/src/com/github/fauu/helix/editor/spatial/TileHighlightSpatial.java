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

package com.github.fauu.helix.editor.spatial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.spatial.Spatial;

public class TileHighlightSpatial extends Spatial {

  private static final float Z_OFFSET = -0.01f;

  private static final Color color = new Color(1, 1, 1, 0.75f);

  private ModelInstance instance;

  public TileHighlightSpatial() {
    ModelBuilder modelBuilder = new ModelBuilder();

    Model model
        = modelBuilder.createRect(0, 0, Z_OFFSET,
                                  1, 0, Z_OFFSET,
                                  1, 1, Z_OFFSET,
                                  0, 1, Z_OFFSET,
                                  0, 0, 1,
                                  GL20.GL_TRIANGLES,
                                  new Material(
                                      new ColorAttribute(
                                          ColorAttribute.createDiffuse(color)),
                                      new BlendingAttribute(
                                          GL20.GL_SRC_ALPHA,
                                          GL20.GL_ONE_MINUS_SRC_ALPHA)),
                                  VertexAttributes.Usage.Position |
                                  VertexAttributes.Usage.TextureCoordinates);

    instance = new ModelInstance(model);
  }

  @Override
  public void update(UpdateType type, Object value) {
    instance.transform.setToTranslation(((Vector3) value).cpy()
                                                         .add(0, 0, Z_OFFSET));
  }

  @Override
  public void getRenderables(Array<Renderable> renderables,
                             Pool<Renderable> pool) {
    instance.getRenderables(renderables, pool);
  }

}
