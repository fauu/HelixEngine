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
import com.github.fauu.helix.datum.SpatialUpdateRequest;
import com.github.fauu.helix.spatial.Spatial;

import java.util.LinkedList;

public class SpatialFormComponent extends Component {

  private Spatial spatial;
  
  private LinkedList<SpatialUpdateRequest> updateRequestQueue;
  
  public SpatialFormComponent() { 
    this.updateRequestQueue = new LinkedList<SpatialUpdateRequest>();
  }
  
  public SpatialFormComponent(Spatial spatial) {
    this();

    set(spatial);
  }
  
  public Spatial get() {
    return spatial;
  }
  
  public void set(Spatial spatial) {
    this.spatial = spatial;
  }

  public void requestUpdate(SpatialUpdateRequest request) {
    updateRequestQueue.add(request);
  }
  
  public SpatialUpdateRequest pollUpdateRequest() {
    return updateRequestQueue.poll();
  }

}
