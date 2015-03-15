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

package com.github.fauu.helix.component;

import com.artemis.Component;

public class TextureNameComponent extends Component {
  
  private String name;
  
  public TextureNameComponent() {
    name = "";
  }
  
  public TextureNameComponent(String name) {
    this.name = name;
  }
  
  public String get() {
    return name;
  }
  
  public void set(String name) {
    this.name = name;
  }

}
