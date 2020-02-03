package org.pushingpixels.lightbeam.componentsFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.Format;

public class JBComponentsFactory implements ComponentsFactory {
    @Override
    public JTextArea createTextArea() {
        JTextArea component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBTextArea");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JTextArea) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JTextArea createTextArea(String text, int rows, int columns){
        JTextArea component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBTextArea");
            Constructor<?> constructor = jbClass.getConstructor(String.class, Integer.TYPE , Integer.TYPE);
            component = (JTextArea) constructor.newInstance(text, rows, columns);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JTextField createTextField(String text) {
        JTextField component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBTextField");
            Constructor<?> constructor = jbClass.getConstructor(String.class);
            component = (JTextField) constructor.newInstance(text);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JTextPane createTextPane() {
        System.out.println("JBTextPane not found. Using default JTextPane");
        return new JTextPane();
    }

    @Override
    public JPasswordField createPasswordField(String text, int columns) {
        JPasswordField component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBPasswordField");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JPasswordField) constructor.newInstance();
            component.setText(text);
            component.setColumns(columns);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JEditorPane createEditorPane(String type, String text) {
        System.out.println("JBEditorPane not found. Using default JEditorPane");
        return new JEditorPane(type, text);
    }

    @Override
    public JFormattedTextField createFormattedTextField(Format format) {
        System.out.println("JBFormattedTextField not found. Using default JBFormattedTextField");
        return new JFormattedTextField(format);
    }

    @Override
    public JLabel createLabel(String text) {
        JLabel component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBLabel");
            Constructor<?> constructor = jbClass.getConstructor(String.class);
            component = (JLabel) constructor.newInstance(text);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JPanel createPanel() {
        JPanel component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBPanel");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JPanel) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JScrollPane createScrollPane(Component view) {
        JScrollPane component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBScrollPane");
            Constructor<?> constructor = jbClass.getConstructor(Component.class);
            component = (JScrollPane) constructor.newInstance(view);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JButton createButton(String text) {
        JButton component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.openapi.ui.FixedSizeButton");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JButton) constructor.newInstance();
            component.setText(text);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }
    
    @Override
    public JToggleButton createToggleButton(String text) {
        JToggleButton component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.OnOffButton");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JToggleButton) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JCheckBox createCheckBox(String text) {
        JCheckBox component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBCheckBox");
            Constructor<?> constructor = jbClass.getConstructor(String.class);
            component = (JCheckBox) constructor.newInstance(text);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JRadioButton createRadioButton(String text) {
        JRadioButton component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBRadioButton");
            Constructor<?> constructor = jbClass.getConstructor(String.class);
            component = (JRadioButton) constructor.newInstance(text);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JComboBox createComboBox(Object[] items) {
        JComboBox component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.openapi.ui.ComboBoxWithWidePopup");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JComboBox) constructor.newInstance();
            component.setModel(new DefaultComboBoxModel(items));
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JCheckBoxMenuItem createCheckBoxMenuItem(String text) {
        JCheckBoxMenuItem component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.openapi.ui.JBCheckboxMenuItem");
            Constructor<?> constructor = jbClass.getConstructor(String.class);
            component = (JCheckBoxMenuItem) constructor.newInstance(text);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JMenu createMenu(String text) {
        JMenu component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBMenu");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JMenu) constructor.newInstance();
            component.setText(text);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JList createList(ListModel dataModel) {
        JList component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBList");
            Constructor<?> constructor = jbClass.getConstructor(ListModel.class);
            component = (JList) constructor.newInstance(dataModel);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JProgressBar createProgressBar(int orient, int min, int max) {
        System.out.println("Using default JProgressBar");
        return new JProgressBar(orient, min, max);
    }

    @Override
    public JComponent createSlider(int min, int max, int value) {
        System.out.println("Using default JSlider");
        return new JSlider(min, max, value);
    }

    @Override
    public JSpinner createSpinner(SpinnerModel model) {
        System.out.println("Using default JSpinner");
        return new JSpinner(model);
    }

    @Override
    public JTable createTable(TableModel dm) {
        JTable component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.table.JBTable");
            Constructor<?> constructor = jbClass.getConstructor(TableModel.class);
            component = (JTable) constructor.newInstance(dm);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }


    @Override
    public JTabbedPane createTabbedPane() {
        JTabbedPane component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.components.JBTabbedPane");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JTabbedPane) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public JTree createTree() {
        JTree component = null;
        try {
            Class<?> jbClass = Class.forName("com.intellij.ui.treeStructure.Tree");
            Constructor<?> constructor = jbClass.getConstructor();
            component = (JTree) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return component;
    }

    @Override
    public String getType() {
        return "JB";
    }
}
