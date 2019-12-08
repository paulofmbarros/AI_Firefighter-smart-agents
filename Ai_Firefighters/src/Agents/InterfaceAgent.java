package Agents;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;

import Classes.FuelResource;
import Classes.WaterResource;
import Messages.ResourcesMessage;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.*;

public class InterfaceAgent extends Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<AID, Point> fireStationGUI, aircraftsGUI,dronesGUI,firetrucksGUI,firesGUI;
	ArrayList<WaterResource> waterResources = new ArrayList<WaterResource>();
	ArrayList<FuelResource> fuelResources = new ArrayList<FuelResource>();
	private Draw draw;
	private JFrame frame;

	@Override
	protected void setup() {
		
		fireStationGUI = new HashMap<>();
		aircraftsGUI = new HashMap<>();
		dronesGUI = new HashMap<>();
		firetrucksGUI = new HashMap<>();
		firesGUI = new HashMap<>();

		frame = new JFrame();
		draw = new Draw(fireStationGUI, aircraftsGUI,dronesGUI,firetrucksGUI,firesGUI);
		frame.add(draw);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(500, 500));
		
		DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("interface");
        sd.setName("Interface");
        
        dfd.addServices(sd);

        try {
            
            DFService.register(this, dfd);
            
           this.addBehaviour(new FetchResources());
           this.addBehaviour(new InfoFirestationState());
           this.addBehaviour(new InfoFireState());
           this.addBehaviour(new InfoFiretruckState());
           this.addBehaviour(new InfoAircraftState());
           this.addBehaviour(new InfoDroneState());
           this.addBehaviour(new FireExtinguished());
           
            
        } catch (FIPAException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	protected void takeDown(){
		super.takeDown();

		try {
			DFService.deregister(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class InfoFirestationState extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate mt2 = MessageTemplate.MatchOntology("info-firestation");
			MessageTemplate mt3 = MessageTemplate.and(mt1, mt2);

			ACLMessage msg = receive(mt3);

			if(msg != null) {
				AID firestation = msg.getSender();
				String[] content_split = msg.getContent().split("::");
				int x = Integer.parseInt(content_split[0]);
				int y = Integer.parseInt(content_split[1]);
				Point pos = new Point(x,y);
				fireStationGUI.put(firestation, pos);
			}else 
				block();
		}
	}
	
	class FireExtinguished extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate mt2 = MessageTemplate.MatchOntology("info-extinguishedFire");
			MessageTemplate mt3 = MessageTemplate.and(mt1, mt2);

			ACLMessage msg = receive(mt3);

			if(msg != null) {
				String[] content_split = msg.getContent().split("::");
				int x = Integer.parseInt(content_split[0]);
				int y = Integer.parseInt(content_split[1]);
				AID fire = null;
				for (HashMap.Entry<AID, Point> entry : firesGUI.entrySet()) {
					if((int) entry.getValue().getX() == x && (int) entry.getValue().getY() == y) {
						fire=entry.getKey();
					}
				}
				
				firesGUI.remove(fire);
			}else 
				block();
		}
	}
	
	class FetchResources extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate mt2 = MessageTemplate.MatchOntology("info-resources");
			MessageTemplate mt3 = MessageTemplate.and(mt1, mt2);

			ACLMessage msg = receive(mt3);
			if(msg!= null) {
			try {
				Object content = msg.getContentObject();
				switch (msg.getPerformative()) {
				case (ACLMessage.INFORM):
					if (content instanceof ResourcesMessage) {
						ResourcesMessage rm = (ResourcesMessage) content;
						waterResources = rm.getWaterResources();
						fuelResources = rm.getFuelResources();
						}
					break;
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}else 
				block();
			
	}
	}
	
	class InfoFiretruckState extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate mt2 = MessageTemplate.MatchOntology("info-firetruck");
			MessageTemplate mt3 = MessageTemplate.and(mt1, mt2);

			ACLMessage msg = receive(mt3);

			if(msg != null) {
				AID firetruck = msg.getSender();
				String[] content_split = msg.getContent().split("::");
				int x = Integer.parseInt(content_split[0]);
				int y = Integer.parseInt(content_split[1]);
				Point pos = new Point(x,y);
				firetrucksGUI.put(firetruck, pos);
			}else 
				block();
		}
	}
	
	class InfoAircraftState extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate mt2 = MessageTemplate.MatchOntology("info-aircraft");
			MessageTemplate mt3 = MessageTemplate.and(mt1, mt2);

			ACLMessage msg = receive(mt3);

			if(msg != null) {
				AID aircraft = msg.getSender();
				String[] content_split = msg.getContent().split("::");
				int x = Integer.parseInt(content_split[0]);
				int y = Integer.parseInt(content_split[1]);
				Point pos = new Point(x,y);
				aircraftsGUI.put(aircraft, pos);
			}else 
				block();
		}
	}
	
	class InfoDroneState extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate mt2 = MessageTemplate.MatchOntology("info-drone");
			MessageTemplate mt3 = MessageTemplate.and(mt1, mt2);

			ACLMessage msg = receive(mt3);

			if(msg != null) {
				AID drone = msg.getSender();
				String[] content_split = msg.getContent().split("::");
				int x = Integer.parseInt(content_split[0]);
				int y = Integer.parseInt(content_split[1]);
				Point pos = new Point(x,y);
				dronesGUI.put(drone, pos);
			}else 
				block();
		}
	}
	
	class InfoFireState extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void action() {
			MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			MessageTemplate mt2 = MessageTemplate.MatchOntology("info-fire");
			MessageTemplate mt3 = MessageTemplate.and(mt1, mt2);

			ACLMessage msg = receive(mt3);

			if(msg != null) {
				AID fire = msg.getSender();
				String[] content_split = msg.getContent().split("::");
				int x = Integer.parseInt(content_split[0]);
				int y = Integer.parseInt(content_split[1]);
				Point pos = new Point(x,y);
				firesGUI.put(fire, pos);
			}else 
				block();
		}
	}

