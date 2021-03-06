package org.ovirt.engine.ui.common.editor;

import org.ovirt.engine.ui.uicommonweb.models.IModel;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;

/**
 * UiCommon specific flavor of {@link SimpleBeanEditorDriver} interface to be used with
 * GWT Editor framework.
 *
 * <pre>
 * public class MyView {
 *
 *      // define the Driver interface that binds Model to View
 *      interface Driver extends UiCommonEditorDriver&lt;MyModel, MyView&gt; {}
 *
 *      // make sure that GWT.create'd instance is not static
 *      private final Driver driver = GWT.create(Driver.class);
 *
 *      public MyView() {
 *          // associate the Driver with this View
 *          driver.initialize(this);
 *      }
 *
 *      public void updateViewFromModel(MyModel object) {
 *          driver.edit(object);
 *      }
 *
 *      public MyModel getUpdatedModelFromView() {
 *          return driver.flush();
 *      }
 *
 *      public void cleanupModelAndView() {
 *          driver.cleanup();
 *      }
 *
 * }
 * </pre>
 *
 * @param <T> The type being edited.
 * @param <E> The associated Editor type.
 */
public interface UiCommonEditorDriver<T extends IModel, E extends Editor<? super T>>
        extends SimpleBeanEditorDriver<T, E> {

    /**
     * Clean up the edited Model object as well as the Editor instance.
     * The generated implementation calls the {@code cleanup} method on following objects:
     * <ul>
     * <li>the associated top-level Model
     * <li>all Models edited through the top-level Model
     * <li>non-Model Editor fields that implement {@code HasCleanup} interface
     * </ul>
     *
     * @see org.ovirt.engine.ui.uicommonweb.HasCleanup
     */
    void cleanup();

}
