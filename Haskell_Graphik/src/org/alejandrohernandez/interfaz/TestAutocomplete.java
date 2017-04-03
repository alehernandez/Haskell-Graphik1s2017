/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alejandrohernandez.interfaz;

import jaco.swing.autocomplete.Autocomplete;
import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author oscar
 */
public class TestAutocomplete extends JFrame {

    public TestAutocomplete() {
        JTextPane jtf = new JTextPane();
        JTextArea jta = new JTextArea();
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);

        String[] entries = {"auto", "autobus", "autocorrect", "autocomplete",
            "autograph", "automate", "automated", "automates", "automatic",
            "automatically", "automation", "automobile", "autonomous",
            "autonomy", "autopilot", "autopsied", "autopsies", "autopsy"};

        // create an autocomplete for JTextField
        Autocomplete autocomplete = new Autocomplete(entries, true);
        autocomplete.setMaximumRowCount(10);
        // configure autocomplete to read the token from the beginning of the line
        autocomplete.setDelimiter(Autocomplete.LINE_START_PATTERN);
        // configure autocomplete to read the token up to the newline rather than the caret position
        autocomplete.setCompletionBoundary(Autocomplete.CompletionBoundary.NEWLINE);
        // ATTENTION: the following methods whose comment is marked with ** are not available in the binary version
        // make autocomplete select/ highlight a menu item when the mouse pointer rolls over it**
        //autocomplete.setSelectedOnRollover(true);
        // make autocomplete pre-insert a completion into the document immediately as it is selected**
    //    autocomplete.setCompleteImmediately(true);
        // allow the menu to clear its selection when user navigates past the first/ last item using the keyboard keys**
   //     autocomplete.setListNavigationMode(ListNavigationMode.EXTENDED);
        // set the visible row count**
   //     autocomplete.setVisibleRowCount(5);
        // set the shortcut key to trigger the autocomplete menu**
       // autocomplete.setShortcutKey(KeyStroke.getKeyStroke("pressed DOWN"));
        autocomplete.setTextComponent(jtf);

        // create an autocomplete for JTextArea
        Autocomplete autocomplete2 = new Autocomplete(Arrays.asList(entries), true);
        autocomplete2.setMinimumPrefixLength(1);
        autocomplete2.setMaximumRowCount(10);
        // set the shortcut key to trigger the autocomplete menu**
        //autocomplete2.setShortcutKey(KeyStroke.getKeyStroke("shift pressed TAB"));
        autocomplete2.setTextComponent(jta);

        add(jtf, BorderLayout.NORTH);
        add(jta, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new TestAutocomplete();
                frame.setTitle("TestAutocomplete");
                frame.setSize(300, 200);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}