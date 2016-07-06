/*
 * Copyright (C) 2014-2016 Helix Engine Developers
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

package com.github.fauu.helix.editor.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.github.fauu.helix.editor.HelixEditor;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

public class NewAreaDialog extends VisWindow {

  // TODO: Add input validation
  public NewAreaDialog() {
    super("New Area");

    setModal(true);

    closeOnEscape();

    row();

    VisTable fieldsetContainer = new VisTable(true);

    fieldsetContainer.add(new VisLabel("Name:")).right();

    final VisTextField nameField = new VisTextField();
    fieldsetContainer.add(nameField);
    fieldsetContainer.row();

    fieldsetContainer.add(new VisLabel("Dimensions:"));

    final VisTextField widthField = new VisTextField("64");
    widthField.setMaxLength(3);
    fieldsetContainer.add(widthField);

    fieldsetContainer.add(new VisLabel("x"));

    final VisTextField lengthField = new VisTextField("48");
    lengthField.setMaxLength(3);
    fieldsetContainer.add(lengthField);

    fieldsetContainer.row();

    // TODO: Get rid of magic numbers. Here, there and everywhere.
    add(fieldsetContainer).padTop(3).spaceBottom(8);

    row();

    VisTable actionButtonsContainer = new VisTable(true);

    VisTextButton cancelButton = new VisTextButton("Cancel");
    cancelButton.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {
            fadeOut();
          }
        });
    actionButtonsContainer.add(cancelButton);

    VisTextButton createButton = new VisTextButton("Create");
    createButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
             HelixEditor.getInstance().closeCurrentAreaAction();

             HelixEditor.getInstance().createAreaAction(
                 nameField.getText(),
                 Integer.parseInt(widthField.getText()),
                 Integer.parseInt(lengthField.getText()));

             HelixEditor.getInstance().loadAreaAction(nameField.getText());

             fadeOut();
           }
        });
    actionButtonsContainer.add(createButton);

    add(actionButtonsContainer).right().padBottom(4);

    pack();
    centerWindow();
  }

}
