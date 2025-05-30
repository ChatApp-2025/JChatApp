
package Interfaces;


import dbmanager.DBManager;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import pojos.ChatClient;
import pojos.Groups;
import pojos.Message;
import pojos.Users;
import services.Chat;
import services.ChatService;


public class AppLayout extends javax.swing.JFrame {
    
    int id;
    Registry reg;
    Chat chat;
    int active_group;

    static int xx, yy;

    ChatClient me;

 
    public AppLayout() {
        initComponents();
        
       
        textusername.setBackground(new java.awt.Color(0,0,0,1));
        textpassword.setBackground(new java.awt.Color(0,0,0,1));
        textregemail.setBackground(new java.awt.Color(0,0,0,1));
        textregusername.setBackground(new java.awt.Color(0,0,0,1));
        textregnickname.setBackground(new java.awt.Color(0,0,0,1));
        textregpassword.setBackground(new java.awt.Color(0,0,0,1));
        textgroupname.setBackground(new java.awt.Color(0,0,0,1));
        textgroupdescription.setBackground(new java.awt.Color(0,0,0,1));
    
        edit_username.setBackground(new java.awt.Color(0,0,0,1));
        edit_nickname.setBackground(new java.awt.Color(0,0,0,1));
        edit_password.setBackground(new java.awt.Color(0,0,0,1));
        client_chat_groups_panel.setBackground(new java.awt.Color(0,0,0,1));
        msg_typer.setBackground(new java.awt.Color(0,0,0,1));


        
        login_panel.setVisible(true);
        register_panel.setVisible(false);
        admin_panel.setVisible(false);
        create_chat_panel.setVisible(false);
        list_groups_panel.setVisible(false);
        chat_panel.setVisible(false);
        edit_profile_panel.setVisible(false);
        manage_users_panel.setVisible(false);
        
 
        
        
        
    }
    
    public void app_ui_reset(){
        login_panel.setVisible(false);
        register_panel.setVisible(false);
        admin_panel.setVisible(false);
        create_chat_panel.setVisible(false);
        list_groups_panel.setVisible(false);
        chat_panel.setVisible(false);
        edit_profile_panel.setVisible(false);
        manage_users_panel.setVisible(false);
        
    }
   

    

    
        public ImageIcon toImageIcon(byte[] img) {
        BufferedImage Imgavatar;
        ImageIcon avatar = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(img);
            Imgavatar = ImageIO.read(bis);
            if (Imgavatar != null) {
                avatar = new ImageIcon(Imgavatar);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return avatar;
    }
        
      public BufferedImage ImageIconToBufferedImage(ImageIcon icon) {
        BufferedImage bi = new BufferedImage(
                icon.getIconWidth(),
                icon.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        // paint the Icon to the BufferedImage.
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        return bi;
    }
      
       public void showUsers(){
          List data = DBManager.getDBM().allUsers();

     JTable tbl = new JTable();
     DefaultTableModel table_users = new DefaultTableModel(0, 0);
     String header[] = new String[] { "Prority", "Task Title", "Start",
      "Pause", "Stop", "Statulses" };
      table_users.setColumnIdentifiers(header);
      tbl.setModel(table_users);



          for (Iterator iterator = data.iterator(); iterator.hasNext();){
            Users user = (Users) iterator.next(); 
            
            table_users.addRow(new Object[] { "data", "data", "data",
        "data", "data", "data" });
 
          }
          
          
      }
    
    public ArrayList<String> validatelogin(String username, String password) {
        ArrayList<String> errors = new ArrayList<>();

        if ("Username".equals(username) || "".equals(username)) {
            errors.add("Username is requird");
        }

        if ("Password".equals(password) || "".equals(password)) {
            errors.add("Password is requird");
        }

        return errors;
    }
        public void start_client() {

        try {
            reg = LocateRegistry.getRegistry("localhost", 2123);
            chat = (Chat) reg.lookup("ChatAdmin");

        } catch (RemoteException | NotBoundException ex) {
            System.out.println(ex);
        }

    }
        
        public void sender() {
        String m = msg_typer.getText();
        if (m.equalsIgnoreCase("bye")) {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String time_now = myDateObj.format(myFormatObj);

            
            Message msg = new Message();
            msg.setDate_time(time_now);
            String user = me.getUsername();
            m = "****** " + user  + " has left the chat " + " ******";

       
            try {
                chat.unsubscribre(enterd_grup_id, me);
            } catch (RemoteException ex) {
                Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
            }

            
            app_ui_reset();
            login_panel.setVisible(true);


            System.out.println(m);
            msg.setMessage(m);
            
  
            
            JScrollBar vertical = msgScrollPane.getVerticalScrollBar();
            msgScrollPane.setMaximumSize(vertical.getMaximumSize());
        

            try {
                chat.send_message(msg);
                
                msg_typer.setText("");
            } catch (RemoteException ex) {
                System.out.println(ex);
            }
            
            msgScrollPane.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });
        } else {

            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String time_now = myDateObj.format(myFormatObj);

            Message msg = new Message();
            msg.setGroup_id(enterd_grup_id);
            msg.setMsgid(msg.hashCode());
            msg.setUserid(me.getId());
            msg.setName(me.getUsername());
            msg.setMessage(m);
            msg.setDate_time(time_now);

            try {
                chat.send_message(msg);
                
                
           
        
                msg_typer.setText("");
            } catch (RemoteException ex) {
                System.out.println(ex);
            }
        }
        


    }
        
        
        
        
        public ArrayList<String> validateform(String email, String username,String password) {

        ArrayList<String> errors = new ArrayList<>();

        if (email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$") == false) {
            errors.add("Invalid email");
        }

        if ("Username".equals(username) || "".equals(username)) {
            errors.add("Username is requird");
        }

        if ("Password".equals(password) || "".equals(password)) {
            errors.add("Password is requird");
        }

        if (password.length() < 4) {
            errors.add("Password must contain more than 4 characters");
        }

        return errors;
    }
        int y = 13;
        
