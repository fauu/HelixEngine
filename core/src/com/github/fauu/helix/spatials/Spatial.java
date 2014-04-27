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

package com.github.fauu.helix.spatials;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;

public abstract class Spatial implements RenderableProvider {

  boolean ready;

  public Spatial() {}

  public boolean isReady() {
    return ready;
  }

}
