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

package com.github.fauu.helix.datum;

import com.github.fauu.helix.displayable.Displayable;

public class DisplayableUpdateRequest {

  private Displayable.UpdateType type;
  
  private Object value;
  
  public DisplayableUpdateRequest(Displayable.UpdateType type, Object value) {
    setType(type);
    setValue(value);
  }

  public Displayable.UpdateType getType() {
    return type;
  }

  public void setType(Displayable.UpdateType type) {
    this.type = type;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

}
