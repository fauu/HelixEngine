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

package com.github.fauu.helix.editor.manager;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.github.fauu.helix.component.DisplayableComponent;
import com.github.fauu.helix.displayable.ModelDisplayable;
import com.github.fauu.helix.manager.AreaManager;

public class DisplayableIntermediary extends Manager {

  @Wire
  private AreaManager areaManager;

  @Wire
  private ComponentMapper<DisplayableComponent> displayableMapper;

  public void setAreaDisplayableOpacity(float value) {
    Entity area = areaManager.getArea();

    ((ModelDisplayable) displayableMapper.get(area).get()).updateOpacity(value);
  }

}
