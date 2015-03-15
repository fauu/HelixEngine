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

package com.github.fauu.helix.postprocessing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class BloomShaderLoader {

  public static ShaderProgram createShader(String vertexName,
      String fragmentName) {

    String vertexShader = Gdx.files.internal(
        "shader/bloom/" + vertexName
            + ".vertex.glsl").readString();
    String fragmentShader = Gdx.files.internal(
        "shader/bloom/" + fragmentName
            + ".fragment.glsl").readString();
    ShaderProgram.pedantic = false;
    ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
    if (!shader.isCompiled()) {
      System.out.println(shader.getLog());
      Gdx.app.exit();
    } else
      Gdx.app.debug("shader compiled", shader.getLog());
    return shader;
  }
}
