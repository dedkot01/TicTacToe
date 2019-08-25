/*
 * Клиент для игры в крестики-нолики
 */
package tictactoe;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Dadon
 */
public class MainFormClient extends javax.swing.JFrame {

    private Socket connection;          // сокет для связи с сервером
    private String side, enemySide;     // сторона текущего клиента и противника

    private LinkedList<JButton> field;  // игровое поле из кнопок
    
    private ClientThread client;        // нить обработки сообщений от сервера
    private BufferedReader in;          // входящий буфер сети
    private BufferedWriter out;         // исходящий буфер сети
    
    /**
     * Объект класса {@code ClientThread} обрабатывает входящие сообщения от сервера.
     * Расширен от класса {@code Thread}.
     */
    class ClientThread extends Thread {
        @Override
        public void run() {
            try {
                // Будет принимать сообщения, пока игра не закончится
                boolean isEnd = false;
                char res[] = new char[3];
                while (!isEnd) {
                    String mes = in.readLine();
                    switch(mes) {
                        // Сервер отсоединяет клиента
                        case "Disconnect":
                            for (JButton but : field)
                                but.setEnabled(false);
                            lStatus.setText("Отключение...");
                            isEnd = true;
                            break;
                        // Сервер сообщает, что сейчас наш ход
                        case "UStep":
                            for (JButton currentBut : field) {
                                if ("".equals(currentBut.getText()))
                                    currentBut.setEnabled(true);
                            }
                            lStatus.setText("Ваш ход");
                            break;
                        // Сервер сообщает, что сейчас ход противника
                        case "EnemyStep":
                            lStatus.setText("Ход противника");
                            break;
                        // Ожидание противника
                        case "WaitEnemy":
                            lStatus.setText("Ждём противника");
                            break;
                        // Мы победили
                        case "WIN":
                            // Выделяем цветом победную комбинацию
                            in.read(res, 0, 3);
                            for (char i : res)
                                if (Character.getNumericValue(i) - 1 >= 0 && Character.getNumericValue(i) - 1 < 9)
                                    field.get(Character.getNumericValue(i) - 1).setForeground(Color.green);
                            for (JButton but : field)
                                but.setEnabled(false);
                            lStatus.setText("ПОБЕДА!");
                            isEnd = true;
                            break;
                        // Мы проиграли
                        case "DEFEAT":
                            // Выделяем цветом победную комбинацию
                            in.read(res, 0, 3);
                            for (char i : res)
                                if (Character.getNumericValue(i) - 1 >= 0 && Character.getNumericValue(i) - 1 < 9)
                                    field.get(Character.getNumericValue(i) - 1).setForeground(Color.red);
                            for (JButton but : field)
                                but.setEnabled(false);
                            lStatus.setText("ПРОИГРЫШ!");
                            isEnd = true;
                            break;
                        // Ничья
                        case "DRAW":
                            isEnd = true;
                            lStatus.setText("НИЧЬЯ!");
                            break;
                        // Мы будем играть за крестики
                        case "X":
                            side = "X";
                            enemySide = "O";
                            break;
                        // Мы будем играть за нолики
                        case "O":
                            side = "O";
                            enemySide = "X";
                            break;
                        // Сделанный ход противника
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "9":
                            field.get(Integer.parseInt(mes)-1).setText(enemySide);
                            break;
                    }
                }
                out.write("BYE\n");
                out.flush();
                sleep(5000);
                restart();
            } catch (Exception ex) {
                restart();
            }
        }
    }
    
    // Возвращает интерфейс в исходное состояние
    private void restart() {
        lStatus.setText("Подключитесь к серверу");
        tfAddress.setEditable(true);
        butDisConnect.setText("Подключиться");
        butDisConnect.setEnabled(true);
        for (JButton but : field) {
            but.setText("");
            but.setForeground(Color.BLACK);
            but.setEnabled(false);
        }
    }
    
