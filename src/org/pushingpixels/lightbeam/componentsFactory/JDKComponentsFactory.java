package org.pushingpixels.lightbeam.componentsFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.Format;

public class JDKComponentsFactory implements ComponentsFactory {
    @Override
    public JTextArea createTextArea() {
        return new JTextArea();
    }

    @Override
    public JTextArea createTextArea(String text, int rows, int columns){
        return new JTextArea(text,rows,columns);
    }

    @Override
    public JTextField createTextField(String text) {
        return new JTextField(text);
    }

    @Override
    public JTextPane createTextPane() {
        return new JTextPane();
    }

    @Override
    public JPasswordField createPasswordField(String text, int columns) {
        return new JPasswordField(text, columns);
    }

    @Override
    public JEditorPane createEditorPane(String type, String text) {
        return new JEditorPane();
    }

    @Override
    public JFormattedTextField createFormattedTextField(Format format) {
        return new JFormattedTextField(format);
    }

    @Override
    public JLabel createLabel(String text) {
        return new JLabel(text);
    }

    @Override
    public JPanel createPanel() {
        return new JPanel();
    }

    @Override
    public JScrollPane createScrollPane(Component view) {
        return new JScrollPane(view);
    }

    @Override
    public JButton createButton(String text) {
        return new JButton(text);
    }

    @Override
    public JToggleButton createToggleButton(String text) {
        return new JToggleButton(text);
    }

    @Override
    public JCheckBox createCheckBox(String text) {
        return new JCheckBox(text);
    }

    @Override
    public JRadioButton createRadioButton(String text) {
        return new JRadioButton(text);
    }

    @Override
    public JComboBox createComboBox(Object[] items) {
        return new JComboBox(items);
    }

    @Override
    public JCheckBoxMenuItem createCheckBoxMenuItem(String text) {
        return new JCheckBoxMenuItem(text);
    }

    @Override
    public JMenu createMenu(String s) {
        return new JMenu(s);
    }

    @Override
    public JList createList(ListModel dataModel) {
        return new JList(dataModel);
    }

    @Override
    public JProgressBar createProgressBar(int orient, int min, int max) {
        return new JProgressBar(orient, min, max);
    }

    @Override
    public JComponent createSlider(int min, int max, int value) {
        return new JSlider(min, max, value);
    }

    @Override
    public JSpinner createSpinner(SpinnerModel model) {
        return new JSpinner(model);
    }

    @Override
    public JTable createTable(TableModel dm) {
        return new JTable(dm);
    }

    @Override
    public JTabbedPane createTabbedPane() {
        return new JTabbedPane();
    }

    @Override
    public JTree createTree() {
        return new JTree();
    }

    @Override
    public String getType() {
        return "JDK";
    }
}
