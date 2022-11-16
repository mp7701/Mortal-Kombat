import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.MouseInfo;
import java.awt.Font.*;

public class MortalKombat1 implements KeyListener
{
   int character1 = 2,character2 = (int)(5 * Math.random() + 1);
   JFrame frame = new JFrame("Mortal Kombat");
   Drawing draw = new Drawing();
   Host animation = new Host(); // animation thread 
   Player one = new Player(character1,200,450);
   Player two = new Player(character2,700,450);
   Player cage = new Player(1,700,450);
   Player sonya = new Player(2,700,450);
   Player raiden = new Player(3,700,450);
   Player liuKang = new Player(4,700,450);
   Player scorpion = new Player(5,700,450);
   // array indices: 0 = W, 1 = S, 2 = A, 3 = D, 4 = J, 5 = I, 6 = L
   boolean [] p1Keys = new boolean [7];
   boolean [] p2Keys = new boolean[7];
   int [] JReaction = {2,3,6,4}; // array for jump reactions
   int [] DReaction = {6,0,1}; //array for duck reactions
   int [] PReaction = {5,1,6,4}; //array for punch reactions
   int [] BReaction = {6,2,3}; // array for block reactions
   int [] KReaction = {1,0,6}; //array for kick reactions
   int [] WReaction = {0,1,2,3,4,5,6}; //array for walk reactions
   int randJumpReaction = (int)(Math.random() * 4);
   int randDuckReaction = (int)(Math.random() * 3);
   int randPunchReaction = (int)(Math.random() * 4);
   int randBlockReaction = (int)(Math.random() * 3);
   int randKickReaction = (int)(Math.random() * 3);
   int randWalk = (int)(Math.random() * 7); 
   boolean singlePlayer = false;
   boolean actionHappened = false;
   long start1;
   long start2;
   boolean oneHit = false;
   boolean twoHit = false;
   int oneScore = 0;
   int twoScore = 0;
   boolean gameOver = false;
   boolean uppercut1 = false;
   boolean lowkick1 = false;
   boolean uppercut2 = false;
   boolean lowkick2 = false;
   boolean flipped = false; // keeps track of orientation of images
   Polygon [] hitDetection = new Polygon [10];
   double seconds = 99.0;
   
   ImageIcon startscreen = new ImageIcon("Images/Menu Backgrounds/startscreen.jpg");
   ImageIcon instructions = new ImageIcon("Images/Menu Backgrounds/instructionsBackground.jpg");
   ImageIcon gamemodeSelect = new ImageIcon("Images/Menu Backgrounds/GameModeSelect.jpg");
   ImageIcon fighterSelect = new ImageIcon("Images/Menu Backgrounds/ChooseFighter.jpg");
   
   ImageIcon headshotOne = new ImageIcon("Images/Headshots/character1_HEADSHOT.jpg");
   ImageIcon headshotTwo = new ImageIcon("Images/Headshots/character2_HEADSHOT.jpg");
   ImageIcon headshotThree = new ImageIcon("Images/Headshots/character3_HEADSHOT.jpg");
   ImageIcon headshotFour = new ImageIcon("Images/Headshots/character4_HEADSHOT.jpg");
   ImageIcon headshotFive = new ImageIcon("Images/Headshots/character5_HEADSHOT.jpg");
   ImageIcon headshotSix = new ImageIcon("Images/Headshots/character6_HEADSHOT.jpg");
   ImageIcon headshotSeven = new ImageIcon("Images/Headshots/character7_HEADSHOT.jpg");
   ImageIcon headshotTen = new ImageIcon("Images/Headshots/character10_HEADSHOT.jpg");
   
   ImageIcon background_1 = new ImageIcon("Images/Arenas/randomBackground_1.jpg");
   ImageIcon background_2 = new ImageIcon("Images/Arenas/randomBackground_2.jpg");
   ImageIcon background_3 = new ImageIcon("Images/Arenas/randomBackground_3.jpg");
   ImageIcon background_4 = new ImageIcon("Images/Arenas/randomBackground_4.jpg");
   ImageIcon background_5 = new ImageIcon("Images/Arenas/randomBackground_5.jpg");
   ImageIcon background_6 = new ImageIcon("Images/Arenas/randomBackground_6.jpg");
   
   int x = 0, y = 0, screen = 0, xx = 0, yy = 0;
   int background = (int)(6 * Math.random() + 1);
   Color darkGreen = new Color(0,153,0);

