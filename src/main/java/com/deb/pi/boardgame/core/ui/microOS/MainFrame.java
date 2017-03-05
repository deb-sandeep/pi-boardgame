package com.deb.pi.boardgame.core.ui.microOS;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MainFrame implements KeyListener {

	JFrame       frame   = null;
	JPanel       panel   = null;
	JTextArea    area    = null;
	List<String> options = null;
	int          currOpn = 0;
	
	public MainFrame() {
		setUpUI();
	}
	
	private void setUpUI() {
		createArrayList();
		setUpFrame();
		setUpPanel();
		addPanelToFrame();
	}
	
	private void createArrayList() {
		options = new ArrayList<>();
		options.add( " 1 " );
		options.add( " 2 " );
		options.add( " 3 " );
		options.add( " 4 " );
		options.add( " 5 " );
	}

	public void show() {
		frame.setVisible( true );
	}

	private void setUpFrame() {
		frame = new JFrame();
		frame.setSize( 370, 175 );
		frame.setResizable( false );
		frame.setLocationRelativeTo( null );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private void setUpPanel() {
		panel = new JPanel();
		panel.setBackground( new Color( 220, 255, 120 ) );
		setUpTextArea();
		addTextAreaToPanel();
	}

	private void setUpTextArea() {
		area = new JTextArea( 4, 20 );
		area.setLineWrap( true );
		area.setWrapStyleWord( false );
		area.setFont( new Font( Font.MONOSPACED, Font.PLAIN, 30 ) );
		area.setBackground( new Color( 220, 255, 120 ) );
		area.setCaretColor( new Color( 220, 255, 120 ) );
		area.addKeyListener( this );
		
		area.setEditable( false );
		refreshTextArea();
	}
	
	private void refreshTextArea() {
		checkCurrOpnBounds();
		if( currOpn == 0 ) {
			area.setText( " Select difficulty:\n\n\n "
					+ "       " + options.get(currOpn) + ">" );			
		}
		else if( currOpn == options.size()-1 ){
			area.setText( " Select difficulty:\n\n\n "
					+ "      <" + options.get(currOpn) + "" );			
		}
		else {
			area.setText( " Select difficulty:\n\n\n "
					+ "      <" + options.get(currOpn) + ">" );						
		}
	}
	
	private void checkCurrOpnBounds() {
		if( currOpn < 0 ) {
			currOpn = 0;
		}
		else if( currOpn == options.size() ) {
			currOpn--;
		}
	}
		
	private void displayOKMessage() {
		area.setText( options.get( currOpn ) );
	}

	private void addTextAreaToPanel() {
		panel.add( area );
	}
	
	private void addPanelToFrame() {
		frame.setContentPane( panel );
	}
	
	public static void main( String[] args ) {
		MainFrame frame = new MainFrame();
		frame.show();
	}

	public void keyTyped( KeyEvent e ) {
	}

	public void keyPressed( KeyEvent e ) {
		char keyChar = e.getKeyChar();
		
		if( keyChar == 'f' ) {
			currOpn--;
			refreshTextArea();
		}
		else if( keyChar == 'h' ) {
			currOpn++;
			refreshTextArea();
		}
		else if( keyChar == 'g' ) {
			displayOKMessage();
		}
	}

	public void keyReleased( KeyEvent e ) {
	}

}
