/**
 * Program for helping improve at CSing in League of Legends.
 * 
 * By Evan Choquette AKA VPG Darkstar
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * This class does various analysis for cs at given times.
 */

public class CSAnalysis extends JFrame{

    //Components for GUI
    private JMenuBar menuBar;                   //Menu bar
    private JMenu fileMenu;                     //The File menu
    private JMenu viewMenu;                     //The view menu
    private JMenuItem saveItem;                 //Save the cs data
    private JMenuItem loadItem;                 //Load cs data
    private JMenuItem exitItem;                 //To exit
    private JMenuItem viewItem;                 //To view all past games
    private JPanel comboPanel;			            // A holding panel
    private JPanel inputPanel;                  // A holding panel for the inputs 
    private JPanel analyzePanel;                // A holding panel for the calculate button
    private JLabel messageLabel1;					      // A message to the user
    private JLabel messageLabel2;	            	// A message to the user
    private JLabel messageLabel3;		            // A message to the user
    private JLabel comboLabel;                  // A message to the user
    private JTextField csAmount5;               // To hold user input
    private JTextField csAmount10;              // To hold user input
    private JTextField csAmount13;              // To hold user input
    private JButton analyzeButton;              // Analyze button
    private JComboBox laneType;                 //Combo box for the lane
    
    //Array of lanes
    String[] lanes = {"Select one...", "Top Lane", "Mid Lane", "Bot Lane"};
    
    //ArrayList of games
    ArrayList<Game> games;                      

    /**
     * Constructor
     */
    
    public CSAnalysis(){
        //ArrayList of games
        games = new ArrayList<Game>();
        
        // Set the title
        setTitle("CS Analyzer v2.0");

        //Create a new BorderLayout manager
        setLayout(new BorderLayout());

        // Centers the window
        setLocationRelativeTo(null);

        // Specify an action for the close button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Build the panels and add them to the frame
        buildInputPanel();
        buildComboPanel();
        buildAnalyzePanel();

        // Add the panel to the frame's content pane
        add(inputPanel, BorderLayout.NORTH);
        add(comboPanel, BorderLayout.CENTER);
        add(analyzePanel, BorderLayout.SOUTH);
        
        //Build the menu bar
        buildMenuBar();

        //Clean up and display the window
        pack();
        setVisible(true);
    }
    
    /**
     * Private inner class creates new window for displaying past game data
     */
    
    private class PastDataWindow extends JFrame{
        //Components for the second window
        private JPanel viewPanel;       //A holding panel
        private JTextArea textArea;     //A text area to display past game info
        private JScrollPane scrollPane; //A scroll pane for the window

        /**
         * Constructor
         */

        public PastDataWindow(){
            // Set the title
            setTitle("Past CS Data Viewer");

            //Create a new BorderLayout manager
            setLayout(new BorderLayout());

            // Centers the window
            setLocationRelativeTo(null);
            
            //Build the view panel
            buildViewPanel();
            
            //Add text area to the window
            add(viewPanel, BorderLayout.CENTER);
            
            //Clean up and display the window
            pack();
            setVisible(true);
        }
        
        /**
         * buildViewPanel method creates the text area with the data.
         */
        
        private void buildViewPanel(){
            //Create the view panel
            viewPanel = new JPanel();
            
            //Create border around text area
            viewPanel.setBorder(new TitledBorder(new EtchedBorder(), "Saved Game Data" ));
            
            //Create the text area with the scroll pane
            textArea = new JTextArea(16, 36);
            textArea.setEditable(false);
            scrollPane = new JScrollPane(textArea);
            
            //Show the scroll bar
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

            //Add the data to the text area
            textArea.append("Lane:" + "\t" + "CS at 5:" + "\t" + "CS at 10:" 
                            + "\t" + "CS at 13:" + "\t"+ "Grade:" + "\n");
            
            String formatedString = games.toString()
                .replace(",", "")  //remove the commas
                .replace("[", "")  //remove the right bracket
                .replace("]", "")  //remove the left bracket
                .trim();           //remove trailing spaces from partially initialized arrays
            
            //Add game data
            textArea.append(" " + formatedString);
            
            //Add to the panel
            viewPanel.add(scrollPane);
        }
    }

    /**
     * The buildInputPanel method adds a label and 3 text fields.
     */
    private void buildInputPanel(){
        //Create the holding panel
        inputPanel = new JPanel();

        //Create a GridLayout manager for the panel with 3 rows and 2 columns
        inputPanel.setLayout(new GridLayout(3,2));

        // Create the labels and text fields
        messageLabel1 = new JLabel("\tEnter CS at 5 minutes: ");
        csAmount5 = new JTextField(5);
        messageLabel2 = new JLabel("\tEnter CS at 10 minutes: ");
        csAmount10 = new JTextField(5);
        messageLabel3 = new JLabel("\tEnter CS at 13 minutes: ");
        csAmount13 = new JTextField(5);

        //Add the components to the panel
        inputPanel.add(messageLabel1);
        inputPanel.add(csAmount5);
        inputPanel.add(messageLabel2);
        inputPanel.add(csAmount10);
        inputPanel.add(messageLabel3);
        inputPanel.add(csAmount13);
    }

