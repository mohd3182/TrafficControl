package com.oneware.traffic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.db.TRFProgramDetail;

public class SimulatorControl extends JPanel implements ActionListener { 

	

	
	 JButton signal1 = new JButton("1");
	 JButton signal2 = new JButton("2");
	 JButton signal3 = new JButton("3");
	 JButton signal4 = new JButton("4");
	 JLabel emptyLabel = new JLabel("....");
	 JFrame frame = new JFrame("Control Simulator");
	public static void main(String[] arg) {

		new SimulatorControl();
	}

	public SimulatorControl(){
		 JFrame frame = new JFrame("ButtonTest");
		 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		emptyLabel.setBackground(new Color(10, 100, 10));
		frame.getContentPane().add(signal1, BorderLayout.EAST);
		frame.getContentPane().add(signal2, BorderLayout.NORTH);
		frame.getContentPane().add(signal3, BorderLayout.SOUTH);
		frame.getContentPane().add(signal4, BorderLayout.WEST);

		BufferedImage myPicture;
		JLabel picLabel ;
		try {
			System.out.println("vvvvvs");
			myPicture = ImageIO.read(new File("c://traffic/green.jpg"));
			System.out.println("ooooo");
			 picLabel = new JLabel(new ImageIcon(myPicture));
			 picLabel.setBounds(100, 100, 100, 100);
			 frame.getContentPane().add(picLabel, BorderLayout.CENTER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		frame.setSize(new Dimension(800, 800));
		// 4. Size the frame.
		signal1.setBackground(new Color(250, 0, 0));
		signal4.setBackground(new Color(250, 0, 0));
		signal2.setBackground(new Color(0, 250, 0));
		signal3.setBackground(new Color(0, 250, 0));
		
		frame.show();
		// 5. Show it.
	//	this.setVisible(true);
		
		trafficInitiate();
		trafficProcess();
	}
	
	static Map programsMap = new HashMap();

	public  void trafficInitiate() {
		BuildItems buildItems = new BuildItems();
		ArrayList dataRetrived = buildItems
				.getData(new TRFProgramDetail(),
						"select * from TRF_PROGRAM_DETAIL where PROGRAM_ID=1 order by step_id");
		Iterator itr = dataRetrived.iterator();
		Map trafficData = new HashMap();
		while (itr.hasNext()) {
			TRFProgramDetail tRFProgramDetailData = (TRFProgramDetail) itr
					.next();
			trafficData.put(tRFProgramDetailData.getStepId(),
					tRFProgramDetailData);
		}
		programsMap.put(1, trafficData);
	}

	public  void trafficProcess() {

		Timer timer = new Timer();

		TRFProgramDetail tRFProgramDetail;
		timer.scheduleAtFixedRate(new TimerTask() {
			int CurrentStep = 1;
			int currentSecond = 0;
			int currentProgram;

			@Override
			public void run() {

				Map data = (Map) programsMap.get(1);

				TRFProgramDetail currentStepData = (TRFProgramDetail) data
						.get(CurrentStep);
				
				signalControl(1, currentStepData.group1Action);
				signalControl(2, currentStepData.group2Action);
				signalControl(3, currentStepData.group3Action);
				signalControl(4, currentStepData.group4Action);
				emptyLabel.setText("Count down:"+(currentStepData.secDuration-currentSecond));
				currentSecond++;
				if (currentSecond == currentStepData.secDuration) {
									
					CurrentStep = currentStepData.nextStep;
					
					currentSecond = 0;
				}
			}
		},  1000,  1000);
	}

	public  void signalControl(int signalPort, String actionNeeded) {

		if (actionNeeded.equalsIgnoreCase("R")) {
			switch (signalPort) {
			case 1:
				signal1.setBackground(new Color(255,0,0));
				signal1.setText("Red");
			
				break;
			case 2:
				signal2.setBackground(new Color(255,0,0));
				signal2.setText("Red");
				break;
			case 3:
				signal3.setBackground(new Color(255,0,0));
				signal3.setText("Red");
				break;
			case 4:
				signal4.setBackground(new Color(255,0,0));
				signal4.setText("Red");
				break;
			default:
				break;
			}
		}

		
		
		if (actionNeeded.equalsIgnoreCase("G")) {
			switch (signalPort) {
			case 1:
				signal1.setBackground(new Color(0,255,0));
				signal1.setText("Green");
			
		
				break;
			case 2:
				signal2.setBackground(new Color(0,255,0));
				signal2.setText("Green");
				break;
			case 3:
				signal3.setBackground(new Color(0,255,0));
				signal3.setText("Green");
				break;
			case 4:
				signal4.setBackground(new Color(0,255,0));
				signal4.setText("Green");
				break;
			default:
				break;
			}
		}
		
		if (actionNeeded.equalsIgnoreCase("Y")) {
			switch (signalPort) {
			case 1:
				signal1.setBackground(new Color(255,255,0));
				signal1.setText("Yellow");
			
	
				break;
			case 2:
				signal2.setBackground(new Color(255,255,0));
				signal2.setText("Yellow");

				break;
			case 3:
				signal3.setBackground(new Color(255,255,0));
				signal3.setText("Yellow");

				break;
			case 4:
				signal4.setBackground(new Color(255,255,0));
				signal4.setText("Yellow");

				break;
			default:
				break;
			}
		}

		revalidate();
		 repaint();
		frame.repaint();
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
