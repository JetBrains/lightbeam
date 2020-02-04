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
package org.pushingpixels.lightbeam;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import org.pushingpixels.lightbeam.componentsFactory.*;
import org.pushingpixels.lightbeam.panels.BigTextAreaPanel;
import org.pushingpixels.lightbeam.panels.ButtonsPanel;
import org.pushingpixels.lightbeam.panels.CombosPanel;
import org.pushingpixels.lightbeam.panels.LightBeamMenuBar;
import org.pushingpixels.lightbeam.panels.ListPanel;
import org.pushingpixels.lightbeam.panels.ProgressBarPanel;
import org.pushingpixels.lightbeam.panels.SliderPanel;
import org.pushingpixels.lightbeam.panels.SpinnerPanel;
import org.pushingpixels.lightbeam.panels.TablePanel;
import org.pushingpixels.lightbeam.panels.TabsPanel;
import org.pushingpixels.lightbeam.panels.TextAreasPanel;
import org.pushingpixels.lightbeam.panels.TextFieldsPanel;
import org.pushingpixels.lightbeam.panels.TreePanel;

public class DynamicPerformanceSuite {

    public static final String GTK_LAF_CLASS = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    private ThreadMXBean threadBean;

    private long edtThreadId;

    private static String lafClass;

    private static ComponentsFactory factory;

    public class ComponentInfo {
        public Component tabComponent;

        public List<PerformanceScenario> scenarios;

        public ComponentInfo(Component tabComponent, List<PerformanceScenario> scenarios) {
            super();
            this.tabComponent = tabComponent;
            this.scenarios = scenarios;
        }
    }

    Map<String, ComponentInfo> scenarios;

    JTabbedPane tabs;

    private JFrame frame;

    boolean isCancelled;

    private JButton cancelButton;

    private JButton startButton;

    private List<ScenarioTimesInfo> scenarioTimes;

    public static class ScenarioTimesInfo {
        public String tabTitle;

        public String scenarioName;

        public List<Long> times;

        public ScenarioTimesInfo(String tabTitle, String scenarioName) {
            this.tabTitle = tabTitle;
            this.scenarioName = scenarioName;
            this.times = new ArrayList<Long>();
        }
    }

