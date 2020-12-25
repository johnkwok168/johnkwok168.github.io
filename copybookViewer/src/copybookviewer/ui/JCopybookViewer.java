package copybookviewer.ui;

import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import jkje.io.*;
import jkje.utils.*;
import jkje.ui.*;


/**
 * <p>Title: convert copybook to different types</p>
 *
 * <p>Description: cloned from JCopybook2TableDlg</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: jkje pty ltd</p>
 *
 * @author John Kwok
 * @version 1.0
 */


public class JCopybookViewer
    extends JDialog {
  Vector tableList=null;
  String creator="";
  //JTable jTable=new JTable();
  //JTable inTable=null;
  boolean[] bb;
  private Object[][] data;
  private boolean okPressed=false;
  private Vector copybook;
 // private boolean isCheckBox=true;
  private boolean isIORestricted=false;
  private static boolean system_exit=false;
  private int rowSelected=-1;


 // private CopyBookTreeNode copyBookTreeNode;
  //private CopyBook[] data;

  //public JCopybookViewer(JTextArea ja, JFrame parent) {
  //  super(parent);
  //  JCopybookViewer(ja);
  //}


  public JCopybookViewer(JTextArea ja) {

    this(ja,false);

  }


 // public JCopybookViewer(JTextArea ja) {
  //  this(ja,false);
 // }
  public JCopybookViewer(JTextArea ja, boolean isIORestricted) {
   super(jkje.app.Registry.getParentFrame());
   //this.addWindowListener(new MyWindowAdapter());
   this.isIORestricted=isIORestricted;
   ja.setFont(new java.awt.Font("Courier", Font.PLAIN, 12));
   //this.isCheckBox=true;
   this.setModal(false);

   CobolCopyBook cobolCopyBook = new CobolCopyBook();
   Vector v = cobolCopyBook.toCopyBookNode(ja.getText());
   data=new Object[v.size()][12];
   setData(v, data);


   try {
     jbInit();
   }
   catch (Exception ex) {
     ex.printStackTrace();
   }

  }

  public JCopybookViewer(String copybookName, String path) {
    super(jkje.app.Registry.getParentFrame());
    this.setModal(true);
    //CobolCopyBook cobolCopyBook = new CobolCopyBook(copybookName);
    CobolCopyBook cobolCopyBook = new CobolCopyBook(copybookName, path);
    Vector v = cobolCopyBook.getCopyBookNode(copybookName);

    Enumeration en=v.elements();
    data=new Object[v.size()][12];
    setData(v,data);
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public JCopybookViewer(String copybookName) {
    //super((JDialog)c);
    super(jkje.app.Registry.getParentFrame());

    this.setModal(true);
    //CobolCopyBook cobolCopyBook = new CobolCopyBook(copybookName);
    CobolCopyBook cobolCopyBook = new CobolCopyBook();
    Vector v = cobolCopyBook.getCopyBookNode(copybookName);

    Enumeration en=v.elements();
    data=new Object[v.size()][12];
    setData(v,data);
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

 public void setSystemExit(boolean system_exit) {
    JCopybookViewer.system_exit=system_exit;
 }

 public void setData(Vector v, Object[][] data) {
   Enumeration en=v.elements();
  // data=new Object[v.size()][11];
   int j=-1;
   int cnt=0;
   while (en.hasMoreElements()) {
     cnt = cnt + 1;
     j=j+1;
     CopyBookNode cp=(CopyBookNode)(en.nextElement());
     String fn=cp.FIELD_NAME;
     int ft=cp.FIELD_TYPE;
     String fts="ALPHA";

     if (ft==CobolCopyBook.TYPE_9) {
       fts="NUMERIC";
     }
     if (ft==CobolCopyBook.TYPE_X) {
        fts="ALPHA";
     }
     if (ft==CobolCopyBook.TYPE_COMP) {
       fts="COMP";
     }
     if (ft==CobolCopyBook.TYPE_COMP3) {
        fts="COMP-3";
     }

     if (ft==CobolCopyBook.TYPE_COMP1) {
       fts="COMP-1";
    }

    if (ft==CobolCopyBook.TYPE_COMP2) {
       fts="COMP-2";
    }

    if (ft==CobolCopyBook.TYPE_POINTER) {
        fts="POINTER";
    }
    String red="";
//     System.out.println("field name=="+cp.FIELD_NAME+" gr eln="+cp.FIELD_GROUP_LEN);
    if (cp.FIELD_GROUP_LEN>0) {
       red="Group";
     }

     if (cp.REDEFINED) {
       red=cp.REDEFINED_NAME;
     }
    Integer decimal=new Integer(cp.FIELD_DECIMAL);
    Integer len=new Integer(cp.FIELD_LENGTH);
    if (cp.FIELD_GROUP_LEN>0)
       len=new Integer(cp.FIELD_GROUP_LEN);
    int off=cp.FIELD_OFFSET+1;
    Integer offset=new Integer(off);

    data[j][0]=fn;
    data[j][1]=red;
    data[j][2]=fts;
    data[j][3]=offset.toString();
    int e=offset.intValue()+len.intValue()-1;
    Integer end=new Integer(e);
    data[j][4]=end.toString();
    data[j][5]=len.toString();
    data[j][6] = new Integer(cp.FIELD_INTEGER_PART).toString();
    data[j][7]=decimal.toString();
    data[j][8]=new Boolean(cp.FIELD_SIGNED).toString();
    data[j][9]=cp.PIC_PATTERN;
    if (cp.FIELD_GROUP_LEN>0)
      data[j][10]=new Boolean(false);
    else  data[j][10]=new Boolean(true);
    int lvl=cp.FIELD_LEVEL;
    data[j][11]=new Integer(lvl);

    }

 }



  //String lastOpenDir="";
  public static void main(String[] args) {
      system_exit=true;
    //final String lastOpenDir="";
    //String histFile="JCopybookViewerDir.txt";
   // Vector v=new Vector();
   // v.addElement("test1");
   // v.addElement("test2");
   // v.addElement("test3");
    //JCopybookViewer(JTextField textField,Container c,Vector listData, String creator, JTable table, int row, int column) {

  //  JCopybookViewer JCopybookViewer = new JCopybookViewer("MHDBUP");
  //  JCopybookViewer.setSize(800,600);
  //  JCopybookViewer.setVisible(true);

// using jtext

    final String[] lastOpenDir=new String[2];
    lastOpenDir[0]="";
    lastOpenDir[1]="";
    final CobolCopyBook cobolCopyBook = new CobolCopyBook();
    final JFrame f=new JFrame("Convert cobol copybook to table view");
    f.addWindowFocusListener(new WindowAdapter() {
       @Override
	public void windowClosing(WindowEvent e) {
         System.out.println("saving hist..");
         if (lastOpenDir.length > 0) {
             try {
               File lastOpenFile = new File("JCopybookViewerDir.txt");
               if (!lastOpenFile.exists()) {
                 lastOpenFile.createNewFile();
               }
               Writer output = new BufferedWriter(new FileWriter(lastOpenFile));
               output.write(lastOpenDir[0]);
               output.close();
               System.out.println("saving " + lastOpenDir + " to " +
                                  lastOpenFile.getAbsoluteFile() );
             }
             catch (IOException e2) {}

          }


       }
     });



    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jkje.app.Registry.setParentFrame(f);
    final JTextArea jt=new JTextArea();
    String  l1="      *\n";
    String  l2="      * Thus is a comment line\n";
    String  l3="      * Below is the sample copybook\n";
    String  l4="       01 A1.\n";
    String  l5="          03 B1   PIC X(10).\n";
    String  l6="          03 C1   PIC S9(5)V99 COMP-3.\n";
    String  l7="          03 D1   USAGE IS COMP-1.\n";
    String  l8="          03 E1   COMP-2.\n";
    String  l9="          03 F1   PIC S9(5)V99 COMP-4.\n";
    String l10="          03 G1   PIC S9(5)V99 COMP-5.\n";
    String l11="          03 H1 USAGE IS COMPUTATIONAL-3.\n";
    String l12="             05 H11   PIC S9(11)V9(2).\n";
    String l13="             05 H12   PIC S9(11)V9(2).\n";
    String l14="          03 I1   PIC +9(11).9(2).\n";
    jt.setText(l1+l2+l3+l4+l5+l6+l7+l8+l9+l10+l11+l12+l13+l14);

    jt.setFont(new java.awt.Font("Courier", Font.PLAIN, 12));

    //final JCopybookViewer JCopybookViewer = new JCopybookViewer(jt,false);
    //JCopybookViewer.setSystemExit(true);
    JButton okBtn=new JButton();
     okBtn.setText("OK");
     okBtn.setBackground(new Color(0, 105, 255));
     okBtn.setForeground(Color.white);
     JButton exitBtn=new JButton();
     exitBtn.setText("Exit");
     exitBtn.setBackground(new Color(0, 105, 255));
     exitBtn.setForeground(Color.white);
     JButton clearBtn=new JButton();
     clearBtn.setText("Clear");
     clearBtn.setBackground(new Color(0, 105, 255));
     clearBtn.setForeground(Color.white);

     JButton importBtn=new JButton();
      importBtn.setText("Import");
      importBtn.setBackground(new Color(0, 105, 255));
     importBtn.setForeground(Color.white);
     //okBtn.setMaximumSize(new Dimension(31,111));
     GridBagLayout gridBagLayout1 = new GridBagLayout();
     f.getContentPane().setLayout(gridBagLayout1);
    // f.setLayout(new Gr);
     f.getContentPane().add(new JScrollPane(jt),
                            new GridBagConstraints(0, 0, 1, 1, 1, 1
      , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));
     JPanel jPanel=new JPanel();
     jPanel.setLayout(gridBagLayout1);
     jPanel.setBackground(Color.white);
     // f.getContentPane().add(new JScrollPane(jt));
     f.getContentPane().add(jPanel,
                            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
       , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));
     JLabel jLabel = new JLabel("Copy (ctrl-c) copybook from col 1 (NOT from col 8 where the record level 01 is) and paste (ctrl-v) it in the text area above");
    //jLabel1.setText("Copyright 2004-2005 JKJE Pty Ltd.  All Rights Reserved");
     f.getContentPane().add(jLabel,
                         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
      , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));


      JLabel jLabel2 = new JLabel("Copyright 2010-2011 JKJE Pty Ltd.  All Rights Reserved");
      jLabel2.setFont(new java.awt.Font("Arial", Font.PLAIN, 9));
      jLabel2.setBackground(Color.white);
      f.getContentPane().add(jLabel2,
                          new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
       , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
       new Insets(0, 0, 0, 0), 0, 0));

      jPanel.add(okBtn, new GridBagConstraints(0, 0, 1, 1, 0, 0
                    , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                      new Insets(0, 0, 0, 0), 0, 0));

      jPanel.add(clearBtn, new GridBagConstraints(1, 0, 1, 1, 0, 0
      , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));


     jPanel.add(importBtn, new GridBagConstraints(2, 0, 1, 1, 0, 0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(0, 0, 0, 0), 0, 0));
      jPanel.add(exitBtn, new GridBagConstraints(3, 0, 1, 1, 0, 0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                  new Insets(0, 0, 0, 0), 0, 0));


     //f.getContentPane().add(okBtn,
     //                       new GridBagConstraints(0, 1, 1, 1, 0, 0
      //, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
     // new Insets(0, 0, 0, 0), 0, 0));


     okBtn.addActionListener(new java.awt.event.ActionListener() {
     @Override
	public void actionPerformed(java.awt.event.ActionEvent evt) {
           // cobolCopyBook.copyBook2CopyBookTable(jt.getText());
            JCopybookViewer JCopybookViewer = new JCopybookViewer(jt,false);
            JCopybookViewer.setSystemExit(true);
            JCopybookViewer.setSize(850,600);
            JCopybookViewer.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            JCopybookViewer.setVisible(true);


     }
     });
     exitBtn.addActionListener(new java.awt.event.ActionListener() {
      @Override
	public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (lastOpenDir.length > 0&&lastOpenDir[0]!=""||lastOpenDir[0]!=null) {
            try {
              File lastOpenFile = new File("JCopybookViewerDir.txt");
              if (!lastOpenFile.exists()) {
                lastOpenFile.createNewFile();
              }
              Writer output = new BufferedWriter(new FileWriter(lastOpenFile));
              output.write(lastOpenDir[0]);
              output.close();
              System.out.println("saving " + lastOpenDir[0] + " to " +
                                 lastOpenFile.getAbsoluteFile() );
            }
            catch (IOException e2) {}

         }



        f.setVisible(false);
        if (system_exit) {
          System.exit(0);
        }
      }
     });

     clearBtn.addActionListener(new java.awt.event.ActionListener() {
      @Override
	public void actionPerformed(java.awt.event.ActionEvent evt) {
        jt.setText("");
      }
     });
     //final String histFile="JCopybookViewerDir.txt";

     importBtn.addActionListener(new java.awt.event.ActionListener() {
     @Override
	public void actionPerformed(java.awt.event.ActionEvent evt) {
      // String lastOpenDir="";
       try {

         File lastOpenCopyFile = new File("JCopybookViewerDir.txt");
         System.out.println("reading " + lastOpenCopyFile.getAbsoluteFile() );
         StringBuffer contents = new StringBuffer();
         BufferedReader reader = null;
         String text = null;

         reader = new BufferedReader(new FileReader(lastOpenCopyFile));
         while ( (text = reader.readLine()) != null) {
           contents.append(text);
         }
         lastOpenDir[0] = contents.toString();
       }
        catch (IOException e2) {}

      JFileChooser jFileChooser1 = new JFileChooser();
      if (lastOpenDir[0].length()>0)
        jFileChooser1.setCurrentDirectory(new File(lastOpenDir[0]));



       JDialog jf = new JDialog(f);
       jf.setSize(400, 600);
       //JFileChooser jFileChooser1 = new JFileChooser();
       //jFileChooser1.setCurrentDirectory(new File(jTextField2.getText().trim()));
       jFileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
       jFileChooser1.setDialogTitle("Select a copybook");

       int result = jFileChooser1.showOpenDialog(jf);
       // .showSaveDialog(jf);
       File selectedFile = jFileChooser1.getSelectedFile();
       if (selectedFile==null) return;
       lastOpenDir[0]=selectedFile.getAbsolutePath();
      // jFileChooser1.setCurrentDirectory(selectedFile);
       String infile=selectedFile.getAbsolutePath();
       String line="";
       if (selectedFile != null) {
         try {
           BufferedReader br = new BufferedReader(new FileReader(infile));
           boolean eof=false;
           while (!eof) {
            line=br.readLine();
            if (line==null) eof=true;
            else {
              jt.append(line+"\n");
            }
           }

         } catch (Exception e){}
       }
       else {
         //System.out.println("no file selected");
       }
    //jFileChooser1.setVisible(true);

    }
    });



    f.setSize(800,550);
    f.setVisible(true);



  }






  private void jbInit() throws Exception {



   // border6 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
    //    new Color(178, 178, 178)), "Please Check the Required Fields");
    //this.setTitle("Select columns to display");
    //this.addWindowListener(new MyWindowAdapter());
    this.setTitle("Copybook Table View");
    //this.setModal(true);
    this.getContentPane().setBackground(Color.white);
   // this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    this.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    this.getContentPane().setLayout(gridBagLayout1);
    jTextField.setPreferredSize(new Dimension(31,21));
    jTextField2.setPreferredSize(new Dimension(31,21));
    jPanel2.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
    jPanel1.setLayout(gridBagLayout3);
    /*
    jButton1.setBackground(new Color(0, 105, 255));
    jButton1.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
    jButton1.setForeground(Color.white);
    jButton1.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
        new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
        new Color(0, 73, 178)));
    //jButton1.setMaximumSize(new Dimension(71, 21));
    //jButton1.setMinimumSize(new Dimension(71, 21));
    //jButton1.setPreferredSize(new Dimension(71, 21));
    jButton1.setToolTipText("");
    jButton1.setText("Cancel");
    jButton1.addActionListener(new JCopybookViewer_jButton1_actionAdapter(this));
*/
    jButton2.setBackground(new Color(0, 105, 255));
    jButton2.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
    jButton2.setForeground(Color.white);
    jButton2.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
        new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
        new Color(0, 73, 178)));
   // jButton2.setMaximumSize(new Dimension(63, 21));
   // jButton2.setMinimumSize(new Dimension(63, 21));
   // jButton2.setPreferredSize(new Dimension(63, 21));
    jButton2.setText("Return");
    jButton2.addActionListener(new JCopybookViewer_jButton2_actionAdapter(this));
    jButton3.setBackground(new Color(0, 105, 255));
    jButton3.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
    jButton3.setForeground(Color.white);
    jButton3.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
        new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
        new Color(0, 73, 178)));
    //jButton3.setMaximumSize(new Dimension(63, 21));
    //jButton3.setMinimumSize(new Dimension(63, 21));
    //jButton3.setPreferredSize(new Dimension(63, 21));
    jButton3.setText("Select All");

    border6 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,
        new Color(178, 178, 178)), "Please Check the Required Fields");

    jButton3.addActionListener(new JCopybookViewer_jButton3_actionAdapter(this));

    jButton4.setBackground(new Color(0, 105, 255));
    jButton4.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
    jButton4.setForeground(Color.white);
    jButton4.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
        new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
        new Color(0, 73, 178)));
   // jButton4.setMaximumSize(new Dimension(101, 21));
   // jButton4.setMinimumSize(new Dimension(101, 21));
    //jButton4.setPreferredSize(new Dimension(101, 21));
    jButton4.setText("Unselect All");

    jButton4.addActionListener(new JCopybookViewer_jButton4_actionAdapter(this));

    jButton5.setBackground(new Color(0, 105, 255));
    jButton5.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
    jButton5.setForeground(Color.white);
    jButton5.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
        new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
        new Color(0, 73, 178)));
    //jButton5.setMaximumSize(new Dimension(121, 21));
    //jButton5.setMinimumSize(new Dimension(121, 21));
    //jButton5.setPreferredSize(new Dimension(121, 21));
    jButton5.setText("EasyTrieve View(W)");
    //if (!isCheckBox) jButton5.setVisible(false);
    jButton5.addActionListener(new JCopybookViewer_jButton5_actionAdapter(this));

    jButton6.setBackground(new Color(0, 105, 255));
    jButton6.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
    jButton6.setForeground(Color.white);
    jButton6.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
        new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
        new Color(0, 73, 178)));
    //jButton6.setMaximumSize(new Dimension(131, 21));
   // jButton6.setMinimumSize(new Dimension(131, 21));
    //jButton6.setPreferredSize(new Dimension(131, 21));
    jButton6.setText("EasyTrieve View(File)");
    //if (!isCheckBox) jButton5.setVisible(false);
    jButton6.addActionListener(new JCopybookViewer_jButton6_actionAdapter(this));

    jButton7.setBackground(new Color(0, 105, 255));
     jButton7.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
     jButton7.setForeground(Color.white);
     jButton7.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
         new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
         new Color(0, 73, 178)));
    // jButton7.setMaximumSize(new Dimension(71, 21));
    // jButton7.setMinimumSize(new Dimension(71, 21));
    // jButton7.setPreferredSize(new Dimension(71, 21));
     jButton7.setText("SAS View");
     //if (!isCheckBox) jButton5.setVisible(false);
     jButton7.addActionListener(new JCopybookViewer_jButton7_actionAdapter(this));

      jButton8.setBackground(new Color(0, 105, 255));
      jButton8.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
      jButton8.setForeground(Color.white);
      jButton8.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
          new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
          new Color(0, 73, 178)));
      //jButton8.setMaximumSize(new Dimension(91, 21));
      //jButton8.setMinimumSize(new Dimension(91, 21));
      //jButton8.setPreferredSize(new Dimension(91, 21));
      jButton8.setText("Display/Print");
      //if (!isCheckBox) jButton5.setVisible(false);
      jButton8.addActionListener(new JCopybookViewer_jButton8_actionAdapter(this));
      jButton8.setEnabled(false);
      if (isIORestricted)
        jButton8.setVisible(false);

      jButton9.setBackground(new Color(0, 105, 255));
      jButton9.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
      jButton9.setForeground(Color.white);
      jButton9.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
          new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
          new Color(0, 73, 178)));
      //jButton9.setMaximumSize(new Dimension(131, 21));
      //jButton9.setMinimumSize(new Dimension(131, 21));
      //jButton9.setPreferredSize(new Dimension(131, 21));
      jButton9.setText("Export to csv file");
      jButton9.setEnabled(true);
      if (isIORestricted)
        jButton9.setVisible(false);
      //if (!isCheckBox) jButton5.setVisible(false);
      jButton9.addActionListener(new JCopybookViewer_jButton9_actionAdapter(this));

      jButton10.setBackground(new Color(0, 105, 255));
      jButton10.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
      jButton10.setForeground(Color.white);
      jButton10.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
            new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
            new Color(0, 73, 178)));
      //jButton10.setMaximumSize(new Dimension(71, 21));
      //jButton10.setMinimumSize(new Dimension(71, 21));
      //jButton10.setPreferredSize(new Dimension(71, 21));
      jButton10.setText("java View");
      jButton10.addActionListener(new JCopybookViewer_jButton10_actionAdapter(this));

     // jButton9.addActionListener(new JCopybookViewer_jButton9_actionAdapter(this));

      jButton11.setBackground(new Color(0, 105, 255));
      jButton11.setFont(new java.awt.Font("Arial", Font.BOLD, 11));
      jButton11.setForeground(Color.white);
      jButton11.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,
            new Color(0, 214, 255), new Color(0, 150, 255), new Color(0, 51, 124),
            new Color(0, 73, 178)));
      //jButton11.setMaximumSize(new Dimension(71, 21));
      //jButton11.setMinimumSize(new Dimension(71, 21));
      //jButton11.setPreferredSize(new Dimension(71, 21));
      jButton11.setText("SQL Create");
      jButton11.addActionListener(new JCopybookViewer_jButton11_actionAdapter(this));

    jPanel2.setBackground(Color.white);
    jPanel2.setLayout(gridBagLayout2);
    jPanel1.setBackground(Color.white);
    jPanel1.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));

     jPanel1.setBorder(border19);

    jPanel3.setBackground(Color.white);
    jPanel3.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
    jPanel3.setBorder(border15);
    jPanel3.setLayout(gridBagLayout4);

    jPanel4.setBackground(Color.white);
    jPanel4.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
    jPanel4.setBorder(border31);
    jPanel4.setLayout(gridBagLayout5);

    jPanel5.setBackground(Color.white);
    jPanel5.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
    jPanel5.setBorder(border33);
    jPanel5.setLayout(gridBagLayout5);


    ButtonGroup grp=new ButtonGroup();
    JRadioButton displayCurr = new JRadioButton("Display current row");
    JRadioButton displayAll = new JRadioButton("Display all rows");
    JRadioButton displayRange = new JRadioButton("Display range");
    JRadioButton displayVert = new JRadioButton("Vertical");
    JRadioButton displayHorizontal = new JRadioButton("Horizontal");


    grp.add(displayCurr);
    grp.add(displayAll);
    grp.add(displayRange);

    ButtonGroup grp2=new ButtonGroup();
    displayVert.setSelected(true);
    grp2.add(displayVert);
    grp2.add(displayHorizontal);
    displayVert.setSelected(true);
    //jPanel3.setVisible(true);
    displayVert.setBackground(Color.WHITE);
    displayHorizontal.setBackground(Color.WHITE);
    jTable1.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));

    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    jTable1.setColumnSelectionAllowed(true);
    model=createModel1();
    jTable1.setModel(model);
   // populateColumns();

    jScrollPane2.getViewport().setBackground(Color.white);
    jScrollPane2.setFont(new java.awt.Font("Arial", Font.PLAIN, 11));
    //jList.setSelectionMode();
    //jList.setSelectionInterval(0,3);
    //DefaultListSelectionModel lsm=new DefaultListSelectionModel();
    //lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    ListSelectionModel rowSM = jTable1.getSelectionModel();
    ListSelectionAction listSelectionAction = new ListSelectionAction();
    rowSM.addListSelectionListener(listSelectionAction);
    rowSM.clearSelection();
    rowSM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    rowSM.setValueIsAdjusting(false);


    this.getContentPane().add(jPanel2,
                              new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    jScrollPane2.getViewport().add(jTable1);
    jPanel1.add(jScrollPane2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    //easy(w)
     jPanel2.add(jButton5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0, 0));
     //easy(f)
     jPanel2.add(jButton6, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(0, 0, 0, 0), 0, 0));
    //sas
    jPanel2.add(jButton7, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0), 0, 0));

   //java
   jPanel2.add(jButton10, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0), 0, 0));
  //sql
   jPanel2.add(jButton11, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.CENTER,
                                              GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0), 0, 0));
  //display
  jPanel2.add(jButton8, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                             new Insets(0, 0, 0, 0), 0, 0));

  //export
  jPanel2.add(jButton9, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                                new Insets(0, 0, 0, 0), 0, 0));



    //ok
    jPanel2.add(jButton2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                 new Insets(0, 0, 0, 0), 0, 0));
    //cancel
    //jPanel2.add(jButton1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
    //                                             , GridBagConstraints.CENTER,
    //                                             GridBagConstraints.BOTH,
    //                                             new Insets(0, 0, 0, 0), 0, 0));
    //selectall
    jPanel2.add(jButton3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(0, 0, 0, 0), 0, 0));
    //unselectall
    jPanel2.add(jButton4, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(0, 0, 0, 0), 0, 0));



     jPanel3.add(jPanel4,
                              new GridBagConstraints(0, 0, 1, 1, 1.0, 0.2
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));


     jPanel4.add(displayCurr,
                           new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0
                           , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                           new Insets(0, 0, 0, 0), 0, 0));
     jPanel4.add(displayAll,
                        new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0
                        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(displayRange,
                      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                      , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jTextField,
                    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                    , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
    jPanel4.add(jLabel,
                new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
  jPanel4.add(jTextField2,
                  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
                  , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

    jPanel3.add(jPanel5,
                             new GridBagConstraints(1, 0, 1, 1, 1.0, 0.2
       , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    this.getContentPane().add(jPanel1,
                              new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));

    jPanel5.add(displayVert,
                         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                         , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                         new Insets(0, 0, 0, 0), 0, 0));
   jPanel5.add(displayHorizontal,
                      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                      , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                      new Insets(0, 0, 0, 0), 0, 0));


  }



  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  JButton jButton4 = new JButton();
  JButton jButton5 = new JButton();
  JButton jButton6 = new JButton();
  JButton jButton7 = new JButton();
  JButton jButton8 = new JButton();
  JButton jButton9 = new JButton();
  JButton jButton10 = new JButton();
  JButton jButton11 = new JButton();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  Border border1 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
 // Border border2 = new TitledBorder(border1, "Select Tables for FROM clause");
  String sep="";
  //enter
  String sql="";
  JPanel jPanel3 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JPanel jPanel5 = new JPanel();
  Border border3 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border4 = new TitledBorder(border3, "Select A Table");
  Border border5 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border6 = new TitledBorder(border5, "Check Columns");
  Border border7 = BorderFactory.createEtchedBorder(Color.white,
      new Color(178, 178, 178));
  Border border8 = new TitledBorder(border7, "then check required columns");
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTable jTable1 = new JTable();
  AbstractTableModel model=null;
  //jkje.swing.MyJTable queryTable = new MyJTable();

  //ok
  public void jButton2_actionPerformed(ActionEvent e){
    okPressed=true;
    setVisible(false);
    dispose();


  }

  //select all
  public void jButton3_actionPerformed(ActionEvent e){
    for (int i=0;i<jTable1.getRowCount();i++) {
      //System.out.println("column["+i+"]="+jTable1.getValueAt(i,0));
      //Boolean b = (Boolean) jTable1.getValueAt(i, 1);
      //if (b == null)break;
      Boolean t=new Boolean(true);
      Boolean f=new Boolean(false);
     // String s=(String)data[i][5];
    //  if (s==null) {
    //    data[i][5]=t;
    //    continue;
   //   }
   //   s=s.trim();
   //   if (s.length()>0)
        data[i][10]=t;
   //   else data[i][5]=f;

    }
    AbstractTableModel tm=(AbstractTableModel)jTable1.getModel();
    tm.fireTableDataChanged();
  }

  //unselect all
  public void jButton4_actionPerformed(ActionEvent e){
    for (int i=0;i<jTable1.getRowCount();i++) {
      //System.out.println("column["+i+"]="+jTable1.getValueAt(i,0));
      //Boolean b = (Boolean) jTable1.getValueAt(i, 1);
      //if (b == null)break;
      Boolean t=new Boolean(true);
      Boolean f=new Boolean(false);
 //     String s=(String)data[i][5];
 //     if (s==null) continue;
 //     s=s.trim();
     // if (s.length()>0)
      data[i][10]=f;

    }
    AbstractTableModel tm=(AbstractTableModel)jTable1.getModel();
    tm.fireTableDataChanged();
  }

  //easytrive view
  public void jButton5_actionPerformed(ActionEvent e){
   // String[] columnNames2={"Filed Name", "Redefine/Group","Field Type", "Start" ,"End", "Field Length", "Decimal Places"};
    JFrame f=new JFrame("Easytrieve View(w)");
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    JTextArea jt=new JTextArea();
    String[] str=new String[data.length];
    String fn01=(String)data[0][0];
    int j=0;
    int prevlvl=1;
    fn01="W";
    
    for (int i=0;i<data.length;i++) {
      String fn = (String) data[i][0];
      fn=fn.trim();
      if (fn.length()==0) {
    	  continue;
      }
      String fn2 = "";
      if (i == 0) {
    	  fn2 = "W";
      }

      Boolean b=(Boolean)data[i][10];
      boolean selected=b.booleanValue();
      Integer LVL=(Integer)data[i][11];
      int lvl=LVL.intValue();
     
      String s2="";
      //System.out.println("s2="+s2);
      if (data[i][1]==null) {
    	  s2="";
      } else {
    	  s2=(String)data[i][1]; 
      }
      s2=s2.toUpperCase();

      //if (lvl==1&&s2.equals("GROUP")) {	
      if (lvl==1&&s2.length()>0) {	  
     // if (lvl<prevlvl) {
    	
    	if (s2.length()>0) {
    	  if (s2.equals("GROUP")) fn01=(String)data[i][0];	
    	  else fn01=(String)data[i][1];
    	} else fn01="W";  
      } 
  
 /*     
      if (lvl>prevlvl){
    	  prevlvl = lvl;
    	  fn01="";
      }
 */     
      if (!selected) continue;

      if (i!=0) fn2=fn01;
      //else if (data[i][1].equals("Redefines")) fn2 = (String) data[i][1];
      //else fn2 = "";

      String s=(String)data[i][3];
      Integer off = new Integer(s);
      int ioff=off.intValue();
      ioff=ioff-1;
      Integer iioff=new Integer(ioff);
      //data[i][3]=iioff.toString();

      String offset = "";
     // if (!(data[i][3].equals("0"))) offset = "+" + (String) (data[i][3]);

      if (!(data[i][3].equals("1")))
        offset = "+" + iioff.toString();
        //offset = "+" + (String) (data[i][3]);
      String fl = (String) data[i][5];
      String ft = "A";
      if (data[i][2].equals("NUMERIC")) {
        ft = "N";
      }
      if (data[i][2].equals("ALPHA")) {
        ft = "A";
      }
      if (data[i][2].equals("COMP")) {
        ft = "B";
      }
      if (data[i][2].equals("COMP-3")) {
        ft = "P";
      }
      String dec="";
      if (ft.equals("A"))
        dec="";
      else {
        dec = (String) data[i][7];
        if (dec.equals("0")) {
          dec="";
        }
      }
      boolean isWrite=true;
      if (fn!=null) {
        if (fn.equals("FILLER")) {
          isWrite=false;
          j = j + 1;
          fn = "FILLER" + j;
        }
      } else {
        isWrite=false;
        j = j + 1;
        fn = "FILLER" + j;
      }
      if (fn.length()<25&&fn2.length()<25&&offset.length()<8&&fl.length()<8) {
        str[i] = fn + whiteSpaces(25 - fn.length()) + fn2 +
            whiteSpaces(25 - fn2.length()) + offset + whiteSpaces(8-offset.length()) + fl +
            whiteSpaces(8-fl.length()) + ft + " " + dec + "\n";
      } else {
        str[i] = fn + "  " + fn2 +
            " " + offset + "   " + fl + " " + ft +
            " " + dec + "\n";
      }
      if (isWrite)
       jt.setText(jt.getText() + str[i]);

    }
     jt.setFont(new java.awt.Font("Courier", Font.PLAIN, 12));
      //okBtn.setMaximumSize(new Dimension(31,111));
      GridBagLayout gridBagLayout1 = new GridBagLayout();
      f.getContentPane().setLayout(gridBagLayout1);

      f.getContentPane().add(new JScrollPane(jt),
                             new GridBagConstraints(0, 0, 1, 1, 1, 1
       , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
       new Insets(0, 0, 0, 0), 0, 0));
    f.setSize(500,500);
    f.setVisible(true);

  }

  public String whiteSpaces (int len) {
    String str="";
    for (int i=0;i<len;i++) {
      str=str+" ";
    }
    return str;
  }

  //easytrive view
  public void jButton6_actionPerformed(ActionEvent e){
   // String[] columnNames2={"Filed Name", "Redefine/Group","Field Type", "Start" ,"End", "Field Length", "Decimal Places"};
    JFrame f=new JFrame("Easytrieve View(File)");
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    JTextArea jt=new JTextArea();
    String[] str=new String[data.length];
    String fn01=(String)data[0][0];
    int j=0;
    for (int i=0;i<data.length;i++) {
      String fn = (String) data[i][0];
      String fn2 = "";
      if (i == 0) fn2 = "W";

      Boolean b=(Boolean)data[i][10];
      boolean selected=b.booleanValue();
      if (!selected) continue;

      if (i!=0) fn2=fn01;
      //else if (data[i][1].equals("Redefines")) fn2 = (String) data[i][1];
      //else fn2 = "";
      String offset =(String)data[i][3];;
      Integer ii=new Integer(offset);
      int iii=ii.intValue();
      ii=new Integer(iii);
      String offset1=ii.toString();
     // if (!(data[i][3].equals("0"))) offset =(String) (data[i][3]);
      String fl = (String) data[i][5];
      String ft = "A";
      if (data[i][2].equals("NUMERIC")) {
        ft = "N";
      }
      if (data[i][2].equals("ALPHA")) {
        ft = "A";
      }
      if (data[i][2].equals("COMP")) {
        ft = "B";
      }
      if (data[i][2].equals("COMP-3")) {
        ft = "P";
      }
      String dec="";
      if (ft.equals("A"))
        dec="";
      else {
        dec = (String) data[i][7];
        if (dec.equals("0")) {
          dec="";
        }
      }
      //String fl=(String)data[i][5];
      boolean isWrite=true;
      if (fn!=null) {
         if (fn.equals("FILLER")) {
           isWrite=false;
            j = j + 1;
            fn = "FILLER" + j;
         }
      } else {
        isWrite=false;
        j = j + 1;
        fn="FILLER"+j;
      }

        if (fn.length() < 30 && fl.length() < 8 && offset1.length() < 8) {
          str[i] = fn + whiteSpaces(30 - fn.length()) + offset1 +
              whiteSpaces(8 - offset1.length()) + fl +
              whiteSpaces(8 - fl.length()) + ft + " " + dec + "\n";
        }
        else {
          str[i] = fn + " " + offset1 + "   " + fl +
              " " + ft + " " + dec + "\n";
        }
        if (isWrite)
          jt.setText(jt.getText() + str[i]);

    }
     jt.setFont(new java.awt.Font("Courier", Font.PLAIN, 12));
      //okBtn.setMaximumSize(new Dimension(31,111));
      GridBagLayout gridBagLayout1 = new GridBagLayout();
      f.getContentPane().setLayout(gridBagLayout1);

      f.getContentPane().add(new JScrollPane(jt),
                             new GridBagConstraints(0, 0, 1, 1, 1, 1
       , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
       new Insets(0, 0, 0, 0), 0, 0));
    f.setSize(500,500);
    f.setVisible(true);

  }


  //sas view
   public void jButton7_actionPerformed(ActionEvent e){
    // String[] columnNames2={"Filed Name", "Redefine/Group","Field Type", "Start" ,"End", "Field Length", "Decimal Places"};
     JFrame f=new JFrame("SAS View");
     f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
     JTextArea jt=new JTextArea();
     String[] str=new String[data.length];
     String fn01=(String)data[0][0];
     int j=0;
     for (int i=0;i<data.length;i++) {
       String fn = (String) data[i][0];
       String fn2 = "";
       if (i == 0) fn2 = "W";

       Boolean b=(Boolean)data[i][10];
       boolean selected=b.booleanValue();
      if (!selected) continue;

       if (i!=0) fn2=fn01;
       //else if (data[i][1].equals("Redefines")) fn2 = (String) data[i][1];
       //else fn2 = "";
       String offset =(String)data[i][3];;
       Integer ii=new Integer(offset);
       int iii=ii.intValue();
       ii=new Integer(iii);
       String offset1=ii.toString();
      // if (!(data[i][3].equals("0"))) offset =(String) (data[i][3]);
       String fl = (String) data[i][5];
       String ft = "$";
       if (data[i][2].equals("NUMERIC")) {
         ft = "z";
       }
       if (data[i][2].equals("ALPHA")) {
         ft = "$";
       }
       if (data[i][2].equals("COMP")) {
         ft = "IB";
       }
       if (data[i][2].equals("COMP-1")) {
         ft = "RB";
       }
       if (data[i][2].equals("COMP-2")) {
         ft = "RB";
       }
       if (data[i][2].equals("COMP-3")) {
         ft = "PD";
       }
       String dec="";
       if (ft.equals("$"))
         dec="";
       else {
         dec = (String) data[i][7];
         if (dec.equals("0")) {
           dec="";
         }
       }
       //String fl=(String)data[i][5];
       boolean isWrite=true;
       if (fn!=null) {
          if (fn.equals("FILLER")) {
            isWrite=false;
             j = j + 1;
             fn = "FILLER" + j;
          }
       } else {
         isWrite=false;
         j = j + 1;
         fn="FILLER"+j;
       }
         fn=fn.replace('-','_');
         if (fn.length() < 30 && fl.length() < 8 && offset1.length() < 8) {
           str[i] = "@"+offset1 + whiteSpaces(8 - offset1.length()) +
               fn + whiteSpaces(30 - fn.length()) +  ft + fl +
               "." + dec + "\n";
         }
         else {
           str[i] = "@" + offset1 + "  " + fn +  "   " + ft + fl +
               "." + dec + "\n";
         }
         if (isWrite)
           jt.setText(jt.getText() + str[i]);

     }
      jt.setFont(new java.awt.Font("Courier", Font.PLAIN, 12));
       //okBtn.setMaximumSize(new Dimension(31,111));
       GridBagLayout gridBagLayout1 = new GridBagLayout();
       f.getContentPane().setLayout(gridBagLayout1);

       f.getContentPane().add(new JScrollPane(jt),
                              new GridBagConstraints(0, 0, 1, 1, 1, 1
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 0, 0));
     f.setSize(500,500);
     f.setVisible(true);

  }



  public boolean getOkPressed() {
    return okPressed;
  }

  public Object[][] getColumnData() {
    return data;
  }

  //display/print
   public void jButton8_actionPerformed(ActionEvent e){
       File tempFolder = new File("temp");
       if (!(tempFolder.exists()))
          tempFolder.mkdir();
       File dir=new File("temp/"+System.getProperty("user.name"));
       //System.out.println("tempDir absolute path " + dir.getAbsolutePath());
        if (!(dir.exists()))
          dir.mkdir();
        if (rowSelected==-1) rowSelected=0;
        jkje.utils.HTMLDisplay dis=new HTMLDisplay("Copybook Table Layout",dir,
                                                   "print01.html","copybook",
                                                   jTable1,rowSelected);

   }

   //save to csv
     public void jButton9_actionPerformed(ActionEvent e){
       //jkje.app.Registry.setCurrContainer(this);
       JExportDlg je = new JExportDlg(jTable1,false);
       //je.system_exit=false;
       //JExportDlg je = new JExportDlg(
       //je.setVisible(true);
   }

  public void javaView2(String[][] data) {

  }
   //java view
public void jButton10_actionPerformed(ActionEvent e){
  JFrame f=new JFrame("java View");
  JTextArea jt=new JTextArea();
  String[] str=new String[data.length];
  String fn01=(String)data[0][0];
  int j=0;

  for (int i=0;i<data.length;i++) {
    String fn = (String) data[i][0];
    fn=fn.replace('-','_');
    if (i==0) {
      jt.setText("class "+fn+" {"+"\n");
    }
    String fn2 = "";
    if (i == 0) fn2 = "";

    Boolean b=(Boolean)data[i][10];
    boolean selected=b.booleanValue();
    if (!selected) continue;

    if (i!=0) fn2=fn01;
    //else if (data[i][1].equals("Redefines")) fn2 = (String) data[i][1];
    //else fn2 = "";
    String offset =(String)data[i][3];;
    Integer ii=new Integer(offset);
    int iii=ii.intValue()+1;
    ii=new Integer(iii);
    String offset1=ii.toString();
    String gr=(String)data[i][1];
    if (gr.length()>0&&!selected) {
      continue;
    }
   // if (!(data[i][3].equals("0"))) offset =(String) (data[i][3]);
    String fl = (String) data[i][5];
    int ifl=new Integer(fl).intValue();
    String ft = "String";
    //if (data[i][2].equals("NUMERIC")) {
    //  ft = "";
   // }
    if (data[i][2].equals("ALPHA")) {
      ft = "String";
    }
    if (data[i][2].equals("COMP")) {
      ft = "int";
      if (ifl> 4) {
        ft = "long";
      }
    }
    if (data[i][2].equals("COMP-1")) {
        ft = "float";
      }
      if (data[i][2].equals("COMP-2")) {
        ft = "double";
    }
    if (data[i][2].equals("COMP-3")) {
      ft = "double";
    }

    String dec="";
    if (ft.equals("String"))
         dec="";
    else {
       dec = (String) data[i][7];
       if (dec.equals("0")) {
          dec="";
       }
    }

    if (data[i][2].equals("NUMERIC")) {
      if (dec.equals("0"))
        ft="int";
      else
        ft = "double";
    }

    //String fl=(String)data[i][5];
    boolean isWrite=true;
    if (fn!=null) {
       if (fn.equals("FILLER")) {
         isWrite=false;
          j = j + 1;
          fn = "FILLER" + j;
       }
    } else {
      isWrite=false;
      j = j + 1;
      fn="FILLER"+j;
    }
    fn=fn.replace('-','_');

    str[i] = "    " +ft + "  " + fn +  ";  " + "\n";

    if (isWrite)
        jt.setText(jt.getText() + str[i]);

  }
  jt.setText(jt.getText()+ "\n"+"}");
   jt.setFont(new java.awt.Font("Courier", Font.PLAIN, 12));
    //okBtn.setMaximumSize(new Dimension(31,111));
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    f.getContentPane().setLayout(gridBagLayout1);

    f.getContentPane().add(new JScrollPane(jt),
                           new GridBagConstraints(0, 0, 1, 1, 1, 1
     , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
     new Insets(0, 0, 0, 0), 0, 0));
   f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
   f.setSize(500,500);
   f.setVisible(true);

}

// SQL Create
 public void jButton11_actionPerformed(ActionEvent e){
   JFrame f=new JFrame("SQL Create");
   JTextArea jt=new JTextArea();

   String[] str=new String[data.length];
   String fn01=(String)data[0][0];
   //fn01=fn01.replace('-','_');
   //String txt1="Create Table " + fn01+" (";
   int j=0;
   String comma="     ";
   String txt="";
   for (int i=0;i<data.length;i++) {
     String fn = (String) data[i][0];
     fn=fn.replace('-','_');
     if (i==0) {
       jt.setText("Create Table "+fn+" {"+"\n");
     }
     Boolean b=(Boolean)data[i][10];
     boolean selected=b.booleanValue();
     if (!selected) continue;
     String fn2 = "";
     if (i == 0) fn2 = "";
     if (i!=0) fn2=fn01;
     //else if (data[i][1].equals("Redefines")) fn2 = (String) data[i][1];
     //else fn2 = "";
     String offset =(String)data[i][3];;
     Integer ii=new Integer(offset);
     int iii=ii.intValue()+1;
     ii=new Integer(iii);
     String offset1=ii.toString();
     String gr=(String)data[i][1];
     if (gr.length()>0&&!selected) {
       continue;
     }
    // if (!(data[i][3].equals("0"))) offset =(String) (data[i][3]);
     String fl = (String) data[i][5];
     int ifl=new Integer(fl).intValue();
     // 88 level
     if (ifl==0) {
    	 continue;
     }
     String decimal = (String)data[i][7];
     int idec=new Integer(decimal).intValue();
     String integral=(String)data[i][6];
     int iIntegral=new Integer(integral).intValue();
     String ft = (String)data[i][2];

    //String fl=(String)data[i][5];
     boolean isWrite=true;
     if (fn!=null) {
        if (fn.equals("FILLER")) {
          isWrite=false;
           j = j + 1;
           fn = "FILLER" + j;
           continue;
        }
     } else {
       isWrite=false;
       j = j + 1;
       fn="FILLER"+j;
       continue;
     }
     fn=fn.replace('-','_');

     if (ft.equals("COMP-3")) {
       ifl=iIntegral+idec;
       String sfl=new Integer(ifl).toString();
       txt=txt+comma+fn+" Decimal(" + sfl + ","+decimal+")\n";
     } else if (ft.equals("COMP-1")) {
       txt=txt+comma+fn+" Float \n";
     } else if (ft.equals("COMP-2")) {
       txt=txt+comma+fn+" Double \n";
     } else if (ft.equals("COMP")) {
       if (ifl>4) txt=txt+comma+fn+" Integer\n";
       else txt=txt+comma+fn+" smallint\n";
     } else if (ft.equals("NUMERIC")) {
       txt=txt+comma+fn+" Numeric(" + fl + ","+decimal+")\n";
     } else {
       txt=txt+comma+fn+" Varchar(" + fl + ")\n";
     }
      comma="    ,";

     //str[i] = "    " +ft + "  " + fn +  ";  " + "\n";

     //if (isWrite)
      //   jt.setText(jt.getText() + txt);

   }
   jt.setText(jt.getText() + txt);
    jt.setText(jt.getText()+ "\n"+"}");
    jt.setFont(new java.awt.Font("Courier", Font.PLAIN, 12));
     //okBtn.setMaximumSize(new Dimension(31,111));
     GridBagLayout gridBagLayout1 = new GridBagLayout();
     f.getContentPane().setLayout(gridBagLayout1);

     f.getContentPane().add(new JScrollPane(jt),
                            new GridBagConstraints(0, 0, 1, 1, 1, 1
      , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
      new Insets(0, 0, 0, 0), 0, 0));
    f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    f.setSize(500,500);
    f.setVisible(true);

}
/*
  public void jList_valueChanged(ListSelectionEvent e) {
    int rowSelected=0;
    String tableSelected="";
    String tableId="";
    JList src=(JList)e.getSource();

    if (e.getValueIsAdjusting()==false) {
      int[] selectedIndices=src.getSelectedIndices();
   //   System.out.println("selected indices are: ");
      for (int i=0;i<selectedIndices.length;i++) {
        //System.out.println(selectedIndices[i]+" ");
      }
      Object[] selectedValues=src.getSelectedValues();
    //  System.out.println("selected values are: ");
      for (int i=0;i<selectedValues.length;i++) {
      //  System.out.println(selectedValues[i]+" ");
        tableSelected=(String)selectedValues[i];
      }
      StringTokenizer st = new StringTokenizer(tableSelected," ");
      if (st.hasMoreTokens()) {
        tableSelected=st.nextToken();
      }
      if (st.hasMoreTokens()) {
       tableId=st.nextToken();
     }

      getColumns(tableId,tableSelected);
    }



  }

  public void getColumns(String tableId,String tableSelected) {
   JDBCTableAdapter jt=new JDBCTableAdapter();
   int j=tableSelected.indexOf(".");
   if (j>0) {
     tableSelected=tableSelected.substring(j+1,tableSelected.length());
   }
   String sql="Select NAME from sysibm.syscolumns where tbname = '"+
              tableSelected.toUpperCase()+"' AND TBCREATOR = '"+creator.toUpperCase()+"' order by colno";
   String s3="";
   try {
     s3 = jkje.tools.Tools.getSql(jkje.app.Registry.getDbms(),"draw",sysSqlFileName);
   } catch (Exception ee){}

   if (s3.length()>0){
      s3=s3.replaceAll("#tblName",tableSelected);
      s3=s3.replaceAll("#tblCreator",creator);
      sql=s3.trim();
   }

  // System.out.println("sql="+sql);
   Vector v=jt.getRows(sql);
   Enumeration en=v.elements();
   int i=-1;
   while (en.hasMoreElements()) {
     i=i+1;
     String s2=en.nextElement().toString();
     String s1=s2.substring(1,s2.length()-1);
     if (tableId.length()>0)
         data[i][0]=tableId+"."+s1;
     else data[i][0]=s1;
     data[i][1]=new Boolean(false);
   }
   model.fireTableDataChanged();

  }
*/
  //String[][] data=new String[100][2];
  Boolean t=new Boolean(true); //.booleanValue();
  Boolean f=new Boolean(false); //.booleanValue();
  //Object[][] data={{"row1",t},{"row2",f}};
  //Object[][] data=new Object[200][2];
  //boolean[] bs=new boolean[jTable.getColumnCount()];
  String[] columnNames={
      "Filed Name",
      "Redefine/Group",
      "Field Type",
      "Start" ,
      "End",
      "Field Length",
      "Integral",
      "Decimal Places",
      "Signed?",
      "PIC Pattern",
      "Check to Select"};
 // String[] columnNames2={"Filed Name", "Redefine/Group","Field Type", "Start" ,"End", "Field Length", "Decimal Places", "PIC Pattern"};
  //String[] columnNames=new String[columnNames2.length+1];
  Border border9 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border10 = new TitledBorder(border9, "Enter Report Title");
  Border border2 = BorderFactory.createEtchedBorder(Color.white,
      new Color(178, 178, 178));
  Border border11 = new TitledBorder(border2, "Check the required columns");
  Border border12 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border13 = new TitledBorder(border12, "Print options");
  Border border14 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border15 = new TitledBorder(border14, "Display/Print options");
  Border border16 = BorderFactory.createEtchedBorder(Color.white,
      new Color(178, 178, 178));
  Border border17 = new TitledBorder(border16,
                                     "Check the required columns to display");
  Border border18 = BorderFactory.createEtchedBorder(Color.white,
      new Color(178, 178, 178));
  Border border19 = new TitledBorder(border18, "Check the required columns");
  Border border20 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border21 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));

  JTextField jTextField=new JTextField();
  JTextField jTextField2=new JTextField();
  JLabel jLabel=new JLabel(" to ");
  Border border22 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border23 = new TitledBorder(border22, "Ra");
  Border border24 = BorderFactory.createEtchedBorder(Color.white,
      new Color(178, 178, 178));
  Border border25 = new TitledBorder(border24, "View");
  Border border26 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border27 = new TitledBorder(border26, "View");
  Border border28 = BorderFactory.createEtchedBorder(Color.white,
      new Color(178, 178, 178));
  Border border29 = new TitledBorder(border28, "Range");
  Border border30 = BorderFactory.createEtchedBorder(Color.white,
      new Color(178, 178, 178));
  Border border31 = new TitledBorder(border30, "Range option");
  Border border32 = BorderFactory.createEtchedBorder(EtchedBorder.RAISED,
      Color.white, new Color(178, 178, 178));
  Border border33 = new TitledBorder(border32, "View option");

  public AbstractTableModel createModel1() {

       AbstractTableModel model = new AbstractTableModel() {

       @Override
	public int getRowCount() {
         return data.length;
       }

       @Override
	public Class getColumnClass(int column) {

            if (column == 10) {
              return Boolean.class;
              //String str=(String)data[row][0];
              //if (str.length()>0) return Boolean.class;
            }

          return String.class;
       }



       @Override
	public int getColumnCount() {

         return columnNames.length;

       }


      @Override
	public String getColumnName (int column) {

        return columnNames[column];
      }



      @Override
	public Object getValueAt(int row, int col) {
        //if (col==1)
        //  if (data[row][0]==null) {
        //    return null;
        //  }
        return data[row][col];
       // return super.getValueAt(row,col);
      }// end getValue at

      @Override
	public void setValueAt(Object value, int row, int column) {
        if (column==10) {
          Boolean b=(Boolean)(value);
          data[row][column] = b;
        } else
            data[row][column] = value;
     }

      @Override
	public boolean isCellEditable(int row, int column) {

         if (column==0||column==10){
             return true;
         } else return false;
      }



    };
    return model;

  }



