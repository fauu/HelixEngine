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

package com.github.fauu.helix.spatial;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.github.fauu.helix.datum.Tile;
import com.github.fauu.helix.manager.GeometryManager;
import com.github.fauu.helix.shader.ShaderAttribute;

// TODO: Refactor to use util.MeshUtil.mergeMeshes()
public class TerrainSpatial extends Spatial implements RenderableProvider {
  
  private static final int NUM_POSITION_COMPONENTS = 3;

  private static final int NUM_TEXTURE_COMPONENTS = 2;
  
  private static final int NUM_COMPONENTS
      = NUM_POSITION_COMPONENTS + NUM_TEXTURE_COMPONENTS;
  
  // TODO: refine this
  private static final int NUM_MAX_VERTICES_PER_TILE = 32 * NUM_COMPONENTS;
  
  private GeometryManager geometryManager;
  
  private Mesh mesh;
  
  private int meshSize;
  
  private Renderable renderable;
  
  private TextureAtlas textureSet;
  
  public TerrainSpatial(TextureAtlas textureSet) {
    super();

    this.textureSet = textureSet;
  }
  
  public void initialize(Array<Tile> tiles) {
    update(tiles);
    
    ready = true;
  }
  
  public void update(Array<Tile> tiles) {
    constructMesh(tiles);

    if (renderable == null) {
      renderable = createRenderable(mesh, meshSize, textureSet);
    } else {
      renderable.mesh = mesh;
      renderable.meshPartSize = meshSize;
    }
  }
  
  private void constructMesh(Array<Tile> tiles) {
    meshSize = NUM_MAX_VERTICES_PER_TILE * tiles.size;
    
    mesh = new Mesh(true, 
                    meshSize, 
                    0,
                    new VertexAttribute(Usage.Position, 
                                        NUM_POSITION_COMPONENTS, 
                                        "a_position"),
                    new VertexAttribute(Usage.TextureCoordinates, 
                                        NUM_TEXTURE_COMPONENTS, 
                                        "a_texCoord0"));
    
    float[] vertices = new float[meshSize];
    
    int vertexCount = 0;
    for (Tile tile : tiles) {
      Texture texture = textureSet.getTextures().first();

      /* TODO: cache the result! */
      TextureRegion textureRegion 
          = textureSet.findRegion(tile.getTextureName());
      
      Vector2 textureCoords 
          = new Vector2(textureRegion.getU(), textureRegion.getV());
      Vector2 textureScaling 
          = new Vector2((float) textureRegion.getRegionWidth() /
                                texture.getWidth(),
                        (float) textureRegion.getRegionHeight() /
                                texture.getHeight());
      
      Matrix3 transformationMatrixUV
          = new Matrix3().translate(textureCoords)
                         .scale(textureScaling.x, -1 * textureScaling.y)
                         .translate(0, -1);
      
      Matrix4 transformationMatrix
          = new Matrix4().translate(tile.getPosition())
                         .translate(0.5f, 0.5f, 0)
                         .rotate(0, 0, 1, tile.getOrientation().getAngle())
                         .translate(-0.5f, -0.5f, 0);
      
      Mesh geometry = geometryManager.getGeometry(tile.getGeometryName())
                                     .getMesh()
                                     .copy(true);

      geometry.transform(transformationMatrix);
      geometry.transformUV(transformationMatrixUV);
      
      float[] tileVertices 
          = new float[NUM_COMPONENTS * geometry.getNumIndices()];
      
      geometry.getVertices(tileVertices);
      
      System.arraycopy(tileVertices, 
                       0, 
                       vertices, 
                       vertexCount, 
                       tileVertices.length);
      
      vertexCount += tileVertices.length;
    }
    
    mesh.setVertices(vertices, 0, vertexCount);
  }
  
  public Renderable createRenderable(Mesh mesh, 
                                     int meshSize, 
                                     TextureAtlas textureSet) {
    Renderable renderable = new Renderable();
    
    renderable.mesh = mesh;
    renderable.material = new Material(
        new TextureAttribute(TextureAttribute.Diffuse, 
                             textureSet.getTextures().first()),
        new ShaderAttribute(1));
    renderable.meshPartOffset = 0;
    renderable.meshPartSize = meshSize;
    renderable.primitiveType = GL20.GL_TRIANGLES;
    
    return renderable;
  }

  @Override
  public void update(Spatial.UpdateType type, Object value) {
    switch (type) {
      default: throw new UnsupportedOperationException();
    }
  }
  
  public void setGeometryManager(GeometryManager manager) {
    this.geometryManager = manager;
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, 
                             Pool<Renderable> pool) {
    renderables.add(renderable);
  }

}