    /**
     * Creates new form MainForm
     */
    public MainFormClient() {
        initComponents();
        
        field = new LinkedList<>();
        field.add(butField0);
        field.add(butField1);
        field.add(butField2);
        field.add(butField3);
        field.add(butField4);
        field.add(butField5);
        field.add(butField6);
        field.add(butField7);
        field.add(butField8);
        restart();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        butField0 = new javax.swing.JButton();
        butField1 = new javax.swing.JButton();
        butField2 = new javax.swing.JButton();
        butField3 = new javax.swing.JButton();
        butField4 = new javax.swing.JButton();
        butField5 = new javax.swing.JButton();
        butField6 = new javax.swing.JButton();
        butField7 = new javax.swing.JButton();
        butField8 = new javax.swing.JButton();
        lStatus = new javax.swing.JLabel();
        tfAddress = new javax.swing.JTextField();
        butDisConnect = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Крестики-нолики");
        setBackground(javax.swing.UIManager.getDefaults().getColor("window"));
        setLocation(new java.awt.Point(200, 200));
        setMaximumSize(new java.awt.Dimension(200, 280));
        setMinimumSize(new java.awt.Dimension(200, 280));
        setName("mainForm"); // NOI18N
        setPreferredSize(new java.awt.Dimension(200, 280));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        butField0.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField0.setText("1");
        butField0.setEnabled(false);
        butField0.setMaximumSize(new java.awt.Dimension(50, 50));
        butField0.setMinimumSize(new java.awt.Dimension(50, 50));
        butField0.setName(""); // NOI18N
        butField0.setPreferredSize(new java.awt.Dimension(50, 50));
        butField0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField0ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        getContentPane().add(butField0, gridBagConstraints);
        butField0.getAccessibleContext().setAccessibleName("butField0");

        butField1.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField1.setText("2");
        butField1.setEnabled(false);
        butField1.setMaximumSize(new java.awt.Dimension(50, 50));
        butField1.setMinimumSize(new java.awt.Dimension(50, 50));
        butField1.setName(""); // NOI18N
        butField1.setPreferredSize(new java.awt.Dimension(50, 50));
        butField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        getContentPane().add(butField1, gridBagConstraints);

        butField2.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField2.setText("3");
        butField2.setEnabled(false);
        butField2.setMaximumSize(new java.awt.Dimension(50, 50));
        butField2.setMinimumSize(new java.awt.Dimension(50, 50));
        butField2.setName(""); // NOI18N
        butField2.setPreferredSize(new java.awt.Dimension(50, 50));
        butField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        getContentPane().add(butField2, gridBagConstraints);

        butField3.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField3.setText("4");
        butField3.setEnabled(false);
        butField3.setMaximumSize(new java.awt.Dimension(50, 50));
        butField3.setMinimumSize(new java.awt.Dimension(50, 50));
        butField3.setName(""); // NOI18N
        butField3.setPreferredSize(new java.awt.Dimension(50, 50));
        butField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(butField3, gridBagConstraints);

        butField4.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField4.setText("5");
        butField4.setEnabled(false);
        butField4.setMaximumSize(new java.awt.Dimension(50, 50));
        butField4.setMinimumSize(new java.awt.Dimension(50, 50));
        butField4.setName(""); // NOI18N
        butField4.setPreferredSize(new java.awt.Dimension(50, 50));
        butField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        getContentPane().add(butField4, gridBagConstraints);

        butField5.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField5.setText("6");
        butField5.setEnabled(false);
        butField5.setMaximumSize(new java.awt.Dimension(50, 50));
        butField5.setMinimumSize(new java.awt.Dimension(50, 50));
        butField5.setName(""); // NOI18N
        butField5.setPreferredSize(new java.awt.Dimension(50, 50));
        butField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        getContentPane().add(butField5, gridBagConstraints);

        butField6.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField6.setText("7");
        butField6.setToolTipText("");
        butField6.setEnabled(false);
        butField6.setMaximumSize(new java.awt.Dimension(50, 50));
        butField6.setMinimumSize(new java.awt.Dimension(50, 50));
        butField6.setName(""); // NOI18N
        butField6.setPreferredSize(new java.awt.Dimension(50, 50));
        butField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        getContentPane().add(butField6, gridBagConstraints);

        butField7.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField7.setText("8");
        butField7.setEnabled(false);
        butField7.setMaximumSize(new java.awt.Dimension(50, 50));
        butField7.setMinimumSize(new java.awt.Dimension(50, 50));
        butField7.setName(""); // NOI18N
        butField7.setPreferredSize(new java.awt.Dimension(50, 50));
        butField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        getContentPane().add(butField7, gridBagConstraints);

        butField8.setFont(new java.awt.Font("Times New Roman", 1, 22)); // NOI18N
        butField8.setText("9");
        butField8.setEnabled(false);
        butField8.setMaximumSize(new java.awt.Dimension(50, 50));
        butField8.setMinimumSize(new java.awt.Dimension(50, 50));
        butField8.setName(""); // NOI18N
        butField8.setPreferredSize(new java.awt.Dimension(50, 50));
        butField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butField8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        getContentPane().add(butField8, gridBagConstraints);

        lStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lStatus.setText("Подключитесь к серверу");
        lStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lStatusMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        getContentPane().add(lStatus, gridBagConstraints);

        tfAddress.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfAddress.setText("127.0.0.1:8888");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        getContentPane().add(tfAddress, gridBagConstraints);

        butDisConnect.setText("Подключиться");
        butDisConnect.setOpaque(false);
        butDisConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butDisConnectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(butDisConnect, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Методы обработки ячеек полей, когда нажали, закрасили своим знаком
    private void butField0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField0ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField0.setText(side);
        
        try {
            out.write("1\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField0ActionPerformed

    private void butField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField1ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField1.setText(side);
        
        try {
            out.write("2\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField1ActionPerformed

    private void butField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField2ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField2.setText(side);
        
        try {
            out.write("3\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField2ActionPerformed

    private void butField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField3ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField3.setText(side);
        
        try {
            out.write("4\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField3ActionPerformed

    private void butField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField4ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField4.setText(side);
        
        try {
            out.write("5\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField4ActionPerformed

    private void butField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField5ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField5.setText(side);
        
        try {
            out.write("6\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField5ActionPerformed

    private void butField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField6ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField6.setText(side);
        
        try {
            out.write("7\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField6ActionPerformed

    private void butField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField7ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField7.setText(side);
        
        try {
            out.write("8\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField7ActionPerformed

    private void butField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butField8ActionPerformed
        for (JButton but : field)
            but.setEnabled(false);
        
        butField8.setText(side);
        
        try {
            out.write("9\n");
            out.flush();
        } catch (Exception ex) {
            lStatus.setText("Сервер не отвечает");
            restart();
        }
    }//GEN-LAST:event_butField8ActionPerformed
    // Конец методов обработки ячеек полей
    
    // Метод обработки кнопки "Подключиться/Отключиться"
    private void butDisConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butDisConnectActionPerformed
        try {
            // Если существует соединение, значит отключаемся от сервера
            if (client != null && client.isAlive()) {
                out.write("BYE\n");
                out.flush();
                connection.close();
                restart();
            // иначе подключаемся к серверу
            } else {
                try {
                    String ip = tfAddress.getText().substring(0, tfAddress.getText().indexOf(":"));
                    int port = Integer.parseInt(tfAddress.getText().substring(
                            tfAddress.getText().indexOf(":") + 1, tfAddress.getText().length()));
                    
                    connection = new Socket(ip, port);
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                    lStatus.setText("Подключение установлено");

                    client = new ClientThread();
                    client.start();

                    tfAddress.setEditable(false);
                    butDisConnect.setText("Отключиться");
                } catch (Exception ex) {
                    lStatus.setText("Адрес не корректен");
                }
            }
        } 
        catch (IOException ex) {
            restart();
        }
    }//GEN-LAST:event_butDisConnectActionPerformed

    // Метод обработки поля статуса, выводит информацию об авторе
    private void lStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lStatusMouseClicked
        JOptionPane.showMessageDialog(this, "Игра: Крестики-нолики\nАвтор: dedkot\ndedkot01@gmail.com", 
                "Справка", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_lStatusMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFormClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFormClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butDisConnect;
    private javax.swing.JButton butField0;
    private javax.swing.JButton butField1;
    private javax.swing.JButton butField2;
    private javax.swing.JButton butField3;
    private javax.swing.JButton butField4;
    private javax.swing.JButton butField5;
    private javax.swing.JButton butField6;
    private javax.swing.JButton butField7;
    private javax.swing.JButton butField8;
    private javax.swing.JLabel lStatus;
    private javax.swing.JTextField tfAddress;
    // End of variables declaration//GEN-END:variables
}
