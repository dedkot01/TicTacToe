/*
 * Представляет собой клиента, играющего за одну из сторон Крестики-нолики
 */
package tictactoeserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 * @author dedkot
 */

/**
* Объект класса {@code Client} обрабатывает сообщения одного клиента.
* Расширен от класса {@code Thread}.
*/
class Client extends Thread {
    private Socket client;
    private boolean statusConnection;
    private BufferedReader in;
    private BufferedWriter out;

    private String side;    // Сторона, за которую будет играть клиент
    private Client enemy;
    private String[] field;
    
    private JTextArea taLog;
    
    /**
     * Конструктор, инициализирует объект пустым соединением и задаёт сторону для клиента.
     * @param field Игровое поле.
     * @param sidePlayer Сторона, за которую будет играть клиент (крестики или нолики).
     * @param log Интерфейс, куда выводится информация о ходе действий игрока.
     */
    Client(String[] field, String sidePlayer, JTextArea log) {
        client = new Socket();
        statusConnection = false;

        this.field = field;
        side = sidePlayer;
        
        taLog = log;
    }

    /**
     * Задаёт сокет для объекта клиента.
     * @param s Сокет соединения, от него будет создан
     * {@code BufferedReader} и {@code BufferedWriter}, изменит состояние соединения.
     * @throws IOException При инициализации буферов может создать исключение.
     */
    public void setSocket(Socket s) throws IOException {
        client = s;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        statusConnection = true;
    }

    /**
     * Передаёт ссылку на противника.
     * @param obj противник.
     */
    public void setEnemy(Client obj) {
        enemy = obj;
    }
    
    /**
     * Проверка состояния соединения.
     * @return состояние соединения.
     */
    public boolean isConnection() {
        return statusConnection;
    }
    
    /**
     * Отправляет сообщение клиенту.
     * @param message сообщение.
     * @throws IOException 
     */
    public void sendMsg(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    /**
     * Читает строку из буфера соединения с клиентом.
     * @return Сообщение.
     * @throws IOException 
     */
    public String readMsg() throws IOException {
        return in.readLine();
    }

    /**
     * Выполняет проверку на победу
     * @param side проверяемая сторона
     * @return true, если сторона победила
     */
    private String checkField(String side) {
        /* Поле:
        * 0 1 2
        * 3 4 5
        * 6 7 8
        */
        if (field[0] == side && field[1] == side && field[2] == side)
            return "123";
        
        if (field[3] == side && field[4] == side && field[5] == side)
            return "456";
        
        if (field[6] == side && field[7] == side && field[8] == side)
            return "789";
        
        if (field[0] == side && field[3] == side && field[6] == side)
            return "147";
        
        if (field[1] == side && field[4] == side && field[7] == side)
            return "258";
        
        if (field[2] == side && field[5] == side && field[8] == side)
            return "369";
        
        if (field[0] == side && field[4] == side && field[8] == side)
            return "159";
        
        if (field[2] == side && field[4] == side && field[6] == side)
            return "357";
        
        return null;
        /*
        return (field[0] == side && field[1] == side && field[2] == side ||
                field[3] == side && field[4] == side && field[5] == side ||
                field[6] == side && field[7] == side && field[8] == side ||

                field[0] == side && field[3] == side && field[6] == side ||
                field[1] == side && field[4] == side && field[7] == side ||
                field[2] == side && field[5] == side && field[8] == side ||

                field[0] == side && field[4] == side && field[8] == side ||
                field[2] == side && field[4] == side && field[6] == side);*/
    }

    /**
     * Закрывает все связанные с клиентом соединения.
     * @throws IOException 
     */
    public void close() throws IOException {
        if (in != null)
            in.close();
        if (out != null)
            out.close();
        if (client != null && !client.isClosed())
            client.close();
        statusConnection = false;
    }

    /**
     * Главный метод нити.
     */
    @Override
    public void run() {
        try {
            // Главный цикл, который завершается как только соединение будет
            // оборвано или завершится игра
            while (statusConnection) {
                // Читает и обрабатывает сообщение
                String msg = readMsg();
                switch (msg) {
                    // Клиент хочет покинуть игру (нажал "Отсоединиться")
                    case "Leave":
                        sendMsg("Disconnect");
                    // Клиент отсоединился от сервера
                    case "BYE":
                        close();
                        taLog.append("Игрок " + side + " отключился.\n");
                        if (enemy != null && enemy.statusConnection) {
                           enemy.sendMsg("Disconnect");
                        }
                        break;
                    // Занял ячейку поля
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                    case "9":
                        field[Integer.parseInt(msg)-1] = side;
                        enemy.sendMsg(msg);
                        String res = checkField(side);
                        if (res != null) {
                            sendMsg("WIN\n" + res);
                            enemy.sendMsg("DEFEAT\n" + res);
                            taLog.append(side + " победили!\n");
                        } else {
                            boolean isDraw = false;
                            for (String cell : field) {
                                if (cell == null) {
                                    isDraw = false;
                                    sendMsg("EnemyStep");
                                    enemy.sendMsg("UStep");
                                    break;
                                }
                                isDraw = true;
                            }
                            if (isDraw) {
                                sendMsg("DRAW");
                                enemy.sendMsg("DRAW");
                                taLog.append("Ничья!\n");
                            }
                        }
                        break;
                }
            }
        } catch (IOException ex) {
            // Чаще всего возникает, когда выполняется readLine()
            statusConnection = false;
            taLog.append("Игрок " + side + " не отвечает и был отсоединён.\n");
            try {
                if (enemy != null && enemy.statusConnection) {
                    enemy.sendMsg("Disconnect");
                }
            } catch (IOException ex1) {
                enemy.statusConnection = false;
                taLog.append("Игрок " + enemy.side + " не отвечает и был отсоединён.\n");
            }
        }
    }
}
