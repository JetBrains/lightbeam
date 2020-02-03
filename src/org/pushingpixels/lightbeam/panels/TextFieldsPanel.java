/*
 * Copyright (c) 2008-2016 LightBeam Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of LightBeam Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.pushingpixels.lightbeam.panels;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import org.pushingpixels.lightbeam.*;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;
import org.pushingpixels.lightbeam.componentsFactory.ComponentsFactory;

/**
 * Test application panel for testing {@link JTextArea}, {@link JTextField}, {@link JTextPane},
 * {@link JPasswordField}, {@link JEditorPane} and {@link JFormattedTextField} components.
 * 
 * @author Kirill Grouchnikov
 */
public class TextFieldsPanel extends JPanel {
    private ComponentsFactory factory;
    /**
     * Returns the panel contents.
     * 
     * @return The panel contents.
     */
    private JPanel getContents() {
        FormLayout lm = new FormLayout("right:pref, 4dlu, fill:default:grow(1), 4dlu,"
                + "fill:default:grow(1), 4dlu, fill:default:grow(1)", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(lm, new JPanel())
                .border(Borders.DIALOG);

        JLabel textLabel = new JLabel("Text fields");
        JLabel formattedTextLabel = new JLabel("Formatted text fields");
        JLabel passwordLabel = new JLabel("Password fields");

        builder.append("", textLabel);
        builder.append(formattedTextLabel, passwordLabel);

        JTextField jtf1 = factory.createTextField("sample text");
        builder.append("Enabled", jtf1);

        JFormattedTextField jftf1 = factory.createFormattedTextField(new DecimalFormat("###0.0000"));
        jftf1.setText("2430.0000");
        JPasswordField jpf1 = factory.createPasswordField("password", 10);
        builder.append(jftf1, jpf1);

        JTextField jtfNotEditable = factory.createTextField("sample text");
        jtfNotEditable.setEditable(false);
        builder.append("Not editable", jtfNotEditable);

        JFormattedTextField jftfNotEditable = factory.createFormattedTextField(
                new DecimalFormat("###0.0000"));
        jftfNotEditable.setText("2430.0000");
        jftfNotEditable.setEditable(false);
        JPasswordField jpfNotEditable = factory.createPasswordField("password", 10);
        jpfNotEditable.setEditable(false);
        builder.append(jftfNotEditable, jpfNotEditable);

        JTextField jtfDisabled = factory.createTextField("sample text");
        jtfDisabled.setEnabled(false);
        builder.append("Disabled", jtfDisabled);

        JFormattedTextField jftfDisabled = factory.createFormattedTextField(new DecimalFormat("###0.0000"));
        jftfDisabled.setText("2430.0000");
        jftfDisabled.setEnabled(false);
        JPasswordField jpfDisabled = factory.createPasswordField("password", 10);
        jpfDisabled.setEnabled(false);
        builder.append(jftfDisabled, jpfDisabled);

        JTextField jtfNonOpaque = factory.createTextField("sample text");
        jtfNonOpaque.setOpaque(false);
        builder.append("Non opaque", jtfNonOpaque);

        JFormattedTextField jftfNonOpaque = factory.createFormattedTextField(new DecimalFormat("###0.0000"));
        jftfNonOpaque.setText("2430.0000");
        jftfNonOpaque.setOpaque(false);
        JPasswordField jpfNonOpaque = factory.createPasswordField("password", 10);
        jpfNonOpaque.setOpaque(false);
        builder.append(jftfNonOpaque, jpfNonOpaque);

        JTextField jtf6 = factory.createTextField("sample text");
        jtf6.setMargin(new Insets(2, 2, 2, 2));
        builder.append("All margin 2px", jtf6);

        JFormattedTextField jftf6 = factory.createFormattedTextField(new DecimalFormat("###0.0000"));
        jftf6.setText("2430.0000");
        jftf6.setMargin(new Insets(2, 2, 2, 2));
        JPasswordField jpf6 = factory.createPasswordField("password", 10);
        jpf6.setMargin(new Insets(2, 2, 2, 2));
        builder.append(jftf6, jpf6);

        return builder.getPanel();
    }

    /**
     * Creates a test panel with text components.
     */
    public TextFieldsPanel(ComponentsFactory factory) {
        this.factory = factory;
        setLayout(new BorderLayout());
        this.add(new JScrollPane(getContents()), BorderLayout.CENTER);
    }

    @PerformanceScenarioParticipant
    public PerformanceScenario getSetTextComponentTextPerformanceScenario() {
        return new BasePerformanceScenario<JTextComponent>(TextFieldsPanel.this,
                JTextComponent.class, false) {
            int[] perms;

            @Override
            public String getName() {
                return "Changing text fields text";
            }

            @Override
            public void setup() {
                super.setup();
                this.perms = LightBeamUtils.getPermutation(LightBeamUtils.dictionary.length,
                        getIterationCount() * this.controls.size() * 2);
            }

            @Override
            public int getIterationCount() {
                return 10;
            };

            @Override
            public void runSingleIteration(int iterationNumber) {
                int count = 0;
                for (JTextComponent field : this.controls) {
                    int index = this.controls.size() * iterationNumber + count;
                    if (field instanceof JFormattedTextField) {
                        int value = 100 + this.perms[index] * 10;
                        field.setText(value + "0.000");
                    } else {
                        field.setText(LightBeamUtils.dictionary[this.perms[index]] + " "
                                + LightBeamUtils.dictionary[this.perms[index + 1]]);
                    }
                    count++;
                }
                paintImmediately(new Rectangle(0, 0, getWidth(), getHeight()));
            };
        };
    }

}