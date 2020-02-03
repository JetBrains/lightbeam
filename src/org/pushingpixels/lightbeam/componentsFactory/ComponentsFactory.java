package org.pushingpixels.lightbeam.componentsFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.Format;

public interface ComponentsFactory {
    JTextArea createTextArea();
    JTextArea createTextArea(String text, int rows, int columns);
    JTextField createTextField(String text);
    JTextPane createTextPane();
    JPasswordField createPasswordField(String text, int columns);
    JEditorPane createEditorPane(String type, String text);
    JFormattedTextField createFormattedTextField(Format format);
    JLabel createLabel(String text);
    JPanel createPanel();
    JScrollPane createScrollPane(Component view);
    JButton createButton(String text);
    JToggleButton createToggleButton(String text);
    JCheckBox createCheckBox(String text);
    JRadioButton createRadioButton(String text);
    JComboBox createComboBox(Object[] items);
    JCheckBoxMenuItem createCheckBoxMenuItem(String text);
    JMenu createMenu(String s);
    JList createList(ListModel dataModel);
    JProgressBar createProgressBar(int orient, int min, int max);
    JComponent createSlider(int min, int max, int value);
    JSpinner createSpinner(SpinnerModel model);
    JTable createTable(TableModel dm);
    JTabbedPane createTabbedPane();
    JTree createTree();
    String getType();
}
