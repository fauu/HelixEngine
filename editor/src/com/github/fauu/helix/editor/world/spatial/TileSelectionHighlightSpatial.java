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

package com.github.fauu.helix.editor.world.spatial;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.shader.ShaderAttribute;
import com.github.fauu.helix.spatial.Spatial;
import com.github.fauu.helix.util.MeshUtil;

// TODO: Somehow merge this with TerrainSpatial (shared superclass?)
public class TileSelectionHighlightSpatial extends Spatial 
                                           implements RenderableProvider {

  private static final Color COLOR = new Color(0.0f, 0.0f, 1.0f, 0.1f);
  
  private static final float Z_OFFSET = 0.06f;

  private GeometryManager geometryManager;

  private Mesh mesh;
  
  private int meshSize;
  
  private Renderable renderable;
  
  public TileSelectionHighlightSpatial() {
    super();
  }
  
  public void initialize(Array<Tile> tiles) {
    refresh(tiles);
  }

  public void refresh(Array<Tile> tiles) {
    ready = false;
    
    constructMesh(tiles);
    
    if (renderable == null) {
      renderable = createRenderable(mesh, meshSize);
    } else {
      renderable.mesh = mesh;
      renderable.meshPartSize = meshSize;
    }
    
    ready = true;
  }
  
  private void constructMesh(Array<Tile> tiles) {
    LinkedList<Mesh> geometries = new LinkedList<Mesh>();
    LinkedList<Matrix4> transformations = new LinkedList<Matrix4>();
    
    meshSize = 0;

    for (Tile tile : tiles) {
      Mesh geometry = geometryManager.getGeometry(tile.getGeometryName())
                                     .getMesh()
                                     .copy(true);
      geometries.add(geometry);

      meshSize += geometry.getNumIndices();

      Matrix4 transformation
          = new Matrix4().translate(tile.getPosition())
                         .translate(0.5f, 0.5f, 0)
                         .rotate(0, 0, 1, tile.getOrientation().getAngle())
                         .translate(-0.5f, -0.5f, Z_OFFSET);
      transformations.add(transformation);
    }

    mesh = MeshUtil.mergeMeshes(geometries, transformations);
  }

  public Renderable createRenderable(Mesh mesh, 
                                     int meshSize) {
    Renderable renderable = new Renderable();
    
    renderable.mesh = mesh;
    renderable.material
        = new Material(new ColorAttribute(ColorAttribute.Diffuse, COLOR),
                       new BlendingAttribute(GL20.GL_SRC_ALPHA, 
                                             GL20.GL_ONE_MINUS_SRC_ALPHA),
                       new ShaderAttribute(3));
    renderable.meshPartOffset = 0;
    renderable.meshPartSize = meshSize;
    renderable.primitiveType = GL20.GL_TRIANGLES;
    
    return renderable;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void update(UpdateType type, Object value) {
    switch (type) {
      case TILE_DATA:
        refresh((Array<Tile>) value);
        break;
      default: throw new UnsupportedOperationException();
    }
  }

  @Override
  public void getRenderables(Array<Renderable> renderables,
                             Pool<Renderable> pool) {
    renderables.add(renderable);
  }

  public void setGeometryManager(GeometryManager manager) {
    this.geometryManager = manager;
  }
  

}