        public void load_admin_group(boolean is_called_signin) {
            
         y = 13;
         List groups = DBManager.getDBM().get_chat_groups();

         admin_group_list.removeAll();

         
            for (Iterator iterator = groups.iterator(); iterator.hasNext();) {
            Groups next = (Groups) iterator.next();

            if (is_called_signin) {
             
                DBManager.getDBM().put_offline(next.getId());
            }

            JPanel group = new javax.swing.JPanel();
            group.setBackground(new java.awt.Color(0,51,51)); //Chat Group Label Color
        
            group.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

            JLabel g_action = new javax.swing.JLabel();
            g_action.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (DBManager.getDBM().is_online(next.getId())) {
                g_action.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/end.png"))); //Icon to set Group LIVE
            } else {
                g_action.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/start.png"))); //Icon to set Group OFFLine 
            }

            g_action.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                
                    active_group = next.getId();
                    group_action(next.getId(), g_action);

                }
            });
            

            JLabel g_des = new javax.swing.JLabel();
            g_des.setForeground(new java.awt.Color(255, 255, 255));
            g_des.setText("<html>" + next.getDescription() + "</html>");
            
            

            JLabel g_name = new javax.swing.JLabel();
            g_name.setFont(new java.awt.Font("consolas", 1, 13));
            g_name.setForeground(new java.awt.Color(255, 255, 255));
            g_name.setText(next.getName());
            group.add(g_action, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, -1, 29));
            group.add(g_des, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 36, 163, 33));
            group.add(g_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 160, -1));
            admin_group_list.add(group, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, y, 294, 90));
            

            y += 110;
        
        
        
         }
        }
        
        public void group_action(int chat_id, JLabel g_action) {
            

            File btn_icon = new File("");
            String abspath = btn_icon.getAbsolutePath();

            if (DBManager.getDBM().is_online(chat_id)) {
              DBManager.getDBM().put_offline(chat_id);
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\start.png");
                g_action.setIcon(icon);
            } else if (DBManager.getDBM().put_online(chat_id)) {
                
                 System.out.println("chat is offline");
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\end.png");
                g_action.setIcon(icon);
                
                start_server(chat_id);


            }
    }
        
        
       public void subscribe_action(int grp_id, JLabel sub_btn) {
        try {
            File btn_icon = new File("");
            String abspath = btn_icon.getAbsolutePath();

            if (chat.is_subscribed(me.getId())) {
                chat.unsubscribre(grp_id, me);
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\join.png");
                sub_btn.setIcon(icon);
            } else {
                chat.subscribre(grp_id, me);
                ImageIcon icon = new ImageIcon(abspath + "\\src\\interfaces\\icons\\left.png");
                sub_btn.setIcon(icon);
            }

        } catch (RemoteException ex) {
            System.out.println(ex);
        }
    }
      

       
       
        static int enterd_grup_id;
        public void enter_to_chat(int grup_id) {
            try {
                if (chat.is_subscribed(me.getId())) {
                    app_ui_reset();
                    chat_panel.setVisible(true);
                    
                    enterd_grup_id = grup_id;
                    retrivemsg.start();
                }

            } catch (RemoteException ex) {
                System.out.println(ex);
            }
    }
        
        
        
        
        
        
        int y1 = 13;
        
        
                   
               
        public void load_client_groups() {
            
        List chats = DBManager.getDBM().get_chat_groups();
        client_chat_groups_panel.removeAll();
        
            for (Iterator iterator = chats.iterator(); iterator.hasNext();) {
            Groups next = (Groups) iterator.next();
            
       

            JPanel client_grp_panel = new javax.swing.JPanel();
            client_grp_panel.setBackground(new java.awt.Color(0,51,51));//Subscribe Panel Color
            client_grp_panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            client_grp_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            
            client_grp_panel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    enter_to_chat(next.getId());

                }
            });
            
            boolean is_sub = false;
            
            JLabel subscribe = new javax.swing.JLabel();

            if (is_sub) {
                subscribe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/left.png"))); 
            } else {
                subscribe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/join.png"))); 
            }

            if (next.isStatus()== true) {
                subscribe.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        subscribe_action(next.getId(), subscribe);
                        String m = msg_typer.getText();
  
                        LocalDateTime myDateObj = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String time_now = myDateObj.format(myFormatObj);
                        Message msg = new Message();
                        msg.setDate_time(time_now);
                        String user = me.getUsername();
                        m = "****** " + user  + " has join the chat " + " ******";
                        msg.setMessage(m);
                        try {
                            
                            chat.send_message(msg);
                            System.out.println(msg);
                        } catch (RemoteException ex) {
                            Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
                        }


                    }
                });

            } else {
                subscribe.setEnabled(false);
                subscribe.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }

            JLabel grp_dec = new javax.swing.JLabel();
            grp_dec.setForeground(new java.awt.Color(255, 255, 255));
            grp_dec.setText(next.getDescription());

            JLabel statuts_txt = new javax.swing.JLabel();
            statuts_txt.setBackground(new java.awt.Color(28, 36, 47));
            statuts_txt.setForeground(new java.awt.Color(255, 255, 255));

            JLabel statuts_icon = new javax.swing.JLabel();

            if (next.isStatus()== true) {
                statuts_txt.setText("Live");
                statuts_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/online.png")));
            } else {
                statuts_txt.setText("Offline");
                statuts_icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/offline.png")));
            }

            JLabel grp_name = new javax.swing.JLabel();
            grp_name.setFont(new java.awt.Font("consolas", 1, 13)); 
            grp_name.setForeground(new java.awt.Color(255, 255, 255));
            grp_name.setText(next.getName());

            client_grp_panel.add(subscribe, new org.netbeans.lib.awtextra.AbsoluteConstraints(184, 42, 99, 35));
            client_grp_panel.add(grp_dec, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 42, 160, 35));
            client_grp_panel.add(statuts_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 13, 51, -1));
            client_grp_panel.add(statuts_icon, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 13, 18, 16));
            client_grp_panel.add(grp_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, 160, -1));
            client_chat_groups_panel.add(client_grp_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, y1, 299, 96));

            y1 += 110;

     
        }
        }
        
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        login_panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        textusername = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        textpassword = new javax.swing.JPasswordField();
        disable = new javax.swing.JLabel();
        btnlogin = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        show = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        linkreg = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        text_login_errors = new javax.swing.JLabel();
        underline = new javax.swing.JLabel();
        underline1 = new javax.swing.JLabel();
        register_panel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        textregemail = new javax.swing.JTextField();
        textregpassword = new javax.swing.JPasswordField();
        disable2 = new javax.swing.JLabel();
        btnreg = new javax.swing.JButton();
        show2 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        linklog = new javax.swing.JLabel();
        line1 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        line2 = new javax.swing.JLabel();
        textregnickname = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        line3 = new javax.swing.JLabel();
        text_reg_errors = new javax.swing.JLabel();
        textregusername = new javax.swing.JTextField();
        line4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        signup_profile_pic = new javax.swing.JLabel();
        admin_panel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        admin_group_list = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        img_profile = new javax.swing.JLabel();
        text_admin_username = new javax.swing.JLabel();
        create_group3 = new javax.swing.JLabel();
        create_group = new javax.swing.JLabel();
        link_all_users = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        create_chat_panel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        textgroupname = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        textgroupdescription = new javax.swing.JTextField();
        btn_create_group = new javax.swing.JButton();
        groupnameLine = new javax.swing.JLabel();
        groupdescriptionLine = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        group_create_text = new javax.swing.JLabel();
        img_profile3 = new javax.swing.JLabel();
        text_admin_username2 = new javax.swing.JLabel();
        btn_chat_groups = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        logout1 = new javax.swing.JLabel();
        list_groups_panel = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        client_chat_groups_panel = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        img_profile5 = new javax.swing.JLabel();
        text_user_username = new javax.swing.JLabel();
        edit_profile_link_1 = new javax.swing.JLabel();
        logout2 = new javax.swing.JLabel();
        chat_panel = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        msg_typer = new javax.swing.JTextField();
        send_btn = new javax.swing.JButton();
        msgScrollPane = new javax.swing.JScrollPane();
        chat_background = new javax.swing.JPanel();
        img_profile4 = new javax.swing.JLabel();
        text_user_username1 = new javax.swing.JLabel();
        logout3 = new javax.swing.JLabel();
        edit_profile_panel = new javax.swing.JPanel();
        img_profile_anel = new javax.swing.JPanel();
        text_user_username2 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        logout4 = new javax.swing.JLabel();
        img_profile2 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        edit_password = new javax.swing.JPasswordField();
        disable3 = new javax.swing.JLabel();
        btnreg1 = new javax.swing.JButton();
        show3 = new javax.swing.JLabel();
        passwordLine = new javax.swing.JLabel();
        edit_username = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        nicknameLine = new javax.swing.JLabel();
        update_msg = new javax.swing.JLabel();
        edit_profile_image = new javax.swing.JLabel();
        text_reg_errors2 = new javax.swing.JLabel();
        edit_nickname = new javax.swing.JTextField();
        usernameLine = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        manage_users_panel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        userlist1 = new javax.swing.JComboBox<>();
        remove_user = new javax.swing.JButton();
        user_panel = new javax.swing.JPanel();
        text_delete = new javax.swing.JLabel();
        img_profile6 = new javax.swing.JLabel();
        text_admin_username3 = new javax.swing.JLabel();
        btn_chat_groups1 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        logout6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane1.setBackground(new java.awt.Color(255, 255, 255));

        login_panel.setBackground(new java.awt.Color(255, 255, 255));
        login_panel.setPreferredSize(new java.awt.Dimension(1000, 567));
        login_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                login_panelMouseClicked(evt);
            }
        });
        login_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(504, 567));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/Login-bro.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        login_panel.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 620));

        jPanel2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(868, 567));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Password");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, -1, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("____________________________________________");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 260, -1, 35));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_user_20px_1.png"))); // NOI18N
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, 56, 56));

        textusername.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        textusername.setForeground(new java.awt.Color(255, 255, 255));
        textusername.setBorder(null);
        textusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textusernameActionPerformed(evt);
            }
        });
        jPanel2.add(textusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, 220, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Password");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, -1, -1));

        textpassword.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        textpassword.setForeground(new java.awt.Color(255, 255, 255));
        textpassword.setBorder(null);
        textpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textpasswordActionPerformed(evt);
            }
        });
        jPanel2.add(textpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 220, 30));

        disable.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        disable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        disable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_invisible_20px_1.png"))); // NOI18N
        disable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                disableMouseClicked(evt);
            }
        });
        jPanel2.add(disable, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, 56, 43));

        btnlogin.setBackground(new java.awt.Color(0, 102, 102));
        btnlogin.setFont(new java.awt.Font("Consolas", 1, 26)); // NOI18N
        btnlogin.setForeground(new java.awt.Color(0, 204, 204));
        btnlogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_login.png"))); // NOI18N
        btnlogin.setBorder(null);
        btnlogin.setBorderPainted(false);
        btnlogin.setContentAreaFilled(false);
        btnlogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnlogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnloginMouseClicked(evt);
            }
        });
        btnlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnloginActionPerformed(evt);
            }
        });
        jPanel2.add(btnlogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, 160, 60));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Username");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 390, -1, -1));

        show.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        show.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        show.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_eye_20px_1.png"))); // NOI18N
        show.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMouseClicked(evt);
            }
        });
        jPanel2.add(show, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, 56, 43));

        jLabel9.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Don't you have an account?");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 390, -1, -1));

        linkreg.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        linkreg.setForeground(new java.awt.Color(0, 204, 255));
        linkreg.setText("Register");
        linkreg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        linkreg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                linkregMouseClicked(evt);
            }
        });
        jPanel2.add(linkreg, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 390, -1, -1));

        jLabel13.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Username");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, -1, -1));

        text_login_errors.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        text_login_errors.setForeground(new java.awt.Color(255, 0, 51));
        text_login_errors.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(text_login_errors, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 360, 270, 20));

        underline.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        underline.setForeground(new java.awt.Color(255, 255, 255));
        underline.setText("______________________");
        jPanel2.add(underline, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 250, 230, -1));

        underline1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        underline1.setForeground(new java.awt.Color(255, 255, 255));
        underline1.setText("______________________");
        jPanel2.add(underline1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 230, -1));

        login_panel.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 60, 450, 440));

        register_panel.setBackground(new java.awt.Color(255, 255, 255));
        register_panel.setMinimumSize(new java.awt.Dimension(970, 500));
        register_panel.setPreferredSize(new java.awt.Dimension(1016, 585));
        register_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(520, 500));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/register.png"))); // NOI18N
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-160, 20, 590, 520));

        register_panel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 460, 550));

        jPanel6.setBackground(new java.awt.Color(0, 51, 102));
        jPanel6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Profile Image");
        jPanel6.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, -1, -1));

        textregemail.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textregemail.setForeground(new java.awt.Color(255, 255, 255));
        textregemail.setBorder(null);
        textregemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textregemailActionPerformed(evt);
            }
        });
        jPanel6.add(textregemail, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 240, 30));

        textregpassword.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textregpassword.setForeground(new java.awt.Color(255, 255, 255));
        textregpassword.setBorder(null);
        textregpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textregpasswordActionPerformed(evt);
            }
        });
        jPanel6.add(textregpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 310, 240, 34));

        disable2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        disable2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        disable2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_invisible_20px_1.png"))); // NOI18N
        disable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                disable2MouseClicked(evt);
            }
        });
        jPanel6.add(disable2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 30, 30));

        btnreg.setBackground(new java.awt.Color(0, 102, 102));
        btnreg.setFont(new java.awt.Font("Consolas", 1, 26)); // NOI18N
        btnreg.setForeground(new java.awt.Color(0, 204, 204));
        btnreg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_register.png"))); // NOI18N
        btnreg.setBorder(null);
        btnreg.setContentAreaFilled(false);
        btnreg.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnreg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnregMouseClicked(evt);
            }
        });
        btnreg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnregActionPerformed(evt);
            }
        });
        jPanel6.add(btnreg, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 440, 160, 50));

        show2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        show2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        show2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_eye_20px_1.png"))); // NOI18N
        show2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                show2MouseClicked(evt);
            }
        });
        jPanel6.add(show2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 30, 30));

        jLabel31.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Do You have an account?");
        jPanel6.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 500, -1, -1));

        linklog.setBackground(new java.awt.Color(0, 204, 204));
        linklog.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        linklog.setForeground(new java.awt.Color(0, 204, 255));
        linklog.setText("Login");
        linklog.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        linklog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                linklogMouseClicked(evt);
            }
        });
        jPanel6.add(linklog, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 500, -1, -1));

        line1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        line1.setForeground(new java.awt.Color(255, 255, 255));
        line1.setText("_______________________");
        jPanel6.add(line1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 320, 240, -1));

        jLabel33.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Email");
        jPanel6.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, -1, -1));

        line2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        line2.setForeground(new java.awt.Color(255, 255, 255));
        line2.setText("_______________________");
        jPanel6.add(line2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 240, -1));

        textregnickname.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textregnickname.setForeground(new java.awt.Color(255, 255, 255));
        textregnickname.setBorder(null);
        textregnickname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textregnicknameActionPerformed(evt);
            }
        });
        jPanel6.add(textregnickname, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 240, 240, 30));

        jLabel15.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Nickname");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, -1, -1));

        line3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        line3.setForeground(new java.awt.Color(255, 255, 255));
        line3.setText("_______________________");
        jPanel6.add(line3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 240, -1));

        text_reg_errors.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        text_reg_errors.setForeground(new java.awt.Color(255, 0, 51));
        text_reg_errors.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(text_reg_errors, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 420, 270, 20));

        textregusername.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textregusername.setForeground(new java.awt.Color(255, 255, 255));
        textregusername.setBorder(null);
        textregusername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textregusernameActionPerformed(evt);
            }
        });
        jPanel6.add(textregusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 240, 30));

        line4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        line4.setForeground(new java.awt.Color(255, 255, 255));
        line4.setText("_______________________");
        jPanel6.add(line4, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 240, -1));

        jLabel20.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Username");
        jPanel6.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, -1, -1));

        jLabel32.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Password");
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 280, -1, -1));

        signup_profile_pic.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        signup_profile_pic.setForeground(new java.awt.Color(255, 255, 255));
        signup_profile_pic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        signup_profile_pic.setText("Choose");
        signup_profile_pic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        signup_profile_pic.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signup_profile_pic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signup_profile_picMouseClicked(evt);
            }
        });
        jPanel6.add(signup_profile_pic, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 130, 30));

        register_panel.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 550, 550));

        admin_panel.setBackground(new java.awt.Color(153, 204, 255));
        admin_panel.setPreferredSize(new java.awt.Dimension(1000, 567));
        admin_panel.setRequestFocusEnabled(false);

        jPanel3.setBackground(new java.awt.Color(0, 51, 102));
        jPanel3.setPreferredSize(new java.awt.Dimension(230, 567));

        jLabel18.setFont(new java.awt.Font("Consolas", 1, 36)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("GROUPS");

        jScrollPane1.setForeground(new java.awt.Color(0, 51, 255));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setToolTipText("");

        admin_group_list.setBackground(new java.awt.Color(0, 102, 153));
        admin_group_list.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane1.setViewportView(admin_group_list);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel18))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(153, 204, 255));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(590, 567));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        img_profile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user.png"))); // NOI18N
        jPanel4.add(img_profile, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 80));

        text_admin_username.setBackground(new java.awt.Color(0, 0, 0));
        text_admin_username.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        text_admin_username.setText("Welcome Admin");
        jPanel4.add(text_admin_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, 20));

        create_group3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        create_group3.setForeground(new java.awt.Color(255, 255, 255));
        create_group3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_groups.png"))); // NOI18N
        create_group3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        create_group3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_group3MouseClicked(evt);
            }
        });
        jPanel4.add(create_group3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 150, 160));

        create_group.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        create_group.setForeground(new java.awt.Color(255, 255, 255));
        create_group.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_new-group.png"))); // NOI18N
        create_group.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        create_group.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_groupMouseClicked(evt);
            }
        });
        jPanel4.add(create_group, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 140, 150, 160));

        link_all_users.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        link_all_users.setForeground(new java.awt.Color(255, 255, 255));
        link_all_users.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_users.png"))); // NOI18N
        link_all_users.setText("USERS");
        link_all_users.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        link_all_users.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                link_all_usersMouseClicked(evt);
            }
        });
        jPanel4.add(link_all_users, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 150, 160));

        logout.setBackground(new java.awt.Color(0, 102, 102));
        logout.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logout.setForeground(new java.awt.Color(0, 204, 204));
        logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_logout.png"))); // NOI18N
        logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });
        jPanel4.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 480, 160, 80));

        javax.swing.GroupLayout admin_panelLayout = new javax.swing.GroupLayout(admin_panel);
        admin_panel.setLayout(admin_panelLayout);
        admin_panelLayout.setHorizontalGroup(
            admin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(admin_panelLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        admin_panelLayout.setVerticalGroup(
            admin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(admin_panelLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        create_chat_panel.setBackground(new java.awt.Color(255, 255, 255));
        create_chat_panel.setPreferredSize(new java.awt.Dimension(1000, 567));

        jPanel7.setBackground(new java.awt.Color(0, 51, 102));
        jPanel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel55.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Group Name");

        textgroupname.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textgroupname.setForeground(new java.awt.Color(255, 255, 255));
        textgroupname.setBorder(null);
        textgroupname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textgroupnameActionPerformed(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Group Description");

        textgroupdescription.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        textgroupdescription.setForeground(new java.awt.Color(255, 255, 255));
        textgroupdescription.setBorder(null);
        textgroupdescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textgroupdescriptionActionPerformed(evt);
            }
        });

        btn_create_group.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_create_group.setForeground(new java.awt.Color(0, 153, 255));
        btn_create_group.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_create.png"))); // NOI18N
        btn_create_group.setBorder(null);
        btn_create_group.setContentAreaFilled(false);
        btn_create_group.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_create_group.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_create_groupMouseClicked(evt);
            }
        });
        btn_create_group.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_create_groupActionPerformed(evt);
            }
        });

        groupnameLine.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        groupnameLine.setForeground(new java.awt.Color(255, 255, 255));
        groupnameLine.setText("________________________________________________");

        groupdescriptionLine.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        groupdescriptionLine.setForeground(new java.awt.Color(255, 255, 255));
        groupdescriptionLine.setText("________________________________________________");

        jLabel42.setFont(new java.awt.Font("Consolas", 1, 36)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("NEW CHAT GROUP");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groupnameLine, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel55)
                            .addComponent(textgroupname)
                            .addComponent(jLabel52)
                            .addComponent(btn_create_group)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(textgroupdescription, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(groupdescriptionLine, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 56, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupnameLine)
                    .addComponent(textgroupname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(112, 112, 112)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupdescriptionLine)
                    .addComponent(textgroupdescription, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(btn_create_group, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel8.setBackground(new java.awt.Color(153, 204, 255));
        jPanel8.setForeground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(590, 567));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        group_create_text.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        group_create_text.setForeground(new java.awt.Color(255, 0, 51));
        group_create_text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel8.add(group_create_text, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 370, 270, 20));

        img_profile3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user.png"))); // NOI18N
        jPanel8.add(img_profile3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 80));

        text_admin_username2.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        text_admin_username2.setText("Welcome Admin");
        jPanel8.add(text_admin_username2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, 20));

        btn_chat_groups.setBackground(new java.awt.Color(153, 51, 255));
        btn_chat_groups.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn_chat_groups.setForeground(new java.awt.Color(255, 255, 255));
        btn_chat_groups.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_groups.png"))); // NOI18N
        btn_chat_groups.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_chat_groups.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_chat_groupsMouseClicked(evt);
            }
        });
        jPanel8.add(btn_chat_groups, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, -1, -1));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_new-group.png"))); // NOI18N
        jLabel38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel38MouseClicked(evt);
            }
        });
        jPanel8.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, -1, -1));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_users.png"))); // NOI18N
        jLabel40.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel40MouseClicked(evt);
            }
        });
        jPanel8.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 190, -1, -1));

        logout1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logout1.setForeground(new java.awt.Color(255, 255, 255));
        logout1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_logout.png"))); // NOI18N
        logout1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout1MouseClicked(evt);
            }
        });
        jPanel8.add(logout1, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 500, 150, 60));

        javax.swing.GroupLayout create_chat_panelLayout = new javax.swing.GroupLayout(create_chat_panel);
        create_chat_panel.setLayout(create_chat_panelLayout);
        create_chat_panelLayout.setHorizontalGroup(
            create_chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, create_chat_panelLayout.createSequentialGroup()
                .addGap(0, 373, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(create_chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(create_chat_panelLayout.createSequentialGroup()
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 627, Short.MAX_VALUE)))
        );
        create_chat_panelLayout.setVerticalGroup(
            create_chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(create_chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        list_groups_panel.setBackground(new java.awt.Color(153, 204, 255));
        list_groups_panel.setPreferredSize(new java.awt.Dimension(1000, 567));
        list_groups_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_groups_panelMouseClicked(evt);
            }
        });

        jPanel13.setBackground(new java.awt.Color(0, 51, 102));
        jPanel13.setPreferredSize(new java.awt.Dimension(230, 567));

        jScrollPane2.setForeground(new java.awt.Color(0, 51, 255));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setToolTipText("");

        client_chat_groups_panel.setBackground(new java.awt.Color(0, 102, 153));
        client_chat_groups_panel.setForeground(new java.awt.Color(0, 51, 51));
        client_chat_groups_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane2.setViewportView(client_chat_groups_panel);

        jLabel63.setFont(new java.awt.Font("Consolas", 1, 36)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("GROUPS");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel63))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(153, 204, 255));
        jPanel14.setForeground(new java.awt.Color(255, 255, 255));
        jPanel14.setPreferredSize(new java.awt.Dimension(590, 567));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        img_profile5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user.png"))); // NOI18N
        jPanel14.add(img_profile5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 80));

        text_user_username.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        text_user_username.setForeground(new java.awt.Color(255, 255, 255));
        text_user_username.setText("Welcome User");
        jPanel14.add(text_user_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, 20));

        edit_profile_link_1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        edit_profile_link_1.setForeground(new java.awt.Color(255, 255, 255));
        edit_profile_link_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_edit-profile.png"))); // NOI18N
        edit_profile_link_1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        edit_profile_link_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edit_profile_link_1MouseClicked(evt);
            }
        });
        jPanel14.add(edit_profile_link_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 200, -1, -1));

        logout2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logout2.setForeground(new java.awt.Color(255, 255, 255));
        logout2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_logout.png"))); // NOI18N
        logout2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout2MouseClicked(evt);
            }
        });
        jPanel14.add(logout2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 480, -1, -1));

        javax.swing.GroupLayout list_groups_panelLayout = new javax.swing.GroupLayout(list_groups_panel);
        list_groups_panel.setLayout(list_groups_panelLayout);
        list_groups_panelLayout.setHorizontalGroup(
            list_groups_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(list_groups_panelLayout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        list_groups_panelLayout.setVerticalGroup(
            list_groups_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
            .addGroup(list_groups_panelLayout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        chat_panel.setBackground(new java.awt.Color(255, 255, 255));
        chat_panel.setPreferredSize(new java.awt.Dimension(1000, 567));
        chat_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                chat_panelMouseClicked(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(153, 204, 255));
        jPanel12.setForeground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(590, 567));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        msg_typer.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        msg_typer.setForeground(new java.awt.Color(255, 255, 255));
        msg_typer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        msg_typer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                msg_typerActionPerformed(evt);
            }
        });
        msg_typer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                msg_typerKeyPressed(evt);
            }
        });
        jPanel12.add(msg_typer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 890, 40));

        send_btn.setBackground(new java.awt.Color(153, 51, 255));
        send_btn.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        send_btn.setForeground(new java.awt.Color(0, 153, 255));
        send_btn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/sendPlane.png"))); // NOI18N
        send_btn.setBorder(null);
        send_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        send_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                send_btnMouseClicked(evt);
            }
        });
        send_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_btnActionPerformed(evt);
            }
        });
        jPanel12.add(send_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 520, 70, 40));

        msgScrollPane.setBackground(new java.awt.Color(102, 204, 255));

        chat_background.setBackground(new java.awt.Color(255, 255, 255));
        chat_background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        msgScrollPane.setViewportView(chat_background);

        jPanel12.add(msgScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 980, 410));

        img_profile4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user.png"))); // NOI18N
        jPanel12.add(img_profile4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 80, 80));

        text_user_username1.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        text_user_username1.setText(" User");
        jPanel12.add(text_user_username1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, 20));

        logout3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logout3.setForeground(new java.awt.Color(21, 215, 152));
        logout3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/logoutSmall.png"))); // NOI18N
        logout3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout3MouseClicked(evt);
            }
        });
        jPanel12.add(logout3, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 30, 50, 50));

        javax.swing.GroupLayout chat_panelLayout = new javax.swing.GroupLayout(chat_panel);
        chat_panel.setLayout(chat_panelLayout);
        chat_panelLayout.setHorizontalGroup(
            chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1166, Short.MAX_VALUE)
        );
        chat_panelLayout.setVerticalGroup(
            chat_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chat_panelLayout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        edit_profile_panel.setBackground(new java.awt.Color(102, 204, 255));
        edit_profile_panel.setPreferredSize(new java.awt.Dimension(1000, 567));
        edit_profile_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edit_profile_panelMouseClicked(evt);
            }
        });

        img_profile_anel.setBackground(new java.awt.Color(153, 204, 255));
        img_profile_anel.setPreferredSize(new java.awt.Dimension(230, 567));
        img_profile_anel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                img_profile_anelMouseClicked(evt);
            }
        });

        text_user_username2.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        text_user_username2.setText("Welcome User");

        jLabel58.setBackground(new java.awt.Color(153, 51, 255));
        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_join-a-group.png"))); // NOI18N
        jLabel58.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel58MouseClicked(evt);
            }
        });

        logout4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logout4.setForeground(new java.awt.Color(255, 255, 255));
        logout4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_logout.png"))); // NOI18N
        logout4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout4MouseClicked(evt);
            }
        });

        img_profile2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user.png"))); // NOI18N

        javax.swing.GroupLayout img_profile_anelLayout = new javax.swing.GroupLayout(img_profile_anel);
        img_profile_anel.setLayout(img_profile_anelLayout);
        img_profile_anelLayout.setHorizontalGroup(
            img_profile_anelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(img_profile_anelLayout.createSequentialGroup()
                .addGroup(img_profile_anelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(img_profile_anelLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(text_user_username2))
                    .addGroup(img_profile_anelLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(img_profile2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(img_profile_anelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(img_profile_anelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58)
                            .addComponent(logout4))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        img_profile_anelLayout.setVerticalGroup(
            img_profile_anelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(img_profile_anelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(img_profile2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(text_user_username2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(jLabel58)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logout4)
                .addGap(46, 46, 46))
        );

        jPanel19.setBackground(new java.awt.Color(0, 51, 102));
        jPanel19.setForeground(new java.awt.Color(255, 255, 255));
        jPanel19.setPreferredSize(new java.awt.Dimension(590, 567));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Consolas", 1, 36)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("EDIT PROFILE");
        jPanel19.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, -1, -1));

        jLabel57.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Password");
        jPanel19.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 320, -1, -1));

        edit_password.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edit_password.setForeground(new java.awt.Color(255, 255, 255));
        edit_password.setBorder(null);
        edit_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_passwordActionPerformed(evt);
            }
        });
        jPanel19.add(edit_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 350, 308, 34));

        disable3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        disable3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        disable3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_invisible_20px_1.png"))); // NOI18N
        disable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                disable3MouseClicked(evt);
            }
        });
        jPanel19.add(disable3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 56, 43));

        btnreg1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        btnreg1.setForeground(new java.awt.Color(0, 153, 255));
        btnreg1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_update.png"))); // NOI18N
        btnreg1.setBorder(null);
        btnreg1.setContentAreaFilled(false);
        btnreg1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnreg1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnreg1MouseClicked(evt);
            }
        });
        btnreg1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnreg1ActionPerformed(evt);
            }
        });
        jPanel19.add(btnreg1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 410, 150, 50));

        show3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        show3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        show3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/icons8_eye_20px_1.png"))); // NOI18N
        show3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                show3MouseClicked(evt);
            }
        });
        jPanel19.add(show3, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 56, 43));

        passwordLine.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        passwordLine.setForeground(new java.awt.Color(255, 255, 255));
        passwordLine.setText("_______________________________");
        jPanel19.add(passwordLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 360, -1, -1));

        edit_username.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edit_username.setForeground(new java.awt.Color(255, 255, 255));
        edit_username.setBorder(null);
        edit_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_usernameActionPerformed(evt);
            }
        });
        jPanel19.add(edit_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 180, 308, 30));

        jLabel21.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Nickname");
        jPanel19.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 230, -1, -1));

        nicknameLine.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        nicknameLine.setForeground(new java.awt.Color(255, 255, 255));
        nicknameLine.setText("_______________________________");
        jPanel19.add(nicknameLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 280, -1, -1));

        update_msg.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        update_msg.setForeground(new java.awt.Color(255, 0, 51));
        update_msg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel19.add(update_msg, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 500, 270, 20));

        edit_profile_image.setBackground(new java.awt.Color(255, 255, 255));
        edit_profile_image.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        edit_profile_image.setForeground(new java.awt.Color(255, 255, 255));
        edit_profile_image.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        edit_profile_image.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        edit_profile_image.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        edit_profile_image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                edit_profile_imageMouseClicked(evt);
            }
        });
        jPanel19.add(edit_profile_image, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 300, 310));

        text_reg_errors2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        text_reg_errors2.setForeground(new java.awt.Color(255, 0, 51));
        text_reg_errors2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel19.add(text_reg_errors2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 470, 270, 20));

        edit_nickname.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        edit_nickname.setForeground(new java.awt.Color(255, 255, 255));
        edit_nickname.setBorder(null);
        edit_nickname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edit_nicknameActionPerformed(evt);
            }
        });
        jPanel19.add(edit_nickname, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 270, 308, 30));

        usernameLine.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        usernameLine.setForeground(new java.awt.Color(255, 255, 255));
        usernameLine.setText("_______________________________");
        jPanel19.add(usernameLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 190, -1, -1));

        jLabel24.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Username");
        jPanel19.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 150, -1, -1));

        javax.swing.GroupLayout edit_profile_panelLayout = new javax.swing.GroupLayout(edit_profile_panel);
        edit_profile_panel.setLayout(edit_profile_panelLayout);
        edit_profile_panelLayout.setHorizontalGroup(
            edit_profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edit_profile_panelLayout.createSequentialGroup()
                .addComponent(img_profile_anel, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 942, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        edit_profile_panelLayout.setVerticalGroup(
            edit_profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(img_profile_anel, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
            .addGroup(edit_profile_panelLayout.createSequentialGroup()
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        manage_users_panel.setBackground(new java.awt.Color(255, 255, 255));
        manage_users_panel.setPreferredSize(new java.awt.Dimension(1000, 567));
        manage_users_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(0, 51, 102));
        jPanel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel44.setFont(new java.awt.Font("Consolas", 1, 36)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("MANAGE USERS");

        jLabel59.setFont(new java.awt.Font("Consolas", 1, 20)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("DELETE USER");

        userlist1.setEditable(true);
        userlist1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        userlist1.setBorder(null);
        userlist1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                userlist1ItemStateChanged(evt);
            }
        });
        userlist1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userlist1ActionPerformed(evt);
            }
        });

        remove_user.setBackground(new java.awt.Color(51, 153, 255));
        remove_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_delete.png"))); // NOI18N
        remove_user.setContentAreaFilled(false);
        remove_user.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        remove_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                remove_userMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel44))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel59))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(userlist1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(remove_user, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel44)
                .addGap(150, 150, 150)
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userlist1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                .addComponent(remove_user)
                .addGap(78, 78, 78))
        );

        manage_users_panel.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 570));

        user_panel.setBackground(new java.awt.Color(153, 204, 255));
        user_panel.setForeground(new java.awt.Color(255, 255, 255));
        user_panel.setPreferredSize(new java.awt.Dimension(590, 567));
        user_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        text_delete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        text_delete.setForeground(new java.awt.Color(255, 0, 51));
        text_delete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        user_panel.add(text_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 420, 270, 20));

        img_profile6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/interfaces/icons/user.png"))); // NOI18N
        user_panel.add(img_profile6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 80));

        text_admin_username3.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        text_admin_username3.setText("Welcome Admin");
        user_panel.add(text_admin_username3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, -1, 20));

        btn_chat_groups1.setBackground(new java.awt.Color(153, 51, 255));
        btn_chat_groups1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btn_chat_groups1.setForeground(new java.awt.Color(255, 255, 255));
        btn_chat_groups1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_groups.png"))); // NOI18N
        btn_chat_groups1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_chat_groups1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_chat_groups1MouseClicked(evt);
            }
        });
        user_panel.add(btn_chat_groups1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, -1, -1));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_new-group.png"))); // NOI18N
        jLabel39.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel39MouseClicked(evt);
            }
        });
        user_panel.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, -1, -1));

        logout6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        logout6.setForeground(new java.awt.Color(255, 255, 255));
        logout6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Interfaces/icons/button_logout.png"))); // NOI18N
        logout6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        logout6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logout6MouseClicked(evt);
            }
        });
        user_panel.add(logout6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 490, 150, 50));

        manage_users_panel.add(user_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 630, 570));

        jLayeredPane1.setLayer(login_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(register_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(admin_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(create_chat_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(list_groups_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(chat_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(edit_profile_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(manage_users_panel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(register_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(admin_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1172, Short.MAX_VALUE)
                    .addGap(22, 22, 22)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(create_chat_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(194, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(list_groups_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
                    .addGap(24, 24, 24)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(chat_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1166, Short.MAX_VALUE)
                    .addGap(28, 28, 28)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(edit_profile_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(194, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 100, Short.MAX_VALUE)
                    .addComponent(manage_users_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 100, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(login_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(13, Short.MAX_VALUE)
                    .addComponent(register_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(admin_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addGap(23, 23, 23)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(create_chat_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(42, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(list_groups_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 562, Short.MAX_VALUE)
                    .addGap(52, 52, 52)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(chat_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 585, Short.MAX_VALUE)
                    .addGap(29, 29, 29)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(edit_profile_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(42, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(0, 26, Short.MAX_VALUE)
                    .addComponent(manage_users_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 27, Short.MAX_VALUE)))
        );

        getContentPane().add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1018, 585));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void signup_profile_picMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signup_profile_picMouseClicked
        JFileChooser chooser = new JFileChooser(); //open image file file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"); //set image type filter
        chooser.setFileFilter(filter); //filter
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) { //if image selected
            File file = chooser.getSelectedFile(); //get selected file
            String strfilepath = file.getAbsolutePath(); //get abs path

            try {
                ImageIcon icon = new ImageIcon(strfilepath); //string image path open as a image icon
                ImageIcon iconresized = new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT)); //resize image icon fit for profile icon label
                signup_profile_pic.setText(null); // remove label text
                signup_profile_pic.setIcon(iconresized); //set seleted image to profile icon label 


            } catch (Exception e) {
                System.out.println("Exception occurred : " + e.getMessage());
            }
        }
    }//GEN-LAST:event_signup_profile_picMouseClicked

    private void textregemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textregemailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textregemailActionPerformed

    private void textregpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textregpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textregpasswordActionPerformed

    private void disable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_disable2MouseClicked
        // TODO add your handling code here:
        textregpassword.setEchoChar((char)0);
        disable2.setVisible(false);
        disable2.setEnabled(false);
        show2.setVisible(true);
        show2.setEnabled(true);
    }//GEN-LAST:event_disable2MouseClicked

    private void btnregActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnregActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnregActionPerformed

    private void show2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_show2MouseClicked
        // TODO add your handling code here:
        textregpassword.setEchoChar((char)8226);
        disable2.setVisible(true);
        disable2.setEnabled(true);
        show2.setVisible(false);
        show2.setEnabled(false);
    }//GEN-LAST:event_show2MouseClicked

    private void textregnicknameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textregnicknameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textregnicknameActionPerformed

    private void textgroupdescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textgroupdescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textgroupdescriptionActionPerformed

    private void textgroupnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textgroupnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textgroupnameActionPerformed

    private void btn_create_groupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_create_groupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_create_groupActionPerformed

    private void msg_typerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_msg_typerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_msg_typerActionPerformed

    private void send_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_send_btnActionPerformed

    private void linklogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_linklogMouseClicked
        // TODO add your handling code here:
        register_panel.setVisible(false);
        login_panel.setVisible(true);

    }//GEN-LAST:event_linklogMouseClicked

    private void login_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_login_panelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_login_panelMouseClicked

    private void btnregMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnregMouseClicked
        // TODO add your handling code here:
        
        //get data from text boxes
        String email = textregemail.getText();
        String username = textregusername.getText();
        String nickname = textregnickname.getText();
        String password = textregpassword.getText();

        //Checking Errors
        ArrayList<String> error = validateform(email, username, password);

        if (error.isEmpty() == false) {
            text_reg_errors.setText(error.get(0));
        } else {
            text_reg_errors.setText(null);
            
            byte[] img = null;
            ImageIcon avatar = (ImageIcon) signup_profile_pic.getIcon();
            if (avatar != null) {
                try {
                    
                    BufferedImage bImage = ImageIconToBufferedImage(avatar);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", bos);
                    img = bos.toByteArray();

                } catch (IOException ex) {
                    Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (DBManager.getDBM().insert(img, email, username,nickname, password)) {
                text_reg_errors.setText("You Registered Successfully");
            }

        }
    }//GEN-LAST:event_btnregMouseClicked

    private void edit_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edit_passwordActionPerformed

    private void disable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_disable3MouseClicked
        // TODO add your handling code here:
        edit_password.setEchoChar((char)0);
        disable3.setVisible(false);
        disable3.setEnabled(false);
        show3.setVisible(true);
        show3.setEnabled(true);
    }//GEN-LAST:event_disable3MouseClicked

    private void btnreg1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnreg1MouseClicked
        // TODO add your handling code here:
  
        String username = edit_username.getText().trim();
        String nickname = edit_nickname.getText().trim();
        String password = edit_password.getText().trim();
        
            byte[] img = null;
            ImageIcon avatar = (ImageIcon) edit_profile_image.getIcon();
            if (avatar != null) {
                try {
                 
                    BufferedImage bImage = ImageIconToBufferedImage(avatar);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ImageIO.write(bImage, "jpg", bos);
                    img = bos.toByteArray();

                } catch (IOException ex) {
                    Logger.getLogger(AppLayout.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        
        DBManager.getDBM().update(img, username, nickname,password, id);
        update_msg.setText("Sucessfully Updated..");
        img_profile2.setIcon(avatar);
        img_profile2.setIcon(avatar);
        
    }//GEN-LAST:event_btnreg1MouseClicked

    private void btnreg1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnreg1ActionPerformed
        // TODO add your handling code here:
        

    }//GEN-LAST:event_btnreg1ActionPerformed

    private void show3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_show3MouseClicked
        // TODO add your handling code here:
        
        edit_password.setEchoChar((char)8226);
        disable3.setVisible(true);
        disable3.setEnabled(true);
        show3.setVisible(false);
        show3.setEnabled(false);
    }//GEN-LAST:event_show3MouseClicked

    private void edit_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edit_usernameActionPerformed

    private void edit_profile_imageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edit_profile_imageMouseClicked
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser(); //open image file file
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png"); //set image type filter
        chooser.setFileFilter(filter); //filter
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) { //if image selected
            File file = chooser.getSelectedFile(); //get selected file
            String strfilepath = file.getAbsolutePath(); //get abs path

            try {
                ImageIcon icon = new ImageIcon(strfilepath); //string image path open as a image icon
                ImageIcon iconresized = new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT)); //resize image icon fit for profile icon label
                edit_profile_image.setText(null); // remove label text
                edit_profile_image.setIcon(iconresized); //set seleted image to profile icon label 


            } catch (Exception e) {
                System.out.println("Exception occurred : " + e.getMessage());
            }
        }
    }//GEN-LAST:event_edit_profile_imageMouseClicked

    private void edit_profile_link_1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edit_profile_link_1MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        edit_profile_panel.setVisible(true);
    }//GEN-LAST:event_edit_profile_link_1MouseClicked

    private void btn_create_groupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_create_groupMouseClicked
        // TODO add your handling code here:
        
        String name = textgroupname.getText();
        String desc = textgroupdescription.getText();

        if (DBManager.getDBM().create_chat_group(name, desc)) {
            group_create_text.setText("Group created sucessfully");
        } else {
            group_create_text.setText("Group can not create!");
        }
    }//GEN-LAST:event_btn_create_groupMouseClicked

    private void textregusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textregusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textregusernameActionPerformed

    private void edit_nicknameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edit_nicknameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edit_nicknameActionPerformed

    private void logout1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout1MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout1MouseClicked

    private void list_groups_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_groups_panelMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_list_groups_panelMouseClicked

    private void chat_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chat_panelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_chat_panelMouseClicked

    private void logout2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout2MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout2MouseClicked

    private void logout3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout3MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout3MouseClicked

    private void edit_profile_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_edit_profile_panelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_edit_profile_panelMouseClicked

    private void img_profile_anelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_img_profile_anelMouseClicked
        // TODO add your handling code here:
    
    }//GEN-LAST:event_img_profile_anelMouseClicked

    private void logout4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout4MouseClicked
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout4MouseClicked

    private void msg_typerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_msg_typerKeyPressed
        // TODO add your handling code here:
           if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.sender();
        }
    }//GEN-LAST:event_msg_typerKeyPressed

    private void send_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_send_btnMouseClicked
        // TODO add your handling code here:
            this.sender();
            
        msgScrollPane.getVerticalScrollBar().addAdjustmentListener((AdjustmentEvent e) -> {
            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });
        
        
        
        
    }//GEN-LAST:event_send_btnMouseClicked

    private void btn_chat_groupsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_chat_groupsMouseClicked
        // TODO add your handling code here:
       app_ui_reset();
       admin_panel.setVisible(true);
    }//GEN-LAST:event_btn_chat_groupsMouseClicked

    private void jLabel38MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel38MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel38MouseClicked

    private void create_group3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_group3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_create_group3MouseClicked

    private void link_all_usersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_link_all_usersMouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        manage_users_panel.setVisible(true);
    }//GEN-LAST:event_link_all_usersMouseClicked

    private void create_groupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_groupMouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        create_chat_panel.setVisible(true);
    }//GEN-LAST:event_create_groupMouseClicked

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logoutMouseClicked

    private void jLabel58MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel58MouseClicked
        // TODO add your handling code here:
         app_ui_reset();
         list_groups_panel.setVisible(true);
    }//GEN-LAST:event_jLabel58MouseClicked

    private void jLabel39MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel39MouseClicked
        // TODO add your handling code here:
         app_ui_reset();
         create_chat_panel.setVisible(true);
    }//GEN-LAST:event_jLabel39MouseClicked

    private void btn_chat_groups1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_chat_groups1MouseClicked
        // TODO add your handling code here:
         app_ui_reset();
         admin_panel.setVisible(true);
    }//GEN-LAST:event_btn_chat_groups1MouseClicked

    private void logout6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logout6MouseClicked
        // TODO add your handling code here:
        app_ui_reset();
        login_panel.setVisible(true);
    }//GEN-LAST:event_logout6MouseClicked

    private void userlist1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userlist1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userlist1ActionPerformed

    private void remove_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_remove_userMouseClicked
       
       String del_user = (String) userlist1.getSelectedItem();
       String del_user_id = del_user.split("-")[0];
       
       int user_id = Integer.parseInt(del_user_id);
       DBManager.getDBM().delete_user(user_id);
       text_delete.setText("User Successfully Deleted ");
       userlist1.removeItem(userlist1.getSelectedItem());
   
    }//GEN-LAST:event_remove_userMouseClicked

    private void jLabel40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel40MouseClicked
        // TODO add your handling code here:
         app_ui_reset();
        manage_users_panel.setVisible(true);
    }//GEN-LAST:event_jLabel40MouseClicked

    private void userlist1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_userlist1ItemStateChanged

    }//GEN-LAST:event_userlist1ItemStateChanged

    private void linkregMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_linkregMouseClicked
        // TODO add your handling code here:
        login_panel.setVisible(false);
        register_panel.setVisible(true);
    }//GEN-LAST:event_linkregMouseClicked

    private void showMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showMouseClicked
        // TODO add your handling code here:
        textpassword.setEchoChar((char)8226);
        disable.setVisible(true);
        disable.setEnabled(true);
        show.setVisible(false);
        show.setEnabled(false);
    }//GEN-LAST:event_showMouseClicked

    private void btnloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnloginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnloginActionPerformed

    private void btnloginMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnloginMouseClicked
        // TODO add your handling code here:
        String user_name = textusername.getText();
        String user_pwd = textpassword.getText();

        ArrayList<String> error = validatelogin(user_name, user_pwd);

        if (error.isEmpty() == false) {
            text_login_errors.setText(error.get(0));
        } else {

            List data = DBManager.getDBM().loginHandler(user_name, user_pwd);
            Iterator i = data.iterator();
            if (i.hasNext()) {
                Users user = (Users) i.next();

                String email = user.getEmail();
                String username = user.getUsername();
                String nickname = user.getNickname();
                String password = user.getPassword();
                byte[] profile_image = user.getProfileImage();
                id = user.getId();

                edit_username.setText(username);
                edit_nickname.setText(nickname);
                edit_password.setText(password);

                if(profile_image != null){

                    ImageIcon imageicon = toImageIcon(profile_image);

                    ImageIcon iconresized1 = new ImageIcon(imageicon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
                    img_profile.setIcon(iconresized1);
                    img_profile2.setIcon(iconresized1);
                    img_profile3.setIcon(iconresized1);
                    img_profile4.setIcon(iconresized1);
                    img_profile5.setIcon(iconresized1);
                    img_profile6.setIcon(iconresized1);

                    ImageIcon iconresized2 = new ImageIcon(imageicon.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
                    edit_profile_image.setIcon(iconresized2);
                }

                if (user.getUserType().equalsIgnoreCase("admin")) {
                    

                    text_admin_username.setText("Welcome " + username);
                    text_admin_username2.setText("Welcome " + username);
                    text_admin_username3.setText("Welcome " + username);

                    List users = DBManager.getDBM().get_all_users();

                    for (Iterator iterator = users.iterator(); iterator.hasNext();) {

                        Users next = (Users) iterator.next();
                        String del_userid =next.getId().toString();
                        String del_username = next.getUsername();

                        userlist1.addItem(del_userid +"- "+ del_username);

                    }

                    List groups = DBManager.getDBM().get_chat_groups();

                    for (Iterator iterator = groups.iterator(); iterator.hasNext();) {

                        Groups next = (Groups) iterator.next();
                        String del_groupid =next.getId().toString();
                        String del_groupname = next.getName();

                    }

                    login_panel.setVisible(false);
                    load_admin_group(true);
                    admin_panel.setVisible(true);

                } else{
                    

                    text_user_username.setText( username);
                    text_user_username1.setText( username);
                    text_user_username2.setText( username);

                    me = new ChatClient(user.getId(), user.getUsername(), user.getNickname(), user.getEmail());

                    load_client_groups();
                    this.start_client();
                    login_panel.setVisible(false);
                    list_groups_panel.setVisible(true);

                }

            } else {
                System.out.println("Username or Password Incorrect");
                text_login_errors.setText("Username or Password Incorrect");
            }

        }
    }//GEN-LAST:event_btnloginMouseClicked

    private void disableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_disableMouseClicked
        // TODO add your handling code here:
        textpassword.setEchoChar((char)0);
        disable.setVisible(false);
        disable.setEnabled(false);
        show.setVisible(true);
        show.setEnabled(true);
    }//GEN-LAST:event_disableMouseClicked

    private void textpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textpasswordActionPerformed

    private void textusernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textusernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textusernameActionPerformed

    int y2 = 210;

    public void recive_msg_handler(Message msg) {

        chat_background.repaint();
        chat_background.revalidate();

        JLabel msg_content = new javax.swing.JLabel();
        msg_content.setForeground(new java.awt.Color(255, 255, 255));
        msg_content.setText("<html>" + msg.getMessage() + "</html>");

        JLabel msg_time = new javax.swing.JLabel();
        msg_time.setForeground(new java.awt.Color(255, 255, 255));
        msg_time.setText(msg.getDate_time());

        JLabel msg_name = new javax.swing.JLabel();
        msg_name.setFont(new java.awt.Font("consolas", 1, 12)); 
        msg_name.setForeground(new java.awt.Color(255, 255, 255));
        msg_name.setText(msg.getName());

        JLabel msg_dp = new javax.swing.JLabel();
        msg_dp.setBackground(new java.awt.Color(0,102,102));

        List data = DBManager.getDBM().get_avatart(msg.getUserid());
        Iterator i = data.iterator();
        if (i.hasNext()) {
            Users user = (Users) i.next();
            ImageIcon iconresized = new ImageIcon(toImageIcon(user.getProfileImage()).getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT));
            msg_dp.setIcon(iconresized);
        }

        JPanel msg_layer = new javax.swing.JPanel();

        msg_layer.setBackground(
                new java.awt.Color(0,102,102));
        msg_layer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        msg_layer.setLayout(
                new org.netbeans.lib.awtextra.AbsoluteLayout());

        msg_layer.add(msg_content,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 260, 40));
        msg_layer.add(msg_time,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 210, -1));
        msg_layer.add(msg_name,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 210, -1));
        msg_layer.add(msg_dp,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 15, 35, 35));


        chat_background.add(msg_layer,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(20, y2, 280, 110));

        chat_background.repaint();
        chat_background.revalidate();

        
        
        chat_background.repaint();
        chat_background.revalidate();
        

        y2 += 120;

    }
    

       public void send_msg_handler(Message msg) {

        chat_background.repaint();
        chat_background.revalidate();

        JLabel msg_content = new javax.swing.JLabel();
        msg_content.setForeground(new java.awt.Color(255,255,255));
        msg_content.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        msg_content.setText("<html>" + msg.getMessage() + "</html>");

        JLabel msg_time = new javax.swing.JLabel();
        msg_time.setForeground(new java.awt.Color(255,255,255));
        msg_time.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        msg_time.setText(msg.getDate_time());

        JLabel msg_name = new javax.swing.JLabel();
        msg_name.setFont(new java.awt.Font("consolas", 1, 12)); 
        msg_name.setForeground(new java.awt.Color(255,255,255));
        msg_name.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        msg_name.setText(msg.getName());

        JLabel msg_dp = new javax.swing.JLabel();
        msg_dp.setBackground(new java.awt.Color(0,102,102));

        List data = DBManager.getDBM().get_avatart(msg.getUserid());
        Iterator i = data.iterator();
        if (i.hasNext()) {
            Users user = (Users) i.next();
            ImageIcon iconresized = new ImageIcon(toImageIcon(user.getProfileImage()).getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT));
            msg_dp.setIcon(iconresized);
        }

        JPanel msg_layer = new javax.swing.JPanel();
        msg_layer.setBackground(new java.awt.Color(0,102,102));
        msg_layer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        msg_layer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        msg_layer.add(msg_content, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 260, 40));
        msg_layer.add(msg_time, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 210, -1));
        msg_layer.add(msg_name, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 210, -1));
        msg_layer.add(msg_dp, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 35, 35));

        
        chat_background.add(msg_layer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, y2, 280, 110));

        JScrollBar sb = msgScrollPane.getVerticalScrollBar();
        sb.setValue(sb.getMaximum());

        chat_background.repaint();
        chat_background.revalidate();

        y2 += 120;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    Thread retrivemsg = new Thread() {
        public void run() {

            int preiv = 0;

            while (true) {
                try {

                    Message nmsg = chat.broadcast();
                    if (nmsg != null) {
                        if (preiv != nmsg.getMsgid()) {
                            

                            System.out.println(nmsg.getMsgid() + "-" + me.getId());
                            if (nmsg.getUserid() == me.getId()) {
                                send_msg_handler(nmsg);
                            } else {
                                recive_msg_handler(nmsg);
                            }

                            preiv = nmsg.getMsgid();
                        }
                    }


                    Thread.sleep(100);
                } catch (RemoteException | NullPointerException ex) {
                    System.out.println(ex);
                } catch (InterruptedException ex) {

                }
            }

        }
    }; 
    
    
    
    
    public void start_server(int g_id) {
        try {
        Chat chat = new ChatService(g_id);
            Registry reg = LocateRegistry.createRegistry(2123);
            reg.bind("ChatAdmin", chat);

            System.out.println("Chat server is running...");

        } catch (RemoteException | AlreadyBoundException e) {
            System.out.println("Exception ocured : " + e.getMessage());
        }
    }
    
    
    
    
  
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
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppLayout().setVisible(true);
            }
        });
    }

 
        
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel admin_group_list;
    private javax.swing.JPanel admin_panel;
    private javax.swing.JLabel btn_chat_groups;
    private javax.swing.JLabel btn_chat_groups1;
    private javax.swing.JButton btn_create_group;
    private javax.swing.JButton btnlogin;
    private javax.swing.JButton btnreg;
    private javax.swing.JButton btnreg1;
    private javax.swing.JPanel chat_background;
    private javax.swing.JPanel chat_panel;
    private javax.swing.JPanel client_chat_groups_panel;
    private javax.swing.JPanel create_chat_panel;
    private javax.swing.JLabel create_group;
    private javax.swing.JLabel create_group3;
    private javax.swing.JLabel disable;
    private javax.swing.JLabel disable2;
    private javax.swing.JLabel disable3;
    private javax.swing.JTextField edit_nickname;
    private javax.swing.JPasswordField edit_password;
    private javax.swing.JLabel edit_profile_image;
    private javax.swing.JLabel edit_profile_link_1;
    private javax.swing.JPanel edit_profile_panel;
    private javax.swing.JTextField edit_username;
    private javax.swing.JLabel group_create_text;
    private javax.swing.JLabel groupdescriptionLine;
    private javax.swing.JLabel groupnameLine;
    private javax.swing.JLabel img_profile;
    private javax.swing.JLabel img_profile2;
    private javax.swing.JLabel img_profile3;
    private javax.swing.JLabel img_profile4;
    private javax.swing.JLabel img_profile5;
    private javax.swing.JLabel img_profile6;
    private javax.swing.JPanel img_profile_anel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel line1;
    private javax.swing.JLabel line2;
    private javax.swing.JLabel line3;
    private javax.swing.JLabel line4;
    private javax.swing.JLabel link_all_users;
    private javax.swing.JLabel linklog;
    private javax.swing.JLabel linkreg;
    private javax.swing.JPanel list_groups_panel;
    private javax.swing.JPanel login_panel;
    private javax.swing.JLabel logout;
    private javax.swing.JLabel logout1;
    private javax.swing.JLabel logout2;
    private javax.swing.JLabel logout3;
    private javax.swing.JLabel logout4;
    private javax.swing.JLabel logout6;
    private javax.swing.JPanel manage_users_panel;
    private javax.swing.JScrollPane msgScrollPane;
    private javax.swing.JTextField msg_typer;
    private javax.swing.JLabel nicknameLine;
    private javax.swing.JLabel passwordLine;
    private javax.swing.JPanel register_panel;
    private javax.swing.JButton remove_user;
    private javax.swing.JButton send_btn;
    private javax.swing.JLabel show;
    private javax.swing.JLabel show2;
    private javax.swing.JLabel show3;
    private javax.swing.JLabel signup_profile_pic;
    private javax.swing.JLabel text_admin_username;
    private javax.swing.JLabel text_admin_username2;
    private javax.swing.JLabel text_admin_username3;
    private javax.swing.JLabel text_delete;
    private javax.swing.JLabel text_login_errors;
    private javax.swing.JLabel text_reg_errors;
    private javax.swing.JLabel text_reg_errors2;
    private javax.swing.JLabel text_user_username;
    private javax.swing.JLabel text_user_username1;
    private javax.swing.JLabel text_user_username2;
    private javax.swing.JTextField textgroupdescription;
    private javax.swing.JTextField textgroupname;
    private javax.swing.JPasswordField textpassword;
    private javax.swing.JTextField textregemail;
    private javax.swing.JTextField textregnickname;
    private javax.swing.JPasswordField textregpassword;
    private javax.swing.JTextField textregusername;
    private javax.swing.JTextField textusername;
    private javax.swing.JLabel underline;
    private javax.swing.JLabel underline1;
    private javax.swing.JLabel update_msg;
    private javax.swing.JPanel user_panel;
    private javax.swing.JComboBox<String> userlist1;
    private javax.swing.JLabel usernameLine;
    // End of variables declaration//GEN-END:variables
}
