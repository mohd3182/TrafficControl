package com.oneware.traffic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.db.TRFProgramDetail;

public class Controler {

	public Controler() {
		super();

	}

	static JButton signal1 = new JButton("1");
	static JButton signal2 = new JButton("2");
	static JButton signal3 = new JButton("3");
	static JButton signal4 = new JButton("4");
	static JFrame frame = new JFrame("Control Simulator");
	public static void main(String[] arg) {

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(10, 100, 10));
		JLabel emptyLabel = new JLabel("....");
		JButton signal1 = new JButton("1");
		JButton signal2 = new JButton("2");
		JButton signal3 = new JButton("3");
		JButton signal4 = new JButton("4");
		emptyLabel.setBackground(new Color(10, 100, 10));
		frame.getContentPane().add(signal1, BorderLayout.EAST);
		frame.getContentPane().add(signal2, BorderLayout.NORTH);
		frame.getContentPane().add(signal3, BorderLayout.SOUTH);
		frame.getContentPane().add(signal4, BorderLayout.WEST);
		frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		frame.setSize(new Dimension(800, 800));
		// 4. Size the frame.
		signal1.setBackground(new Color(250, 0, 0));
		signal4.setBackground(new Color(250, 0, 0));
		signal2.setBackground(new Color(0, 250, 0));
		signal3.setBackground(new Color(0, 250, 0));
		frame.pack();
		
		// 5. Show it.
		frame.setVisible(true);
		
		trafficInitiate();
		trafficProcess();
	}

	static Map programsMap = new HashMap();

	public static void trafficInitiate() {
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

	public static void trafficProcess() {

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
				currentSecond++;
				if (currentSecond == currentStepData.secDuration) {
					CurrentStep = currentStepData.nextStep;
					currentSecond = 0;
				}
			}
		},  1000,  1000);
	}

	public static void signalControl(int signalPort, String actionNeeded) {

		if (actionNeeded.equalsIgnoreCase("R")) {
			switch (signalPort) {
			case 1:
				signal1.setBackground(new Color(125));
				
				break;
			case 2:
				signal2.setBackground(new Color(125));
				
				break;
			case 3:
				signal3.setBackground(new Color(125));
				
				break;
			case 4:
				signal4.setBackground(new Color(125));
				
				break;
			default:
				break;
			}
		}

		
		
		if (actionNeeded.equalsIgnoreCase("G")) {
			switch (signalPort) {
			case 1:
				signal1.setBackground(new Color(211));
				
				break;
			case 2:
				signal2.setBackground(new Color(211));
				
				break;
			case 3:
				signal3.setBackground(new Color(211));
			
				break;
			case 4:
				signal4.setBackground(new Color(211));
				
				break;
			default:
				break;
			}
		}
		
		if (actionNeeded.equalsIgnoreCase("Y")) {
			switch (signalPort) {
			case 1:
				signal1.setBackground(new Color(246));
			
				break;
			case 2:
				signal2.setBackground(new Color(246));
				
				break;
			case 3:
				signal3.setBackground(new Color(246));
				
				break;
			case 4:
				signal4.setBackground(new Color(246));
				
				break;
			default:
				break;
			}
		}
		signal1.revalidate();
		signal1.repaint();
		signal1.doLayout();
		signal2.revalidate();
		signal2.repaint();
		signal2.doLayout();
		signal3.revalidate();
		signal3.repaint();
		signal3.doLayout();
		signal4.revalidate();
		signal4.repaint();
		signal4.doLayout();
		frame.repaint();
		frame.pack();
		frame.show();
		frame.invalidate();
		//frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
	}
}
