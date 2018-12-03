package com.skateboard.modulegenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.*;


public class ModuleInfoDialog extends JDialog {

    private DialogCallback dialogCallback;

    private final static String OK = "OK";

    private final static String CANCEL = "CANCEL";

    private final static String TITLE = "create module";

    private final static String MODULE_NAME_HINT = "module name:";

    private final static String PACKAGE_NAME_HINT = "package:";

    private final static String ERROR_MESSAGE = "Module name and package name cannot be empty";

    public ModuleInfoDialog(DialogCallback dialogCallback) {
        setSize(450, 300);
        setModal(true);
        setTitle(TITLE);
        prepareUI();
        this.dialogCallback = dialogCallback;

    }

    private void prepareUI() {


        GridBagLayout gridBagLayout = new GridBagLayout();
        JPanel contentPannel = new JPanel();
        contentPannel.setLayout(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 0;


        JLabel moduleHintLabel = new JLabel();
        moduleHintLabel.setText(MODULE_NAME_HINT);
        contentPannel.add(moduleHintLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx=0;
        JLabel packageNameHintLabel = new JLabel();
        packageNameHintLabel.setText(PACKAGE_NAME_HINT);
        contentPannel.add(packageNameHintLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        JTextField moduleNameImp = new JTextField();
//        moduleNameImp.setForeground(Color.WHITE);
        contentPannel.add(moduleNameImp, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        JTextField packageNameImp = new JTextField();
//        packageNameImp.setForeground(Color.WHITE);
        contentPannel.add(packageNameImp, constraints);


        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);


        JButton okBtn = new JButton();
        okBtn.setText(OK);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moduleNameImp.getText().isEmpty() || packageNameImp.getText().isEmpty()) {
                    errorLabel.setVisible(true);
                    errorLabel.setText(ERROR_MESSAGE);
                    return;
                }
                if (dialogCallback != null) {
                    dialogCallback.onOkClicked(moduleNameImp.getText(), packageNameImp.getText());
                }
                dispose();
            }
        });


        JButton cancelBtn = new JButton();
        cancelBtn.setText(CANCEL);
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (dialogCallback != null) {
                    dialogCallback.onCancelClicked();
                }
                dispose();
            }
        });

        constraints.fill=GridBagConstraints.NONE;
        constraints.anchor=WEST;
        constraints.weightx=0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPannel.add(okBtn, constraints);

        constraints.gridy = 2;
        constraints.gridx = 1;
        contentPannel.add(cancelBtn, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        contentPannel.add(errorLabel, constraints);

        setContentPane(contentPannel);
    }


    interface DialogCallback {

        void onOkClicked(String moduleName, String packageName);

        void onCancelClicked();
    }


    public static void pop(DialogCallback callback) {
        ModuleInfoDialog infoDialog = new ModuleInfoDialog(callback);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        infoDialog.setLocation(screenSize.width / 2 - infoDialog.getWidth() / 2, screenSize.height / 2 - infoDialog.getHeight() / 2);
        infoDialog.setVisible(true);
    }

}
