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

package com.github.fauu.helix.spatial.dto;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureDTO {

  private Texture setTexture;

  private TextureRegion region;

  public TextureDTO(Texture setTexture, TextureRegion region) {
    this.setTexture = setTexture;
    this.region = region;
  }

  public Texture getSetTexture() {
    return setTexture;
  }
  
  public TextureRegion getRegion() {
    return region;
  }

}