    /**
     * buildComboPanel method to create the combo box
     */

    private void buildComboPanel(){
        //Create the combo panel
        comboPanel = new JPanel();
        
        //Combo box
        laneType = new JComboBox(lanes);
        
        //Allow the user to type input into combo field
        laneType.setEditable(true);
        
        comboLabel = new JLabel("Lane: ");
        
        //Add the components to the panel
        comboPanel.add(comboLabel);
        comboPanel.add(laneType);
    }

    /**
     * buildAnalyzePanel method to create the analyze button
     */

    private void buildAnalyzePanel(){
        //Create the buttonPanel
        analyzePanel = new JPanel();

        //Create the button
        analyzeButton = new JButton("Analyze");

        // Add an action listener to the button
        analyzeButton.addActionListener(new ButtonListener());

        //Add the button to the panel
        analyzePanel.add(analyzeButton);
    }
    
    /**
     * The buildMenuBar method builds the menu bar.
     */
    
    private void buildMenuBar(){
        //Create the manu bar
        menuBar = new JMenuBar();
        
        //Create the file and run menus
        buildFileMenu();
        buildViewMenu();
        
        //Add the file and run menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        
        //Set the window's menu bar
        setJMenuBar(menuBar);
    }
    
    /**
     * The buildFileMenu method builds the File Menu and
     * returns a reference to its JMenu object.
     */
    