class Draw extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int D_W = 600;
	private static final int D_H = 600;
	private HashMap<AID, Point> fireStation, aircrafts, drones, firetrucks, fires;

	public Draw(HashMap<AID, Point> fireStation, HashMap<AID, Point> aircrafts, HashMap<AID, Point> drones,HashMap<AID, Point> firetrucks, HashMap<AID, Point> fires) {
		this.fireStation = fireStation;
		this.aircrafts = aircrafts;
		this.drones = drones;
		this.firetrucks = firetrucks;
		this.fires = fires;

		/* JPanel Properties */
		ActionListener listener = new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		};

		javax.swing.Timer timer = new javax.swing.Timer(1000, listener);
		timer.start();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage background = null;

		try {
			background = ImageIO.read(new File("src/img/map.jpg"));
		} catch (IOException e) {
			System.out.println(e);
		}
		g.drawImage(background, 0, 0, 600, 600, null);


		for (HashMap.Entry<AID, Point> entry : fireStation.entrySet()) {
			int x = (int) entry.getValue().getX();
			int y = (int) entry.getValue().getY();
			BufferedImage img = null;

			try {
				img = ImageIO.read(new File("src/img/firestation.png"));
			} catch (IOException e) {
				System.out.println(e);
			}
			g.drawImage(img, x, y,89, 51, null);
		}
		
		for (HashMap.Entry<AID, Point> entry : aircrafts.entrySet()) {
			int x = (int) entry.getValue().getX();
			int y = (int) entry.getValue().getY();
			BufferedImage img = null;

			try {
				img = ImageIO.read(new File("src/img/aircraft.png"));
			} catch (IOException e) {
				System.out.println(e);
			}
			g.drawImage(img, x, y,92, 57, null);
		}
		
		for (HashMap.Entry<AID, Point> entry : firetrucks.entrySet()) {
			int x = (int) entry.getValue().getX();
			int y = (int) entry.getValue().getY();
			BufferedImage img = null;

			try {
				img = ImageIO.read(new File("src/img/firetruck.png"));
			} catch (IOException e) {
				System.out.println(e);
			}
			g.drawImage(img, x, y,50, 29, null);
		}
		
		for (HashMap.Entry<AID, Point> entry : drones.entrySet()) {
			int x = (int) entry.getValue().getX();
			int y = (int) entry.getValue().getY();
			BufferedImage img = null;

			try {
				img = ImageIO.read(new File("src/img/drone.png"));
			} catch (IOException e) {
				System.out.println(e);
			}
			g.drawImage(img, x, y,64, 38, null);
		}
		
		for (HashMap.Entry<AID, Point> entry : fires.entrySet()) {
			int x = (int) entry.getValue().getX();
			int y = (int) entry.getValue().getY();
			BufferedImage img = null;

			try {
				img = ImageIO.read(new File("src/img/fire.png"));
			} catch (IOException e) {
				System.out.println(e);
			}
			g.drawImage(img, x, y,30, 34, null);
		}
		for (WaterResource entry : waterResources) {
			int x = (int) entry.getWorldObject().getPosition().getX();
			int y = (int) entry.getWorldObject().getPosition().getY();
			BufferedImage img = null;

			try {
				img = ImageIO.read(new File("src/img/waterresource.png"));
			} catch (IOException e) {
				System.out.println(e);
			}
			g.drawImage(img, x, y,86, 51, null);
		}
		for (FuelResource entry : fuelResources) {
			int x = (int) entry.getWorldObject().getPosition().getX();
			int y = (int) entry.getWorldObject().getPosition().getY();
			BufferedImage img = null;

			try {
				img = ImageIO.read(new File("src/img/fuelresources.png"));
			} catch (IOException e) {
				System.out.println(e);
			}
			g.drawImage(img, x, y,97, 52, null);
		}

	}

	public Dimension getPreferredSize() {
		return new Dimension(D_W, D_H);
	}
}
}