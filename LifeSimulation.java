
package gameOfLife;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.*;  // Needed for ActionListener

class LifeSimulationGUI extends JFrame implements ActionListener, ChangeListener, MouseListener, MouseMotionListener
{
    //declare instant variables
     Colony colony = new Colony (0.1);
     JSlider speedSldr = new JSlider ();
     Timer t;
     int ro, co, si, fil;
     int right;
     JComboBox row, col, size, dir, files;
     boolean populate, eradicate;

    //======================================================== constructor
    public LifeSimulationGUI ()//constructor called LifeSimulationGUI
    {
        // 1... Create/initialize components
        JButton simulateBtn = new JButton ("Simulate");//create JButton "Simulate"
        simulateBtn.addActionListener (this);//add ActionListener to simulateBtn
        JButton populate= new JButton ("Populate");//create JButton "Populate"
        JButton eradicate= new JButton("Eradicate");//create JButton "Eradicate"
        JButton load= new JButton("Load");//create JButton "Load"
        JButton save= new JButton("Save");//create JButton "Save"
        addMouseListener(this);//addMouseListener
        
        String [] s= new String[colony.getRow()/2];

        for( int x= 0; x < s.length; x ++ ){//declare as 0; loop so long as x is less than s.length; add x by 1
            s[x]= "" + x;//add x as String to s[x]
        }
        
        //create file as String for JComboBox files
        String [] file= {"SaveFiles.txt", "triangle.txt", "sierpinskiTriangle.txt", "Patterns.txt"};
        
        //set the attributes of the JComboBox
        files= new JComboBox(file);
        files.setSelectedIndex(0);
        files.addActionListener(files);
        
        //set the attributes of the JComboBox
        size= new JComboBox(s);
        size.setSelectedIndex(0);
        size.addActionListener(size);
        
        size.addActionListener(this);//add ActionListener to size
        
        //add ActionListener to each JButton
        populate.addActionListener(this);
        eradicate.addActionListener(this);
        load.addActionListener(this);
        save.addActionListener(this);
        speedSldr.addChangeListener (this);
        
        
        
        // 2... Create content pane, set layout
        JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
        JPanel north = new JPanel ();
        north.setLayout (new FlowLayout ()); // Use FlowLayout for input area

        DrawArea board = new DrawArea (500, 500);
//        board.addMouseMotionListener(this);
        // 3... Add the components to the input area.

        north.add (simulateBtn);
        north.add (populate);
        north.add (eradicate);
        north.add (load);
        north.add (save);
        north.add (size);
        north.add (files);
        north.add (speedSldr);

        content.add (north, "North"); // Input area
        content.add (board, "South"); // Output area

        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setTitle ("Life Simulation Demo");
        setSize (510, 570);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.
    }

    public void stateChanged (ChangeEvent e)//method called stateChanged
    {
        if (t != null)
            t.setDelay (400 - 4 * speedSldr.getValue ()); // 0 to 400 ms
    }

    public void actionPerformed (ActionEvent e)//actionPerformed
    {
        //determine the indexes selexted from each JComboBox
        fil= files.getSelectedIndex();        
        si= size.getSelectedIndex();
        
        if (e.getActionCommand ().equals ("Simulate"))//if Simulate is pressed
        {
            Movement moveColony = new Movement (colony); // ActionListener
            t = new Timer (200, moveColony); // set up timer
            t.start (); // start simulation
        }
        if (e.getActionCommand ().equals ("Populate"))//if populate is pressed
        {
//                colony.populate(ro, co, si);
            
            populate = true;//declare populate as true
            eradicate= false;//declare eradicate as false
        }
        if (e.getActionCommand ().equals ("Eradicate"))//if eradicate is pressed
        {
//            colony.eradicate(ro, co, si);
            eradicate= true;//declare eradicate as true
            populate= false;//declare populate as false
        }
        if (e.getActionCommand ().equals ("Load"))//if load is pressed
        {
            try//try
            {
                if( fil == 0 )//determines whether fil is equal to 0
                    colony.load("SaveFiles.txt");//load SaveFiles.txt
                if( fil == 1 )//determines whether fil is equal to 1
                    colony.load("triangle.txt");//load triangle.txt
                if( fil == 2 )//determines whether fil is equal to 2
                    colony.load("sierpinskiTriangle.txt");//load sierpinskiTriangle.txt
                if( fil == 3 )//determines whether fil is equal to 3
                    colony.load("Patterns.txt");//load Patterns.txt
            } 
            catch (IOException ex) {}//catch
        }
        if (e.getActionCommand ().equals ("Save"))//if save is pressed
        {
            try//try
            {
                colony.save();//save colony
            } 
            catch (IOException ex) {}//catch
        }
                    
                    
        repaint ();            // refresh display of deck
    }
    
    public void mouseClicked(MouseEvent e) { // Respond to mouse click
    //determines which cell in the colony/grid is pressed
    co = (e.getX())/5 - 2;
    ro = (e.getY() - 2)/5 - 12;
    
    //determines which size is selected from the JComboBox
    si= size.getSelectedIndex();
    
    System.out.println("" + ro);
    System.out.println("" + co);
    
      if( populate )//determines whether populate evaluates to true
          colony.populate(ro, co, si);//activate method populate
      if( eradicate )//determines whether eradicate evaluates to true
          colony.eradicate(ro, co, si);//activate method eradicate
      
  }  
  
  public void mousePressed(MouseEvent e) { // All interface methods must be defined
  }
  
  public void mouseReleased(MouseEvent e) { // All interface methods must be defined
  }
  
  public void mouseEntered(MouseEvent e) { // All interface methods must be defined
  }
  
  public void mouseExited(MouseEvent e) { // All interface methods must be defined
  }
  public void mouseDragged(MouseEvent e){
    
  }
  public void mouseMoved(MouseEvent e){
      
  }


    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)//constructor called DrawArea
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)//method called PaintComponent
        {
            colony.show (g);//activate method called show
        }
    }
 
    class Movement implements ActionListener//method called Movement
    {
        private Colony colony;//declare instant variable

        public Movement (Colony col)//constructor called Movement
        {
            colony = col;//colony is equal to col
        }

        public void actionPerformed (ActionEvent event)//method called actionPerformed
        {
            colony.advance ();//activate method advance
            repaint ();//repaint
        }
    }
    


//class main//main method
//{
    //======================================================== method main
    public static void main (String[] args)//main
    {
        LifeSimulationGUI window = new LifeSimulationGUI ();//create window
        window.setVisible (true);//let window be seen
    }
//}
}