    private void buildFileMenu(){
        //Create a Save menu item
        saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.addActionListener(new SaveListener());
        
        //Create a Load menu item
        loadItem = new JMenuItem("Load");
        loadItem.setMnemonic(KeyEvent.VK_L);
        loadItem.addActionListener(new LoadListener());

        //Create an Exit menu item
        exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.addActionListener(new ExitListener());
        
        //Create a JMenu object for the file menu
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        
        //Add the items to the menu
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exitItem);
    }
    
    /**
     * The buildViewMenu method builds the View Menu and
     * returns a reference to its JMenu object.
     */
    
    private void buildViewMenu(){
        //Create a Run Payroll menu item
        viewItem = new JMenuItem("View CS History");
        viewItem.setMnemonic(KeyEvent.VK_H);
        viewItem.addActionListener(new ViewListener());
        
        //Create a JMenu object for the run menu
        viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        
        //Add the item to the menu
        viewMenu.add(viewItem);
    }
    
    /**
     * Private inner class that handles the event for when the user selects
     * Exit from the File menu.
     */
    
    private class ExitListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }
    
    /**
     * Private inner class that handles the event for when the user selects
     * Save from the File menu.
     */
    
    private class SaveListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //Save the employees to a file the user chooses
            JFileChooser fileChooser = new JFileChooser();
            int status = fileChooser.showSaveDialog(null);
            
            //Check if they actually saved the file
            if (status == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                
                //Try block to make sure file actually exists
                try {
                    FileOutputStream fos = new FileOutputStream(fileToSave);
                    ObjectOutputStream out = new ObjectOutputStream(fos);
                    out.writeObject(games);
                    
                    //Close the file
                    fos.close();
                    out.close();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error: File not found");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: File I/O problem");
                }
                
                //Tell the user it saved successfully
                JOptionPane.showMessageDialog(null, "Saved as file: " + fileToSave.getAbsolutePath());
            }
        }
    }
    
    /**
     * Private inner class that handles the event for when the user selects
     * Load from the File menu.
     */
    
    private class LoadListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //Load in games from a file
            JFileChooser fileChooser = new JFileChooser();
            int status = fileChooser.showOpenDialog(null);
            
            //Check if user selected to open something
            if (status == JFileChooser.APPROVE_OPTION) {
                //Read the data from the file    
                File fileToOpen = fileChooser.getSelectedFile();
                
                try {
                    //Read in the game objects to the ArrayList
                    FileInputStream fis = new FileInputStream(fileToOpen);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    games = (ArrayList<Game>) ois.readObject();
                    
                    //Close the file
                    fis.close();
                    ois.close();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error: File not found");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: File I/O problem");
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Class not found");
                }
                
                //Tell the user that it opened successfully
                JOptionPane.showMessageDialog(null,"Loaded in past game data from: " + fileToOpen.getAbsolutePath());
            }
        }
    }
    
    /**
     * Private inner class that handles the event for when the user selects
     * Run Payroll from the Run menu.
     */
    
    private class ViewListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //Try block to create window if data is loaded
            try{
                //Open a new window with the past game data
                new PastDataWindow();
            }catch(NullPointerException ex){
                JOptionPane.showMessageDialog(null, "Error! No data loaded!","Error!",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Private inner class that handles the event when
     * the user clicks the analyze button
     */
    
    private class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            String input1, input2, input3, currentLane;	// To hold the user's input and then the current lane
            char grade = 'Z';                   //To hold user's cs grade
            double result1 = 0.0;	// To hold the percentage
            double result2 = 0.0;	// To hold the percentage
            double result3 = 0.0;	// To hold the percentage
            double resultAvg = 0.0;     // Average of results
            final ImageIcon icon = new ImageIcon(this.getClass().getResource("minion.png")); //Icon for display window

            // Get the cs entered
            input1 = csAmount5.getText();
            input2 = csAmount10.getText();
            input3 = csAmount13.getText();

            // Determine which lane was chosen
            if(laneType.getSelectedIndex() == 0){
                JOptionPane.showMessageDialog(null, "Error! Choose a lane!","Error!",JOptionPane.ERROR_MESSAGE);
            }
            else if(laneType.getSelectedIndex() == 1){
                //Get percentage
                result1 = (Double.parseDouble(input1) / 38.0) * 100.0;
                result2 = (Double.parseDouble(input2) / 101.0) * 100.0;
                result3 = (Double.parseDouble(input3) / 139.0) * 100.0;
                
                //Set the current lane
                currentLane = "Top Lane";
                
                //Check if CS is valid
                if(result1 > 100 || result2 > 100 || result3 > 100){
                    JOptionPane.showMessageDialog(null, "Error! Invalid CS amounts!","Error!",JOptionPane.ERROR_MESSAGE);
                }
                
                //Calculate average of three percentages
                resultAvg = ((result1 + result2 + result3)) / 3.0;

                if(resultAvg <= 60.0){
                    grade = 'F';
                }else if(resultAvg <= 70.0){
                    grade = 'D';
                }else if(resultAvg <= 80.0){
                    grade = 'C';
                }else if(resultAvg <= 90.0){
                    grade = 'B';
                }else if(resultAvg <= 100.0){
                    grade = 'A';
                }
                
                //Add the data to the ArrayList
                games.add(new Game(currentLane, Integer.parseInt(input1), Integer.parseInt(input2), Integer.parseInt(input3), grade));
            }
            else if(laneType.getSelectedIndex() == 2){
                //Get percentage
                result1 = (Double.parseDouble(input1) / 44.0) * 100.0;
                result2 = (Double.parseDouble(input2) / 107.0) * 100.0;
                result3 = (Double.parseDouble(input3) / 145.0) * 100.0;
                
                //Set the current lane
                currentLane = "Mid Lane";
                
                //Check if CS is valid
                if(result1 > 100 || result2 > 100 || result3 > 100){
                    JOptionPane.showMessageDialog(null, "Error! Invalid CS amounts!","Error!",JOptionPane.ERROR_MESSAGE);
                }
                
                //Calculate average of three percentages
                resultAvg = ((result1 + result2 + result3)) / 3.0;

                if(resultAvg <= 60.0){
                    grade = 'F';
                }else if(resultAvg <= 70.0){
                    grade = 'D';
                }else if(resultAvg <= 80.0){
                    grade = 'C';
                }else if(resultAvg <= 90.0){
                    grade = 'B';
                }else if(resultAvg <= 100.0){
                    grade = 'A';
                }
                
                //Add the data to the ArrayList
                games.add(new Game(currentLane, Integer.parseInt(input1), Integer.parseInt(input2), Integer.parseInt(input3), grade));
            }
            else if(laneType.getSelectedIndex() == 3){
                //Get percentage
                result1 = (Double.parseDouble(input1) / 38.0) * 100.0;
                result2 = (Double.parseDouble(input2) / 101.0) * 100.0;
                result3 = (Double.parseDouble(input3) / 139.0) * 100.0;
                
                //Set the current lane
                currentLane = "Bot Lane";
                
                //Check if CS is valid
                if(result1 > 100 || result2 > 100 || result3 > 100){
                    JOptionPane.showMessageDialog(null, "Error! Invalid CS amounts!","Error!",JOptionPane.ERROR_MESSAGE);
                }
                
                //Calculate average of three percentages
                resultAvg = ((result1 + result2 + result3)) / 3.0;

                if(resultAvg <= 60.0){
                    grade = 'F';
                }else if(resultAvg <= 70.0){
                    grade = 'D';
                }else if(resultAvg <= 80.0){
                    grade = 'C';
                }else if(resultAvg <= 90.0){
                    grade = 'B';
                }else if(resultAvg <= 100.0){
                    grade = 'A';
                }
                
                //Add the data to the ArrayList
                games.add(new Game(currentLane, Integer.parseInt(input1), Integer.parseInt(input2), Integer.parseInt(input3), grade));
            }
            
            // Display the results
            if(result1 > 100 || result2 > 100 || result3 > 100){
            }else {
                JOptionPane.showMessageDialog(null, String.format(input1 + " CS is %.1f%% of the max possible at 5 minutes.\n" +
                    input2 + " CS is %.1f%% of the max possible at 10 minutes.\n" + 
                    input3 + " CS is %.1f%% of the max possible at 13 minutes.\n\n" +
                    "Overall CS performance: %c\n",result1,result2,result3,grade),
                    "CS Analyzer Report Card v2.0",JOptionPane.PLAIN_MESSAGE,icon);
            }
        }

    }
        
    public static void main(String[] args) {
        //Run the analysis
        new CSAnalysis();      
    }
}
