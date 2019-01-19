package com.sae.gandhi.spring.ui.common;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.shared.Registration;

/**
 * Abstract base class for dialogs adding, editing or deleting items.
 *
 * Subclasses are expected to
 * <ul>
 * <li>add, during construction, the needed UI components to
 * {@link #getFormLayout()} and bind them using {@link #getBinder()}, as well
 * as</li>
 * <li>override {@link #confirmDelete()} to open the confirmation dialog with
 * the desired message (by calling
 * {@link #openConfirmationDialog(String, String, String)}.</li>
 * </ul>
 *
 * @param <T>
 *            the type of the item to be added, edited or deleted
 */
public abstract class AbstractEditorDialog<T extends Serializable>
        extends Dialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3871945513612180158L;

	/**
     * The operations supported by this dialog. Delete is enabled when editing
     * an already existing item.
     */
    public enum Operation {
        ADD("Nuevo", "add", false), EDIT("Editar", "edit", true);

        private final String nameInTitle;
        private final String nameInText;
        private final boolean deleteEnabled;

        Operation(String nameInTitle, String nameInText,
                boolean deleteEnabled) {
            this.nameInTitle = nameInTitle;
            this.nameInText = nameInText;
            this.deleteEnabled = deleteEnabled;
        }

        public String getNameInTitle() {
            return nameInTitle;
        }

        public String getNameInText() {
            return nameInText;
        }

        public boolean isDeleteEnabled() {
            return deleteEnabled;
        }
    }

    private final H3 titleField = new H3();
    private final Button saveButton = new Button("Guardar");
    private final Button cancelButton = new Button("Cancelar");
    private final Button deleteButton = new Button("Borrar");
    private Registration registrationForSave;

    private final FormLayout formLayout = new FormLayout();
    private final HorizontalLayout buttonBar = new HorizontalLayout(saveButton,
            cancelButton, deleteButton);

    private Binder<T> binder = new Binder<>();
    private Boolean validFields;
    private T currentItem;

    private final ConfirmationDialog<T> confirmationDialog = new ConfirmationDialog<>();

    private final String itemType;
    private final BiConsumer<T, Operation> itemSaver;
    private final Consumer<T> itemDeleter;
    private Operation mainOperation;
    
    
    /**
     * Constructs a new instance.
     *
     * @param itemType
     *            The readable name of the item type
     * @param itemSaver
     *            Callback to save the edited item
     * @param itemDeleter
     *            Callback to delete the edited item
     */
    protected AbstractEditorDialog(String itemType,
            BiConsumer<T, Operation> itemSaver, Consumer<T> itemDeleter) {
        this.itemType = itemType;
        this.itemSaver = itemSaver;
        this.itemDeleter = itemDeleter;

        initTitle();
        initFormLayout();
        initButtonBar();
        setCloseOnEsc(true);
        setCloseOnOutsideClick(false);
    }

    private void initTitle() {
        add(titleField);
    }

    private void initFormLayout() {
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("25em", 2));
        Div div = new Div(formLayout);
        div.addClassName("has-padding");
        add(div);
    }

    private void initButtonBar() {
        saveButton.setAutofocus(true);
        saveButton.getElement().setAttribute("theme", "primary");
        cancelButton.addClickListener(e -> close());
        deleteButton.addClickListener(e -> deleteClicked());
        deleteButton.getElement().setAttribute("theme", "error");
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);
        add(buttonBar);
    }

    /**
     * Gets the form layout, where additional components can be added for
     * displaying or editing the item's properties.
     *
     * @return the form layout
     */
    protected final FormLayout getFormLayout() {
        return formLayout;
    }

    /**
     * Gets the binder.
     *
     * @return the binder
     */
    protected final Binder<T> getBinder() {
        return binder;
    }

    /**
     * Gets the item currently being edited.
     *
     * @return the item currently being edited
     */
    protected final T getCurrentItem() {
        return currentItem;
    }

    /**
     * Opens the given item for editing in the dialog.
     *
     * @param item
     *            The item to edit; it may be an existing or a newly created
     *            instance
     * @param operation
     *            The operation being performed on the item
     */
    public final void open(T item, Operation operation) {
        currentItem = item;
        mainOperation = operation;
        
        titleField.setText(operation.getNameInTitle() + " " + itemType);
        if (registrationForSave != null) {
            registrationForSave.remove();
        }
        registrationForSave = saveButton
                .addClickListener(e -> saveClicked(operation));
        
        binder.readBean(currentItem);

        deleteButton.setEnabled(operation.isDeleteEnabled());
        open();
    }

    private void saveClicked(Operation operation) {
        boolean isValid = binder.writeBeanIfValid(currentItem);
        boolean isValidFields = validateFields();

        if (isValid && isValidFields) {
            itemSaver.accept(currentItem, operation);
            close();
        } else {
            BinderValidationStatus<T> status = binder.validate();
        }
    }

    private void deleteClicked() {
        if (confirmationDialog.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(confirmationDialog));
        }
        confirmDelete();
    }

    protected abstract void confirmDelete();

    /**
     * Opens the confirmation dialog before deleting the current item.
     *
     * The dialog will display the given title and message(s), then call
     * {@link #deleteConfirmed(Serializable)} if the Delete button is clicked.
     *
     * @param title
     *            The title text
     * @param message
     *            Detail message (optional, may be empty)
     * @param additionalMessage
     *            Additional message (optional, may be empty)
     */
    protected final void openConfirmationDialog(String title, String message,
            String additionalMessage) {
        close();
        confirmationDialog.open(title, message, additionalMessage, "Borrar",
                true, getCurrentItem(), this::deleteConfirmed, this::open);
    }

    /**
     * Removes the {@code item} from the backend and close the dialog.
     *
     * @param item
     *            the item to delete
     */
    protected void doDelete(T item) {
        itemDeleter.accept(item);
        close();
    }

    private void deleteConfirmed(T item) {
        doDelete(item);
    }

	public Boolean getValidFields() {
		return validFields;
	}

	public void setValidFields(Boolean validFields) {
		this.validFields = validFields;
	}
	
	/* Metodo para realizar las validaciones de los campos */
	protected abstract Boolean validateFields();

	public Operation getMainOperation() {
		return mainOperation;
	}

	public void setMainOperation(Operation mainOperation) {
		this.mainOperation = mainOperation;
	}
}