//cancel
  public void jButton1_actionPerformed(ActionEvent e) {
    setVisible(false);
    dispose();
  }


  class MyWindowAdapter extends WindowAdapter {
      @Override
	public void windowClosing(WindowEvent e) {
        System.exit(0);
     }

   }


   class ListSelectionAction implements ListSelectionListener {
     private final static long serialVersionUID=1L;
     @Override
	public void valueChanged(ListSelectionEvent e) {
       int minIndex=0, maxIndex=0;

       ListSelectionModel lsm = (ListSelectionModel)e.getSource();
       //System.out.println(
       //     "JSelectTableListPane/ListSelectionAction getValueIsAdjusting="
       //     +lsm.getValueIsAdjusting()
       //     +" isSelectionEmpty()="+lsm.isSelectionEmpty());
       if (lsm.getValueIsAdjusting())
         return;
       if (lsm.isSelectionEmpty()) {
         //System.out.println("No row is selected.");
       }
       else {
         minIndex = lsm.getMinSelectionIndex();
         maxIndex = lsm.getMaxSelectionIndex();
         rowSelected = minIndex;
       }
       //System.out.println(
       //    "JSelectTableListPane/ListSelectionAction min selected="+rowSelected
       //    +" max selected="+maxIndex);
     }
    }




}// end class


