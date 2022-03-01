
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
//import javax.swing.JLabel;
//import javax.swing.JTextField;

public class Hex implements ActionListener,HexGame,Cloneable {
	private JFrame frame;
	private JButton[][] board;
	private int size,gameType=2;
	private int currentPlayer,moveNumber=0;
	private char [][]control=new char[100][100];
	private final String user1="x";
	private final String user2="o";
	private final String empty=".";
	private int []rows;
	private int []columns;
	public Hex() {
		currentPlayer=0;
	}
	public void Play() {
		takeSize();
		gameType();
		createBoard();
	}
	public Object clone()throws CloneNotSupportedException{  
		Hex h1=(Hex)super.clone();
		h1.rows=new int[size*size];
		h1.columns=new int[size*size];
		for(int i=0;i<moveNumber;i++) {
			h1.rows[i]=rows[i];
			h1.columns[i]=columns[i];
		}
		h1.board=new JButton[size][size];
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				h1.board[i][j]=board[i][j];
			}
		}
		return h1;  
	   }
	public void takeSize() {
		String str;
		while(true) {
			str=JOptionPane.showInputDialog("Enter board Size ");
			int number=Integer.parseInt(str);
			if(number>5) {
				size=number;
				board= new JButton[size][size];
				break;
			}
			else {
				JOptionPane.showMessageDialog(null,"Invalıd number ");
			}
		}
	}
	public void gameType() {
		JRadioButton vsPlayer   = new JRadioButton("Player vs Player");
        JRadioButton vsComputer  = new JRadioButton("Player vs Computer");
        vsPlayer.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
				if(vsPlayer.isSelected()) {
					gameType=1;
				}
				
			}
	     });
        vsComputer.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
				if(vsComputer.isSelected()) {
					gameType=0;
				}
			}
	     });
	    JFrame gametype = new JFrame();
	    JButton btn=new JButton("Enter");
	    btn.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==btn) {
					if(gameType==2)
						JOptionPane.showMessageDialog(null,"Please select game type!");
					else {
						gametype.setVisible(false);
						frame.setVisible(true);
					}
				}
			}
	     });
	    gametype.setSize(300,300);
	    gametype.add(vsPlayer);
	    gametype.add(vsComputer);
	    gametype.add(btn);
	    gametype.setLayout(new GridLayout(3,1));
	    gametype.setVisible(true);
	            
	}
	public void reset() {
		for(int row=0;row<size;row++) {
			for(int col=0;col<size;col++) {
				board[row][col].setText(".");
				board[row][col].setBackground(null);
				moveNumber=0;
			}
		}
	}
	public void undo() {
		int a,b;
		if(moveNumber>0){
			moveNumber--;
			a=rows[moveNumber];
			b=columns[moveNumber];
			board[a][b].setText(empty);
			board[a][b].setBackground(null);
			a=rows[moveNumber-1];
			b=columns[moveNumber-1];
			board[a][b].setText(empty);
			board[a][b].setBackground(null);
			moveNumber--;
		}

		else
			JOptionPane.showMessageDialog(null,"You can't go back anymore!!!  ");
	}
	public void createBoard() {
		frame=new JFrame("Hex Game");
		rows=new int[size*size];
		columns=new int[size*size];
		int width=100,height=100,tempWidth;
		for(int row=0;row<size;row++) {
			tempWidth=width;
			for(int col=0;col<size;col++) {
				board[row][col]=new JButton(".");
				board[row][col].setBounds(width, height, 50, 50);
				board[row][col].addActionListener(this);
				frame.add(board[row][col]);
				width+=50;
			}
			width=tempWidth+25;
			height+=50;
		}
		JButton reset=new JButton("Reset");
		reset.setBounds(size*80, size*25, 100, 50);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==reset) {
					reset();
				}	
			}
		});
		JButton undo=new JButton("Undo");
		undo.setBounds(size*80+100, size*25, 100, 50);
		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==undo) {
					undo();
				}	
			}
		});
		JButton save=new JButton("SAVE");
		save.setBounds(size*80, size*25+50, 100, 50);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==save) {
					try {
						saveGame();
					}
					catch(IOException e1) {
						JOptionPane.showMessageDialog(null,"File is not opened");
					}
				}	
			}
		});
		JButton load=new JButton("LOAD");
		load.setBounds(size*80+100, size*25+50, 100, 50);
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==load) {
					try {
						loadGame();
					}
					catch(IOException e1) {
						JOptionPane.showMessageDialog(null,"File is not opened");
					}
				}	
			}
		});
		frame.add(load);
		frame.add(save);
		frame.add(reset);
		frame.add(undo);
		frame.setSize(size*120,size*120);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void saveGame() throws IOException {
		String str;
		str=JOptionPane.showInputDialog("Enter name of text ");
		File file = new File(str);
		if(file.exists()) {
			file.createNewFile();
		}
		FileWriter fWriter=new FileWriter(file);
		BufferedWriter bWriter = new BufferedWriter(fWriter);
		bWriter.write(size+"\n");
		bWriter.write(gameType+"\n");
		bWriter.write(currentPlayer+"\n");
		for(int row=0;row<size;row++) {
			for(int col=0;col<size;col++) {
				bWriter.write(board[row][col].getText());
			}
			bWriter.write("\n");
		}
		bWriter.write(moveNumber+"\n");
		for(int i=0;i<moveNumber;i++) {
			bWriter.write(rows[i]+"\n");
			bWriter.write(columns[i]+"\n");
		}
		bWriter.close();
		
	}
	public void loadGame() throws IOException {
		String str,line;
		int row=0,t=0;
		str=JOptionPane.showInputDialog("Enter name of text ");
		File file = new File(str);
		if(file.exists()) {
			file.createNewFile();
		}
		FileReader fReader=new FileReader(file);
		BufferedReader bReader = new BufferedReader(fReader);
		line = bReader.readLine();
		size=Integer.parseInt(line);
		frame.dispose();
		createBoard();
		frame.setVisible(true);
		line = bReader.readLine();
		gameType=Integer.parseInt(line);
		line = bReader.readLine();
		currentPlayer=Integer.parseInt(line);
		
		while(row<size) {
			line = bReader.readLine();
			for(int col=0;col<size;col++) {
				if(line.charAt(col)=='x')
					board[row][col].setBackground(Color.yellow);
				
				else if(line.charAt(col)=='o')
					board[row][col].setBackground(Color.green);
				
				board[row][col].setText(""+line.charAt(col));
			}
			row++;
		}
		line = bReader.readLine();
		moveNumber=Integer.parseInt(line);
		while((line = bReader.readLine())!=null) {
			rows[t]= Integer.parseInt(line);
			line = bReader.readLine();
			columns[t]= Integer.parseInt(line);
			t++;
		}
		bReader.close();
	}
	public void keepMoves(int row,int col) {
		rows[moveNumber]=row;
		columns[moveNumber]=col;
		moveNumber++;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		for(int row=0;row<size;row++) {
			for(int col=0;col<size;col++) {
				if(e.getSource()==board[row][col]) {
					if(!board[row][col].getText().equals(empty))
						JOptionPane.showMessageDialog(null,"Play empty place.");
					else {
						if(gameType==1) {
							if(currentPlayer==0) {
								board[row][col].setText(user1);
								board[row][col].setBackground(Color.yellow);
								checkEndGame();
								currentPlayer=1;
								keepMoves(row,col);
							}
							else {
								board[row][col].setText(user2);
								board[row][col].setBackground(Color.green);
								checkEndGame();
								currentPlayer=0;
								keepMoves(row,col);
							}
						}
						else {
							if(currentPlayer==0) {
								board[row][col].setText(user1);
								board[row][col].setBackground(Color.yellow);
								keepMoves(row,col);
								if(!checkEndGame()) {
									currentPlayer=1;
									playRandom();
									checkEndGame();
									currentPlayer=0;
								}
							}
						}
					}
				}
			}
		}
	}
	public void playRandom() {
		Random randnum=new Random();
		int compRow,compCol;
		while(true) {
			compRow=randnum.nextInt(size-2);
			compCol=randnum.nextInt(size-2);
			if(board[compRow][compCol].getText().equals(empty)) {
				board[compRow][compCol].setText(user2);
				board[compRow][compCol].setBackground(Color.green);
				keepMoves(compRow,compCol);
				break;
			}		
		}
	}
	public boolean checkEndGame(){
		  boolean check=false;
		  clear(control);
		  if(currentPlayer==0){
		    for(int row=0;row<size;row++){
		      if(board[row][0].getText().equals(user1)){
		        check=checkrec(row,0,user1);
		        if(check==true){                 //if X wins, make uppercase the letters.                        
		          doUppercase(row,0,user1);
		          JOptionPane.showMessageDialog(null,"Congratulations! Player 1 winner!!!");
		          reset();
		          break;
		        }
		      }     
		    }
		  }
		  if(currentPlayer==1){
		    for(int column=0;column<size;column++){
		      if(board[0][column].getText().equals(user2)){
		        check=checkrec(0,column,user2);
		        if(check==true){                 //if O wins, make uppercase the letters.
		          doUppercase(0,column,user2);
		          JOptionPane.showMessageDialog(null,"Congratulations! Player 2 winner!!!");
		          reset();
		          break;  
		        }
		      }
		    }
		  } 
		  return check;
	}
	public boolean checkrec(int row,int column, String ch){

	  	if(row<0||column<0)
	  		return false;
	  	if(row>=size||column>=size)
	  		return false;
	  	if(!board[row][column].getText().equals(ch))
	   		return false;
	  	
	  	if(control[row][column]!='1')
	  		control[row][column]='1';		//control[row][column]='1'; //If the point is checked, it makes the position of that point '1' in the array.
	  	
	  	else                        //If it is already checked don't check it again. So return 0
	   		return false;
	  	
	    if(column==size-1&&ch==user1)    //For 'x' (the winning side if it reaches the right and left walls), if it reaches the last column, it wins.So return 1.
	    	return true;
	    

	    if(row==size-1&&ch==user2)   //For Player2 (the winning side if it reaches the top and bottom walls), if it reaches the last row, it wins.So return 1.
	        return true;
	    
	   
	    /*It controls recursively in 6 directions.*/
	    if(checkrec(row,column+1,ch))
	      return true;

	    if(checkrec(row,column-1,ch))
	      return true;

	    if(checkrec(row+1,column,ch))
	      return true;

	    if(checkrec(row+1,column-1,ch))
	      return true;

	    if(checkrec(row-1,column,ch))
	      return true;

	    if(checkrec(row-1,column+1,ch))
	      return true;

	    return false;
	}
	public void doUppercase(int row,int column,String ch){//if there is a winner, this function make uppercase the letters
			 //by subtracting 32(ASCII) from the winner's letters recursively.

		if(row<0||column<0)
			return;
		if(row==size||column==size)
			return;
		if(!board[row][column].getText().equals(ch))
			return;
		if(board[row][column].getText().equals(ch)) {
			if(ch==user1)
				board[row][column].setText("X");
			else
				board[row][column].setText("O");
		}
		
		if(column==size-1&&ch==user1)
			return;
		
		if(row==size-1&&ch==user2)
			return;
		
		doUppercase(row,column+1,ch);
		
		
		doUppercase(row,column-1,ch);
		
		
		doUppercase(row+1,column,ch);
		
		
		doUppercase(row+1,column-1,ch);
		
		
		doUppercase(row-1,column,ch);
		
		
		doUppercase(row-1,column+1,ch);


		return;
}
	public void clear(char control[][]){
		for(int i=0;i<100;i++){
			for(int j=0;j<100;j++){
				control[i][j]='\0';
			}
		}
	}
}
