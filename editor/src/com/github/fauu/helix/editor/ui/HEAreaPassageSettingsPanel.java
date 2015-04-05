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

package com.github.fauu.helix.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.fauu.helix.TilePermission;
import com.github.fauu.helix.editor.HelixEditor;
import com.github.fauu.helix.editor.event.TilePassageAreaListStateChangedEvent;
import com.github.fauu.helix.editor.event.TilePermissionListStateChangedEvent;
import com.github.fauu.helix.editor.state.TilePassageAreaListState;
import com.github.fauu.helix.util.IntVector2;
import com.google.common.eventbus.Subscribe;
import com.kotcrab.vis.ui.VisTable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextField;

public class HEAreaPassageSettingsPanel extends VisTable {

  private SelectBox<String> areaSelectBox;

  private IntVector2 targetPosition;

  public HEAreaPassageSettingsPanel() {
    targetPosition = new IntVector2();

    HelixEditor.getInstance().getUIEventBus().register(this);

    setVisible(false);

    add(new VisLabel("Area passage settings:"));

    row();

    VisTable fieldsetContainer = new VisTable(true);

    fieldsetContainer.row().colspan(4);

    fieldsetContainer.add(new VisLabel("Target area:"));

    fieldsetContainer.row().colspan(4);

    areaSelectBox = new VisSelectBox<>();
    areaSelectBox.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        HelixEditor.getInstance()
            .getTilePassageAreaListState()
            .setSelected(areaSelectBox.getSelected());
      }
    });
    fieldsetContainer.add(areaSelectBox);

    fieldsetContainer.row();

    fieldsetContainer.add(new VisLabel("Target position:"));

    final VisTextField positionXField = new VisTextField();
    positionXField.setMaxLength(3);
    positionXField.setTextFieldListener(new VisTextField.TextFieldListener() {
          @Override
          public void keyTyped(VisTextField textField, char c) {
            try {
              targetPosition.x = Integer.parseInt(positionXField.getText());

              HelixEditor.getInstance()
                  .getTilePassageTargetPositionFieldState()
                  .setPosition(targetPosition);
            } catch (NumberFormatException ex) {
              ;
            }
          }
        });
    fieldsetContainer.add(positionXField).width(30);

    fieldsetContainer.add(new VisLabel(":"));

    final VisTextField positionYField = new VisTextField();
    positionYField.setMaxLength(3);
    positionYField.setTextFieldListener(new VisTextField.TextFieldListener() {
          @Override
          public void keyTyped(VisTextField textField, char c) {
            try {
              targetPosition.y = Integer.parseInt(positionYField.getText());

              HelixEditor.getInstance()
                         .getTilePassageTargetPositionFieldState()
                         .setPosition(targetPosition);
            } catch (NumberFormatException ex) {
              ;
            }
          }
        });
    fieldsetContainer.add(positionYField).width(30);

    add(fieldsetContainer).padTop(3).spaceBottom(8);

    pack();
  }

  @Subscribe
  public void tilePermissionsListStateChanged(
      TilePermissionListStateChangedEvent e) {
    setVisible(e.getMessage() == TilePermission.PASSAGE);
  }

  @Subscribe
  public void areaListStateChanged(TilePassageAreaListStateChangedEvent e) {
    TilePassageAreaListState state = e.getMessage();

    areaSelectBox.setItems(state.getItems());
    areaSelectBox.setSelected(state.getSelected());
  }

}
