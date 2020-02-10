import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gamepanel extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 500, HEIGHT = 500;
	
	private Thread thread;
	
	private boolean running;
	
	private boolean right = true, left = false, up = false, down = false;
	
	private BodyPart b;
	private ArrayList<BodyPart> snake;
	
	private Apple apple;
	private ArrayList<Apple> apples;
	
	private Random r;
	
	private int xCoor = 10, yCoor = 10, size = 5;
	private int ticks = 0;
	public int score = 0;
	public String scoreString;
	 

	public Gamepanel() {
		setFocusable(true);

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		
		snake = new ArrayList<BodyPart>();
		apples = new ArrayList<Apple>();
		
		r = new Random();
		
		start();
	}

	public void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void tick() {
		 if(snake.size() == 0) {
			 b = new BodyPart(xCoor, yCoor, 10);
			 snake.add(b);
		 }
		 ticks++;
		 if(ticks > 500000) {
			 if(right) xCoor++;
			 if(left) xCoor--;
			 if(up) yCoor--;
			 if(down) yCoor++;
			 
			 ticks = 0;
			 
			 b = new BodyPart(xCoor, yCoor, 10);
			 snake.add(b);
			 
			 if(snake.size()> size){
				 snake.remove(0);
			 }			 
		 }
		 if(apples.size() == 0) {
			 int xCoor = r.nextInt(49);
			 int yCoor = r.nextInt(49);
			 
			 apple = new Apple(xCoor, yCoor, 10);
			 apples.add(apple);
		 }
		 
		 for(int i = 0; i < apples.size(); i++) {
			 if(xCoor == apples.get(i).getxCoor() && yCoor == apples.get(i).getyCoor()) {
				 size++;
				 apples.remove(i);
				 i++;
				 score++;
				 System.out.println("SCORE: " + score);
			 }
		 }
//		 //collision on snake body part
//		 for(int i = 0; i < snake.size(); i++) {
//			 if(xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
//				 if(i != snake.size() - 1) {
//					 System.out.println("Game Over");
//					 stop();
//				 }
//			 }
//		 }
//		 
//		 //collision on border
//		 if(xCoor < 0 || xCoor > 49 || yCoor < 0 || yCoor > 49) {
//			 System.out.println("Game Over");
//			 stop();
//		 }
		 for(int i = 0; i < snake.size(); i++) {
	            if(xCoor == snake.get(i).getxCoor() && yCoor == snake.get(i).getyCoor()) {
	                if(i != snake.size() - 1) {
	                    int answer = JOptionPane.showConfirmDialog(null, "Retry?");
	                    if (answer == 0) {
	                        restart();
	                    } else if(answer == 1) {
	                        System.exit(0);
	                    } else {
	                        stop();
	                    }
	                }
	            }
	        }

	        if (xCoor < 0 || xCoor > 49 || yCoor < 0 || yCoor > 49) {
	            int answer = JOptionPane.showConfirmDialog(null, "Retry?");
	            if (answer == 0) {
	                restart();
	            } else if(answer == 1) {
	                    System.exit(0);
	            } else {
	                stop();
	            }
	        }
	}



	public void paint(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(int i = 0; i < WIDTH/10 ; i++) {
			g.drawLine(i * 10, 0, i * 10, HEIGHT);
		}
		for(int i = 0; i < HEIGHT/10 ; i++) {
			g.drawLine(0,i * 10, HEIGHT, i * 10);
		}
		for(int i = 0; i < snake.size() ; i++) {
			snake.get(i).draw(g);
		}
		for(int i = 0; i < apples.size(); i++) {
			apples.get(i).draw(g);
		}
		scoreString = Integer.toString(score);
		g.setColor(Color.GREEN);
		Font stringFont = new Font("SansSerif", Font.BOLD, 20);
		g.setFont(stringFont);
		g.drawString(scoreString, 230, 30);
	}
	public void restart() {
        xCoor = 10;
        yCoor = 10;
        right = true;
        left = false;
        down = false;
        up = false;
        size = 5;
        snake.clear();
        apples = new ArrayList<Apple>();
        b = new BodyPart(xCoor, yCoor, 10);
        snake.add(b);
        apple = new Apple(xCoor, yCoor, 10);
        apples.add(apple);
        score = -1;
    }

	@Override
	public void run() {
		while(running) {
			tick();
			repaint();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_RIGHT && !left) {
			right = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_LEFT && !right) {
			left = true;
			up = false;
			down = false;
		}
		if(key == KeyEvent.VK_UP && !down) {
			up = true;
			left = false;			
			right = false;
		}
		if(key == KeyEvent.VK_DOWN && !up) {
			down = true;
			left = false;			
			right = false;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