class JCopybookViewer_jButton1_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton1_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {

    adaptee.jButton1_actionPerformed(e);
  }
}


class JCopybookViewer_jButton2_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton2_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {

    adaptee.jButton2_actionPerformed(e);
  }
}

class JCopybookViewer_jButton3_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton3_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton3_actionPerformed(e);
  }
}

class JCopybookViewer_jButton4_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton4_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton4_actionPerformed(e);
  }
}

class JCopybookViewer_jButton5_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton5_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton5_actionPerformed(e);
  }
}

class JCopybookViewer_jButton6_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton6_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton6_actionPerformed(e);
  }
}


class JCopybookViewer_jButton7_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton7_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton7_actionPerformed(e);
  }
}

class JCopybookViewer_jButton8_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton8_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton8_actionPerformed(e);
  }
}

class JCopybookViewer_jButton9_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton9_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton9_actionPerformed(e);
  }
}

class JCopybookViewer_jButton10_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton10_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }

  @Override
public void actionPerformed(ActionEvent e) {
     adaptee.jButton10_actionPerformed(e);
  }
}

class JCopybookViewer_jButton11_actionAdapter
    implements ActionListener {
  private JCopybookViewer adaptee;
  JCopybookViewer_jButton11_actionAdapter(JCopybookViewer adaptee) {
    this.adaptee = adaptee;
  }


  @Override
public void actionPerformed(ActionEvent e) {
    adaptee.jButton11_actionPerformed(e);
  }
}

