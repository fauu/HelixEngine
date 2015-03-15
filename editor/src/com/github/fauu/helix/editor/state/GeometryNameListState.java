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

package com.github.fauu.helix.editor.state;

import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.datum.GeometryNameEntry;
import com.github.fauu.helix.editor.event.GeometryNameListStateChangedEvent;

import java.util.LinkedList;
import java.util.List;

public class GeometryNameListState {

  private List<GeometryNameEntry> data;
  
  public GeometryNameListState() {
    data = new LinkedList<>();
  }
  
  public void initialize(List<GeometryNameEntry> entries) {
    this.data = entries;
    
    postChangeEvent();
  }
  
  public List<GeometryNameEntry> getData() {
    return data;
  }
  
  public void setSelected(String name, boolean value) {
    for (GeometryNameEntry gne : data) {
      if (gne.getName().equalsIgnoreCase(name)) {
        gne.setSelected(value);
        
        break;
      }
    }
  }
  
  private void postChangeEvent() {
    HelixEditor.getInstance()
               .getUIEventBus()
               .post(new GeometryNameListStateChangedEvent(data));
  }

}