    public DynamicPerformanceSuite() {
        this.scenarios = new TreeMap<String, ComponentInfo>();
        this.tabs = new JTabbedPane();

        try {
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName objName = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
            Set<ObjectName> mbeans = mbeanServer.queryNames(objName, null);
            for (ObjectName name : mbeans) {
                threadBean = ManagementFactory.newPlatformMXBeanProxy(mbeanServer, name.toString(),
                        ThreadMXBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long threadIds[] = threadBean.getAllThreadIds();
        for (long threadId : threadIds) {
            ThreadInfo threadInfo = threadBean.getThreadInfo(threadId, Integer.MAX_VALUE);
            if (threadInfo.getThreadName().startsWith("AWT-EventQueue")) {
                edtThreadId = threadId;
            }
        }

        this.scenarioTimes = new ArrayList<ScenarioTimesInfo>();
    }

    public void initialize() {
        this.frame = new JFrame("Dynamic performance suite");

        ButtonsPanel buttonsPanel = new ButtonsPanel(factory);
        this.scanAndAddTab("Buttons", buttonsPanel);

        CombosPanel combosPanel = new CombosPanel(factory);
        this.scanAndAddTab("Combos", combosPanel);

        TablePanel tablePanel = new TablePanel(factory);
        this.scanAndAddTab("Table", tablePanel);

        ListPanel listPanel = new ListPanel(factory);
        this.scanAndAddTab("List", listPanel);

        if (factory instanceof JDKComponentsFactory) {
            SliderPanel slidersPanel = new SliderPanel();
            this.scanAndAddTab("Sliders", slidersPanel);
        }

        ProgressBarPanel progressBarsPanel = new ProgressBarPanel(factory);
        this.scanAndAddTab("Progress bars", progressBarsPanel);

        TextFieldsPanel textFieldsPanel = new TextFieldsPanel(factory);
        this.scanAndAddTab("Text fields", textFieldsPanel);

        TextAreasPanel textAreasPanel = new TextAreasPanel(factory);
        this.scanAndAddTab("Text areas", textAreasPanel);

        TabsPanel tabsPanel = new TabsPanel(factory);
        this.scanAndAddTab("Tabs", tabsPanel);

        BigTextAreaPanel bigTextAreaPanel = new BigTextAreaPanel(factory);
        this.scanAndAddTab("Text area", bigTextAreaPanel);

        TreePanel treePanel = new TreePanel(factory);
        this.scanAndAddTab("Tree", treePanel);

        SpinnerPanel spinnersPanel = new SpinnerPanel(factory);
        this.scanAndAddTab("Spinners", spinnersPanel);

        JMenuBar menuBar = new LightBeamMenuBar(factory);
        this.frame.setJMenuBar(menuBar);
        this.scan("Menu bar", menuBar);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        cancelButton = new JButton("cancel");
        cancelButton.addActionListener((ActionEvent e) -> isCancelled = true);

        startButton = new JButton("start");
        startButton.addActionListener((ActionEvent e) -> {
            startButton.setEnabled(false);
            cancelButton.setEnabled(true);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    runSingleRound(false, null);
                }

            };

            Executors.newCachedThreadPool().execute(runnable);
        });
        cancelButton.setEnabled(false);
        controls.add(startButton);
        controls.add(cancelButton);

        this.frame.add(this.tabs, BorderLayout.CENTER);
        this.frame.add(controls, BorderLayout.SOUTH);
        this.frame.setSize(800, 600);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setVisible(true);
    }

    private void runSingleRound(boolean toTime, String specificScenarioId) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>> START >>>>>>>>>>>>>>>>>>>>>");
        isCancelled = false;
        long total = 0;
        for (final Map.Entry<String, ComponentInfo> scenarioEntry : scenarios.entrySet()) {
            if (isCancelled)
                break;

            String tabTitle = scenarioEntry.getKey();
            final Component tabComponent = scenarioEntry.getValue().tabComponent;
            List<PerformanceScenario> scenarios = scenarioEntry.getValue().scenarios;
            for (final PerformanceScenario scenario : scenarios) {
                if (specificScenarioId != null) {
                    if (!specificScenarioId.equals(scenario.getName()))
                        continue;
                }

                // System.out.println("Running "
                // + scenario.getName());

                final boolean isTabPanel = (tabs.indexOfComponent(tabComponent) >= 0);
                try {
                    // must run scenario setup on EDT
                    // since most probably it involves
                    // UI-related operations
                    SwingUtilities.invokeAndWait(new Runnable() {
                        @Override
                        public void run() {
                            tabs.setVisible(isTabPanel);
                            if (!isTabPanel) {
                                tabs.setSelectedIndex(0);
                            } else {
                                tabs.setSelectedComponent(tabComponent);
                            }
                            scenario.setup();
                        }
                    });
                } catch (Exception exc) {
                    exc.printStackTrace();
                    System.exit(1);
                }

                final int iterationCount = scenario.getIterationCount();
                final CountDownLatch latch = new CountDownLatch(1);
                long start = System.nanoTime();

                long startEdtUser = threadBean.getThreadUserTime(edtThreadId);
                long startEdtCPU = threadBean.getThreadCpuTime(edtThreadId);

                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // System.out
                        // .println("Started working");
                        for (int i = 0; i < iterationCount; i++) {
                            this.publish(i);
                        }
                        return null;
                    }

                    @Override
                    protected void process(List<Integer> chunks) {
                        // System.out.println("Running "
                        // + chunks.size());
                        for (int chunk : chunks) {
                            if (!isCancelled)
                                scenario.runSingleIteration(chunk);
                        }
                    }

                    @Override
                    protected void done() {
                        // System.out.println("Done");
                        latch.countDown();
                    }
                };
                // System.out.println("Executing worker"
                // );
                worker.execute();

                try {
                    latch.await();
                } catch (InterruptedException ie) {
                }

                long end = System.nanoTime();
                long time = end - start;

                long endEdtUser = threadBean.getThreadUserTime(edtThreadId);
                long endEdtCPU = threadBean.getThreadCpuTime(edtThreadId);

                long edtUserTime = endEdtUser - startEdtUser;
                long edtCPUTime = endEdtCPU - startEdtCPU;

                try {
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            scenario.tearDown();
                        }
                    });
                } catch (Exception exc) {
                    exc.printStackTrace();
                    System.exit(1);
                }

                StringBuilder sbKey = new StringBuilder();
                Formatter keyFormatter = new Formatter(sbKey, Locale.US);
                keyFormatter.format("%1$15s : %2$s", tabTitle, scenario.getName());

                StringBuilder sb = new StringBuilder();
                Formatter formatter = new Formatter(sb, Locale.US);
                formatter.format("%1$4d [cpu %2$4d / usr %3$4d]", time / 1000000,
                        edtCPUTime / 1000000, edtUserTime / 1000000);
                System.out.println(sb.toString() + keyFormatter.toString());
                keyFormatter.close();
                formatter.close();
                total += time;

                if (toTime) {
                    getTimes(tabTitle, scenario.getName()).times.add(time / 1000000);
                }

                // System.out.println(tabTitle + " : " + scenario.getName());
                // System.out.println("\tTime: " + time / 1000000
                // + " ms, EDT CPU time: " + edtCPUTime / 1000000
                // + " ms, EDT user time: " + edtUserTime / 1000000
                // + " ms");
                // long heapSize = Runtime.getRuntime().totalMemory();
                // long heapFreeSize = Runtime.getRuntime().freeMemory();
                // int heapSizeKB = (int) (heapSize / 1024);
                // int takenHeapSizeKB = (int) ((heapSize - heapFreeSize) / 1024
                // );
                // System.out.println("\tHeap before GC: " + takenHeapSizeKB
                // + " out of " + heapSizeKB);
                //
                // System.gc();
                //
                // heapSize = Runtime.getRuntime().totalMemory();
                // heapFreeSize = Runtime.getRuntime().freeMemory();
                // heapSizeKB = (int) (heapSize / 1024);
                // takenHeapSizeKB = (int) ((heapSize - heapFreeSize) / 1024);
                // System.out.println("\tHeap after GC: " + takenHeapSizeKB
                // + " out of " + heapSizeKB);
            }
        }
        System.out.println("\n" + total / 1000000 + " total");
        startButton.setEnabled(true);
        cancelButton.setEnabled(false);
        System.out.println(">>>>>>>>>>>>>>>>>>>>> END >>>>>>>>>>>>>>>>>>>>>");
        System.out.println();
    }

    private static final StyleSheet DEFAULT_HTML_KIT_CSS;

    static {
        // save the default JRE CSS and ..
        HTMLEditorKit kit = new HTMLEditorKit();
        DEFAULT_HTML_KIT_CSS = kit.getStyleSheet();
    }

    public static StyleSheet createStyleSheet() {
        StyleSheet style = new StyleSheet();
        style.addStyleSheet(isUnderDarcula() ? (StyleSheet)UIManager.getDefaults().get("StyledEditorKit.JBDefaultStyle") : DEFAULT_HTML_KIT_CSS);
        style.addRule("code { font-size: 100%; }"); // small by Swing's default
        style.addRule("small { font-size: small; }"); // x-small by Swing's default
        style.addRule("a { text-decoration: none;}");

        return style;
    }

    public static boolean isUnderDarcula() {
        return UIManager.getLookAndFeel().getName().contains("Darcula");
    }

    private static void setLookAndFeel() throws UnsupportedLookAndFeelException, IllegalAccessException,
            InstantiationException, ClassNotFoundException {

        if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("mac") ||
                System.getProperty("os.name").toLowerCase(Locale.US).startsWith("windows"))
            UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
        else if (System.getProperty("os.name").toLowerCase(Locale.US).startsWith("linux")) {
            UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
            UIManager.setLookAndFeel(
                    (BasicLookAndFeel) ClassLoader.getSystemClassLoader().loadClass(GTK_LAF_CLASS).newInstance());
        }

        lafClass = System.getProperty("test.laf");
        UIManager.setLookAndFeel(
                (BasicLookAndFeel) ClassLoader.getSystemClassLoader().loadClass(lafClass).newInstance());

        // static init it is hell - UIUtil static init is called too early, so, call it to init properly
        // (otherwise null stylesheet added and it leads to NPE on set comment text)
        UIManager.getDefaults().put("javax.swing.JLabel.userStyleSheet", createStyleSheet());
    }
    private static void initControlsFactory() {
        String controlsType = System.getProperty("test.controls","jdk");
        if(controlsType.toLowerCase().equals("jb")) {
            factory = new JBComponentsFactory();
        } else if (controlsType.toLowerCase().equals("jdk")) {
            factory = new JDKComponentsFactory();
        }
    }

    public static void main(final String[] args) {
        try {
            setLookAndFeel();
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException
                | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        initControlsFactory();
        boolean prepareTCChart = System.getProperty("tcchart.enable", "").toLowerCase().contains("true");

        SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);

            final DynamicPerformanceSuite suite = new DynamicPerformanceSuite();
            suite.initialize();

            if (args.length > 0) {
                Runnable auto = new Runnable() {
                    @Override
                    public void run() {
                        int loopCount = Integer.parseInt(args[0]);
                        String specificScenarioId = null;
                        if (args.length == 2) {
                            specificScenarioId = args[1];
                        }
                        int warmups = 5;
                        System.out.println("Look-and-Feel: " + lafClass);
                        System.out.println("Controls: " + factory.getType());
                        for (int i = 0; i < loopCount + warmups; i++) {
                            suite.runSingleRound(i >= warmups, specificScenarioId);
                        }
                        long totalMin = 0;
                        StringBuilder tcReport = new StringBuilder();

                        for (ScenarioTimesInfo timesInfo : suite.scenarioTimes) {
                            List<Long> times = timesInfo.times;
                            long avg = 0;
                            for (long time : times) {
                                avg += time;
                            }
                            avg /= times.size();
                            long min = times.get(0);
                            for (long time : times) {
                                min = Math.min(min, time);
                            }
                            long max = times.get(0);
                            for (long time : times) {
                                max = Math.max(max, time);
                            }
                            double deviance = 0;
                            for (long time : times) {
                                deviance += (time - avg) * (time - avg);
                            }
                            deviance = Math.sqrt(deviance / times.size()) / avg;

                            StringBuilder sb = new StringBuilder();
                            Formatter formatter = new Formatter(sb, Locale.US);
                            formatter.format(
                                    "avg %1$5d, min %2$5d, max %3$5d, dev %4$4.2f %5$15s : %6$s",
                                    avg, min, max, deviance, timesInfo.tabTitle,
                                    timesInfo.scenarioName);
                            if (prepareTCChart) {
                                StringBuilder tcSb = new StringBuilder();
                                Formatter tcFormatter = new Formatter(tcSb, Locale.US);
                                tcFormatter.format(
                                        "##teamcity[buildStatisticValue key='%8$s_%7$s:%5$s:%6$s' value='%1$d']",
                                        //"avg %1$4d, min %2$4d, max %3$4d, dev %4$4.2f %5$15s : %6$s",
                                        avg, min, max, deviance, timesInfo.tabTitle.replace(' ', '_'),
                                        timesInfo.scenarioName.replace(' ', '_'),
                                        lafClass.substring(lafClass.lastIndexOf(".") + 1).trim(),
                                        factory.getType());
                                tcFormatter.close();
                                tcReport.append(tcSb.toString() + "\n");
                            }
                            formatter.close();
                            System.out.println(sb.toString());
                            totalMin += min;
                        }
                        System.out.println("\n" + totalMin + " totalMin");
                        if (prepareTCChart) {
                            System.out.println("\n\n" + tcReport);
                        }

                        System.exit(0);
                    }
                };
                Executors.newCachedThreadPool().execute(auto);
            }
        });
    }

    private void scan(String title, Component comp) {
        Class<?> compClass = comp.getClass();
        List<PerformanceScenario> scenarioList = new LinkedList<PerformanceScenario>();
        ComponentInfo componentInfo = new ComponentInfo(comp, scenarioList);
        scenarios.put(title, componentInfo);
        for (Method m : compClass.getDeclaredMethods()) {
            // check annotation
            if (m.getAnnotation(PerformanceScenarioParticipant.class) != null) {
                // check return type and parameters
                if ((m.getReturnType() == PerformanceScenario.class)
                        && (m.getParameterTypes().length == 0)) {
                    try {
                        PerformanceScenario scenario = (PerformanceScenario) m.invoke(comp);
                        componentInfo.scenarios.add(scenario);
                    } catch (Exception exc) {
                    }
                }
            }
        }
        componentInfo.scenarios.sort(new Comparator<PerformanceScenario>() {
            public int compare(PerformanceScenario o1, PerformanceScenario o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private void scanAndAddTab(String tabTitle, Component tabComp) {
        this.scan(tabTitle, tabComp);
        this.tabs.addTab(tabTitle, tabComp);
    }

    private ScenarioTimesInfo getTimes(String tabTitle, String scenarioName) {
        for (ScenarioTimesInfo sti : this.scenarioTimes) {
            if (tabTitle.equals(sti.tabTitle) && (scenarioName.equals(sti.scenarioName)))
                return sti;
        }
        ScenarioTimesInfo sti = new ScenarioTimesInfo(tabTitle, scenarioName);
        this.scenarioTimes.add(sti);
        return sti;
    }
}