   public MortalKombat1()
   {
      one.initialize(); // sets up character 1
      two.initialize(); // sets up character 2
      cage.initialize();
      sonya.initialize();
      raiden.initialize();
      liuKang.initialize();
      scorpion.initialize();
      for(int j = 0; j < two.imageW.length; j++)
         two.imageW[j]*=-1;
      for(int i = 0; i < 10; i++)
      {
         two.xwalk1[i] = ((two.xwalk1[i] - two.x) * -1) + (two.x);
         two.xfstance1[i] = ((two.xfstance1[i] - two.x) * -1)+ (two.x);
         two.xjump1[i] = ((two.xjump1[i] - two.x) * -1)  + (two.x);
         two.xjump2[i] = ((two.xjump2[i] - two.x) * -1) + (two.x);
         two.xduck1[i] = ((two.xduck1[i] - two.x) * -1) + (two.x);
         two.xpunch1[i] = ((two.xpunch1[i] - two.x) * -1) + (two.x);
         two.xpunch2[i] = ((two.xpunch2[i] - two.x) * -1) + (two.x);
         two.xpunch3[i] = ((two.xpunch3[i] - two.x) * -1) + (two.x);
         two.xpunch6[i] = ((two.xpunch6[i] - two.x) * -1) + (two.x);
         two.xblock1[i] = ((two.xblock1[i] - two.x) * -1) + (two.x);
         two.xkick1 [i]= ((two.xkick1[i] - two.x) * -1)  + (two.x);
         two.xkick2 [i]= ((two.xkick2[i] - two.x) * -1) + (two.x);
         two.xkick3 [i]= ((two.xkick3[i] - two.x) * -1) + (two.x);
         two.xkick4 [i]= ((two.xkick4[i] - two.x) * -1) + (two.x);
         two.xuppercut1[i] = ((two.xuppercut1[i] - two.x) * -1) + (two.x);
         two.xuppercut2[i] = ((two.xuppercut2[i] - two.x) * -1)  + (two.x);
         two.xuppercut3[i] = ((two.xuppercut3[i] - two.x) * -1)  + (two.x);
         two.xuppercut4[i] = ((two.xuppercut4[i] - two.x) * -1) + (two.x);
         two.xlowblock1[i] = ((two.xlowblock1[i] - two.x) * -1) +(two.x);
         two.xlowkick1 [i]= ((two.xlowkick1[i] - two.x) * -1)  + (two.x);
         two.xlowkick2 [i]= ((two.xlowkick2[i] - two.x) * -1)  + (two.x);
         two.xlowkick3 [i]= ((two.xlowkick3[i] - two.x) * -1) + (two.x);
         two.xduck2[i]= ((two.xduck2[i] - two.x) * -1) + (two.x);
         two.xduck3[i]= ((two.xduck3[i] - two.x) * -1) + (two.x);
         two.xwalk2[i]= ((two.xwalk2[i] - two.x) * -1) + (two.x);
         two.xwalk3[i]= ((two.xwalk3[i] - two.x) * -1) + (two.x);
         two.xwalk4[i]= ((two.xwalk4[i] - two.x) * -1) + (two.x);
         two.xwalk5[i]= ((two.xwalk5[i] - two.x) * -1) + (two.x);
         two.xwalk6[i]= ((two.xwalk6[i] - two.x) * -1) + (two.x);
         two.xwalk7[i]= ((two.xwalk7[i] - two.x) * -1) + (two.x);
         two.xwalk8[i]= ((two.xwalk8[i] - two.x) * -1) + (two.x);
         two.xwalk9[i]= ((two.xwalk9[i] - two.x) * -1) + (two.x);
         two.xpunch4[i]= ((two.xpunch4[i] - two.x) * -1) + (two.x);
         two.xpunch5[i]= ((two.xpunch5[i] - two.x) * -1) + (two.x);
         two.xpunch7[i]= ((two.xpunch7[i] - two.x) * -1) + (two.x);
         two.xkick5[i]= ((two.xkick5[i] - two.x) * -1) + (two.x);
         two.xkick6[i]= ((two.xkick6[i] - two.x) * -1) + (two.x);
         two.xuppercut5[i]= ((two.xuppercut5[i] - two.x) * -1) + (two.x);
         two.xlowkick4[i]= ((two.xlowkick4[i] - two.x) * -1) + (two.x);
      }
      two.reinitialize();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1000,900);
      frame.add(draw);
      frame.addKeyListener(this);
      frame.setVisible(true); 
      animation.start();   
      draw.addMouseListener(new MouseListen());
      draw.addMouseMotionListener(new MouseListen2()); 
   }

   /*** keyPressed ***************************************
   * Purpose: Deal with the key being pressed            *
   * Parameters: e - details about the key event         *
   * Returns: none                                       *
   ******************************************************/
   public void keyPressed(KeyEvent e)
   {
      if (singlePlayer == true)
      {

         int typed = e.getKeyCode();
         if (typed == KeyEvent.VK_D) // walk right
         {
            p1Keys[3] = true;
            randWalk = (int)(Math.random() * 7);
            p2Keys[WReaction[randWalk]]= true;
         }
         if (typed == KeyEvent.VK_A) // walk left
         {
            p1Keys[2] = true;
            randWalk = (int)(Math.random() * 7);
            p2Keys[WReaction[randWalk]]= true;
         }
         if (typed == KeyEvent.VK_W) // jump
         {
            if(System.currentTimeMillis() - start1 > one.speed)
               p1Keys[0] = true;
               randJumpReaction = (int)(Math.random() * 4);
               p2Keys[JReaction[randJumpReaction]]= true;
         }
         if (typed == KeyEvent.VK_S) // duck
         {
            if(p1Keys[4] == true)
               uppercut1 = true;
            else if (p1Keys[6] == true)
               lowkick1 = true;
            else
               p1Keys[1] = true;
               randDuckReaction = (int)(Math.random() * 3);
               p2Keys[DReaction[randDuckReaction]]= true;
         }
         if (typed == KeyEvent.VK_J) // punch
         {
            if(System.currentTimeMillis() - start1 > one.speed)
            {
               if(p1Keys[1] == true)
                  uppercut1 = true;
               else
                  p1Keys[4] = true;
            }
            randPunchReaction = (int)(Math.random() * 4);
            p2Keys[DReaction[randDuckReaction]]= true;
         }
         if (typed == KeyEvent.VK_I) // block
         {
            p1Keys[5] = true;
            randBlockReaction = (int)(Math.random() * 3);
            p2Keys[BReaction[randBlockReaction]]= true;
   
         }
         if (typed == KeyEvent.VK_L) //kick
         {
            if(System.currentTimeMillis() - start1 > one.speed)
            {
               if(p1Keys[1] == true)
                  lowkick1 = true;
               else
                  p1Keys[6] = true;
            }
            randKickReaction = (int)(Math.random() * 3);
            p2Keys[KReaction[randKickReaction]]= true;
         }

      }
      else //  Multi Player
      {
         int typed = e.getKeyCode();
         if (typed == KeyEvent.VK_D) // walk right
         {
            p1Keys[3] = true;
         }
         if (typed == KeyEvent.VK_A) // walk left
         {
            p1Keys[2] = true;
         }
         if (typed == KeyEvent.VK_W) // jump
         {
            if(System.currentTimeMillis() - start1 > one.speed)
               p1Keys[0] = true;
         }
         if (typed == KeyEvent.VK_S) // duck
         {
            if(p1Keys[4] == true)
               uppercut1 = true;
            else if (p1Keys[6] == true)
               lowkick1 = true;
            else
               p1Keys[1] = true;
             
         }
         if (typed == KeyEvent.VK_J) // punch
         {
            if(System.currentTimeMillis() - start1 > one.speed)
            {
               if(p1Keys[1] == true)
                  uppercut1 = true;
               else
                  p1Keys[4] = true;
            }
         }
         if (typed == KeyEvent.VK_I) // block
         {
            p1Keys[5] = true;
         }
         if (typed == KeyEvent.VK_L) //kick
         {
            if(System.currentTimeMillis() - start1 > one.speed)
            {
               if(p1Keys[1] == true)
                  lowkick1 = true;
               else
                  p1Keys[6] = true;
            }
      
         }
         if (typed == KeyEvent.VK_RIGHT) // walk right
         {
            p2Keys[3] = true;
         }
         if (typed == KeyEvent.VK_LEFT) // walk left
         {
            p2Keys[2] = true;
         }
         if (typed == KeyEvent.VK_UP) // jump
         {
            if(System.currentTimeMillis() - start2 > two.speed)
               p2Keys[0] = true;
         }
         if (typed == KeyEvent.VK_DOWN) // duck
         {
            if(p2Keys[4] == true)
               uppercut2 = true;
            else if (p2Keys[6] == true)
               lowkick2 = true;
            else
               p2Keys[1] = true;     
         }
         if (typed == KeyEvent.VK_4) // punch NUMPAD
         {
            if(System.currentTimeMillis() - start2 > two.speed)
            {
               if(p2Keys[1] == true)
                  uppercut2 = true;
               else
                  p2Keys[4] = true;
            }  
         }
         if (typed == KeyEvent.VK_8) // block NUMPAD
         {
            p2Keys[5] = true;
         }
         if (typed == KeyEvent.VK_6) //kick NUMPAD
         {
            if(System.currentTimeMillis() - start2 > two.speed)
            {
               if(p2Keys[1] == true)
                  lowkick2 = true;
               else
                  p2Keys[6] = true;
            }
         }
      }
   }
   
   
   /*** keyReleased **************************************
   * Purpose: Deal with the key being released           *
   * Parameters: e - details about the key event         *
   * Returns: none                                       *
   ******************************************************/
   public void keyReleased(KeyEvent e)
   {
      if (singlePlayer == true)
      {
         
         int typed = e.getKeyCode();
         if (typed == KeyEvent.VK_D) // walk right
         {
            p1Keys[3] = false;
            p2Keys[WReaction[randWalk]]= false;
         }
         if (typed == KeyEvent.VK_A) // walk left
         {
            p1Keys[2] = false;
            p2Keys[WReaction[randWalk]]= false;
         }
         if (typed == KeyEvent.VK_W) // jump
         {
            p2Keys[JReaction[randJumpReaction]]= false; 
         }
         if (typed == KeyEvent.VK_S) // duck
         {
            p1Keys[1] = false;
            uppercut1 = false;
            lowkick1 = false;
            p2Keys[DReaction[randDuckReaction]]= false;
         }
         if (typed == KeyEvent.VK_J) // punch
         {  
            p2Keys[DReaction[randDuckReaction]]= false;     
         }
         if (typed == KeyEvent.VK_I) // block
         {
            p1Keys[5] = false;
            p2Keys[BReaction[randBlockReaction]]= false;
         }
         if (typed == KeyEvent.VK_L) // kick
         {
            p2Keys[KReaction[randKickReaction]]= false;
   
         }

      
      }
      else  //Multi Player
      {   
         int typed = e.getKeyCode();
         if (typed == KeyEvent.VK_D) // walk right
         {
            p1Keys[3] = false;
         }
         if (typed == KeyEvent.VK_A) // walk left
         {
            p1Keys[2] = false;
         }
         if (typed == KeyEvent.VK_W) // jump
         {
         }
         if (typed == KeyEvent.VK_S) // duck
         {
            p1Keys[1] = false;
         }
         if (typed == KeyEvent.VK_J) // punch
         {
        
         }
         if (typed == KeyEvent.VK_I) // block
         {
            p1Keys[5] = false;
         }
         if (typed == KeyEvent.VK_L) // kick
         {
         }
          if (typed == KeyEvent.VK_RIGHT) // walk right
         {
            p2Keys[3] = false;
         }
         if (typed == KeyEvent.VK_LEFT) // walk left
         {
            p2Keys[2] = false;
         }
         if (typed == KeyEvent.VK_UP) // jump
         {
         }
         if (typed == KeyEvent.VK_DOWN) // duck
         {
            p2Keys[1] = false;
         }
         if (typed == KeyEvent.VK_4) // punch NUMPAD
         {
         }
         if (typed == KeyEvent.VK_8) // block NUMPAD
         {
            p2Keys[5] = false;
         }
         if (typed == KeyEvent.VK_6) // kick NUMPAD
         {
         }
      
      
      }

   }
   public void keyTyped(KeyEvent e)
   {
   }
   
  
   
   
   class Drawing extends JComponent
   {
      /*** paint ********************************************
      * Purpose: Prints the number of times the button was  *
      *          clicked                                    *
      * Parameters: g - Graphics object for drawing on      *
      * Returns: none                                       *
      ******************************************************/
      public void paint(Graphics g)
      {
         Font main = new Font("Serif", Font.BOLD, 55);
         
         Font words = new Font("Serif", Font.PLAIN, 30);
         
         Font headings = new Font("Serif", Font.BOLD, 30);
            
         if (screen == 0)
         {
            g.drawImage(startscreen.getImage(),0,0,1000,870,this);
            g.setFont(main);
            g.setColor(Color.green);

            
            if (x > 350 && x < 610 && y < 760 && y > 710)
            {
               screen = -1;
               draw.repaint();
            }
            else if (x > 350 && x < 610 && y > 770 && y < 810)
            {
               screen = 1;
               draw.repaint();
            }
            
            if (xx > 350 && xx < 610 && yy < 760 && yy > 710)
            {
               g.setColor(Color.blue);
               g.drawString("Instructions",350,760);
               g.setColor(Color.green);
               g.drawString("Play Game",350,806);
               draw.repaint();
            }
            else if (xx > 350 && xx < 610 && yy > 770 && yy < 810)
            {
               g.setColor(Color.green);
               g.drawString("Instructions",350,760);
               g.setColor(Color.blue);
               g.drawString("Play Game",350,806);
               draw.repaint();
            }
            else
            {
               g.setColor(Color.green);
               g.drawString("Instructions",350,760);
               g.drawString("Play Game",350,806);
               draw.repaint();
            } 
         }
         else if (screen == -1)
         {
            g.drawImage(instructions.getImage(),0,0,1000,870,this);
            g.setFont(main);
            g.setColor(Color.yellow);
            g.drawString("Controls",350,60);
            g.setFont(words);
            g.drawString("Back",20,40);
            g.drawRect(10,10,80,45);
            g.setColor(Color.yellow);
            g.setFont(headings);
            g.drawString("Player One:",100,100);
            g.drawString("Player Two:",600,100);
            g.setFont(words);
            g.setColor(Color.white);
            g.drawString("W = Jump",100,140);
            g.drawString("S = Duck",100,180);
            g.drawString("A = Move Left",100,220);
            g.drawString("D = Move Right",100,260);
            g.drawString("J = Punch",100,300);
            g.drawString("I = Block",100,340);
            g.drawString("L = Kick",100,380);
            g.drawString("Up Arrow = Jump",600,140);
            g.drawString("Down Arrow = Duck",600,180);
            g.drawString("Left Arrow = Move Left",600,220);
            g.drawString("Right Arrow = Move Right",600,260);
            g.drawString("4 = Punch",600,300);
            g.drawString("8 = Block",600,340);
            g.drawString("6 = Kick",600,380);
            g.setFont(headings);
            g.setColor(Color.yellow);
            g.drawString("COMBOS:",350,440);
            g.setFont(words);
            g.setColor(Color.white);
            g.drawString("Duck + Punch = Uppercut",280,480);
            g.drawString("Duck + Kick = Low Kick",280,520);
            g.drawString("Duck + Block = Low Block",280,560);
            g.setFont(headings);
            g.setColor(Color.yellow);
            g.drawString("Gameplay Instructions:",20,620);
            g.setFont(words);
            g.setColor(Color.white);
            g.drawString("You have 99 seconds to defeat your opponent. To win",320,620);
            g.drawString("the game, you must use your punches and kicks to deal damage to your opponent",20,660);
            g.drawString("with the objective of forcing their health to zero. Your opponent will also",20,700);
            g.drawString("be dealing damage to your character so be sure to effectively use your",20,740);
            g.drawString("blocks to defend yourself as well.",20,780);
            
            if (x > 10 && x < 90 && y > 10 && y < 55)
            {
               screen = 0;
               draw.repaint();
            }
            if (xx > 10 && xx < 90 && yy > 10 && yy < 55)
            {
               g.setColor(Color.blue);
               g.drawString("Back",20,40);
               g.drawRect(10,10,80,45);
            }
             
         }
         
         else if (screen == 1)
         {
            g.drawImage(gamemodeSelect.getImage(),0,0,1000,870,this);
            g.setFont(headings);
            g.setColor(Color.white);
            g.drawString("CHOOSE A GAME MODE",303,150);
            g.drawRect(298,110,367,52);
            g.setFont(main);
            g.setColor(Color.yellow);
            g.drawString("SINGLE PLAYER",260,350);
            g.drawString("MULTIPLAYER",275,500);
            g.drawImage(headshotOne.getImage(),0,0,165,225,this);
            g.drawImage(headshotTwo.getImage(),0,225,165,225,this);
            g.drawImage(headshotThree.getImage(),0,450,165,225,this);
            g.drawImage(headshotFour.getImage(),0,675,165,225,this);
            g.drawImage(headshotFive.getImage(),835,0,165,225,this);
            g.drawImage(headshotSix.getImage(),835,225,165,225,this);
            g.drawImage(headshotSeven.getImage(),835,450,165,225,this); 
            g.drawImage(headshotOne.getImage(),835,675,165,225,this);
            g.setFont(words);
            g.drawRect(180,10,80,45);
            g.drawString("Back",190,40);
            
            if (x > 260 && x < 710 && y > 305 && y < 350)
            {
               x = 0;
               y = 0;
               screen = 2;
               draw.repaint();
               singlePlayer = true;
            }
            else if (x > 275 && x < 680 && y > 455 && y < 500)
            {
               x = 0;
               y = 0;
               screen = 10;
               singlePlayer = false;
               draw.repaint();
            }      
            else if (x > 180 && x < 260 && y > 10 && y < 55)
            {
               screen = 0;
               draw.repaint();
            }
            
            if (xx > 180 && xx < 260 && yy > 10 && yy < 55)
            {
               g.setColor(Color.blue);
               g.drawRect(180,10,80,45);
               g.drawString("Back",190,40);
            }
            if (xx > 260 && xx < 710 && yy > 305 && yy < 350)
            {
               g.setColor(Color.blue);
               g.setFont(main);
               g.drawString("SINGLE PLAYER",260,350);
            }
            if (xx > 275 && xx < 680 && yy > 455 && yy < 500)
            {
               g.setColor(Color.blue);
               g.setFont(main);
               g.drawString("MULTIPLAYER",275,500);
            }
         }
         else if (screen == 2) 
         {
            g.drawImage(fighterSelect.getImage(),0,0,1000,870,this);
            g.setColor(Color.yellow);
            g.setFont(words);
            g.drawRect(10,10,80,45);
            g.drawString("Back",20,40);
            g.setFont(main);
            g.drawString("Choose Your Fighter",250,100);
            g.drawImage(headshotOne.getImage(),260,200,140,180,this);
            
            g.drawImage(headshotTen.getImage(),410,200,140,180,this);
            
            g.drawImage(headshotFour.getImage(),560,200,140,180,this);
            g.drawImage(headshotFive.getImage(),260,385,140,180,this);
            g.drawImage(headshotSix.getImage(),410,385,140,180,this);
            g.drawImage(headshotSeven.getImage(),560,385,140,180,this);
            
            if (x > 10 && x < 90 && y > 10 && y < 55)
            {
               screen = 1;
               draw.repaint();
            }
            
            if (xx > 10 && xx < 90 && yy > 10 && yy < 55)
            {
               g.setColor(Color.blue);
               g.setFont(words);
               g.drawString("Back",20,40);
               g.drawRect(10,10,80,45);
            }
            else if (xx > 260 && xx < 400 && yy > 200 && yy < 380) //1
            {
               g.drawImage(cage.fstance[cage.fstanceCount].getImage(), 125, 450, cage.imageW[0],cage.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(260,200,140,180);
               g.drawString("1",320,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 10",680,720);
               g.drawString("Speed: 100",680,790);
                          
            }
            else if (xx > 560 && xx < 700 && yy > 200 && yy < 380)
            {
               g.drawImage(sonya.fstance[sonya.fstanceCount].getImage(), 125, 450, sonya.imageW[0],sonya.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(560,200,140,180);
               g.drawString("1",620,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 9",680,720);
               g.drawString("Speed: 120",680,790);      
            }
            else if (xx > 260 && xx < 400 && yy > 385 && yy < 765)//5
            { 
               g.drawImage(raiden.fstance[raiden.fstanceCount].getImage(), 125, 450, raiden.imageW[0],raiden.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(260,385,140,180);
               g.drawString("1",320,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 230",680,650);
               g.drawString("Damage: 11",680,720);
               g.drawString("Speed: 60",680,790);
            }
            else if (xx > 410 && xx < 550 && yy > 385 && yy < 765)
            {
               g.drawImage(liuKang.fstance[liuKang.fstanceCount].getImage(), 125, 450, liuKang.imageW[0],liuKang.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(410,385,140,180);
               g.drawString("1",470,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 170",680,650);
               g.drawString("Damage: 12",680,720);
               g.drawString("Speed: 130",680,790);                         
            }
            else if (xx > 560 && xx < 700 && yy > 385 && yy < 765)
            {               
               g.drawImage(scorpion.fstance[scorpion.fstanceCount].getImage(), 125, 450, scorpion.imageW[0],scorpion.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(560,385,140,180);
               g.drawString("1",620,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 290",680,650);
               g.drawString("Damage: 8",680,720);
               g.drawString("Speed: 150",680,790);                        
            }
           
            if (x > 260 && x < 400 && y > 200 && y < 380) //1
            {
               g.setColor(Color.green);
               g.drawRect(260,200,140,180);
               g.drawString("1",320,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 10",680,720);
               g.drawString("Speed: 100",680,790);
               character1 = 1;
               one = new Player(character1,250,450);
               one.initialize();  
               screen = 3;
            }
            else if (x > 560 && x < 700 && y > 200 && y < 380)
            {
               g.setColor(Color.green);
               g.drawRect(560,200,140,180);
               g.drawString("1",620,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 9",680,720);
               g.drawString("Speed: 120",680,790);
               character1 = 2;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 3;
            }
            else if (x > 260 && x < 400 && y > 385 && y < 765)//5
            {
               g.setColor(Color.green);
               g.drawRect(260,385,140,180);
               g.drawString("1",320,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 230",680,650);
               g.drawString("Damage: 11",680,720);
               g.drawString("Speed: 60",680,790);
               character1 = 3;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 3;
            }
            else if (x > 410 && x < 550 && y > 385 && y < 765)
            {
               g.setColor(Color.green);
               g.drawRect(410,385,140,180);
               g.drawString("1",470,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 170",680,650);
               g.drawString("Damage: 12",680,720);
               g.drawString("Speed: 130",680,790);
               character1 = 4;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 3;
            }
            else if (x > 560 && x < 700 && y > 385 && y < 765)
            {
               g.setColor(Color.green);
               g.drawRect(560,385,140,180);
               g.drawString("1",620,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 290",680,650);
               g.drawString("Damage: 8",680,720);
               g.drawString("Speed: 150",680,790);
               character1 = 5;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 3;
            }
      
        }
        else if (screen == 3)
        {
            if (background == 1)
               g.drawImage(background_1.getImage(),0,0,1000,870,this);
            else if (background == 2)
               g.drawImage(background_2.getImage(),0,0,1000,870,this);
            else if (background == 3)
               g.drawImage(background_3.getImage(),0,0,1000,870,this);
            else if (background == 4)
               g.drawImage(background_4.getImage(),0,0,1000,870,this);
            else if (background == 5)
               g.drawImage(background_5.getImage(),0,0,1000,870,this);
            else if (background == 6)
               g.drawImage(background_6.getImage(),0,0,1000,870,this);
               
            g.setFont(words);
            g.setColor(Color.red); 
            g.drawString("" + Math.round(seconds), 490,130);
   
            //Player 1
            if(one.action == 0)
            {
               g.drawImage(one.fstance[one.fstanceCount].getImage(), one.x, one.y - 5, one.imageW[0],one.imageH[0], this);
            }
            if (one.action == 1)
            {
              g.drawImage(one.jump[one.jumpCount].getImage(), one.x, one.y-70, one.imageW[1],one.imageH[1], this); 
            } 
            if (one.action == 2)
            {
               g.drawImage(one.duck[one.duckCount].getImage(), one.x, one.y + 50, one.imageW[2],one.imageH[2], this);
            }
            if (one.action == 3)
            {
              g.drawImage(one.walk[one.walkCount].getImage(), one.x, one.y, one.imageW[3],one.imageH[3], this);
            }
            if (one.action == 4)
            {
               g.drawImage(one.punch[one.punchCount].getImage(), one.x, one.y, one.imageW[4],one.imageH[4], this);
            }
            if (one.action == 5)
            {
               g.drawImage(one.block[one.blockCount].getImage(), one.x, one.y, one.imageW[5],one.imageH[5], this);
            }
            if (one.action == 6)
            {
               g.drawImage(one.kick[one.kickCount].getImage(), one.x, one.y, one.imageW[6],one.imageH[6], this);
            }
            if (one.action == 7)
            {
               g.drawImage(one.uppercut[one.uppercutCount].getImage(), one.x, one.y, one.imageW[7],one.imageH[7], this); 
            }
            if (one.action == 8)
            {
               g.drawImage(one.lowBlock[one.lowblockCount].getImage(), one.x, one.y + 65, one.imageW[8],one.imageH[8], this); 
            }
            if (one.action == 9)
            {
               g.drawImage(one.lowKick[one.lowkickCount].getImage(), one.x, one.y, one.imageW[9],one.imageH[9], this); 
            }
            if (oneHit == true)
            
          g.setFont(main);
          g.setColor(Color.orange);
          g.drawString(""+oneScore,100,100);
          g.drawString(""+twoScore,700,100);
             
            //Player 2   
            if(two.action == 0)
            {
               g.drawImage(two.fstance[two.fstanceCount].getImage(), two.x, two.y - 5, two.imageW[0],two.imageH[0], this);
            }
            if (two.action == 1)
            {
              g.drawImage(two.jump[two.jumpCount].getImage(), two.x, two.y-70, two.imageW[1],two.imageH[1], this); 
            } 
            if (two.action == 2)
            {
               g.drawImage(two.duck[two.duckCount].getImage(), two.x, two.y + 50, two.imageW[2],two.imageH[2], this);
            }
            if (two.action == 3)
            {
               g.drawImage(two.walk[two.walkCount].getImage(), two.x, two.y, two.imageW[3],two.imageH[3], this);
            }
            if (two.action == 4)
            {
               g.drawImage(two.punch[two.punchCount].getImage(), two.x, two.y, two.imageW[4],two.imageH[4], this);
            }
            if (two.action == 5)
            {
               g.drawImage(two.block[two.blockCount].getImage(), two.x, two.y, two.imageW[5],two.imageH[5], this);
            }
            if (two.action == 6)
            {
               g.drawImage(two.kick[two.kickCount].getImage(), two.x, two.y, two.imageW[6],two.imageH[6], this);
            }
            if (two.action == 7)
            {
               g.drawImage(two.uppercut[two.uppercutCount].getImage(), two.x, two.y, two.imageW[7],two.imageH[7], this); 
            }
            if (two.action == 8)
            {
               g.drawImage(two.lowBlock[two.lowblockCount].getImage(), two.x, two.y + 65, two.imageW[8],two.imageH[8], this); 
            }
            if (two.action == 9)
            {
               g.drawImage(two.lowKick[two.lowkickCount].getImage(), two.x, two.y, two.imageW[9],two.imageH[9], this); 
            }
            if (twoHit == true)
             g.drawImage(two.hit[two.hitCount].getImage(), two.x, two.y, two.imageW[0],two.imageH[0], this); 
   
               
            if (character1 == 1)
            {
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 *(one.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 200.0) + 50),100,(int)(400 * ((one.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Cage",100,145);
            }
            else if (character1 == 2)
            {
               
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 200.0) + 50),100,(int)(400 * ((one.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Sonya",100,145);
            }  
            else if (character1 == 3)
            {  
              
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 230.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 230.0) + 50),100,(int)(400 * ((one.health / 230.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Raiden",100,145);
            }
            else if (character1 == 4)
            {
               
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 170.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 170.0) + 50),100,(int)(400 * ((one.health / 170.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Lui Kang",100,145);
            }
            else if (character1 == 5)
            {
               
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 290.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 290.0) + 50),100,(int)(400 * ((one.health / 290.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Scorpion",100,145);
            }
            
            //AI     
            if (character2 == 1)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 200.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Cage",750,145);
            }
            else if (character2 == 2)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 200.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Sonya",750,145);
            }  
            else if (character2 == 3)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 230.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 230.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 230.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Raiden",725,145);
            }
            else if (character2 == 4)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 170.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 170.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 170.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Lui Kang",700,145);
            }
            else if (character2 == 5)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 290.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 290.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 290.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Scorpion",700,145);
            }
            if (one.health <= 0 || two.health <= 0)
               screen = 4;
               
            g.setColor(Color.orange);
            g.setFont(main);
            g.drawString("Fight",425,60);
        }
        else if (screen == 4)
        {
            if (background == 1)
               g.drawImage(background_1.getImage(),0,0,1000,870,this);
            else if (background == 2)
               g.drawImage(background_2.getImage(),0,0,1000,870,this);
            else if (background == 3)
               g.drawImage(background_3.getImage(),0,0,1000,870,this);
            else if (background == 4)
               g.drawImage(background_4.getImage(),0,0,1000,870,this);
            else if (background == 5)
               g.drawImage(background_5.getImage(),0,0,1000,870,this);
            else if (background == 6)
               g.drawImage(background_6.getImage(),0,0,1000,870,this);
            
            if (one.health <= 0)
            { 
               while (gameOver == false)
               {
                  if(two.action == 0)
                    g.drawImage(two.fstance[two.fstanceCount].getImage(), two.x, two.y - 5, two.imageW[0],one.imageH[0], this);
                  if (two.action == 1)
                     g.drawImage(two.jump[two.jumpCount].getImage(), two.x, two.y - 70, two.imageW[1],one.imageH[1], this); 
                  if (two.action == 2)
                     g.drawImage(two.duck[two.duckCount].getImage(), two.x, two.y + 50, two.imageW[2],one.imageH[2], this);
                  if (two.action == 3)
                     g.drawImage(two.walk[two.walkCount].getImage(), two.x, two.y, two.imageW[3],one.imageH[3],this);
                  if (two.action == 4)
                     g.drawImage(two.punch[two.punchCount].getImage(), two.x, two.y, two.imageW[4],one.imageH[4],this);
                  if (two.action == 5)
                     g.drawImage(two.block[two.blockCount].getImage(), two.x, two.y,two.imageW[5],one.imageH[5],this); 
                  if (two.action == 6)
                     g.drawImage(two.kick[two.kickCount].getImage(), two.x, two.y,two.imageW[6],one.imageH[6], this);
                  if (two.action == 7)
                     g.drawImage(two.uppercut[two.uppercutCount].getImage(), two.x, two.y,two.imageW[7],one.imageH[7],this); 
                  if (two.action == 8)
                     g.drawImage(two.lowBlock[two.lowblockCount].getImage(), two.x, two.y, two.imageW[8],one.imageH[8], this); 
                  if (two.action == 9)
                     g.drawImage(two.lowKick[two.lowkickCount].getImage(), two.x, two.y, two.imageW[9],one.imageH[9], this);    
                  
                  g.drawImage(one.dizzy[one.dizzyCount].getImage(), two.x, two.y - 5, one.imageW[0],one.imageH[0], this);
                  
                  if (oneHit == true)
                     gameOver = true;
              }
              
              if (gameOver == true)
              {
                 g.setFont(main);
                 g.setColor(Color.orange);
                 g.drawString("Winner",450,400); 
                 g.drawImage(two.victory[two.victoryCount].getImage(), two.x, two.y - 5, two.imageW[0],two.imageH[0], this);
                 g.drawImage(one.fall[one.fallCount].getImage(), one.x, one.y - 5, one.imageW[0],one.imageH[0], this);
              }
            }
            if (two.health <= 0)
            { 
               while (gameOver == false)
               {
                  if(one.action == 0)
                    g.drawImage(one.fstance[one.fstanceCount].getImage(), one.x, one.y - 5, one.imageW[0],one.imageH[0], this);
                  if (one.action == 1)
                     g.drawImage(one.jump[one.jumpCount].getImage(), one.x, one.y-70, one.imageW[1],one.imageH[1], this);  
                  if (one.action == 2)
                     g.drawImage(one.duck[one.duckCount].getImage(), one.x, one.y + 50, one.imageW[2],one.imageH[2], this);
                  if (one.action == 3)
                     g.drawImage(one.walk[one.walkCount].getImage(), one.x, one.y, one.imageW[3],one.imageH[3], this);
                  if (one.action == 4)
                     g.drawImage(one.punch[one.punchCount].getImage(), one.x, one.y, one.imageW[4],one.imageH[4], this);
                  if (one.action == 5)
                     g.drawImage(one.block[one.blockCount].getImage(), one.x, one.y, one.imageW[5],one.imageH[5], this); 
                  if (one.action == 6)
                     g.drawImage(one.kick[one.kickCount].getImage(), one.x, one.y, one.imageW[6],one.imageH[6], this);
                  if (one.action == 7)
                     g.drawImage(one.uppercut[one.uppercutCount].getImage(), one.x, one.y, one.imageW[7],one.imageH[7], this); 
                  if (one.action == 8)
                     g.drawImage(one.lowBlock[one.lowblockCount].getImage(), one.x, one.y, one.imageW[8],one.imageH[8], this); 
                  if (one.action == 9)
                     g.drawImage(one.lowKick[one.lowkickCount].getImage(), one.x, one.y, one.imageW[9],one.imageH[9], this); 
                  
                  g.drawImage(two.dizzy[two.dizzyCount].getImage(), two.x, two.y - 5, two.imageW[0],two.imageH[0], this);
                  
                  if (twoHit == true)
                     gameOver = true;
              }
              
              if (gameOver == true)
              {
                 g.setFont(main);
                 g.setColor(Color.orange);
                 g.drawString("Winner",450,400); 
                 g.drawImage(one.victory[one.victoryCount].getImage(), one.x, one.y - 5, one.imageW[0],one.imageH[0], this);
                 g.drawImage(two.fall[two.fallCount].getImage(), two.x, two.y - 5, two.imageW[0],two.imageH[0], this);
              }
            }
            if (seconds == 0.0)
            {
               if (gameOver == true && two.health > one.health)
                 {
                    g.setFont(main);
                    g.setColor(Color.orange);
                    g.drawString("Winner",450,400); 
                    g.drawImage(two.victory[two.victoryCount].getImage(), two.x, two.y - 5, two.imageW[0],two.imageH[0], this);
                    g.drawImage(one.fall[one.fallCount].getImage(), one.x, one.y - 5, one.imageW[0],one.imageH[0], this);
                 }
               else if (gameOver == true && two.health < one.health)
                {
                 g.setFont(main);
                 g.setColor(Color.orange);
                 g.drawString("Winner",450,400); 
                 g.drawImage(one.victory[one.victoryCount].getImage(), one.x, one.y - 5, one.imageW[0],one.imageH[0], this);
                 g.drawImage(two.fall[two.fallCount].getImage(), two.x, two.y - 5, two.imageW[0],two.imageH[0], this);
              }
            }

   
        }    
        else if (screen == 10)
        {
            g.drawImage(fighterSelect.getImage(),0,0,1000,870,this);
            g.setColor(Color.yellow);
            g.setFont(words);
            g.drawRect(10,10,80,45);
            g.drawString("Back",20,40);
            g.setFont(main);
            g.drawString("Choose Your Fighter",250,100);
            g.drawImage(headshotOne.getImage(),260,200,140,180,this);
           
            g.drawImage(headshotTen.getImage(),410,200,140,180,this);
           
            g.drawImage(headshotFour.getImage(),560,200,140,180,this);
            g.drawImage(headshotFive.getImage(),260,385,140,180,this);
            g.drawImage(headshotSix.getImage(),410,385,140,180,this);
            g.drawImage(headshotSeven.getImage(),560,385,140,180,this);
            
            
            if (x > 10 && x < 90 && y > 10 && y < 55)
            {
               screen = 1;
               draw.repaint();
            }
            
            if (xx > 10 && xx < 90 && yy > 10 && yy < 55)
            {
               g.setColor(Color.blue);
               g.setFont(words);
               g.drawString("Back",20,40);
               g.drawRect(10,10,80,45);
            }
            else if (xx > 260 && xx < 400 && yy > 200 && yy < 380) //1
            {
               g.drawImage(cage.fstance[cage.fstanceCount].getImage(), 125, 450, cage.imageW[0],cage.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(260,200,140,180);
               g.drawString("1",320,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 10",680,720);
               g.drawString("Speed: 100",680,790);
            }
            else if (xx > 560 && xx < 700 && yy > 200 && yy < 380)
            {
               g.drawImage(sonya.fstance[sonya.fstanceCount].getImage(), 125, 450, sonya.imageW[0],sonya.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(560,200,140,180);
               g.drawString("1",620,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 9",680,720);
               g.drawString("Speed: 120",680,790);
            }
            else if (xx > 260 && xx < 400 && yy > 385 && yy < 765)//5
            {
               g.drawImage(raiden.fstance[raiden.fstanceCount].getImage(), 125, 450, raiden.imageW[0],raiden.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(260,385,140,180);
               g.drawString("1",320,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 230",680,650);
               g.drawString("Damage: 11",680,720);
               g.drawString("Speed: 60",680,790);
            }
            else if (xx > 410 && xx < 550 && yy > 385 && yy < 765)
            {
               g.drawImage(liuKang.fstance[liuKang.fstanceCount].getImage(), 125, 450, liuKang.imageW[0],liuKang.imageH[0], this);        
               g.setColor(Color.green);
               g.drawRect(410,385,140,180);
               g.drawString("1",470,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 170",680,650);
               g.drawString("Damage: 12",680,720);
               g.drawString("Speed: 130",680,790);
            }
            else if (xx > 560 && xx < 700 && yy > 385 && yy < 765)
            {
               g.drawImage(scorpion.fstance[scorpion.fstanceCount].getImage(), 125, 450, scorpion.imageW[0],scorpion.imageH[0], this);
               g.setColor(Color.green);
               g.drawRect(560,385,140,180);
               g.drawString("1",620,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 290",680,650);
               g.drawString("Damage: 8",680,720);
               g.drawString("Speed: 150",680,790);
            }
           
            if (x > 260 && x < 400 && y > 200 && y < 380) //1
            {
               g.setColor(Color.green);
               g.drawRect(260,200,140,180);
               g.drawString("1",320,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 10",680,720);
               g.drawString("Speed: 100",680,790);
               character1 = 1; 
               x = 0;
               y = 0;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 11;
            }
            else if (x > 560 && x < 700 && y > 200 && y < 380)
            {
               g.setColor(Color.green);
               g.drawRect(560,200,140,180);
               g.drawString("1",620,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 9",680,720);
               g.drawString("Speed: 120",680,790);
               character1 = 2;
               x = 0;
               y = 0;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 11;
            }
            else if (x > 260 && x < 400 && y > 385 && y < 765)//5
            {
               g.setColor(Color.green);
               g.drawRect(260,385,140,180);
               g.drawString("1",320,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 230",680,650);
               g.drawString("Damage: 11",680,720);
               g.drawString("Speed: 60",680,790);
               character1 = 3;
               x = 0;
               y = 0;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 11;
            }
            else if (x > 410 && x < 550 && y > 385 && y < 765)
            {
               g.setColor(Color.green);
               g.drawRect(410,385,140,180);
               g.drawString("1",470,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 170",680,650);
               g.drawString("Damage: 12",680,720);
               g.drawString("Speed: 130",680,790);
               character1 = 4;
               x = 0;
               y = 0;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 11;
            }
            else if (x > 560 && x < 700 && y > 385 && y < 765)
            {
               g.setColor(Color.green);
               g.drawRect(560,385,140,180);
               g.drawString("1",620,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 290",680,650);
               g.drawString("Damage: 8",680,720);
               g.drawString("Speed: 150",680,790);
               character1 = 5;
               x = 0;
               y = 0;
               one = new Player(character1,250,450);
               one.initialize();
               screen = 11;
            }
            for(int j = 0; j < two.imageW.length; j++)
                  two.imageW[j]*=-1;
        }

        else if (screen == 11)
        {
            g.drawImage(fighterSelect.getImage(),0,0,1000,870,this);
            g.setColor(Color.yellow);
            g.setFont(words);
            g.drawRect(10,10,80,45);
            g.drawString("Back",20,40);
            g.setFont(main);
            g.drawString("Choose Your Fighter",250,100);
            g.drawImage(headshotOne.getImage(),260,200,140,180,this);
           
            g.drawImage(headshotTen.getImage(),410,200,140,180,this);
           
            g.drawImage(headshotFour.getImage(),560,200,140,180,this);
            g.drawImage(headshotFive.getImage(),260,385,140,180,this);
            g.drawImage(headshotSix.getImage(),410,385,140,180,this);
            g.drawImage(headshotSeven.getImage(),560,385,140,180,this);
            
            if (character1 == 1)
            {
               g.setColor(Color.green);
               g.drawRect(260,200,140,180);
               g.drawString("1",320,290);
            }
            else if (character1 == 2)
            {
               g.setColor(Color.green);
               g.drawRect(560,200,140,180);
               g.drawString("1",620,290);
            }
            else if (character1 == 3)
            {
               g.setColor(Color.green);
               g.drawRect(260,385,140,180);
               g.drawString("1",320,475);
            }
            else if (character1 == 4)
            {
               g.setColor(Color.green);
               g.drawRect(410,385,140,180);
               g.drawString("1",470,475);
            }
            else if (character1 == 5)
            {
               g.setColor(Color.green);
               g.drawRect(560,385,140,180);
               g.drawString("1",620,475);
            }  
            
            if (x > 10 && x < 90 && y > 10 && y < 55)
            {
               screen = 1;
               draw.repaint();
            }
            
            if (xx > 10 && xx < 90 && yy > 10 && yy < 55)
            {
               g.setColor(Color.blue);
               g.setFont(words);
               g.drawString("Back",20,40);
               g.drawRect(10,10,80,45);
            }
            else if (xx > 260 && xx < 400 && yy > 200 && yy < 380) //1
            {
               g.drawImage(cage.fstance[cage.fstanceCount].getImage(), 125, 450, cage.imageW[0],cage.imageH[0], this);           
               g.setColor(Color.red);
               g.drawRect(260,200,140,180);
               g.drawString("2",320,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 10",680,720);
               g.drawString("Speed: 100",680,790); 
            }
            else if (xx > 560 && xx < 700 && yy > 200 && yy < 380)
            {
               g.drawImage(sonya.fstance[sonya.fstanceCount].getImage(), 125, 450, sonya.imageW[0],sonya.imageH[0], this);
               g.setColor(Color.red);
               g.drawRect(560,200,140,180);
               g.drawString("2",620,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 9",680,720);
               g.drawString("Speed: 120",680,790);
            }
            else if (xx > 260 && xx < 400 && yy > 385 && yy < 765)//5
            {
               g.drawImage(raiden.fstance[raiden.fstanceCount].getImage(), 125, 450, raiden.imageW[0],raiden.imageH[0], this);
               g.setColor(Color.red);
               g.drawRect(260,385,140,180);
               g.drawString("2",320,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 230",680,650);
               g.drawString("Damage: 11",680,720);
               g.drawString("Speed: 60",680,790);
            }
            else if (xx > 410 && xx < 550 && yy > 385 && yy < 765)
            {
               g.drawImage(liuKang.fstance[liuKang.fstanceCount].getImage(), 125, 450, liuKang.imageW[0],liuKang.imageH[0], this);
               g.setColor(Color.red);
               g.drawRect(410,385,140,180);
               g.drawString("2",470,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 170",680,650);
               g.drawString("Damage: 12",680,720);
               g.drawString("Speed: 130",680,790);
            }
            else if (xx > 560 && xx < 700 && yy > 385 && yy < 765)
            {
               g.drawImage(scorpion.fstance[scorpion.fstanceCount].getImage(), 125, 450, scorpion.imageW[0],scorpion.imageH[0], this);
               g.setColor(Color.red);
               g.drawRect(560,385,140,180);
               g.drawString("2",620,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 290",680,650);
               g.drawString("Damage: 8",680,720);
               g.drawString("Speed: 150",680,790);
            }
            
            if (x > 260 && x < 400 && y > 200 && y < 380) //1
            {
               g.setColor(Color.red);
               g.drawRect(260,200,140,180);
               g.drawString("2",320,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 10",680,720);
               g.drawString("Speed: 100",680,790);
               character2 = 1;
               two = new Player(character2,700,450);
               two.initialize();
               screen = 12; 
               draw.repaint();
            }
            else if (x > 560 && x < 700 && y > 200 && y < 380)
            {
               g.setColor(Color.red);
               g.drawRect(560,200,140,180);
               g.drawString("2",620,290);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 200",680,650);
               g.drawString("Damage: 9",680,720);
               g.drawString("Speed: 120",680,790);
               character2 = 2;
               two = new Player(character2,700,450);
               two.initialize();
               screen = 12;
               draw.repaint();
            }
            else if (x > 260 && x < 400 && y > 385 && y < 765)//5
            {
               g.setColor(Color.red);
               g.drawRect(260,385,140,180);
               g.drawString("2",320,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 230",680,650);
               g.drawString("Damage: 11",680,720);
               g.drawString("Speed: 60",680,790);
               character2 = 3;
               two = new Player(character2,700,450);
               two.initialize();
               screen = 12;
               draw.repaint();
            }
            else if (x > 410 && x < 550 && y > 385 && y < 765)
            {
               g.setColor(Color.red);
               g.drawRect(410,385,140,180);
               g.drawString("2",470,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 170",680,650);
               g.drawString("Damage: 12",680,720);
               g.drawString("Speed: 130",680,790);
               character2 = 4;
               two = new Player(character2,700,450);
               two.initialize();
               screen = 12;
               draw.repaint();
            }
            else if (x > 560 && x < 700 && y > 385 && y < 765)
            {
               g.setColor(Color.red);
               g.drawRect(560,385,140,180);
               g.drawString("2",620,475);
               g.setColor(Color.white);
               g.setFont(main);
               g.drawString("Health: 290",680,650);
               g.drawString("Damage: 8",680,720);
               g.drawString("Speed: 150",680,790);
               character2 = 5;
               two = new Player(character2,700,450);
               two.initialize();
               screen = 12;
               draw.repaint();
            }
            for(int j = 0; j < one.imageW.length; j++)
                  two.imageW[j]*=-1;  
            for(int i = 0; i < 10; i++)
      {
         two.xwalk1[i] = ((two.xwalk1[i] - two.x) * -1) + (two.x);
         two.xfstance1[i] = ((two.xfstance1[i] - two.x) * -1)+ (two.x);
         two.xjump1[i] = ((two.xjump1[i] - two.x) * -1)  + (two.x);
         two.xjump2[i] = ((two.xjump2[i] - two.x) * -1) + (two.x);
         two.xduck1[i] = ((two.xduck1[i] - two.x) * -1) + (two.x);
         two.xpunch1[i] = ((two.xpunch1[i] - two.x) * -1) + (two.x);
         two.xpunch2[i] = ((two.xpunch2[i] - two.x) * -1) + (two.x);
         two.xpunch3[i] = ((two.xpunch3[i] - two.x) * -1) + (two.x);
         two.xpunch6[i] = ((two.xpunch6[i] - two.x) * -1) + (two.x);
         two.xblock1[i] = ((two.xblock1[i] - two.x) * -1) + (two.x);
         two.xkick1 [i]= ((two.xkick1[i] - two.x) * -1)  + (two.x);
         two.xkick2 [i]= ((two.xkick2[i] - two.x) * -1) + (two.x);
         two.xkick3 [i]= ((two.xkick3[i] - two.x) * -1) + (two.x);
         two.xkick4 [i]= ((two.xkick4[i] - two.x) * -1) + (two.x);
         two.xuppercut1[i] = ((two.xuppercut1[i] - two.x) * -1) + (two.x);
         two.xuppercut2[i] = ((two.xuppercut2[i] - two.x) * -1)  + (two.x);
         two.xuppercut3[i] = ((two.xuppercut3[i] - two.x) * -1)  + (two.x);
         two.xuppercut4[i] = ((two.xuppercut4[i] - two.x) * -1) + (two.x);
         two.xlowblock1[i] = ((two.xlowblock1[i] - two.x) * -1) +(two.x);
         two.xlowkick1 [i]= ((two.xlowkick1[i] - two.x) * -1)  + (two.x);
         two.xlowkick2 [i]= ((two.xlowkick2[i] - two.x) * -1)  + (two.x);
         two.xlowkick3 [i]= ((two.xlowkick3[i] - two.x) * -1) + (two.x);
         two.xduck2[i]= ((two.xduck2[i] - two.x) * -1) + (two.x);
         two.xduck3[i]= ((two.xduck3[i] - two.x) * -1) + (two.x);
         two.xwalk2[i]= ((two.xwalk2[i] - two.x) * -1) + (two.x);
         two.xwalk3[i]= ((two.xwalk3[i] - two.x) * -1) + (two.x);
         two.xwalk4[i]= ((two.xwalk4[i] - two.x) * -1) + (two.x);
         two.xwalk5[i]= ((two.xwalk5[i] - two.x) * -1) + (two.x);
         two.xwalk6[i]= ((two.xwalk6[i] - two.x) * -1) + (two.x);
         two.xwalk7[i]= ((two.xwalk7[i] - two.x) * -1) + (two.x);
         two.xwalk8[i]= ((two.xwalk8[i] - two.x) * -1) + (two.x);
         two.xwalk9[i]= ((two.xwalk9[i] - two.x) * -1) + (two.x);
         two.xpunch4[i]= ((two.xpunch4[i] - two.x) * -1) + (two.x);
         two.xpunch5[i]= ((two.xpunch5[i] - two.x) * -1) + (two.x);
         two.xpunch7[i]= ((two.xpunch7[i] - two.x) * -1) + (two.x);
         two.xkick5[i]= ((two.xkick5[i] - two.x) * -1) + (two.x);
         two.xkick6[i]= ((two.xkick6[i] - two.x) * -1) + (two.x);
         two.xuppercut5[i]= ((two.xuppercut5[i] - two.x) * -1) + (two.x);
         two.xlowkick4[i]= ((two.xlowkick4[i] - two.x) * -1) + (two.x);
      }
      two.reinitialize();

        }
        else if (screen == 12)
        {
            if (background == 1)
               g.drawImage(background_1.getImage(),0,0,1000,870,this);
            else if (background == 2)
               g.drawImage(background_2.getImage(),0,0,1000,870,this);
            else if (background == 3)
               g.drawImage(background_3.getImage(),0,0,1000,870,this);
            else if (background == 4)
               g.drawImage(background_4.getImage(),0,0,1000,870,this);
            else if (background == 5)
               g.drawImage(background_5.getImage(),0,0,1000,870,this);
            else if (background == 6)
               g.drawImage(background_6.getImage(),0,0,1000,870,this);
            
            g.setFont(words);
            g.setColor(Color.red); 
            g.drawString("" + Math.round(seconds), 490,130);
            
            

             //Player 1
            if(one.action == 0)
            {
               g.drawImage(one.fstance[one.fstanceCount].getImage(), one.x, one.y - 5, one.imageW[0],one.imageH[0], this);
            }
            if (one.action == 1)
            {
              g.drawImage(one.jump[one.jumpCount].getImage(), one.x, one.y-70, one.imageW[1],one.imageH[1], this); 
            } 
            if (one.action == 2)
            {
               g.drawImage(one.duck[one.duckCount].getImage(), one.x, one.y + 50, one.imageW[2],one.imageH[2], this);
            }
            if (one.action == 3)
            {
               g.drawImage(one.walk[one.walkCount].getImage(), one.x, one.y, one.imageW[3],one.imageH[3], this);
            }
            if (one.action == 4)
            {
               g.drawImage(one.punch[one.punchCount].getImage(), one.x, one.y, one.imageW[4],one.imageH[4], this);
            }
            if (one.action == 5)
            {
               g.drawImage(one.block[one.blockCount].getImage(), one.x, one.y, one.imageW[5],one.imageH[5], this);
            }
            if (one.action == 6)
            {
               g.drawImage(one.kick[one.kickCount].getImage(), one.x, one.y, one.imageW[6],one.imageH[6], this);
            }
            if (one.action == 7)
            {
               g.drawImage(one.uppercut[one.uppercutCount].getImage(), one.x, one.y, one.imageW[7],one.imageH[7], this); 
            }
            if (one.action == 8)
            {
               g.drawImage(one.lowBlock[one.lowblockCount].getImage(), one.x, one.y+65, one.imageW[8],one.imageH[8], this); 
            }
            if (one.action == 9)
            {
               g.drawImage(one.lowKick[one.lowkickCount].getImage(), one.x, one.y, one.imageW[9],one.imageH[9], this); 
            }
            if (oneHit == true)
               g.drawImage(one.hit[one.hitCount].getImage(), one.x, one.y, one.imageW[0],one.imageH[0], this); 
          
          g.setFont(main);
          g.setColor(Color.orange);
          g.drawString(""+oneScore,100,100);
          g.drawString(""+twoScore,700,100);
           
         //Player 2   
         if(two.action == 0)
            {
               g.drawImage(two.fstance[two.fstanceCount].getImage(), two.x, two.y - 5, two.imageW[0],two.imageH[0], this);
            }
            if (two.action == 1)
            {
              g.drawImage(two.jump[two.jumpCount].getImage(), two.x, two.y-70, two.imageW[1],two.imageH[1], this); 
            } 
            if (two.action == 2)
            {
               g.drawImage(two.duck[two.duckCount].getImage(), two.x, two.y + 50, two.imageW[2],two.imageH[2], this);
            }
            if (two.action == 3)
            {
               g.drawImage(two.walk[two.walkCount].getImage(), two.x, two.y, two.imageW[3],two.imageH[3], this);
            }
            if (two.action == 4)
            {
               g.drawImage(two.punch[two.punchCount].getImage(), two.x, two.y, two.imageW[4],two.imageH[4], this);
            }
            if (two.action == 5)
            {
               g.drawImage(two.block[two.blockCount].getImage(), two.x, two.y, two.imageW[5],two.imageH[5], this);
            }
            if (two.action == 6)
            {
               g.drawImage(two.kick[two.kickCount].getImage(), two.x, two.y, two.imageW[6],two.imageH[6], this);
            }
            if (two.action == 7)
            {
               g.drawImage(two.uppercut[two.uppercutCount].getImage(), two.x, two.y, two.imageW[7],two.imageH[7], this); 
            }
            if (two.action == 8)
            {
               g.drawImage(two.lowBlock[two.lowblockCount].getImage(), two.x, two.y +65, two.imageW[8],two.imageH[8], this); 
            }
            if (two.action == 9)
            {
               g.drawImage(two.lowKick[two.lowkickCount].getImage(), two.x, two.y, two.imageW[9],two.imageH[9], this); 
            }
            if (twoHit == true)
             g.drawImage(two.hit[two.hitCount].getImage(), two.x, two.y, two.imageW[0],two.imageH[0], this); 

         g.setColor(Color.orange);
         g.setFont(main);
         g.drawString("Fight",425,60);
               
          //P1        
            if (character1 == 1)
            {
               
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 200.0) + 50),100,(int)(400 * ((one.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Cage",100,145);
            }
            else if (character1 == 2)
            {
               
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 200.0) + 50),100,(int)(400 * ((one.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Sonya",100,145);
            }  
            else if (character1 == 3)
            {
               
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 230.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 230.0) + 50),100,(int)(400 * ((one.health / 230.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Raiden",100,145);
            }
            else if (character1 == 4)
            {
              
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 170.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 170.0) + 50),100,(int)(400 * ((one.health / 170.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Lui Kang",100,145);
            }
            else if (character1 == 5)
            {
               
               g.setColor(darkGreen);
               g.fillRect(50,100,(int)(400 * (one.health / 290.0)),50);
               g.setColor(Color.red);
               g.fillRect((int)(400 * (one.health / 290.0) + 50),100,(int)(400 * ((one.health / 290.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(50,100,400,50);
               g.setFont(main);
               g.drawString("Scorpion",100,145);
            }
       //P2     
            if (character2 == 1)
            {                                             
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 200.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Cage",750,145);
            }
            else if (character2 == 2)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 200.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 200.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 200.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Sonya",750,145);
            }  
            else if (character2 == 3)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 230.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 230.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 230.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Raiden",725,145);
            }
            else if (character2 == 4)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 170.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 170.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 170.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Lui Kang",700,145);
            }
            else if (character2 == 5)
            {
               
               g.setColor(darkGreen);
               g.fillRect((int)(400 * ((two.health / 290.0 - 1) * - 1 + 1) + 150),100,(int)(400 * (two.health / 290.0)),50);
               g.setColor(Color.red);
               g.fillRect(550,100,(int)(400 * ((two.health / 290.0 - 1) * -1)),50);
               g.setColor(Color.yellow);
               g.drawRect(550,100,400,50);
               g.setFont(main);
               g.drawString("Scorpion",700,145);
            }
            if (one.health <= 0 || two.health <= 0)
               screen = 4;
            g.setFont(main);   
        }
        }      
   }

    public static void main(String[] args) throws InterruptedException
    {
      new MortalKombat1();
    }
     /*** hit **********************************************
    * Purpose: Checks if player got hit                   *
    * Parameters: none                                    *
    * Returns: none                                       *
    ******************************************************/
    public void hit ()
    {
      // p1 being hit
      if (one.action == 0)
      {
         if(one.fightingStancePoly[one.fstanceCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }
         if(one.fightingStancePoly[one.fstanceCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {   
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }
         if(one.fightingStancePoly[one.fstanceCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }

         if(one.fightingStancePoly[one.fstanceCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {   
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }
        
      }
      if (one.action == 1)
      {
         if(one.jumpPoly[one.jumpCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }

         if(one.jumpPoly[one.jumpCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }

         if(one.jumpPoly[one.jumpCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }

         if(one.jumpPoly[one.jumpCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
      }
      if (one.action == 2)
      {
         if(one.duckPoly[one.duckCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.duckPoly[one.duckCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.duckPoly[one.duckCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.duckPoly[one.duckCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }
        
      }
      if (one.action == 3)
      {
         if(one.walkPoly[one.walkCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.walkPoly[one.walkCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.walkPoly[one.walkCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.walkPoly[one.walkCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            one.health -= two.strength; 
            twoScore += 100; 
            oneHit = true;
         }
      
      }
      if (one.action == 4)
      {
         if(one.punchPoly[one.punchCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.punchPoly[one.punchCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.punchPoly[one.punchCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.punchPoly[one.punchCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
        {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }
        
      }
      if (one.action == 5)
      {
         if(one.blockPoly[one.blockCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            oneScore += 50;
         } 
         if(one.blockPoly[one.blockCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
          {
            one.health -= 5;
            twoScore += 100;
            oneHit = true;
         }  
         if(one.blockPoly[one.blockCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            one.health -= 5; 
            twoScore += 100; 
            oneHit = true;
         }
      
      }
      if (one.action == 6)
      {
         if(one.kickPoly[one.kickCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.kickPoly[one.kickCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.kickPoly[one.kickCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.kickPoly[one.kickCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            one.health -= two.strength;  
            twoScore += 100; 
            oneHit = true;
         }
     
      }
      if (one.action == 7)
      {
         if(one.uppercutPoly[one.uppercutCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.uppercutPoly[one.uppercutCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }
      
         if(one.uppercutPoly[one.uppercutCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.uppercutPoly[one.uppercutCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            one.health -= two.strength;
            twoScore += 100; 
            oneHit = true;
         }
       
      }
      if (one.action == 8)
      {
         if(one.lowBlockPoly[one.lowblockCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength;
            twoScore += 100;
            oneHit = true;
         }
         if(one.lowBlockPoly[one.lowblockCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength; 
            twoScore += 100;   
            oneHit = true;
         }
         if(one.lowBlockPoly[one.lowblockCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            oneScore += 50;   
         }
         if(one.lowBlockPoly[one.lowblockCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            oneScore += 50;   
         }
  
      }
      if (one.action == 9)
      {
         if(one.lowKickPoly[one.lowkickCount].contains((two.x - two.punchx[two.punchCount]),(two.y + two.punchy[two.punchCount])) && two.punchx[two.punchCount] != 0 && two.action == 4)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.lowKickPoly[one.lowkickCount].contains((two.x - two.kickx[two.kickCount]),(two.y + two.kicky[two.kickCount])) && two.kickx[two.kickCount] != 0 && two.action == 6)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.lowKickPoly[one.lowkickCount].contains((two.x - two.uppercutx[two.uppercutCount]),(two.y + two.uppercuty[two.uppercutCount])) && two.uppercutx[two.uppercutCount] != 0 && two.action == 7)
         {
            one.health -= two.strength; 
            twoScore += 100;
            oneHit = true;
         }
       
         if(one.lowKickPoly[one.lowkickCount].contains((two.x - two.lowkickx[two.lowkickCount]),(two.y + two.lowkicky[two.lowkickCount])) && two.lowkickx[two.lowkickCount] != 0 && two.action == 9)
         {
            one.health -= two.strength;
            twoScore += 100;  
            oneHit = true;
         }
      
      }
      if (two.action == 0)
      {
         if(two.fightingStancePoly[two.fstanceCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
          {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.fightingStancePoly[two.fstanceCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
          {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
        if(two.fightingStancePoly[two.fstanceCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
        {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.fightingStancePoly[two.fstanceCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }      
      }
      
      // p2 being hit
      if (two.action == 1)
      {
         if(two.jumpPoly[two.jumpCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.jumpPoly[two.jumpCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
         {   
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.jumpPoly[two.jumpCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {  
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.jumpPoly[two.jumpCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {   
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }        
      }
      if (two.action == 2)
      {
         if(two.duckPoly[two.duckCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.duckPoly[two.duckCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         { 
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.duckPoly[two.duckCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {  
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }        
      }
      if (two.action == 3)
      {
         if(two.walkPoly[two.walkCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         { 
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.walkPoly[two.walkCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
         {   
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.walkPoly[two.walkCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {   
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.walkPoly[two.walkCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {   
            two.health -= one.strength;
            oneScore += 100; 
            twoHit = true;
         }       
      }
      if (two.action == 4)
      {
         if(two.punchPoly[two.punchCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.punchPoly[two.punchCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
         {  
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.punchPoly[two.punchCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {   
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.punchPoly[two.punchCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {   
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }       
      }
      if (two.action == 5)
      {
         if(two.blockPoly[two.blockCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            twoScore += 50;
         }

         if(two.blockPoly[two.blockCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.blockPoly[two.blockCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {  
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true; 
         }       
      }
      
      if (two.action == 6)
      {
         if(two.kickPoly[two.kickCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.kickPoly[two.kickCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.kickPoly[two.kickCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.kickPoly[two.kickCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;  
         }      
      }
      if (two.action == 7)
      {
         if(two.uppercutPoly[two.uppercutCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.uppercutPoly[two.uppercutCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.uppercutPoly[two.uppercutCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.uppercutPoly[two.uppercutCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }      
      }
      if (two.action == 8)
      {
         if(two.lowBlockPoly[two.lowblockCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {
            twoScore += 50;
         }
         if(two.lowBlockPoly[two.lowblockCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {
            twoScore += 50;
         }
         
         if(two.lowBlockPoly[two.lowblockCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.lowBlockPoly[two.lowblockCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }       
      }
      if (two.action == 9)
      {
         if(two.lowKickPoly[two.lowkickCount].contains((one.x + one.punchx[one.punchCount]),(one.y + one.punchy[one.punchCount])) && one.punchx[one.punchCount] != 0 && one.action == 4)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.lowKickPoly[two.lowkickCount].contains((one.x + one.kickx[one.kickCount]),(one.y + one.kicky[one.kickCount])) && one.kickx[one.kickCount] != 0 && one.action == 6)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.lowKickPoly[two.lowkickCount].contains((one.x + one.uppercutx[one.uppercutCount]),(one.y + one.uppercuty[one.uppercutCount])) && one.uppercutx[one.uppercutCount] != 0 && one.action == 7)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
         if(two.lowKickPoly[two.lowkickCount].contains((one.x + one.lowkickx[one.lowkickCount]),(one.y + one.lowkicky[one.lowkickCount])) && one.lowkickx[one.lowkickCount] != 0 && one.action == 9)
         {
            two.health -= one.strength;
            oneScore += 100;
            twoHit = true;
         }
      }        
 
    }  
    // animation thread
    class Host extends Thread
       {
        public void run()
         {
            try
            {
               while (true)
               {   
                  hit();
                  if (one.x + 120 > two.x && flipped == false)
                  {
                     one.x += one.imageW[0];
                     two.x += two.imageW[0];
                     for(int j = 0; j < one.imageW.length; j++)
                     {
                        one.imageW[j]*=-1;
                        two.imageW[j]*=-1;
                     }
                     for(int i =0; i < one.punchx.length; i++)
                        one.punchx[i]*=-1;
                     for(int i =0; i < one.kickx.length; i++)
                        one.kickx[i]*=-1;
                     for(int i =0; i < one.uppercutx.length; i++)
                        one.uppercutx[i]*=-1;
                     for(int i =0; i < one.lowkickx.length; i++)
                        one.lowkickx[i]*=-1;
                     for(int i =0; i < two.punchx.length; i++)
                        two.punchx[i]*=-1;
                     for(int i =0; i < two.kickx.length; i++)
                        two.kickx[i]*=-1;
                     for(int i =0; i < two.uppercutx.length; i++)
                        two.uppercutx[i]*=-1;
                     for(int i =0; i < two.lowkickx.length; i++)
                        two.lowkickx[i]*=-1;
                     flipped = true;
                     one.reinitialize();
                     two.reinitialize();
                  }
                  else if (one.x - 120 < two.x && flipped == true)
                  {
                     one.x += one.imageW[0];
                     two.x += two.imageW[0];

                     for(int j = 0; j < one.imageW.length; j++)
                     {
                        one.imageW[j]*=-1;
                        two.imageW[j]*=-1;
                     }
                     for(int i =0; i < one.punchx.length; i++)
                        one.punchx[i]*=-1;
                     for(int i =0; i < one.kickx.length; i++)
                        one.kickx[i]*=-1;
                     for(int i =0; i < one.uppercutx.length; i++)
                        one.uppercutx[i]*=-1;
                     for(int i =0; i < one.lowkickx.length; i++)
                        one.lowkickx[i]*=-1;
                     for(int i =0; i < two.punchx.length; i++)
                        two.punchx[i]*=-1;
                     for(int i =0; i < two.kickx.length; i++)
                        two.kickx[i]*=-1;
                     for(int i =0; i < two.uppercutx.length; i++)
                        two.uppercutx[i]*=-1;
                     for(int i =0; i < two.lowkickx.length; i++)
                        two.lowkickx[i]*=-1;
                     flipped = false; 
                     one.reinitialize();
                     two.reinitialize();
                  }
                  
                  
                  // Cage
                  int trueCount = 0;
                  for (int i = 0; i < 7; i++)
                  {
                     if (p1Keys[i] == true)
                        trueCount++;
                  }
                  if (trueCount == 0)
                  {
                     cage.action = 0;
                     if(cage.fstanceCount + 1 < cage.fstance.length)
                        cage.fstanceCount++;
                     else
                        cage.fstanceCount = 0;
                  }
                  // sonya     
                  trueCount = 0;
                  for (int i = 0; i < 7; i++)
                  {
                     if (p1Keys[i] == true)
                        trueCount++;
                  }
                  if (trueCount == 0)
                  {
                     sonya.action = 0;
                     if(sonya.fstanceCount + 1 < sonya.fstance.length)
                        sonya.fstanceCount++;
                     else
                        sonya.fstanceCount = 0;
                  }
                  // raiden  
                  trueCount = 0;
                  for (int i = 0; i < 7; i++)
                  {
                     if (p1Keys[i] == true)
                        trueCount++;
                  }
                  if (trueCount == 0)
                  {
                     raiden.action = 0;
                     if(raiden.fstanceCount + 1 < raiden.fstance.length)
                        raiden.fstanceCount++;
                     else
                        raiden.fstanceCount = 0;
                  }
                  // Liu Kang     
                  trueCount = 0;
                  for (int i = 0; i < 7; i++)
                  {
                     if (p1Keys[i] == true)
                        trueCount++;
                  }
                  if (trueCount == 0)
                  {
                     liuKang.action = 0;
                     if(liuKang.fstanceCount + 1 < liuKang.fstance.length)
                        liuKang.fstanceCount++;
                     else
                        liuKang.fstanceCount = 0;
                  }    
                  // Scorpion  
                  trueCount = 0;
                  for (int i = 0; i < 7; i++)
                  {
                     if (p1Keys[i] == true)
                        trueCount++;
                  }
                  if (trueCount == 0)
                  {
                     scorpion.action = 0;
                     if(scorpion.fstanceCount + 1 < scorpion.fstance.length)
                        scorpion.fstanceCount++;
                     else
                        scorpion.fstanceCount = 0;
                  }
             // P1
                  trueCount = 0;
                  for (int i = 0; i < 7; i++)
                  {
                     if (p1Keys[i] == true)
                        trueCount++;
                  }
                  if (trueCount == 0)
                  {
                     one.action = 0;
                     if(one.fstanceCount + 1 < 7)
                     {
                        one.fstanceCount++; 
                     }
                     else
                     {
                        one.fstanceCount = 0;
                     }
                  }
                  //jump
                  if (p1Keys[0] == true)
                  {
                     one.action = 1;
                     if(one.jumpCount + 1 < one.jump.length)
                        one.jumpCount++; 
                     else
                     {
                        start1 = System.currentTimeMillis();
                        p1Keys[0] = false;
                        one.jumpCount = 0;
                        one.action = 0;
                      }
                     draw.repaint();

                  }
                  //duck
                  if (p1Keys[1] == true)
                  {
                     one.action = 2;
                  }
                  //walk left
                  if (p1Keys[2] == true)
                  {
                      one.action = 3;
                      if(one.x - 15 >= -10)
                      {
                        one.x-=15;
                        for(int i = 0; i < 10; i++) 
                        { 
                           one.xfstance1[i]-=15;
                           one.xjump1[i]-=15;
                           one.xjump2[i]-=15; 
                           one.xduck1[i]-=15; 
                           one.xwalk1[i]-=15;
                           one.xpunch1[i]-=15;
                           one.xpunch2[i]-=15; 
                           one.xpunch3[i]-=15;  
                           one.xpunch6[i]-=15;
                           one.xblock1[i]-=15;
                           one.xkick1[i]-=15;
                           one.xkick2[i]-=15;
                           one.xkick3[i]-=15;
                           one.xkick4[i]-=15;
                           one.xuppercut1[i]-=15;
                           one.xuppercut2[i]-=15;
                           one.xuppercut3[i]-=15;
                           one.xuppercut4[i]-=15;
                           one.xlowblock1[i]-=15;
                           one.xlowkick1[i]-=15;
                           one.xlowkick2[i]-=15;
                           one.xlowkick3[i]-=15;
                           one.xduck2[i]-=15;
                           one.xduck3[i]-=15;
                           one.xwalk2[i]-=15;
                           one.xwalk3[i]-=15;
                           one.xwalk4[i]-=15;
                           one.xwalk5[i]-=15;
                           one.xwalk6[i]-=15;
                           one.xwalk7[i]-=15;
                           one.xwalk8[i]-=15;
                           one.xwalk9[i]-=15;
                           one.xpunch4[i]-=15;
                           one.xpunch5[i]-=15;
                           one.xpunch7[i]-=15;
                           one.xkick5[i]-=15;
                           one.xkick6[i]-=15;
                           one.xuppercut5[i]-=15;
                           one.xlowkick4[i]-=15;
                       }
                        one.reinitialize();
                      }
                      if(one.walkCount + 1 < one.walk.length)
                      {
                        one.walkCount++; 
                      }
                      else
                      {
                        one.walkCount = 0;
                      }
                      draw.repaint();
                     
                  }
                  //walk right
                  if (p1Keys[3] == true)
                  {
                     one.action = 3;
                     if(one.x + 15 <= 1010)
                     {
                        one.x+=15;
                        for(int i = 0; i < 10; i++)
                        { 
                           one.xfstance1[i]+=15;
                           one.xjump1[i]+=15;
                           one.xjump2[i]+=15; 
                           one.xduck1[i]+=15; 
                           one.xwalk1[i]+=15;
                           one.xpunch1[i]+=15;
                           one.xpunch2[i]+=15; 
                           one.xpunch3[i]+=15;  
                           one.xpunch6[i]+=15;
                           one.xblock1[i]+=15;
                           one.xkick1[i]+=15;
                           one.xkick2[i]+=15;
                           one.xkick3[i]+=15;
                           one.xkick4[i]+=15;
                           one.xuppercut1[i]+=15;
                           one.xuppercut2[i]+=15;
                           one.xuppercut3[i]+=15;
                           one.xuppercut4[i]+=15;
                           one.xlowblock1[i]+=15;
                           one.xlowkick1[i]+=15;
                           one.xlowkick2[i]+=15;
                           one.xlowkick3[i]+=15;
                           one.xduck2[i]+=15;
                           one.xduck3[i]+=15;
                           one.xwalk2[i]+=15;
                           one.xwalk3[i]+=15;
                           one.xwalk4[i]+=15;
                           one.xwalk5[i]+=15;
                           one.xwalk6[i]+=15;
                           one.xwalk7[i]+=15;
                           one.xwalk8[i]+=15;
                           one.xwalk9[i]+=15;
                           one.xpunch4[i]+=15;
                           one.xpunch5[i]+=15;
                           one.xpunch7[i]+=15;
                           one.xkick5[i]+=15;
                           one.xkick6[i]+=15;
                           one.xuppercut5[i]+=15;
                           one.xlowkick4[i]+=15;
                        }
                        one.reinitialize();
                     }
                     if(one.walkCount + 1 < one.walk.length)
                        one.walkCount++; 
                     else
                        one.walkCount = 0;
                     draw.repaint();
                     
                  }
                  //punch
                  if (p1Keys[4] == true)
                  {
                     one.action = 4;
                     if(one.punchCount + 1 < one.punch.length)
                        one.punchCount++; 
                      else
                      {
                        start1 = System.currentTimeMillis();
                        p1Keys[4] = false;
                        one.punchCount = 0;
                        one.action = 0;
                      }
                     draw.repaint();
                  }
                  //block
                  if (p1Keys[5] == true)
                  {
                     one.action = 5;
                     if(one.blockCount + 1 < one.block.length)
                        one.blockCount++; 
                      else
                        one.blockCount = 0;
                     draw.repaint();
                  }
                  //kick
                  if (p1Keys[6] == true)
                  {
                     one.action = 6;
                     if(one.kickCount + 1 < one.kick.length)
                        one.kickCount++; 
                      else
                      {
                        start1 = System.currentTimeMillis();
                        p1Keys[6] = false;
                        one.kickCount = 0;
                        one.action = 0;
                      }
                     draw.repaint();
                     
                  }
                  //uppercut
                  if (uppercut1 == true)
                  {
                     one.action = 7;
                     if(one.uppercutCount + 1 < one.uppercut.length)
                        one.uppercutCount++; 
                      else
                      {
                        start1 = System.currentTimeMillis();
                        uppercut1 = false;
                        one.uppercutCount = 0;
                        one.action = 0;
                      }
                     draw.repaint();
                  }
                  //low block
                  if (p1Keys[1] == true && p1Keys[5] == true)
                  {
                     one.action = 8;
                     if(one.lowblockCount + 1 < one.lowBlock.length)
                        one.lowblockCount++; 
                      else
                        one.lowblockCount = 0;
                     draw.repaint();
                  }
                  //low kick
                  if (lowkick1 == true)
                  {
                     one.action = 9;
                     if(one.lowkickCount + 1 < one.lowKick.length)
                        one.lowkickCount++; 
                      else
                      {
                        start1 = System.currentTimeMillis();
                        lowkick1 = false;
                        one.lowkickCount = 0;
                        one.action = 0;
                      }
                     draw.repaint();
                  }
                  //being hit
                  if (oneHit == true)
                  {
                     one.action = -1;
                     if(one.hitCount + 1 < one.hit.length)
                        one.hitCount++; 
                     else
                     {
                        oneHit = false;
                        one.hitCount = 0;
                        one.action = 0;
                     } 
                     draw.repaint();
                  }      
                  
                  //Player 2
                  int trueCount2 = 0;
                  for (int i = 0; i < 7; i++)
                  {
                     if (p2Keys[i] == true)
                        trueCount2++;
                  }
                  if (trueCount2 == 0)
                  {
                     two.action = 0;
                     if(two.fstanceCount + 1 < 7)
                     {
                        two.fstanceCount++; 
                     }
                     else
                        two.fstanceCount = 0;
                  }
                   
                  //jump
                  if (p2Keys[0] == true)
                  {
                     two.action = 1;
                     if(two.jumpCount + 1 < two.jump.length)
                        two.jumpCount++; 
                     else
                     {
                        start2 = System.currentTimeMillis();
                        p2Keys[0] = false;
                        two.jumpCount = 0;
                        two.action = 0;
                     }
                     draw.repaint();

                  }
                  //duck
                  if (p2Keys[1] == true)
                  {
                     two.action = 2;
                  }
                  //walk left
                  if (p2Keys[2] == true)
                  {
                      two.action = 3;
                      if(two.x - 15 >= -10 )
                      {
                        two.x-=15;
                        for(int i = 0; i < 10; i++) 
                        { 
                           two.xfstance1[i]-=15;
                           two.xjump1[i]-=15;
                           two.xjump2[i]-=15; 
                           two.xduck1[i]-=15; 
                           two.xwalk1[i]-=15;
                           two.xpunch1[i]-=15;
                           two.xpunch2[i]-=15; 
                           two.xpunch3[i]-=15;  
                           two.xpunch6[i]-=15;
                           two.xblock1[i]-=15;
                           two.xkick1[i]-=15;
                           two.xkick2[i]-=15;
                           two.xkick3[i]-=15;
                           two.xkick4[i]-=15;
                           two.xuppercut1[i]-=15;
                           two.xuppercut2[i]-=15;
                           two.xuppercut3[i]-=15;
                           two.xuppercut4[i]-=15;
                           two.xlowblock1[i]-=15;
                           two.xlowkick1[i]-=15;
                           two.xlowkick2[i]-=15;
                           two.xlowkick3[i]-=15;
                           two.xduck2[i]-=15;
                           two.xduck3[i]-=15;
                           two.xwalk2[i]-=15;
                           two.xwalk3[i]-=15;
                           two.xwalk4[i]-=15;
                           two.xwalk5[i]-=15;
                           two.xwalk6[i]-=15;
                           two.xwalk7[i]-=15;
                           two.xwalk8[i]-=15;
                           two.xwalk9[i]-=15;
                           two.xpunch4[i]-=15;
                           two.xpunch5[i]-=15;
                           two.xpunch7[i]-=15;
                           two.xkick5[i]-=15;
                           two.xkick6[i]-=15;
                           two.xuppercut5[i]-=15;
                           two.xlowkick4[i]-=15;
                        }
                        two.reinitialize();
                      }
                      if(two.walkCount + 1 < two.walk.length)
                        two.walkCount++; 
                      else
                        two.walkCount = 0;
                      draw.repaint();
                     
                  }
                  //walk right
                  if (p2Keys[3] == true)
                  {
                     two.action = 3;
                     if(two.x + 15 <= 1010)
                     {
                        two.x+=15;
                       for(int i = 0; i < 10; i++)
                        { 
                            two.xfstance1[i]+=15;
                           two.xjump1[i]+=15;
                           two.xjump2[i]+=15; 
                           two.xduck1[i]+=15; 
                           two.xwalk1[i]+=15;
                           two.xpunch1[i]+=15;
                           two.xpunch2[i]+=15; 
                           two.xpunch3[i]+=15;  
                           two.xpunch6[i]+=15;
                           two.xblock1[i]+=15;
                           two.xkick1[i]+=15;
                           two.xkick2[i]+=15;
                           two.xkick3[i]+=15;
                           two.xkick4[i]+=15;
                           two.xuppercut1[i]+=15;
                           two.xuppercut2[i]+=15;
                           two.xuppercut3[i]+=15;
                           two.xuppercut4[i]+=15;
                           two.xlowblock1[i]+=15;
                           two.xlowkick1[i]+=15;
                           two.xlowkick2[i]+=15;
                           two.xlowkick3[i]+=15;
                           two.xduck2[i]+=15;
                           two.xduck3[i]+=15;
                           two.xwalk2[i]+=15;
                           two.xwalk3[i]+=15;
                           two.xwalk4[i]+=15;
                           two.xwalk5[i]+=15;
                           two.xwalk6[i]+=15;
                           two.xwalk7[i]+=15;
                           two.xwalk8[i]+=15;
                           two.xwalk9[i]+=15;
                           two.xpunch4[i]+=15;
                           two.xpunch5[i]+=15;
                           two.xpunch7[i]+=15;
                           two.xkick5[i]+=15;
                           two.xkick6[i]+=15;
                           two.xuppercut5[i]+=15;
                           two.xlowkick4[i]+=15;
                        }
                        two.reinitialize();
                     }
                     if(two.walkCount + 1 < two.walk.length)
                        two.walkCount++; 
                     else
                        two.walkCount = 0;
                     draw.repaint();
                     
                  }
                  //punch
                  if (p2Keys[4] == true)
                  {
                     two.action = 4;
                     if(two.punchCount + 1 < two.punch.length)
                        two.punchCount++; 
                      else
                      {
                        start2 = System.currentTimeMillis();
                        p2Keys[4] = false;
                        two.punchCount = 0;
                        two.action = 0;
                      }
                     draw.repaint();
                  }
                  //block
                  if (p2Keys[5] == true)
                  {
                     two.action = 5;
                     if(two.blockCount + 1 < two.block.length)
                        two.blockCount++; 
                      else
                        two.blockCount = 0;
                     draw.repaint();
                  }
                  //kick
                  if (p2Keys[6] == true)
                  {
                     two.action = 6;
                     if(two.kickCount + 1 < two.kick.length)
                        two.kickCount++; 
                      else
                      {
                        start2 = System.currentTimeMillis();
                        p2Keys[6] = false;
                        two.kickCount = 0;
                        two.action = 0;
                      }
                     draw.repaint();
                     
                  }
                  //uppercut
                  if (uppercut2 == true)
                  {
                     two.action = 7;
                     if(two.uppercutCount + 1 < two.uppercut.length)
                        two.uppercutCount++; 
                      else
                      {
                        start2 = System.currentTimeMillis();
                        two.uppercutCount = 0;
                        uppercut2 = false;
                        two.action = 0;
                      }
                     draw.repaint();
                  }
                  //low block
                  if (p2Keys[1] == true && p2Keys[5] == true)
                  {
                     two.action = 8;
                     if(two.lowblockCount + 1 < two.lowBlock.length)
                        two.lowblockCount++; 
                      else
                        two.lowblockCount = 0;
                     draw.repaint();
                  }
                  //low kick
                  if (lowkick2 == true)
                  {
                     two.action = 9;
                     if(two.lowkickCount + 1 < two.lowKick.length)
                        two.lowkickCount++; 
                      else
                      {
                        start2 = System.currentTimeMillis();
                        two.lowkickCount = 0;
                        lowkick2 = false;
                        two.action = 0;
                      }
                     draw.repaint();
                  }
                  //being hit
                  if (twoHit == true)
                  {
                     two.action = -1;
                     if(two.hitCount + 1 < two.hit.length)
                        two.hitCount++; 
                      else
                       {
                        twoHit = false;
                        two.hitCount = 0;
                        two.action = 0;
                       } 
                       draw.repaint();
                  }

               sleep(100);
               if ((screen == 3|| screen ==12) && seconds > 0)
                  seconds -= 0.1;
               if (seconds == 1.3475054405631681E-12 )
               {
                  gameOver = true;
                  screen = 4;
                  seconds = 0;
               }
               if (screen == 4 && one.health <= 0)
               {
                  if(two.victoryCount + 1 < two.victory.length)
                        two.victoryCount++;
                  
                  if(one.dizzyCount + 1 < one.dizzy.length)
                        one.dizzyCount++;
                     else
                        one.dizzyCount = 0;
                  
                  if (one.fallCount + 1 < one.fall.length)
                     one.fallCount++;
                  
                  draw.repaint();
               }
               else if (screen == 4 && two.health <= 0)
               {
                  if(one.victoryCount + 1 < one.victory.length)
                        one.victoryCount++;
                    
                  if(two.dizzyCount + 1 < two.dizzy.length)
                        two.dizzyCount++;
                     else
                        two.dizzyCount = 0;
                        
                  if (two.fallCount + 1 < two.fall.length)
                     two.fallCount++;
                  
                  draw.repaint();
               }
               if (screen == 4 && (two.health < one.health))
               {
                  if(one.victoryCount + 1 < one.victory.length)
                        one.victoryCount++;
                  if(two.dizzyCount + 1 < two.dizzy.length)
                        two.dizzyCount++;
                     else
                        two.dizzyCount = 0;
                        
                  if (two.fallCount + 1 < two.fall.length)
                     two.fallCount++;
                  
                  draw.repaint();
               }
               if (screen == 4 && (one.health < two.health))
               {
                  if(two.victoryCount + 1 < two.victory.length)
                        two.victoryCount++;
                     else;
                  
                  if(one.dizzyCount + 1 < one.dizzy.length)
                        one.dizzyCount++;
                     else
                        one.dizzyCount = 0;
                  
                  if (one.fallCount + 1 < one.fall.length)
                     one.fallCount++;
                  else;
                  
                     draw.repaint();
               }

              
               }
               
               }
               catch (InterruptedException e)
               {
               }
         }
       }
   class MouseListen extends MouseAdapter
   {
      /*** mouseReleased ************************************
      * Purpose: Deal with the mouse being released         *
      * Parameters: e - details about the mouse event       *
      * Returns: none                                       *
      ******************************************************/
      public void mouseReleased(MouseEvent e)
      {
         x = e.getX();
         y = e.getY();
         draw.repaint();
      }
   }
   class MouseListen2 extends MouseMotionAdapter
   {
      /*** mouseMoved ***************************************
      * Purpose: Deal with the mouse being moved            *
      * Parameters: e - details about the mouse event       *
      * Returns: none                                       *
      ******************************************************/

      public void mouseMoved(MouseEvent e)
      {
         xx = e.getX();
         yy = e.getY();
         draw.repaint();
      }
   }

} 
