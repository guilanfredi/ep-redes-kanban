package usp.kanban.client.View;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import usp.kanban.client.Cookie;

public final class Form
{

    public static Hashtable<String,String> LoginForm(){
        /*
            Código original em https://stackoverflow.com/questions/18395615/joptionpane-with-username-and-password-input
        */
        Hashtable<String, String> loginInformation = new Hashtable<String, String>();
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
        label.add(new JLabel("Usuario", SwingConstants.RIGHT));
        label.add(new JLabel("Senha", SwingConstants.RIGHT));
        panel.add(label, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField username = new JTextField();
        controls.add(username);
        JPasswordField password = new JPasswordField();
        controls.add(password);
        panel.add(controls, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(frame, panel, "Login", JOptionPane.QUESTION_MESSAGE);

        loginInformation.put("user", username.getText());
        loginInformation.put("pass", new String(password.getPassword()));
        
        /* Fim do código do stackoverflow */
        
        return loginInformation;
    }
}